package io.github.base.cache.spi;

import io.github.base.cache.api.Cache;
import io.github.base.cache.api.CacheManager;

/**
 * Service Provider Interface (SPI) for plugging in cache providers.
 * <p>
 * This interface allows different cache implementations to be discovered and
 * used at runtime through the Java ServiceLoader mechanism. Implementations
 * of this interface should be registered in the {@code META-INF/services}
 * directory.
 * <p>
 * <strong>Provider Discovery:</strong> Providers are discovered using
 * {@link java.util.ServiceLoader} and can be used both in Spring Boot
 * applications (via auto-configuration) and in plain Java applications.
 * <p>
 * <strong>Example Providers:</strong>
 * <ul>
 *   <li>CaffeineCacheProvider - for in-memory caching with Caffeine</li>
 *   <li>RedisCacheProvider - for distributed caching with Redis</li>
 *   <li>EhCacheProvider - for enterprise caching with EhCache</li>
 * </ul>
 *
 * @since 1.0.0
 */
public interface CacheProvider {

    /**
     * Returns the unique name of this cache provider.
     * <p>
     * The name should be unique across all providers and is typically used
     * for configuration and selection purposes.
     * <p>
     * <strong>Examples:</strong> "caffeine", "redis", "ehcache"
     *
     * @return the provider name, never null or empty
     */
    String getName();

    /**
     * Creates a new cache instance with the specified name.
     * <p>
     * The cache will be created using the default configuration for this
     * provider. Different providers may have different default configurations
     * (TTL, max size, etc.).
     *
     * @param <K>       the type of keys for the cache
     * @param <V>       the type of values for the cache
     * @param cacheName the name of the cache to create
     * @return a new cache instance, never null
     * @throws IllegalArgumentException if cacheName is null or empty
     * @throws io.github.base.cache.api.CacheException if cache creation fails
     */
    <K, V> Cache<K, V> createCache(String cacheName);

    /**
     * Creates a new cache manager instance.
     * <p>
     * The cache manager will be configured with the default settings for
     * this provider and will be responsible for managing multiple named caches.
     *
     * @return a new cache manager instance, never null
     * @throws io.github.base.cache.api.CacheException if cache manager creation fails
     */
    CacheManager createCacheManager();
}
