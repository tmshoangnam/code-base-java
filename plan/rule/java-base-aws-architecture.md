# Java Base AWS - Architecture Design

## Tổng quan kiến trúc

### Module Structure
```
java-base-aws (Core AWS Contracts)
├── api/
│   ├── s3/           # S3 operations contracts
│   ├── sqs/          # SQS operations contracts  
│   ├── sns/          # SNS operations contracts
│   ├── lambda/       # Lambda operations contracts
│   ├── dynamodb/     # DynamoDB operations contracts
│   ├── secrets/      # Secrets Manager contracts
│   ├── common/       # Common AWS utilities, exceptions
│   └── config/       # AWS configuration contracts
├── internal/         # Non-public APIs
└── AwsServices.java  # ServiceLoader support

java-base-starter-aws (Spring Boot Integration)
├── config/           # AutoConfiguration cho từng service
├── impl/             # AWS SDK implementations
│   ├── s3/           # S3 implementation
│   ├── sqs/          # SQS implementation
│   ├── sns/          # SNS implementation
│   ├── lambda/       # Lambda implementation
│   ├── dynamodb/     # DynamoDB implementation
│   └── secrets/      # Secrets Manager implementation
└── decorators/       # Resilience, Logging, Metrics decorators
```

## 1. Core AWS Contracts

### 1.1 S3 Operations
```java
package io.github.base.aws.api.s3;

public interface S3Operations {
    // File operations (tích hợp với java-base-file)
    S3UploadResult uploadFile(String bucket, String key, Object content, S3Metadata metadata);
    S3DownloadResult<Object> downloadFile(String bucket, String key, Class<?> targetType);
    boolean deleteFile(String bucket, String key);
    boolean fileExists(String bucket, String key);
    List<S3ObjectSummary> listFiles(String bucket, String prefix, int maxResults);
    
    // Bucket operations
    boolean bucketExists(String bucket);
    S3BucketInfo createBucket(String bucket, String region);
    boolean deleteBucket(String bucket);
    List<S3BucketInfo> listBuckets();
    
    // Presigned URL operations
    String generatePresignedUploadUrl(String bucket, String key, Duration expiration);
    String generatePresignedDownloadUrl(String bucket, String key, Duration expiration);
    
    // Multipart upload operations
    S3MultipartUpload initiateMultipartUpload(String bucket, String key, S3Metadata metadata);
    S3MultipartUploadPart uploadPart(String bucket, String key, String uploadId, int partNumber, Object content);
    S3MultipartUploadResult completeMultipartUpload(String bucket, String key, String uploadId, List<S3MultipartUploadPart> parts);
    void abortMultipartUpload(String bucket, String key, String uploadId);
}
```

### 1.2 SQS Operations
```java
package io.github.base.aws.api.sqs;

public interface SqsOperations {
    // Queue operations
    SqsQueueInfo createQueue(String queueName, Map<String, String> attributes);
    boolean queueExists(String queueUrl);
    boolean deleteQueue(String queueUrl);
    List<SqsQueueInfo> listQueues(String prefix);
    
    // Message operations
    SqsSendResult sendMessage(String queueUrl, Object message, Map<String, String> attributes);
    SqsSendResult sendMessageBatch(String queueUrl, List<SqsMessage> messages);
    List<SqsMessage> receiveMessages(String queueUrl, int maxMessages, Duration visibilityTimeout);
    boolean deleteMessage(String queueUrl, String receiptHandle);
    boolean deleteMessageBatch(String queueUrl, List<String> receiptHandles);
    
    // Queue attributes
    Map<String, String> getQueueAttributes(String queueUrl, List<String> attributeNames);
    void setQueueAttributes(String queueUrl, Map<String, String> attributes);
}
```

### 1.3 SNS Operations
```java
package io.github.base.aws.api.sns;

public interface SnsOperations {
    // Topic operations
    SnsTopicInfo createTopic(String topicName, Map<String, String> attributes);
    boolean topicExists(String topicArn);
    boolean deleteTopic(String topicArn);
    List<SnsTopicInfo> listTopics();
    
    // Subscription operations
    SnsSubscriptionInfo subscribe(String topicArn, String protocol, String endpoint, Map<String, String> attributes);
    boolean unsubscribe(String subscriptionArn);
    List<SnsSubscriptionInfo> listSubscriptions(String topicArn);
    
    // Publishing operations
    SnsPublishResult publish(String topicArn, Object message, Map<String, String> attributes);
    SnsPublishResult publishBatch(String topicArn, List<SnsMessage> messages);
}
```

### 1.4 Lambda Operations
```java
package io.github.base.aws.api.lambda;

public interface LambdaOperations {
    // Function operations
    LambdaFunctionInfo createFunction(String functionName, LambdaFunctionConfig config);
    boolean functionExists(String functionName);
    boolean deleteFunction(String functionName);
    List<LambdaFunctionInfo> listFunctions();
    
    // Invocation operations
    LambdaInvokeResult invoke(String functionName, Object payload, LambdaInvokeConfig config);
    LambdaInvokeResult invokeAsync(String functionName, Object payload, Map<String, String> attributes);
    
    // Function management
    LambdaFunctionInfo updateFunctionCode(String functionName, LambdaCodeConfig codeConfig);
    LambdaFunctionInfo updateFunctionConfiguration(String functionName, LambdaConfig config);
    LambdaFunctionInfo getFunction(String functionName);
}
```

### 1.5 DynamoDB Operations
```java
package io.github.base.aws.api.dynamodb;

public interface DynamoDbOperations {
    // Table operations
    DynamoTableInfo createTable(String tableName, DynamoTableConfig config);
    boolean tableExists(String tableName);
    boolean deleteTable(String tableName);
    List<DynamoTableInfo> listTables();
    
    // Item operations
    DynamoPutResult putItem(String tableName, Map<String, Object> item, Map<String, Object> conditions);
    DynamoGetResult getItem(String tableName, Map<String, Object> key);
    DynamoUpdateResult updateItem(String tableName, Map<String, Object> key, Map<String, Object> updates, Map<String, Object> conditions);
    DynamoDeleteResult deleteItem(String tableName, Map<String, Object> key, Map<String, Object> conditions);
    
    // Query operations
    List<Map<String, Object>> query(String tableName, DynamoQueryConfig config);
    List<Map<String, Object>> scan(String tableName, DynamoScanConfig config);
}
```

### 1.6 Secrets Manager Operations
```java
package io.github.base.aws.api.secrets;

public interface SecretsManagerOperations {
    // Secret operations
    String createSecret(String secretName, String secretValue, String description);
    boolean secretExists(String secretName);
    boolean deleteSecret(String secretName);
    List<String> listSecrets();
    
    // Secret value operations
    String getSecretValue(String secretName);
    String getSecretValue(String secretName, String versionId);
    String updateSecretValue(String secretName, String secretValue);
    
    // Secret metadata
    SecretsManagerSecretInfo getSecretInfo(String secretName);
    void updateSecretDescription(String secretName, String description);
}
```

## 2. Common AWS Models

### 2.1 Base Models
```java
package io.github.base.aws.api.common;

public record AwsCredentials(
    String accessKeyId,
    String secretAccessKey,
    String sessionToken
) {}

public record AwsRegion(String regionName) {
    public static AwsRegion of(String regionName) {
        return new AwsRegion(regionName);
    }
}

public record AwsClientConfig(
    AwsCredentials credentials,
    AwsRegion region,
    Duration connectionTimeout,
    Duration readTimeout,
    int maxConnections
) {}

public abstract class AwsException extends RuntimeException {
    private final String serviceName;
    private final String operation;
    private final String requestId;
    
    // Common exception handling
}
```

### 2.2 S3 Models
```java
package io.github.base.aws.api.s3;

public record S3Metadata(
    String contentType,
    Map<String, String> userMetadata,
    Map<String, String> systemMetadata
) {}

public record S3UploadResult(
    String bucket,
    String key,
    String etag,
    String versionId,
    boolean success,
    String errorMessage
) {}

public record S3DownloadResult<T>(
    T content,
    S3Metadata metadata,
    String etag,
    String versionId,
    boolean success,
    String errorMessage
) {}

public record S3ObjectSummary(
    String bucket,
    String key,
    long size,
    Instant lastModified,
    String etag,
    String storageClass
) {}
```

## 3. AWS Configuration

### 3.1 Configuration Properties
```java
package io.github.base.aws.api.config;

@ConfigurationProperties(prefix = "base.aws")
public class AwsProperties {
    
    private AwsCredentials credentials;
    private AwsRegion region;
    private Map<String, AwsServiceConfig> services = new HashMap<>();
    
    // S3 specific config
    private S3Config s3 = new S3Config();
    
    // SQS specific config  
    private SqsConfig sqs = new SqsConfig();
    
    // SNS specific config
    private SnsConfig sns = new SnsConfig();
    
    // Lambda specific config
    private LambdaConfig lambda = new LambdaConfig();
    
    // DynamoDB specific config
    private DynamoDbConfig dynamodb = new DynamoDbConfig();
    
    // Secrets Manager specific config
    private SecretsManagerConfig secrets = new SecretsManagerConfig();
}

public class S3Config {
    private String defaultBucket;
    private boolean multipartUploadEnabled = true;
    private long multipartThreshold = 64 * 1024 * 1024; // 64MB
    private int maxConnections = 50;
}

public class SqsConfig {
    private String defaultQueueUrl;
    private Duration defaultVisibilityTimeout = Duration.ofSeconds(30);
    private int defaultMaxMessages = 10;
}

public class SnsConfig {
    private String defaultTopicArn;
    private Duration defaultMessageTimeout = Duration.ofSeconds(30);
}
```

## 4. Integration với Existing Modules

### 4.1 Tích hợp với java-base-file
```java
package io.github.base.aws.api.s3;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.model.FileMetadata;

public class S3FileStorageAdapter implements FileStorage {
    
    private final S3Operations s3Operations;
    private final String defaultBucket;
    
    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        S3Metadata s3Metadata = convertToS3Metadata(metadata);
        S3UploadResult result = s3Operations.uploadFile(defaultBucket, fileId, content, s3Metadata);
        
        return UploadResult.success(
            fileId, 
            "s3://" + defaultBucket + "/" + fileId, 
            convertToFileMetadata(result, metadata)
        );
    }
    
    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> targetType) {
        S3DownloadResult<Object> result = s3Operations.downloadFile(defaultBucket, fileId, targetType);
        
        if (result.success()) {
            return DownloadResult.success(
                (T) result.content(),
                convertToFileMetadata(result)
            );
        } else {
            return DownloadResult.failure(result.errorMessage());
        }
    }
    
    // ... other methods
}
```

### 4.2 Tích hợp với java-base-core
```java
package io.github.base.aws.api.resilience;

import io.github.base.core.api.resilience.ResilienceFactories;
import io.github.base.aws.api.s3.S3Operations;

public class ResilientS3Operations implements S3Operations {
    
    private final S3Operations delegate;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;
    private final TimeLimiter timeLimiter;
    
    public ResilientS3Operations(S3Operations delegate) {
        this.delegate = delegate;
        this.circuitBreaker = ResilienceFactories.defaultCircuitBreakerRegistry()
            .circuitBreaker("aws-s3");
        this.retry = ResilienceFactories.defaultRetryRegistry()
            .retry("aws-s3");
        this.timeLimiter = ResilienceFactories.defaultTimeLimiterRegistry()
            .timeLimiter("aws-s3");
    }
    
    @Override
    public S3UploadResult uploadFile(String bucket, String key, Object content, S3Metadata metadata) {
        return CircuitBreaker.decorateSupplier(circuitBreaker, () ->
            Retry.decorateSupplier(retry, () ->
                TimeLimiter.decorateSupplier(timeLimiter, () ->
                    delegate.uploadFile(bucket, key, content, metadata)
                ).get()
            ).get()
        ).get();
    }
    
    // ... other methods with similar decoration
}
```

## 5. Spring Boot Integration

### 5.1 AutoConfiguration
```java
package io.github.base.aws.starter.config;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
@ConditionalOnClass(AwsOperations.class)
public class AwsAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public AwsClientConfig awsClientConfig(AwsProperties properties) {
        return AwsClientConfig.builder()
            .credentials(properties.getCredentials())
            .region(properties.getRegion())
            .build();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public S3Operations s3Operations(AwsClientConfig clientConfig, AwsProperties properties) {
        S3Client s3Client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    clientConfig.credentials().accessKeyId(),
                    clientConfig.credentials().secretAccessKey()
                )
            ))
            .region(Region.of(clientConfig.region().regionName()))
            .build();
            
        S3Operations operations = new S3OperationsImpl(s3Client);
        
        // Apply decorators
        if (properties.isResilienceEnabled()) {
            operations = new ResilientS3Operations(operations);
        }
        
        if (properties.isLoggingEnabled()) {
            operations = new LoggingS3Operations(operations);
        }
        
        return operations;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public SqsOperations sqsOperations(AwsClientConfig clientConfig, AwsProperties properties) {
        // Similar implementation for SQS
    }
    
    // ... other AWS services
}
```

### 5.2 Configuration Example
```yaml
base:
  aws:
    credentials:
      access-key-id: ${AWS_ACCESS_KEY_ID}
      secret-access-key: ${AWS_SECRET_ACCESS_KEY}
    region: us-east-1
    
    s3:
      default-bucket: my-app-bucket
      multipart-upload-enabled: true
      multipart-threshold: 64MB
      
    sqs:
      default-queue-url: https://sqs.us-east-1.amazonaws.com/123456789012/my-queue
      default-visibility-timeout: 30s
      
    sns:
      default-topic-arn: arn:aws:sns:us-east-1:123456789012:my-topic
      
    lambda:
      default-function-name: my-function
      
    dynamodb:
      default-table-name: my-table
      
    secrets:
      default-secret-name: my-secret
```

## 6. Usage Examples

### 6.1 S3 Operations
```java
@Service
public class DocumentService {
    
    private final S3Operations s3Operations;
    private final SqsOperations sqsOperations;
    
    public void uploadDocument(String bucket, String key, byte[] content) {
        S3Metadata metadata = S3Metadata.builder()
            .contentType("application/pdf")
            .userMetadata(Map.of("owner", "user123"))
            .build();
            
        S3UploadResult result = s3Operations.uploadFile(bucket, key, content, metadata);
        
        if (result.success()) {
            // Send notification via SQS
            sqsOperations.sendMessage("document-uploaded-queue", Map.of(
                "bucket", bucket,
                "key", key,
                "etag", result.etag()
            ));
        }
    }
}
```

### 6.2 SQS Message Processing
```java
@Service
public class MessageProcessor {
    
    private final SqsOperations sqsOperations;
    private final S3Operations s3Operations;
    
    @Scheduled(fixedDelay = 5000)
    public void processMessages() {
        List<SqsMessage> messages = sqsOperations.receiveMessages(
            "processing-queue", 
            10, 
            Duration.ofSeconds(30)
        );
        
        for (SqsMessage message : messages) {
            try {
                processMessage(message);
                sqsOperations.deleteMessage("processing-queue", message.receiptHandle());
            } catch (Exception e) {
                // Message will be retried
                log.error("Failed to process message", e);
            }
        }
    }
}
```

## 7. Testing Strategy

### 7.1 LocalStack Integration
```java
@SpringBootTest
@Testcontainers
class S3OperationsIntegrationTest {
    
    @Container
    static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
        .withServices(LocalStackContainer.Service.S3);
    
    @Test
    void shouldUploadAndDownloadFile() {
        // Test with LocalStack
    }
}
```

### 7.2 Moto Integration
```java
@ExtendWith(MockitoExtension.class)
class S3OperationsTest {
    
    @Mock
    private S3Client s3Client;
    
    @Test
    void shouldHandleUploadFailure() {
        // Test error scenarios
    }
}
```

## 8. Dependencies

### 8.1 Core Dependencies
```xml
<dependencies>
    <!-- AWS SDK v2 -->
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>s3</artifactId>
    </dependency>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>sqs</artifactId>
    </dependency>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>sns</artifactId>
    </dependency>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>lambda</artifactId>
    </dependency>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>dynamodb</artifactId>
    </dependency>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>secretsmanager</artifactId>
    </dependency>
    
    <!-- Integration with existing modules -->
    <dependency>
        <groupId>io.github.tmshoangnam</groupId>
        <artifactId>java-base-core</artifactId>
    </dependency>
    <dependency>
        <groupId>io.github.tmshoangnam</groupId>
        <artifactId>java-base-file</artifactId>
    </dependency>
</dependencies>
```

### 8.2 Test Dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>localstack</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.github.tomakehurst</groupId>
        <artifactId>wiremock-jre8</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Với kiến trúc này, `java-base-aws` sẽ cung cấp một foundation layer mạnh mẽ cho AWS services, tích hợp tốt với các module hiện có và tuân thủ nguyên tắc contract-first design.
