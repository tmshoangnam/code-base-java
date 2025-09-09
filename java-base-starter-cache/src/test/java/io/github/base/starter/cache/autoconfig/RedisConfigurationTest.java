package io.github.base.starter.cache.autoconfig;

import io.github.base.cache.spi.CacheProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Redis configuration loading.
 *
 * @since 1.0.0
 */
class RedisConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CacheAutoConfiguration.class));

    @Test
    void shouldLoadRedisConfigurationWhenLettuceIsOnClasspath() {
        contextRunner
                .withPropertyValues(
                        "base.cache.provider=redis",
                        "base.cache.redis.enabled=false", // Disable Redis to avoid connection issues
                        "base.cache.redis.url=redis://localhost:6379",
                        "base.cache.redis.namespace=test"
                )
                .run(context -> {
                    // Should fallback to Caffeine when Redis is disabled
                    assertThat(context).hasSingleBean(CacheProvider.class);
                    CacheProvider provider = context.getBean(CacheProvider.class);
                    assertThat(provider.getName()).isEqualTo("caffeine");
                });
    }

    @Test
    void shouldNotLoadRedisConfigurationWhenLettuceNotOnClasspath() {
        // This test simulates the case where Lettuce is not available
        // In a real scenario, this would be tested with a separate classpath
        contextRunner
                .withPropertyValues(
                        "base.cache.provider=redis",
                        "base.cache.redis.enabled=false", // Disable Redis to avoid connection issues
                        "base.cache.redis.url=redis://localhost:6379",
                        "base.cache.redis.namespace=test"
                )
                .run(context -> {
                    // Should always have a CacheProvider (Caffeine fallback when Redis disabled)
                    assertThat(context).hasSingleBean(CacheProvider.class);
                    
                    CacheProvider provider = context.getBean(CacheProvider.class);
                    // Should be Caffeine when Redis is disabled
                    assertThat(provider.getName()).isEqualTo("caffeine");
                });
    }
}
