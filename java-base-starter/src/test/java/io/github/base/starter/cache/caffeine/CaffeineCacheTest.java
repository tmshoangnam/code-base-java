package io.github.base.starter.cache.caffeine;

import io.github.base.cache.api.CacheException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for CaffeineCache.
 *
 * @since 1.0.0
 */
class CaffeineCacheTest {

    private CaffeineCache<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new CaffeineCache<>(60, 1000);
    }

    @Test
    void shouldStoreAndRetrieveValue() {
        cache.put("key1", "value1");
        
        Optional<String> result = cache.get("key1");
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo("value1");
    }

    @Test
    void shouldReturnEmptyForNonExistentKey() {
        Optional<String> result = cache.get("non-existent");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldEvictKey() {
        cache.put("key1", "value1");
        cache.evict("key1");
        
        Optional<String> result = cache.get("key1");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldClearAllEntries() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.clear();
        
        assertThat(cache.get("key1")).isEmpty();
        assertThat(cache.get("key2")).isEmpty();
    }

    @Test
    void shouldThrowExceptionForNegativeTtl() {
        assertThatThrownBy(() -> new CaffeineCache<>(-1, 1000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("TTL seconds cannot be negative: -1");
    }

    @Test
    void shouldThrowExceptionForNonPositiveMaxSize() {
        assertThatThrownBy(() -> new CaffeineCache<>(60, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Max size must be positive: 0");
    }
}
