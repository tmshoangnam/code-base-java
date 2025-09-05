package io.github.base.starter.cache.caffeine;

//import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.base.cache.api.Cache;
import io.github.base.cache.api.CacheException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine-backed Cache implementation.
 * <p>
 * This implementation wraps a Caffeine cache and provides the standard
 * cache interface. It supports configurable TTL (time-to-live) and
 * maximum size limits.
 * <p>
 * <strong>Thread Safety:</strong> This implementation is thread-safe as
 * it delegates to Caffeine's thread-safe cache implementation.
 * <p>
 * <strong>Configuration:</strong> The cache is configured with:
 * <ul>
 *   <li>Write expiration (TTL)</li>
 *   <li>Maximum size limit</li>
 * </ul>
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of values stored in this cache
 * @since 1.0.0
 */
public final class CaffeineCache<K, V> implements Cache<K, V> {

    private final com.github.benmanes.caffeine.cache.Cache<K, V> delegate;

    /**
     * Creates a new CaffeineCache with the specified configuration.
     *
     * @param ttlSeconds the time-to-live in seconds for cache entries
     * @param maxSize    the maximum number of entries in the cache
     * @throws IllegalArgumentException if ttlSeconds is negative or maxSize is not positive
     */
    public CaffeineCache(long ttlSeconds, long maxSize) {
        if (ttlSeconds < 0) {
            throw new IllegalArgumentException("TTL seconds cannot be negative: " + ttlSeconds);
        }
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size must be positive: " + maxSize);
        }

        Caffeine<Object, Object> builder = Caffeine.newBuilder()
                .maximumSize(maxSize);

        if (ttlSeconds > 0) {
            builder.expireAfterWrite(ttlSeconds, TimeUnit.SECONDS);
        }

        this.delegate = builder.build();
    }

    @Override
    public Optional<V> get(K key) {
        try {
            return Optional.ofNullable(delegate.getIfPresent(key));
        } catch (Exception e) {
            throw new CacheException("Failed to get key: " + key, e);
        }
    }

    @Override
    public void put(K key, V value) {
        try {
            delegate.put(key, value);
        } catch (Exception e) {
            throw new CacheException("Failed to put key: " + key, e);
        }
    }

    @Override
    public void evict(K key) {
        try {
            delegate.invalidate(key);
        } catch (Exception e) {
            throw new CacheException("Failed to evict key: " + key, e);
        }
    }

    @Override
    public void clear() {
        try {
            delegate.invalidateAll();
        } catch (Exception e) {
            throw new CacheException("Failed to clear cache", e);
        }
    }
}
