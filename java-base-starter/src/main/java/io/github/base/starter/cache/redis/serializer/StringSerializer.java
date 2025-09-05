package io.github.base.starter.cache.redis.serializer;

/**
 * Default string serializer that performs no transformation.
 * <p>
 * This serializer is suitable for cases where cache values are already strings
 * or when simple string-based caching is sufficient.
 * <p>
 * <strong>Thread Safety:</strong> This implementation is thread-safe and stateless.
 *
 * @since 1.0.0
 */
public final class StringSerializer implements Serializer<String, String> {

    /**
     * Creates a new StringSerializer instance.
     */
    public StringSerializer() {
        // Stateless implementation
    }

    @Override
    public String serialize(String value) {
        return value;
    }

    @Override
    public String deserialize(String raw) {
        return raw;
    }
}
