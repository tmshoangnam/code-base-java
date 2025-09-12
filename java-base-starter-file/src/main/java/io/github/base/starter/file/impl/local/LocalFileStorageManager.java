package io.github.base.starter.file.impl.local;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;
import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.model.DownloadResult;
import io.github.base.starter.file.constants.FileStorageConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Local file storage manager implementation.
 *
 * <p>This manager provides a simple implementation of FileStorageManager
 * that manages a single local file storage instance.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class LocalFileStorageManager implements FileStorageManager {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileStorageManager.class);

    private final Map<String, FileStorage> storageInstances;
    private final String defaultProvider;

    /**
     * Creates a new local file storage manager.
     *
     * @param defaultProvider the default provider name
     */
    public LocalFileStorageManager(String defaultProvider) {
        this.storageInstances = new ConcurrentHashMap<>();
        this.defaultProvider = defaultProvider;
    }

    @Override
    public FileStorage getStorage(String providerName) {
        return storageInstances.computeIfAbsent(providerName, name -> {
            logger.info("Creating storage instance for provider: {}", name);

            // For local storage, we create a new instance with default configuration
            if (FileStorageConstants.LOCAL_PROVIDER_NAME.equals(name)) {
                LocalFileStorageProvider provider = new LocalFileStorageProvider();
                return provider.createStorage();
            }

            throw new IllegalArgumentException("Unsupported provider: " + name);
        });
    }

    @Override
    public FileStorage getDefaultStorage() {
        return getStorage(defaultProvider);
    }

    @Override
    public FileStorage getBestStorage(io.github.base.file.api.model.FileType fileType, long fileSize, String contentType) {
        // For local storage, we always return the default storage
        return getDefaultStorage();
    }

    @Override
    public Set<String> getAvailableProviders() {
        return Set.of(FileStorageConstants.LOCAL_PROVIDER_NAME);
    }

    @Override
    public boolean isProviderHealthy(String providerName) {
        return FileStorageConstants.LOCAL_PROVIDER_NAME.equals(providerName);
    }

    @Override
    public Map<String, Boolean> getProviderHealthStatus() {
        return Map.of(FileStorageConstants.LOCAL_PROVIDER_NAME, true);
    }

    @Override
    public UploadResult upload(String fileId, File file, FileMetadata metadata) {
        return getDefaultStorage().upload(fileId, file, metadata);
    }

    @Override
    public UploadResult upload(String fileId, byte[] data, FileMetadata metadata) {
        return getDefaultStorage().upload(fileId, data, metadata);
    }

    @Override
    public UploadResult upload(String fileId, InputStream inputStream, FileMetadata metadata) {
        return getDefaultStorage().upload(fileId, inputStream, metadata);
    }

    @Override
    public UploadResult upload(String providerName, String fileId, File file, FileMetadata metadata) {
        return getStorage(providerName).upload(fileId, file, metadata);
    }

    @Override
    public UploadResult upload(String providerName, String fileId, byte[] data, FileMetadata metadata) {
        return getStorage(providerName).upload(fileId, data, metadata);
    }

    @Override
    public UploadResult upload(String providerName, String fileId, InputStream inputStream, FileMetadata metadata) {
        return getStorage(providerName).upload(fileId, inputStream, metadata);
    }

    @Override
    public DownloadResult<File> downloadAsFile(String fileId) {
        return getDefaultStorage().downloadAsFile(fileId);
    }

    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> type) {
        return getDefaultStorage().download(fileId, type);
    }

    @Override
    public DownloadResult<File> downloadAsFile(String providerName, String fileId) {
        return getStorage(providerName).downloadAsFile(fileId);
    }

    @Override
    public <T> DownloadResult<T> download(String providerName, String fileId, Class<T> type) {
        return getStorage(providerName).download(fileId, type);
    }

    @Override
    public boolean delete(String fileId) {
        return getDefaultStorage().delete(fileId);
    }

    @Override
    public boolean delete(String providerName, String fileId) {
        return getStorage(providerName).delete(fileId);
    }

    @Override
    public boolean exists(String fileId) {
        return getDefaultStorage().exists(fileId);
    }

    @Override
    public boolean exists(String providerName, String fileId) {
        return getStorage(providerName).exists(fileId);
    }

    @Override
    public FileMetadata getMetadata(String fileId) {
        return getDefaultStorage().getMetadata(fileId);
    }

    @Override
    public FileMetadata getMetadata(String providerName, String fileId) {
        return getStorage(providerName).getMetadata(fileId);
    }

    @Override
    public List<FileMetadata> listFiles(String prefix, int maxResults) {
        return getDefaultStorage().listFiles(prefix, maxResults);
    }

    @Override
    public List<FileMetadata> listFiles(String providerName, String prefix, int maxResults) {
        return getStorage(providerName).listFiles(prefix, maxResults);
    }

    @Override
    public CompletableFuture<UploadResult> uploadAsync(String fileId, File file, FileMetadata metadata) {
        return getDefaultStorage().uploadAsync(fileId, file, metadata);
    }

    @Override
    public CompletableFuture<UploadResult> uploadAsync(String fileId, byte[] data, FileMetadata metadata) {
        return getDefaultStorage().uploadAsync(fileId, data, metadata);
    }

    @Override
    public CompletableFuture<DownloadResult<byte[]>> downloadAsync(String fileId) {
        return getDefaultStorage().downloadAsync(fileId);
    }

    @Override
    public String getDefaultProvider() {
        return null;
    }

    @Override
    public String getDefaultProviderName() {
        return defaultProvider;
    }

    @Override
    public void setDefaultProvider(String providerName) {
        if (providerName == null || providerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Provider name cannot be null or empty");
        }
        if (!FileStorageConstants.LOCAL_PROVIDER_NAME.equals(providerName)) {
            throw new IllegalStateException("Only 'local' provider is supported");
        }
        // For local storage manager, we don't change the default provider
        logger.info("Default provider is already set to: {}", defaultProvider);
    }

    /**
     * Adds a storage instance for a specific provider.
     *
     * @param providerName the provider name
     * @param storage the storage instance
     */
    public void addStorage(String providerName, FileStorage storage) {
        storageInstances.put(providerName, storage);
        logger.info("Added storage instance for provider: {}", providerName);
    }

    /**
     * Removes a storage instance for a specific provider.
     *
     * @param providerName the provider name
     * @return the removed storage instance, or null if not found
     */
    public FileStorage removeStorage(String providerName) {
        FileStorage removed = storageInstances.remove(providerName);
        if (removed != null) {
            logger.info("Removed storage instance for provider: {}", providerName);
        }
        return removed;
    }

    /**
     * Gets the number of storage instances.
     *
     * @return the number of storage instances
     */
    public int getStorageCount() {
        return storageInstances.size();
    }

    /**
     * Checks if a storage instance exists for the given provider.
     *
     * @param providerName the provider name
     * @return true if the storage instance exists
     */
    public boolean hasStorage(String providerName) {
        return storageInstances.containsKey(providerName);
    }

    /**
     * Clears all storage instances.
     */
    public void clearStorage() {
        storageInstances.clear();
        logger.info("Cleared all storage instances");
    }
}
