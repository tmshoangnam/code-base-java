package io.github.base.starter.file.config;

import io.github.base.starter.file.autoconfig.FileStorageProperties;
import io.github.base.starter.file.constants.FileStorageConstants;
import io.github.base.starter.file.impl.local.LocalFileStorageProvider;
import io.github.base.starter.file.impl.s3.S3FileStorageProvider;
import io.github.base.starter.file.impl.gcs.GcsFileStorageProvider;
import io.github.base.starter.file.impl.azure.AzureFileStorageProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Validator for file storage configuration.
 * 
 * <p>This class provides validation for file storage configuration
 * including provider configuration validation and environment checks.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@Component
public class FileStorageConfigValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(FileStorageConfigValidator.class);
    
    /**
     * Validates the file storage configuration.
     * 
     * @param properties the file storage properties
     * @return list of validation errors, empty if valid
     */
    public List<String> validateConfiguration(FileStorageProperties properties) {
        List<String> errors = new ArrayList<>();
        
        if (properties == null) {
            errors.add("File storage properties cannot be null");
            return errors;
        }
        
        // Validate default provider
        String defaultProvider = properties.getDefaultProvider();
        if (defaultProvider == null || defaultProvider.trim().isEmpty()) {
            errors.add("Default provider cannot be null or empty");
        } else if (!isValidProviderName(defaultProvider)) {
            errors.add("Invalid default provider: " + defaultProvider);
        }
        
        // Validate provider configurations
        if (properties.getProviders() != null) {
            for (Map.Entry<String, FileStorageProperties.ProviderConfig> entry : properties.getProviders().entrySet()) {
                String providerName = entry.getKey();
                FileStorageProperties.ProviderConfig config = entry.getValue();
                
                if (!isValidProviderName(providerName)) {
                    errors.add("Invalid provider name: " + providerName);
                    continue;
                }
                
                // Validate provider-specific configuration
                List<String> providerErrors = validateProviderConfiguration(providerName, config);
                errors.addAll(providerErrors);
            }
        }
        
        // Validate cache configuration
        if (properties.getCacheTtlSeconds() < 0) {
            errors.add("Cache TTL cannot be negative");
        }
        
        if (properties.getMaxCacheSize() < 0) {
            errors.add("Maximum cache size cannot be negative");
        }
        
        return errors;
    }
    
    /**
     * Validates provider-specific configuration.
     * 
     * @param providerName the provider name
     * @param config the provider configuration
     * @return list of validation errors
     */
    private List<String> validateProviderConfiguration(String providerName, FileStorageProperties.ProviderConfig config) {
        List<String> errors = new ArrayList<>();
        
        try {
            switch (providerName) {
                case FileStorageConstants.LOCAL_PROVIDER_NAME:
                    errors.addAll(validateLocalProviderConfig(config));
                    break;
                case FileStorageConstants.S3_PROVIDER_NAME:
                    errors.addAll(validateS3ProviderConfig(config));
                    break;
                case FileStorageConstants.GCS_PROVIDER_NAME:
                    errors.addAll(validateGcsProviderConfig(config));
                    break;
                case FileStorageConstants.AZURE_PROVIDER_NAME:
                    errors.addAll(validateAzureProviderConfig(config));
                    break;
                default:
                    errors.add("Unknown provider: " + providerName);
            }
        } catch (Exception e) {
            logger.error("Error validating provider configuration for: " + providerName, e);
            errors.add("Error validating provider configuration: " + e.getMessage());
        }
        
        return errors;
    }
    
    /**
     * Validates local provider configuration.
     */
    private List<String> validateLocalProviderConfig(FileStorageProperties.ProviderConfig config) {
        List<String> errors = new ArrayList<>();
        
        if (config.getConfiguration() != null) {
            LocalFileStorageProvider provider = new LocalFileStorageProvider();
            errors.addAll(provider.validateConfiguration(config.getConfiguration()));
        }
        
        return errors;
    }
    
    /**
     * Validates S3 provider configuration.
     */
    private List<String> validateS3ProviderConfig(FileStorageProperties.ProviderConfig config) {
        List<String> errors = new ArrayList<>();
        
        if (config.getConfiguration() != null) {
            S3FileStorageProvider provider = new S3FileStorageProvider();
            errors.addAll(provider.validateConfiguration(config.getConfiguration()));
        }
        
        return errors;
    }
    
    /**
     * Validates GCS provider configuration.
     */
    private List<String> validateGcsProviderConfig(FileStorageProperties.ProviderConfig config) {
        List<String> errors = new ArrayList<>();
        
        if (config.getConfiguration() != null) {
            GcsFileStorageProvider provider = new GcsFileStorageProvider();
            errors.addAll(provider.validateConfiguration(config.getConfiguration()));
        }
        
        return errors;
    }
    
    /**
     * Validates Azure provider configuration.
     */
    private List<String> validateAzureProviderConfig(FileStorageProperties.ProviderConfig config) {
        List<String> errors = new ArrayList<>();
        
        if (config.getConfiguration() != null) {
            AzureFileStorageProvider provider = new AzureFileStorageProvider();
            errors.addAll(provider.validateConfiguration(config.getConfiguration()));
        }
        
        return errors;
    }
    
    /**
     * Checks if a provider name is valid.
     * 
     * @param providerName the provider name
     * @return true if valid
     */
    private boolean isValidProviderName(String providerName) {
        return FileStorageConstants.LOCAL_PROVIDER_NAME.equals(providerName) ||
               FileStorageConstants.S3_PROVIDER_NAME.equals(providerName) ||
               FileStorageConstants.GCS_PROVIDER_NAME.equals(providerName) ||
               FileStorageConstants.AZURE_PROVIDER_NAME.equals(providerName);
    }
    
    /**
     * Validates the environment for file storage.
     * 
     * @return list of environment validation errors
     */
    public List<String> validateEnvironment() {
        List<String> errors = new ArrayList<>();
        
        // Check if temp directory is writable
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            if (tempDir == null || tempDir.trim().isEmpty()) {
                errors.add("Java temp directory is not configured");
            } else {
                java.io.File tempFile = new java.io.File(tempDir);
                if (!tempFile.exists()) {
                    errors.add("Java temp directory does not exist: " + tempDir);
                } else if (!tempFile.canWrite()) {
                    errors.add("Java temp directory is not writable: " + tempDir);
                }
            }
        } catch (Exception e) {
            logger.error("Error checking temp directory", e);
            errors.add("Error checking temp directory: " + e.getMessage());
        }
        
        return errors;
    }
}
