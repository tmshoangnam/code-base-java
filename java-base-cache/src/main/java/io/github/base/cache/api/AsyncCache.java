package io.github.base.cache.api;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Optional asynchronous cache contract.
 * <p>
 * This interface extends the synchronous {@link Cache} interface with
 * asynchronous operations that return {@link CompletableFuture}.
 * <p>
 * <strong>Use Cases:</strong>
 * <ul>
 *   <li>Remote caches (Redis, Memcached) where network I/O is involved</li>
 *   <li>High-throughput applications where non-blocking operations are preferred</li>
 *   <li>Integration with reactive programming models</li>
 * </ul>
 * <p>
 * <strong>Implementation Note:</strong> Default implementations may delegate
 * to synchronous cache operations wrapped in CompletableFuture.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of values stored in this cache
 * @since 1.0.0
 */
public interface AsyncCache<K, V> extends Cache<K, V> {

    /**
     * Returns the value associated with the specified key asynchronously.
     * <p>
     * If the key is not present in the cache, the returned CompletableFuture
     * will complete with {@link Optional#empty()}.
     *
     * @param key the key whose associated value is to be returned
     * @return a CompletableFuture that will complete with an Optional containing
     *         the value, or empty if not found
     * @throws io.github.base.cache.api.CacheException if the operation fails
     */
    CompletableFuture<Optional<V>> getAsync(K key);

    /**
     * Associates the specified value with the specified key in this cache asynchronously.
     * <p>
     * If the cache previously contained a mapping for the key, the old value
     * is replaced by the specified value.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return a CompletableFuture that will complete when the operation finishes
     * @throws io.github.base.cache.api.CacheException if the operation fails
     */
    CompletableFuture<?> putAsync(K key, V value);

    /**
     * Removes the mapping for a key from this cache if it is present asynchronously.
     * <p>
     * If the key is not present, this operation has no effect.
     *
     * @param key the key whose mapping is to be removed from the cache
     * @return a CompletableFuture that will complete when the operation finishes
     * @throws io.github.base.cache.api.CacheException if the operation fails
     */
    CompletableFuture<?> evictAsync(K key);
}
