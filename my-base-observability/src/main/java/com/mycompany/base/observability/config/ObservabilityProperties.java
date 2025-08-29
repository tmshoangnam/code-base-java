package com.mycompany.base.observability.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
import java.util.List;

/**
 * Configuration properties for observability module.
 * Provides comprehensive configuration for monitoring, metrics, tracing, and health checks.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "my-base.observability")
public class ObservabilityProperties {
    
    @NestedConfigurationProperty
    private Metrics metrics = new Metrics();
    
    @NestedConfigurationProperty
    private Tracing tracing = new Tracing();
    
    @NestedConfigurationProperty
    private Health health = new Health();
    
    @NestedConfigurationProperty
    private Logging logging = new Logging();
    
    @NestedConfigurationProperty
    private Prometheus prometheus = new Prometheus();
    
    @NestedConfigurationProperty
    private Grafana grafana = new Grafana();
    
    @NestedConfigurationProperty
    private Alerting alerting = new Alerting();
    
    // Getters and Setters
    public Metrics getMetrics() {
        return metrics;
    }
    
    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }
    
    public Tracing getTracing() {
        return tracing;
    }
    
    public void setTracing(Tracing tracing) {
        this.tracing = tracing;
    }
    
    public Health getHealth() {
        return health;
    }
    
    public void setHealth(Health health) {
        this.health = health;
    }
    
    public Logging getLogging() {
        return logging;
    }
    
    public void setLogging(Logging logging) {
        this.logging = logging;
    }
    
    public Prometheus getPrometheus() {
        return prometheus;
    }
    
    public void setPrometheus(Prometheus prometheus) {
        this.prometheus = prometheus;
    }
    
    public Grafana getGrafana() {
        return grafana;
    }
    
    public void setGrafana(Grafana grafana) {
        this.grafana = grafana;
    }
    
    public Alerting getAlerting() {
        return alerting;
    }
    
    public void setAlerting(Alerting alerting) {
        this.alerting = alerting;
    }
    
    /**
     * Metrics configuration properties.
     */
    public static class Metrics {
        private boolean enabled = true;
        private String registryType = "micrometer";
        private Duration exportInterval = Duration.ofSeconds(15);
        private boolean enableJvmMetrics = true;
        private boolean enableSystemMetrics = true;
        private boolean enableProcessMetrics = true;
        private boolean enableTomcatMetrics = true;
        private boolean enableHikariMetrics = true;
        private boolean enableCacheMetrics = true;
        private boolean enableCustomMetrics = true;
        private List<String> enabledMetrics = List.of("jvm", "system", "process", "tomcat", "hikari", "cache");
        private Duration metricsRetention = Duration.ofDays(30);
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getRegistryType() {
            return registryType;
        }
        
        public void setRegistryType(String registryType) {
            this.registryType = registryType;
        }
        
        public Duration getExportInterval() {
            return exportInterval;
        }
        
        public void setExportInterval(Duration exportInterval) {
            this.exportInterval = exportInterval;
        }
        
        public boolean isEnableJvmMetrics() {
            return enableJvmMetrics;
        }
        
        public void setEnableJvmMetrics(boolean enableJvmMetrics) {
            this.enableJvmMetrics = enableJvmMetrics;
        }
        
        public boolean isEnableSystemMetrics() {
            return enableSystemMetrics;
        }
        
        public void setEnableSystemMetrics(boolean enableSystemMetrics) {
            this.enableSystemMetrics = enableSystemMetrics;
        }
        
        public boolean isEnableProcessMetrics() {
            return enableProcessMetrics;
        }
        
        public void setEnableProcessMetrics(boolean enableProcessMetrics) {
            this.enableProcessMetrics = enableProcessMetrics;
        }
        
        public boolean isEnableTomcatMetrics() {
            return enableTomcatMetrics;
        }
        
        public void setEnableTomcatMetrics(boolean enableTomcatMetrics) {
            this.enableTomcatMetrics = enableTomcatMetrics;
        }
        
        public boolean isEnableHikariMetrics() {
            return enableHikariMetrics;
        }
        
        public void setEnableHikariMetrics(boolean enableHikariMetrics) {
            this.enableHikariMetrics = enableHikariMetrics;
        }
        
        public boolean isEnableCacheMetrics() {
            return enableCacheMetrics;
        }
        
        public void setEnableCacheMetrics(boolean enableCacheMetrics) {
            this.enableCacheMetrics = enableCacheMetrics;
        }
        
        public boolean isEnableCustomMetrics() {
            return enableCustomMetrics;
        }
        
        public void setEnableCustomMetrics(boolean enableCustomMetrics) {
            this.enableCustomMetrics = enableCustomMetrics;
        }
        
        public List<String> getEnabledMetrics() {
            return enabledMetrics;
        }
        
        public void setEnabledMetrics(List<String> enabledMetrics) {
            this.enabledMetrics = enabledMetrics;
        }
        
        public Duration getMetricsRetention() {
            return metricsRetention;
        }
        
        public void setMetricsRetention(Duration metricsRetention) {
            this.metricsRetention = metricsRetention;
        }
    }
    
    /**
     * Tracing configuration properties.
     */
    public static class Tracing {
        private boolean enabled = true;
        private String tracerType = "opentelemetry";
        private String serviceName = "my-base-service";
        private String serviceVersion = "1.0.0";
        private String environment = "development";
        private boolean enableSampling = true;
        private double samplingRate = 1.0;
        private Duration traceTimeout = Duration.ofSeconds(30);
        private boolean enableBaggage = true;
        private boolean enableCorrelation = true;
        private List<String> excludedPaths = List.of("/health/**", "/actuator/**", "/metrics/**");
        private boolean enableAsyncTracing = true;
        private int maxTraceDepth = 100;
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getTracerType() {
            return tracerType;
        }
        
        public void setTracerType(String tracerType) {
            this.tracerType = tracerType;
        }
        
        public String getServiceName() {
            return serviceName;
        }
        
        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }
        
        public String getServiceVersion() {
            return serviceVersion;
        }
        
        public void setServiceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
        }
        
        public String getEnvironment() {
            return environment;
        }
        
        public void setEnvironment(String environment) {
            this.environment = environment;
        }
        
        public boolean isEnableSampling() {
            return enableSampling;
        }
        
        public void setEnableSampling(boolean enableSampling) {
            this.enableSampling = enableSampling;
        }
        
        public double getSamplingRate() {
            return samplingRate;
        }
        
        public void setSamplingRate(double samplingRate) {
            this.samplingRate = samplingRate;
        }
        
        public Duration getTraceTimeout() {
            return traceTimeout;
        }
        
        public void setTraceTimeout(Duration traceTimeout) {
            this.traceTimeout = traceTimeout;
        }
        
        public boolean isEnableBaggage() {
            return enableBaggage;
        }
        
        public void setEnableBaggage(boolean enableBaggage) {
            this.enableBaggage = enableBaggage;
        }
        
        public boolean isEnableCorrelation() {
            return enableCorrelation;
        }
        
        public void setEnableCorrelation(boolean enableCorrelation) {
            this.enableCorrelation = enableCorrelation;
        }
        
        public List<String> getExcludedPaths() {
            return excludedPaths;
        }
        
        public void setExcludedPaths(List<String> excludedPaths) {
            this.excludedPaths = excludedPaths;
        }
        
        public boolean isEnableAsyncTracing() {
            return enableAsyncTracing;
        }
        
        public void setEnableAsyncTracing(boolean enableAsyncTracing) {
            this.enableAsyncTracing = enableAsyncTracing;
        }
        
        public int getMaxTraceDepth() {
            return maxTraceDepth;
        }
        
        public void setMaxTraceDepth(int maxTraceDepth) {
            this.maxTraceDepth = maxTraceDepth;
        }
    }
    
    /**
     * Health check configuration properties.
     */
    public static class Health {
        private boolean enabled = true;
        private Duration checkInterval = Duration.ofSeconds(30);
        private Duration timeout = Duration.ofSeconds(10);
        private boolean enableLivenessProbe = true;
        private boolean enableReadinessProbe = true;
        private boolean enableStartupProbe = true;
        private String livenessEndpoint = "/actuator/health/liveness";
        private String readinessEndpoint = "/actuator/health/readiness";
        private String startupEndpoint = "/actuator/health/startup";
        private List<String> healthIndicators = List.of("diskSpace", "ping", "db", "redis", "cache");
        private boolean enableHealthGroups = true;
        private Duration startupTimeout = Duration.ofMinutes(5);
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public Duration getCheckInterval() {
            return checkInterval;
        }
        
        public void setCheckInterval(Duration checkInterval) {
            this.checkInterval = checkInterval;
        }
        
        public Duration getTimeout() {
            return timeout;
        }
        
        public void setTimeout(Duration timeout) {
            this.timeout = timeout;
        }
        
        public boolean isEnableLivenessProbe() {
            return enableLivenessProbe;
        }
        
        public void setEnableLivenessProbe(boolean enableLivenessProbe) {
            this.enableLivenessProbe = enableLivenessProbe;
        }
        
        public boolean isEnableReadinessProbe() {
            return enableReadinessProbe;
        }
        
        public void setEnableReadinessProbe(boolean enableReadinessProbe) {
            this.enableReadinessProbe = enableReadinessProbe;
        }
        
        public boolean isEnableStartupProbe() {
            return enableStartupProbe;
        }
        
        public void setEnableStartupProbe(boolean enableStartupProbe) {
            this.enableStartupProbe = enableStartupProbe;
        }
        
        public String getLivenessEndpoint() {
            return livenessEndpoint;
        }
        
        public void setLivenessEndpoint(String livenessEndpoint) {
            this.livenessEndpoint = livenessEndpoint;
        }
        
        public String getReadinessEndpoint() {
            return readinessEndpoint;
        }
        
        public void setReadinessEndpoint(String readinessEndpoint) {
            this.readinessEndpoint = readinessEndpoint;
        }
        
        public String getStartupEndpoint() {
            return startupEndpoint;
        }
        
        public void setStartupEndpoint(String startupEndpoint) {
            this.startupEndpoint = startupEndpoint;
        }
        
        public List<String> getHealthIndicators() {
            return healthIndicators;
        }
        
        public void setHealthIndicators(List<String> healthIndicators) {
            this.healthIndicators = healthIndicators;
        }
        
        public boolean isEnableHealthGroups() {
            return enableHealthGroups;
        }
        
        public void setEnableHealthGroups(boolean enableHealthGroups) {
            this.enableHealthGroups = enableHealthGroups;
        }
        
        public Duration getStartupTimeout() {
            return startupTimeout;
        }
        
        public void setStartupTimeout(Duration startupTimeout) {
            this.startupTimeout = startupTimeout;
        }
    }
    
    /**
     * Logging configuration properties.
     */
    public static class Logging {
        private boolean enabled = true;
        private String level = "INFO";
        private String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
        private boolean enableJsonFormat = false;
        private boolean enableStructuredLogging = true;
        private boolean enableCorrelationId = true;
        private boolean enableRequestId = true;
        private boolean enableUserId = true;
        private boolean enablePerformanceLogging = true;
        private Duration performanceThreshold = Duration.ofMillis(100);
        private List<String> excludedPaths = List.of("/health/**", "/actuator/**", "/metrics/**");
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getLevel() {
            return level;
        }
        
        public void setLevel(String level) {
            this.level = level;
        }
        
        public String getPattern() {
            return pattern;
        }
        
        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
        
        public boolean isEnableJsonFormat() {
            return enableJsonFormat;
        }
        
        public void setEnableJsonFormat(boolean enableJsonFormat) {
            this.enableJsonFormat = enableJsonFormat;
        }
        
        public boolean isEnableStructuredLogging() {
            return enableStructuredLogging;
        }
        
        public void setEnableStructuredLogging(boolean enableStructuredLogging) {
            this.enableStructuredLogging = enableStructuredLogging;
        }
        
        public boolean isEnableCorrelationId() {
            return enableCorrelationId;
        }
        
        public void setEnableCorrelationId(boolean enableCorrelationId) {
            this.enableCorrelationId = enableCorrelationId;
        }
        
        public boolean isEnableRequestId() {
            return enableRequestId;
        }
        
        public void setEnableRequestId(boolean enableRequestId) {
            this.enableRequestId = enableRequestId;
        }
        
        public boolean isEnableUserId() {
            return enableUserId;
        }
        
        public void setEnableUserId(boolean enableUserId) {
            this.enableUserId = enableUserId;
        }
        
        public boolean isEnablePerformanceLogging() {
            return enablePerformanceLogging;
        }
        
        public void setEnablePerformanceLogging(boolean enablePerformanceLogging) {
            this.enablePerformanceLogging = enablePerformanceLogging;
        }
        
        public Duration getPerformanceThreshold() {
            return performanceThreshold;
        }
        
        public void setPerformanceThreshold(Duration performanceThreshold) {
            this.performanceThreshold = performanceThreshold;
        }
        
        public List<String> getExcludedPaths() {
            return excludedPaths;
        }
        
        public void setExcludedPaths(List<String> excludedPaths) {
            this.excludedPaths = excludedPaths;
        }
    }
    
    /**
     * Prometheus configuration properties.
     */
    public static class Prometheus {
        private boolean enabled = true;
        private String endpoint = "/actuator/prometheus";
        private Duration scrapeInterval = Duration.ofSeconds(15);
        private boolean enableHistograms = true;
        private boolean enableSummaries = true;
        private boolean enableCounters = true;
        private boolean enableGauges = true;
        private List<String> buckets = List.of("0.1", "0.5", "1.0", "2.0", "5.0", "10.0", "30.0", "60.0");
        private Duration metricsRetention = Duration.ofDays(15);
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getEndpoint() {
            return endpoint;
        }
        
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        
        public Duration getScrapeInterval() {
            return scrapeInterval;
        }
        
        public void setScrapeInterval(Duration scrapeInterval) {
            this.scrapeInterval = scrapeInterval;
        }
        
        public boolean isEnableHistograms() {
            return enableHistograms;
        }
        
        public void setEnableHistograms(boolean enableHistograms) {
            this.enableHistograms = enableHistograms;
        }
        
        public boolean isEnableSummaries() {
            return enableSummaries;
        }
        
        public void setEnableSummaries(boolean enableSummaries) {
            this.enableSummaries = enableSummaries;
        }
        
        public boolean isEnableCounters() {
            return enableCounters;
        }
        
        public void setEnableCounters(boolean enableCounters) {
            this.enableCounters = enableCounters;
        }
        
        public boolean isEnableGauges() {
            return enableGauges;
        }
        
        public void setEnableGauges(boolean enableGauges) {
            this.enableGauges = enableGauges;
        }
        
        public List<String> getBuckets() {
            return buckets;
        }
        
        public void setBuckets(List<String> buckets) {
            this.buckets = buckets;
        }
        
        public Duration getMetricsRetention() {
            return metricsRetention;
        }
        
        public void setMetricsRetention(Duration metricsRetention) {
            this.metricsRetention = metricsRetention;
        }
    }
    
    /**
     * Grafana configuration properties.
     */
    public static class Grafana {
        private boolean enabled = false;
        private String url = "http://localhost:3000";
        private String username = "admin";
        private String password = "admin";
        private String apiKey;
        private boolean enableDashboardProvisioning = false;
        private String dashboardPath = "classpath:grafana/dashboards";
        private boolean enableDataSourceProvisioning = false;
        private String dataSourcePath = "classpath:grafana/datasources";
        private Duration provisioningInterval = Duration.ofMinutes(5);
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public String getApiKey() {
            return apiKey;
        }
        
        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
        
        public boolean isEnableDashboardProvisioning() {
            return enableDashboardProvisioning;
        }
        
        public void setEnableDashboardProvisioning(boolean enableDashboardProvisioning) {
            this.enableDashboardProvisioning = enableDashboardProvisioning;
        }
        
        public String getDashboardPath() {
            return dashboardPath;
        }
        
        public void setDashboardPath(String dashboardPath) {
            this.dashboardPath = dashboardPath;
        }
        
        public boolean isEnableDataSourceProvisioning() {
            return enableDataSourceProvisioning;
        }
        
        public void setEnableDataSourceProvisioning(boolean enableDataSourceProvisioning) {
            this.enableDataSourceProvisioning = enableDataSourceProvisioning;
        }
        
        public String getDataSourcePath() {
            return dataSourcePath;
        }
        
        public void setDataSourcePath(String dataSourcePath) {
            this.dataSourcePath = dataSourcePath;
        }
        
        public Duration getProvisioningInterval() {
            return provisioningInterval;
        }
        
        public void setProvisioningInterval(Duration provisioningInterval) {
            this.provisioningInterval = provisioningInterval;
        }
    }
    
    /**
     * Alerting configuration properties.
     */
    public static class Alerting {
        private boolean enabled = false;
        private String alertManagerUrl = "http://localhost:9093";
        private Duration alertCheckInterval = Duration.ofSeconds(30);
        private boolean enableSlackNotifications = false;
        private String slackWebhookUrl;
        private String slackChannel = "#alerts";
        private String slackUsername = "AlertManager";
        private boolean enableEmailNotifications = false;
        private String smtpHost = "localhost";
        private int smtpPort = 587;
        private String smtpUsername;
        private String smtpPassword;
        private String fromEmail = "alerts@mycompany.com";
        private List<String> toEmails = List.of("admin@mycompany.com");
        private boolean enablePagerDuty = false;
        private String pagerDutyServiceKey;
        private boolean enableWebhookNotifications = false;
        private List<String> webhookUrls = List.of();
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getAlertManagerUrl() {
            return alertManagerUrl;
        }
        
        public void setAlertManagerUrl(String alertManagerUrl) {
            this.alertManagerUrl = alertManagerUrl;
        }
        
        public Duration getAlertCheckInterval() {
            return alertCheckInterval;
        }
        
        public void setAlertCheckInterval(Duration alertCheckInterval) {
            this.alertCheckInterval = alertCheckInterval;
        }
        
        public boolean isEnableSlackNotifications() {
            return enableSlackNotifications;
        }
        
        public void setEnableSlackNotifications(boolean enableSlackNotifications) {
            this.enableSlackNotifications = enableSlackNotifications;
        }
        
        public String getSlackWebhookUrl() {
            return slackWebhookUrl;
        }
        
        public void setSlackWebhookUrl(String slackWebhookUrl) {
            this.slackWebhookUrl = slackWebhookUrl;
        }
        
        public String getSlackChannel() {
            return slackChannel;
        }
        
        public void setSlackChannel(String slackChannel) {
            this.slackChannel = slackChannel;
        }
        
        public String getSlackUsername() {
            return slackUsername;
        }
        
        public void setSlackUsername(String slackUsername) {
            this.slackUsername = slackUsername;
        }
        
        public boolean isEnableEmailNotifications() {
            return enableEmailNotifications;
        }
        
        public void setEnableEmailNotifications(boolean enableEmailNotifications) {
            this.enableEmailNotifications = enableEmailNotifications;
        }
        
        public String getSmtpHost() {
            return smtpHost;
        }
        
        public void setSmtpHost(String smtpHost) {
            this.smtpHost = smtpHost;
        }
        
        public int getSmtpPort() {
            return smtpPort;
        }
        
        public void setSmtpPort(int smtpPort) {
            this.smtpPort = smtpPort;
        }
        
        public String getSmtpUsername() {
            return smtpUsername;
        }
        
        public void setSmtpUsername(String smtpUsername) {
            this.smtpUsername = smtpUsername;
        }
        
        public String getSmtpPassword() {
            return smtpPassword;
        }
        
        public void setSmtpPassword(String smtpPassword) {
            this.smtpPassword = smtpPassword;
        }
        
        public String getFromEmail() {
            return fromEmail;
        }
        
        public void setFromEmail(String fromEmail) {
            this.fromEmail = fromEmail;
        }
        
        public List<String> getToEmails() {
            return toEmails;
        }
        
        public void setToEmails(List<String> toEmails) {
            this.toEmails = toEmails;
        }
        
        public boolean isEnablePagerDuty() {
            return enablePagerDuty;
        }
        
        public void setEnablePagerDuty(boolean enablePagerDuty) {
            this.enablePagerDuty = enablePagerDuty;
        }
        
        public String getPagerDutyServiceKey() {
            return pagerDutyServiceKey;
        }
        
        public void setPagerDutyServiceKey(String pagerDutyServiceKey) {
            this.pagerDutyServiceKey = pagerDutyServiceKey;
        }
        
        public boolean isEnableWebhookNotifications() {
            return enableWebhookNotifications;
        }
        
        public void setEnableWebhookNotifications(boolean enableWebhookNotifications) {
            this.enableWebhookNotifications = enableWebhookNotifications;
        }
        
        public List<String> getWebhookUrls() {
            return webhookUrls;
        }
        
        public void setWebhookUrls(List<String> webhookUrls) {
            this.webhookUrls = webhookUrls;
        }
    }
}
