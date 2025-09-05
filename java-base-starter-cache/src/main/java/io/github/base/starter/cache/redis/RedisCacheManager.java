package io.github.base.starter.cache.redis;

import io.github.base.cache.api.Cache;
import io.github.base.cache.api.CacheException;
import io.github.base.cache.api.CacheManager;
import io.github.base.starter.cache.redis.serializer.Serializer;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis-backed CacheManager.
 * <p>
 * This cache manager creates and manages multiple Redis-based caches.
 * Each cache is created on-demand with the same configuration (connection,
 * serializer, and namespace prefix).
 * <p>
 * <strong>Thread Safety:</strong> This implementation is thread-safe and uses
 * ConcurrentHashMap for cache storage.
 * <p>
 * <strong>Cache Creation:</strong> Caches are created lazily when first requested
 * through {@link #getCache(String)}. Each cache gets its own namespace to avoid
 * key collisions.
 *
 * @since 1.0.0
 */
public final class RedisCacheManager implements CacheManager {

    private final Map<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();
    private final RedisCommands<String, String> syncCommands;
    private final RedisAsyncCommands<String, String> asyncCommands;
    private final Serializer<?, ?> serializer;
    private final String baseNamespace;

    /**
     * Creates a new RedisCacheManager with the specified configuration.
     *
     * @param syncCommands   the synchronous Redis commands
     * @param asyncCommands  the asynchronous Redis commands
     * @param serializer     the serializer for cache values
     * @param baseNamespace  the base namespace for all caches
     * @throws IllegalArgumentException if any parameter is null
     */
    public RedisCacheManager(RedisCommands<String, String> syncCommands,
                             RedisAsyncCommands<String, String> asyncCommands,
                             Serializer<?, ?> serializer,
                             String baseNamespace) {
        if (syncCommands == null) {
            throw new IllegalArgumentException("Sync commands cannot be null");
        }
        if (asyncCommands == null) {
            throw new IllegalArgumentException("Async commands cannot be null");
        }
        if (serializer == null) {
            throw new IllegalArgumentException("Serializer cannot be null");
        }
        if (baseNamespace == null) {
            throw new IllegalArgumentException("Base namespace cannot be null");
        }

        this.syncCommands = syncCommands;
        this.asyncCommands = asyncCommands;
        this.serializer = serializer;
        this.baseNamespace = baseNamespace;
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
     * Creates a new RedisCache instance for the specified cache name.
     *
     * @param name the name of the cache to create
     * @return a new RedisCache instance
     */
    @SuppressWarnings("unchecked")
    private Cache<?, ?> createCache(String name) {
        try {
            String cacheNamespace = baseNamespace + ":" + name;
            return new RedisCache<>(
                    syncCommands,
                    asyncCommands,
                    cacheNamespace,
                    (Serializer<Object, Object>) serializer
            );
        } catch (Exception e) {
            throw new CacheException("Failed to create Redis cache: " + name, e);
        }
    }
}
