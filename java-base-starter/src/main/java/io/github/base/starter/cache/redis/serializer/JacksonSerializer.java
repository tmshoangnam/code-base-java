package io.github.base.starter.cache.redis.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.base.cache.api.CacheException;

/**
 * Jackson-based JSON serializer for cache values.
 * <p>
 * This serializer uses Jackson ObjectMapper to serialize and deserialize
 * objects to/from JSON format. It's suitable for complex objects that
 * need to be stored as JSON in Redis.
 * <p>
 * <strong>Thread Safety:</strong> This implementation is thread-safe as
 * ObjectMapper is thread-safe for read operations.
 * <p>
 * <strong>Requirements:</strong> The target class must be serializable by Jackson
 * (have appropriate annotations or follow JavaBean conventions).
 *
 * @param <V> the type of values to serialize/deserialize
 * @since 1.0.0
 */
public final class JacksonSerializer<V> implements Serializer<String, V> {

    private final ObjectMapper objectMapper;
    private final Class<V> valueType;

    /**
     * Creates a new JacksonSerializer with the specified value type.
     *
     * @param valueType the class type of values to serialize/deserialize
     */
    public JacksonSerializer(Class<V> valueType) {
        this(new ObjectMapper(), valueType);
    }

    /**
     * Creates a new JacksonSerializer with the specified ObjectMapper and value type.
     *
     * @param objectMapper the ObjectMapper to use for serialization
     * @param valueType    the class type of values to serialize/deserialize
     * @throws IllegalArgumentException if objectMapper or valueType is null
     */
    public JacksonSerializer(ObjectMapper objectMapper, Class<V> valueType) {
        if (objectMapper == null) {
            throw new IllegalArgumentException("ObjectMapper cannot be null");
        }
        if (valueType == null) {
            throw new IllegalArgumentException("Value type cannot be null");
        }

        this.objectMapper = objectMapper;
        this.valueType = valueType;
    }

    @Override
    public String serialize(V value) {
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new CacheException("Failed to serialize value: " + value, e);
        }
    }

    @Override
    public V deserialize(String raw) {
        if (raw == null) {
            return null;
        }

        try {
            return objectMapper.readValue(raw, valueType);
        } catch (JsonProcessingException e) {
            throw new CacheException("Failed to deserialize value: " + raw, e);
        }
    }
}
