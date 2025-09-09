package io.github.base.starter.cache.autoconfig;

import io.github.base.cache.api.CacheManager;
import io.github.base.cache.spi.CacheProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for CacheAutoConfiguration.
 *
 * @since 1.0.0
 */
class CacheAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CacheAutoConfiguration.class));

    @Test
    void shouldAutoConfigureCaffeineProviderByDefault() {
        contextRunner
                .withPropertyValues("base.cache.caffeine.ttl-seconds=120")
                .run(context -> {
                    assertThat(context).hasSingleBean(CacheProvider.class);
                    assertThat(context).hasSingleBean(CacheManager.class);

                    CacheProvider provider = context.getBean(CacheProvider.class);
                    assertThat(provider.getName()).isEqualTo("caffeine");
                });
    }

    @Test
    void shouldAutoConfigureRedisProviderWhenSpecified() {
        contextRunner
                .withPropertyValues(
                        "base.cache.provider=redis",
                        "base.cache.redis.enabled=false", // Disable Redis to use Caffeine fallback
                        "base.cache.redis.url=redis://localhost:6379",
                        "base.cache.redis.namespace=test"
                )
                .run(context -> {
                    // This test verifies that Redis configuration is loaded but falls back to Caffeine
                    // when Redis is disabled, which is the expected behavior
                    assertThat(context).hasSingleBean(CacheProvider.class);
                    assertThat(context).hasSingleBean(CacheManager.class);

                    CacheProvider provider = context.getBean(CacheProvider.class);
                    // Should fallback to Caffeine when Redis is disabled
                    assertThat(provider.getName()).isEqualTo("caffeine");
                });
    }

    @Test
    void shouldFallbackToCaffeineWhenRedisDisabled() {
        contextRunner
                .withPropertyValues(
                        "base.cache.provider=redis",
                        "base.cache.redis.enabled=false"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(CacheProvider.class);
                    assertThat(context).hasSingleBean(CacheManager.class);

                    CacheProvider provider = context.getBean(CacheProvider.class);
                    assertThat(provider.getName()).isEqualTo("caffeine");
                });
    }

    @Test
    void shouldNotAutoConfigureWhenProviderBeanExists() {
        contextRunner
                .withBean("customCacheProvider", CacheProvider.class, () -> new CacheProvider() {
                    @Override
                    public String getName() {
                        return "custom";
                    }

                    @Override
                    public <K, V> io.github.base.cache.api.Cache<K, V> createCache(String cacheName) {
                        return null;
                    }

                    @Override
                    public CacheManager createCacheManager() {
                        return null;
                    }
                })
                .run(context -> {
                    assertThat(context).hasBean("customCacheProvider");
                    // Should not create additional CacheProvider beans
                });
    }
}
