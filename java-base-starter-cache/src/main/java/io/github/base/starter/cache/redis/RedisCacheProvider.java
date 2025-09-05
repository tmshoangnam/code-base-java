package io.github.base.starter.cache.redis;

import io.github.base.cache.api.Cache;
import io.github.base.cache.api.CacheException;
import io.github.base.cache.api.CacheManager;
import io.github.base.cache.spi.CacheProvider;
import io.github.base.starter.cache.redis.serializer.Serializer;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CacheProvider implementation for Redis using Lettuce client.
 * <p>
 * This provider creates Redis-based cache instances using Lettuce Redis client.
 * It manages the Redis connection lifecycle and provides both synchronous
 * and asynchronous cache operations.
 * <p>
 * <strong>Connection Management:</strong> The provider creates and manages
 * a single Redis connection that is shared across all cache instances.
 * <p>
 * <strong>Serialization:</strong> Values are serialized using the provided
 * serializer before being stored in Redis.
 * <p>
 * <strong>Resource Cleanup:</strong> The Redis connection should be closed
 * when the provider is no longer needed to free up resources.
 * <p>
 * <strong>Lazy Connection:</strong> Connection is established lazily when
 * first cache operation is performed to avoid startup failures.

 * @since 1.0.0
 */
public final class RedisCacheProvider implements CacheProvider {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheProvider.class);

    /**
     * The provider name for Redis.
     */
    public static final String PROVIDER_NAME = "redis";

    private final String redisUrl;
    private final Serializer<?, ?> serializer;
    private final String namespace;
    private volatile RedisClient client;
    private volatile StatefulRedisConnection<String, String> connection;

    /**
     * Creates a new RedisCacheProvider with the specified configuration.
     * <p>
     * Note: Connection is established lazily when first cache operation is performed.
     *
     * @param redisUrl   the Redis server URL (e.g., "redis://localhost:6379")
     * @param serializer the serializer for cache values
     * @param namespace  the namespace prefix for all cache keys
     * @throws IllegalArgumentException if any parameter is null
     */
    public RedisCacheProvider(String redisUrl, Serializer<?, ?> serializer, String namespace) {
        if (redisUrl == null || redisUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Redis URL cannot be null or empty");
        }
        if (serializer == null) {
            throw new IllegalArgumentException("Serializer cannot be null");
        }
        if (namespace == null || namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Namespace cannot be null or empty");
        }

        this.redisUrl = redisUrl;
        this.serializer = serializer;
        this.namespace = namespace;
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> createCache(String cacheName) {
        if (cacheName == null || cacheName.trim().isEmpty()) {
            throw new IllegalArgumentException("Cache name cannot be null or empty");
        }

        try {
            ensureConnection();
            String cacheNamespace = namespace + ":" + cacheName;
            RedisCommands<String, String> syncCommands = connection.sync();
            RedisAsyncCommands<String, String> asyncCommands = connection.async();

            return new RedisCache<>(
                    syncCommands,
                    asyncCommands,
                    cacheNamespace,
                    (Serializer<K, V>) serializer
            );
        } catch (Exception e) {
            throw new CacheException("Failed to create Redis cache: " + cacheName, e);
        }
    }

    @Override
    public CacheManager createCacheManager() {
        try {
            ensureConnection();
            RedisCommands<String, String> syncCommands = connection.sync();
            RedisAsyncCommands<String, String> asyncCommands = connection.async();

            return new RedisCacheManager(
                    syncCommands,
                    asyncCommands,
                    serializer,
                    namespace
            );
        } catch (Exception e) {
            throw new CacheException("Failed to create Redis cache manager", e);
        }
    }

    /**
     * Ensures that Redis connection is established.
     * <p>
     * This method implements double-checked locking to ensure thread-safe
     * lazy initialization of the Redis connection.
     *
     * @throws CacheException if connection fails
     */
    private void ensureConnection() {
        if (connection == null) {
            synchronized (this) {
                if (connection == null) {
                    try {
                        this.client = RedisClient.create(redisUrl);
                        this.connection = client.connect();
                        
                        // Test the connection with a simple ping
                        connection.sync().ping();
                        logger.info("Successfully connected to Redis at: {}", redisUrl);
                    } catch (Exception e) {
                        logger.error("Failed to connect to Redis at: {}", redisUrl, e);
                        // Clean up resources if connection fails
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (Exception closeEx) {
                                logger.warn("Failed to close Redis connection during cleanup", closeEx);
                            }
                            connection = null;
                        }
                        if (client != null) {
                            try {
                                client.shutdown();
                            } catch (Exception closeEx) {
                                logger.warn("Failed to shutdown Redis client during cleanup", closeEx);
                            }
                            client = null;
                        }
                        throw new CacheException("Failed to connect to Redis: " + redisUrl, e);
                    }
                }
            }
        }
    }

    /**
     * Closes the Redis connection and releases resources.
     * <p>
     * This method should be called when the provider is no longer needed
     * to properly clean up Redis connections.
     */
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (client != null) {
                client.shutdown();
            }
        } catch (Exception e) {
            // Log warning but don't throw exception during cleanup
            System.err.println("Warning: Failed to close Redis connection: " + e.getMessage());
        }
    }
}
