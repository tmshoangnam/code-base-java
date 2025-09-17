# Java Base Starter File

A Spring Boot starter for file storage and AWS services integration.

## Overview

This starter provides a comprehensive file storage solution with support for multiple storage providers and AWS services integration. It includes auto-configuration, decorators for resilience and observability, and a clean API for file operations.

## Features

- **Multiple Storage Providers**: Local, S3, GCS, Azure Blob Storage
- **AWS Services Integration**: S3, SQS, SNS, Lambda, DynamoDB, Secrets Manager
- **Resilience Patterns**: Circuit breaker, retry, time limiter
- **Observability**: Metrics, logging, tracing
- **Security**: Access control, audit logging, validation
- **Caching**: Metadata and existence caching
- **Auto-configuration**: Spring Boot auto-configuration

## Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-starter-file</artifactId>
    <version>1.0.5-SNAPSHOT</version>
</dependency>
```

### 2. Configure Storage

```yaml
base:
  file:
    storage:
      enabled: true
      default-provider: s3
      providers:
        local:
          type: local
          properties:
            base-path: /tmp/files
        s3:
          type: s3
          properties:
            bucket-name: my-bucket
            region: us-east-1
        gcs:
          type: gcs
          properties:
            bucket-name: my-gcs-bucket
            project-id: my-project
        azure:
          type: azure
          properties:
            container-name: my-container
            connection-string: ${AZURE_STORAGE_CONNECTION_STRING}
  aws:
    enabled: true
    region: us-east-1
    credentials:
      access-key-id: ${AWS_ACCESS_KEY_ID}
      secret-access-key: ${AWS_SECRET_ACCESS_KEY}
    s3:
      default-bucket: my-bucket
    sqs:
      default-queue-url: https://sqs.us-east-1.amazonaws.com/123456789012/my-queue
    sns:
      default-topic-arn: arn:aws:sns:us-east-1:123456789012:my-topic
```

### 3. Use in Your Code

```java
@Service
public class DocumentService {
    
    private final FileStorage fileStorage;
    private final SqsOperations sqsOperations;
    
    public DocumentService(FileStorage fileStorage, SqsOperations sqsOperations) {
        this.fileStorage = fileStorage;
        this.sqsOperations = sqsOperations;
    }
    
    @PostMapping("/upload")
    public String uploadDocument(@RequestParam MultipartFile file) {
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
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String fileId) {
        DownloadResult<byte[]> result = fileStorage.download(fileId, byte[].class);
        
        if (result.success()) {
            return ResponseEntity.ok()
                .header("Content-Type", result.metadata().contentType())
                .header("Content-Disposition", "attachment; filename=\"" + result.metadata().fileName() + "\"")
                .body(result.content());
        }
        
        return ResponseEntity.notFound().build();
    }
    
    private String generateFileId() {
        return UUID.randomUUID().toString();
    }
}
```

## Configuration

### File Storage Configuration

| Property | Description | Default |
|----------|-------------|---------|
| `base.file.storage.enabled` | Enable file storage auto-configuration | `true` |
| `base.file.storage.default-provider` | Default storage provider | `local` |
| `base.file.storage.cache-enabled` | Enable metadata caching | `true` |
| `base.file.storage.cache-ttl-seconds` | Cache TTL in seconds | `300` |
| `base.file.storage.max-cache-size` | Maximum cache size | `1000` |
| `base.file.storage.metrics-enabled` | Enable metrics collection | `true` |

### AWS Configuration

| Property | Description | Default |
|----------|-------------|---------|
| `base.aws.enabled` | Enable AWS services auto-configuration | `true` |
| `base.aws.region` | AWS region | `us-east-1` |
| `base.aws.credentials.access-key-id` | AWS access key ID | - |
| `base.aws.credentials.secret-access-key` | AWS secret access key | - |
| `base.aws.s3.default-bucket` | Default S3 bucket | - |
| `base.aws.sqs.default-queue-url` | Default SQS queue URL | - |
| `base.aws.sns.default-topic-arn` | Default SNS topic ARN | - |

## Storage Providers

### Local Storage

```yaml
base:
  file:
    storage:
      providers:
        local:
          type: local
          properties:
            base-path: /tmp/files
            create-directories: true
            file-permissions: "644"
            directory-permissions: "755"
```

### S3 Storage

```yaml
base:
  file:
    storage:
      providers:
        s3:
          type: s3
          properties:
            bucket-name: my-bucket
            region: us-east-1
            endpoint: https://s3.amazonaws.com
            path-style-access: false
```

### Google Cloud Storage

```yaml
base:
  file:
    storage:
      providers:
        gcs:
          type: gcs
          properties:
            bucket-name: my-gcs-bucket
            project-id: my-project
            credentials-file: /path/to/credentials.json
```

### Azure Blob Storage

```yaml
base:
  file:
    storage:
      providers:
        azure:
          type: azure
          properties:
            container-name: my-container
            connection-string: ${AZURE_STORAGE_CONNECTION_STRING}
```

## AWS Services

### S3 Operations

```java
@Autowired
private S3Operations s3Operations;

// Upload a file
S3UploadResult result = s3Operations.uploadObject("my-bucket", "key", content, metadata);

// Download a file
S3DownloadResult<byte[]> result = s3Operations.downloadObject("my-bucket", "key", byte[].class);

// Generate presigned URL
String presignedUrl = s3Operations.generatePresignedUrl("my-bucket", "key", Duration.ofHours(1));
```

### SQS Operations

```java
@Autowired
private SqsOperations sqsOperations;

// Send a message
sqsOperations.sendMessage("my-queue", "Hello, World!");

// Receive messages
List<SqsMessage> messages = sqsOperations.receiveMessages("my-queue", 10);

// Delete a message
sqsOperations.deleteMessage("my-queue", "receipt-handle");
```

### SNS Operations

```java
@Autowired
private SnsOperations snsOperations;

// Publish a message
snsOperations.publishMessage("my-topic", "Hello, World!");

// Create a subscription
snsOperations.createSubscription("my-topic", "email", "user@example.com");
```

## Decorators

The starter includes several decorators that can be applied to file storage operations:

- **ResilientFileStorage**: Adds circuit breaker, retry, and time limiter patterns
- **LoggingFileStorage**: Adds structured logging for all operations
- **CachedFileStorage**: Adds metadata and existence caching
- **SecureFileStorage**: Adds access control and validation
- **MetricsFileStorage**: Adds metrics collection

## Testing

### Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    
    @Mock
    private FileStorage fileStorage;
    
    @Mock
    private SqsOperations sqsOperations;
    
    @InjectMocks
    private DocumentService documentService;
    
    @Test
    void shouldUploadDocumentSuccessfully() {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.txt");
        when(file.getContentType()).thenReturn("text/plain");
        when(file.getBytes()).thenReturn("Hello, World!".getBytes());
        
        UploadResult uploadResult = new UploadResult(true, "file-id", "location", null);
        when(fileStorage.upload(anyString(), any(byte[].class), any(FileMetadata.class)))
            .thenReturn(uploadResult);
        
        // When
        String result = documentService.uploadDocument(file);
        
        // Then
        assertThat(result).contains("Upload successful");
        verify(sqsOperations).sendMessage(eq("document-uploaded-queue"), any(Map.class));
    }
}
```

### Integration Tests

```java
@SpringBootTest
@Testcontainers
class FileStorageIntegrationTest {
    
    @Container
    static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
        .withServices(LocalStackContainer.Service.S3, LocalStackContainer.Service.SQS);
    
    @Autowired
    private FileStorage fileStorage;
    
    @Test
    void shouldUploadAndDownloadFile() {
        // Given
        String fileId = "test-file.txt";
        byte[] content = "Hello, World!".getBytes();
        FileMetadata metadata = FileMetadata.of("test-file.txt", "text/plain", content.length);
        
        // When
        UploadResult uploadResult = fileStorage.upload(fileId, content, metadata);
        DownloadResult<byte[]> downloadResult = fileStorage.download(fileId, byte[].class);
        
        // Then
        assertThat(uploadResult.success()).isTrue();
        assertThat(downloadResult.success()).isTrue();
        assertThat(downloadResult.content()).isEqualTo(content);
    }
}
```

## Monitoring

### Metrics

The starter provides various metrics for monitoring:

- `file.storage.operations`: Operation counts by type
- `file.storage.duration`: Operation durations
- `file.storage.results`: Success/failure counts
- `file.storage.size`: File sizes
- `file.storage.list.size`: List operation result sizes

### Logging

Structured logging is provided for all operations with the following fields:

- `operationId`: Unique operation identifier
- `fileId`: File identifier
- `operation`: Operation type
- `duration`: Operation duration
- `success`: Success/failure status
- `error`: Error message (if applicable)

## Security

### Access Control

The `SecureFileStorage` decorator provides:

- Authentication checks
- Role-based access control
- File pattern validation
- File size limits
- Content type validation

### Audit Logging

All file operations are logged for audit purposes with:

- User information
- Operation details
- File identifiers
- Timestamps
- Success/failure status

## Troubleshooting

### Common Issues

1. **File not found**: Check if the file exists and you have the correct file ID
2. **Permission denied**: Verify your AWS credentials and permissions
3. **Storage provider not available**: Check the provider configuration and availability
4. **Cache issues**: Clear the cache or check cache configuration

### Debug Logging

Enable debug logging to see detailed operation information:

```yaml
logging:
  level:
    io.github.base.starter.file: DEBUG
    io.github.base.file: DEBUG
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
