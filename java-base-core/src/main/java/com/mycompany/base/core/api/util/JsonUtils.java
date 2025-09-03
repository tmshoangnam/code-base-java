package com.mycompany.base.core.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Objects;

/**
 * Utility class for JSON serialization and deserialization.
 * 
 * <p>This class provides common JSON operations using Jackson ObjectMapper.
 * It includes proper configuration for Java 8+ time types and handles
 * common serialization scenarios.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Thread-safe operations with static ObjectMapper</li>
 *   <li>Proper configuration for Java 8+ time types</li>
 *   <li>Null-safe operations where appropriate</li>
 *   <li>Minimal dependencies - only Jackson core</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Serialize object to JSON
 * String json = JsonUtils.toJson(user);
 * 
 * // Deserialize JSON to object
 * User user = JsonUtils.fromJson(json, User.class);
 * 
 * // Pretty print JSON
 * String prettyJson = JsonUtils.toPrettyJson(user);
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public final class JsonUtils {
    
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();
    
    private JsonUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Creates a configured ObjectMapper instance.
     * 
     * @return configured ObjectMapper
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper;
    }
    
    /**
     * Serializes an object to JSON string.
     * 
     * @param object the object to serialize
     * @return JSON string representation
     * @throws JsonProcessingException if serialization fails
     */
    public static String toJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return OBJECT_MAPPER.writeValueAsString(object);
    }
    
    /**
     * Serializes an object to JSON string, returning null on error.
     * 
     * @param object the object to serialize
     * @return JSON string representation or null if serialization fails
     */
    public static String toJsonSafe(Object object) {
        try {
            return toJson(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    
    /**
     * Serializes an object to pretty-printed JSON string.
     * 
     * @param object the object to serialize
     * @return pretty-printed JSON string representation
     * @throws JsonProcessingException if serialization fails
     */
    public static String toPrettyJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
    
    /**
     * Serializes an object to pretty-printed JSON string, returning null on error.
     * 
     * @param object the object to serialize
     * @return pretty-printed JSON string representation or null if serialization fails
     */
    public static String toPrettyJsonSafe(Object object) {
        try {
            return toPrettyJson(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    
    /**
     * Deserializes a JSON string to an object of the specified type.
     * 
     * @param json the JSON string to deserialize
     * @param clazz the target class
     * @param <T> the target type
     * @return deserialized object
     * @throws JsonProcessingException if deserialization fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        if (json == null) {
            return null;
        }
        Objects.requireNonNull(clazz, "clazz cannot be null");
        return OBJECT_MAPPER.readValue(json, clazz);
    }
    
    /**
     * Deserializes a JSON string to an object of the specified type, returning null on error.
     * 
     * @param json the JSON string to deserialize
     * @param clazz the target class
     * @param <T> the target type
     * @return deserialized object or null if deserialization fails
     */
    public static <T> T fromJsonSafe(String json, Class<T> clazz) {
        try {
            return fromJson(json, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    
    /**
     * Checks if a string is valid JSON.
     * 
     * @param json the string to check
     * @return true if the string is valid JSON, false otherwise
     */
    public static boolean isValidJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
    
    /**
     * Gets the configured ObjectMapper instance.
     * 
     * <p>This method provides access to the internal ObjectMapper for
     * advanced use cases that require custom configuration.
     * 
     * @return the configured ObjectMapper instance
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
