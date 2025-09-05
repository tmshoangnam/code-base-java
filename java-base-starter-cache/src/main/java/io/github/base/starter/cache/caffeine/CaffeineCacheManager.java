package io.github.base.starter.cache.caffeine;

import io.github.base.cache.api.Cache;
import io.github.base.cache.api.CacheException;
import io.github.base.cache.api.CacheManager;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CacheManager backed by Caffeine caches.
 * <p>
 * This cache manager creates and manages multiple Caffeine-based caches.
 * Each cache is created on-demand with the same configuration (TTL and max size).
 * <p>
 * <strong>Thread Safety:</strong> This implementation is thread-safe and uses
 * ConcurrentHashMap for cache storage.
 * <p>
 * <strong>Cache Creation:</strong> Caches are created lazily when first requested
 * through {@link #getCache(String)}.
 *
 * @since 1.0.0
 */
public final class CaffeineCacheManager implements CacheManager {

    private final Map<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();
    private final long ttlSeconds;
    private final long maxSize;

    /**
     * Creates a new CaffeineCacheManager with the specified configuration.
     *
     * @param ttlSeconds the time-to-live in seconds for all caches
     * @param maxSize    the maximum number of entries for all caches
     * @throws IllegalArgumentException if ttlSeconds is negative or maxSize is not positive
     */
    public CaffeineCacheManager(long ttlSeconds, long maxSize) {
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
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getCache(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Cache name cannot be null or empty");
        }

        return (Cache<K, V>) caches.computeIfAbsent(name, this::createCache);
    }

    @Override
    public Map<String, Cache<?, ?>> getAllCaches() {
        return Collections.unmodifiableMap(caches);
    }

    /**
     * Creates a new CaffeineCache instance.
     *
     * @param name the name of the cache to create
     * @return a new CaffeineCache instance
     */
    private Cache<?, ?> createCache(String name) {
        try {
            return new CaffeineCache<>(ttlSeconds, maxSize);
        } catch (Exception e) {
            throw new CacheException("Failed to create cache: " + name, e);
        }
    }
}
