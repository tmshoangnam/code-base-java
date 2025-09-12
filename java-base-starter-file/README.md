# Java Base Starter File Storage

[![Maven Central](https://img.shields.io/maven-central/v/io.github.tmshoangnam/java-base-starter-file.svg)](https://mvnrepository.com/artifact/io.github.tmshoangnam/java-base-starter-file)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/java-17%2B-orange.svg)](https://openjdk.java.net/)

A Spring Boot starter for file storage operations, providing auto-configuration and seamless integration with various storage providers including local filesystem, AWS S3, Google Cloud Storage, and Azure Blob Storage.

## 🚀 Features

- **Auto-Configuration**: Zero-configuration setup with Spring Boot
- **Multiple Providers**: Support for local, S3, GCS, and Azure storage
- **Decorator Pattern**: Built-in decorators for caching, logging, metrics, and resilience
- **Configuration Properties**: Externalized configuration via application properties
- **Health Checks**: Built-in health indicators for storage providers
- **Metrics**: Micrometer integration for monitoring
- **Validation**: Comprehensive configuration validation
- **Factory Pattern**: Flexible storage instance creation

## 📦 Installation

### Maven

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-starter-file</artifactId>
    <version>1.0.5-SNAPSHOT</version>
</dependency>
```

### Gradle

```gradle
implementation 'io.github.tmshoangnam:java-base-starter-file:1.0.5-SNAPSHOT'
```

## 🏗️ Architecture

### Core Components

- **FileStorageAutoConfiguration**: Main auto-configuration class
- **FileStorageProperties**: Configuration properties binding
- **FileStorageFactory**: Factory for creating storage instances
- **FileStorageConfigValidator**: Configuration validation
- **Storage Providers**: Local, S3, GCS, and Azure implementations
- **Decorators**: Caching, logging, metrics, and resilience decorators

### Package Structure

```
io.github.base.starter.file
├── autoconfig/            # Auto-configuration classes
├── config/                # Configuration utilities
├── constants/             # Constants and defaults
├── decorators/            # Storage decorators
├── factory/               # Factory classes
├── impl/                  # Provider implementations
│   ├── local/            # Local storage implementation
│   ├── s3/               # S3 storage implementation
│   ├── gcs/              # GCS storage implementation
│   └── azure/            # Azure storage implementation
└── util/                  # Utility classes
```

## 🔧 Usage

### Basic Configuration

Add the starter to your Spring Boot application and configure the storage provider:

```yaml
# application.yml
base:
  file:
    storage:
      enabled: true
      default-provider: local
      cache-enabled: true
      metrics-enabled: true
      cache-ttl-seconds: 300
      max-cache-size: 1000
      providers:
        local:
          enabled: true
          cache-enabled: true
          metrics-enabled: true
        s3:
          enabled: true
          cache-enabled: true
          metrics-enabled: true
```

### Using FileStorageManager

```java
import io.github.base.file.api.storage.FileStorageManager;
import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.model.DownloadResult;

@RestController
public class FileController {
    
    private final FileStorageManager fileStorageManager;
    
    public FileController(FileStorageManager fileStorageManager) {
        this.fileStorageManager = fileStorageManager;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = UUID.randomUUID().toString();
            UploadResult result = fileStorageManager.upload(
                fileId,
                file.getBytes(),
                file.getOriginalFilename(),
                file.getContentType()
            );
            
            if (result.isSuccess()) {
                return ResponseEntity.ok("File uploaded: " + result.getLocation());
            } else {
                return ResponseEntity.badRequest().body("Upload failed: " + result.getErrorMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
        try {
            DownloadResult<byte[]> result = fileStorageManager.download(fileId);
            
            if (result.isSuccess()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(result.getMetadata().getContentType()))
                    .body(result.getContent());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
```

### Using Specific Storage Provider

```java
import io.github.base.file.api.storage.FileStorage;

@Service
public class FileService {
    
    private final FileStorage localStorage;
    private final FileStorage s3Storage;
    
    public FileService(FileStorageManager fileStorageManager) {
        this.localStorage = fileStorageManager.getStorage("local");
        this.s3Storage = fileStorageManager.getStorage("s3");
    }
    
    public void uploadToLocal(String fileId, byte[] content, String fileName, String contentType) {
        localStorage.upload(fileId, content, fileName, contentType);
    }
    
    public void uploadToS3(String fileId, byte[] content, String fileName, String contentType) {
        s3Storage.upload(fileId, content, fileName, contentType);
    }
}
```

### Configuration Properties

```yaml
# application.yml
base:
  file:
    storage:
      # Global settings
      enabled: true
      default-provider: local
      cache-enabled: true
      metrics-enabled: true
      cache-ttl-seconds: 300
      max-cache-size: 1000
      
      # Provider-specific settings
      providers:
        local:
          enabled: true
          cache-enabled: true
          metrics-enabled: true
          resilient-enabled: true
          secure-enabled: true
          logging-enabled: true
          configuration:
            base-path: "/tmp/file-storage"
            
        s3:
          enabled: true
          cache-enabled: true
          metrics-enabled: true
          resilient-enabled: true
          secure-enabled: true
          logging-enabled: true
          configuration:
            bucket-name: "my-bucket"
            region: "us-east-1"
            
        gcs:
          enabled: true
          cache-enabled: true
          metrics-enabled: true
          resilient-enabled: true
          secure-enabled: true
          logging-enabled: true
          configuration:
            bucket-name: "my-bucket"
            project-id: "my-project"
            
        azure:
          enabled: true
          cache-enabled: true
          metrics-enabled: true
          resilient-enabled: true
          secure-enabled: true
          logging-enabled: true
          configuration:
            container-name: "my-container"
            account-name: "myaccount"
```

### AWS Configuration

```yaml
# application.yml
base:
  aws:
    region: us-east-1
    credentials:
      access-key-id: ${AWS_ACCESS_KEY_ID}
      secret-access-key: ${AWS_SECRET_ACCESS_KEY}
      session-token: ${AWS_SESSION_TOKEN}
    s3:
      enabled: true
      bucket-name: my-bucket
      region: us-east-1
```

### Health Checks

The starter provides health indicators for storage providers:

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

Access health information at `/actuator/health`:

```json
{
  "status": "UP",
  "components": {
    "fileStorage": {
      "status": "UP",
      "details": {
        "local": "UP",
        "s3": "UP"
      }
    }
  }
}
```

### Metrics

The starter integrates with Micrometer for metrics collection:

```yaml
# application.yml
management:
  metrics:
    export:
      prometheus:
        enabled: true
```

Available metrics:
- `file.storage.upload.count` - Upload operation count
- `file.storage.download.count` - Download operation count
- `file.storage.delete.count` - Delete operation count
- `file.storage.operation.duration` - Operation duration
- `file.storage.cache.hit.count` - Cache hit count
- `file.storage.cache.miss.count` - Cache miss count

### Custom Configuration

```java
@Configuration
public class FileStorageConfig {
    
    @Bean
    @ConfigurationProperties("base.file.storage")
    public FileStorageProperties fileStorageProperties() {
        return new FileStorageProperties();
    }
    
    @Bean
    public FileStorageFactory fileStorageFactory() {
        return new FileStorageFactory();
    }
    
    @Bean
    public FileStorageConfigValidator fileStorageConfigValidator() {
        return new FileStorageConfigValidator();
    }
}
```

### Custom Storage Provider

```java
@Component
public class CustomFileStorageProvider implements FileStorageProvider {
    
    @Override
    public String getProviderName() {
        return "custom";
    }
    
    @Override
    public String getProviderDescription() {
        return "Custom file storage provider";
    }
    
    @Override
    public String getProviderVersion() {
        return "1.0.0";
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
    
    @Override
    public int getPriority() {
        return 50;
    }
    
    @Override
    public FileStorage createStorage() {
        return new CustomFileStorage();
    }
    
    // ... implement other methods
}
```

## 🧪 Testing

The starter includes comprehensive unit and integration tests with 80%+ code coverage.

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=FileStorageAutoConfigurationTest
```

### Test Configuration

```java
@SpringBootTest
@TestPropertySource(properties = {
    "base.file.storage.enabled=true",
    "base.file.storage.default-provider=local",
    "base.file.storage.cache-enabled=true"
})
class FileStorageIntegrationTest {
    
    @Autowired
    private FileStorageManager fileStorageManager;
    
    @Test
    void shouldUploadFile() {
        // Test implementation
    }
}
```

## 📊 Monitoring and Observability

### Health Indicators

- **FileStorageHealthIndicator**: Overall storage health
- **ProviderHealthIndicator**: Individual provider health
- **CacheHealthIndicator**: Cache system health

### Metrics

- **Operation Metrics**: Upload, download, delete counts
- **Performance Metrics**: Operation duration and throughput
- **Cache Metrics**: Hit rates and cache performance
- **Error Metrics**: Failure rates and error types

### Logging

- **Structured Logging**: JSON-formatted logs
- **Correlation IDs**: Request tracing across services
- **Audit Logging**: Security and compliance logging

## 🔒 Security

- **Input Validation**: Comprehensive input validation
- **Path Traversal Protection**: Prevents directory traversal attacks
- **Content Type Validation**: Validates file types and content
- **Access Control**: Role-based access control
- **Audit Logging**: Comprehensive audit trail

## 🚀 Performance

- **Caching**: Built-in caching for metadata and files
- **Async Operations**: Support for asynchronous operations
- **Connection Pooling**: Efficient connection management
- **Batch Operations**: Support for batch file operations
- **Streaming**: Support for streaming large files

## 📝 Configuration Reference

### FileStorageProperties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `base.file.storage.enabled` | boolean | true | Enable file storage auto-configuration |
| `base.file.storage.default-provider` | string | local | Default storage provider |
| `base.file.storage.cache-enabled` | boolean | true | Enable caching |
| `base.file.storage.metrics-enabled` | boolean | true | Enable metrics collection |
| `base.file.storage.cache-ttl-seconds` | long | 300 | Cache TTL in seconds |
| `base.file.storage.max-cache-size` | int | 1000 | Maximum cache size |

### Provider Configuration

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `base.file.storage.providers.{name}.enabled` | boolean | true | Enable specific provider |
| `base.file.storage.providers.{name}.cache-enabled` | boolean | null | Override global cache setting |
| `base.file.storage.providers.{name}.metrics-enabled` | boolean | null | Override global metrics setting |
| `base.file.storage.providers.{name}.resilient-enabled` | boolean | true | Enable resilience features |
| `base.file.storage.providers.{name}.secure-enabled` | boolean | true | Enable security features |
| `base.file.storage.providers.{name}.logging-enabled` | boolean | true | Enable logging features |

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Development Setup

1. Clone the repository
2. Install dependencies: `mvn install`
3. Run tests: `mvn test`
4. Build the project: `mvn package`

### Code Style

We follow the Google Java Style Guide with some customizations:
- 4 spaces for indentation
- Maximum line length: 120 characters
- Use `@DisplayName` for test methods
- Comprehensive Javadoc for public APIs

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [Wiki](https://github.com/tmshoangnam/java-base-starter-file/wiki)
- **Issues**: [GitHub Issues](https://github.com/tmshoangnam/java-base-starter-file/issues)
- **Discussions**: [GitHub Discussions](https://github.com/tmshoangnam/java-base-starter-file/discussions)

## 🔄 Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed list of changes.

## 🏷️ Versioning

We use [Semantic Versioning](https://semver.org/) for version numbers:
- **MAJOR**: Breaking changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes (backward compatible)

## 🙏 Acknowledgments

- Spring Boot for auto-configuration framework
- Spring Cloud for configuration management
- Micrometer for metrics collection
- The Java community for feedback and contributions

---

**Made with ❤️ by the Java Base Team**