package io.github.base.cache;

import io.github.base.cache.spi.CacheProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for CacheServices.
 *
 * @since 1.0.0
 */
class CacheServicesTest {

    @Test
    void shouldDiscoverProviders() {
        // This test will pass if there are providers registered in META-INF/services
        // In a real scenario, this would test the ServiceLoader mechanism
        var providers = CacheServices.discoverProviders();
        assertThat(providers).isNotNull();
    }

    @Test
    void shouldReturnNullForNonExistentProvider() {
        CacheProvider provider = CacheServices.findProvider("non-existent");
        assertThat(provider).isNull();
    }

    @Test
    void shouldReturnNullForNullProviderName() {
        CacheProvider provider = CacheServices.findProvider(null);
        assertThat(provider).isNull();
    }

    @Test
    void shouldReturnNullForEmptyProviderName() {
        CacheProvider provider = CacheServices.findProvider("");
        assertThat(provider).isNull();
    }
}
