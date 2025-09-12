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
import io.github.base.starter.file.factory.FileStorageFactory;
import io.github.base.starter.file.config.FileStorageConfigValidator;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.InputStream;
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

    /**
     * Creates a file storage factory.
     *
     * @return the file storage factory
     */
    @Bean
    @ConditionalOnMissingBean
    public FileStorageFactory fileStorageFactory() {
        return new FileStorageFactory();
    }

    /**
     * Creates a file storage configuration validator.
     *
     * @return the configuration validator
     */
    @Bean
    @ConditionalOnMissingBean
    public FileStorageConfigValidator fileStorageConfigValidator() {
        return new FileStorageConfigValidator();
    }

    /**
     * Creates a file storage manager with all available providers.
     *
     * @param properties the file storage properties
     * @param factory the file storage factory
     * @param validator the configuration validator
     * @return the file storage manager
     */
    @Bean
    @ConditionalOnMissingBean
    public FileStorageManager fileStorageManager(FileStorageProperties properties,
                                                FileStorageFactory factory,
                                                FileStorageConfigValidator validator) {
        // Validate configuration
        List<String> validationErrors = validator.validateConfiguration(properties);
        if (!validationErrors.isEmpty()) {
            throw new IllegalStateException("File storage configuration validation failed: " + String.join(", ", validationErrors));
        }

        // Validate environment
        List<String> environmentErrors = validator.validateEnvironment();
        if (!environmentErrors.isEmpty()) {
            throw new IllegalStateException("File storage environment validation failed: " + String.join(", ", environmentErrors));
        }

        List<FileStorageProvider> providers = FileServices.getProviders();

        // Filter enabled providers based on configuration
        List<FileStorageProvider> enabledProviders = providers.stream()
            .filter(provider -> isProviderEnabled(provider, properties))
            .toList();

        return new DefaultFileStorageManager(enabledProviders, properties, factory);
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
        private final FileStorageFactory factory;
        private final Map<String, FileStorage> storageCache;

        public DefaultFileStorageManager(List<FileStorageProvider> providers, FileStorageProperties properties, FileStorageFactory factory) {
            this.providers = providers;
            this.properties = properties;
            this.factory = factory;
            this.storageCache = new java.util.concurrent.ConcurrentHashMap<>();
        }

        @Override
        public FileStorage getDefaultStorage() {
            return getStorage(properties.getDefaultProvider());
        }

        @Override
        public FileStorage getStorage(String providerName) {
            return storageCache.computeIfAbsent(providerName, name -> {
                try {
                    return factory.createStorage(name, properties);
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to create storage for provider: " + name, e);
                }
            });
        }

        @Override
        public FileStorage getBestStorage(FileType fileType, long fileSize, String contentType) {
            // Find the best provider based on capabilities and priority
            return providers.stream()
                .filter(FileStorageProvider::isAvailable)
                .filter(provider -> {
                    Map<String, Boolean> capabilities = provider.getCapabilities();
                    return capabilities.getOrDefault("upload", false) &&
                           capabilities.getOrDefault("download", false);
                })
                .filter(provider -> {
                    long maxSize = provider.getMaxFileSize();
                    return maxSize == 0 || fileSize <= maxSize;
                })
                .max((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()))
                .map(FileStorageProvider::createStorage)
                .orElse(getDefaultStorage());
        }

        @Override
        public Set<String> getAvailableProviders() {
            return providers.stream()
                .map(FileStorageProvider::getProviderName)
                .collect(Collectors.toSet());
        }

        @Override
        public boolean isProviderHealthy(String providerName) {
            try {
                FileStorageProvider provider = providers.stream()
                    .filter(p -> p.getProviderName().equals(providerName))
                    .findFirst()
                    .orElse(null);

                if (provider == null) {
                    return false;
                }

                return provider.isAvailable();
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public Map<String, Boolean> getProviderHealthStatus() {
            return providers.stream()
                .collect(Collectors.toMap(
                    FileStorageProvider::getProviderName,
                    provider -> {
                        try {
                            return provider.isAvailable();
                        } catch (Exception e) {
                            return false;
                        }
                    }
                ));
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
