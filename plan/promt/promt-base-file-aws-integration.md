You are a Senior Software Engineer + System Architect (or an AI coding agent acting like one).
Your task: design and implement a production-ready, enterprise-grade file storage and AWS services foundation with three modules:
	• java-base-file — pure Java library (jar) that defines file storage contracts, SPI, and utilities (no Spring, no cloud SDKs).
	• java-base-aws — pure Java library (jar) that defines AWS service contracts, SPI, and utilities (no Spring, no AWS SDK).
	• java-base-starter-file — Spring Boot starter that provides concrete storage provider implementations and auto-configuration for Local, AWS S3, Google Cloud, Azure Blob, and demonstrates full runtime wiring (including ServiceLoader fallback).

📦 Integration Design Plan

🎯 Overall Objectives
	• Modular Architecture
		○ java-base-file: Pure file storage contracts (framework-agnostic).
		○ java-base-aws: Pure AWS service contracts (framework-agnostic).
		○ java-base-starter-file: Spring Boot integration + all storage providers.
	• Contract-First Design
		○ Mỗi module định nghĩa contracts & SPI riêng biệt.
		○ java-base-starter-file tích hợp cả file storage và AWS services.
	• Scalability & Maintainability
		○ Dễ mở rộng (custom storage providers, new AWS services).
		○ Backward-compatible API.
		○ Đảm bảo dùng dài hạn trong các enterprise projects.

📂 Module Structure

java-base-parent
├── java-base-bom (pom)
├── java-base-core (jar)
├── java-base-cache (jar)
├── java-base-observability (jar)
├── java-base-security (jar)
├── java-base-file (jar)              <-- file storage contracts
├── java-base-aws (jar)               <-- AWS service contracts
└── java-base-starter-file (jar)      <-- Spring Boot integration + all providers

📦 java-base-file (jar, framework-agnostic)
Top-level package: io.github.base.file

io.github.base.file
 ├─ api
 │   ├─ storage
 │   │   ├─ FileStorage.java           // Core file storage contract
 │   │   ├─ FileStorageManager.java    // Multi-storage management
 │   │   └─ FileStorageProvider.java   // SPI for storage providers
 │   ├─ model
 │   │   ├─ FileType.java              // FILE, BYTE, STREAM enum
 │   │   ├─ FileMetadata.java          // File metadata record
 │   │   ├─ UploadResult.java          // Upload operation result
 │   │   └─ DownloadResult.java        // Download operation result
 │   ├─ exception
 │   │   ├─ FileStorageException.java  // Base exception
 │   │   ├─ FileNotFoundException.java
 │   │   └─ FileUploadException.java
 │   └─ util
 │       ├─ FileUtils.java             // File utilities
 │       └─ FileMetadataValidator.java // Validation utilities
 ├─ spi
 │   └─ FileStorageProvider.java       // SPI, loadable via ServiceLoader
 ├─ internal
 │   └─ …                              // helper classes (non-public)
 └─ FileServices.java                  // ServiceLoader helper

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

📦 java-base-starter-file (Spring Boot starter)
Top-level package: io.github.base.starter.file

io.github.base.starter.file
 ├─ autoconfig
 │   ├─ FileStorageAutoConfiguration.java
 │   ├─ FileStorageProperties.java
 │   ├─ AwsAutoConfiguration.java
 │   └─ AwsProperties.java
 ├─ impl
 │   ├─ local
 │   │   ├─ LocalFileStorageProvider.java
 │   │   ├─ LocalFileStorage.java
 │   │   └─ LocalFileStorageManager.java
 │   ├─ s3
 │   │   ├─ S3FileStorageProvider.java
 │   │   ├─ S3FileStorage.java
 │   │   ├─ S3FileStorageManager.java
 │   │   └─ S3FileStorageAdapter.java   // Bridge to java-base-aws
 │   ├─ gcs
 │   │   ├─ GcsFileStorageProvider.java
 │   │   ├─ GcsFileStorage.java
 │   │   └─ GcsFileStorageManager.java
 │   └─ azure
 │       ├─ AzureFileStorageProvider.java
 │       ├─ AzureFileStorage.java
 │       └─ AzureFileStorageManager.java
 ├─ aws
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
 │   ├─ ResilientFileStorage.java      // Resilience patterns
 │   ├─ LoggingFileStorage.java        // Structured logging
 │   ├─ CachedFileStorage.java         // Caching support
 │   ├─ SecureFileStorage.java         // Security integration
 │   └─ MetricsFileStorage.java        // Observability
 └─ resources
     └─ META-INF/services/io.github.base.file.spi.FileStorageProvider

✅ Characteristics:
	• Auto-config cho FileStorage, FileStorageManager, và AWS services.
	• Hỗ trợ chọn provider qua application.yml:

base.file.storage.default-provider: s3
base.file.storage.providers:
  local:
    base-path: /tmp/files
  s3:
    bucket-name: my-bucket
    region: us-east-1
  gcs:
    bucket-name: my-gcs-bucket
    project-id: my-project
  azure:
    container-name: my-container
    connection-string: ${AZURE_STORAGE_CONNECTION_STRING}

base.aws.credentials.access-key-id: ${AWS_ACCESS_KEY_ID}
base.aws.credentials.secret-access-key: ${AWS_SECRET_ACCESS_KEY}
base.aws.region: us-east-1
base.aws.s3.default-bucket: my-bucket
base.aws.sqs.default-queue-url: https://sqs.us-east-1.amazonaws.com/123456789012/my-queue
base.aws.sns.default-topic-arn: arn:aws:sns:us-east-1:123456789012:my-topic
	• Tích hợp với java-base-core (resilience, logging).
	• Tích hợp với java-base-cache (metadata caching).
	• Tích hợp với java-base-security (access control, encryption).
	• Tích hợp với java-base-observability (metrics, tracing).

🧩 Key Components

1. File Storage Contract
	• FileStorage: core interface cho file operations (upload, download, delete, exists, list).
	• FileStorageManager: quản lý multiple storage providers.
	• Support cho 3 file types: FILE (java.io.File), BYTE (byte[]), STREAM (InputStream/OutputStream).

2. AWS Service Contracts
	• S3Operations: file operations (upload, download, delete, list, presigned URLs).
	• SqsOperations: message operations (send, receive, delete, queue management).
	• SnsOperations: notification operations (publish, subscribe, topic management).
	• LambdaOperations: function operations (invoke, create, update, delete).
	• DynamoDbOperations: database operations (put, get, query, scan, table management).
	• SecretsManagerOperations: secret operations (create, get, update, delete).

3. Integration Layer
	• S3FileStorageAdapter: bridge giữa FileStorage và S3Operations.
	• Decorator pattern cho resilience, logging, caching, security, metrics.
	• Spring Boot auto-configuration cho tất cả services.

📖 Example Usage

Spring Boot App:

base.file.storage.default-provider: s3
base.file.storage.providers.s3.bucket-name: my-bucket
base.aws.region: us-east-1
base.aws.s3.default-bucket: my-bucket
base.aws.sqs.default-queue-url: https://sqs.us-east-1.amazonaws.com/123456789012/my-queue

@Service
class DocumentService {
    private final FileStorage fileStorage;
    private final SqsOperations sqsOperations;
    
    DocumentService(FileStorage fileStorage, SqsOperations sqsOperations) {
        this.fileStorage = fileStorage;
        this.sqsOperations = sqsOperations;
    }
    
    @PostMapping("/upload")
    String uploadDocument(@RequestParam MultipartFile file) {
        FileMetadata metadata = FileMetadata.of(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize()
        );
        
        UploadResult result = fileStorage.upload(
            generateFileId(), 
            file.getBytes(), 
            metadata
        );
        
        if (result.success()) {
            // Send notification via SQS
            sqsOperations.sendMessage("document-uploaded-queue", Map.of(
                "fileId", result.fileId(),
                "location", result.location(),
                "size", metadata.size()
            ));
            return "Upload successful: " + result.location();
        }
        
        return "Upload failed: " + result.errorMessage();
    }
    
    @GetMapping("/download/{fileId}")
    ResponseEntity<byte[]> downloadDocument(@PathVariable String fileId) {
        DownloadResult<byte[]> result = fileStorage.download(fileId, byte[].class);
        
        if (result.success()) {
            return ResponseEntity.ok()
                .header("Content-Type", result.metadata().contentType())
                .header("Content-Disposition", "attachment; filename=\"" + result.metadata().fileName() + "\"")
                .body(result.content());
        }
        
        return ResponseEntity.notFound().build();
    }
}

🧪 Testing & Quality
	• Unit tests (java-base-file, java-base-aws): contracts, SPI loading, exceptions.
	• Integration tests (java-base-starter-file): Local, S3, GCS, Azure providers + AWS services.
	• Testcontainers với LocalStack cho AWS services testing.
	• Code coverage ≥ 80% (Jacoco).
	• Backward compatibility check (Japicmp / Revapi).

✅ Standards
	• Java 21+
	• Javadoc đầy đủ cho public API.
	• Static utils: final class + private constructor.
	• Không phụ thuộc Spring trong core modules.
	• Package spi chỉ để load provider.
	• Follows SOLID, KISS, DRY.

🚀 Deliverables
	• java-base-file: file storage abstraction + SPI.
	• java-base-aws: AWS services abstraction + SPI.
	• java-base-starter-file: Spring Boot integration + all storage providers + AWS services.
	• README.md: mô tả design, usage, extensibility.
	• Sample demo app dùng multiple storage providers và AWS services.
