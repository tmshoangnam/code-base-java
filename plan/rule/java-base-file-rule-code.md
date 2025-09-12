# Complete Plan & Examples - java-base-file Module

## 1. Objective

Provide a foundation layer for file operations with:

- **Multiple file types support**: FILE (java.io.File), BYTE (byte[]), STREAM (InputStream/OutputStream)
- **Multiple storage providers**: AWS S3, Google Cloud Storage, Azure Blob, Local filesystem
- **Resilience patterns**: retry, timeout, circuit breaker integration
- **Structured logging**: correlation ID, file metadata tracking
- **No Spring dependency**: pure Java library with contract-first design

**Architecture:**
- `java-base-file` = pure Java library (contracts + utilities)
- `java-base-starter-file` = Spring Boot integration + implementations

## 2. Dependencies

### Runtime (core library)
Managed centrally in BOM:

```xml
<!-- Core dependencies -->
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
</dependency>

<dependency>
  <groupId>jakarta.validation</groupId>
  <artifactId>jakarta.validation-api</artifactId>
</dependency>

<!-- Resilience4j for retry/timeout -->
<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-retry</artifactId>
</dependency>
<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-timelimiter</artifactId>
</dependency>
<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-circuitbreaker</artifactId>
</dependency>
```

### Test only
```xml
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-core</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>
```

### Excluded from core
- No Spring (spring-boot-starter-*)
- No cloud SDKs (AWS SDK, GCP SDK, Azure SDK)
- No logging implementation (Logback, Log4j2)

## 3. Package Structure

```
io.github.base.file
 ├── api
 │    ├── storage        # Storage contracts, file operations
 │    ├── model          # File metadata, upload/download results
 │    ├── exception      # File-specific exceptions
 │    └── util           # File utilities, validation
 ├── internal            # Non-public APIs
 └── FileServices.java   # ServiceLoader support
```

## 4. Code Examples

### 4.1 File Types & Models

```java
package io.github.base.file.api.model;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Represents different types of file content that can be stored.
 * 
 * @since 1.0.0
 */
public enum FileType {
    /**
     * Java File object - for local filesystem operations
     */
    FILE,
    
    /**
     * Byte array - for in-memory content
     */
    BYTE,
    
    /**
     * InputStream/OutputStream - for streaming operations
     */
    STREAM
}

/**
 * File metadata information.
 * 
 * @since 1.0.0
 */
public record FileMetadata(
    String fileName,
    String contentType,
    long size,
    String checksum,
    Map<String, String> tags,
    Instant createdAt,
    Instant modifiedAt
) {
    public static FileMetadata of(String fileName, String contentType, long size) {
        return new FileMetadata(
            fileName, 
            contentType, 
            size, 
            null, 
            Map.of(), 
            Instant.now(), 
            Instant.now()
        );
    }
}

/**
 * Result of file upload operation.
 * 
 * @since 1.0.0
 */
public record UploadResult(
    String fileId,
    String location,
    FileMetadata metadata,
    boolean success,
    String errorMessage
) {
    public static UploadResult success(String fileId, String location, FileMetadata metadata) {
        return new UploadResult(fileId, location, metadata, true, null);
    }
    
    public static UploadResult failure(String errorMessage) {
        return new UploadResult(null, null, null, false, errorMessage);
    }
}

/**
 * Result of file download operation.
 * 
 * @since 1.0.0
 */
public record DownloadResult<T>(
    T content,
    FileMetadata metadata,
    boolean success,
    String errorMessage
) {
    public static <T> DownloadResult<T> success(T content, FileMetadata metadata) {
        return new DownloadResult<>(content, metadata, true, null);
    }
    
    public static <T> DownloadResult<T> failure(String errorMessage) {
        return new DownloadResult<>(null, null, false, errorMessage);
    }
}
```

### 4.2 Storage Contracts

```java
package io.github.base.file.api.storage;

import io.github.base.file.api.model.*;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

/**
 * Core contract for file storage operations.
 * 
 * This interface defines the standard operations for file storage,
 * supporting multiple file types and storage providers.
 * 
 * @since 1.0.0
 */
public interface FileStorage {

    /**
     * Uploads a file to storage.
     * 
     * @param fileId unique identifier for the file
     * @param content file content (File, byte[], or InputStream)
     * @param metadata file metadata
     * @return upload result
     * @throws FileStorageException if upload fails
     */
    UploadResult upload(String fileId, Object content, FileMetadata metadata);

    /**
     * Downloads a file from storage.
     * 
     * @param fileId unique identifier for the file
     * @param targetType expected content type (FILE, BYTE, or STREAM)
     * @return download result with content
     * @throws FileStorageException if download fails
     */
    <T> DownloadResult<T> download(String fileId, Class<T> targetType);

    /**
     * Downloads file content to an OutputStream.
     * 
     * @param fileId unique identifier for the file
     * @param outputStream target output stream
     * @return download result
     * @throws FileStorageException if download fails
     */
    DownloadResult<Void> downloadToStream(String fileId, OutputStream outputStream);

    /**
     * Checks if a file exists in storage.
     * 
     * @param fileId unique identifier for the file
     * @return true if file exists, false otherwise
     * @throws FileStorageException if check fails
     */
    boolean exists(String fileId);

    /**
     * Deletes a file from storage.
     * 
     * @param fileId unique identifier for the file
     * @return true if deleted successfully, false if file doesn't exist
     * @throws FileStorageException if deletion fails
     */
    boolean delete(String fileId);

    /**
     * Lists files matching the given criteria.
     * 
     * @param prefix optional prefix filter
     * @param maxResults maximum number of results
     * @return list of file IDs
     * @throws FileStorageException if listing fails
     */
    List<String> listFiles(String prefix, int maxResults);

    /**
     * Gets file metadata.
     * 
     * @param fileId unique identifier for the file
     * @return file metadata if exists
     * @throws FileStorageException if operation fails
     */
    Optional<FileMetadata> getMetadata(String fileId);
}

/**
 * Storage manager for multiple storage providers.
 * 
 * @since 1.0.0
 */
public interface FileStorageManager {

    /**
     * Gets a storage instance by name.
     * 
     * @param name storage name
     * @return storage instance
     * @throws IllegalArgumentException if storage not found
     */
    FileStorage getStorage(String name);

    /**
     * Gets the default storage instance.
     * 
     * @return default storage instance
     * @throws IllegalStateException if no default storage configured
     */
    FileStorage getDefaultStorage();

    /**
     * Lists all available storage instances.
     * 
     * @return map of storage names to instances
     */
    Map<String, FileStorage> getAllStorages();
}
```

### 4.3 Storage Provider SPI

```java
package io.github.base.file.spi;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;

/**
 * Service Provider Interface for file storage implementations.
 * 
 * This interface allows different storage providers to be discovered
 * and used at runtime through the Java ServiceLoader mechanism.
 * 
 * @since 1.0.0
 */
public interface FileStorageProvider {

    /**
     * Returns the unique name of this storage provider.
     * 
     * @return the provider name, never null or empty
     */
    String getName();

    /**
     * Creates a storage instance with the given configuration.
     * 
     * @param config storage configuration
     * @return configured storage instance
     * @throws IllegalArgumentException if configuration is invalid
     */
    FileStorage createStorage(Map<String, Object> config);

    /**
     * Creates a storage manager for this provider.
     * 
     * @param configs map of storage names to configurations
     * @return configured storage manager
     * @throws IllegalArgumentException if configurations are invalid
     */
    FileStorageManager createManager(Map<String, Map<String, Object>> configs);

    /**
     * Returns the supported file types for this provider.
     * 
     * @return set of supported file types
     */
    Set<FileType> getSupportedFileTypes();
}
```

### 4.4 Exception Hierarchy

```java
package io.github.base.file.api.exception;

/**
 * Base exception for file storage operations.
 * 
 * @since 1.0.0
 */
public class FileStorageException extends RuntimeException {
    
    private final String fileId;
    private final String operation;

    public FileStorageException(String message) {
        super(message);
        this.fileId = null;
        this.operation = null;
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
        this.fileId = null;
        this.operation = null;
    }

    public FileStorageException(String fileId, String operation, String message) {
        super(String.format("File operation failed [fileId=%s, operation=%s]: %s", 
                           fileId, operation, message));
        this.fileId = fileId;
        this.operation = operation;
    }

    public FileStorageException(String fileId, String operation, String message, Throwable cause) {
        super(String.format("File operation failed [fileId=%s, operation=%s]: %s", 
                           fileId, operation, message), cause);
        this.fileId = fileId;
        this.operation = operation;
    }

    public String getFileId() {
        return fileId;
    }

    public String getOperation() {
        return operation;
    }
}

/**
 * Exception thrown when file is not found.
 * 
 * @since 1.0.0
 */
public class FileNotFoundException extends FileStorageException {
    
    public FileNotFoundException(String fileId) {
        super(fileId, "GET", "File not found");
    }
}

/**
 * Exception thrown when file upload fails.
 * 
 * @since 1.0.0
 */
public class FileUploadException extends FileStorageException {
    
    public FileUploadException(String fileId, String message) {
        super(fileId, "PUT", message);
    }
    
    public FileUploadException(String fileId, String message, Throwable cause) {
        super(fileId, "PUT", message, cause);
    }
}
```

### 4.5 File Utilities

```java
package io.github.base.file.api.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for file operations and validation.
 * 
 * @since 1.0.0
 */
public final class FileUtils {

    private FileUtils() {}

    /**
     * Validates file content based on type.
     * 
     * @param content file content
     * @param expectedType expected file type
     * @throws IllegalArgumentException if content doesn't match expected type
     */
    public static void validateContent(Object content, FileType expectedType) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }

        switch (expectedType) {
            case FILE -> {
                if (!(content instanceof File)) {
                    throw new IllegalArgumentException("Content must be File for FILE type");
                }
            }
            case BYTE -> {
                if (!(content instanceof byte[])) {
                    throw new IllegalArgumentException("Content must be byte[] for BYTE type");
                }
            }
            case STREAM -> {
                if (!(content instanceof InputStream)) {
                    throw new IllegalArgumentException("Content must be InputStream for STREAM type");
                }
            }
        }
    }

    /**
     * Calculates MD5 checksum for file content.
     * 
     * @param content file content
     * @return MD5 checksum as hex string
     * @throws IOException if reading fails
     */
    public static String calculateChecksum(Object content) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            if (content instanceof File file) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                md.update(bytes);
            } else if (content instanceof byte[] bytes) {
                md.update(bytes);
            } else if (content instanceof InputStream inputStream) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            } else {
                throw new IllegalArgumentException("Unsupported content type: " + content.getClass());
            }
            
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    /**
     * Gets file size from content.
     * 
     * @param content file content
     * @return file size in bytes
     * @throws IOException if reading fails
     */
    public static long getFileSize(Object content) throws IOException {
        if (content instanceof File file) {
            return file.length();
        } else if (content instanceof byte[] bytes) {
            return bytes.length;
        } else if (content instanceof InputStream inputStream) {
            // For streams, we can't determine size without reading
            return -1;
        } else {
            throw new IllegalArgumentException("Unsupported content type: " + content.getClass());
        }
    }

    /**
     * Detects content type from file extension.
     * 
     * @param fileName file name
     * @return detected content type
     */
    public static String detectContentType(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "application/octet-stream";
        }
        
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            case "json" -> "application/json";
            case "xml" -> "application/xml";
            case "zip" -> "application/zip";
            default -> "application/octet-stream";
        };
    }
}
```

### 4.6 ServiceLoader Support

```java
package io.github.base.file;

import io.github.base.file.spi.FileStorageProvider;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * ServiceLoader helper for runtime discovery of file storage providers.
 * 
 * @since 1.0.0
 */
public final class FileServices {

    private FileServices() {}

    /**
     * Discovers all available file storage providers.
     * 
     * @return list of all discovered providers
     */
    public static List<FileStorageProvider> discoverProviders() {
        ServiceLoader<FileStorageProvider> serviceLoader = ServiceLoader.load(FileStorageProvider.class);
        return StreamSupport.stream(serviceLoader.spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Finds a file storage provider by name.
     * 
     * @param name the name of the provider to find
     * @return the provider if found, or null if not found
     */
    public static FileStorageProvider findProvider(String name) {
        return discoverProviders().stream()
                .filter(provider -> provider.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
```

## 5. Storage Provider Examples

### 5.1 Local Filesystem Provider

```java
// Implementation in java-base-starter-file
package io.github.base.file.impl.local;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.model.*;
import io.github.base.file.spi.FileStorageProvider;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LocalFileStorageProvider implements FileStorageProvider {

    @Override
    public String getName() {
        return "local";
    }

    @Override
    public FileStorage createStorage(Map<String, Object> config) {
        String basePath = (String) config.get("basePath");
        if (basePath == null) {
            throw new IllegalArgumentException("basePath is required for local storage");
        }
        return new LocalFileStorage(Paths.get(basePath));
    }

    @Override
    public FileStorageManager createManager(Map<String, Map<String, Object>> configs) {
        return new LocalFileStorageManager(configs);
    }

    @Override
    public Set<FileType> getSupportedFileTypes() {
        return Set.of(FileType.FILE, FileType.BYTE, FileType.STREAM);
    }
}

class LocalFileStorage implements FileStorage {
    private final Path basePath;

    public LocalFileStorage(Path basePath) {
        this.basePath = basePath;
        try {
            Files.createDirectories(basePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create base directory", e);
        }
    }

    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        try {
            Path targetPath = basePath.resolve(fileId);
            
            if (content instanceof File file) {
                Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } else if (content instanceof byte[] bytes) {
                Files.write(targetPath, bytes);
            } else if (content instanceof InputStream inputStream) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                return UploadResult.failure("Unsupported content type: " + content.getClass());
            }
            
            return UploadResult.success(fileId, targetPath.toString(), metadata);
            
        } catch (IOException e) {
            return UploadResult.failure("Upload failed: " + e.getMessage());
        }
    }

    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> targetType) {
        try {
            Path filePath = basePath.resolve(fileId);
            if (!Files.exists(filePath)) {
                return DownloadResult.failure("File not found: " + fileId);
            }

            Object content;
            if (targetType == File.class) {
                content = filePath.toFile();
            } else if (targetType == byte[].class) {
                content = Files.readAllBytes(filePath);
            } else if (targetType == InputStream.class) {
                content = Files.newInputStream(filePath);
            } else {
                return DownloadResult.failure("Unsupported target type: " + targetType);
            }

            FileMetadata metadata = FileMetadata.of(
                filePath.getFileName().toString(),
                Files.probeContentType(filePath),
                Files.size(filePath)
            );

            return DownloadResult.success((T) content, metadata);

        } catch (IOException e) {
            return DownloadResult.failure("Download failed: " + e.getMessage());
        }
    }

    // ... other methods implementation
}
```

### 5.2 AWS S3 Provider

```java
// Implementation in java-base-starter-file
package io.github.base.file.impl.s3;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.model.*;
import io.github.base.file.spi.FileStorageProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

public class S3FileStorageProvider implements FileStorageProvider {

    @Override
    public String getName() {
        return "s3";
    }

    @Override
    public FileStorage createStorage(Map<String, Object> config) {
        String bucketName = (String) config.get("bucketName");
        if (bucketName == null) {
            throw new IllegalArgumentException("bucketName is required for S3 storage");
        }
        
        S3Client s3Client = S3Client.builder()
            .region((String) config.get("region"))
            .build();
            
        return new S3FileStorage(s3Client, bucketName);
    }

    @Override
    public FileStorageManager createManager(Map<String, Map<String, Object>> configs) {
        return new S3FileStorageManager(configs);
    }

    @Override
    public Set<FileType> getSupportedFileTypes() {
        return Set.of(FileType.BYTE, FileType.STREAM);
    }
}

class S3FileStorage implements FileStorage {
    private final S3Client s3Client;
    private final String bucketName;

    public S3FileStorage(S3Client s3Client, String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        try {
            PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileId)
                .contentType(metadata.contentType())
                .contentLength(metadata.size());

            if (content instanceof byte[] bytes) {
                s3Client.putObject(requestBuilder.build(), 
                    RequestBody.fromBytes(bytes));
            } else if (content instanceof InputStream inputStream) {
                s3Client.putObject(requestBuilder.build(), 
                    RequestBody.fromInputStream(inputStream, metadata.size()));
            } else {
                return UploadResult.failure("S3 only supports BYTE and STREAM content types");
            }

            String location = String.format("s3://%s/%s", bucketName, fileId);
            return UploadResult.success(fileId, location, metadata);

        } catch (Exception e) {
            return UploadResult.failure("S3 upload failed: " + e.getMessage());
        }
    }

    // ... other methods implementation
}
```

## 6. Integration with Existing Modules

### 6.1 Resilience Integration

```java
package io.github.base.file.api.resilience;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.base.file.api.storage.FileStorage;

/**
 * Decorated file storage with resilience patterns.
 * 
 * @since 1.0.0
 */
public class ResilientFileStorage implements FileStorage {
    
    private final FileStorage delegate;
    private final Retry retry;
    private final TimeLimiter timeLimiter;

    public ResilientFileStorage(FileStorage delegate, Retry retry, TimeLimiter timeLimiter) {
        this.delegate = delegate;
        this.retry = retry;
        this.timeLimiter = timeLimiter;
    }

    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        return Retry.decorateSupplier(retry, () -> 
            TimeLimiter.decorateSupplier(timeLimiter, () -> 
                delegate.upload(fileId, content, metadata)
            ).get()
        ).get();
    }

    // ... other methods with similar decoration
}
```

### 6.2 Logging Integration

```java
package io.github.base.file.api.logging;

import io.github.base.core.api.logging.StructuredLogger;
import io.github.base.file.api.storage.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File storage with structured logging.
 * 
 * @since 1.0.0
 */
public class LoggingFileStorage implements FileStorage {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingFileStorage.class);
    private final FileStorage delegate;

    public LoggingFileStorage(FileStorage delegate) {
        this.delegate = delegate;
    }

    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        String correlationId = generateCorrelationId();
        
        StructuredLogger.logBusinessEvent(correlationId, "file_upload_start", Map.of(
            "fileId", fileId,
            "fileName", metadata.fileName(),
            "size", metadata.size()
        ));

        try {
            UploadResult result = delegate.upload(fileId, content, metadata);
            
            if (result.success()) {
                StructuredLogger.logBusinessEvent(correlationId, "file_upload_success", Map.of(
                    "fileId", fileId,
                    "location", result.location()
                ));
            } else {
                StructuredLogger.logError(correlationId, "file_upload_failed", 
                    new RuntimeException(result.errorMessage()), Map.of(
                        "fileId", fileId,
                        "error", result.errorMessage()
                    ));
            }
            
            return result;
            
        } catch (Exception e) {
            StructuredLogger.logError(correlationId, "file_upload_exception", e, Map.of(
                "fileId", fileId
            ));
            throw e;
        }
    }

    private String generateCorrelationId() {
        return "file-" + System.currentTimeMillis() + "-" + Thread.currentThread().getId();
    }
}
```

## 7. Starter Module Integration

### 7.1 AutoConfiguration

```java
// In java-base-starter-file
package io.github.base.file.starter.config;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;
import io.github.base.file.spi.FileStorageProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(FileStorage.class)
@EnableConfigurationProperties(FileStorageProperties.class)
public class FileStorageAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FileStorageManager fileStorageManager(FileStorageProperties properties) {
        // Auto-configure based on properties
        return createStorageManager(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public FileStorage defaultFileStorage(FileStorageManager manager) {
        return manager.getDefaultStorage();
    }
}
```

### 7.2 Properties Configuration

```java
package io.github.base.file.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;

@ConfigurationProperties(prefix = "base.file.storage")
public class FileStorageProperties {
    
    private String defaultProvider = "local";
    private Map<String, Map<String, Object>> providers = Map.of();
    
    // getters and setters
}
```

## 8. Usage Examples

### 8.1 Basic Usage

```java
// In application code
@Service
public class DocumentService {
    
    private final FileStorage fileStorage;
    
    public DocumentService(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }
    
    public String uploadDocument(byte[] content, String fileName) {
        FileMetadata metadata = FileMetadata.of(fileName, "application/pdf", content.length);
        String fileId = generateFileId();
        
        UploadResult result = fileStorage.upload(fileId, content, metadata);
        if (result.success()) {
            return result.location();
        } else {
            throw new RuntimeException("Upload failed: " + result.errorMessage());
        }
    }
    
    public byte[] downloadDocument(String fileId) {
        DownloadResult<byte[]> result = fileStorage.download(fileId, byte[].class);
        if (result.success()) {
            return result.content();
        } else {
            throw new RuntimeException("Download failed: " + result.errorMessage());
        }
    }
}
```

### 8.2 Multiple Storage Providers

```java
// Configuration
@Configuration
public class FileStorageConfig {
    
    @Bean
    public FileStorageManager fileStorageManager() {
        Map<String, Map<String, Object>> configs = Map.of(
            "local", Map.of("basePath", "/tmp/files"),
            "s3", Map.of(
                "bucketName", "my-bucket",
                "region", "us-east-1"
            ),
            "gcs", Map.of(
                "bucketName", "my-gcs-bucket",
                "projectId", "my-project"
            )
        );
        
        return FileServices.findProvider("s3").createManager(configs);
    }
}

// Usage
@Service
public class MultiStorageService {
    
    private final FileStorageManager storageManager;
    
    public void uploadToS3(String fileId, byte[] content) {
        FileStorage s3Storage = storageManager.getStorage("s3");
        s3Storage.upload(fileId, content, FileMetadata.of("file.pdf", "application/pdf", content.length));
    }
    
    public void uploadToLocal(String fileId, File file) {
        FileStorage localStorage = storageManager.getStorage("local");
        localStorage.upload(fileId, file, FileMetadata.of(file.getName(), "application/pdf", file.length()));
    }
}
```

## 9. Quality & Testing

- **Unit tests**: Test all contracts, utilities, and exception handling
- **Integration tests**: Test with actual storage providers (using Testcontainers)
- **Jacoco ≥ 80% coverage**
- **PIT Mutation Testing** for file operations
- **Revapi/Japicmp** for backward compatibility

## 10. Documentation

- **Javadoc**: All public APIs with usage examples
- **Usage guide**: `docs/file-storage-usage.md`
- **Provider guide**: `docs/storage-providers.md`
- **Migration guide**: For existing file handling code

---

✅ **With this structure, java-base-file provides:**

- **Flexible file types**: FILE, BYTE, STREAM support
- **Multiple storage providers**: Local, S3, GCS, Azure
- **Enterprise-ready**: Resilience, logging, validation
- **Contract-first**: Clean separation of concerns
- **Spring Boot integration**: Auto-configuration and properties
- **Extensible**: Easy to add new storage providers

