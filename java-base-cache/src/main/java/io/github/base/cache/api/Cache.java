package io.github.base.cache.api;

import java.util.Optional;

/**
 * Minimal key-value cache abstraction.
 * <p>
 * This interface provides a simple, thread-safe cache contract that can be
 * implemented by various cache providers (Caffeine, Redis, etc.).
 * <p>
 * <strong>Thread Safety:</strong> All implementations must be thread-safe.
 * <p>
 * <strong>Null Handling:</strong> Keys and values may be null depending on
 * the underlying implementation. Check provider documentation for specifics.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of values stored in this cache
 * @since 1.0.0
 */
public interface Cache<K, V> {

    /**
     * Returns the value associated with the specified key.
     * <p>
     * If the key is not present in the cache, returns {@link Optional#empty()}.
     *
     * @param key the key whose associated value is to be returned
     * @return an Optional containing the value, or empty if not found
     * @throws io.github.base.cache.api.CacheException if the operation fails
     */
    Optional<V> get(K key);

    /**
     * Associates the specified value with the specified key in this cache.
     * <p>
     * If the cache previously contained a mapping for the key, the old value
     * is replaced by the specified value.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @throws io.github.base.cache.api.CacheException if the operation fails
     */
    void put(K key, V value);

    /**
     * Removes the mapping for a key from this cache if it is present.
     * <p>
     * If the key is not present, this operation has no effect.
     *
     * @param key the key whose mapping is to be removed from the cache
     * @throws io.github.base.cache.api.CacheException if the operation fails
     */
    void evict(K key);

    /**
     * Removes all mappings from this cache.
     * <p>
     * The cache will be empty after this call returns.
     * <p>
     * <strong>Note:</strong> Some implementations may not support this operation
     * (e.g., distributed caches). In such cases, this method may throw
     * {@link UnsupportedOperationException}.
     *
     * @throws io.github.base.cache.api.CacheException if the operation fails
     * @throws UnsupportedOperationException if clear is not supported
     */
    void clear();
}
