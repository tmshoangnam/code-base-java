You are a Senior Software Engineer + System Architect (or an AI coding agent acting like one).
Your task: design and implement a production-ready, enterprise-grade cache foundation split into two modules:
	• java-base-cache — pure Java library (jar) that defines cache contracts, SPI, and utilities (no Spring, no client libs).
	• java-base-starter — Spring Boot starter that provides concrete provider implementations and auto-configuration for Caffeine and Redis (Lettuce), and demonstrates full runtime wiring (including ServiceLoader fallback).
Be strict about boundaries: the core module must have no runtime dependency on Spring or specific cache clients; starter holds implementations.
🎯 Objectives
	1. Provide a clean, stable cache abstraction (sync + async) and provider SPI that is:
		○ Framework-agnostic (jar only).
		○ Backwards-compatible and minimal dependency.
		○ Thread-safe and production-sensible.
	2. Provide a starter module that:
		○ Supplies CaffeineCacheProvider and RedisCacheProvider (Lettuce).
		○ Uses Spring Boot auto-configuration and @ConfigurationProperties.
		○ Supports ServiceLoader discovery as fallback to Spring beans.
		○ Allows selecting provider by properties (e.g., base.cache.provider = caffeine|redis).
	3. Provide clear, minimal, safe defaults for serialization, namespaces, TTL, max-size.
	4. Keep separation of concerns: metrics, tracing, and instrumentation remain optional and wired by starter (not core). No logging implementation forced.
✅ Requirements & Constraints
	• Java 17 compatible.
	• Core module (java-base-cache) must not depend on Spring, Lettuce, Caffeine, Jackson, or other provider-specific libs.
	• Starter may depend on:
		○ com.github.ben-manes.caffeine:caffeine
		○ io.lettuce:lettuce-core
		○ com.fasterxml.jackson.core:jackson-databind (optional, for JSON serializer)
		○ org.springframework.boot:spring-boot-autoconfigure / spring-boot-starter
	• Use ServiceLoader in core as optional SPI discovery mechanism; starter should register providers for Spring and also include META-INF/services entries.
	• Provide both synchronous and asynchronous (CompletableFuture) APIs where appropriate (Redis driver is async-capable).
	• Keep error handling uniform: CacheException runtime for failures.
	• Serializer SPI: core should define a simple interface? Better: define in starter; but core should accept String keys / generic values and let provider accept serializers. (Make a safe, explicit design decision in prompt.)
📦 Module & Package Structure (deliver exactly)
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

🧩 API Contracts — minimal, precise examples (must be implemented)
Provide code for these core interfaces/classes (no Spring annotations):
	1. Cache<K,V> — Optional<V> get(K key), void put(K key, V value), void evict(K key), void clear().
	2. AsyncCache<K,V> — extends Cache and adds CompletableFuture<V> getAsync(K key), CompletableFuture<Void> putAsync(K key, V value), CompletableFuture<Void> evictAsync(K key).
	3. CacheManager — <K,V> Cache<K,V> getCache(String name), Map<String, Cache<?,?>> getAllCaches().
	4. CacheProvider (SPI) — String getName(), <K,V> Cache<K,V> createCache(String cacheName), CacheManager createCacheManager().
	5. CacheException — runtime wrapper for provider errors.
	6. KeyGenerator + SimpleKeyGenerator.
Include minimal Javadoc explaining thread-safety and semantics.
🧪 Starter Implementations — Caffeine + Redis (deliver full, safe code)
	• CaffeineCache and CaffeineCacheManager that wrap Caffeine native cache, configurable TTL and max-size.
	• CaffeineCacheProvider implements CacheProvider.
	• RedisCache and RedisCacheManager using Lettuce RedisClient and StatefulRedisConnection:
		○ Provide synchronous wrappers using connection.sync() and also async methods via connection.async().
		○ Provide Serializer<K,V> SPI in starter: default StringSerializer and optional JacksonSerializer.
		○ Make clear() throw UnsupportedOperationException or implement safe SCAN-based deletion only if namespace is provided — be explicit in README.
	• RedisCacheProvider implements CacheProvider. Manage connection lifecycle and offer graceful shutdown hooks if possible (close connection).
	• Register providers in META-INF/services.
🔧 Auto-configuration behaviour (exact)
	• Spring property root: base.cache.
	• base.cache.provider = caffeine | redis (default: caffeine).
	• Provide @ConfigurationProperties classes CaffeineProperties & RedisProperties.
	• CacheAutoConfiguration should:
		○ Create a CacheProvider bean based on base.cache.provider.
		○ Expose CacheManager bean (from provider.createCacheManager()).
		○ Use @ConditionalOnMissingBean to allow override.
		○ Avoid classloading issues when Lettuce/Caffeine not present: use @ConditionalOnClass for provider auto-config, but still safely fallback to ServiceLoader for non-Spring consumers.
	• Document how to enable both providers on the classpath but select one at runtime.
🔐 Safety & Compatibility
	• Starter must not force load Lettuce/Caffeine when those classes are absent; use conditional loading.
	• Avoid shading or conflicting transitive dependencies; declare provider client deps with appropriate scope in BOM/pom.
	• Keep class names and packages specific to avoid collisions with other libraries.
	• All public APIs must be stable (annotate @since where appropriate).
📘 Tests & Quality
	• Unit tests in core validating contracts and key generator (use JUnit 5 + AssertJ).
	• Starter unit/integration tests: mock Lettuce connections for Redis; use embedded Caffeine (in-memory) for Caffeine tests.
	• Achieve ≥ 80% coverage for core; starter has separate coverage goals.
	• Provide example integration test demonstrating Spring Boot wiring of provider via properties.
🧾 Deliverables (explicit)
For each module produce:
	1. Maven POM (parent + module POMs) with BOM management and minimal dependencies.
	2. Fully implemented and documented Java classes for all interfaces and sample implementations listed above.
	3. META-INF/services entries for providers.
	4. Spring Boot auto-configuration classes with @ConfigurationProperties.
	5. Unit tests & at least one integration test for starter wiring.
	6. README.md for the repo explaining:
		○ Architecture and separation of concerns.
		○ How to add new providers.
		○ How to select provider and configuration properties.
		○ Limitations (e.g., Redis clear() semantics).
		○ Security notes and production tips (connection pooling, serializers, namespace).
	7. Example usage snippets demonstrating:
		○ Using provider via CacheProvider bean in Spring.
		○ Using ServiceLoader discovery outside Spring.
		○ Example of multi-level cache composition (optional section).
	8. Code style: JavaDoc on public APIs, final static utility classes with private constructors, clean exception messages, no logging impl in core.
🧭 Non-functional expectations
	• Production ready: consider connection lifecycle, thread-safety, resource cleanup, and failure modes.
	• No surprising behavior: document default TTL, eviction, serialization format.
	• Ease of use for application developers: simple API, clear examples.
🚀 Expected Output (what you must return)
Produce a single artifact (or repo template) containing two modules with code and tests described above. At minimum in your response include:
	• The complete, ready-to-build Maven POM files (parent + module poms).
	• Full source code for:
		○ Core interfaces/classes (Cache, AsyncCache, CacheManager, CacheProvider, KeyGenerator, CacheException).
		○ Caffeine implementation classes and provider.
		○ Redis implementation classes and provider (Lettuce) and default serializer (String + Jackson).
		○ Spring Boot auto-configuration classes and properties classes.
	• META-INF/services contents.
	• Representative unit tests (core + starter).
	• README.md content.
	• Short usage guide and examples.
	• A short checklist of safety and compatibility checks you performed or recommend (e.g., conditional @ConditionalOnClass, resource cleanup).
Be practical: if a full feature is risky (e.g., Redis clear() for production), implement a safe default and document trade-offs.
