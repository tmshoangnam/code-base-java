package io.github.base.starter.file.autoconfig;

import io.github.base.file.FileServices;
import io.github.base.file.api.model.DownloadResult;
import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.FileType;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;
import io.github.base.file.spi.FileStorageProvider;
import io.github.base.starter.file.impl.local.LocalFileStorageProvider;
import io.github.base.starter.file.impl.s3.S3FileStorageProvider;
import io.github.base.starter.file.impl.gcs.GcsFileStorageProvider;
import io.github.base.starter.file.impl.azure.AzureFileStorageProvider;
import io.github.base.starter.file.decorators.ResilientFileStorage;
import io.github.base.starter.file.decorators.LoggingFileStorage;
import io.github.base.starter.file.decorators.CachedFileStorage;
import io.github.base.starter.file.decorators.SecureFileStorage;
import io.github.base.starter.file.decorators.MetricsFileStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Auto-configuration for file storage services.
 *
 * <p>This class provides auto-configuration for file storage services including
 * local, S3, GCS, and Azure Blob Storage providers with decorators for
 * resilience, logging, caching, security, and metrics.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@AutoConfiguration
@ConditionalOnClass(FileStorage.class)
@ConditionalOnProperty(prefix = "base.file.storage", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(FileStorageProperties.class)
public class FileStorageAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageAutoConfiguration.class);

    /**
     * Creates a file storage manager with all available providers.
     *
     * @param properties the file storage properties
     * @return the file storage manager
     */
    @Bean
    @ConditionalOnMissingBean
    public FileStorageManager fileStorageManager(FileStorageProperties properties) {
        List<FileStorageProvider> providers = FileServices.getProviders();

        // Filter enabled providers based on configuration
        List<FileStorageProvider> enabledProviders = providers.stream()
            .filter(provider -> isProviderEnabled(provider, properties))
            .toList();

        return new DefaultFileStorageManager(enabledProviders, properties);
    }

    /**
     * Creates the primary file storage bean.
     *
     * @param fileStorageManager the file storage manager
     * @param properties the file storage properties
     * @return the primary file storage
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public FileStorage fileStorage(FileStorageManager fileStorageManager, FileStorageProperties properties) {
        try {
            FileStorage storage = fileStorageManager.getStorage(properties.getDefaultProvider());

            // Apply decorators based on configuration
            if (properties.isCacheEnabled()) {
                storage = new CachedFileStorage(storage);
            }

            if (properties.isMetricsEnabled()) {
                storage = new MetricsFileStorage(storage);
            }

            storage = new LoggingFileStorage(storage);
            storage = new ResilientFileStorage(storage);
            storage = new SecureFileStorage(storage);

            return storage;
        } catch (Exception e) {
            // If default storage fails (e.g., S3 without config), try to get any available storage
            logger.warn("Failed to get default storage '{}': {}. Attempting to get any available storage.",
                properties.getDefaultProvider(), e.getMessage());

            var availableProviders = fileStorageManager.getAvailableProviders();
            for (String providerName : availableProviders) {
                try {
                    FileStorage storage = fileStorageManager.getStorage(providerName);
                    if (storage != null) {
                        logger.info("Using fallback storage provider: {}", providerName);

                        // Apply decorators based on configuration
                        if (properties.isCacheEnabled()) {
                            storage = new CachedFileStorage(storage);
                        }

                        if (properties.isMetricsEnabled()) {
                            storage = new MetricsFileStorage(storage);
                        }

                        storage = new LoggingFileStorage(storage);
                        storage = new ResilientFileStorage(storage);
                        storage = new SecureFileStorage(storage);

                        return storage;
                    }
                } catch (Exception ex) {
                    logger.debug("Provider {} also failed: {}", providerName, ex.getMessage());
                }
            }

            // If all providers fail, throw the original exception
            throw new IllegalStateException("No file storage providers available. " + e.getMessage(), e);
        }
    }

    /**
     * Creates a local file storage provider.
     *
     * @return the local file storage provider
     */
    @Bean
    @ConditionalOnMissingBean
    public LocalFileStorageProvider localFileStorageProvider() {
        return new LocalFileStorageProvider();
    }

    /**
     * Creates an S3 file storage provider.
     *
     * @return the S3 file storage provider
     */
    @Bean
    @ConditionalOnMissingBean
    public S3FileStorageProvider s3FileStorageProvider() {
        return new S3FileStorageProvider();
    }

    /**
     * Creates a GCS file storage provider.
     *
     * @return the GCS file storage provider
     */
    @Bean
    @ConditionalOnMissingBean
    public GcsFileStorageProvider gcsFileStorageProvider() {
        return new GcsFileStorageProvider();
    }

    /**
     * Creates an Azure file storage provider.
     *
     * @return the Azure file storage provider
     */
    @Bean
    @ConditionalOnMissingBean
    public AzureFileStorageProvider azureFileStorageProvider() {
        return new AzureFileStorageProvider();
    }

    /**
     * Checks if a provider is enabled based on configuration.
     *
     * @param provider the provider to check
     * @param properties the file storage properties
     * @return true if the provider is enabled
     */
    private boolean isProviderEnabled(FileStorageProvider provider, FileStorageProperties properties) {
        if (properties.getProviders() == null) {
            return true; // All providers enabled by default
        }

        FileStorageProperties.ProviderConfig config = properties.getProviders().get(provider.getProviderName());
        return config == null || config.isEnabled();
    }

    /**
     * Default implementation of FileStorageManager.
     */
    private static class DefaultFileStorageManager implements FileStorageManager {
        private final List<FileStorageProvider> providers;
        private final FileStorageProperties properties;
        private final Map<String, FileStorage> storageCache;

        public DefaultFileStorageManager(List<FileStorageProvider> providers, FileStorageProperties properties) {
            this.providers = providers;
            this.properties = properties;
            this.storageCache = new java.util.concurrent.ConcurrentHashMap<>();
        }

        @Override
        public FileStorage getDefaultStorage() {
            return getStorage(properties.getDefaultProvider());
        }

        @Override
        public FileStorage getStorage(String providerName) {
            return storageCache.computeIfAbsent(providerName, name -> {
                FileStorageProvider provider = providers.stream()
                    .filter(p -> p.getProviderName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Provider not found: " + name));

                FileStorageProperties.ProviderConfig config = properties.getProviders() != null
                    ? properties.getProviders().get(name)
                    : null;

                // Check if provider supports configuration-based creation
                if (config != null && config.getProperties() != null && !config.getProperties().isEmpty()) {
                    // Try to use configuration-based creation if available
                    try {
                        // Check if provider has createStorage(Map<String, String>) method
                        java.lang.reflect.Method createStorageWithConfig = provider.getClass()
                            .getMethod("createStorage", Map.class);
                        logger.debug("Using configuration-based createStorage for provider: {}", name);
                        return (FileStorage) createStorageWithConfig.invoke(provider, config.getProperties());
                    } catch (NoSuchMethodException e) {
                        logger.debug("Provider {} does not support configuration-based createStorage, falling back to parameterless method", name);
                        return provider.createStorage();
                    } catch (IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
                        logger.warn("Error invoking createStorage with configuration for provider {}: {}. Falling back to parameterless method.", name, e.getMessage());
                        // Check if the cause is related to missing AWS service providers
                        Throwable cause = e.getCause();
                        if (cause != null && (cause.getMessage().contains("ServiceConfigurationError") ||
                                             cause.getMessage().contains("not found"))) {
                            logger.warn("AWS service provider not found for S3. This is expected in test environments. Using parameterless createStorage().");
                        }
                        return provider.createStorage();
                    }
                } else {
                    logger.debug("No configuration available for provider: {}, using parameterless createStorage", name);
                    // For S3 provider, try to get configuration from environment variables as fallback
                    if ("s3".equals(name)) {
                        logger.info("S3 provider detected without configuration, attempting to use environment variables as fallback");
                    }
                    return provider.createStorage();
                }
            });
        }

        @Override
        public FileStorage getBestStorage(FileType fileType, long fileSize, String contentType) {
            return null;
        }

        @Override
        public Set<String> getAvailableProviders() {
            return providers.stream()
                .map(FileStorageProvider::getProviderName)
                .collect(Collectors.toSet());
        }

        @Override
        public boolean isProviderHealthy(String providerName) {
            return false;
        }

        @Override
        public Map<String, Boolean> getProviderHealthStatus() {
            return Collections.emptyMap();
        }

        @Override
        public UploadResult upload(String fileId, File file, FileMetadata metadata) {
            return null;
        }

        @Override
        public UploadResult upload(String fileId, byte[] data, FileMetadata metadata) {
            return null;
        }

        @Override
        public UploadResult upload(String fileId, InputStream inputStream, FileMetadata metadata) {
            return null;
        }

        @Override
        public UploadResult upload(String providerName, String fileId, File file, FileMetadata metadata) {
            return null;
        }

        @Override
        public UploadResult upload(String providerName, String fileId, byte[] data, FileMetadata metadata) {
            return null;
        }

        @Override
        public UploadResult upload(String providerName, String fileId, InputStream inputStream, FileMetadata metadata) {
            return null;
        }

        @Override
        public DownloadResult<File> downloadAsFile(String fileId) {
            return null;
        }

        @Override
        public <T> DownloadResult<T> download(String fileId, Class<T> type) {
            return null;
        }

        @Override
        public DownloadResult<File> downloadAsFile(String providerName, String fileId) {
            return null;
        }

        @Override
        public <T> DownloadResult<T> download(String providerName, String fileId, Class<T> type) {
            return null;
        }

        @Override
        public boolean delete(String fileId) {
            return false;
        }

        @Override
        public boolean delete(String providerName, String fileId) {
            return false;
        }

        @Override
        public boolean exists(String fileId) {
            return false;
        }

        @Override
        public boolean exists(String providerName, String fileId) {
            return false;
        }

        @Override
        public FileMetadata getMetadata(String fileId) {
            return null;
        }

        @Override
        public FileMetadata getMetadata(String providerName, String fileId) {
            return null;
        }

        @Override
        public List<FileMetadata> listFiles(String prefix, int maxResults) {
            return Collections.emptyList();
        }

        @Override
        public List<FileMetadata> listFiles(String providerName, String prefix, int maxResults) {
            return Collections.emptyList();
        }

        @Override
        public CompletableFuture<UploadResult> uploadAsync(String fileId, File file, FileMetadata metadata) {
            return null;
        }

        @Override
        public CompletableFuture<UploadResult> uploadAsync(String fileId, byte[] data, FileMetadata metadata) {
            return null;
        }

        @Override
        public CompletableFuture<DownloadResult<byte[]>> downloadAsync(String fileId) {
            return null;
        }

        @Override
        public String getDefaultProvider() {
            return properties.getDefaultProvider();
        }

        @Override
        public String getDefaultProviderName() {
            return properties.getDefaultProvider();
        }

        @Override
        public void setDefaultProvider(String providerName) {
            if (providers.stream().noneMatch(p -> p.getProviderName().equals(providerName))) {
                throw new IllegalArgumentException("Provider not found: " + providerName);
            }
            // Update properties
            properties.setDefaultProvider(providerName);
        }
    }
}
