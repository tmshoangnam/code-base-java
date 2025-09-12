# Java Base File - Integration Guide

## Tích hợp với các module hiện có

### 1. Tích hợp với java-base-core

#### 1.1 Sử dụng Resilience Patterns

```java
// Trong java-base-file, sử dụng resilience từ java-base-core
package io.github.base.file.api.resilience;

import io.github.base.core.api.resilience.ResilienceFactories;
import io.github.base.file.api.storage.FileStorage;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;

/**
 * File storage với resilience patterns từ java-base-core.
 */
public class ResilientFileStorage implements FileStorage {
    
    private final FileStorage delegate;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;
    private final TimeLimiter timeLimiter;

    public ResilientFileStorage(FileStorage delegate) {
        this.delegate = delegate;
        this.circuitBreaker = ResilienceFactories.defaultCircuitBreakerRegistry()
            .circuitBreaker("file-storage");
        this.retry = ResilienceFactories.defaultRetryRegistry()
            .retry("file-storage");
        this.timeLimiter = ResilienceFactories.defaultTimeLimiterRegistry()
            .timeLimiter("file-storage");
    }

    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        return CircuitBreaker.decorateSupplier(circuitBreaker, () ->
            Retry.decorateSupplier(retry, () ->
                TimeLimiter.decorateSupplier(timeLimiter, () ->
                    delegate.upload(fileId, content, metadata)
                ).get()
            ).get()
        ).get();
    }
}
```

#### 1.2 Sử dụng Structured Logging

```java
// Tích hợp với StructuredLogger từ java-base-core
package io.github.base.file.api.logging;

import io.github.base.core.api.logging.StructuredLogger;
import io.github.base.file.api.storage.FileStorage;
import java.util.Map;

public class LoggingFileStorage implements FileStorage {
    
    private final FileStorage delegate;

    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        String correlationId = generateCorrelationId();
        
        // Sử dụng StructuredLogger từ java-base-core
        StructuredLogger.logBusinessEvent(correlationId, "file_upload_start", Map.of(
            "fileId", fileId,
            "fileName", metadata.fileName(),
            "size", metadata.size(),
            "contentType", metadata.contentType()
        ));

        try {
            UploadResult result = delegate.upload(fileId, content, metadata);
            
            if (result.success()) {
                StructuredLogger.logBusinessEvent(correlationId, "file_upload_success", Map.of(
                    "fileId", fileId,
                    "location", result.location(),
                    "duration_ms", System.currentTimeMillis() - startTime
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
}
```

#### 1.3 Sử dụng Validation Utils

```java
// Tích hợp với ValidationUtils từ java-base-core
package io.github.base.file.api.util;

import io.github.base.core.api.util.ValidationUtils;
import io.github.base.file.api.model.FileMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FileMetadataValidator {
    
    public static void validateMetadata(FileMetadata metadata) {
        // Sử dụng ValidationUtils từ java-base-core
        ValidationUtils.validate(metadata);
        
        // Additional file-specific validation
        if (metadata.size() < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        
        if (metadata.fileName() != null && metadata.fileName().contains("..")) {
            throw new IllegalArgumentException("File name cannot contain '..'");
        }
    }
}

// FileMetadata với validation annotations
public record FileMetadata(
    @NotBlank String fileName,
    @NotNull String contentType,
    @Positive long size,
    String checksum,
    Map<String, String> tags,
    @NotNull Instant createdAt,
    @NotNull Instant modifiedAt
) {
    // Constructor sẽ tự động validate khi sử dụng ValidationUtils.validate()
}
```

### 2. Tích hợp với java-base-cache

#### 2.1 Cache File Metadata

```java
// Sử dụng Cache từ java-base-cache để cache metadata
package io.github.base.file.api.cache;

import io.github.base.cache.api.Cache;
import io.github.base.cache.api.CacheManager;
import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.storage.FileStorage;

public class CachedFileStorage implements FileStorage {
    
    private final FileStorage delegate;
    private final Cache<String, FileMetadata> metadataCache;
    
    public CachedFileStorage(FileStorage delegate, CacheManager cacheManager) {
        this.delegate = delegate;
        this.metadataCache = cacheManager.getCache("file-metadata");
    }

    @Override
    public Optional<FileMetadata> getMetadata(String fileId) {
        // Check cache first
        Optional<FileMetadata> cached = metadataCache.get(fileId);
        if (cached.isPresent()) {
            return cached;
        }
        
        // Fetch from storage and cache
        Optional<FileMetadata> metadata = delegate.getMetadata(fileId);
        metadata.ifPresent(m -> metadataCache.put(fileId, m));
        
        return metadata;
    }

    @Override
    public boolean delete(String fileId) {
        boolean deleted = delegate.delete(fileId);
        if (deleted) {
            // Remove from cache
            metadataCache.evict(fileId);
        }
        return deleted;
    }
}
```

#### 2.2 Cache File Content (for small files)

```java
// Cache small files in memory
public class InMemoryCachedFileStorage implements FileStorage {
    
    private final FileStorage delegate;
    private final Cache<String, byte[]> contentCache;
    private final long maxCacheSize;
    
    public InMemoryCachedFileStorage(FileStorage delegate, CacheManager cacheManager, long maxCacheSize) {
        this.delegate = delegate;
        this.contentCache = cacheManager.getCache("file-content");
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> targetType) {
        if (targetType == byte[].class) {
            // Check cache first
            Optional<byte[]> cached = contentCache.get(fileId);
            if (cached.isPresent()) {
                FileMetadata metadata = delegate.getMetadata(fileId).orElse(null);
                return DownloadResult.success((T) cached.get(), metadata);
            }
        }
        
        // Download from storage
        DownloadResult<T> result = delegate.download(fileId, targetType);
        
        // Cache small files
        if (result.success() && targetType == byte[].class && result.metadata().size() <= maxCacheSize) {
            contentCache.put(fileId, (byte[]) result.content());
        }
        
        return result;
    }
}
```

### 3. Tích hợp với java-base-security

#### 3.1 File Access Control

```java
// Tích hợp với security để kiểm soát quyền truy cập file
package io.github.base.file.api.security;

import io.github.base.security.api.AccessControl;
import io.github.base.security.api.SecurityContext;
import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.model.FileMetadata;

public class SecureFileStorage implements FileStorage {
    
    private final FileStorage delegate;
    private final AccessControl accessControl;
    
    public SecureFileStorage(FileStorage delegate, AccessControl accessControl) {
        this.delegate = delegate;
        this.accessControl = accessControl;
    }

    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        // Check upload permission
        if (!accessControl.hasPermission(SecurityContext.getCurrentUser(), "file:upload")) {
            return UploadResult.failure("Insufficient permissions for file upload");
        }
        
        // Check file type restrictions
        if (!isAllowedFileType(metadata.contentType())) {
            return UploadResult.failure("File type not allowed: " + metadata.contentType());
        }
        
        return delegate.upload(fileId, content, metadata);
    }

    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> targetType) {
        // Check download permission
        if (!accessControl.hasPermission(SecurityContext.getCurrentUser(), "file:download")) {
            return DownloadResult.failure("Insufficient permissions for file download");
        }
        
        // Check file access
        FileMetadata metadata = delegate.getMetadata(fileId).orElse(null);
        if (metadata != null && !canAccessFile(metadata)) {
            return DownloadResult.failure("Access denied to file: " + fileId);
        }
        
        return delegate.download(fileId, targetType);
    }
    
    private boolean isAllowedFileType(String contentType) {
        // Define allowed file types based on security policy
        return contentType.startsWith("image/") || 
               contentType.startsWith("application/pdf") ||
               contentType.startsWith("text/");
    }
    
    private boolean canAccessFile(FileMetadata metadata) {
        // Check if user can access this specific file
        String currentUser = SecurityContext.getCurrentUser();
        return metadata.tags().get("owner").equals(currentUser) ||
               accessControl.hasRole(currentUser, "ADMIN");
    }
}
```

#### 3.2 File Encryption

```java
// Tích hợp với security để mã hóa file
package io.github.base.file.api.security;

import io.github.base.security.api.EncryptionService;
import io.github.base.file.api.storage.FileStorage;

public class EncryptedFileStorage implements FileStorage {
    
    private final FileStorage delegate;
    private final EncryptionService encryptionService;
    
    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        try {
            // Encrypt content before upload
            Object encryptedContent = encryptContent(content);
            
            // Update metadata to indicate encryption
            FileMetadata encryptedMetadata = new FileMetadata(
                metadata.fileName(),
                metadata.contentType(),
                getEncryptedSize(encryptedContent),
                metadata.checksum(),
                addEncryptionTag(metadata.tags()),
                metadata.createdAt(),
                metadata.modifiedAt()
            );
            
            return delegate.upload(fileId, encryptedContent, encryptedMetadata);
            
        } catch (Exception e) {
            return UploadResult.failure("Encryption failed: " + e.getMessage());
        }
    }

    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> targetType) {
        DownloadResult<T> result = delegate.download(fileId, targetType);
        
        if (result.success()) {
            try {
                // Decrypt content after download
                T decryptedContent = decryptContent(result.content());
                
                // Update metadata to remove encryption info
                FileMetadata decryptedMetadata = removeEncryptionTag(result.metadata());
                
                return DownloadResult.success(decryptedContent, decryptedMetadata);
                
            } catch (Exception e) {
                return DownloadResult.failure("Decryption failed: " + e.getMessage());
            }
        }
        
        return result;
    }
}
```

### 4. Tích hợp với java-base-observability

#### 4.1 Metrics Integration

```java
// Tích hợp với Micrometer metrics
package io.github.base.file.api.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.MeterRegistry;
import io.github.base.file.api.storage.FileStorage;

public class MetricsFileStorage implements FileStorage {
    
    private final FileStorage delegate;
    private final Counter uploadCounter;
    private final Counter downloadCounter;
    private final Counter errorCounter;
    private final Timer uploadTimer;
    private final Timer downloadTimer;
    
    public MetricsFileStorage(FileStorage delegate, MeterRegistry meterRegistry) {
        this.delegate = delegate;
        
        this.uploadCounter = Counter.builder("file.upload.total")
            .description("Total number of file uploads")
            .register(meterRegistry);
            
        this.downloadCounter = Counter.builder("file.download.total")
            .description("Total number of file downloads")
            .register(meterRegistry);
            
        this.errorCounter = Counter.builder("file.operations.errors")
            .description("Total number of file operation errors")
            .register(meterRegistry);
            
        this.uploadTimer = Timer.builder("file.upload.duration")
            .description("File upload duration")
            .register(meterRegistry);
            
        this.downloadTimer = Timer.builder("file.download.duration")
            .description("File download duration")
            .register(meterRegistry);
    }

    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        Timer.Sample sample = Timer.start();
        
        try {
            UploadResult result = delegate.upload(fileId, content, metadata);
            
            uploadCounter.increment();
            sample.stop(uploadTimer);
            
            if (!result.success()) {
                errorCounter.increment();
            }
            
            return result;
            
        } catch (Exception e) {
            errorCounter.increment();
            sample.stop(uploadTimer);
            throw e;
        }
    }

    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> targetType) {
        Timer.Sample sample = Timer.start();
        
        try {
            DownloadResult<T> result = delegate.download(fileId, targetType);
            
            downloadCounter.increment();
            sample.stop(downloadTimer);
            
            if (!result.success()) {
                errorCounter.increment();
            }
            
            return result;
            
        } catch (Exception e) {
            errorCounter.increment();
            sample.stop(downloadTimer);
            throw e;
        }
    }
}
```

#### 4.2 Tracing Integration

```java
// Tích hợp với OpenTelemetry tracing
package io.github.base.file.api.observability;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.github.base.file.api.storage.FileStorage;

public class TracingFileStorage implements FileStorage {
    
    private final FileStorage delegate;
    private final Tracer tracer;
    
    public TracingFileStorage(FileStorage delegate, Tracer tracer) {
        this.delegate = delegate;
        this.tracer = tracer;
    }

    @Override
    public UploadResult upload(String fileId, Object content, FileMetadata metadata) {
        Span span = tracer.spanBuilder("file.upload")
            .setAttribute("file.id", fileId)
            .setAttribute("file.name", metadata.fileName())
            .setAttribute("file.size", metadata.size())
            .setAttribute("file.content_type", metadata.contentType())
            .startSpan();
            
        try (var scope = span.makeCurrent()) {
            UploadResult result = delegate.upload(fileId, content, metadata);
            
            span.setAttribute("file.upload.success", result.success());
            if (result.success()) {
                span.setAttribute("file.location", result.location());
            } else {
                span.setAttribute("file.error", result.errorMessage());
                span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, result.errorMessage());
            }
            
            return result;
            
        } catch (Exception e) {
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
```

### 5. Composite Pattern - Kết hợp tất cả

```java
// Kết hợp tất cả các tính năng
package io.github.base.file.api.composite;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.resilience.ResilientFileStorage;
import io.github.base.file.api.logging.LoggingFileStorage;
import io.github.base.file.api.cache.CachedFileStorage;
import io.github.base.file.api.security.SecureFileStorage;
import io.github.base.file.api.observability.MetricsFileStorage;
import io.github.base.file.api.observability.TracingFileStorage;

public class CompositeFileStorageBuilder {
    
    public static FileStorage build(FileStorage baseStorage, 
                                  CacheManager cacheManager,
                                  AccessControl accessControl,
                                  MeterRegistry meterRegistry,
                                  Tracer tracer) {
        
        // Layer 1: Base storage
        FileStorage storage = baseStorage;
        
        // Layer 2: Security (innermost)
        storage = new SecureFileStorage(storage, accessControl);
        
        // Layer 3: Caching
        storage = new CachedFileStorage(storage, cacheManager);
        
        // Layer 4: Resilience
        storage = new ResilientFileStorage(storage);
        
        // Layer 5: Observability
        storage = new MetricsFileStorage(storage, meterRegistry);
        storage = new TracingFileStorage(storage, tracer);
        
        // Layer 6: Logging (outermost)
        storage = new LoggingFileStorage(storage);
        
        return storage;
    }
}
```

### 6. Configuration Example

```yaml
# application.yml
base:
  file:
    storage:
      default-provider: s3
      providers:
        local:
          base-path: /tmp/files
        s3:
          bucket-name: my-bucket
          region: us-east-1
        gcs:
          bucket-name: my-gcs-bucket
          project-id: my-project
      cache:
        enabled: true
        max-size: 100MB
      security:
        encryption:
          enabled: true
          algorithm: AES-256-GCM
        access-control:
          enabled: true
      observability:
        metrics:
          enabled: true
        tracing:
          enabled: true
```

Với cách tích hợp này, module `java-base-file` sẽ tận dụng tối đa các tính năng từ các module hiện có, đảm bảo tính nhất quán và tái sử dụng code trong toàn bộ hệ thống base libraries.

