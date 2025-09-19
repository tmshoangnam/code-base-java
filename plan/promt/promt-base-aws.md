You are a Senior Software Engineer + System Architect (or an AI coding agent acting like one).
Your task: design and implement a production-ready, enterprise-grade AWS services foundation split into two modules:
	• java-base-aws — pure Java library (jar) that defines AWS service contracts, SPI, and utilities (no Spring, no AWS SDK).
	• java-base-starter-aws — Spring Boot starter that provides concrete AWS SDK implementations and auto-configuration for S3, SQS, SNS, Lambda, DynamoDB, Secrets Manager, and demonstrates full runtime wiring (including ServiceLoader fallback).

📦 java-base-aws Design Plan

🎯 Overall Objectives
	• Spring Boot Starter Ecosystem
		○ Cung cấp auto-configuration + starter module.
		○ Sẵn sàng plug-and-play trong Spring Boot apps.
	• Contract-First Design
		○ java-base-aws định nghĩa contracts & SPI (không phụ thuộc Spring).
		○ java-base-starter-aws mới chứa implementation (S3, SQS, SNS, Lambda, DynamoDB, Secrets Manager).
	• Scalability & Maintainability
		○ Dễ mở rộng (custom AWS services, new providers).
		○ Backward-compatible API.
		○ Đảm bảo dùng dài hạn trong các enterprise projects.

📂 Module Structure

java-base-parent
├── java-base-bom (pom)
├── java-base-core (jar)
├── java-base-cache (jar)
├── java-base-observability (jar)
├── java-base-security (jar)
├── java-base-file (jar)
├── java-base-aws (jar)                <-- abstraction + contracts
└── java-base-starter-aws (jar)        <-- Spring Boot auto-config & impl

📦 java-base-aws (jar, framework-agnostic)
Top-level package: io.github.base.aws

io.github.base.aws
 ├─ api
 │   ├─ s3
 │   │   ├─ S3Operations.java          // S3 operations contract
 │   │   ├─ S3Metadata.java            // S3 metadata model
 │   │   ├─ S3UploadResult.java        // S3 upload result
 │   │   └─ S3DownloadResult.java      // S3 download result
 │   ├─ sqs
 │   │   ├─ SqsOperations.java         // SQS operations contract
 │   │   ├─ SqsMessage.java            // SQS message model
 │   │   └─ SqsQueueInfo.java          // SQS queue info
 │   ├─ sns
 │   │   ├─ SnsOperations.java         // SNS operations contract
 │   │   ├─ SnsTopicInfo.java          // SNS topic info
 │   │   └─ SnsSubscriptionInfo.java   // SNS subscription info
 │   ├─ lambda
 │   │   ├─ LambdaOperations.java      // Lambda operations contract
 │   │   ├─ LambdaFunctionInfo.java    // Lambda function info
 │   │   └─ LambdaInvokeResult.java    // Lambda invoke result
 │   ├─ dynamodb
 │   │   ├─ DynamoDbOperations.java    // DynamoDB operations contract
 │   │   ├─ DynamoTableInfo.java       // DynamoDB table info
 │   │   └─ DynamoQueryConfig.java     // DynamoDB query config
 │   ├─ secrets
 │   │   ├─ SecretsManagerOperations.java // Secrets Manager contract
 │   │   └─ SecretsManagerSecretInfo.java // Secret info model
 │   ├─ common
 │   │   ├─ AwsCredentials.java        // AWS credentials model
 │   │   ├─ AwsRegion.java             // AWS region model
 │   │   ├─ AwsClientConfig.java       // AWS client config
 │   │   └─ AwsException.java          // Base AWS exception
 │   └─ config
 │       └─ AwsServiceConfig.java      // AWS service config contract
 ├─ spi
 │   └─ AwsServiceProvider.java        // SPI, loadable via ServiceLoader
 ├─ internal
 │   └─ …                              // helper classes (non-public)
 └─ AwsServices.java                   // ServiceLoader helper

✅ Characteristics:
	• Pure Java, không Spring, không AWS SDK.
	• Định nghĩa chuẩn về S3Operations, SqsOperations, SnsOperations, etc.
	• SPI để load provider (S3, SQS, SNS, Lambda, DynamoDB, Secrets Manager) bằng ServiceLoader.
	• Support multiple AWS services với unified interface.

📦 java-base-starter-aws (Spring Boot starter)
Top-level package: io.github.base.starter.aws

io.github.base.starter.aws
 ├─ autoconfig
 │   ├─ AwsAutoConfiguration.java
 │   ├─ AwsProperties.java
 │   ├─ S3AutoConfiguration.java
 │   ├─ SqsAutoConfiguration.java
 │   ├─ SnsAutoConfiguration.java
 │   ├─ LambdaAutoConfiguration.java
 │   ├─ DynamoDbAutoConfiguration.java
 │   └─ SecretsManagerAutoConfiguration.java
 ├─ impl
 │   ├─ s3
 │   │   ├─ S3OperationsImpl.java
 │   │   ├─ S3ServiceProvider.java
 │   │   └─ S3ClientFactory.java
 │   ├─ sqs
 │   │   ├─ SqsOperationsImpl.java
 │   │   ├─ SqsServiceProvider.java
 │   │   └─ SqsClientFactory.java
 │   ├─ sns
 │   │   ├─ SnsOperationsImpl.java
 │   │   ├─ SnsServiceProvider.java
 │   │   └─ SnsClientFactory.java
 │   ├─ lambda
 │   │   ├─ LambdaOperationsImpl.java
 │   │   ├─ LambdaServiceProvider.java
 │   │   └─ LambdaClientFactory.java
 │   ├─ dynamodb
 │   │   ├─ DynamoDbOperationsImpl.java
 │   │   ├─ DynamoDbServiceProvider.java
 │   │   └─ DynamoDbClientFactory.java
 │   └─ secrets
 │       ├─ SecretsManagerOperationsImpl.java
 │       ├─ SecretsManagerServiceProvider.java
 │       └─ SecretsManagerClientFactory.java
 ├─ decorators
 │   ├─ ResilientAwsOperations.java    // Resilience patterns
 │   ├─ LoggingAwsOperations.java      // Structured logging
 │   ├─ CachedAwsOperations.java       // Caching support
 │   └─ MetricsAwsOperations.java      // Observability
 └─ resources
     └─ META-INF/services/io.github.base.aws.spi.AwsServiceProvider

✅ Characteristics:
	• Auto-config cho từng AWS service.
	• Hỗ trợ chọn provider qua application.yml:

base.aws.credentials.access-key-id: ${AWS_ACCESS_KEY_ID}
base.aws.credentials.secret-access-key: ${AWS_SECRET_ACCESS_KEY}
base.aws.region: us-east-1

base.aws.s3.default-bucket: my-bucket
base.aws.sqs.default-queue-url: https://sqs.us-east-1.amazonaws.com/123456789012/my-queue
base.aws.sns.default-topic-arn: arn:aws:sns:us-east-1:123456789012:my-topic
base.aws.lambda.default-function-name: my-function
base.aws.dynamodb.default-table-name: my-table
base.aws.secrets.default-secret-name: my-secret
	• Tích hợp với java-base-core (resilience, logging).
	• Tích hợp với java-base-cache (operation caching).
	• Tích hợp với java-base-security (credentials management).
	• Tích hợp với java-base-observability (metrics, tracing).

🧩 Key Components

1. AWS Service Contracts
	• S3Operations: file operations (upload, download, delete, list, presigned URLs).
	• SqsOperations: message operations (send, receive, delete, queue management).
	• SnsOperations: notification operations (publish, subscribe, topic management).
	• LambdaOperations: function operations (invoke, create, update, delete).
	• DynamoDbOperations: database operations (put, get, query, scan, table management).
	• SecretsManagerOperations: secret operations (create, get, update, delete).

2. AWS Models
	• AwsCredentials: AWS credentials (access key, secret key, session token).
	• AwsRegion: AWS region information.
	• AwsClientConfig: AWS client configuration.
	• Service-specific models: S3Metadata, SqsMessage, SnsTopicInfo, etc.

3. AWS Service Provider SPI
	• AwsServiceProvider: loadable via ServiceLoader.
	• Trả về implementation cho từng AWS service.
	• Support cho S3, SQS, SNS, Lambda, DynamoDB, Secrets Manager.

4. Integration Layer
	• Decorator pattern cho resilience, logging, caching, metrics.
	• Tích hợp với existing base modules.
	• Spring Boot auto-configuration.

📖 Example Usage

Core (framework-agnostic):

AwsServiceProvider provider = AwsServices.findProvider("s3");
S3Operations s3Ops = provider.createS3Operations(awsConfig);

S3Metadata metadata = S3Metadata.builder()
    .contentType("application/pdf")
    .userMetadata(Map.of("owner", "user123"))
    .build();
    
S3UploadResult result = s3Ops.uploadFile("my-bucket", "document.pdf", content, metadata);

if (result.success()) {
    System.out.println("File uploaded: " + result.location());
}

Spring Boot App:

base.aws.region: us-east-1
base.aws.s3.default-bucket: my-bucket
base.aws.sqs.default-queue-url: https://sqs.us-east-1.amazonaws.com/123456789012/my-queue

@Service
class DocumentService {
    private final S3Operations s3Operations;
    private final SqsOperations sqsOperations;
    
    DocumentService(S3Operations s3Operations, SqsOperations sqsOperations) {
        this.s3Operations = s3Operations;
        this.sqsOperations = sqsOperations;
    }
    
    @PostMapping("/upload")
    String uploadDocument(@RequestParam MultipartFile file) {
        S3Metadata metadata = S3Metadata.builder()
            .contentType(file.getContentType())
            .userMetadata(Map.of("uploadedBy", getCurrentUser()))
            .build();
            
        S3UploadResult result = s3Operations.uploadFile(
            "my-bucket", 
            generateFileId(), 
            file.getBytes(), 
            metadata
        );
        
        if (result.success()) {
            // Send notification via SQS
            sqsOperations.sendMessage("document-uploaded-queue", Map.of(
                "bucket", "my-bucket",
                "key", result.key(),
                "etag", result.etag()
            ));
            return "Upload successful: " + result.location();
        }
        
        return "Upload failed: " + result.errorMessage();
    }
}

🧪 Testing & Quality
	• Unit tests (java-base-aws): contracts, SPI loading, exceptions.
	• Integration tests (java-base-starter-aws): S3, SQS, SNS, Lambda, DynamoDB, Secrets Manager.
	• Testcontainers với LocalStack cho AWS services testing.
	• Code coverage ≥ 80% (Jacoco).
	• Backward compatibility check (Japicmp / Revapi).

✅ Standards
	• Java 21+
	• Javadoc đầy đủ cho public API.
	• Static utils: final class + private constructor.
	• Không phụ thuộc Spring trong core.
	• Package spi chỉ để load provider.
	• Follows SOLID, KISS, DRY.

🚀 Deliverables
	• java-base-aws: abstraction + SPI.
	• java-base-starter-aws: Spring Boot auto-config + S3/SQS/SNS/Lambda/DynamoDB/Secrets Manager providers.
	• README.md: mô tả design, usage, extensibility.
	• Sample demo app dùng multiple AWS services.
