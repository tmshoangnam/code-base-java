package io.github.base.starter.file.impl.s3;

import io.github.base.aws.api.s3.S3Operations;
import io.github.base.aws.api.common.AwsCredentials;
import io.github.base.aws.api.common.AwsRegion;
import io.github.base.aws.api.common.AwsClientConfig;
import io.github.base.aws.api.config.AwsServiceConfig;
import io.github.base.aws.AwsServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Configuration class for S3FileStorage.
 *
 * <p>This class handles the creation and configuration of S3FileStorage instances
 * by managing S3Operations and related AWS services.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class S3FileStorageConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(S3FileStorageConfiguration.class);

    private final String bucketName;
    private final String keyPrefix;
    private final S3Operations s3Operations;

    /**
     * Creates a new S3FileStorageConfiguration.
     *
     * @param bucketName S3 bucket name
     * @param keyPrefix optional key prefix for all files
     * @param s3Operations S3 operations interface
     */
    public S3FileStorageConfiguration(String bucketName, String keyPrefix, S3Operations s3Operations) {
        this.bucketName = bucketName;
        this.keyPrefix = keyPrefix;
        this.s3Operations = s3Operations;
    }

    /**
     * Creates a new S3FileStorageConfiguration without key prefix.
     *
     * @param bucketName S3 bucket name
     * @param s3Operations S3 operations interface
     */
    public S3FileStorageConfiguration(String bucketName, S3Operations s3Operations) {
        this(bucketName, null, s3Operations);
    }

    /**
     * Creates a S3FileStorageConfiguration from configuration properties.
     *
     * @param configuration configuration properties
     * @return S3FileStorageConfiguration instance
     * @throws IllegalArgumentException if configuration is invalid
     */
    public static S3FileStorageConfiguration fromConfiguration(Map<String, String> configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }

        String bucketName = configuration.get("bucket-name");
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("S3 bucket name is required");
        }

        String keyPrefix = configuration.get("key-prefix");
        if (keyPrefix != null && keyPrefix.trim().isEmpty()) {
            keyPrefix = null;
        }

        // Create AWS credentials
        AwsCredentials credentials = createAwsCredentials(configuration);

        // Create AWS region
        AwsRegion region = createAwsRegion(configuration);

        // Create AWS client config
        AwsClientConfig clientConfig = createAwsClientConfig(configuration, credentials, region);

        // Get S3 operations from AwsServices
        S3Operations s3Operations = getS3Operations(clientConfig);

        return new S3FileStorageConfiguration(bucketName, keyPrefix, s3Operations);
    }

    /**
     * Creates a S3FileStorage instance.
     *
     * @return S3FileStorage instance
     */
    public S3FileStorage createFileStorage() {
        return new S3FileStorage(s3Operations, bucketName, keyPrefix);
    }

    /**
     * Gets the bucket name.
     *
     * @return bucket name
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * Gets the key prefix.
     *
     * @return key prefix or null if not set
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * Gets the S3 operations.
     *
     * @return S3 operations interface
     */
    public S3Operations getS3Operations() {
        return s3Operations;
    }

    // Private helper methods

    private static AwsCredentials createAwsCredentials(Map<String, String> configuration) {
        // Support both naming conventions
        String accessKeyId = configuration.get("access-key-id");
        if (accessKeyId == null) {
            accessKeyId = configuration.get("access-key");
        }

        String secretAccessKey = configuration.get("secret-access-key");
        if (secretAccessKey == null) {
            secretAccessKey = configuration.get("secret-key");
        }

        String sessionToken = configuration.get("session-token");
        String region = configuration.get("region");

        if (accessKeyId != null && secretAccessKey != null) {
            AwsCredentials builder = AwsCredentials.of(accessKeyId, secretAccessKey, region);

            if (sessionToken != null) {
                builder = builder.withSessionToken(sessionToken);
            }

            if (region != null) {
                builder = builder.withRegion(region);
            }

            return builder;
        } else {
            // Use default credentials - for now, create a basic credentials object
            // In a real implementation, this would use the AWS credentials provider chain
            return AwsCredentials.of("default", "default", region);
        }
    }

    private static AwsRegion createAwsRegion(Map<String, String> configuration) {
        String region = configuration.get("region");
        if (region != null && !region.trim().isEmpty()) {
            return AwsRegion.of(region);
        } else {
            return AwsRegion.of("us-east-1"); // Default region
        }
    }

    private static AwsClientConfig createAwsClientConfig(Map<String, String> configuration,
                                                        AwsCredentials credentials,
                                                        AwsRegion region) {
        String regionName = region.name();
        AwsClientConfig config = AwsClientConfig.of(regionName, credentials);

        // Add optional configuration
        String endpoint = configuration.get("endpoint");
        if (endpoint != null && !endpoint.trim().isEmpty()) {
            // For now, we'll use the basic config. Advanced endpoint configuration would need more complex setup
            config = new AwsClientConfig(
                config.region(),
                config.credentials(),
                config.connectionTimeout(),
                config.socketTimeout(),
                config.maxConnections(),
                config.maxRetries(),
                config.retryMode(),
                endpoint,
                config.useAccelerateEndpoint(),
                config.useDualstackEndpoint(),
                config.usePathStyleAccess(),
                config.additionalProperties()
            );
        }

        String pathStyleAccess = configuration.get("path-style-access");
        if ("true".equalsIgnoreCase(pathStyleAccess)) {
            Map<String, Object> additionalProps = new java.util.HashMap<>(config.additionalProperties());
            additionalProps.put("path-style-access", "true");
            config = new AwsClientConfig(
                config.region(),
                config.credentials(),
                config.connectionTimeout(),
                config.socketTimeout(),
                config.maxConnections(),
                config.maxRetries(),
                config.retryMode(),
                config.endpointOverride(),
                config.useAccelerateEndpoint(),
                config.useDualstackEndpoint(),
                true, // usePathStyleAccess
                additionalProps
            );
        }

        return config;
    }

    private static S3Operations getS3Operations(AwsClientConfig clientConfig) {
        try {
            // Create a simple AwsServiceConfig implementation
            AwsServiceConfig serviceConfig = new SimpleAwsServiceConfig("s3", "1.0.0", clientConfig);

            Object s3Service = AwsServices.getService("s3", serviceConfig);
            if (s3Service instanceof S3Operations s3Operations) {
                return s3Operations;
            } else {
                throw new IllegalStateException("S3 service is not available or not of expected type");
            }
        } catch (Exception e) {
            logger.error("Failed to get S3 operations", e);
            throw new IllegalStateException("Failed to get S3 operations", e);
        }
    }

    /**
     * Simple implementation of AwsServiceConfig for S3 operations.
     */
    private static class SimpleAwsServiceConfig implements AwsServiceConfig {
        private final String serviceName;
        private final String serviceVersion;
        private final AwsClientConfig clientConfig;

        public SimpleAwsServiceConfig(String serviceName, String serviceVersion, AwsClientConfig clientConfig) {
            this.serviceName = serviceName;
            this.serviceVersion = serviceVersion;
            this.clientConfig = clientConfig;
        }

        @Override
        public AwsCredentials getCredentials() {
            return clientConfig.credentials();
        }

        @Override
        public AwsRegion getRegion() {
            return AwsRegion.of(clientConfig.region());
        }

        @Override
        public AwsClientConfig getClientConfig() {
            return clientConfig;
        }

        @Override
        public String getServiceName() {
            return serviceName;
        }

        @Override
        public String getServiceVersion() {
            return serviceVersion;
        }

        @Override
        public String getEndpoint() {
            return clientConfig.endpointOverride();
        }

        @Override
        public Map<String, Object> getServiceProperties() {
            return clientConfig.additionalProperties();
        }

        @Override
        public Object getServiceProperty(String key) {
            return clientConfig.additionalProperties().get(key);
        }

        @Override
        public boolean hasServiceProperty(String key) {
            return clientConfig.additionalProperties().containsKey(key);
        }

        @Override
        public boolean hasEndpoint() {
            return clientConfig.endpointOverride() != null;
        }

        @Override
        public boolean hasServiceProperties() {
            return !clientConfig.additionalProperties().isEmpty();
        }

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("serviceName", serviceName);
            map.put("serviceVersion", serviceVersion);
            map.put("region", clientConfig.region());
            map.put("endpoint", clientConfig.endpointOverride());
            map.putAll(clientConfig.additionalProperties());
            return map;
        }

        @Override
        public AwsServiceConfig withCredentials(AwsCredentials credentials) {
            AwsClientConfig newClientConfig = new AwsClientConfig(
                clientConfig.region(),
                credentials,
                clientConfig.connectionTimeout(),
                clientConfig.socketTimeout(),
                clientConfig.maxConnections(),
                clientConfig.maxRetries(),
                clientConfig.retryMode(),
                clientConfig.endpointOverride(),
                clientConfig.useAccelerateEndpoint(),
                clientConfig.useDualstackEndpoint(),
                clientConfig.usePathStyleAccess(),
                clientConfig.additionalProperties()
            );
            return new SimpleAwsServiceConfig(serviceName, serviceVersion, newClientConfig);
        }

        @Override
        public AwsServiceConfig withRegion(AwsRegion region) {
            AwsClientConfig newClientConfig = new AwsClientConfig(
                region.name(),
                clientConfig.credentials(),
                clientConfig.connectionTimeout(),
                clientConfig.socketTimeout(),
                clientConfig.maxConnections(),
                clientConfig.maxRetries(),
                clientConfig.retryMode(),
                clientConfig.endpointOverride(),
                clientConfig.useAccelerateEndpoint(),
                clientConfig.useDualstackEndpoint(),
                clientConfig.usePathStyleAccess(),
                clientConfig.additionalProperties()
            );
            return new SimpleAwsServiceConfig(serviceName, serviceVersion, newClientConfig);
        }

        @Override
        public AwsServiceConfig withClientConfig(AwsClientConfig clientConfig) {
            return new SimpleAwsServiceConfig(serviceName, serviceVersion, clientConfig);
        }

        @Override
        public AwsServiceConfig withEndpoint(String endpoint) {
            AwsClientConfig newClientConfig = new AwsClientConfig(
                clientConfig.region(),
                clientConfig.credentials(),
                clientConfig.connectionTimeout(),
                clientConfig.socketTimeout(),
                clientConfig.maxConnections(),
                clientConfig.maxRetries(),
                clientConfig.retryMode(),
                endpoint,
                clientConfig.useAccelerateEndpoint(),
                clientConfig.useDualstackEndpoint(),
                clientConfig.usePathStyleAccess(),
                clientConfig.additionalProperties()
            );
            return new SimpleAwsServiceConfig(serviceName, serviceVersion, newClientConfig);
        }

        @Override
        public AwsServiceConfig withServiceProperties(Map<String, Object> serviceProperties) {
            AwsClientConfig newClientConfig = new AwsClientConfig(
                clientConfig.region(),
                clientConfig.credentials(),
                clientConfig.connectionTimeout(),
                clientConfig.socketTimeout(),
                clientConfig.maxConnections(),
                clientConfig.maxRetries(),
                clientConfig.retryMode(),
                clientConfig.endpointOverride(),
                clientConfig.useAccelerateEndpoint(),
                clientConfig.useDualstackEndpoint(),
                clientConfig.usePathStyleAccess(),
                serviceProperties
            );
            return new SimpleAwsServiceConfig(serviceName, serviceVersion, newClientConfig);
        }

        @Override
        public AwsServiceConfig withServiceProperty(String key, Object value) {
            Map<String, Object> newProperties = new java.util.HashMap<>(clientConfig.additionalProperties());
            newProperties.put(key, value);
            return withServiceProperties(newProperties);
        }

        @Override
        public AwsServiceConfig withoutServiceProperty(String key) {
            Map<String, Object> newProperties = new java.util.HashMap<>(clientConfig.additionalProperties());
            newProperties.remove(key);
            return withServiceProperties(newProperties);
        }
    }
}
