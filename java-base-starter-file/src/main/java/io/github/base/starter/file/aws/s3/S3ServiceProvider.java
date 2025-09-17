package io.github.base.starter.file.aws.s3;

import io.github.base.aws.api.config.AwsServiceConfig;
import io.github.base.aws.spi.AwsServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * AWS S3 service provider implementation.
 * 
 * <p>This provider implements the AwsServiceProvider interface for S3 services.
 * It creates S3Operations instances using the AWS SDK.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class S3ServiceProvider implements AwsServiceProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(S3ServiceProvider.class);
    
    @Override
    public String getServiceName() {
        return "s3";
    }
    
    @Override
    public String getVersion() {
        return "1.0.5-SNAPSHOT";
    }
    
    @Override
    public int getPriority() {
        return 100; // High priority for S3
    }
    
    @Override
    public boolean isAvailable() {
        try {
            // Check if AWS SDK S3 is available
            Class.forName("software.amazon.awssdk.services.s3.S3Client");
            return true;
        } catch (ClassNotFoundException e) {
            logger.warn("S3 service provider is not available - AWS SDK not found", e);
            return false;
        }
    }
    
    @Override
    public Object createService(AwsServiceConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        
        try {
            // Create S3Operations using the configuration
            return new S3OperationsImpl(config);
        } catch (Exception e) {
            logger.error("Failed to create S3 service", e);
            throw new IllegalStateException("Failed to create S3 service", e);
        }
    }
    
    @Override
    public Map<String, Boolean> getCapabilities() {
        Map<String, Boolean> capabilities = new HashMap<>();
        capabilities.put("upload", true);
        capabilities.put("download", true);
        capabilities.put("delete", true);
        capabilities.put("exists", true);
        capabilities.put("metadata", true);
        capabilities.put("list", true);
        capabilities.put("presigned-urls", true);
        capabilities.put("versioning", true);
        capabilities.put("encryption", true);
        return capabilities;
    }
    
    @Override
    public Map<String, String> getRequiredConfiguration() {
        return Map.of(
            "region", "AWS region for S3 service",
            "credentials", "AWS credentials"
        );
    }
    
    @Override
    public Map<String, String> getOptionalConfiguration() {
        return Map.of(
            "endpoint", "Custom S3 endpoint URL",
            "path-style-access", "Use path-style access",
            "encryption", "Server-side encryption",
            "storage-class", "S3 storage class"
        );
    }
    
    @Override
    public List<String> validateConfiguration(AwsServiceConfig config) {
        // Basic validation - in a real implementation, this would be more comprehensive
        return List.of();
    }
    
    @Override
    public String getDescription() {
        return "AWS S3 service provider for file storage operations";
    }
    
}
