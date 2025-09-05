package io.github.base.starter.cache.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Redis cache provider.
 * <p>
 * These properties allow customization of Redis cache behavior through
 * Spring Boot configuration files (application.yml, application.properties).
 *
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "base.cache.redis")
public class RedisProperties {

    /**
     * Whether Redis cache provider is enabled.
     * Default: true
     */
    private boolean enabled = true;

    /**
     * Redis server URL.
     * Default: redis://localhost:6379
     */
    private String url = "redis://localhost:6379";

    /**
     * Namespace prefix for all cache keys.
     * Default: "base"
     */
    private String namespace = "base";

    /**
     * Connection timeout in milliseconds.
     * Default: 2000ms
     */
    private int connectionTimeout = 2000;

    /**
     * Command timeout in milliseconds.
     * Default: 5000ms
     */
    private int commandTimeout = 5000;

    /**
     * Gets whether Redis cache provider is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether Redis cache provider is enabled.
     *
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the Redis server URL.
     *
     * @return the Redis URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the Redis server URL.
     *
     * @param url the Redis URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the namespace prefix for cache keys.
     *
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Sets the namespace prefix for cache keys.
     *
     * @param namespace the namespace
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Gets the connection timeout in milliseconds.
     *
     * @return the connection timeout
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets the connection timeout in milliseconds.
     *
     * @param connectionTimeout the connection timeout
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Gets the command timeout in milliseconds.
     *
     * @return the command timeout
     */
    public int getCommandTimeout() {
        return commandTimeout;
    }

    /**
     * Sets the command timeout in milliseconds.
     *
     * @param commandTimeout the command timeout
     */
    public void setCommandTimeout(int commandTimeout) {
        this.commandTimeout = commandTimeout;
    }
}
