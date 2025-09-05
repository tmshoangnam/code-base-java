package io.github.base.starter.cache.caffeine;

import io.github.base.cache.api.Cache;
import io.github.base.cache.api.CacheException;
import io.github.base.cache.api.CacheManager;
import io.github.base.cache.spi.CacheProvider;

/**
 * CacheProvider implementation for Caffeine.
 * <p>
 * This provider creates Caffeine-based cache instances with configurable
 * TTL and maximum size settings. It's suitable for in-memory caching
 * scenarios where high performance and low latency are required.
 * <p>
 * <strong>Configuration:</strong> The provider accepts TTL (in seconds) and
 * maximum size parameters that will be applied to all caches created by
 * this provider.
 *
 * @since 1.0.0
 */
public final class CaffeineCacheProvider implements CacheProvider {

    /**
     * The provider name for Caffeine.
     */
    public static final String PROVIDER_NAME = "caffeine";

    private final long ttlSeconds;
    private final long maxSize;

    /**
     * Creates a new CaffeineCacheProvider with the specified configuration.
     *
     * @param ttlSeconds the time-to-live in seconds for cache entries
     * @param maxSize    the maximum number of entries in each cache
     * @throws IllegalArgumentException if ttlSeconds is negative or maxSize is not positive
     */
    public CaffeineCacheProvider(long ttlSeconds, long maxSize) {
        if (ttlSeconds < 0) {
            throw new IllegalArgumentException("TTL seconds cannot be negative: " + ttlSeconds);
        }
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size must be positive: " + maxSize);
        }

        this.ttlSeconds = ttlSeconds;
        this.maxSize = maxSize;
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public <K, V> Cache<K, V> createCache(String cacheName) {
        if (cacheName == null || cacheName.trim().isEmpty()) {
            throw new IllegalArgumentException("Cache name cannot be null or empty");
        }

        try {
            return new CaffeineCache<>(ttlSeconds, maxSize);
        } catch (Exception e) {
            throw new CacheException("Failed to create Caffeine cache: " + cacheName, e);
        }
    }

    @Override
    public CacheManager createCacheManager() {
        try {
            return new CaffeineCacheManager(ttlSeconds, maxSize);
        } catch (Exception e) {
            throw new CacheException("Failed to create Caffeine cache manager", e);
        }
    }
}
