package io.github.base.starter.file.impl.s3;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.spi.FileStorageProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * S3 file storage provider implementation.
 *
 * <p>This provider implements file storage using AWS S3.
 * It supports all standard file operations and provides
 * cloud-based storage solution.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class S3FileStorageProvider implements FileStorageProvider {

    private static final Logger logger = LoggerFactory.getLogger(S3FileStorageProvider.class);

    private static final String PROVIDER_NAME = "s3";
    private static final String BUCKET_NAME_PROPERTY = "bucket-name";
    private static final String REGION_PROPERTY = "region";
    private static final String ACCESS_KEY_ID_PROPERTY = "access-key-id";
    private static final String SECRET_ACCESS_KEY_PROPERTY = "secret-access-key";
    private static final String ACCESS_KEY_PROPERTY = "access-key"; // Alternative naming
    private static final String SECRET_KEY_PROPERTY = "secret-key"; // Alternative naming

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getProviderDescription() {
        return "AWS S3 file storage provider";
    }

    @Override
    public String getProviderVersion() {
        return "1.0.5-SNAPSHOT";
    }

    @Override
    public boolean isAvailable() {
        try {
            // Check if AWS SDK is available
            Class.forName("software.amazon.awssdk.services.s3.S3Client");
            return true;
        } catch (ClassNotFoundException e) {
            logger.warn("S3 file storage provider is not available - AWS SDK not found", e);
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 50; // Medium priority for S3 storage
    }

    @Override
    public Map<String, String> getRequiredConfiguration() {
        return Map.of(
            BUCKET_NAME_PROPERTY, "S3 bucket name for file storage",
            REGION_PROPERTY, "AWS region for S3 bucket"
        );
    }

    @Override
    public Map<String, String> getOptionalConfiguration() {
        return Map.of(
            "endpoint", "Custom S3 endpoint URL (for testing with LocalStack)",
            "path-style-access", "Use path-style access (default: false)",
            "encryption", "Server-side encryption (default: none)",
            "storage-class", "S3 storage class (default: STANDARD)"
        );
    }

    @Override
    public FileStorage createStorage() {
        // Try to create with default configuration from environment variables
        Map<String, String> defaultConfig = createDefaultConfiguration();

        // Validate that we have minimum required configuration
        List<String> errors = validateConfiguration(defaultConfig);
        if (!errors.isEmpty()) {
            throw new IllegalStateException("S3FileStorage requires configuration. " +
                "Please provide AWS credentials and bucket name through environment variables or " +
                "use createStorage(Map<String, String> configuration) method. " +
                "Missing: " + String.join(", ", errors));
        }

        return createStorage(defaultConfig);
    }

    /**
     * Creates a FileStorage instance with the given configuration.
     *
     * @param configuration configuration properties
     * @return FileStorage instance
     * @throws IllegalArgumentException if configuration is invalid
     */
    public FileStorage createStorage(Map<String, String> configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }

        // Validate configuration
        List<String> errors = validateConfiguration(configuration);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid configuration: " + String.join(", ", errors));
        }

        try {
            // Create S3FileStorageConfiguration
            S3FileStorageConfiguration config = S3FileStorageConfiguration.fromConfiguration(configuration);

            // Create and return S3FileStorage
            return config.createFileStorage();

        } catch (Exception e) {
            logger.error("Failed to create S3FileStorage", e);
            throw new IllegalStateException("Failed to create S3FileStorage", e);
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
        capabilities.put("streaming", true);
        capabilities.put("concurrent", true);
        capabilities.put("replication", true);
        capabilities.put("encryption", true);
        capabilities.put("versioning", true);
        return capabilities;
    }

    @Override
    public Map<String, String> getLimitations() {
        return Map.of(
            "max-file-size", "5TB",
            "max-files", "Unlimited",
            "cost", "Pay per use",
            "latency", "Network dependent"
        );
    }

    @Override
    public List<String> validateConfiguration(Map<String, String> configuration) {
        List<String> errors = new ArrayList<>();

        if (configuration == null) {
            errors.add("Configuration is required for S3 provider");
            return errors;
        }

        String bucketName = configuration.get(BUCKET_NAME_PROPERTY);
        if (bucketName == null || bucketName.trim().isEmpty()) {
            errors.add("S3 bucket name is required");
        } else if (!isValidBucketName(bucketName)) {
            errors.add("Invalid S3 bucket name: " + bucketName);
        }

        String region = configuration.get(REGION_PROPERTY);
        if (region == null || region.trim().isEmpty()) {
            errors.add("AWS region is required");
        } else if (!isValidRegion(region)) {
            errors.add("Invalid AWS region: " + region);
        }

        // Validate credentials (support both naming conventions)
        String accessKeyId = configuration.get(ACCESS_KEY_ID_PROPERTY);
        if (accessKeyId == null) {
            accessKeyId = configuration.get(ACCESS_KEY_PROPERTY);
        }

        String secretAccessKey = configuration.get(SECRET_ACCESS_KEY_PROPERTY);
        if (secretAccessKey == null) {
            secretAccessKey = configuration.get(SECRET_KEY_PROPERTY);
        }

        if (accessKeyId == null || accessKeyId.trim().isEmpty()) {
            errors.add("AWS access key ID is required (use 'access-key-id' or 'access-key')");
        }

        if (secretAccessKey == null || secretAccessKey.trim().isEmpty()) {
            errors.add("AWS secret access key is required (use 'secret-access-key' or 'secret-key')");
        }

        return errors;
    }

    /**
     * Creates default configuration from environment variables.
     *
     * @return default configuration map
     */
    private Map<String, String> createDefaultConfiguration() {
        Map<String, String> config = new HashMap<>();

        // Try to get configuration from environment variables
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        if (bucketName == null) {
            bucketName = System.getProperty("aws.s3.bucket.name");
        }
        if (bucketName != null) {
            config.put(BUCKET_NAME_PROPERTY, bucketName);
        }

        String region = System.getenv("AWS_REGION");
        if (region == null) {
            region = System.getProperty("aws.region");
        }
        if (region == null) {
            region = "us-east-1"; // Default region
        }
        config.put(REGION_PROPERTY, region);

        String accessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
        if (accessKeyId == null) {
            accessKeyId = System.getProperty("aws.access.key.id");
        }
        if (accessKeyId != null) {
            config.put(ACCESS_KEY_ID_PROPERTY, accessKeyId);
        }

        String secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        if (secretAccessKey == null) {
            secretAccessKey = System.getProperty("aws.secret.access.key");
        }
        if (secretAccessKey != null) {
            config.put(SECRET_ACCESS_KEY_PROPERTY, secretAccessKey);
        }

        return config;
    }


    /**
     * Validates S3 bucket name format.
     *
     * @param bucketName the bucket name to validate
     * @return true if valid
     */
    private boolean isValidBucketName(String bucketName) {
        if (bucketName == null || bucketName.length() < 3 || bucketName.length() > 63) {
            return false;
        }

        // S3 bucket name rules
        if (bucketName.startsWith(".") || bucketName.endsWith(".")) {
            return false;
        }

        if (bucketName.contains("..")) {
            return false;
        }

        return bucketName.matches("^[a-z0-9][a-z0-9.-]*[a-z0-9]$");
    }

    /**
     * Validates AWS region format.
     *
     * @param region the region to validate
     * @return true if valid
     */
    private boolean isValidRegion(String region) {
        if (region == null || region.trim().isEmpty()) {
            return false;
        }

        // Basic region format validation
        return region.matches("^[a-z0-9-]+$");
    }
}
