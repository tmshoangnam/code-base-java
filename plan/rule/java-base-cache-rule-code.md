1. Objective

Provide a cache abstraction library with:

Unified cache contracts (simple key-value, async, multi-level).

Basic SPI for plugging in cache providers (Caffeine, Redis, EHCache…).

Standard error handling for cache operations.

No Spring dependency, pure Java.

Starter handles wiring (Spring Boot auto-config, provider beans, metrics).

👉 Tư tưởng giống java-base-core:

cache = interface + contract.

starter = Spring Boot wiring + implementation binding.

2. Dependencies

Runtime (managed in BOM):
```xml
<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
</dependency>

<!-- Optional, only when implementing provider adapters in starter -->
<dependency>
  <groupId>com.github.ben-manes.caffeine</groupId>
  <artifactId>caffeine</artifactId>
  <scope>provided</scope>
</dependency>

<dependency>
  <groupId>io.lettuce</groupId>
  <artifactId>lettuce-core</artifactId>
  <scope>provided</scope>
</dependency>


Test only:

<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-core</artifactId>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>
```

Excluded:

No Spring, no Micrometer, no Redis client directly.

Starter mới add implementation (CaffeineCacheProvider, RedisCacheProvider…).

3.📦 Module & Package Structure (deliver exactly)

Module: java-base-cache (jar)
Top-level package: com.mycompany.base.cache

Suggested structure:

com.mycompany.base.cache
 ├─ api
 │   ├─ Cache.java                 // sync key-value API
 │   ├─ AsyncCache.java            // optional async API (CompletableFuture)
 │   ├─ CacheManager.java          // registry for named caches
 │   ├─ CacheException.java
 │   ├─ KeyGenerator.java
 │   └─ SimpleKeyGenerator.java
 ├─ spi
 │   └─ CacheProvider.java         // ServiceLoader SPI
 ├─ internal
 │   └─ (non-public helpers)
 └─ CacheServices.java             // ServiceLoader helper for runtime discovery


Module: java-base-starter (jar, Spring Boot starter)
Top-level package: com.mycompany.base.starter.cache

Suggested structure:

com.mycompany.base.starter.cache
 ├─ autoconfig
 │   ├─ CacheAutoConfiguration.java  // binds chosen provider by props
 │   ├─ CaffeineProperties.java
 │   └─ RedisProperties.java
 ├─ caffeine
 │   ├─ CaffeineCache.java
 │   ├─ CaffeineCacheManager.java
 │   └─ CaffeineCacheProvider.java
 ├─ redis
 │   ├─ RedisCache.java
 │   ├─ RedisCacheManager.java
 │   ├─ RedisCacheProvider.java
 │   └─ serializer
 │       ├─ Serializer.java         // minimal serializer SPI (String <-> V)
 │       └─ JacksonSerializer.java  // optional default using Jackson
 └─ resources
     └─ META-INF/services/com.mycompany.base.cache.spi.CacheProvider

4. Code Examples
```java
4.1 Core Cache Contract
package com.mycompany.base.cache.api;

import java.util.Optional;

/**
 * Minimal key-value cache abstraction.
 *
 * NOTE:
 * - Pure Java interface, no Spring or 3rd-party dependency.
 * - Implementations MUST be thread-safe.
 *
 * @since 1.0.0
 */
public interface Cache<K, V> {

    Optional<V> get(K key);

    void put(K key, V value);

    void evict(K key);

    void clear();
}

4.2 Async Extension
package com.mycompany.base.cache.api;

import java.util.concurrent.CompletableFuture;

/**
 * Optional asynchronous cache contract.
 *
 * NOTE:
 * - Useful for Redis or remote caches.
 * - Default implementation may delegate to sync cache.
 *
 * @since 1.0.0
 */
public interface AsyncCache<K, V> extends Cache<K, V> {

    CompletableFuture<V> getAsync(K key);

    CompletableFuture<Void> putAsync(K key, V value);

    CompletableFuture<Void> evictAsync(K key);
}

4.3 Cache Manager
package com.mycompany.base.cache.api;

import java.util.Map;

/**
 * Registry for named caches.
 *
 * Usage:
 * CacheManager mgr = ...
 * Cache<String, User> userCache = mgr.getCache("users");
 */
public interface CacheManager {

    <K, V> Cache<K, V> getCache(String name);

    Map<String, Cache<?, ?>> getAllCaches();
}

4.4 SPI for Providers
package com.mycompany.base.cache.spi;

import com.mycompany.base.cache.api.Cache;
import com.mycompany.base.cache.api.CacheManager;

/**
 * SPI for plugging in cache providers.
 *
 * Implementations discovered via ServiceLoader.
 *
 * Example providers:
 * - CaffeineCacheProvider
 * - RedisCacheProvider
 *
 * @since 1.0.0
 */
public interface CacheProvider {

    String getName();

    <K, V> Cache<K, V> createCache(String cacheName);

    CacheManager createCacheManager();
}

4.5 Unified Cache Exception
package com.mycompany.base.cache.api;

/**
 * Unified runtime exception for cache operations.
 */
public class CacheException extends RuntimeException {
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(String message) {
        super(message);
    }
}

4.6 Key Generator
package com.mycompany.base.cache.api;

/**
 * Strategy for generating cache keys.
 *
 * NOTE:
 * - Default implementation: SimpleKeyGenerator (join with ":").
 */
@FunctionalInterface
public interface KeyGenerator {
    String generate(Object... params);
}

1. Abstraction Layer (java-base-cache)

Đã có ở trên (Cache, AsyncCache, CacheManager, CacheProvider, CacheException, KeyGenerator).
Thêm 1 default SimpleKeyGenerator để dùng chung:

package com.mycompany.base.cache.api;

import java.util.Arrays;

/**
 * Default implementation of KeyGenerator.
 *
 * NOTE:
 * - Produces colon-separated keys.
 * - Example: "user:123:orders"
 */
public class SimpleKeyGenerator implements KeyGenerator {
    @Override
    public String generate(Object... params) {
        return Arrays.stream(params)
            .map(String::valueOf)
            .reduce((a, b) -> a + ":" + b)
            .orElse("");
    }
}

2. Implementation Layer (java-base-starter)
2.1 CaffeineCache Implementation
package com.mycompany.base.starter.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache as CaffeineNativeCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mycompany.base.cache.api.Cache;
import com.mycompany.base.cache.api.CacheException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine-backed Cache implementation.
 *
 * @since 1.0.0
 */
public class CaffeineCache<K, V> implements Cache<K, V> {

    private final CaffeineNativeCache<K, V> delegate;

    public CaffeineCache(long ttlSeconds, long maxSize) {
        this.delegate = Caffeine.newBuilder()
                .expireAfterWrite(ttlSeconds, TimeUnit.SECONDS)
                .maximumSize(maxSize)
                .build();
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
        delegate.invalidate(key);
    }

    @Override
    public void clear() {
        delegate.invalidateAll();
    }
}

2.2 CaffeineCacheManager
package com.mycompany.base.starter.cache.caffeine;

import com.mycompany.base.cache.api.Cache;
import com.mycompany.base.cache.api.CacheManager;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CacheManager backed by Caffeine caches.
 *
 * NOTE:
 * - Thread-safe.
 * - Caches created on-demand.
 */
public class CaffeineCacheManager implements CacheManager {

    private final Map<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();
    private final long ttlSeconds;
    private final long maxSize;

    public CaffeineCacheManager(long ttlSeconds, long maxSize) {
        this.ttlSeconds = ttlSeconds;
        this.maxSize = maxSize;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getCache(String name) {
        return (Cache<K, V>) caches.computeIfAbsent(name,
            n -> new CaffeineCache<>(ttlSeconds, maxSize));
    }

    @Override
    public Map<String, Cache<?, ?>> getAllCaches() {
        return Collections.unmodifiableMap(caches);
    }
}

2.3 Provider SPI Implementation
package com.mycompany.base.starter.cache.caffeine;

import com.mycompany.base.cache.api.Cache;
import com.mycompany.base.cache.api.CacheManager;
import com.mycompany.base.cache.spi.CacheProvider;

/**
 * CacheProvider implementation for Caffeine.
 *
 * @since 1.0.0
 */
public class CaffeineCacheProvider implements CacheProvider {

    private final long ttlSeconds;
    private final long maxSize;

    public CaffeineCacheProvider(long ttlSeconds, long maxSize) {
        this.ttlSeconds = ttlSeconds;
        this.maxSize = maxSize;
    }

    @Override
    public String getName() {
        return "caffeine";
    }

    @Override
    public <K, V> Cache<K, V> createCache(String cacheName) {
        return new CaffeineCache<>(ttlSeconds, maxSize);
    }

    @Override
    public CacheManager createCacheManager() {
        return new CaffeineCacheManager(ttlSeconds, maxSize);
    }
}

2.4 ServiceLoader Registration (resources/META-INF/services)

File: META-INF/services/com.mycompany.base.cache.spi.CacheProvider

com.mycompany.base.starter.cache.caffeine.CaffeineCacheProvider

2.5 Spring Boot Auto-Configuration
package com.mycompany.base.starter.cache.autoconfig;

import com.mycompany.base.cache.spi.CacheProvider;
import com.mycompany.base.starter.cache.caffeine.CaffeineCacheProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Caffeine-based cache.
 */
@Configuration
public class CacheAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "base.cache.caffeine")
    public CaffeineCacheProperties caffeineCacheProperties() {
        return new CaffeineCacheProperties();
    }

    @Bean
    @ConditionalOnMissingBean(CacheProvider.class)
    public CacheProvider cacheProvider(CaffeineCacheProperties props) {
        return new CaffeineCacheProvider(props.getTtlSeconds(), props.getMaxSize());
    }

    public static class CaffeineCacheProperties {
        private long ttlSeconds = 60;
        private long maxSize = 10_000;

        public long getTtlSeconds() { return ttlSeconds; }
        public void setTtlSeconds(long ttlSeconds) { this.ttlSeconds = ttlSeconds; }

        public long getMaxSize() { return maxSize; }
        public void setMaxSize(long maxSize) { this.maxSize = maxSize; }
    }
}

```


✅ RedisCacheProvider Full Flow
```java
1. RedisCache Implementation
package com.mycompany.base.starter.cache.redis;

import com.mycompany.base.cache.api.Cache;
import com.mycompany.base.cache.api.CacheException;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Optional;

/**
 * Redis-backed cache implementation (synchronous).
 *
 * @since 1.0.0
 */
public class RedisCache<K, V> implements Cache<K, V> {

    private final RedisCommands<String, String> commands;
    private final String namespace;
    private final Serializer<K, V> serializer;

    public RedisCache(RedisCommands<String, String> commands,
                      String namespace,
                      Serializer<K, V> serializer) {
        this.commands = commands;
        this.namespace = namespace;
        this.serializer = serializer;
    }

    private String buildKey(K key) {
        return namespace + ":" + key.toString();
    }

    @Override
    public Optional<V> get(K key) {
        try {
            String value = commands.get(buildKey(key));
            return Optional.ofNullable(serializer.deserialize(value));
        } catch (Exception e) {
            throw new CacheException("Failed to get from Redis: " + key, e);
        }
    }

    @Override
    public void put(K key, V value) {
        try {
            commands.set(buildKey(key), serializer.serialize(value));
        } catch (Exception e) {
            throw new CacheException("Failed to put into Redis: " + key, e);
        }
    }

    @Override
    public void evict(K key) {
        try {
            commands.del(buildKey(key));
        } catch (Exception e) {
            throw new CacheException("Failed to evict key from Redis: " + key, e);
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Clear not supported in RedisCache without scan");
    }
}

2. Serializer SPI
package com.mycompany.base.starter.cache.redis;

/**
 * Simple serializer abstraction.
 *
 * NOTE:
 * - Starter can provide JSON (Jackson/Gson) or custom implementations.
 */
public interface Serializer<K, V> {
    String serialize(V value);
    V deserialize(String raw);
}


Ví dụ default String serializer:

package com.mycompany.base.starter.cache.redis;

public class StringSerializer implements Serializer<String, String> {
    @Override
    public String serialize(String value) {
        return value;
    }

    @Override
    public String deserialize(String raw) {
        return raw;
    }
}

3. RedisCacheManager
package com.mycompany.base.starter.cache.redis;

import com.mycompany.base.cache.api.Cache;
import com.mycompany.base.cache.api.CacheManager;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis-backed CacheManager.
 */
public class RedisCacheManager implements CacheManager {

    private final RedisCommands<String, String> commands;
    private final Serializer<?, ?> serializer;
    private final String namespace;
    private final Map<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();

    public RedisCacheManager(RedisCommands<String, String> commands,
                             Serializer<?, ?> serializer,
                             String namespace) {
        this.commands = commands;
        this.serializer = serializer;
        this.namespace = namespace;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getCache(String name) {
        return (Cache<K, V>) caches.computeIfAbsent(name,
            n -> new RedisCache<>(commands, namespace + ":" + n, (Serializer<K, V>) serializer));
    }

    @Override
    public Map<String, Cache<?, ?>> getAllCaches() {
        return Collections.unmodifiableMap(caches);
    }
}

4. RedisCacheProvider
package com.mycompany.base.starter.cache.redis;

import com.mycompany.base.cache.api.Cache;
import com.mycompany.base.cache.api.CacheManager;
import com.mycompany.base.cache.spi.CacheProvider;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

/**
 * CacheProvider implementation for Redis (Lettuce).
 *
 * @since 1.0.0
 */
public class RedisCacheProvider implements CacheProvider {

    private final RedisClient client;
    private final StatefulRedisConnection<String, String> connection;
    private final Serializer<?, ?> serializer;
    private final String namespace;

    public RedisCacheProvider(String redisUrl, Serializer<?, ?> serializer, String namespace) {
        this.client = RedisClient.create(redisUrl);
        this.connection = client.connect();
        this.serializer = serializer;
        this.namespace = namespace;
    }

    @Override
    public String getName() {
        return "redis";
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> createCache(String cacheName) {
        return new RedisCache<>(
            connection.sync(),
            namespace + ":" + cacheName,
            (Serializer<K, V>) serializer
        );
    }

    @Override
    public CacheManager createCacheManager() {
        return new RedisCacheManager(connection.sync(), serializer, namespace);
    }
}

5. ServiceLoader Registration

File: META-INF/services/com.mycompany.base.cache.spi.CacheProvider

com.mycompany.base.starter.cache.caffeine.CaffeineCacheProvider
com.mycompany.base.starter.cache.redis.RedisCacheProvider

6. Spring Boot Auto-Configuration
package com.mycompany.base.starter.cache.autoconfig;

import com.mycompany.base.cache.spi.CacheProvider;
import com.mycompany.base.starter.cache.redis.RedisCacheProvider;
import com.mycompany.base.starter.cache.redis.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Redis cache.
 */
@Configuration
public class RedisCacheAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "base.cache.redis")
    public RedisCacheProperties redisCacheProperties() {
        return new RedisCacheProperties();
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisCacheProvider")
    public CacheProvider redisCacheProvider(RedisCacheProperties props) {
        return new RedisCacheProvider(props.getUrl(), new StringSerializer(), props.getNamespace());
    }

    public static class RedisCacheProperties {
        private String url = "redis://localhost:6379";
        private String namespace = "base";

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getNamespace() { return namespace; }
        public void setNamespace(String namespace) { this.namespace = namespace; }
    }
}
```
