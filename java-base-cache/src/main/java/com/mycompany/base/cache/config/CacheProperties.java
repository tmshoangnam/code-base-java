package com.mycompany.base.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for cache module.
 * Provides comprehensive cache configuration for Redis, Caffeine, and other cache providers.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "my-base.cache")
public class CacheProperties {
    
    @NestedConfigurationProperty
    private Redis redis = new Redis();
    
    @NestedConfigurationProperty
    private Caffeine caffeine = new Caffeine();
    
    @NestedConfigurationProperty
    private Hazelcast hazelcast = new Hazelcast();
    
    @NestedConfigurationProperty
    private General general = new General();
    
    @NestedConfigurationProperty
    private Metrics metrics = new Metrics();
    
    // Getters and Setters
    public Redis getRedis() {
        return redis;
    }
    
    public void setRedis(Redis redis) {
        this.redis = redis;
    }
    
    public Caffeine getCaffeine() {
        return caffeine;
    }
    
    public void setCaffeine(Caffeine caffeine) {
        this.caffeine = caffeine;
    }
    
    public Hazelcast getHazelcast() {
        return hazelcast;
    }
    
    public void setHazelcast(Hazelcast hazelcast) {
        this.hazelcast = hazelcast;
    }
    
    public General getGeneral() {
        return general;
    }
    
    public void setGeneral(General general) {
        this.general = general;
    }
    
    public Metrics getMetrics() {
        return metrics;
    }
    
    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }
    
    /**
     * Redis cache configuration properties.
     */
    public static class Redis {
        private boolean enabled = true;
        private String host = "localhost";
        private int port = 6379;
        private String password;
        private int database = 0;
        private Duration connectionTimeout = Duration.ofSeconds(5);
        private Duration readTimeout = Duration.ofSeconds(3);
        private Duration writeTimeout = Duration.ofSeconds(3);
        private int maxConnections = 8;
        private int maxIdleConnections = 8;
        private Duration maxIdleTime = Duration.ofMinutes(10);
        private Duration maxLifeTime = Duration.ofHours(1);
        private boolean enableSsl = false;
        private String keyPrefix = "my-base:";
        private Duration defaultTtl = Duration.ofHours(1);
        private boolean enableCompression = false;
        private int compressionThreshold = 1024;
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getHost() {
            return host;
        }
        
        public void setHost(String host) {
            this.host = host;
        }
        
        public int getPort() {
            return port;
        }
        
        public void setPort(int port) {
            this.port = port;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public int getDatabase() {
            return database;
        }
        
        public void setDatabase(int database) {
            this.database = database;
        }
        
        public Duration getConnectionTimeout() {
            return connectionTimeout;
        }
        
        public void setConnectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }
        
        public Duration getReadTimeout() {
            return readTimeout;
        }
        
        public void setReadTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
        }
        
        public Duration getWriteTimeout() {
            return writeTimeout;
        }
        
        public void setWriteTimeout(Duration writeTimeout) {
            this.writeTimeout = writeTimeout;
        }
        
        public int getMaxConnections() {
            return maxConnections;
        }
        
        public void setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
        }
        
        public int getMaxIdleConnections() {
            return maxIdleConnections;
        }
        
        public void setMaxIdleConnections(int maxIdleConnections) {
            this.maxIdleConnections = maxIdleConnections;
        }
        
        public Duration getMaxIdleTime() {
            return maxIdleTime;
        }
        
        public void setMaxIdleTime(Duration maxIdleTime) {
            this.maxIdleTime = maxIdleTime;
        }
        
        public Duration getMaxLifeTime() {
            return maxLifeTime;
        }
        
        public void setMaxLifeTime(Duration maxLifeTime) {
            this.maxLifeTime = maxLifeTime;
        }
        
        public boolean isEnableSsl() {
            return enableSsl;
        }
        
        public void setEnableSsl(boolean enableSsl) {
            this.enableSsl = enableSsl;
        }
        
        public String getKeyPrefix() {
            return keyPrefix;
        }
        
        public void setKeyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
        }
        
        public Duration getDefaultTtl() {
            return defaultTtl;
        }
        
        public void setDefaultTtl(Duration defaultTtl) {
            this.defaultTtl = defaultTtl;
        }
        
        public boolean isEnableCompression() {
            return enableCompression;
        }
        
        public void setEnableCompression(boolean enableCompression) {
            this.enableCompression = enableCompression;
        }
        
        public int getCompressionThreshold() {
            return compressionThreshold;
        }
        
        public void setCompressionThreshold(int compressionThreshold) {
            this.compressionThreshold = compressionThreshold;
        }
    }
    
    /**
     * Caffeine cache configuration properties.
     */
    public static class Caffeine {
        private boolean enabled = true;
        private long maximumSize = 10000;
        private Duration expireAfterWrite = Duration.ofHours(1);
        private Duration expireAfterAccess = Duration.ofMinutes(30);
        private Duration refreshAfterWrite = Duration.ofMinutes(15);
        private boolean recordStats = true;
        private boolean weakKeys = false;
        private boolean weakValues = false;
        private boolean softValues = false;
        private Map<String, CaffeineCacheConfig> caches = new HashMap<>();
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public long getMaximumSize() {
            return maximumSize;
        }
        
        public void setMaximumSize(long maximumSize) {
            this.maximumSize = maximumSize;
        }
        
        public Duration getExpireAfterWrite() {
            return expireAfterWrite;
        }
        
        public void setExpireAfterWrite(Duration expireAfterWrite) {
            this.expireAfterWrite = expireAfterWrite;
        }
        
        public Duration getExpireAfterAccess() {
            return expireAfterAccess;
        }
        
        public void setExpireAfterAccess(Duration expireAfterAccess) {
            this.expireAfterAccess = expireAfterAccess;
        }
        
        public Duration getRefreshAfterWrite() {
            return refreshAfterWrite;
        }
        
        public void setRefreshAfterWrite(Duration refreshAfterWrite) {
            this.refreshAfterWrite = refreshAfterWrite;
        }
        
        public boolean isRecordStats() {
            return recordStats;
        }
        
        public void setRecordStats(boolean recordStats) {
            this.recordStats = recordStats;
        }
        
        public boolean isWeakKeys() {
            return weakKeys;
        }
        
        public void setWeakKeys(boolean weakKeys) {
            this.weakKeys = weakKeys;
        }
        
        public boolean isWeakValues() {
            return weakValues;
        }
        
        public void setWeakValues(boolean weakValues) {
            this.weakValues = weakValues;
        }
        
        public boolean isSoftValues() {
            return softValues;
        }
        
        public void setSoftValues(boolean softValues) {
            this.softValues = softValues;
        }
        
        public Map<String, CaffeineCacheConfig> getCaches() {
            return caches;
        }
        
        public void setCaches(Map<String, CaffeineCacheConfig> caches) {
            this.caches = caches;
        }
        
        /**
         * Individual Caffeine cache configuration.
         */
        public static class CaffeineCacheConfig {
            private long maximumSize = 1000;
            private Duration expireAfterWrite = Duration.ofHours(1);
            private Duration expireAfterAccess = Duration.ofMinutes(30);
            private Duration refreshAfterWrite = Duration.ofMinutes(15);
            
            // Getters and Setters
            public long getMaximumSize() {
                return maximumSize;
            }
            
            public void setMaximumSize(long maximumSize) {
                this.maximumSize = maximumSize;
            }
            
            public Duration getExpireAfterWrite() {
                return expireAfterWrite;
            }
            
            public void setExpireAfterWrite(Duration expireAfterWrite) {
                this.expireAfterWrite = expireAfterWrite;
            }
            
            public Duration getExpireAfterAccess() {
                return expireAfterAccess;
            }
            
            public void setExpireAfterAccess(Duration expireAfterAccess) {
                this.expireAfterAccess = expireAfterAccess;
            }
            
            public Duration getRefreshAfterWrite() {
                return refreshAfterWrite;
            }
            
            public void setRefreshAfterWrite(Duration refreshAfterWrite) {
                this.refreshAfterWrite = refreshAfterWrite;
            }
        }
    }
    
    /**
     * Hazelcast cache configuration properties.
     */
    public static class Hazelcast {
        private boolean enabled = false;
        private String instanceName = "my-base-hazelcast";
        private int port = 5701;
        private int portCount = 100;
        private boolean portAutoIncrement = true;
        private String groupName = "my-base";
        private String groupPassword = "my-base-pass";
        private Duration connectionTimeout = Duration.ofSeconds(5);
        private Duration operationTimeout = Duration.ofSeconds(30);
        private int maxConnections = 100;
        private boolean enableMulticast = true;
        private String multicastGroup = "224.2.2.3";
        private int multicastPort = 54327;
        private boolean enableTcpIp = false;
        private String[] tcpIpMembers = {"127.0.0.1"};
        private boolean enableManagementCenter = false;
        private String managementCenterUrl = "http://localhost:8080/mancenter";
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getInstanceName() {
            return instanceName;
        }
        
        public void setInstanceName(String instanceName) {
            this.instanceName = instanceName;
        }
        
        public int getPort() {
            return port;
        }
        
        public void setPort(int port) {
            this.port = port;
        }
        
        public int getPortCount() {
            return portCount;
        }
        
        public void setPortCount(int portCount) {
            this.portCount = portCount;
        }
        
        public boolean isPortAutoIncrement() {
            return portAutoIncrement;
        }
        
        public void setPortAutoIncrement(boolean portAutoIncrement) {
            this.portAutoIncrement = portAutoIncrement;
        }
        
        public String getGroupName() {
            return groupName;
        }
        
        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }
        
        public String getGroupPassword() {
            return groupPassword;
        }
        
        public void setGroupPassword(String groupPassword) {
            this.groupPassword = groupPassword;
        }
        
        public Duration getConnectionTimeout() {
            return connectionTimeout;
        }
        
        public void setConnectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }
        
        public Duration getOperationTimeout() {
            return operationTimeout;
        }
        
        public void setOperationTimeout(Duration operationTimeout) {
            this.operationTimeout = operationTimeout;
        }
        
        public int getMaxConnections() {
            return maxConnections;
        }
        
        public void setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
        }
        
        public boolean isEnableMulticast() {
            return enableMulticast;
        }
        
        public void setEnableMulticast(boolean enableMulticast) {
            this.enableMulticast = enableMulticast;
        }
        
        public String getMulticastGroup() {
            return multicastGroup;
        }
        
        public void setMulticastGroup(String multicastGroup) {
            this.multicastGroup = multicastGroup;
        }
        
        public int getMulticastPort() {
            return multicastPort;
        }
        
        public void setMulticastPort(int multicastPort) {
            this.multicastPort = multicastPort;
        }
        
        public boolean isEnableTcpIp() {
            return enableTcpIp;
        }
        
        public void setEnableTcpIp(boolean enableTcpIp) {
            this.enableTcpIp = enableTcpIp;
        }
        
        public String[] getTcpIpMembers() {
            return tcpIpMembers;
        }
        
        public void setTcpIpMembers(String[] tcpIpMembers) {
            this.tcpIpMembers = tcpIpMembers;
        }
        
        public boolean isEnableManagementCenter() {
            return enableManagementCenter;
        }
        
        public void setEnableManagementCenter(boolean enableManagementCenter) {
            this.enableManagementCenter = enableManagementCenter;
        }
        
        public String getManagementCenterUrl() {
            return managementCenterUrl;
        }
        
        public void setManagementCenterUrl(String managementCenterUrl) {
            this.managementCenterUrl = managementCenterUrl;
        }
    }
    
    /**
     * General cache configuration properties.
     */
    public static class General {
        private String defaultProvider = "caffeine";
        private boolean enableCacheManager = true;
        private boolean enableCacheResolver = true;
        private boolean enableKeyGenerator = true;
        private Duration defaultTtl = Duration.ofHours(1);
        private boolean enableCacheEviction = true;
        private Duration evictionCheckInterval = Duration.ofMinutes(5);
        private boolean enableCacheStatistics = true;
        private boolean enableCacheMetrics = true;
        private Map<String, Duration> cacheTtls = new HashMap<>();
        
        // Getters and Setters
        public String getDefaultProvider() {
            return defaultProvider;
        }
        
        public void setDefaultProvider(String defaultProvider) {
            this.defaultProvider = defaultProvider;
        }
        
        public boolean isEnableCacheManager() {
            return enableCacheManager;
        }
        
        public void setEnableCacheManager(boolean enableCacheManager) {
            this.enableCacheManager = enableCacheManager;
        }
        
        public boolean isEnableCacheResolver() {
            return enableCacheResolver;
        }
        
        public void setEnableCacheResolver(boolean enableCacheResolver) {
            this.enableCacheResolver = enableCacheResolver;
        }
        
        public boolean isEnableKeyGenerator() {
            return enableKeyGenerator;
        }
        
        public void setEnableKeyGenerator(boolean enableKeyGenerator) {
            this.enableKeyGenerator = enableKeyGenerator;
        }
        
        public Duration getDefaultTtl() {
            return defaultTtl;
        }
        
        public void setDefaultTtl(Duration defaultTtl) {
            this.defaultTtl = defaultTtl;
        }
        
        public boolean isEnableCacheEviction() {
            return enableCacheEviction;
        }
        
        public void setEnableCacheEviction(boolean enableCacheEviction) {
            this.enableCacheEviction = enableCacheEviction;
        }
        
        public Duration getEvictionCheckInterval() {
            return evictionCheckInterval;
        }
        
        public void setEvictionCheckInterval(Duration evictionCheckInterval) {
            this.evictionCheckInterval = evictionCheckInterval;
        }
        
        public boolean isEnableCacheStatistics() {
            return enableCacheStatistics;
        }
        
        public void setEnableCacheStatistics(boolean enableCacheStatistics) {
            this.enableCacheStatistics = enableCacheStatistics;
        }
        
        public boolean isEnableCacheMetrics() {
            return enableCacheMetrics;
        }
        
        public void setEnableCacheMetrics(boolean enableCacheMetrics) {
            this.enableCacheMetrics = enableCacheMetrics;
        }
        
        public Map<String, Duration> getCacheTtls() {
            return cacheTtls;
        }
        
        public void setCacheTtls(Map<String, Duration> cacheTtls) {
            this.cacheTtls = cacheTtls;
        }
    }
    
    /**
     * Cache metrics configuration properties.
     */
    public static class Metrics {
        private boolean enabled = true;
        private boolean enableHitRate = true;
        private boolean enableMissRate = true;
        private boolean enableEvictionRate = true;
        private boolean enableLoadTime = true;
        private boolean enableSize = true;
        private Duration metricsCollectionInterval = Duration.ofSeconds(30);
        private boolean enablePrometheus = true;
        private boolean enableMicrometer = true;
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public boolean isEnableHitRate() {
            return enableHitRate;
        }
        
        public void setEnableHitRate(boolean enableHitRate) {
            this.enableHitRate = enableHitRate;
        }
        
        public boolean isEnableMissRate() {
            return enableMissRate;
        }
        
        public void setEnableMissRate(boolean enableMissRate) {
            this.enableMissRate = enableMissRate;
        }
        
        public boolean isEnableEvictionRate() {
            return enableEvictionRate;
        }
        
        public void setEnableEvictionRate(boolean enableEvictionRate) {
            this.enableEvictionRate = enableEvictionRate;
        }
        
        public boolean isEnableLoadTime() {
            return enableLoadTime;
        }
        
        public void setEnableLoadTime(boolean enableLoadTime) {
            this.enableLoadTime = enableLoadTime;
        }
        
        public boolean isEnableSize() {
            return enableSize;
        }
        
        public void setEnableSize(boolean enableSize) {
            this.enableSize = enableSize;
        }
        
        public Duration getMetricsCollectionInterval() {
            return metricsCollectionInterval;
        }
        
        public void setMetricsCollectionInterval(Duration metricsCollectionInterval) {
            this.metricsCollectionInterval = metricsCollectionInterval;
        }
        
        public boolean isEnablePrometheus() {
            return enablePrometheus;
        }
        
        public void setEnablePrometheus(boolean enablePrometheus) {
            this.enablePrometheus = enablePrometheus;
        }
        
        public boolean isEnableMicrometer() {
            return enableMicrometer;
        }
        
        public void setEnableMicrometer(boolean enableMicrometer) {
            this.enableMicrometer = enableMicrometer;
        }
    }
}
