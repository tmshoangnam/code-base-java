package io.github.base.starter.file.autoconfig;

import io.github.base.aws.AwsServices;
import io.github.base.aws.api.config.AwsServiceConfig;
import io.github.base.aws.api.s3.S3Operations;
import io.github.base.aws.api.sqs.SqsOperations;
import io.github.base.aws.api.sns.SnsOperations;
import io.github.base.aws.api.lambda.LambdaOperations;
import io.github.base.aws.api.dynamodb.DynamoDbOperations;
import io.github.base.aws.api.secrets.SecretsManagerOperations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link AwsAutoConfiguration}.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AwsAutoConfiguration Tests")
class AwsAutoConfigurationTest {

    private AwsAutoConfiguration configuration;
    private AwsProperties properties;

    @BeforeEach
    void setUp() {
        configuration = new AwsAutoConfiguration();
        properties = createTestAwsProperties();
    }

    private AwsProperties createTestAwsProperties() {
        AwsProperties props = new AwsProperties();
        props.setRegion("us-west-2");
        props.setEnabled(true);
        props.setMetricsEnabled(true);
        props.setTracingEnabled(true);

        // Configure credentials
        AwsProperties.CredentialsConfig credentials = new AwsProperties.CredentialsConfig();
        credentials.setAccessKeyId("AKIAIOSFODNN7EXAMPLE");
        credentials.setSecretAccessKey("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
        credentials.setSessionToken("AQoDYXdzEJr...");
        credentials.setRegion("us-west-2");
        props.setCredentials(credentials);

        // Configure S3
        AwsProperties.S3Config s3Config = new AwsProperties.S3Config();
        s3Config.setDefaultBucket("test-bucket");
        s3Config.setPathStyleAccess(true);
        props.setS3(s3Config);

        // Configure SQS
        AwsProperties.SqsConfig sqsConfig = new AwsProperties.SqsConfig();
        sqsConfig.setDefaultQueueUrl("https://sqs.us-west-2.amazonaws.com/123456789012/test-queue");
        sqsConfig.setMaxMessages(5);
        props.setSqs(sqsConfig);

        // Configure SNS
        AwsProperties.SnsConfig snsConfig = new AwsProperties.SnsConfig();
        snsConfig.setDefaultTopicArn("arn:aws:sns:us-west-2:123456789012:test-topic");
        props.setSns(snsConfig);

        // Configure Lambda
        AwsProperties.LambdaConfig lambdaConfig = new AwsProperties.LambdaConfig();
        lambdaConfig.setDefaultFunctionName("test-function");
        lambdaConfig.setTimeoutSeconds(60);
        props.setLambda(lambdaConfig);

        // Configure DynamoDB
        AwsProperties.DynamoDbConfig dynamoDbConfig = new AwsProperties.DynamoDbConfig();
        dynamoDbConfig.setDefaultTableName("test-table");
        dynamoDbConfig.setLocal(false);
        props.setDynamodb(dynamoDbConfig);

        // Configure Secrets Manager
        AwsProperties.SecretsManagerConfig secretsConfig = new AwsProperties.SecretsManagerConfig();
        secretsConfig.setEndpoint("https://secretsmanager.us-west-2.amazonaws.com");
        props.setSecrets(secretsConfig);

        return props;
    }

    @Nested
    @DisplayName("S3 Operations Bean Tests")
    class S3OperationsBeanTests {

        @Test
        @DisplayName("Should create S3 operations bean")
        void shouldCreateS3OperationsBean() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                S3Operations mockS3Operations = mock(S3Operations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockS3Operations);

                S3Operations result = configuration.s3Operations(properties);

                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(mockS3Operations);
            }
        }

        @Test
        @DisplayName("Should create S3 operations with correct configuration")
        void shouldCreateS3OperationsWithCorrectConfiguration() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                S3Operations mockS3Operations = mock(S3Operations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockS3Operations);

                configuration.s3Operations(properties);

                awsServicesMock.verify(() -> AwsServices.getService(eq("s3"), any(AwsServiceConfig.class)));
            }
        }
    }

    @Nested
    @DisplayName("SQS Operations Bean Tests")
    class SqsOperationsBeanTests {

        @Test
        @DisplayName("Should create SQS operations bean")
        void shouldCreateSqsOperationsBean() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                SqsOperations mockSqsOperations = mock(SqsOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockSqsOperations);

                SqsOperations result = configuration.sqsOperations(properties);

                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(mockSqsOperations);
            }
        }

        @Test
        @DisplayName("Should create SQS operations with correct configuration")
        void shouldCreateSqsOperationsWithCorrectConfiguration() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                SqsOperations mockSqsOperations = mock(SqsOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockSqsOperations);

                configuration.sqsOperations(properties);

                awsServicesMock.verify(() -> AwsServices.getService(eq("sqs"), any(AwsServiceConfig.class)));
            }
        }
    }

    @Nested
    @DisplayName("SNS Operations Bean Tests")
    class SnsOperationsBeanTests {

        @Test
        @DisplayName("Should create SNS operations bean")
        void shouldCreateSnsOperationsBean() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                SnsOperations mockSnsOperations = mock(SnsOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockSnsOperations);

                SnsOperations result = configuration.snsOperations(properties);

                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(mockSnsOperations);
            }
        }

        @Test
        @DisplayName("Should create SNS operations with correct configuration")
        void shouldCreateSnsOperationsWithCorrectConfiguration() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                SnsOperations mockSnsOperations = mock(SnsOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockSnsOperations);

                configuration.snsOperations(properties);

                awsServicesMock.verify(() -> AwsServices.getService(eq("sns"), any(AwsServiceConfig.class)));
            }
        }
    }

    @Nested
    @DisplayName("Lambda Operations Bean Tests")
    class LambdaOperationsBeanTests {

        @Test
        @DisplayName("Should create Lambda operations bean")
        void shouldCreateLambdaOperationsBean() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                LambdaOperations mockLambdaOperations = mock(LambdaOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockLambdaOperations);

                LambdaOperations result = configuration.lambdaOperations(properties);

                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(mockLambdaOperations);
            }
        }

        @Test
        @DisplayName("Should create Lambda operations with correct configuration")
        void shouldCreateLambdaOperationsWithCorrectConfiguration() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                LambdaOperations mockLambdaOperations = mock(LambdaOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockLambdaOperations);

                configuration.lambdaOperations(properties);

                awsServicesMock.verify(() -> AwsServices.getService(eq("lambda"), any(AwsServiceConfig.class)));
            }
        }
    }

    @Nested
    @DisplayName("DynamoDB Operations Bean Tests")
    class DynamoDbOperationsBeanTests {

        @Test
        @DisplayName("Should create DynamoDB operations bean")
        void shouldCreateDynamoDbOperationsBean() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                DynamoDbOperations mockDynamoDbOperations = mock(DynamoDbOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockDynamoDbOperations);

                DynamoDbOperations result = configuration.dynamoDbOperations(properties);

                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(mockDynamoDbOperations);
            }
        }

        @Test
        @DisplayName("Should create DynamoDB operations with correct configuration")
        void shouldCreateDynamoDbOperationsWithCorrectConfiguration() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                DynamoDbOperations mockDynamoDbOperations = mock(DynamoDbOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockDynamoDbOperations);

                configuration.dynamoDbOperations(properties);

                awsServicesMock.verify(() -> AwsServices.getService(eq("dynamodb"), any(AwsServiceConfig.class)));
            }
        }
    }

    @Nested
    @DisplayName("Secrets Manager Operations Bean Tests")
    class SecretsManagerOperationsBeanTests {

        @Test
        @DisplayName("Should create Secrets Manager operations bean")
        void shouldCreateSecretsManagerOperationsBean() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                SecretsManagerOperations mockSecretsManagerOperations = mock(SecretsManagerOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockSecretsManagerOperations);

                SecretsManagerOperations result = configuration.secretsManagerOperations(properties);

                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(mockSecretsManagerOperations);
            }
        }

        @Test
        @DisplayName("Should create Secrets Manager operations with correct configuration")
        void shouldCreateSecretsManagerOperationsWithCorrectConfiguration() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                SecretsManagerOperations mockSecretsManagerOperations = mock(SecretsManagerOperations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockSecretsManagerOperations);

                configuration.secretsManagerOperations(properties);

                awsServicesMock.verify(() -> AwsServices.getService(eq("secrets"), any(AwsServiceConfig.class)));
            }
        }
    }

    @Nested
    @DisplayName("Service Configuration Tests")
    class ServiceConfigurationTests {

        @Test
        @DisplayName("Should handle null service response")
        void shouldHandleNullServiceResponse() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(null);

                S3Operations result = configuration.s3Operations(properties);

                assertThat(result).isNull();
            }
        }

        @Test
        @DisplayName("Should handle service creation failure")
        void shouldHandleServiceCreationFailure() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenThrow(new RuntimeException("Service creation failed"));

                try {
                    configuration.s3Operations(properties);
                } catch (RuntimeException e) {
                    assertThat(e.getMessage()).isEqualTo("Service creation failed");
                }
            }
        }
    }

    @Nested
    @DisplayName("Properties Integration Tests")
    class PropertiesIntegrationTests {

        @Test
        @DisplayName("Should use properties for service configuration")
        void shouldUsePropertiesForServiceConfiguration() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                S3Operations mockS3Operations = mock(S3Operations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockS3Operations);

                // Test with different properties
                AwsProperties differentProperties = new AwsProperties();
                differentProperties.setRegion("eu-west-1");
                differentProperties.setEnabled(false);
                // Set valid credentials to avoid validation errors
                AwsProperties.CredentialsConfig credentials = new AwsProperties.CredentialsConfig();
                credentials.setAccessKeyId("AKIAIOSFODNN7EXAMPLE");
                credentials.setSecretAccessKey("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
                differentProperties.setCredentials(credentials);

                configuration.s3Operations(differentProperties);

                awsServicesMock.verify(() -> AwsServices.getService(eq("s3"), any(AwsServiceConfig.class)));
            }
        }

        @Test
        @DisplayName("Should handle minimal properties")
        void shouldHandleMinimalProperties() {
            try (MockedStatic<AwsServices> awsServicesMock = mockStatic(AwsServices.class)) {
                S3Operations mockS3Operations = mock(S3Operations.class);
                awsServicesMock.when(() -> AwsServices.getService(anyString(), any(AwsServiceConfig.class)))
                    .thenReturn(mockS3Operations);

                AwsProperties minimalProperties = new AwsProperties();
                // Use all default values but set valid credentials
                AwsProperties.CredentialsConfig credentials = new AwsProperties.CredentialsConfig();
                credentials.setAccessKeyId("AKIAIOSFODNN7EXAMPLE");
                credentials.setSecretAccessKey("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
                minimalProperties.setCredentials(credentials);

                S3Operations result = configuration.s3Operations(minimalProperties);

                assertThat(result).isNotNull();
                awsServicesMock.verify(() -> AwsServices.getService(eq("s3"), any(AwsServiceConfig.class)));
            }
        }
    }
}
