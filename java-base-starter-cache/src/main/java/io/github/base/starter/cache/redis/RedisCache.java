package io.github.base.starter.cache.redis;

import io.github.base.cache.api.AsyncCache;
import io.github.base.cache.api.Cache;
import io.github.base.cache.api.CacheException;
import io.github.base.starter.cache.redis.serializer.Serializer;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Redis-backed cache implementation with both synchronous and asynchronous operations.
 * <p>
 * This implementation uses Lettuce Redis client to provide cache operations
 * against a Redis server. It supports both synchronous and asynchronous
 * operations through the {@link AsyncCache} interface.
 * <p>
 * <strong>Thread Safety:</strong> This implementation is thread-safe as
 * Lettuce Redis client is thread-safe.
 * <p>
 * <strong>Serialization:</strong> Values are serialized using the provided
 * {@link Serializer} before being stored in Redis.
 * <p>
 * <strong>Key Namespacing:</strong> All keys are prefixed with the namespace
 * to avoid collisions between different cache instances.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of values stored in this cache
 * @since 1.0.0
 */
public final class RedisCache<K, V> implements AsyncCache<K, V> {

    private final RedisCommands<String, String> syncCommands;
    private final RedisAsyncCommands<String, String> asyncCommands;
    private final String namespace;
    private final Serializer<K, V> serializer;

    /**
     * Creates a new RedisCache with the specified configuration.
     *
     * @param syncCommands  the synchronous Redis commands
     * @param asyncCommands the asynchronous Redis commands
     * @param namespace     the namespace prefix for all keys
     * @param serializer    the serializer for values
     * @throws IllegalArgumentException if any parameter is null
     */
    public RedisCache(RedisCommands<String, String> syncCommands,
                      RedisAsyncCommands<String, String> asyncCommands,
                      String namespace,
                      Serializer<K, V> serializer) {
        if (syncCommands == null) {
            throw new IllegalArgumentException("Sync commands cannot be null");
        }
        if (asyncCommands == null) {
            throw new IllegalArgumentException("Async commands cannot be null");
        }
        if (namespace == null) {
            throw new IllegalArgumentException("Namespace cannot be null");
        }
        if (serializer == null) {
            throw new IllegalArgumentException("Serializer cannot be null");
        }

        this.syncCommands = syncCommands;
        this.asyncCommands = asyncCommands;
        this.namespace = namespace;
        this.serializer = serializer;
    }

    @Override
    public Optional<V> get(K key) {
        try {
            String redisKey = buildKey(key);
            String value = syncCommands.get(redisKey);
            return Optional.ofNullable(serializer.deserialize(value));
        } catch (Exception e) {
            throw new CacheException("Failed to get from Redis: " + key, e);
        }
    }

    @Override
    public void put(K key, V value) {
        try {
            String redisKey = buildKey(key);
            String serializedValue = serializer.serialize(value);
            syncCommands.set(redisKey, serializedValue);
        } catch (Exception e) {
            throw new CacheException("Failed to put into Redis: " + key, e);
        }
    }

    @Override
    public void evict(K key) {
        try {
            String redisKey = buildKey(key);
            syncCommands.del(redisKey);
        } catch (Exception e) {
            throw new CacheException("Failed to evict key from Redis: " + key, e);
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(
                "Clear operation is not supported for Redis cache. " +
                "Use Redis SCAN command or delete the entire namespace manually."
        );
    }

    @Override
    public CompletableFuture<Optional<V>> getAsync(K key) {
        try {
            String redisKey = buildKey(key);
            RedisFuture<String> future = asyncCommands.get(redisKey);

            return future.thenApply(value ->
                Optional.ofNullable(serializer.deserialize(value))
            ).toCompletableFuture();
        } catch (Exception e) {
            CompletableFuture<Optional<V>> failed = new CompletableFuture<>();
            failed.completeExceptionally(new CacheException("Failed to get from Redis: " + key, e));
            return failed;
        }
    }

    @Override
    public CompletableFuture<?> putAsync(K key, V value) {
        try {
            String redisKey = buildKey(key);
            String serializedValue = serializer.serialize(value);
            RedisFuture<String> future = asyncCommands.set(redisKey, serializedValue);

            return future.thenApply(result -> null).toCompletableFuture();
        } catch (Exception e) {
            CompletableFuture<Void> failed = new CompletableFuture<>();
            failed.completeExceptionally(new CacheException("Failed to put into Redis: " + key, e));
            return failed;
        }
    }

    @Override
    public CompletableFuture<?> evictAsync(K key) {
        try {
            String redisKey = buildKey(key);
            RedisFuture<Long> future = asyncCommands.del(redisKey);

            return future.thenApply(result -> null).toCompletableFuture();
        } catch (Exception e) {
            CompletableFuture<Void> failed = new CompletableFuture<>();
            failed.completeExceptionally(new CacheException("Failed to evict key from Redis: " + key, e));
            return failed;
        }
    }

    /**
     * Builds the full Redis key by combining namespace and the provided key.
     *
     * @param key the cache key
     * @return the full Redis key with namespace prefix
     */
    private String buildKey(K key) {
        return namespace + ":" + key.toString();
    }
}
