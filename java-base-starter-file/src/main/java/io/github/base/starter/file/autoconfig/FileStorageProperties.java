package io.github.base.starter.file.autoconfig;

import io.github.base.starter.file.constants.FileStorageConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * Configuration properties for file storage services.
 * 
 * <p>This class provides configuration properties for various file storage
 * providers including local, S3, GCS, and Azure Blob Storage.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@ConfigurationProperties(prefix = "base.file.storage")
public class FileStorageProperties {
    
    /**
     * Default storage provider to use.
     */
    private String defaultProvider = FileStorageConstants.LOCAL_PROVIDER_NAME;
    
    /**
     * Provider-specific configurations.
     */
    @NestedConfigurationProperty
    private Map<String, ProviderConfig> providers;
    
    /**
     * Enable file storage auto-configuration.
     */
    private boolean enabled = true;
    
    /**
     * Enable metrics collection.
     */
    private boolean metricsEnabled = true;
    
    /**
     * Enable caching for file metadata.
     */
    private boolean cacheEnabled = true;
    
    /**
     * Cache TTL in seconds.
     */
    private long cacheTtlSeconds = FileStorageConstants.DEFAULT_CACHE_TTL_SECONDS;
    
    /**
     * Maximum cache size.
     */
    private int maxCacheSize = FileStorageConstants.DEFAULT_MAX_CACHE_SIZE;
    
    /**
     * Provider-specific configuration.
     */
    public static class ProviderConfig {
        
        /**
         * Provider type (local, s3, gcs, azure).
         */
        private String type;
        
        /**
         * Provider-specific properties.
         */
        private Map<String, String> properties;
        
        /**
         * Enable this provider.
         */
        private boolean enabled = true;
        
        /**
         * Provider priority (higher = more preferred).
         */
        private int priority = 0;
        
        // Getters and setters
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public Map<String, String> getProperties() {
            return properties;
        }
        
        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public int getPriority() {
            return priority;
        }
        
        public void setPriority(int priority) {
            this.priority = priority;
        }
    }
    
    // Getters and setters
    public String getDefaultProvider() {
        return defaultProvider;
    }
    
    public void setDefaultProvider(String defaultProvider) {
        this.defaultProvider = defaultProvider;
    }
    
    public Map<String, ProviderConfig> getProviders() {
        return providers;
    }
    
    public void setProviders(Map<String, ProviderConfig> providers) {
        this.providers = providers;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }
    
    public void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }
    
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }
    
    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }
    
    public long getCacheTtlSeconds() {
        return cacheTtlSeconds;
    }
    
    public void setCacheTtlSeconds(long cacheTtlSeconds) {
        this.cacheTtlSeconds = cacheTtlSeconds;
    }
    
    public int getMaxCacheSize() {
        return maxCacheSize;
    }
    
    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }
}
