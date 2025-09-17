package io.github.base.aws.api.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link AwsClientConfig}.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("AwsClientConfig Tests")
class AwsClientConfigTest {

    private static final String REGION = "us-east-1";
    private static final AwsCredentials CREDENTIALS = AwsCredentials.of("accessKey", "secretKey");
    private static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(15);
    private static final Duration SOCKET_TIMEOUT = Duration.ofSeconds(45);
    private static final int MAX_CONNECTIONS = 100;
    private static final int MAX_RETRIES = 5;
    private static final String RETRY_MODE = "ADAPTIVE";
    private static final String ENDPOINT_OVERRIDE = "https://custom-endpoint.com";
    private static final Map<String, Object> ADDITIONAL_PROPERTIES = Map.of("key1", "value1", "key2", 123);

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create AwsClientConfig with all parameters")
        void shouldCreateAwsClientConfigWithAllParameters() {
            // When
            AwsClientConfig config = new AwsClientConfig(
                REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT, MAX_CONNECTIONS,
                MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES
            );

            // Then
            assertThat(config.region()).isEqualTo(REGION);
            assertThat(config.credentials()).isEqualTo(CREDENTIALS);
            assertThat(config.connectionTimeout()).isEqualTo(CONNECTION_TIMEOUT);
            assertThat(config.socketTimeout()).isEqualTo(SOCKET_TIMEOUT);
            assertThat(config.maxConnections()).isEqualTo(MAX_CONNECTIONS);
            assertThat(config.maxRetries()).isEqualTo(MAX_RETRIES);
            assertThat(config.retryMode()).isEqualTo(RETRY_MODE);
            assertThat(config.endpointOverride()).isEqualTo(ENDPOINT_OVERRIDE);
            assertThat(config.useAccelerateEndpoint()).isTrue();
            assertThat(config.useDualstackEndpoint()).isTrue();
            assertThat(config.usePathStyleAccess()).isTrue();
            assertThat(config.additionalProperties()).isEqualTo(ADDITIONAL_PROPERTIES);
        }

        @Test
        @DisplayName("Should create AwsClientConfig with null values")
        void shouldCreateAwsClientConfigWithNullValues() {
            // When
            AwsClientConfig config = new AwsClientConfig(
                null, null, null, null, 0, 0, null, null, false, false, false, null
            );

            // Then
            assertThat(config.region()).isNull();
            assertThat(config.credentials()).isNull();
            assertThat(config.connectionTimeout()).isNull();
            assertThat(config.socketTimeout()).isNull();
            assertThat(config.maxConnections()).isZero();
            assertThat(config.maxRetries()).isZero();
            assertThat(config.retryMode()).isNull();
            assertThat(config.endpointOverride()).isNull();
            assertThat(config.useAccelerateEndpoint()).isFalse();
            assertThat(config.useDualstackEndpoint()).isFalse();
            assertThat(config.usePathStyleAccess()).isFalse();
            assertThat(config.additionalProperties()).isNull();
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("of method with region and credentials should create config with defaults")
        void ofMethodWithRegionAndCredentialsShouldCreateConfigWithDefaults() {
            // When
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);

            // Then
            assertThat(config.region()).isEqualTo(REGION);
            assertThat(config.credentials()).isEqualTo(CREDENTIALS);
            assertThat(config.connectionTimeout()).isEqualTo(Duration.ofSeconds(10));
            assertThat(config.socketTimeout()).isEqualTo(Duration.ofSeconds(30));
            assertThat(config.maxConnections()).isEqualTo(50);
            assertThat(config.maxRetries()).isEqualTo(3);
            assertThat(config.retryMode()).isEqualTo("STANDARD");
            assertThat(config.endpointOverride()).isNull();
            assertThat(config.useAccelerateEndpoint()).isFalse();
            assertThat(config.useDualstackEndpoint()).isFalse();
            assertThat(config.usePathStyleAccess()).isFalse();
            assertThat(config.additionalProperties()).isEmpty();
        }

        @Test
        @DisplayName("of method with all parameters should create config")
        void ofMethodWithAllParametersShouldCreateConfig() {
            // When
            AwsClientConfig config = AwsClientConfig.of(
                REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT, MAX_CONNECTIONS,
                MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES
            );

            // Then
            assertThat(config.region()).isEqualTo(REGION);
            assertThat(config.credentials()).isEqualTo(CREDENTIALS);
            assertThat(config.connectionTimeout()).isEqualTo(CONNECTION_TIMEOUT);
            assertThat(config.socketTimeout()).isEqualTo(SOCKET_TIMEOUT);
            assertThat(config.maxConnections()).isEqualTo(MAX_CONNECTIONS);
            assertThat(config.maxRetries()).isEqualTo(MAX_RETRIES);
            assertThat(config.retryMode()).isEqualTo(RETRY_MODE);
            assertThat(config.endpointOverride()).isEqualTo(ENDPOINT_OVERRIDE);
            assertThat(config.useAccelerateEndpoint()).isTrue();
            assertThat(config.useDualstackEndpoint()).isTrue();
            assertThat(config.usePathStyleAccess()).isTrue();
            assertThat(config.additionalProperties()).isEqualTo(ADDITIONAL_PROPERTIES);
        }

        @Test
        @DisplayName("of method should trim whitespace from region")
        void ofMethodShouldTrimWhitespaceFromRegion() {
            // Given
            String regionWithWhitespace = "  " + REGION + "  ";

            // When
            AwsClientConfig config = AwsClientConfig.of(regionWithWhitespace, CREDENTIALS);

            // Then
            assertThat(config.region()).isEqualTo(REGION);
        }

        @Test
        @DisplayName("of method should handle null timeouts with defaults")
        void ofMethodShouldHandleNullTimeoutsWithDefaults() {
            // When
            AwsClientConfig config = AwsClientConfig.of(
                REGION, CREDENTIALS, null, null, MAX_CONNECTIONS, MAX_RETRIES,
                RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES
            );

            // Then
            assertThat(config.connectionTimeout()).isEqualTo(Duration.ofSeconds(10));
            assertThat(config.socketTimeout()).isEqualTo(Duration.ofSeconds(30));
        }

        @Test
        @DisplayName("of method should handle null retry mode with default")
        void ofMethodShouldHandleNullRetryModeWithDefault() {
            // When
            AwsClientConfig config = AwsClientConfig.of(
                REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT, MAX_CONNECTIONS,
                MAX_RETRIES, null, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES
            );

            // Then
            assertThat(config.retryMode()).isEqualTo("STANDARD");
        }

        @Test
        @DisplayName("of method should handle null additional properties with empty map")
        void ofMethodShouldHandleNullAdditionalPropertiesWithEmptyMap() {
            // When
            AwsClientConfig config = AwsClientConfig.of(
                REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT, MAX_CONNECTIONS,
                MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, null
            );

            // Then
            assertThat(config.additionalProperties()).isEmpty();
        }

        @Test
        @DisplayName("of method should enforce minimum values for maxConnections and maxRetries")
        void ofMethodShouldEnforceMinimumValuesForMaxConnectionsAndMaxRetries() {
            // When
            AwsClientConfig config = AwsClientConfig.of(
                REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT, 0, -1,
                RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES
            );

            // Then
            assertThat(config.maxConnections()).isEqualTo(1);
            assertThat(config.maxRetries()).isZero();
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for null region")
        void ofMethodShouldThrowIllegalArgumentExceptionForNullRegion() {
            // When & Then
            assertThatThrownBy(() -> AwsClientConfig.of(null, CREDENTIALS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Region cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for empty region")
        void ofMethodShouldThrowIllegalArgumentExceptionForEmptyRegion() {
            // When & Then
            assertThatThrownBy(() -> AwsClientConfig.of("", CREDENTIALS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Region cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for blank region")
        void ofMethodShouldThrowIllegalArgumentExceptionForBlankRegion() {
            // When & Then
            assertThatThrownBy(() -> AwsClientConfig.of("   ", CREDENTIALS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Region cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for null credentials")
        void ofMethodShouldThrowIllegalArgumentExceptionForNullCredentials() {
            // When & Then
            assertThatThrownBy(() -> AwsClientConfig.of(REGION, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Credentials cannot be null");
        }
    }

    @Nested
    @DisplayName("Builder Method Tests")
    class BuilderMethodTests {

        @Test
        @DisplayName("withRegion should create new config with updated region")
        void withRegionShouldCreateNewConfigWithUpdatedRegion() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);
            String newRegion = "us-west-2";

            // When
            AwsClientConfig updated = original.withRegion(newRegion);

            // Then
            assertThat(updated.region()).isEqualTo(newRegion);
            assertThat(updated.credentials()).isEqualTo(original.credentials());
            assertThat(updated.connectionTimeout()).isEqualTo(original.connectionTimeout());
            assertThat(updated.socketTimeout()).isEqualTo(original.socketTimeout());
            assertThat(updated.maxConnections()).isEqualTo(original.maxConnections());
            assertThat(updated.maxRetries()).isEqualTo(original.maxRetries());
            assertThat(updated.retryMode()).isEqualTo(original.retryMode());
            assertThat(updated.endpointOverride()).isEqualTo(original.endpointOverride());
            assertThat(updated.useAccelerateEndpoint()).isEqualTo(original.useAccelerateEndpoint());
            assertThat(updated.useDualstackEndpoint()).isEqualTo(original.useDualstackEndpoint());
            assertThat(updated.usePathStyleAccess()).isEqualTo(original.usePathStyleAccess());
            assertThat(updated.additionalProperties()).isEqualTo(original.additionalProperties());
            assertThat(original.region()).isEqualTo(REGION); // Original should be unchanged
        }

        @Test
        @DisplayName("withRegion should trim whitespace from region")
        void withRegionShouldTrimWhitespaceFromRegion() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);
            String newRegionWithWhitespace = "  us-west-2  ";

            // When
            AwsClientConfig updated = original.withRegion(newRegionWithWhitespace);

            // Then
            assertThat(updated.region()).isEqualTo("us-west-2");
        }

        @Test
        @DisplayName("withRegion should throw IllegalArgumentException for null region")
        void withRegionShouldThrowIllegalArgumentExceptionForNullRegion() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);

            // When & Then
            assertThatThrownBy(() -> original.withRegion(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Region cannot be null or empty");
        }

        @Test
        @DisplayName("withCredentials should create new config with updated credentials")
        void withCredentialsShouldCreateNewConfigWithUpdatedCredentials() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);
            AwsCredentials newCredentials = AwsCredentials.of("newAccessKey", "newSecretKey");

            // When
            AwsClientConfig updated = original.withCredentials(newCredentials);

            // Then
            assertThat(updated.credentials()).isEqualTo(newCredentials);
            assertThat(updated.region()).isEqualTo(original.region());
            assertThat(original.credentials()).isEqualTo(CREDENTIALS); // Original should be unchanged
        }

        @Test
        @DisplayName("withCredentials should throw IllegalArgumentException for null credentials")
        void withCredentialsShouldThrowIllegalArgumentExceptionForNullCredentials() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);

            // When & Then
            assertThatThrownBy(() -> original.withCredentials(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Credentials cannot be null");
        }

        @Test
        @DisplayName("withTimeouts should create new config with updated timeouts")
        void withTimeoutsShouldCreateNewConfigWithUpdatedTimeouts() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);
            Duration newConnectionTimeout = Duration.ofSeconds(20);
            Duration newSocketTimeout = Duration.ofSeconds(60);

            // When
            AwsClientConfig updated = original.withTimeouts(newConnectionTimeout, newSocketTimeout);

            // Then
            assertThat(updated.connectionTimeout()).isEqualTo(newConnectionTimeout);
            assertThat(updated.socketTimeout()).isEqualTo(newSocketTimeout);
            assertThat(updated.region()).isEqualTo(original.region());
            assertThat(updated.credentials()).isEqualTo(original.credentials());
            assertThat(original.connectionTimeout()).isEqualTo(Duration.ofSeconds(10)); // Original should be unchanged
        }

        @Test
        @DisplayName("withTimeouts should handle null timeouts")
        void withTimeoutsShouldHandleNullTimeouts() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);

            // When
            AwsClientConfig updated = original.withTimeouts(null, null);

            // Then
            assertThat(updated.connectionTimeout()).isEqualTo(original.connectionTimeout());
            assertThat(updated.socketTimeout()).isEqualTo(original.socketTimeout());
        }

        @Test
        @DisplayName("withConnectionSettings should create new config with updated connection settings")
        void withConnectionSettingsShouldCreateNewConfigWithUpdatedConnectionSettings() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);
            int newMaxConnections = 200;
            int newMaxRetries = 10;

            // When
            AwsClientConfig updated = original.withConnectionSettings(newMaxConnections, newMaxRetries);

            // Then
            assertThat(updated.maxConnections()).isEqualTo(newMaxConnections);
            assertThat(updated.maxRetries()).isEqualTo(newMaxRetries);
            assertThat(updated.region()).isEqualTo(original.region());
            assertThat(updated.credentials()).isEqualTo(original.credentials());
            assertThat(original.maxConnections()).isEqualTo(50); // Original should be unchanged
        }

        @Test
        @DisplayName("withConnectionSettings should enforce minimum values")
        void withConnectionSettingsShouldEnforceMinimumValues() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);

            // When
            AwsClientConfig updated = original.withConnectionSettings(0, -1);

            // Then
            assertThat(updated.maxConnections()).isEqualTo(1);
            assertThat(updated.maxRetries()).isZero();
        }

        @Test
        @DisplayName("withEndpointSettings should create new config with updated endpoint settings")
        void withEndpointSettingsShouldCreateNewConfigWithUpdatedEndpointSettings() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);
            String newEndpointOverride = "https://new-endpoint.com";

            // When
            AwsClientConfig updated = original.withEndpointSettings(
                newEndpointOverride, true, true, true
            );

            // Then
            assertThat(updated.endpointOverride()).isEqualTo(newEndpointOverride);
            assertThat(updated.useAccelerateEndpoint()).isTrue();
            assertThat(updated.useDualstackEndpoint()).isTrue();
            assertThat(updated.usePathStyleAccess()).isTrue();
            assertThat(updated.region()).isEqualTo(original.region());
            assertThat(updated.credentials()).isEqualTo(original.credentials());
            assertThat(original.useAccelerateEndpoint()).isFalse(); // Original should be unchanged
        }

        @Test
        @DisplayName("withAdditionalProperties should create new config with updated additional properties")
        void withAdditionalPropertiesShouldCreateNewConfigWithUpdatedAdditionalProperties() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);
            Map<String, Object> newProperties = Map.of("newKey", "newValue");

            // When
            AwsClientConfig updated = original.withAdditionalProperties(newProperties);

            // Then
            assertThat(updated.additionalProperties()).isEqualTo(newProperties);
            assertThat(updated.region()).isEqualTo(original.region());
            assertThat(updated.credentials()).isEqualTo(original.credentials());
            assertThat(original.additionalProperties()).isEmpty(); // Original should be unchanged
        }

        @Test
        @DisplayName("withAdditionalProperties should handle null properties")
        void withAdditionalPropertiesShouldHandleNullProperties() {
            // Given
            AwsClientConfig original = AwsClientConfig.of(REGION, CREDENTIALS);

            // When
            AwsClientConfig updated = original.withAdditionalProperties(null);

            // Then
            assertThat(updated.additionalProperties()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Property Access Tests")
    class PropertyAccessTests {

        @Test
        @DisplayName("getAdditionalProperty should return property value when exists")
        void getAdditionalPropertyShouldReturnPropertyValueWhenExists() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When
            Object value = config.getAdditionalProperty("key1");

            // Then
            assertThat(value).isEqualTo("value1");
        }

        @Test
        @DisplayName("getAdditionalProperty should return null when property does not exist")
        void getAdditionalPropertyShouldReturnNullWhenPropertyDoesNotExist() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When
            Object value = config.getAdditionalProperty("nonExistentKey");

            // Then
            assertThat(value).isNull();
        }

        @Test
        @DisplayName("getAdditionalProperty should return null when additional properties are null")
        void getAdditionalPropertyShouldReturnNullWhenAdditionalPropertiesAreNull() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);

            // When
            Object value = config.getAdditionalProperty("key1");

            // Then
            assertThat(value).isNull();
        }

        @Test
        @DisplayName("hasAdditionalProperty should return true when property exists")
        void hasAdditionalPropertyShouldReturnTrueWhenPropertyExists() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.hasAdditionalProperty("key1")).isTrue();
        }

        @Test
        @DisplayName("hasAdditionalProperty should return false when property does not exist")
        void hasAdditionalPropertyShouldReturnFalseWhenPropertyDoesNotExist() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.hasAdditionalProperty("nonExistentKey")).isFalse();
        }

        @Test
        @DisplayName("hasAdditionalProperty should return false when additional properties are null")
        void hasAdditionalPropertyShouldReturnFalseWhenAdditionalPropertiesAreNull() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);

            // When & Then
            assertThat(config.hasAdditionalProperty("key1")).isFalse();
        }
    }

    @Nested
    @DisplayName("Status Check Tests")
    class StatusCheckTests {

        @Test
        @DisplayName("hasEndpointOverride should return true when endpoint override is present")
        void hasEndpointOverrideShouldReturnTrueWhenEndpointOverrideIsPresent() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.hasEndpointOverride()).isTrue();
        }

        @Test
        @DisplayName("hasEndpointOverride should return false when endpoint override is null")
        void hasEndpointOverrideShouldReturnFalseWhenEndpointOverrideIsNull() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);

            // When & Then
            assertThat(config.hasEndpointOverride()).isFalse();
        }

        @Test
        @DisplayName("hasEndpointOverride should return false when endpoint override is empty")
        void hasEndpointOverrideShouldReturnFalseWhenEndpointOverrideIsEmpty() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, "", true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.hasEndpointOverride()).isFalse();
        }

        @Test
        @DisplayName("hasEndpointOverride should return false when endpoint override is blank")
        void hasEndpointOverrideShouldReturnFalseWhenEndpointOverrideIsBlank() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, "   ", true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.hasEndpointOverride()).isFalse();
        }

        @Test
        @DisplayName("hasAdditionalProperties should return true when additional properties are present")
        void hasAdditionalPropertiesShouldReturnTrueWhenAdditionalPropertiesArePresent() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.hasAdditionalProperties()).isTrue();
        }

        @Test
        @DisplayName("hasAdditionalProperties should return false when additional properties are null")
        void hasAdditionalPropertiesShouldReturnFalseWhenAdditionalPropertiesAreNull() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);

            // When & Then
            assertThat(config.hasAdditionalProperties()).isFalse();
        }

        @Test
        @DisplayName("hasAdditionalProperties should return false when additional properties are empty")
        void hasAdditionalPropertiesShouldReturnFalseWhenAdditionalPropertiesAreEmpty() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, Map.of());

            // When & Then
            assertThat(config.hasAdditionalProperties()).isFalse();
        }

        @Test
        @DisplayName("isUsingAccelerateEndpoint should return correct value")
        void isUsingAccelerateEndpointShouldReturnCorrectValue() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.isUsingAccelerateEndpoint()).isTrue();
        }

        @Test
        @DisplayName("isUsingDualstackEndpoint should return correct value")
        void isUsingDualstackEndpointShouldReturnCorrectValue() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.isUsingDualstackEndpoint()).isTrue();
        }

        @Test
        @DisplayName("isUsingPathStyleAccess should return correct value")
        void isUsingPathStyleAccessShouldReturnCorrectValue() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.isUsingPathStyleAccess()).isTrue();
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("getConnectionTimeoutMillis should return correct milliseconds")
        void getConnectionTimeoutMillisShouldReturnCorrectMilliseconds() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When
            long timeoutMillis = config.getConnectionTimeoutMillis();

            // Then
            assertThat(timeoutMillis).isEqualTo(15000);
        }

        @Test
        @DisplayName("getConnectionTimeoutMillis should return default when timeout is null")
        void getConnectionTimeoutMillisShouldReturnDefaultWhenTimeoutIsNull() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);

            // When
            long timeoutMillis = config.getConnectionTimeoutMillis();

            // Then
            assertThat(timeoutMillis).isEqualTo(10000);
        }

        @Test
        @DisplayName("getSocketTimeoutMillis should return correct milliseconds")
        void getSocketTimeoutMillisShouldReturnCorrectMilliseconds() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When
            long timeoutMillis = config.getSocketTimeoutMillis();

            // Then
            assertThat(timeoutMillis).isEqualTo(45000);
        }

        @Test
        @DisplayName("getSocketTimeoutMillis should return default when timeout is null")
        void getSocketTimeoutMillisShouldReturnDefaultWhenTimeoutIsNull() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);

            // When
            long timeoutMillis = config.getSocketTimeoutMillis();

            // Then
            assertThat(timeoutMillis).isEqualTo(30000);
        }

        @Test
        @DisplayName("hasRetriesEnabled should return true when maxRetries > 0")
        void hasRetriesEnabledShouldReturnTrueWhenMaxRetriesGreaterThanZero() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.hasRetriesEnabled()).isTrue();
        }

        @Test
        @DisplayName("hasRetriesEnabled should return false when maxRetries is 0")
        void hasRetriesEnabledShouldReturnFalseWhenMaxRetriesIsZero() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, 0, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config.hasRetriesEnabled()).isFalse();
        }

        @Test
        @DisplayName("getRetryModeEnum should return correct enum value")
        void getRetryModeEnumShouldReturnCorrectEnumValue() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, "ADAPTIVE", ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When
            AwsClientConfig.RetryMode retryMode = config.getRetryModeEnum();

            // Then
            assertThat(retryMode).isEqualTo(AwsClientConfig.RetryMode.ADAPTIVE);
        }

        @Test
        @DisplayName("getRetryModeEnum should return STANDARD for null retry mode")
        void getRetryModeEnumShouldReturnStandardForNullRetryMode() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);

            // When
            AwsClientConfig.RetryMode retryMode = config.getRetryModeEnum();

            // Then
            assertThat(retryMode).isEqualTo(AwsClientConfig.RetryMode.STANDARD);
        }

        @Test
        @DisplayName("getRetryModeEnum should return STANDARD for invalid retry mode")
        void getRetryModeEnumShouldReturnStandardForInvalidRetryMode() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, "INVALID", ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When
            AwsClientConfig.RetryMode retryMode = config.getRetryModeEnum();

            // Then
            assertThat(retryMode).isEqualTo(AwsClientConfig.RetryMode.STANDARD);
        }

        @Test
        @DisplayName("getRetryModeEnum should handle case insensitive retry mode")
        void getRetryModeEnumShouldHandleCaseInsensitiveRetryMode() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, "adaptive", ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When
            AwsClientConfig.RetryMode retryMode = config.getRetryModeEnum();

            // Then
            assertThat(retryMode).isEqualTo(AwsClientConfig.RetryMode.ADAPTIVE);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to identical AwsClientConfig")
        void shouldBeEqualToIdenticalAwsClientConfig() {
            // Given
            AwsClientConfig config1 = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);
            AwsClientConfig config2 = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config1).isEqualTo(config2);
            assertThat(config1.hashCode()).hasSameHashCodeAs(config2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to different AwsClientConfig")
        void shouldNotBeEqualToDifferentAwsClientConfig() {
            // Given
            AwsClientConfig config1 = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);
            AwsClientConfig config2 = AwsClientConfig.of("us-west-2", CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When & Then
            assertThat(config1).isNotEqualTo(config2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);

            // When & Then
            assertThat(config).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS);
            String differentType = "not-config";

            // When & Then
            assertThat(config).isNotEqualTo(differentType);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString should contain all fields")
        void toStringShouldContainAllFields() {
            // Given
            AwsClientConfig config = AwsClientConfig.of(REGION, CREDENTIALS, CONNECTION_TIMEOUT, SOCKET_TIMEOUT,
                MAX_CONNECTIONS, MAX_RETRIES, RETRY_MODE, ENDPOINT_OVERRIDE, true, true, true, ADDITIONAL_PROPERTIES);

            // When
            String string = config.toString();

            // Then
            assertThat(string).contains("AwsClientConfig{")
            .contains("region='" + REGION + "'")
            .contains("credentials=" + CREDENTIALS)
            .contains("connectionTimeout=" + CONNECTION_TIMEOUT)
            .contains("socketTimeout=" + SOCKET_TIMEOUT)
            .contains("maxConnections=" + MAX_CONNECTIONS)
            .contains("maxRetries=" + MAX_RETRIES)
            .contains("retryMode='" + RETRY_MODE + "'")
            .contains("endpointOverride='" + ENDPOINT_OVERRIDE + "'")
            .contains("useAccelerateEndpoint=true")
            .contains("useDualstackEndpoint=true")
            .contains("usePathStyleAccess=true")
            .contains("additionalProperties=" + ADDITIONAL_PROPERTIES);
        }

        @Test
        @DisplayName("toString should handle null values")
        void toStringShouldHandleNullValues() {
            // Given
            AwsClientConfig config = new AwsClientConfig(
                null, null, null, null, 0, 0, null, null, false, false, false, null
            );

            // When
            String string = config.toString();

            // Then
            assertThat(string).contains("region='null'").contains("credentials=null")
            .contains("connectionTimeout=null")
            .contains("socketTimeout=null")
            .contains("retryMode='null'")
            .contains("endpointOverride='null'")
            .contains("additionalProperties=null");
        }
    }
}
