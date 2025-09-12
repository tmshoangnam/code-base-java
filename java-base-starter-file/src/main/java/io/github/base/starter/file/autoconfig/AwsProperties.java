package io.github.base.starter.file.autoconfig;

import io.github.base.starter.file.constants.FileStorageConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * Configuration properties for AWS services.
 * 
 * <p>This class provides configuration properties for various AWS services
 * including S3, SQS, SNS, Lambda, DynamoDB, and Secrets Manager.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@ConfigurationProperties(prefix = "base.aws")
public class AwsProperties {
    
    /**
     * AWS region.
     */
    private String region = FileStorageConstants.DEFAULT_AWS_REGION;
    
    /**
     * AWS credentials configuration.
     */
    @NestedConfigurationProperty
    private CredentialsConfig credentials = new CredentialsConfig();
    
    /**
     * S3 configuration.
     */
    @NestedConfigurationProperty
    private S3Config s3 = new S3Config();
    
    /**
     * SQS configuration.
     */
    @NestedConfigurationProperty
    private SqsConfig sqs = new SqsConfig();
    
    /**
     * SNS configuration.
     */
    @NestedConfigurationProperty
    private SnsConfig sns = new SnsConfig();
    
    /**
     * Lambda configuration.
     */
    @NestedConfigurationProperty
    private LambdaConfig lambda = new LambdaConfig();
    
    /**
     * DynamoDB configuration.
     */
    @NestedConfigurationProperty
    private DynamoDbConfig dynamodb = new DynamoDbConfig();
    
    /**
     * Secrets Manager configuration.
     */
    @NestedConfigurationProperty
    private SecretsManagerConfig secrets = new SecretsManagerConfig();
    
    /**
     * Enable AWS services auto-configuration.
     */
    private boolean enabled = true;
    
    /**
     * Enable metrics collection.
     */
    private boolean metricsEnabled = true;
    
    /**
     * Enable tracing.
     */
    private boolean tracingEnabled = true;
    
    /**
     * AWS credentials configuration.
     */
    public static class CredentialsConfig {
        private String accessKeyId;
        private String secretAccessKey;
        private String sessionToken;
        private String profileName;
        private String roleArn;
        private String externalId;
        private String region;
        
        // Getters and setters
        public String getAccessKeyId() {
            return accessKeyId;
        }
        
        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }
        
        public String getSecretAccessKey() {
            return secretAccessKey;
        }
        
        public void setSecretAccessKey(String secretAccessKey) {
            this.secretAccessKey = secretAccessKey;
        }
        
        public String getSessionToken() {
            return sessionToken;
        }
        
        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }
        
        public String getProfileName() {
            return profileName;
        }
        
        public void setProfileName(String profileName) {
            this.profileName = profileName;
        }
        
        public String getRoleArn() {
            return roleArn;
        }
        
        public void setRoleArn(String roleArn) {
            this.roleArn = roleArn;
        }
        
        public String getExternalId() {
            return externalId;
        }
        
        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }
        
        public String getRegion() {
            return region;
        }
        
        public void setRegion(String region) {
            this.region = region;
        }
    }
    
    /**
     * S3 configuration.
     */
    public static class S3Config {
        private String defaultBucket;
        private String endpoint;
        private boolean pathStyleAccess = false;
        private boolean accelerateMode = false;
        private boolean dualstack = false;
        private boolean forceGlobalBucketAccess = false;
        private Map<String, String> additionalProperties;
        
        // Getters and setters
        public String getDefaultBucket() {
            return defaultBucket;
        }
        
        public void setDefaultBucket(String defaultBucket) {
            this.defaultBucket = defaultBucket;
        }
        
        public String getEndpoint() {
            return endpoint;
        }
        
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        
        public boolean isPathStyleAccess() {
            return pathStyleAccess;
        }
        
        public void setPathStyleAccess(boolean pathStyleAccess) {
            this.pathStyleAccess = pathStyleAccess;
        }
        
        public boolean isAccelerateMode() {
            return accelerateMode;
        }
        
        public void setAccelerateMode(boolean accelerateMode) {
            this.accelerateMode = accelerateMode;
        }
        
        public boolean isDualstack() {
            return dualstack;
        }
        
        public void setDualstack(boolean dualstack) {
            this.dualstack = dualstack;
        }
        
        public boolean isForceGlobalBucketAccess() {
            return forceGlobalBucketAccess;
        }
        
        public void setForceGlobalBucketAccess(boolean forceGlobalBucketAccess) {
            this.forceGlobalBucketAccess = forceGlobalBucketAccess;
        }
        
        public Map<String, String> getAdditionalProperties() {
            return additionalProperties;
        }
        
        public void setAdditionalProperties(Map<String, String> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }
    }
    
    /**
     * SQS configuration.
     */
    public static class SqsConfig {
        private String defaultQueueUrl;
        private String endpoint;
        private int maxMessages = 10;
        private int waitTimeSeconds = 0;
        private int visibilityTimeoutSeconds = 30;
        private Map<String, String> additionalProperties;
        
        // Getters and setters
        public String getDefaultQueueUrl() {
            return defaultQueueUrl;
        }
        
        public void setDefaultQueueUrl(String defaultQueueUrl) {
            this.defaultQueueUrl = defaultQueueUrl;
        }
        
        public String getEndpoint() {
            return endpoint;
        }
        
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        
        public int getMaxMessages() {
            return maxMessages;
        }
        
        public void setMaxMessages(int maxMessages) {
            this.maxMessages = maxMessages;
        }
        
        public int getWaitTimeSeconds() {
            return waitTimeSeconds;
        }
        
        public void setWaitTimeSeconds(int waitTimeSeconds) {
            this.waitTimeSeconds = waitTimeSeconds;
        }
        
        public int getVisibilityTimeoutSeconds() {
            return visibilityTimeoutSeconds;
        }
        
        public void setVisibilityTimeoutSeconds(int visibilityTimeoutSeconds) {
            this.visibilityTimeoutSeconds = visibilityTimeoutSeconds;
        }
        
        public Map<String, String> getAdditionalProperties() {
            return additionalProperties;
        }
        
        public void setAdditionalProperties(Map<String, String> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }
    }
    
    /**
     * SNS configuration.
     */
    public static class SnsConfig {
        private String defaultTopicArn;
        private String endpoint;
        private Map<String, String> additionalProperties;
        
        // Getters and setters
        public String getDefaultTopicArn() {
            return defaultTopicArn;
        }
        
        public void setDefaultTopicArn(String defaultTopicArn) {
            this.defaultTopicArn = defaultTopicArn;
        }
        
        public String getEndpoint() {
            return endpoint;
        }
        
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        
        public Map<String, String> getAdditionalProperties() {
            return additionalProperties;
        }
        
        public void setAdditionalProperties(Map<String, String> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }
    }
    
    /**
     * Lambda configuration.
     */
    public static class LambdaConfig {
        private String defaultFunctionName;
        private String endpoint;
        private int timeoutSeconds = 30;
        private int memorySize = 128;
        private Map<String, String> additionalProperties;
        
        // Getters and setters
        public String getDefaultFunctionName() {
            return defaultFunctionName;
        }
        
        public void setDefaultFunctionName(String defaultFunctionName) {
            this.defaultFunctionName = defaultFunctionName;
        }
        
        public String getEndpoint() {
            return endpoint;
        }
        
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        
        public int getTimeoutSeconds() {
            return timeoutSeconds;
        }
        
        public void setTimeoutSeconds(int timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
        }
        
        public int getMemorySize() {
            return memorySize;
        }
        
        public void setMemorySize(int memorySize) {
            this.memorySize = memorySize;
        }
        
        public Map<String, String> getAdditionalProperties() {
            return additionalProperties;
        }
        
        public void setAdditionalProperties(Map<String, String> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }
    }
    
    /**
     * DynamoDB configuration.
     */
    public static class DynamoDbConfig {
        private String defaultTableName;
        private String endpoint;
        private boolean local = false;
        private Map<String, String> additionalProperties;
        
        // Getters and setters
        public String getDefaultTableName() {
            return defaultTableName;
        }
        
        public void setDefaultTableName(String defaultTableName) {
            this.defaultTableName = defaultTableName;
        }
        
        public String getEndpoint() {
            return endpoint;
        }
        
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        
        public boolean isLocal() {
            return local;
        }
        
        public void setLocal(boolean local) {
            this.local = local;
        }
        
        public Map<String, String> getAdditionalProperties() {
            return additionalProperties;
        }
        
        public void setAdditionalProperties(Map<String, String> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }
    }
    
    /**
     * Secrets Manager configuration.
     */
    public static class SecretsManagerConfig {
        private String endpoint;
        private Map<String, String> additionalProperties;
        
        // Getters and setters
        public String getEndpoint() {
            return endpoint;
        }
        
        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
        
        public Map<String, String> getAdditionalProperties() {
            return additionalProperties;
        }
        
        public void setAdditionalProperties(Map<String, String> additionalProperties) {
            this.additionalProperties = additionalProperties;
        }
    }
    
    // Getters and setters
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public CredentialsConfig getCredentials() {
        return credentials;
    }
    
    public void setCredentials(CredentialsConfig credentials) {
        this.credentials = credentials;
    }
    
    public S3Config getS3() {
        return s3;
    }
    
    public void setS3(S3Config s3) {
        this.s3 = s3;
    }
    
    public SqsConfig getSqs() {
        return sqs;
    }
    
    public void setSqs(SqsConfig sqs) {
        this.sqs = sqs;
    }
    
    public SnsConfig getSns() {
        return sns;
    }
    
    public void setSns(SnsConfig sns) {
        this.sns = sns;
    }
    
    public LambdaConfig getLambda() {
        return lambda;
    }
    
    public void setLambda(LambdaConfig lambda) {
        this.lambda = lambda;
    }
    
    public DynamoDbConfig getDynamodb() {
        return dynamodb;
    }
    
    public void setDynamodb(DynamoDbConfig dynamodb) {
        this.dynamodb = dynamodb;
    }
    
    public SecretsManagerConfig getSecrets() {
        return secrets;
    }
    
    public void setSecrets(SecretsManagerConfig secrets) {
        this.secrets = secrets;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }
    
    public void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }
    
    public boolean isTracingEnabled() {
        return tracingEnabled;
    }
    
    public void setTracingEnabled(boolean tracingEnabled) {
        this.tracingEnabled = tracingEnabled;
    }
}
