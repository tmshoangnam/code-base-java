# Java Base AWS - Implementation Plan

## 🎯 Tổng quan triển khai

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

## 📋 Implementation Timeline

### Phase 1: Core AWS Contracts (Tuần 1-2)
**Mục tiêu**: Tạo foundation layer với contracts và models cơ bản

#### Tuần 1: Base Contracts & Models
- [ ] **Day 1-2**: Common AWS models
  - `AwsCredentials`, `AwsRegion`, `AwsClientConfig`
  - `AwsException` hierarchy
  - `AwsServiceConfig` base classes

- [ ] **Day 3-4**: S3 contracts
  - `S3Operations` interface
  - `S3Metadata`, `S3UploadResult`, `S3DownloadResult` models
  - `S3MultipartUpload` models

- [ ] **Day 5**: SQS contracts
  - `SqsOperations` interface
  - `SqsMessage`, `SqsQueueInfo` models
  - `SqsSendResult` models

#### Tuần 2: Additional Service Contracts
- [ ] **Day 1-2**: SNS contracts
  - `SnsOperations` interface
  - `SnsTopicInfo`, `SnsSubscriptionInfo` models
  - `SnsPublishResult` models

- [ ] **Day 3-4**: Lambda & DynamoDB contracts
  - `LambdaOperations` interface
  - `DynamoDbOperations` interface
  - Related models

- [ ] **Day 5**: Secrets Manager contracts
  - `SecretsManagerOperations` interface
  - `SecretsManagerSecretInfo` models

### Phase 2: S3 Implementation (Tuần 3-4)
**Mục tiêu**: Implement S3 operations với full feature set

#### Tuần 3: S3 Core Implementation
- [ ] **Day 1-2**: Basic S3 operations
  - `S3OperationsImpl` class
  - Upload, download, delete, exists operations
  - Error handling và exception mapping

- [ ] **Day 3-4**: Advanced S3 features
  - Multipart upload implementation
  - Presigned URL generation
  - Bucket operations (create, delete, list)

- [ ] **Day 5**: S3 Testing
  - Unit tests với Mockito
  - Integration tests với LocalStack
  - Performance testing

#### Tuần 4: S3 Integration & Optimization
- [ ] **Day 1-2**: S3 với java-base-file integration
  - `S3FileStorageAdapter` implementation
  - Metadata conversion utilities
  - File type support

- [ ] **Day 3-4**: S3 Resilience & Observability
  - `ResilientS3Operations` decorator
  - `LoggingS3Operations` decorator
  - `MetricsS3Operations` decorator

- [ ] **Day 5**: S3 Documentation
  - Usage examples
  - Configuration guide
  - Troubleshooting guide

### Phase 3: Messaging Services (Tuần 5-6)
**Mục tiêu**: Implement SQS và SNS operations

#### Tuần 5: SQS Implementation
- [ ] **Day 1-2**: SQS Core Implementation
  - `SqsOperationsImpl` class
  - Queue operations (create, delete, list)
  - Message operations (send, receive, delete)

- [ ] **Day 3-4**: SQS Advanced Features
  - Batch operations
  - Dead letter queue support
  - Queue attributes management

- [ ] **Day 5**: SQS Testing & Integration
  - Unit tests và integration tests
  - Spring Boot integration
  - Message processing examples

#### Tuần 6: SNS Implementation
- [ ] **Day 1-2**: SNS Core Implementation
  - `SnsOperationsImpl` class
  - Topic operations (create, delete, list)
  - Subscription operations

- [ ] **Day 3-4**: SNS Advanced Features
  - Batch publishing
  - Message attributes
  - Platform application support

- [ ] **Day 5**: SNS Testing & Integration
  - Unit tests và integration tests
  - Spring Boot integration
  - Notification examples

### Phase 4: Compute & Database Services (Tuần 7-8)
**Mục tiêu**: Implement Lambda và DynamoDB operations

#### Tuần 7: Lambda Implementation
- [ ] **Day 1-2**: Lambda Core Implementation
  - `LambdaOperationsImpl` class
  - Function operations (create, delete, list)
  - Invocation operations (sync, async)

- [ ] **Day 3-4**: Lambda Advanced Features
  - Function configuration management
  - Code deployment support
  - Event source mapping

- [ ] **Day 5**: Lambda Testing & Integration
  - Unit tests và integration tests
  - Spring Boot integration
  - Function examples

#### Tuần 8: DynamoDB Implementation
- [ ] **Day 1-2**: DynamoDB Core Implementation
  - `DynamoDbOperationsImpl` class
  - Table operations (create, delete, list)
  - Item operations (put, get, update, delete)

- [ ] **Day 3-4**: DynamoDB Advanced Features
  - Query và scan operations
  - Batch operations
  - Conditional operations

- [ ] **Day 5**: DynamoDB Testing & Integration
  - Unit tests và integration tests
  - Spring Boot integration
  - Data modeling examples

### Phase 5: Security & Configuration (Tuần 9-10)
**Mục tiêu**: Implement Secrets Manager và configuration management

#### Tuần 9: Secrets Manager Implementation
- [ ] **Day 1-2**: Secrets Manager Core
  - `SecretsManagerOperationsImpl` class
  - Secret operations (create, delete, list)
  - Secret value operations

- [ ] **Day 3-4**: Secrets Manager Advanced
  - Secret rotation support
  - Cross-region replication
  - Secret versioning

- [ ] **Day 5**: Secrets Manager Testing
  - Unit tests và integration tests
  - Spring Boot integration
  - Security examples

#### Tuần 10: Configuration Management
- [ ] **Day 1-2**: AWS Configuration
  - `AwsProperties` configuration classes
  - Service-specific configuration
  - Environment-based configuration

- [ ] **Day 3-4**: Spring Boot AutoConfiguration
  - `AwsAutoConfiguration` classes
  - Conditional bean creation
  - Configuration validation

- [ ] **Day 5**: Configuration Testing
  - Configuration tests
  - Profile-based testing
  - Validation testing

### Phase 6: Integration & Testing (Tuần 11-12)
**Mục tiêu**: Comprehensive integration và testing

#### Tuần 11: Module Integration
- [ ] **Day 1-2**: java-base-core Integration
  - Resilience patterns integration
  - Structured logging integration
  - Validation integration

- [ ] **Day 3-4**: java-base-file Integration
  - S3FileStorageAdapter implementation
  - Metadata conversion utilities
  - File type support

- [ ] **Day 5**: java-base-cache Integration
  - AWS operations caching
  - Metadata caching
  - Performance optimization

#### Tuần 12: Comprehensive Testing
- [ ] **Day 1-2**: Integration Testing
  - End-to-end tests với multiple services
  - Cross-service integration tests
  - Performance testing

- [ ] **Day 3-4**: Security Testing
  - Credentials management testing
  - Access control testing
  - Security vulnerability scanning

- [ ] **Day 5**: Documentation & Examples
  - Complete API documentation
  - Usage examples cho từng service
  - Migration guide

### Phase 7: Release Preparation (Tuần 13-14)
**Mục tiêu**: Chuẩn bị release

#### Tuần 13: Release Preparation
- [ ] **Day 1-2**: Version Management
  - Semantic versioning setup
  - CHANGELOG.md creation
  - Release notes preparation

- [ ] **Day 3-4**: Artifact Preparation
  - Maven Central publishing setup
  - GPG signing configuration
  - Repository configuration

- [ ] **Day 5**: Final Testing
  - Release candidate testing
  - Performance benchmarking
  - Security audit

#### Tuần 14: Release & Documentation
- [ ] **Day 1-2**: Release Execution
  - Maven Central publishing
  - GitHub release creation
  - Announcement preparation

- [ ] **Day 3-4**: Documentation Finalization
  - README.md updates
  - Migration guide completion
  - Troubleshooting guide completion

- [ ] **Day 5**: Post-Release
  - Community feedback collection
  - Issue tracking setup
  - Support documentation

## 🔧 Technical Implementation Details

### 1. AWS SDK v2 Integration

#### 1.1 Client Configuration
```java
package io.github.base.aws.api.config;

public class AwsClientFactory {
    
    public static S3Client createS3Client(AwsClientConfig config) {
        return S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    config.credentials().accessKeyId(),
                    config.credentials().secretAccessKey()
                )
            ))
            .region(Region.of(config.region().regionName()))
            .httpClientBuilder(UrlConnectionHttpClient.builder()
                .connectionTimeout(config.connectionTimeout())
                .socketTimeout(config.readTimeout())
            )
            .build();
    }
    
    public static SqsClient createSqsClient(AwsClientConfig config) {
        return SqsClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    config.credentials().accessKeyId(),
                    config.credentials().secretAccessKey()
                )
            ))
            .region(Region.of(config.region().regionName()))
            .build();
    }
    
    // ... other clients
}
```

#### 1.2 Exception Mapping
```java
package io.github.base.aws.api.common;

public class AwsExceptionMapper {
    
    public static AwsException mapS3Exception(S3Exception ex) {
        return switch (ex.statusCode()) {
            case 404 -> new AwsNotFoundException("S3", "object", ex.getMessage(), ex);
            case 403 -> new AwsAccessDeniedException("S3", "object", ex.getMessage(), ex);
            case 400 -> new AwsBadRequestException("S3", "object", ex.getMessage(), ex);
            default -> new AwsServiceException("S3", "object", ex.getMessage(), ex);
        };
    }
    
    public static AwsException mapSqsException(SqsException ex) {
        return switch (ex.statusCode()) {
            case 404 -> new AwsNotFoundException("SQS", "queue", ex.getMessage(), ex);
            case 403 -> new AwsAccessDeniedException("SQS", "queue", ex.getMessage(), ex);
            default -> new AwsServiceException("SQS", "queue", ex.getMessage(), ex);
        };
    }
    
    // ... other exception mappings
}
```

### 2. Resilience Integration

#### 2.1 Circuit Breaker Configuration
```java
package io.github.base.aws.api.resilience;

public class AwsResilienceConfig {
    
    public static CircuitBreaker createS3CircuitBreaker() {
        return CircuitBreaker.ofDefaults("aws-s3")
            .toBuilder()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofSeconds(30))
            .slidingWindowSize(10)
            .minimumNumberOfCalls(5)
            .build();
    }
    
    public static Retry createS3Retry() {
        return Retry.ofDefaults("aws-s3")
            .toBuilder()
            .maxAttempts(3)
            .waitDuration(Duration.ofSeconds(1))
            .retryOnException(throwable -> 
                throwable instanceof AwsServiceException ||
                throwable instanceof AwsTimeoutException
            )
            .build();
    }
    
    // ... other resilience configurations
}
```

### 3. Observability Integration

#### 3.1 Metrics Configuration
```java
package io.github.base.aws.api.observability;

public class AwsMetricsConfig {
    
    public static void registerS3Metrics(MeterRegistry meterRegistry) {
        Counter.builder("aws.s3.operations.total")
            .description("Total number of S3 operations")
            .tag("service", "s3")
            .register(meterRegistry);
            
        Timer.builder("aws.s3.operations.duration")
            .description("S3 operation duration")
            .tag("service", "s3")
            .register(meterRegistry);
            
        Gauge.builder("aws.s3.connections.active")
            .description("Active S3 connections")
            .tag("service", "s3")
            .register(meterRegistry, s3Client, client -> getActiveConnections(client));
    }
    
    // ... other metrics configurations
}
```

#### 3.2 Tracing Configuration
```java
package io.github.base.aws.api.observability;

public class AwsTracingConfig {
    
    public static void configureS3Tracing(S3Client s3Client, Tracer tracer) {
        s3Client = TracingS3Client.builder()
            .s3Client(s3Client)
            .tracer(tracer)
            .build();
    }
    
    public static void configureSqsTracing(SqsClient sqsClient, Tracer tracer) {
        sqsClient = TracingSqsClient.builder()
            .sqsClient(sqsClient)
            .tracer(tracer)
            .build();
    }
    
    // ... other tracing configurations
}
```

### 4. Testing Strategy

#### 4.1 LocalStack Integration
```java
package io.github.base.aws.test;

@SpringBootTest
@Testcontainers
public class AwsIntegrationTest {
    
    @Container
    static LocalStackContainer localStack = new LocalStackContainer(
        DockerImageName.parse("localstack/localstack:latest")
    )
    .withServices(
        LocalStackContainer.Service.S3,
        LocalStackContainer.Service.SQS,
        LocalStackContainer.Service.SNS,
        LocalStackContainer.Service.LAMBDA,
        LocalStackContainer.Service.DYNAMODB,
        LocalStackContainer.Service.SECRETSMANAGER
    );
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("base.aws.region", () -> localStack.getRegion());
        registry.add("base.aws.credentials.access-key-id", () -> localStack.getAccessKey());
        registry.add("base.aws.credentials.secret-access-key", () -> localStack.getSecretKey());
    }
    
    @Test
    void shouldUploadAndDownloadFromS3() {
        // Test S3 operations
    }
    
    @Test
    void shouldSendAndReceiveSqsMessages() {
        // Test SQS operations
    }
    
    // ... other integration tests
}
```

#### 4.2 Unit Testing
```java
package io.github.base.aws.test;

@ExtendWith(MockitoExtension.class)
class S3OperationsTest {
    
    @Mock
    private S3Client s3Client;
    
    @Mock
    private PutObjectResponse putObjectResponse;
    
    @InjectMocks
    private S3OperationsImpl s3Operations;
    
    @Test
    void shouldUploadFileSuccessfully() {
        // Given
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
            .thenReturn(putObjectResponse);
        when(putObjectResponse.eTag()).thenReturn("etag123");
        
        // When
        S3UploadResult result = s3Operations.uploadFile("bucket", "key", "content", metadata);
        
        // Then
        assertThat(result.success()).isTrue();
        assertThat(result.etag()).isEqualTo("etag123");
    }
    
    @Test
    void shouldHandleUploadFailure() {
        // Given
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
            .thenThrow(S3Exception.builder()
                .statusCode(403)
                .message("Access Denied")
                .build());
        
        // When
        S3UploadResult result = s3Operations.uploadFile("bucket", "key", "content", metadata);
        
        // Then
        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).contains("Access Denied");
    }
}
```

## 📦 Dependencies

### Core Dependencies
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

### Test Dependencies
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
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## ✅ Success Criteria

### Functional Requirements
- [ ] Support cho 6 AWS services: S3, SQS, SNS, Lambda, DynamoDB, Secrets Manager
- [ ] Integration với java-base-file cho S3 storage
- [ ] Integration với tất cả existing modules
- [ ] Spring Boot auto-configuration
- [ ] Comprehensive error handling

### Non-Functional Requirements
- [ ] Test coverage ≥ 80%
- [ ] Performance: < 200ms cho most operations
- [ ] Memory usage: < 50MB overhead
- [ ] Thread safety: Support concurrent access
- [ ] Documentation: Complete API docs + usage guides

### Quality Requirements
- [ ] SonarQube quality gate pass
- [ ] No critical security vulnerabilities
- [ ] Backward compatibility maintained
- [ ] Performance regression testing passed

Với kế hoạch này, `java-base-aws` sẽ cung cấp một foundation layer mạnh mẽ cho AWS services, tích hợp tốt với các module hiện có và tuân thủ nguyên tắc contract-first design.
