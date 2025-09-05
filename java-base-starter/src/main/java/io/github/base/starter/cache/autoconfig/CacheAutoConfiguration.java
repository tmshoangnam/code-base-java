package io.github.base.starter.cache.autoconfig;

import io.github.base.cache.api.CacheManager;
import io.github.base.cache.spi.CacheProvider;
import io.github.base.starter.cache.caffeine.CaffeineCacheProvider;
import io.github.base.starter.cache.redis.RedisCacheProvider;
import io.github.base.starter.cache.redis.serializer.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * Auto-configuration for cache providers.
 * <p>
 * This configuration class automatically configures cache providers based on
 * the available dependencies and configuration properties. It supports both
 * Caffeine and Redis providers with conditional loading to avoid classloading
 * issues when dependencies are not present.
 * <p>
 * <strong>Provider Selection:</strong> The provider is selected based on the
 * {@code base.cache.provider} property. If not specified, Caffeine is used
 * as the default provider.
 * <p>
 * <strong>Conditional Loading:</strong> Each provider configuration is
 * conditionally loaded based on the presence of required classes to avoid
 * ClassNotFoundException when dependencies are not available.
 * <p>
 * <strong>Fallback Strategy:</strong> If Redis is configured but not available,
 * the system will fall back to Caffeine automatically.

 * @since 1.0.0
 */
@AutoConfiguration
@EnableConfigurationProperties({CaffeineProperties.class, RedisProperties.class})
public class CacheAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CacheAutoConfiguration.class);

    /**
     * Auto-configuration for Caffeine cache provider.
     * <p>
     * This configuration is only loaded when Caffeine classes are present
     * on the classpath and the provider is set to "caffeine" or not specified.
     */
    @ConditionalOnClass(name = "com.github.benmanes.caffeine.cache.Cache")
    @ConditionalOnProperty(name = "base.cache.provider", havingValue = "caffeine", matchIfMissing = true)
    static class CaffeineAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public CacheProvider caffeineCacheProvider(CaffeineProperties properties) {
            logger.info("Configuring Caffeine cache provider with TTL: {}s, MaxSize: {}",
                       properties.getTtlSeconds(), properties.getMaxSize());
            return new CaffeineCacheProvider(
                    properties.getTtlSeconds(),
                    properties.getMaxSize()
            );
        }
    }

    /**
     * Auto-configuration for Redis cache provider.
     * <p>
     * This configuration is only loaded when Lettuce classes are present
     * on the classpath, the provider is set to "redis", and Redis is enabled.
     */
    @ConditionalOnClass(name = "io.lettuce.core.RedisClient")
    @ConditionalOnProperty(name = "base.cache.provider", havingValue = "redis")
    static class RedisAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(name = "base.cache.redis.enabled", havingValue = "true", matchIfMissing = true)
        public CacheProvider redisCacheProvider(RedisProperties properties, CaffeineProperties caffeineProperties) {
            try {
                logger.info("Configuring Redis cache provider with URL: {}, Namespace: {}",
                           properties.getUrl(), properties.getNamespace());
                return new RedisCacheProvider(
                        properties.getUrl(),
                        new StringSerializer(),
                        properties.getNamespace()
                );
            } catch (Exception e) {
                logger.warn("Failed to create Redis cache provider, falling back to Caffeine: {}", e.getMessage());
                return new CaffeineCacheProvider(
                        caffeineProperties.getTtlSeconds(),
                        caffeineProperties.getMaxSize()
                );
            }
        }
    }


    /**
     * Creates a CacheManager bean from the configured CacheProvider.
     * <p>
     * This bean is created regardless of which provider is selected and
     * provides a unified way to access cache instances.
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(CacheProvider cacheProvider) {
        logger.info("Creating CacheManager with provider: {}", cacheProvider.getName());
        return cacheProvider.createCacheManager();
    }
}
