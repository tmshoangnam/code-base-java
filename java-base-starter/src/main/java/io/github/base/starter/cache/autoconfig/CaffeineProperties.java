package io.github.base.starter.cache.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Caffeine cache provider.
 * <p>
 * These properties allow customization of Caffeine cache behavior through
 * Spring Boot configuration files (application.yml, application.properties).
 *
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "base.cache.caffeine")
public class CaffeineProperties {

    /**
     * Time-to-live in seconds for cache entries.
     * Default: 60 seconds
     */
    private long ttlSeconds = 60;

    /**
     * Maximum number of entries in each cache.
     * Default: 10,000 entries
     */
    private long maxSize = 10_000;

    /**
     * Gets the TTL in seconds for cache entries.
     *
     * @return the TTL in seconds
     */
    public long getTtlSeconds() {
        return ttlSeconds;
    }

    /**
     * Sets the TTL in seconds for cache entries.
     *
     * @param ttlSeconds the TTL in seconds
     */
    public void setTtlSeconds(long ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }

    /**
     * Gets the maximum number of entries in each cache.
     *
     * @return the maximum number of entries
     */
    public long getMaxSize() {
        return maxSize;
    }

    /**
     * Sets the maximum number of entries in each cache.
     *
     * @param maxSize the maximum number of entries
     */
    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }
}
