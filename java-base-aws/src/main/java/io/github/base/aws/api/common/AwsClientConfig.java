package io.github.base.aws.api.common;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing AWS client configuration.
 * 
 * <p>This record contains configuration options for AWS clients including
 * connection settings, timeouts, and retry policies.
 * 
 * @param region the AWS region
 * @param credentials the AWS credentials
 * @param connectionTimeout connection timeout
 * @param socketTimeout socket timeout
 * @param maxConnections maximum number of connections
 * @param maxRetries maximum number of retries
 * @param retryMode retry mode
 * @param endpointOverride custom endpoint override
 * @param useAccelerateEndpoint whether to use S3 accelerate endpoint
 * @param useDualstackEndpoint whether to use dual-stack endpoint
 * @param usePathStyleAccess whether to use path-style access
 * @param additionalProperties additional configuration properties
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record AwsClientConfig(
    String region,
    AwsCredentials credentials,
    Duration connectionTimeout,
    Duration socketTimeout,
    int maxConnections,
    int maxRetries,
    String retryMode,
    String endpointOverride,
    boolean useAccelerateEndpoint,
    boolean useDualstackEndpoint,
    boolean usePathStyleAccess,
    Map<String, Object> additionalProperties
) {
    
    /**
     * Creates an AWS client config with basic information.
     * 
     * @param region the AWS region
     * @param credentials the AWS credentials
     * @return new AwsClientConfig instance
     * @throws IllegalArgumentException if region is null or empty, or credentials is null
     */
    public static AwsClientConfig of(String region, AwsCredentials credentials) {
        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("Region cannot be null or empty");
        }
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        
        return new AwsClientConfig(
            region.trim(),
            credentials,
            Duration.ofSeconds(10),
            Duration.ofSeconds(30),
            50,
            3,
            "STANDARD",
            null,
            false,
            false,
            false,
            Map.of()
        );
    }
    
    /**
     * Creates an AWS client config with all options.
     * 
     * @param region the AWS region
     * @param credentials the AWS credentials
     * @param connectionTimeout connection timeout
     * @param socketTimeout socket timeout
     * @param maxConnections maximum number of connections
     * @param maxRetries maximum number of retries
     * @param retryMode retry mode
     * @param endpointOverride custom endpoint override
     * @param useAccelerateEndpoint whether to use S3 accelerate endpoint
     * @param useDualstackEndpoint whether to use dual-stack endpoint
     * @param usePathStyleAccess whether to use path-style access
     * @param additionalProperties additional configuration properties
     * @return new AwsClientConfig instance
     * @throws IllegalArgumentException if region is null or empty, or credentials is null
     */
    public static AwsClientConfig of(String region, AwsCredentials credentials, Duration connectionTimeout,
                                    Duration socketTimeout, int maxConnections, int maxRetries, String retryMode,
                                    String endpointOverride, boolean useAccelerateEndpoint, boolean useDualstackEndpoint,
                                    boolean usePathStyleAccess, Map<String, Object> additionalProperties) {
        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("Region cannot be null or empty");
        }
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        
        return new AwsClientConfig(
            region.trim(),
            credentials,
            connectionTimeout != null ? connectionTimeout : Duration.ofSeconds(10),
            socketTimeout != null ? socketTimeout : Duration.ofSeconds(30),
            Math.max(maxConnections, 1),
            Math.max(maxRetries, 0),
            retryMode != null ? retryMode : "STANDARD",
            endpointOverride,
            useAccelerateEndpoint,
            useDualstackEndpoint,
            usePathStyleAccess,
            additionalProperties != null ? additionalProperties : Map.of()
        );
    }
    
    /**
     * Creates a copy of this AwsClientConfig with updated region.
     * 
     * @param region new region
     * @return new AwsClientConfig instance with updated region
     */
    public AwsClientConfig withRegion(String region) {
        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("Region cannot be null or empty");
        }
        
        return new AwsClientConfig(
            region.trim(),
            this.credentials,
            this.connectionTimeout,
            this.socketTimeout,
            this.maxConnections,
            this.maxRetries,
            this.retryMode,
            this.endpointOverride,
            this.useAccelerateEndpoint,
            this.useDualstackEndpoint,
            this.usePathStyleAccess,
            this.additionalProperties
        );
    }
    
    /**
     * Creates a copy of this AwsClientConfig with updated credentials.
     * 
     * @param credentials new credentials
     * @return new AwsClientConfig instance with updated credentials
     */
    public AwsClientConfig withCredentials(AwsCredentials credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        
        return new AwsClientConfig(
            this.region,
            credentials,
            this.connectionTimeout,
            this.socketTimeout,
            this.maxConnections,
            this.maxRetries,
            this.retryMode,
            this.endpointOverride,
            this.useAccelerateEndpoint,
            this.useDualstackEndpoint,
            this.usePathStyleAccess,
            this.additionalProperties
        );
    }
    
    /**
     * Creates a copy of this AwsClientConfig with updated timeouts.
     * 
     * @param connectionTimeout new connection timeout
     * @param socketTimeout new socket timeout
     * @return new AwsClientConfig instance with updated timeouts
     */
    public AwsClientConfig withTimeouts(Duration connectionTimeout, Duration socketTimeout) {
        return new AwsClientConfig(
            this.region,
            this.credentials,
            connectionTimeout != null ? connectionTimeout : this.connectionTimeout,
            socketTimeout != null ? socketTimeout : this.socketTimeout,
            this.maxConnections,
            this.maxRetries,
            this.retryMode,
            this.endpointOverride,
            this.useAccelerateEndpoint,
            this.useDualstackEndpoint,
            this.usePathStyleAccess,
            this.additionalProperties
        );
    }
    
    /**
     * Creates a copy of this AwsClientConfig with updated connection settings.
     * 
     * @param maxConnections new maximum number of connections
     * @param maxRetries new maximum number of retries
     * @return new AwsClientConfig instance with updated connection settings
     */
    public AwsClientConfig withConnectionSettings(int maxConnections, int maxRetries) {
        return new AwsClientConfig(
            this.region,
            this.credentials,
            this.connectionTimeout,
            this.socketTimeout,
            Math.max(maxConnections, 1),
            Math.max(maxRetries, 0),
            this.retryMode,
            this.endpointOverride,
            this.useAccelerateEndpoint,
            this.useDualstackEndpoint,
            this.usePathStyleAccess,
            this.additionalProperties
        );
    }
    
    /**
     * Creates a copy of this AwsClientConfig with updated endpoint settings.
     * 
     * @param endpointOverride new endpoint override
     * @param useAccelerateEndpoint whether to use accelerate endpoint
     * @param useDualstackEndpoint whether to use dual-stack endpoint
     * @param usePathStyleAccess whether to use path-style access
     * @return new AwsClientConfig instance with updated endpoint settings
     */
    public AwsClientConfig withEndpointSettings(String endpointOverride, boolean useAccelerateEndpoint,
                                               boolean useDualstackEndpoint, boolean usePathStyleAccess) {
        return new AwsClientConfig(
            this.region,
            this.credentials,
            this.connectionTimeout,
            this.socketTimeout,
            this.maxConnections,
            this.maxRetries,
            this.retryMode,
            endpointOverride,
            useAccelerateEndpoint,
            useDualstackEndpoint,
            usePathStyleAccess,
            this.additionalProperties
        );
    }
    
    /**
     * Creates a copy of this AwsClientConfig with updated additional properties.
     * 
     * @param additionalProperties new additional properties
     * @return new AwsClientConfig instance with updated additional properties
     */
    public AwsClientConfig withAdditionalProperties(Map<String, Object> additionalProperties) {
        return new AwsClientConfig(
            this.region,
            this.credentials,
            this.connectionTimeout,
            this.socketTimeout,
            this.maxConnections,
            this.maxRetries,
            this.retryMode,
            this.endpointOverride,
            this.useAccelerateEndpoint,
            this.useDualstackEndpoint,
            this.usePathStyleAccess,
            additionalProperties != null ? additionalProperties : Map.of()
        );
    }
    
    /**
     * Gets an additional property value by key.
     * 
     * @param key property key
     * @return property value or null if not found
     */
    public Object getAdditionalProperty(String key) {
        return additionalProperties != null ? additionalProperties.get(key) : null;
    }
    
    /**
     * Checks if an additional property exists.
     * 
     * @param key property key
     * @return true if property exists, false otherwise
     */
    public boolean hasAdditionalProperty(String key) {
        return additionalProperties != null && additionalProperties.containsKey(key);
    }
    
    /**
     * Checks if this config has an endpoint override.
     * 
     * @return true if endpoint override is set, false otherwise
     */
    public boolean hasEndpointOverride() {
        return endpointOverride != null && !endpointOverride.trim().isEmpty();
    }
    
    /**
     * Checks if this config has additional properties.
     * 
     * @return true if additional properties are set, false otherwise
     */
    public boolean hasAdditionalProperties() {
        return additionalProperties != null && !additionalProperties.isEmpty();
    }
    
    /**
     * Checks if this config is using accelerate endpoint.
     * 
     * @return true if accelerate endpoint is enabled, false otherwise
     */
    public boolean isUsingAccelerateEndpoint() {
        return useAccelerateEndpoint;
    }
    
    /**
     * Checks if this config is using dual-stack endpoint.
     * 
     * @return true if dual-stack endpoint is enabled, false otherwise
     */
    public boolean isUsingDualstackEndpoint() {
        return useDualstackEndpoint;
    }
    
    /**
     * Checks if this config is using path-style access.
     * 
     * @return true if path-style access is enabled, false otherwise
     */
    public boolean isUsingPathStyleAccess() {
        return usePathStyleAccess;
    }
    
    /**
     * Gets the connection timeout in milliseconds.
     * 
     * @return connection timeout in milliseconds
     */
    public long getConnectionTimeoutMillis() {
        return connectionTimeout != null ? connectionTimeout.toMillis() : 10000;
    }
    
    /**
     * Gets the socket timeout in milliseconds.
     * 
     * @return socket timeout in milliseconds
     */
    public long getSocketTimeoutMillis() {
        return socketTimeout != null ? socketTimeout.toMillis() : 30000;
    }
    
    /**
     * Checks if this config has retries enabled.
     * 
     * @return true if max retries > 0, false otherwise
     */
    public boolean hasRetriesEnabled() {
        return maxRetries > 0;
    }
    
    /**
     * Gets the retry mode as an enum value.
     * 
     * @return retry mode enum value
     */
    public RetryMode getRetryModeEnum() {
        if (retryMode == null) {
            return RetryMode.STANDARD;
        }
        
        try {
            return RetryMode.valueOf(retryMode.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RetryMode.STANDARD;
        }
    }
    
    /**
     * Enum representing retry modes.
     */
    public enum RetryMode {
        STANDARD,
        ADAPTIVE,
        LEGACY
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AwsClientConfig that = (AwsClientConfig) obj;
        return maxConnections == that.maxConnections &&
               maxRetries == that.maxRetries &&
               useAccelerateEndpoint == that.useAccelerateEndpoint &&
               useDualstackEndpoint == that.useDualstackEndpoint &&
               usePathStyleAccess == that.usePathStyleAccess &&
               Objects.equals(region, that.region) &&
               Objects.equals(credentials, that.credentials) &&
               Objects.equals(connectionTimeout, that.connectionTimeout) &&
               Objects.equals(socketTimeout, that.socketTimeout) &&
               Objects.equals(retryMode, that.retryMode) &&
               Objects.equals(endpointOverride, that.endpointOverride) &&
               Objects.equals(additionalProperties, that.additionalProperties);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(region, credentials, connectionTimeout, socketTimeout, maxConnections, 
                           maxRetries, retryMode, endpointOverride, useAccelerateEndpoint, 
                           useDualstackEndpoint, usePathStyleAccess, additionalProperties);
    }
    
    @Override
    public String toString() {
        return "AwsClientConfig{" +
               "region='" + region + '\'' +
               ", credentials=" + credentials +
               ", connectionTimeout=" + connectionTimeout +
               ", socketTimeout=" + socketTimeout +
               ", maxConnections=" + maxConnections +
               ", maxRetries=" + maxRetries +
               ", retryMode='" + retryMode + '\'' +
               ", endpointOverride='" + endpointOverride + '\'' +
               ", useAccelerateEndpoint=" + useAccelerateEndpoint +
               ", useDualstackEndpoint=" + useDualstackEndpoint +
               ", usePathStyleAccess=" + usePathStyleAccess +
               ", additionalProperties=" + additionalProperties +
               '}';
    }
}
