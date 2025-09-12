package io.github.base.starter.file.decorators;

import io.github.base.file.api.exception.FileStorageException;
import io.github.base.file.api.model.DownloadResult;
import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.storage.FileStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Decorator that adds caching to file storage operations.
 *
 * <p>This decorator caches file metadata and existence checks to improve
 * performance and reduce the number of calls to the underlying storage.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class CachedFileStorage implements FileStorage {

    private static final Logger logger = LoggerFactory.getLogger(CachedFileStorage.class);

    private final FileStorage delegate;
    private final Map<String, FileMetadata> metadataCache;
    private final Map<String, Boolean> existsCache;

    /**
     * Creates a new cached file storage decorator.
     *
     * @param delegate the file storage to decorate
     */
    public CachedFileStorage(FileStorage delegate) {
        this.delegate = delegate;
        this.metadataCache = new ConcurrentHashMap<>();
        this.existsCache = new ConcurrentHashMap<>();
    }

    @Override
    public UploadResult upload(String fileId, File file, FileMetadata metadata) throws FileStorageException {
        UploadResult result = delegate.upload(fileId, file, metadata);
        if (result.success()) {
            // Cache the metadata
            metadataCache.put(fileId, metadata);
            existsCache.put(fileId, true);
        }
        return result;
    }

    @Override
    public UploadResult upload(String fileId, byte[] data, FileMetadata metadata) throws FileStorageException {
        UploadResult result = delegate.upload(fileId, data, metadata);
        if (result.success()) {
            // Cache the metadata
            metadataCache.put(fileId, metadata);
            existsCache.put(fileId, true);
        }
        return result;
    }

    @Override
    public UploadResult upload(String fileId, InputStream inputStream, FileMetadata metadata) throws FileStorageException {
        UploadResult result = delegate.upload(fileId, inputStream, metadata);
        if (result.success()) {
            // Cache the metadata
            metadataCache.put(fileId, metadata);
            existsCache.put(fileId, true);
        }
        return result;
    }

    @Override
    public DownloadResult<File> downloadAsFile(String fileId) throws FileStorageException {
        return delegate.downloadAsFile(fileId);
    }

    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> type) throws FileStorageException {
        return delegate.download(fileId, type);
    }

    @Override
    public boolean delete(String fileId) throws FileStorageException {
        boolean result = delegate.delete(fileId);
        if (result) {
            // Remove from cache
            metadataCache.remove(fileId);
            existsCache.remove(fileId);
        }
        return result;
    }

    @Override
    public boolean exists(String fileId) throws FileStorageException {
        // Check cache first
        Boolean cached = existsCache.get(fileId);
        if (cached != null) {
            logger.debug("Cache hit for exists check: {}", fileId);
            return cached;
        }

        // Not in cache, check delegate
        boolean exists = delegate.exists(fileId);
        existsCache.put(fileId, exists);
        return exists;
    }

    @Override
    public FileMetadata getMetadata(String fileId) throws FileStorageException {
        // Check cache first
        FileMetadata cached = metadataCache.get(fileId);
        if (cached != null) {
            logger.debug("Cache hit for metadata: {}", fileId);
            return cached;
        }

        // Not in cache, get from delegate
        FileMetadata metadata = delegate.getMetadata(fileId);
        if (metadata != null) {
            metadataCache.put(fileId, metadata);
        }
        return metadata;
    }

    @Override
    public List<FileMetadata> listFiles(String prefix, int maxResults) throws FileStorageException {
        return delegate.listFiles(prefix, maxResults);
    }

    @Override
    public Map<String, Object> listFiles(String prefix, String continuationToken, int maxResults) throws FileStorageException {
        return delegate.listFiles(prefix, continuationToken, maxResults);
    }

    @Override
    public String generatePresignedUrl(String fileId, int expirationMinutes) throws FileStorageException {
        return delegate.generatePresignedUrl(fileId, expirationMinutes);
    }

    @Override
    public CompletableFuture<UploadResult> uploadAsync(String fileId, File file, FileMetadata metadata) throws FileStorageException {
        return delegate.uploadAsync(fileId, file, metadata);
    }

    @Override
    public CompletableFuture<UploadResult> uploadAsync(String fileId, byte[] data, FileMetadata metadata) throws FileStorageException {
        return delegate.uploadAsync(fileId, data, metadata);
    }

    @Override
    public CompletableFuture<DownloadResult<byte[]>> downloadAsync(String fileId) throws FileStorageException {
        return delegate.downloadAsync(fileId);
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
     * Clears the cache.
     */
    public void clearCache() {
        metadataCache.clear();
        existsCache.clear();
        logger.info("Cache cleared");
    }

    /**
     * Removes a specific file from the cache.
     *
     * @param fileId the file ID to remove
     */
    public void removeFromCache(String fileId) {
        metadataCache.remove(fileId);
        existsCache.remove(fileId);
        logger.debug("Removed file from cache: {}", fileId);
    }

    /**
     * Gets cache statistics.
     *
     * @return cache statistics
     */
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("metadataCacheSize", metadataCache.size());
        stats.put("existsCacheSize", existsCache.size());
        return stats;
    }
}