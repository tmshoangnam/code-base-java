package io.github.base.cache.api;

import java.util.Map;

/**
 * Registry for named caches.
 * <p>
 * A CacheManager provides a way to create and manage multiple named caches.
 * Each cache is identified by a unique name and can have different configurations.
 * <p>
 * <strong>Thread Safety:</strong> All implementations must be thread-safe.
 * <p>
 * <strong>Usage Example:</strong>
 * <pre>{@code
 * CacheManager manager = ...;
 * Cache<String, User> userCache = manager.getCache("users");
 * Cache<String, Product> productCache = manager.getCache("products");
 * }</pre>
 *
 * @since 1.0.0
 */
public interface CacheManager {

    /**
     * Returns the cache instance associated with the specified name.
     * <p>
     * If a cache with the given name does not exist, it will be created
     * using the default configuration for this cache manager.
     * <p>
     * The returned cache instance is guaranteed to be the same for subsequent
     * calls with the same name, ensuring thread-safe access to the same cache.
     *
     * @param <K>  the type of keys for the cache
     * @param <V>  the type of values for the cache
     * @param name the name of the cache to retrieve
     * @return the cache instance, never null
     * @throws IllegalArgumentException if the name is null or empty
     * @throws io.github.base.cache.api.CacheException if cache creation fails
     */
    <K, V> Cache<K, V> getCache(String name);

    /**
     * Returns an unmodifiable view of all caches managed by this manager.
     * <p>
     * The returned map contains all caches that have been created through
     * {@link #getCache(String)}. The map is a snapshot at the time of the call
     * and will not reflect subsequent cache creations.
     *
     * @return an unmodifiable map of cache names to cache instances
     */
    Map<String, Cache<?, ?>> getAllCaches();
}
