package io.github.base.starter.file.factory;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.spi.FileStorageProvider;
import io.github.base.starter.file.autoconfig.FileStorageProperties;
import io.github.base.starter.file.constants.FileStorageConstants;
import io.github.base.starter.file.impl.local.LocalFileStorageProvider;
import io.github.base.starter.file.impl.s3.S3FileStorageProvider;
import io.github.base.starter.file.impl.gcs.GcsFileStorageProvider;
import io.github.base.starter.file.impl.azure.AzureFileStorageProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for creating file storage instances.
 * 
 * <p>This factory provides a centralized way to create file storage
 * instances with proper configuration and error handling.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@Component
public class FileStorageFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(FileStorageFactory.class);
    
    private final Map<String, FileStorageProvider> providerCache = new ConcurrentHashMap<>();
    private final Map<String, FileStorage> storageCache = new ConcurrentHashMap<>();
    
    /**
     * Creates a file storage instance for the specified provider.
     * 
     * @param providerName the provider name
     * @param properties the file storage properties
     * @return the file storage instance
     * @throws IllegalArgumentException if provider is not supported
     * @throws IllegalStateException if provider cannot be created
     */
    public FileStorage createStorage(String providerName, FileStorageProperties properties) {
        if (providerName == null || providerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Provider name cannot be null or empty");
        }
        
        String key = providerName.trim();
        
        return storageCache.computeIfAbsent(key, name -> {
            logger.info("Creating file storage for provider: {}", name);
            
            try {
                FileStorageProvider provider = getProvider(name);
                
                if (!provider.isAvailable()) {
                    throw new IllegalStateException("Provider is not available: " + name);
                }
                
                FileStorage storage = provider.createStorage();
                logger.info("Successfully created file storage for provider: {}", name);
                return storage;
                
            } catch (Exception e) {
                logger.error("Failed to create file storage for provider: " + name, e);
                throw new IllegalStateException("Failed to create file storage for provider: " + name, e);
            }
        });
    }
    
    /**
     * Gets the file storage provider for the specified name.
     * 
     * @param providerName the provider name
     * @return the file storage provider
     * @throws IllegalArgumentException if provider is not supported
     */
    public FileStorageProvider getProvider(String providerName) {
        if (providerName == null || providerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Provider name cannot be null or empty");
        }
        
        String key = providerName.trim();
        
        return providerCache.computeIfAbsent(key, name -> {
            logger.debug("Creating provider for: {}", name);
            
            switch (name) {
                case FileStorageConstants.LOCAL_PROVIDER_NAME:
                    return new LocalFileStorageProvider();
                case FileStorageConstants.S3_PROVIDER_NAME:
                    return new S3FileStorageProvider();
                case FileStorageConstants.GCS_PROVIDER_NAME:
                    return new GcsFileStorageProvider();
                case FileStorageConstants.AZURE_PROVIDER_NAME:
                    return new AzureFileStorageProvider();
                default:
                    throw new IllegalArgumentException("Unsupported provider: " + name);
            }
        });
    }
    
    /**
     * Gets all available providers.
     * 
     * @return map of provider names to providers
     */
    public Map<String, FileStorageProvider> getAllProviders() {
        Map<String, FileStorageProvider> providers = new HashMap<>();
        
        providers.put(FileStorageConstants.LOCAL_PROVIDER_NAME, new LocalFileStorageProvider());
        providers.put(FileStorageConstants.S3_PROVIDER_NAME, new S3FileStorageProvider());
        providers.put(FileStorageConstants.GCS_PROVIDER_NAME, new GcsFileStorageProvider());
        providers.put(FileStorageConstants.AZURE_PROVIDER_NAME, new AzureFileStorageProvider());
        
        return providers;
    }
    
    /**
     * Clears the storage cache.
     */
    public void clearCache() {
        logger.info("Clearing file storage cache");
        storageCache.clear();
    }
    
    /**
     * Clears the provider cache.
     */
    public void clearProviderCache() {
        logger.info("Clearing file storage provider cache");
        providerCache.clear();
    }
    
    /**
     * Gets cache statistics.
     * 
     * @return cache statistics
     */
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("storageCacheSize", storageCache.size());
        stats.put("providerCacheSize", providerCache.size());
        stats.put("cachedProviders", storageCache.keySet());
        return stats;
    }
}
