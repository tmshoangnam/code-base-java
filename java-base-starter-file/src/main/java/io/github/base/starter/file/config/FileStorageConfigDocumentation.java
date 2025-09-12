package io.github.base.starter.file.config;

import io.github.base.starter.file.constants.FileStorageConstants;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Documentation for file storage configuration.
 * 
 * <p>This class provides comprehensive documentation for file storage
 * configuration options and usage examples.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@Component
public class FileStorageConfigDocumentation {
    
    /**
     * Gets configuration documentation for all providers.
     * 
     * @return map of provider names to configuration documentation
     */
    public Map<String, ProviderDocumentation> getProviderDocumentation() {
        return Map.of(
            FileStorageConstants.LOCAL_PROVIDER_NAME, getLocalProviderDocumentation(),
            FileStorageConstants.S3_PROVIDER_NAME, getS3ProviderDocumentation(),
            FileStorageConstants.GCS_PROVIDER_NAME, getGcsProviderDocumentation(),
            FileStorageConstants.AZURE_PROVIDER_NAME, getAzureProviderDocumentation()
        );
    }
    
    /**
     * Gets local provider documentation.
     */
    private ProviderDocumentation getLocalProviderDocumentation() {
        return new ProviderDocumentation(
            "Local File Storage",
            "Stores files on the local filesystem",
            Map.of(
                "base-path", "Base directory for file storage (default: " + FileStorageConstants.DEFAULT_LOCAL_BASE_PATH + ")",
                "create-directories", "Whether to create directories automatically (default: true)",
                "file-permissions", "File permissions in octal format (default: 644)",
                "directory-permissions", "Directory permissions in octal format (default: 755)"
            ),
            Map.of(
                "max-file-size", "Limited by available disk space",
                "max-files", "Limited by available disk space",
                "replication", "false",
                "encryption", "false",
                "versioning", "false"
            ),
            """
            Example configuration:
            base.file.storage:
              default-provider: local
              providers:
                local:
                  enabled: true
                  configuration:
                    base-path: /var/files
                    create-directories: true
                    file-permissions: "644"
                    directory-permissions: "755"
            """
        );
    }
    
    /**
     * Gets S3 provider documentation.
     */
    private ProviderDocumentation getS3ProviderDocumentation() {
        return new ProviderDocumentation(
            "AWS S3 Storage",
            "Stores files in AWS S3 buckets",
            Map.of(
                "bucket-name", "S3 bucket name for file storage (required)",
                "region", "AWS region for S3 bucket (required)",
                "endpoint", "Custom S3 endpoint URL (for testing with LocalStack)",
                "path-style-access", "Use path-style access (default: false)",
                "encryption", "Server-side encryption (default: none)",
                "storage-class", "S3 storage class (default: STANDARD)"
            ),
            Map.of(
                "max-file-size", "5TB",
                "max-files", "Unlimited",
                "cost", "Pay per use",
                "latency", "Network dependent"
            ),
            """
            Example configuration:
            base.file.storage:
              default-provider: s3
              providers:
                s3:
                  enabled: true
                  configuration:
                    bucket-name: my-file-bucket
                    region: us-east-1
                    encryption: AES256
                    storage-class: STANDARD_IA
            """
        );
    }
    
    /**
     * Gets GCS provider documentation.
     */
    private ProviderDocumentation getGcsProviderDocumentation() {
        return new ProviderDocumentation(
            "Google Cloud Storage",
            "Stores files in Google Cloud Storage buckets",
            Map.of(
                "bucket-name", "GCS bucket name for file storage (required)",
                "project-id", "Google Cloud project ID (required)",
                "credentials-path", "Path to service account credentials JSON file",
                "credentials-json", "Service account credentials JSON content",
                "endpoint", "Custom GCS endpoint URL (for testing)",
                "encryption", "Server-side encryption (default: none)",
                "storage-class", "GCS storage class (default: STANDARD)"
            ),
            Map.of(
                "max-file-size", "5TB",
                "max-files", "Unlimited",
                "cost", "Pay per use",
                "latency", "Network dependent"
            ),
            """
            Example configuration:
            base.file.storage:
              default-provider: gcs
              providers:
                gcs:
                  enabled: true
                  configuration:
                    bucket-name: my-file-bucket
                    project-id: my-project-123
                    credentials-path: /path/to/service-account.json
                    storage-class: NEARLINE
            """
        );
    }
    
    /**
     * Gets Azure provider documentation.
     */
    private ProviderDocumentation getAzureProviderDocumentation() {
        return new ProviderDocumentation(
            "Azure Blob Storage",
            "Stores files in Azure Blob Storage containers",
            Map.of(
                "container-name", "Azure Blob Storage container name (required)",
                "connection-string", "Azure Storage connection string (required)",
                "account-name", "Azure Storage account name",
                "account-key", "Azure Storage account key",
                "sas-token", "Shared Access Signature token",
                "endpoint", "Custom Azure Storage endpoint URL",
                "encryption", "Server-side encryption (default: none)",
                "tier", "Blob access tier (default: HOT)"
            ),
            Map.of(
                "max-file-size", "4.75TB",
                "max-files", "Unlimited",
                "cost", "Pay per use",
                "latency", "Network dependent"
            ),
            """
            Example configuration:
            base.file.storage:
              default-provider: azure
              providers:
                azure:
                  enabled: true
                  configuration:
                    container-name: my-file-container
                    connection-string: "DefaultEndpointsProtocol=https;AccountName=myaccount;AccountKey=mykey;EndpointSuffix=core.windows.net"
                    tier: HOT
            """
        );
    }
    
    /**
     * Gets general configuration documentation.
     */
    public String getGeneralConfigurationDocumentation() {
        return """
        # File Storage Configuration
        
        ## General Settings
        
        ```yaml
        base.file.storage:
          enabled: true                    # Enable file storage auto-configuration
          default-provider: local          # Default provider to use
          metrics-enabled: true            # Enable metrics collection
          cache-enabled: true              # Enable caching for file metadata
          cache-ttl-seconds: 300          # Cache TTL in seconds
          max-cache-size: 1000            # Maximum cache size
        ```
        
        ## Provider Configuration
        
        Each provider can be configured with specific settings:
        
        ```yaml
        base.file.storage:
          providers:
            provider-name:
              enabled: true                # Enable/disable this provider
              configuration:              # Provider-specific configuration
                key: value
        ```
        
        ## Supported Providers
        
        - **local**: Local filesystem storage
        - **s3**: AWS S3 storage
        - **gcs**: Google Cloud Storage
        - **azure**: Azure Blob Storage
        
        ## Decorators
        
        The following decorators are automatically applied based on configuration:
        
        - **LoggingFileStorage**: Always applied for request/response logging
        - **ResilientFileStorage**: Always applied for retry and circuit breaker
        - **SecureFileStorage**: Always applied for security features
        - **CachedFileStorage**: Applied when cache-enabled is true
        - **MetricsFileStorage**: Applied when metrics-enabled is true
        """;
    }
    
    /**
     * Provider documentation record.
     */
    public record ProviderDocumentation(
        String name,
        String description,
        Map<String, String> configurationOptions,
        Map<String, String> limitations,
        String exampleConfiguration
    ) {}
}
