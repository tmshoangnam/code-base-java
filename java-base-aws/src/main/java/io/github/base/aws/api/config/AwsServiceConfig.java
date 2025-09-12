package io.github.base.aws.api.config;

import io.github.base.aws.api.common.AwsCredentials;
import io.github.base.aws.api.common.AwsRegion;
import io.github.base.aws.api.common.AwsClientConfig;

import java.util.Map;

/**
 * Contract for AWS service configuration.
 * 
 * <p>This interface defines the standard configuration options for AWS services
 * including credentials, region, and service-specific settings.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface AwsServiceConfig {
    
    /**
     * Gets the AWS credentials.
     * 
     * @return the AWS credentials
     */
    AwsCredentials getCredentials();
    
    /**
     * Gets the AWS region.
     * 
     * @return the AWS region
     */
    AwsRegion getRegion();
    
    /**
     * Gets the AWS client configuration.
     * 
     * @return the AWS client configuration
     */
    AwsClientConfig getClientConfig();
    
    /**
     * Gets the service name.
     * 
     * @return the service name
     */
    String getServiceName();
    
    /**
     * Gets the service version.
     * 
     * @return the service version
     */
    String getServiceVersion();
    
    /**
     * Gets the service endpoint.
     * 
     * @return the service endpoint or null if using default
     */
    String getEndpoint();
    
    /**
     * Gets the service-specific configuration properties.
     * 
     * @return map of service-specific properties
     */
    Map<String, Object> getServiceProperties();
    
    /**
     * Gets a service-specific property value by key.
     * 
     * @param key the property key
     * @return the property value or null if not found
     */
    Object getServiceProperty(String key);
    
    /**
     * Checks if a service-specific property exists.
     * 
     * @param key the property key
     * @return true if property exists, false otherwise
     */
    boolean hasServiceProperty(String key);
    
    /**
     * Checks if this config has an endpoint override.
     * 
     * @return true if endpoint is set, false otherwise
     */
    boolean hasEndpoint();
    
    /**
     * Checks if this config has service-specific properties.
     * 
     * @return true if service properties are set, false otherwise
     */
    boolean hasServiceProperties();
    
    /**
     * Gets the configuration as a map.
     * 
     * @return map representation of the configuration
     */
    Map<String, Object> toMap();
    
    /**
     * Creates a copy of this config with updated credentials.
     * 
     * @param credentials new credentials
     * @return new config instance with updated credentials
     */
    AwsServiceConfig withCredentials(AwsCredentials credentials);
    
    /**
     * Creates a copy of this config with updated region.
     * 
     * @param region new region
     * @return new config instance with updated region
     */
    AwsServiceConfig withRegion(AwsRegion region);
    
    /**
     * Creates a copy of this config with updated client configuration.
     * 
     * @param clientConfig new client configuration
     * @return new config instance with updated client configuration
     */
    AwsServiceConfig withClientConfig(AwsClientConfig clientConfig);
    
    /**
     * Creates a copy of this config with updated endpoint.
     * 
     * @param endpoint new endpoint
     * @return new config instance with updated endpoint
     */
    AwsServiceConfig withEndpoint(String endpoint);
    
    /**
     * Creates a copy of this config with updated service properties.
     * 
     * @param serviceProperties new service properties
     * @return new config instance with updated service properties
     */
    AwsServiceConfig withServiceProperties(Map<String, Object> serviceProperties);
    
    /**
     * Creates a copy of this config with an additional service property.
     * 
     * @param key property key
     * @param value property value
     * @return new config instance with additional property
     */
    AwsServiceConfig withServiceProperty(String key, Object value);
    
    /**
     * Creates a copy of this config without a service property.
     * 
     * @param key property key to remove
     * @return new config instance without the property
     */
    AwsServiceConfig withoutServiceProperty(String key);
}
