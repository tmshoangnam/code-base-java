package io.github.base.starter.cache.redis.serializer;

/**
 * Simple serializer abstraction for cache values.
 * <p>
 * This interface provides a way to serialize and deserialize cache values
 * for storage in Redis. Different implementations can support various
 * serialization formats (JSON, binary, etc.).
 * <p>
 * <strong>Thread Safety:</strong> All implementations must be thread-safe.
 * <p>
 * <strong>Null Handling:</strong> Implementations should handle null values
 * appropriately, typically by returning null for both serialize and deserialize
 * operations.
 *
 * @param <K> the type of keys (typically String for Redis)
 * @param <V> the type of values to serialize/deserialize
 * @since 1.0.0
 */
public interface Serializer<K, V> {

    /**
     * Serializes the given value to a string representation.
     * <p>
     * The serialized string will be stored in Redis and should be
     * deserializable back to the original value using {@link #deserialize(String)}.
     *
     * @param value the value to serialize
     * @return the serialized string representation, or null if value is null
     * @throws io.github.base.cache.api.CacheException if serialization fails
     */
    String serialize(V value);

    /**
     * Deserializes the given string back to a value.
     * <p>
     * This method should be able to deserialize strings that were created
     * by {@link #serialize(Object)}.
     *
     * @param raw the serialized string representation
     * @return the deserialized value, or null if raw is null
     * @throws io.github.base.cache.api.CacheException if deserialization fails
     */
    V deserialize(String raw);
}
