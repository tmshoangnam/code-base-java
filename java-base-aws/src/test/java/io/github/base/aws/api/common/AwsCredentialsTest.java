package io.github.base.aws.api.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link AwsCredentials}.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("AwsCredentials Tests")
class AwsCredentialsTest {

    private static final String ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE";
    private static final String SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    private static final String SESSION_TOKEN = "AQoEXAMPLEH4aoAH0gNCAPyJxz4BlCFFxWNE1OPTgk5TthT+FvwqnKwRcOIfrRh3c/LTo6UDdyJjwOO3EAG8zJpx0Y0/kVDWw==";
    private static final String REGION = "us-east-1";

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create AwsCredentials with all parameters")
        void shouldCreateAwsCredentialsWithAllParameters() {
            // When
            AwsCredentials credentials = new AwsCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);

            // Then
            assertThat(credentials.accessKeyId()).isEqualTo(ACCESS_KEY_ID);
            assertThat(credentials.secretAccessKey()).isEqualTo(SECRET_ACCESS_KEY);
            assertThat(credentials.sessionToken()).isEqualTo(SESSION_TOKEN);
            assertThat(credentials.region()).isEqualTo(REGION);
        }

        @Test
        @DisplayName("Should create AwsCredentials with null values")
        void shouldCreateAwsCredentialsWithNullValues() {
            // When
            AwsCredentials credentials = new AwsCredentials(null, null, null, null);

            // Then
            assertThat(credentials.accessKeyId()).isNull();
            assertThat(credentials.secretAccessKey()).isNull();
            assertThat(credentials.sessionToken()).isNull();
            assertThat(credentials.region()).isNull();
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("of method with accessKeyId and secretAccessKey should create credentials")
        void ofMethodWithAccessKeyIdAndSecretAccessKeyShouldCreateCredentials() {
            // When
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // Then
            assertThat(credentials.accessKeyId()).isEqualTo(ACCESS_KEY_ID);
            assertThat(credentials.secretAccessKey()).isEqualTo(SECRET_ACCESS_KEY);
            assertThat(credentials.sessionToken()).isNull();
            assertThat(credentials.region()).isNull();
        }

        @Test
        @DisplayName("of method with accessKeyId, secretAccessKey and region should create credentials")
        void ofMethodWithAccessKeyIdSecretAccessKeyAndRegionShouldCreateCredentials() {
            // When
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, REGION);

            // Then
            assertThat(credentials.accessKeyId()).isEqualTo(ACCESS_KEY_ID);
            assertThat(credentials.secretAccessKey()).isEqualTo(SECRET_ACCESS_KEY);
            assertThat(credentials.sessionToken()).isNull();
            assertThat(credentials.region()).isEqualTo(REGION);
        }

        @Test
        @DisplayName("of method with all parameters should create credentials")
        void ofMethodWithAllParametersShouldCreateCredentials() {
            // When
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);

            // Then
            assertThat(credentials.accessKeyId()).isEqualTo(ACCESS_KEY_ID);
            assertThat(credentials.secretAccessKey()).isEqualTo(SECRET_ACCESS_KEY);
            assertThat(credentials.sessionToken()).isEqualTo(SESSION_TOKEN);
            assertThat(credentials.region()).isEqualTo(REGION);
        }

        @Test
        @DisplayName("of method should trim whitespace from accessKeyId and secretAccessKey")
        void ofMethodShouldTrimWhitespaceFromAccessKeyIdAndSecretAccessKey() {
            // Given
            String accessKeyIdWithWhitespace = "  " + ACCESS_KEY_ID + "  ";
            String secretAccessKeyWithWhitespace = "  " + SECRET_ACCESS_KEY + "  ";

            // When
            AwsCredentials credentials = AwsCredentials.of(accessKeyIdWithWhitespace, secretAccessKeyWithWhitespace);

            // Then
            assertThat(credentials.accessKeyId()).isEqualTo(ACCESS_KEY_ID);
            assertThat(credentials.secretAccessKey()).isEqualTo(SECRET_ACCESS_KEY);
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for null accessKeyId")
        void ofMethodShouldThrowIllegalArgumentExceptionForNullAccessKeyId() {
            // When & Then
            assertThatThrownBy(() -> AwsCredentials.of(null, SECRET_ACCESS_KEY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Access key ID cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for empty accessKeyId")
        void ofMethodShouldThrowIllegalArgumentExceptionForEmptyAccessKeyId() {
            // When & Then
            assertThatThrownBy(() -> AwsCredentials.of("", SECRET_ACCESS_KEY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Access key ID cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for blank accessKeyId")
        void ofMethodShouldThrowIllegalArgumentExceptionForBlankAccessKeyId() {
            // When & Then
            assertThatThrownBy(() -> AwsCredentials.of("   ", SECRET_ACCESS_KEY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Access key ID cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for null secretAccessKey")
        void ofMethodShouldThrowIllegalArgumentExceptionForNullSecretAccessKey() {
            // When & Then
            assertThatThrownBy(() -> AwsCredentials.of(ACCESS_KEY_ID, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Secret access key cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for empty secretAccessKey")
        void ofMethodShouldThrowIllegalArgumentExceptionForEmptySecretAccessKey() {
            // When & Then
            assertThatThrownBy(() -> AwsCredentials.of(ACCESS_KEY_ID, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Secret access key cannot be null or empty");
        }

        @Test
        @DisplayName("of method should throw IllegalArgumentException for blank secretAccessKey")
        void ofMethodShouldThrowIllegalArgumentExceptionForBlankSecretAccessKey() {
            // When & Then
            assertThatThrownBy(() -> AwsCredentials.of(ACCESS_KEY_ID, "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Secret access key cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Builder Method Tests")
    class BuilderMethodTests {

        @Test
        @DisplayName("withRegion should create new credentials with updated region")
        void withRegionShouldCreateNewCredentialsWithUpdatedRegion() {
            // Given
            AwsCredentials original = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, "us-west-2");
            String newRegion = "eu-west-1";

            // When
            AwsCredentials updated = original.withRegion(newRegion);

            // Then
            assertThat(updated.accessKeyId()).isEqualTo(original.accessKeyId());
            assertThat(updated.secretAccessKey()).isEqualTo(original.secretAccessKey());
            assertThat(updated.sessionToken()).isEqualTo(original.sessionToken());
            assertThat(updated.region()).isEqualTo(newRegion);
            assertThat(original.region()).isEqualTo("us-west-2"); // Original should be unchanged
        }

        @Test
        @DisplayName("withSessionToken should create new credentials with updated session token")
        void withSessionTokenShouldCreateNewCredentialsWithUpdatedSessionToken() {
            // Given
            AwsCredentials original = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
            String newSessionToken = "new-session-token";

            // When
            AwsCredentials updated = original.withSessionToken(newSessionToken);

            // Then
            assertThat(updated.accessKeyId()).isEqualTo(original.accessKeyId());
            assertThat(updated.secretAccessKey()).isEqualTo(original.secretAccessKey());
            assertThat(updated.sessionToken()).isEqualTo(newSessionToken);
            assertThat(updated.region()).isEqualTo(original.region());
            assertThat(original.sessionToken()).isNull(); // Original should be unchanged
        }
    }

    @Nested
    @DisplayName("Status Check Tests")
    class StatusCheckTests {

        @Test
        @DisplayName("hasSessionToken should return true when session token is present")
        void hasSessionTokenShouldReturnTrueWhenSessionTokenIsPresent() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);

            // When & Then
            assertThat(credentials.hasSessionToken()).isTrue();
        }

        @Test
        @DisplayName("hasSessionToken should return false when session token is null")
        void hasSessionTokenShouldReturnFalseWhenSessionTokenIsNull() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // When & Then
            assertThat(credentials.hasSessionToken()).isFalse();
        }

        @Test
        @DisplayName("hasSessionToken should return false when session token is empty")
        void hasSessionTokenShouldReturnFalseWhenSessionTokenIsEmpty() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, "", REGION);

            // When & Then
            assertThat(credentials.hasSessionToken()).isFalse();
        }

        @Test
        @DisplayName("hasSessionToken should return false when session token is blank")
        void hasSessionTokenShouldReturnFalseWhenSessionTokenIsBlank() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, "   ", REGION);

            // When & Then
            assertThat(credentials.hasSessionToken()).isFalse();
        }

        @Test
        @DisplayName("hasRegion should return true when region is present")
        void hasRegionShouldReturnTrueWhenRegionIsPresent() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, REGION);

            // When & Then
            assertThat(credentials.hasRegion()).isTrue();
        }

        @Test
        @DisplayName("hasRegion should return false when region is null")
        void hasRegionShouldReturnFalseWhenRegionIsNull() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // When & Then
            assertThat(credentials.hasRegion()).isFalse();
        }

        @Test
        @DisplayName("hasRegion should return false when region is empty")
        void hasRegionShouldReturnFalseWhenRegionIsEmpty() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, "");

            // When & Then
            assertThat(credentials.hasRegion()).isFalse();
        }

        @Test
        @DisplayName("hasRegion should return false when region is blank")
        void hasRegionShouldReturnFalseWhenRegionIsBlank() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, "   ");

            // When & Then
            assertThat(credentials.hasRegion()).isFalse();
        }

        @Test
        @DisplayName("isTemporary should return true when session token is present")
        void isTemporaryShouldReturnTrueWhenSessionTokenIsPresent() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);

            // When & Then
            assertThat(credentials.isTemporary()).isTrue();
        }

        @Test
        @DisplayName("isTemporary should return false when session token is not present")
        void isTemporaryShouldReturnFalseWhenSessionTokenIsNotPresent() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // When & Then
            assertThat(credentials.isTemporary()).isFalse();
        }

        @Test
        @DisplayName("isPermanent should return true when session token is not present")
        void isPermanentShouldReturnTrueWhenSessionTokenIsNotPresent() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // When & Then
            assertThat(credentials.isPermanent()).isTrue();
        }

        @Test
        @DisplayName("isPermanent should return false when session token is present")
        void isPermanentShouldReturnFalseWhenSessionTokenIsPresent() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);

            // When & Then
            assertThat(credentials.isPermanent()).isFalse();
        }
    }

    @Nested
    @DisplayName("Masking Tests")
    class MaskingTests {

        @Test
        @DisplayName("getMaskedAccessKeyId should mask access key ID correctly")
        void getMaskedAccessKeyIdShouldMaskAccessKeyIdCorrectly() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // When
            String masked = credentials.getMaskedAccessKeyId();

            // Then
            assertThat(masked).isEqualTo("AKIA****MPLE")
            .doesNotContain(ACCESS_KEY_ID);
        }

        @Test
        @DisplayName("getMaskedAccessKeyId should return **** for short access key ID")
        void getMaskedAccessKeyIdShouldReturnStarsForShortAccessKeyId() {
            // Given
            AwsCredentials credentials = AwsCredentials.of("short", SECRET_ACCESS_KEY);

            // When
            String masked = credentials.getMaskedAccessKeyId();

            // Then
            assertThat(masked).isEqualTo("****");
        }

        @Test
        @DisplayName("getMaskedAccessKeyId should return **** for null access key ID")
        void getMaskedAccessKeyIdShouldReturnStarsForNullAccessKeyId() {
            // Given
            AwsCredentials credentials = new AwsCredentials(null, SECRET_ACCESS_KEY, null, null);

            // When
            String masked = credentials.getMaskedAccessKeyId();

            // Then
            assertThat(masked).isEqualTo("****");
        }

        @Test
        @DisplayName("getMaskedSecretAccessKey should mask secret access key correctly")
        void getMaskedSecretAccessKeyShouldMaskSecretAccessKeyCorrectly() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // When
            String masked = credentials.getMaskedSecretAccessKey();

            // Then
            assertThat(masked).isEqualTo("wJal****EKEY")
            .doesNotContain(SECRET_ACCESS_KEY);
        }

        @Test
        @DisplayName("getMaskedSecretAccessKey should return **** for short secret access key")
        void getMaskedSecretAccessKeyShouldReturnStarsForShortSecretAccessKey() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, "short");

            // When
            String masked = credentials.getMaskedSecretAccessKey();

            // Then
            assertThat(masked).isEqualTo("****");
        }

        @Test
        @DisplayName("getMaskedSecretAccessKey should return **** for null secret access key")
        void getMaskedSecretAccessKeyShouldReturnStarsForNullSecretAccessKey() {
            // Given
            AwsCredentials credentials = new AwsCredentials(ACCESS_KEY_ID, null, null, null);

            // When
            String masked = credentials.getMaskedSecretAccessKey();

            // Then
            assertThat(masked).isEqualTo("****");
        }

        @Test
        @DisplayName("getMaskedSessionToken should mask session token correctly")
        void getMaskedSessionTokenShouldMaskSessionTokenCorrectly() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);

            // When
            String masked = credentials.getMaskedSessionToken();

            // Then
            assertThat(masked).isEqualTo("AQoE****Ww==")
                    .doesNotContain(SESSION_TOKEN);
        }

        @Test
        @DisplayName("getMaskedSessionToken should return **** for short session token")
        void getMaskedSessionTokenShouldReturnStarsForShortSessionToken() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, "short", REGION);

            // When
            String masked = credentials.getMaskedSessionToken();

            // Then
            assertThat(masked).isEqualTo("****");
        }

        @Test
        @DisplayName("getMaskedSessionToken should return **** for null session token")
        void getMaskedSessionTokenShouldReturnStarsForNullSessionToken() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // When
            String masked = credentials.getMaskedSessionToken();

            // Then
            assertThat(masked).isEqualTo("****");
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to identical AwsCredentials")
        void shouldBeEqualToIdenticalAwsCredentials() {
            // Given
            AwsCredentials credentials1 = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);
            AwsCredentials credentials2 = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);

            // When & Then
            assertThat(credentials1).isEqualTo(credentials2);
            assertThat(credentials1.hashCode()).hasSameHashCodeAs(credentials2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to different AwsCredentials")
        void shouldNotBeEqualToDifferentAwsCredentials() {
            // Given
            AwsCredentials credentials1 = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);
            AwsCredentials credentials2 = AwsCredentials.of("DIFFERENT", SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);

            // When & Then
            assertThat(credentials1).isNotEqualTo(credentials2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // When & Then
            assertThat(credentials).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
            String differentType = "not-credentials";

            // When & Then
            assertThat(credentials).isNotEqualTo(differentType);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString should contain masked values")
        void toStringShouldContainMaskedValues() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY, SESSION_TOKEN, REGION);

            // When
            String string = credentials.toString();

            // Then
            assertThat(string).contains("AwsCredentials{")
            .contains("accessKeyId='AKIA****MPLE'")
            .contains("secretAccessKey='wJal****EKEY'")
            .contains("sessionToken='AQoE****Ww=='")
            .contains("region='us-east-1'")
            .doesNotContain(ACCESS_KEY_ID)
            .doesNotContain(SECRET_ACCESS_KEY)
            .doesNotContain(SESSION_TOKEN);
        }

        @Test
        @DisplayName("toString should handle null session token")
        void toStringShouldHandleNullSessionToken() {
            // Given
            AwsCredentials credentials = AwsCredentials.of(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

            // When
            String string = credentials.toString();

            // Then
            assertThat(string).contains("sessionToken='null'");
        }
    }
}
