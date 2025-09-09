package io.github.base.starter.cache.autoconfig;

import io.github.base.cache.spi.CacheProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Redis cache provider.
 * <p>
 * These tests require a running Redis server and will be skipped if Redis is not available.
 *
 * @since 1.0.0
 */
class RedisIntegrationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CacheAutoConfiguration.class));

    /**
     * Checks if Redis is available by attempting to connect to localhost:6379.
     * This method is used by @EnabledIf to conditionally run Redis tests.
     *
     * @return true if Redis is available, false otherwise
     */
    @SuppressWarnings("unused")
    private static boolean isRedisAvailable() {
        try {
            // Try to create a simple Redis connection
            io.lettuce.core.RedisClient client = io.lettuce.core.RedisClient.create("redis://localhost:6379");
            try (io.lettuce.core.api.StatefulRedisConnection<String, String> connection = client.connect()) {
                connection.sync().ping();
                return true;
            } finally {
                client.shutdown();
            }
        } catch (Exception e) {
            System.out.println("Redis not available: " + e.getMessage());
            return false;
        }
    }

    @Test
    @EnabledIf("isRedisAvailable")
    void shouldConfigureRedisProviderWhenRedisIsAvailable() {
        contextRunner
                .withPropertyValues(
                        "base.cache.provider=redis",
                        "base.cache.redis.enabled=true",
                        "base.cache.redis.url=redis://localhost:6379",
                        "base.cache.redis.namespace=test"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(CacheProvider.class);

                    CacheProvider provider = context.getBean(CacheProvider.class);
                    assertThat(provider.getName()).isEqualTo("redis");
                });
    }

}
