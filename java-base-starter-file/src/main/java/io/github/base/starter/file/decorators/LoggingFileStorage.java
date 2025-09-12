package io.github.base.starter.file.decorators;

import io.github.base.file.api.exception.FileStorageException;
import io.github.base.file.api.model.DownloadResult;
import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.storage.FileStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Decorator that adds structured logging to file storage operations.
 * 
 * <p>This decorator adds comprehensive logging to all file storage operations
 * including operation timing, success/failure status, and relevant metadata.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class LoggingFileStorage implements FileStorage {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingFileStorage.class);
    
    private final FileStorage delegate;
    
    /**
     * Creates a new logging file storage decorator.
     * 
     * @param delegate the file storage to decorate
     */
    public LoggingFileStorage(FileStorage delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public UploadResult upload(String fileId, File file, FileMetadata metadata) throws FileStorageException {
        return executeWithLogging("upload", fileId, () -> delegate.upload(fileId, file, metadata));
    }
    
    @Override
    public UploadResult upload(String fileId, byte[] data, FileMetadata metadata) throws FileStorageException {
        return executeWithLogging("upload", fileId, () -> delegate.upload(fileId, data, metadata));
    }
    
    @Override
    public UploadResult upload(String fileId, InputStream inputStream, FileMetadata metadata) throws FileStorageException {
        return executeWithLogging("upload", fileId, () -> delegate.upload(fileId, inputStream, metadata));
    }
    
    @Override
    public DownloadResult<File> downloadAsFile(String fileId) throws FileStorageException {
        return executeWithLogging("downloadAsFile", fileId, () -> delegate.downloadAsFile(fileId));
    }
    
    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> type) throws FileStorageException {
        return executeWithLogging("download", fileId, () -> delegate.download(fileId, type));
    }
    
    @Override
    public boolean delete(String fileId) throws FileStorageException {
        return executeWithLogging("delete", fileId, () -> delegate.delete(fileId));
    }
    
    @Override
    public boolean exists(String fileId) throws FileStorageException {
        return executeWithLogging("exists", fileId, () -> delegate.exists(fileId));
    }
    
    @Override
    public FileMetadata getMetadata(String fileId) throws FileStorageException {
        return executeWithLogging("getMetadata", fileId, () -> delegate.getMetadata(fileId));
    }
    
    @Override
    public List<FileMetadata> listFiles(String prefix, int maxResults) throws FileStorageException {
        return executeWithLogging("listFiles", prefix, () -> delegate.listFiles(prefix, maxResults));
    }
    
    @Override
    public Map<String, Object> listFiles(String prefix, String continuationToken, int maxResults) throws FileStorageException {
        return executeWithLogging("listFiles", prefix, () -> delegate.listFiles(prefix, continuationToken, maxResults));
    }
    
    @Override
    public String generatePresignedUrl(String fileId, int expirationMinutes) throws FileStorageException {
        return executeWithLogging("generatePresignedUrl", fileId, () -> delegate.generatePresignedUrl(fileId, expirationMinutes));
    }
    
    @Override
    public CompletableFuture<UploadResult> uploadAsync(String fileId, File file, FileMetadata metadata) throws FileStorageException {
        return executeWithLoggingAsync("uploadAsync", fileId, () -> delegate.uploadAsync(fileId, file, metadata));
    }
    
    @Override
    public CompletableFuture<UploadResult> uploadAsync(String fileId, byte[] data, FileMetadata metadata) throws FileStorageException {
        return executeWithLoggingAsync("uploadAsync", fileId, () -> delegate.uploadAsync(fileId, data, metadata));
    }
    
    @Override
    public CompletableFuture<DownloadResult<byte[]>> downloadAsync(String fileId) throws FileStorageException {
        return executeWithLoggingAsync("downloadAsync", fileId, () -> delegate.downloadAsync(fileId));
    }
    
    @Override
    public String getProviderName() {
        return delegate.getProviderName();
    }
    
    @Override
    public Map<String, Boolean> getCapabilities() {
        return delegate.getCapabilities();
    }
    
    /**
     * Executes an operation with structured logging.
     * 
     * @param operationName the name of the operation
     * @param fileId the file ID for context
     * @param operation the operation to execute
     * @param <T> the return type
     * @return the result of the operation
     * @throws FileStorageException if the operation fails
     */
    private <T> T executeWithLogging(String operationName, String fileId, Supplier<T> operation) throws FileStorageException {
        long startTime = System.currentTimeMillis();
        
        try {
            // Set MDC for structured logging
            MDC.put("operation", operationName);
            MDC.put("fileId", fileId);
            
            logger.debug("Starting {} operation for file: {}", operationName, fileId);
            
            T result = operation.get();
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Operation {} completed successfully for file: {} in {}ms", operationName, fileId, duration);
            
            return result;
            
        } catch (FileStorageException e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Operation {} failed for file: {} after {}ms - {}", operationName, fileId, duration, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Operation {} failed unexpectedly for file: {} after {}ms", operationName, fileId, duration, e);
            throw new FileStorageException("Operation failed: " + operationName, e);
        } finally {
            // Clear MDC
            MDC.remove("operation");
            MDC.remove("fileId");
        }
    }
    
    /**
     * Executes an async operation with structured logging.
     * 
     * @param operationName the name of the operation
     * @param fileId the file ID for context
     * @param operation the async operation to execute
     * @param <T> the return type
     * @return the result of the operation
     * @throws FileStorageException if the operation fails
     */
    private <T> CompletableFuture<T> executeWithLoggingAsync(String operationName, String fileId, Supplier<CompletableFuture<T>> operation) throws FileStorageException {
        long startTime = System.currentTimeMillis();
        
        try {
            // Set MDC for structured logging
            MDC.put("operation", operationName);
            MDC.put("fileId", fileId);
            
            logger.debug("Starting async {} operation for file: {}", operationName, fileId);
            
            CompletableFuture<T> future = operation.get();
            
            // Add completion logging
            future.whenComplete((result, throwable) -> {
                long duration = System.currentTimeMillis() - startTime;
                if (throwable != null) {
                    logger.error("Async operation {} failed for file: {} after {}ms - {}", operationName, fileId, duration, throwable.getMessage(), throwable);
                } else {
                    logger.info("Async operation {} completed successfully for file: {} in {}ms", operationName, fileId, duration);
                }
            });
            
            return future;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Async operation {} failed unexpectedly for file: {} after {}ms", operationName, fileId, duration, e);
            throw new FileStorageException("Async operation failed: " + operationName, e);
        } finally {
            // Clear MDC
            MDC.remove("operation");
            MDC.remove("fileId");
        }
    }
}