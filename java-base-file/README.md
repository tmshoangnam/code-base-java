# Java Base File Storage API

[![Maven Central](https://img.shields.io/maven-central/v/io.github.tmshoangnam/java-base-file.svg)](https://mvnrepository.com/artifact/io.github.tmshoangnam/java-base-file)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/java-17%2B-orange.svg)](https://openjdk.java.net/)

A comprehensive file storage abstraction library for Java applications, providing a unified API for various storage providers including local filesystem, AWS S3, Google Cloud Storage, and Azure Blob Storage.

## 🚀 Features

- **Unified API**: Single interface for all storage providers
- **Provider Abstraction**: Easy to add new storage providers
- **Type Safety**: Strong typing with comprehensive model classes
- **Validation**: Built-in validation for file metadata and operations
- **Utilities**: Rich set of utility methods for file operations
- **Exception Handling**: Comprehensive exception hierarchy
- **Service Discovery**: SPI-based provider discovery mechanism

## 📦 Installation

### Maven

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-file</artifactId>
    <version>1.0.5-SNAPSHOT</version>
</dependency>
```

### Gradle

```gradle
implementation 'io.github.tmshoangnam:java-base-file:1.0.5-SNAPSHOT'
```

## 🏗️ Architecture

### Core Components

- **FileStorage**: Main interface for file operations
- **FileStorageManager**: Manages multiple storage providers
- **FileStorageProvider**: SPI interface for storage implementations
- **FileMetadata**: Model class for file information
- **FileType**: Enumeration of supported file types
- **FileUtils**: Utility class for file operations

### Package Structure

```
io.github.base.file
├── api
│   ├── exception/          # Exception classes
│   ├── model/             # Data models
│   ├── storage/           # Storage interfaces
│   └── util/              # Utility classes
├── spi/                   # Service Provider Interface
└── FileServices          # Main service class
```

## 🔧 Usage

### Basic File Operations

```java
import io.github.base.file.FileServices;
import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.model.DownloadResult;

// Get a storage instance
FileStorage storage = FileServices.getStorage("local");

// Upload a file
byte[] content = "Hello World".getBytes();
UploadResult uploadResult = storage.upload("file-123", content, "hello.txt", "text/plain");

if (uploadResult.isSuccess()) {
    System.out.println("File uploaded successfully: " + uploadResult.getLocation());
}

// Download a file
DownloadResult<byte[]> downloadResult = storage.download("file-123");
if (downloadResult.isSuccess()) {
    byte[] downloadedContent = downloadResult.getContent();
    FileMetadata metadata = downloadResult.getMetadata();
    System.out.println("Downloaded file: " + metadata.getFileName());
}

// Check if file exists
boolean exists = storage.exists("file-123");

// Delete a file
boolean deleted = storage.delete("file-123");

// Get file metadata
FileMetadata metadata = storage.getMetadata("file-123");
```

### Working with File Metadata

```java
import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.FileType;

// Create file metadata
FileMetadata metadata = FileMetadata.builder()
    .fileId("file-123")
    .fileName("document.pdf")
    .contentType("application/pdf")
    .fileSize(1024L)
    .fileType(FileType.DOCUMENT)
    .addMetadata("author", "John Doe")
    .addMetadata("version", "1.0")
    .build();

// Access metadata
String fileName = metadata.getFileName();
String contentType = metadata.getContentType();
long fileSize = metadata.getFileSize();
FileType fileType = metadata.getFileType();

// Check for specific metadata
if (metadata.hasMetadata("author")) {
    String author = metadata.getMetadataValue("author");
    System.out.println("Author: " + author);
}
```

### File Type Detection

```java
import io.github.base.file.api.util.FileUtils;
import io.github.base.file.api.model.FileType;

// Detect file type from filename
FileType fileType = FileUtils.detectFileType("document.pdf");
// Returns FileType.DOCUMENT

// Get file extension
String extension = FileUtils.getFileExtension("image.jpg");
// Returns "jpg"

// Validate file name
boolean isValid = FileUtils.isValidFileName("document.pdf");
// Returns true

// Format file size
String formattedSize = FileUtils.formatFileSize(1048576);
// Returns "1.0 MB"
```

### Service Provider Interface (SPI)

```java
import io.github.base.file.spi.FileStorageProvider;

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

### Error Handling

```java
import io.github.base.file.api.exception.FileStorageException;
import io.github.base.file.api.exception.FileNotFoundException;
import io.github.base.file.api.exception.FileUploadException;

try {
    FileStorage storage = FileServices.getStorage("local");
    UploadResult result = storage.upload("file-123", content, "test.txt", "text/plain");
    
    if (!result.isSuccess()) {
        System.err.println("Upload failed: " + result.getErrorMessage());
    }
} catch (FileNotFoundException e) {
    System.err.println("File not found: " + e.getMessage());
} catch (FileUploadException e) {
    System.err.println("Upload failed: " + e.getMessage());
} catch (FileStorageException e) {
    System.err.println("Storage error: " + e.getMessage());
}
```

## 🧪 Testing

The library includes comprehensive unit and integration tests with 80%+ code coverage.

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=FileMetadataTest
```

### Test Structure

- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **Mock Tests**: Test with mocked dependencies
- **Edge Case Tests**: Test boundary conditions and error scenarios

## 📊 Metrics and Monitoring

The library provides built-in metrics and monitoring capabilities:

- **Operation Counters**: Track upload, download, delete operations
- **Performance Metrics**: Measure operation latency and throughput
- **Error Tracking**: Monitor failed operations and error rates
- **Cache Statistics**: Track cache hit rates and performance

## 🔒 Security

- **Input Validation**: Comprehensive validation of all inputs
- **Path Traversal Protection**: Prevents directory traversal attacks
- **Content Type Validation**: Validates file types and content
- **Access Control**: Supports access control mechanisms
- **Audit Logging**: Comprehensive audit trail for all operations

## 🚀 Performance

- **Caching**: Built-in caching for metadata and frequently accessed files
- **Async Operations**: Support for asynchronous file operations
- **Connection Pooling**: Efficient connection management
- **Batch Operations**: Support for batch file operations
- **Streaming**: Support for streaming large files

## 📝 Configuration

The library supports various configuration options:

```java
// Configure storage properties
FileStorageProperties properties = FileStorageProperties.builder()
    .defaultProvider("local")
    .enabled(true)
    .cacheEnabled(true)
    .cacheTtlSeconds(300L)
    .maxCacheSize(1000)
    .build();
```

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

- **Documentation**: [Wiki](https://github.com/tmshoangnam/java-base-file/wiki)
- **Issues**: [GitHub Issues](https://github.com/tmshoangnam/java-base-file/issues)
- **Discussions**: [GitHub Discussions](https://github.com/tmshoangnam/java-base-file/discussions)

## 🔄 Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed list of changes.

## 🏷️ Versioning

We use [Semantic Versioning](https://semver.org/) for version numbers:
- **MAJOR**: Breaking changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes (backward compatible)

## 🙏 Acknowledgments

- Spring Framework for inspiration
- AWS SDK for Java for reference implementation
- Google Cloud Storage for design patterns
- Azure SDK for Java for integration ideas

---

**Made with ❤️ by the Java Base Team**
