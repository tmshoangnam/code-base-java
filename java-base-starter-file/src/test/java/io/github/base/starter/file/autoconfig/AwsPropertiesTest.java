package io.github.base.starter.file.autoconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link AwsProperties}.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("AwsProperties Tests")
class AwsPropertiesTest {

    private AwsProperties properties;

    @BeforeEach
    void setUp() {
        properties = new AwsProperties();
    }

    @Nested
    @DisplayName("Main Properties Tests")
    class MainPropertiesTests {

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertThat(properties.getRegion()).isEqualTo("us-east-1");
            assertThat(properties.isEnabled()).isTrue();
            assertThat(properties.isMetricsEnabled()).isTrue();
            assertThat(properties.isTracingEnabled()).isTrue();
            assertThat(properties.getCredentials()).isNotNull();
            assertThat(properties.getS3()).isNotNull();
            assertThat(properties.getSqs()).isNotNull();
            assertThat(properties.getSns()).isNotNull();
            assertThat(properties.getLambda()).isNotNull();
            assertThat(properties.getDynamodb()).isNotNull();
            assertThat(properties.getSecrets()).isNotNull();
        }

        @Test
        @DisplayName("Should set and get region")
        void shouldSetAndGetRegion() {
            String region = "us-west-2";
            properties.setRegion(region);
            assertThat(properties.getRegion()).isEqualTo(region);
        }

        @Test
        @DisplayName("Should set and get enabled flag")
        void shouldSetAndGetEnabledFlag() {
            properties.setEnabled(false);
            assertThat(properties.isEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should set and get metrics enabled flag")
        void shouldSetAndGetMetricsEnabledFlag() {
            properties.setMetricsEnabled(false);
            assertThat(properties.isMetricsEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should set and get tracing enabled flag")
        void shouldSetAndGetTracingEnabledFlag() {
            properties.setTracingEnabled(false);
            assertThat(properties.isTracingEnabled()).isFalse();
        }
    }

    @Nested
    @DisplayName("CredentialsConfig Tests")
    class CredentialsConfigTests {

        private AwsProperties.CredentialsConfig credentials;

        @BeforeEach
        void setUp() {
            credentials = new AwsProperties.CredentialsConfig();
        }

        @Test
        @DisplayName("Should set and get access key ID")
        void shouldSetAndGetAccessKeyId() {
            String accessKeyId = "AKIAIOSFODNN7EXAMPLE";
            credentials.setAccessKeyId(accessKeyId);
            assertThat(credentials.getAccessKeyId()).isEqualTo(accessKeyId);
        }

        @Test
        @DisplayName("Should set and get secret access key")
        void shouldSetAndGetSecretAccessKey() {
            String secretAccessKey = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
            credentials.setSecretAccessKey(secretAccessKey);
            assertThat(credentials.getSecretAccessKey()).isEqualTo(secretAccessKey);
        }

        @Test
        @DisplayName("Should set and get session token")
        void shouldSetAndGetSessionToken() {
            String sessionToken = "AQoDYXdzEJr...";
            credentials.setSessionToken(sessionToken);
            assertThat(credentials.getSessionToken()).isEqualTo(sessionToken);
        }

        @Test
        @DisplayName("Should set and get profile name")
        void shouldSetAndGetProfileName() {
            String profileName = "default";
            credentials.setProfileName(profileName);
            assertThat(credentials.getProfileName()).isEqualTo(profileName);
        }

        @Test
        @DisplayName("Should set and get role ARN")
        void shouldSetAndGetRoleArn() {
            String roleArn = "arn:aws:iam::123456789012:role/MyRole";
            credentials.setRoleArn(roleArn);
            assertThat(credentials.getRoleArn()).isEqualTo(roleArn);
        }

        @Test
        @DisplayName("Should set and get external ID")
        void shouldSetAndGetExternalId() {
            String externalId = "external-id-123";
            credentials.setExternalId(externalId);
            assertThat(credentials.getExternalId()).isEqualTo(externalId);
        }

        @Test
        @DisplayName("Should set and get region")
        void shouldSetAndGetRegion() {
            String region = "eu-west-1";
            credentials.setRegion(region);
            assertThat(credentials.getRegion()).isEqualTo(region);
        }
    }

    @Nested
    @DisplayName("S3Config Tests")
    class S3ConfigTests {

        private AwsProperties.S3Config s3Config;

        @BeforeEach
        void setUp() {
            s3Config = new AwsProperties.S3Config();
        }

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertThat(s3Config.getDefaultBucket()).isNull();
            assertThat(s3Config.getEndpoint()).isNull();
            assertThat(s3Config.isPathStyleAccess()).isFalse();
            assertThat(s3Config.isAccelerateMode()).isFalse();
            assertThat(s3Config.isDualstack()).isFalse();
            assertThat(s3Config.isForceGlobalBucketAccess()).isFalse();
            assertThat(s3Config.getAdditionalProperties()).isNull();
        }

        @Test
        @DisplayName("Should set and get default bucket")
        void shouldSetAndGetDefaultBucket() {
            String bucket = "my-bucket";
            s3Config.setDefaultBucket(bucket);
            assertThat(s3Config.getDefaultBucket()).isEqualTo(bucket);
        }

        @Test
        @DisplayName("Should set and get endpoint")
        void shouldSetAndGetEndpoint() {
            String endpoint = "https://s3.amazonaws.com";
            s3Config.setEndpoint(endpoint);
            assertThat(s3Config.getEndpoint()).isEqualTo(endpoint);
        }

        @Test
        @DisplayName("Should set and get path style access")
        void shouldSetAndGetPathStyleAccess() {
            s3Config.setPathStyleAccess(true);
            assertThat(s3Config.isPathStyleAccess()).isTrue();
        }

        @Test
        @DisplayName("Should set and get accelerate mode")
        void shouldSetAndGetAccelerateMode() {
            s3Config.setAccelerateMode(true);
            assertThat(s3Config.isAccelerateMode()).isTrue();
        }

        @Test
        @DisplayName("Should set and get dualstack")
        void shouldSetAndGetDualstack() {
            s3Config.setDualstack(true);
            assertThat(s3Config.isDualstack()).isTrue();
        }

        @Test
        @DisplayName("Should set and get force global bucket access")
        void shouldSetAndGetForceGlobalBucketAccess() {
            s3Config.setForceGlobalBucketAccess(true);
            assertThat(s3Config.isForceGlobalBucketAccess()).isTrue();
        }

        @Test
        @DisplayName("Should set and get additional properties")
        void shouldSetAndGetAdditionalProperties() {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put("key1", "value1");
            additionalProperties.put("key2", "value2");

            s3Config.setAdditionalProperties(additionalProperties);
            assertThat(s3Config.getAdditionalProperties()).isEqualTo(additionalProperties);
        }
    }

    @Nested
    @DisplayName("SqsConfig Tests")
    class SqsConfigTests {

        private AwsProperties.SqsConfig sqsConfig;

        @BeforeEach
        void setUp() {
            sqsConfig = new AwsProperties.SqsConfig();
        }

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertThat(sqsConfig.getDefaultQueueUrl()).isNull();
            assertThat(sqsConfig.getEndpoint()).isNull();
            assertThat(sqsConfig.getMaxMessages()).isEqualTo(10);
            assertThat(sqsConfig.getWaitTimeSeconds()).isZero();
            assertThat(sqsConfig.getVisibilityTimeoutSeconds()).isEqualTo(30);
            assertThat(sqsConfig.getAdditionalProperties()).isNull();
        }

        @Test
        @DisplayName("Should set and get default queue URL")
        void shouldSetAndGetDefaultQueueUrl() {
            String queueUrl = "https://sqs.us-east-1.amazonaws.com/123456789012/my-queue";
            sqsConfig.setDefaultQueueUrl(queueUrl);
            assertThat(sqsConfig.getDefaultQueueUrl()).isEqualTo(queueUrl);
        }

        @Test
        @DisplayName("Should set and get endpoint")
        void shouldSetAndGetEndpoint() {
            String endpoint = "https://sqs.us-east-1.amazonaws.com";
            sqsConfig.setEndpoint(endpoint);
            assertThat(sqsConfig.getEndpoint()).isEqualTo(endpoint);
        }

        @Test
        @DisplayName("Should set and get max messages")
        void shouldSetAndGetMaxMessages() {
            int maxMessages = 5;
            sqsConfig.setMaxMessages(maxMessages);
            assertThat(sqsConfig.getMaxMessages()).isEqualTo(maxMessages);
        }

        @Test
        @DisplayName("Should set and get wait time seconds")
        void shouldSetAndGetWaitTimeSeconds() {
            int waitTimeSeconds = 20;
            sqsConfig.setWaitTimeSeconds(waitTimeSeconds);
            assertThat(sqsConfig.getWaitTimeSeconds()).isEqualTo(waitTimeSeconds);
        }

        @Test
        @DisplayName("Should set and get visibility timeout seconds")
        void shouldSetAndGetVisibilityTimeoutSeconds() {
            int visibilityTimeoutSeconds = 60;
            sqsConfig.setVisibilityTimeoutSeconds(visibilityTimeoutSeconds);
            assertThat(sqsConfig.getVisibilityTimeoutSeconds()).isEqualTo(visibilityTimeoutSeconds);
        }

        @Test
        @DisplayName("Should set and get additional properties")
        void shouldSetAndGetAdditionalProperties() {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put("key1", "value1");

            sqsConfig.setAdditionalProperties(additionalProperties);
            assertThat(sqsConfig.getAdditionalProperties()).isEqualTo(additionalProperties);
        }
    }

    @Nested
    @DisplayName("SnsConfig Tests")
    class SnsConfigTests {

        private AwsProperties.SnsConfig snsConfig;

        @BeforeEach
        void setUp() {
            snsConfig = new AwsProperties.SnsConfig();
        }

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertThat(snsConfig.getDefaultTopicArn()).isNull();
            assertThat(snsConfig.getEndpoint()).isNull();
            assertThat(snsConfig.getAdditionalProperties()).isNull();
        }

        @Test
        @DisplayName("Should set and get default topic ARN")
        void shouldSetAndGetDefaultTopicArn() {
            String topicArn = "arn:aws:sns:us-east-1:123456789012:my-topic";
            snsConfig.setDefaultTopicArn(topicArn);
            assertThat(snsConfig.getDefaultTopicArn()).isEqualTo(topicArn);
        }

        @Test
        @DisplayName("Should set and get endpoint")
        void shouldSetAndGetEndpoint() {
            String endpoint = "https://sns.us-east-1.amazonaws.com";
            snsConfig.setEndpoint(endpoint);
            assertThat(snsConfig.getEndpoint()).isEqualTo(endpoint);
        }

        @Test
        @DisplayName("Should set and get additional properties")
        void shouldSetAndGetAdditionalProperties() {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put("key1", "value1");

            snsConfig.setAdditionalProperties(additionalProperties);
            assertThat(snsConfig.getAdditionalProperties()).isEqualTo(additionalProperties);
        }
    }

    @Nested
    @DisplayName("LambdaConfig Tests")
    class LambdaConfigTests {

        private AwsProperties.LambdaConfig lambdaConfig;

        @BeforeEach
        void setUp() {
            lambdaConfig = new AwsProperties.LambdaConfig();
        }

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertThat(lambdaConfig.getDefaultFunctionName()).isNull();
            assertThat(lambdaConfig.getEndpoint()).isNull();
            assertThat(lambdaConfig.getTimeoutSeconds()).isEqualTo(30);
            assertThat(lambdaConfig.getMemorySize()).isEqualTo(128);
            assertThat(lambdaConfig.getAdditionalProperties()).isNull();
        }

        @Test
        @DisplayName("Should set and get default function name")
        void shouldSetAndGetDefaultFunctionName() {
            String functionName = "my-function";
            lambdaConfig.setDefaultFunctionName(functionName);
            assertThat(lambdaConfig.getDefaultFunctionName()).isEqualTo(functionName);
        }

        @Test
        @DisplayName("Should set and get endpoint")
        void shouldSetAndGetEndpoint() {
            String endpoint = "https://lambda.us-east-1.amazonaws.com";
            lambdaConfig.setEndpoint(endpoint);
            assertThat(lambdaConfig.getEndpoint()).isEqualTo(endpoint);
        }

        @Test
        @DisplayName("Should set and get timeout seconds")
        void shouldSetAndGetTimeoutSeconds() {
            int timeoutSeconds = 60;
            lambdaConfig.setTimeoutSeconds(timeoutSeconds);
            assertThat(lambdaConfig.getTimeoutSeconds()).isEqualTo(timeoutSeconds);
        }

        @Test
        @DisplayName("Should set and get memory size")
        void shouldSetAndGetMemorySize() {
            int memorySize = 256;
            lambdaConfig.setMemorySize(memorySize);
            assertThat(lambdaConfig.getMemorySize()).isEqualTo(memorySize);
        }

        @Test
        @DisplayName("Should set and get additional properties")
        void shouldSetAndGetAdditionalProperties() {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put("key1", "value1");

            lambdaConfig.setAdditionalProperties(additionalProperties);
            assertThat(lambdaConfig.getAdditionalProperties()).isEqualTo(additionalProperties);
        }
    }

    @Nested
    @DisplayName("DynamoDbConfig Tests")
    class DynamoDbConfigTests {

        private AwsProperties.DynamoDbConfig dynamoDbConfig;

        @BeforeEach
        void setUp() {
            dynamoDbConfig = new AwsProperties.DynamoDbConfig();
        }

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertThat(dynamoDbConfig.getDefaultTableName()).isNull();
            assertThat(dynamoDbConfig.getEndpoint()).isNull();
            assertThat(dynamoDbConfig.isLocal()).isFalse();
            assertThat(dynamoDbConfig.getAdditionalProperties()).isNull();
        }

        @Test
        @DisplayName("Should set and get default table name")
        void shouldSetAndGetDefaultTableName() {
            String tableName = "my-table";
            dynamoDbConfig.setDefaultTableName(tableName);
            assertThat(dynamoDbConfig.getDefaultTableName()).isEqualTo(tableName);
        }

        @Test
        @DisplayName("Should set and get endpoint")
        void shouldSetAndGetEndpoint() {
            String endpoint = "https://dynamodb.us-east-1.amazonaws.com";
            dynamoDbConfig.setEndpoint(endpoint);
            assertThat(dynamoDbConfig.getEndpoint()).isEqualTo(endpoint);
        }

        @Test
        @DisplayName("Should set and get local flag")
        void shouldSetAndGetLocalFlag() {
            dynamoDbConfig.setLocal(true);
            assertThat(dynamoDbConfig.isLocal()).isTrue();
        }

        @Test
        @DisplayName("Should set and get additional properties")
        void shouldSetAndGetAdditionalProperties() {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put("key1", "value1");

            dynamoDbConfig.setAdditionalProperties(additionalProperties);
            assertThat(dynamoDbConfig.getAdditionalProperties()).isEqualTo(additionalProperties);
        }
    }

    @Nested
    @DisplayName("SecretsManagerConfig Tests")
    class SecretsManagerConfigTests {

        private AwsProperties.SecretsManagerConfig secretsConfig;

        @BeforeEach
        void setUp() {
            secretsConfig = new AwsProperties.SecretsManagerConfig();
        }

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertThat(secretsConfig.getEndpoint()).isNull();
            assertThat(secretsConfig.getAdditionalProperties()).isNull();
        }

        @Test
        @DisplayName("Should set and get endpoint")
        void shouldSetAndGetEndpoint() {
            String endpoint = "https://secretsmanager.us-east-1.amazonaws.com";
            secretsConfig.setEndpoint(endpoint);
            assertThat(secretsConfig.getEndpoint()).isEqualTo(endpoint);
        }

        @Test
        @DisplayName("Should set and get additional properties")
        void shouldSetAndGetAdditionalProperties() {
            Map<String, String> additionalProperties = new HashMap<>();
            additionalProperties.put("key1", "value1");

            secretsConfig.setAdditionalProperties(additionalProperties);
            assertThat(secretsConfig.getAdditionalProperties()).isEqualTo(additionalProperties);
        }
    }
}
