package io.github.base.security.spi;

import java.util.Map;
import java.util.Optional;

/**
 * Configuration for security providers.
 * Contains all the necessary configuration parameters for a security provider.
 */
public interface SecurityProviderConfiguration {
    
    /**
     * Gets the provider name.
     *
     * @return the provider name
     */
    String getProviderName();
    
    /**
     * Gets a configuration property by name.
     *
     * @param name the property name
     * @return an optional containing the property value if found
     */
    Optional<Object> getProperty(String name);
    
    /**
     * Gets a configuration property as a string.
     *
     * @param name the property name
     * @return the property value as a string, or null if not found
     */
    String getStringProperty(String name);
    
    /**
     * Gets a configuration property as a string with a default value.
     *
     * @param name the property name
     * @param defaultValue the default value to return if property not found
     * @return the property value as a string, or the default value if not found
     */
    String getStringProperty(String name, String defaultValue);
    
    /**
     * Gets a configuration property as a boolean.
     *
     * @param name the property name
     * @return the property value as a boolean, or false if not found
     */
    boolean getBooleanProperty(String name);
    
    /**
     * Gets a configuration property as a boolean with a default value.
     *
     * @param name the property name
     * @param defaultValue the default value to return if property not found
     * @return the property value as a boolean, or the default value if not found
     */
    boolean getBooleanProperty(String name, boolean defaultValue);
    
    /**
     * Gets a configuration property as an integer.
     *
     * @param name the property name
     * @return the property value as an integer, or 0 if not found
     */
    int getIntProperty(String name);
    
    /**
     * Gets a configuration property as an integer with a default value.
     *
     * @param name the property name
     * @param defaultValue the default value to return if property not found
     * @return the property value as an integer, or the default value if not found
     */
    int getIntProperty(String name, int defaultValue);
    
    /**
     * Gets a configuration property as a long.
     *
     * @param name the property name
     * @return the property value as a long, or 0L if not found
     */
    long getLongProperty(String name);
    
    /**
     * Gets a configuration property as a long with a default value.
     *
     * @param name the property name
     * @param defaultValue the default value to return if property not found
     * @return the property value as a long, or the default value if not found
     */
    long getLongProperty(String name, long defaultValue);
    
    /**
     * Gets all configuration properties.
     *
     * @return a map of all configuration properties
     */
    Map<String, Object> getAllProperties();
    
    /**
     * Checks if a configuration property exists.
     *
     * @param name the property name
     * @return true if the property exists, false otherwise
     */
    boolean hasProperty(String name);
    
    /**
     * Checks if this configuration is valid.
     *
     * @return true if the configuration is valid, false otherwise
     */
    boolean isValid();
}
