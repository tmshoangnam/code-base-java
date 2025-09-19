package com.tms.base.report.spi;

import java.util.Map;

/**
 * Service Provider Interface for data serialization.
 * 
 * @since 1.0.0
 */
public interface Serializer {
    
    /**
     * Serialize object to string.
     * 
     * @param object Object to serialize
     * @return Serialized string
     */
    String serialize(Object object);
    
    /**
     * Serialize object to string with parameters.
     * 
     * @param object Object to serialize
     * @param parameters Serialization parameters
     * @return Serialized string
     */
    String serialize(Object object, Map<String, Object> parameters);
    
    /**
     * Deserialize string to object.
     * 
     * @param <T> Target type
     * @param data Serialized data
     * @param targetClass Target class
     * @return Deserialized object
     */
    <T> T deserialize(String data, Class<T> targetClass);
    
    /**
     * Deserialize string to object with parameters.
     * 
     * @param <T> Target type
     * @param data Serialized data
     * @param targetClass Target class
     * @param parameters Deserialization parameters
     * @return Deserialized object
     */
    <T> T deserialize(String data, Class<T> targetClass, Map<String, Object> parameters);
    
    /**
     * Check if this serializer can handle the given class.
     * 
     * @param clazz Class to check
     * @return true if supported
     */
    boolean canSerialize(Class<?> clazz);
    
    /**
     * Get serializer name.
     * 
     * @return Serializer name
     */
    String getName();
    
    /**
     * Get serializer priority (higher number = higher priority).
     * 
     * @return Priority value
     */
    default int getPriority() {
        return 0;
    }
}
