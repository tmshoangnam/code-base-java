package io.github.base.aws.api.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link AwsException}.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("AwsException Tests")
class AwsExceptionTest {

    private static final String MESSAGE = "AWS service error occurred";
    private static final String ERROR_CODE = "ServiceUnavailable";
    private static final String REQUEST_ID = "12345678-1234-1234-1234-123456789012";
    private static final String SERVICE_NAME = "S3";
    private static final int STATUS_CODE = 503;
    private static final Throwable CAUSE = new RuntimeException("Underlying cause");

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create AwsException with message only")
        void shouldCreateAwsExceptionWithMessageOnly() {
            // When
            AwsException exception = new AwsException(MESSAGE);

            // Then
            assertThat(exception.getMessage()).isEqualTo(MESSAGE);
            assertThat(exception.getCause()).isNull();
            assertThat(exception.getErrorCode()).isNull();
            assertThat(exception.getRequestId()).isNull();
            assertThat(exception.getServiceName()).isNull();
            assertThat(exception.getStatusCode()).isZero();
        }

        @Test
        @DisplayName("Should create AwsException with message and cause")
        void shouldCreateAwsExceptionWithMessageAndCause() {
            // When
            AwsException exception = new AwsException(MESSAGE, CAUSE);

            // Then
            assertThat(exception.getMessage()).isEqualTo(MESSAGE);
            assertThat(exception.getCause()).isEqualTo(CAUSE);
            assertThat(exception.getErrorCode()).isNull();
            assertThat(exception.getRequestId()).isNull();
            assertThat(exception.getServiceName()).isNull();
            assertThat(exception.getStatusCode()).isZero();
        }

        @Test
        @DisplayName("Should create AwsException with message, error code, request ID, and service name")
        void shouldCreateAwsExceptionWithMessageErrorCodeRequestIdAndServiceName() {
            // When
            AwsException exception = new AwsException(MESSAGE, ERROR_CODE, REQUEST_ID, SERVICE_NAME);

            // Then
            assertThat(exception.getMessage()).isEqualTo(MESSAGE);
            assertThat(exception.getCause()).isNull();
            assertThat(exception.getErrorCode()).isEqualTo(ERROR_CODE);
            assertThat(exception.getRequestId()).isEqualTo(REQUEST_ID);
            assertThat(exception.getServiceName()).isEqualTo(SERVICE_NAME);
            assertThat(exception.getStatusCode()).isZero();
        }

        @Test
        @DisplayName("Should create AwsException with message, cause, error code, request ID, and service name")
        void shouldCreateAwsExceptionWithMessageCauseErrorCodeRequestIdAndServiceName() {
            // When
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME);

            // Then
            assertThat(exception.getMessage()).isEqualTo(MESSAGE);
            assertThat(exception.getCause()).isEqualTo(CAUSE);
            assertThat(exception.getErrorCode()).isEqualTo(ERROR_CODE);
            assertThat(exception.getRequestId()).isEqualTo(REQUEST_ID);
            assertThat(exception.getServiceName()).isEqualTo(SERVICE_NAME);
            assertThat(exception.getStatusCode()).isZero();
        }

        @Test
        @DisplayName("Should create AwsException with all information")
        void shouldCreateAwsExceptionWithAllInformation() {
            // When
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, STATUS_CODE);

            // Then
            assertThat(exception.getMessage()).isEqualTo(MESSAGE);
            assertThat(exception.getCause()).isEqualTo(CAUSE);
            assertThat(exception.getErrorCode()).isEqualTo(ERROR_CODE);
            assertThat(exception.getRequestId()).isEqualTo(REQUEST_ID);
            assertThat(exception.getServiceName()).isEqualTo(SERVICE_NAME);
            assertThat(exception.getStatusCode()).isEqualTo(STATUS_CODE);
        }

        @Test
        @DisplayName("Should create AwsException with null values")
        void shouldCreateAwsExceptionWithNullValues() {
            // When
            AwsException exception = new AwsException(null, null, null, null, null, 0);

            // Then
            assertThat(exception.getMessage()).isNull();
            assertThat(exception.getCause()).isNull();
            assertThat(exception.getErrorCode()).isNull();
            assertThat(exception.getRequestId()).isNull();
            assertThat(exception.getServiceName()).isNull();
            assertThat(exception.getStatusCode()).isZero();
        }
    }

    @Nested
    @DisplayName("Status Check Tests")
    class StatusCheckTests {

        @Test
        @DisplayName("hasErrorCode should return true when error code is present")
        void hasErrorCodeShouldReturnTrueWhenErrorCodeIsPresent() {
            // Given
            AwsException exception = new AwsException(MESSAGE, ERROR_CODE, REQUEST_ID, SERVICE_NAME);

            // When & Then
            assertThat(exception.hasErrorCode()).isTrue();
        }

        @Test
        @DisplayName("hasErrorCode should return false when error code is null")
        void hasErrorCodeShouldReturnFalseWhenErrorCodeIsNull() {
            // Given
            AwsException exception = new AwsException(MESSAGE);

            // When & Then
            assertThat(exception.hasErrorCode()).isFalse();
        }

        @Test
        @DisplayName("hasErrorCode should return false when error code is empty")
        void hasErrorCodeShouldReturnFalseWhenErrorCodeIsEmpty() {
            // Given
            AwsException exception = new AwsException(MESSAGE, "", REQUEST_ID, SERVICE_NAME);

            // When & Then
            assertThat(exception.hasErrorCode()).isFalse();
        }

        @Test
        @DisplayName("hasErrorCode should return false when error code is blank")
        void hasErrorCodeShouldReturnFalseWhenErrorCodeIsBlank() {
            // Given
            AwsException exception = new AwsException(MESSAGE, "   ", REQUEST_ID, SERVICE_NAME);

            // When & Then
            assertThat(exception.hasErrorCode()).isFalse();
        }

        @Test
        @DisplayName("hasRequestId should return true when request ID is present")
        void hasRequestIdShouldReturnTrueWhenRequestIdIsPresent() {
            // Given
            AwsException exception = new AwsException(MESSAGE, ERROR_CODE, REQUEST_ID, SERVICE_NAME);

            // When & Then
            assertThat(exception.hasRequestId()).isTrue();
        }

        @Test
        @DisplayName("hasRequestId should return false when request ID is null")
        void hasRequestIdShouldReturnFalseWhenRequestIdIsNull() {
            // Given
            AwsException exception = new AwsException(MESSAGE);

            // When & Then
            assertThat(exception.hasRequestId()).isFalse();
        }

        @Test
        @DisplayName("hasRequestId should return false when request ID is empty")
        void hasRequestIdShouldReturnFalseWhenRequestIdIsEmpty() {
            // Given
            AwsException exception = new AwsException(MESSAGE, ERROR_CODE, "", SERVICE_NAME);

            // When & Then
            assertThat(exception.hasRequestId()).isFalse();
        }

        @Test
        @DisplayName("hasRequestId should return false when request ID is blank")
        void hasRequestIdShouldReturnFalseWhenRequestIdIsBlank() {
            // Given
            AwsException exception = new AwsException(MESSAGE, ERROR_CODE, "   ", SERVICE_NAME);

            // When & Then
            assertThat(exception.hasRequestId()).isFalse();
        }

        @Test
        @DisplayName("hasServiceName should return true when service name is present")
        void hasServiceNameShouldReturnTrueWhenServiceNameIsPresent() {
            // Given
            AwsException exception = new AwsException(MESSAGE, ERROR_CODE, REQUEST_ID, SERVICE_NAME);

            // When & Then
            assertThat(exception.hasServiceName()).isTrue();
        }

        @Test
        @DisplayName("hasServiceName should return false when service name is null")
        void hasServiceNameShouldReturnFalseWhenServiceNameIsNull() {
            // Given
            AwsException exception = new AwsException(MESSAGE);

            // When & Then
            assertThat(exception.hasServiceName()).isFalse();
        }

        @Test
        @DisplayName("hasServiceName should return false when service name is empty")
        void hasServiceNameShouldReturnFalseWhenServiceNameIsEmpty() {
            // Given
            AwsException exception = new AwsException(MESSAGE, ERROR_CODE, REQUEST_ID, "");

            // When & Then
            assertThat(exception.hasServiceName()).isFalse();
        }

        @Test
        @DisplayName("hasServiceName should return false when service name is blank")
        void hasServiceNameShouldReturnFalseWhenServiceNameIsBlank() {
            // Given
            AwsException exception = new AwsException(MESSAGE, ERROR_CODE, REQUEST_ID, "   ");

            // When & Then
            assertThat(exception.hasServiceName()).isFalse();
        }

        @Test
        @DisplayName("hasStatusCode should return true when status code is positive")
        void hasStatusCodeShouldReturnTrueWhenStatusCodeIsPositive() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, STATUS_CODE);

            // When & Then
            assertThat(exception.hasStatusCode()).isTrue();
        }

        @Test
        @DisplayName("hasStatusCode should return false when status code is zero")
        void hasStatusCodeShouldReturnFalseWhenStatusCodeIsZero() {
            // Given
            AwsException exception = new AwsException(MESSAGE);

            // When & Then
            assertThat(exception.hasStatusCode()).isFalse();
        }

        @Test
        @DisplayName("hasStatusCode should return false when status code is negative")
        void hasStatusCodeShouldReturnFalseWhenStatusCodeIsNegative() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, -1);

            // When & Then
            assertThat(exception.hasStatusCode()).isFalse();
        }
    }

    @Nested
    @DisplayName("Error Type Tests")
    class ErrorTypeTests {

        @Test
        @DisplayName("isClientError should return true for 4xx status codes")
        void isClientErrorShouldReturnTrueFor4xxStatusCodes() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 400);

            // When & Then
            assertThat(exception.isClientError()).isTrue();
        }

        @Test
        @DisplayName("isClientError should return true for 404 status code")
        void isClientErrorShouldReturnTrueFor404StatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 404);

            // When & Then
            assertThat(exception.isClientError()).isTrue();
        }

        @Test
        @DisplayName("isClientError should return true for 499 status code")
        void isClientErrorShouldReturnTrueFor499StatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 499);

            // When & Then
            assertThat(exception.isClientError()).isTrue();
        }

        @Test
        @DisplayName("isClientError should return false for 3xx status codes")
        void isClientErrorShouldReturnFalseFor3xxStatusCodes() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 300);

            // When & Then
            assertThat(exception.isClientError()).isFalse();
        }

        @Test
        @DisplayName("isClientError should return false for 5xx status codes")
        void isClientErrorShouldReturnFalseFor5xxStatusCodes() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 500);

            // When & Then
            assertThat(exception.isClientError()).isFalse();
        }

        @Test
        @DisplayName("isClientError should return false for zero status code")
        void isClientErrorShouldReturnFalseForZeroStatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE);

            // When & Then
            assertThat(exception.isClientError()).isFalse();
        }

        @Test
        @DisplayName("isServerError should return true for 5xx status codes")
        void isServerErrorShouldReturnTrueFor5xxStatusCodes() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 500);

            // When & Then
            assertThat(exception.isServerError()).isTrue();
        }

        @Test
        @DisplayName("isServerError should return true for 503 status code")
        void isServerErrorShouldReturnTrueFor503StatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 503);

            // When & Then
            assertThat(exception.isServerError()).isTrue();
        }

        @Test
        @DisplayName("isServerError should return true for 599 status code")
        void isServerErrorShouldReturnTrueFor599StatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 599);

            // When & Then
            assertThat(exception.isServerError()).isTrue();
        }

        @Test
        @DisplayName("isServerError should return false for 4xx status codes")
        void isServerErrorShouldReturnFalseFor4xxStatusCodes() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 400);

            // When & Then
            assertThat(exception.isServerError()).isFalse();
        }

        @Test
        @DisplayName("isServerError should return false for 3xx status codes")
        void isServerErrorShouldReturnFalseFor3xxStatusCodes() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 300);

            // When & Then
            assertThat(exception.isServerError()).isFalse();
        }

        @Test
        @DisplayName("isServerError should return false for zero status code")
        void isServerErrorShouldReturnFalseForZeroStatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE);

            // When & Then
            assertThat(exception.isServerError()).isFalse();
        }
    }

    @Nested
    @DisplayName("Retryable Tests")
    class RetryableTests {

        @Test
        @DisplayName("isRetryable should return true for 5xx status codes")
        void isRetryableShouldReturnTrueFor5xxStatusCodes() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 500);
            AwsException exception503 = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 503);

            // When & Then
            assertThat(exception.isRetryable()).isTrue();
            assertThat(exception503.isRetryable()).isTrue();
        }

        @Test
        @DisplayName("isRetryable should return true for 503 status code")
        void isRetryableShouldReturnTrueFor503StatusCode() {

        }

        @Test
        @DisplayName("isRetryable should return true for 408 status code")
        void isRetryableShouldReturnTrueFor408StatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 408);

            // When & Then
            assertThat(exception.isRetryable()).isTrue();
        }

        @Test
        @DisplayName("isRetryable should return true for 429 status code")
        void isRetryableShouldReturnTrueFor429StatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 429);

            // When & Then
            assertThat(exception.isRetryable()).isTrue();
        }

        @Test
        @DisplayName("isRetryable should return true for 502 status code")
        void isRetryableShouldReturnTrueFor502StatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 502);

            // When & Then
            assertThat(exception.isRetryable()).isTrue();
        }

        @Test
        @DisplayName("isRetryable should return true for 504 status code")
        void isRetryableShouldReturnTrueFor504StatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 504);

            // When & Then
            assertThat(exception.isRetryable()).isTrue();
        }

        @Test
        @DisplayName("isRetryable should return false for 4xx status codes except retryable ones")
        void isRetryableShouldReturnFalseFor4xxStatusCodesExceptRetryableOnes() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 400);

            // When & Then
            assertThat(exception.isRetryable()).isFalse();
        }

        @Test
        @DisplayName("isRetryable should return false for 404 status code")
        void isRetryableShouldReturnFalseFor404StatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, 404);

            // When & Then
            assertThat(exception.isRetryable()).isFalse();
        }

        @Test
        @DisplayName("isRetryable should return false for zero status code")
        void isRetryableShouldReturnFalseForZeroStatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE);

            // When & Then
            assertThat(exception.isRetryable()).isFalse();
        }

        @Test
        @DisplayName("isRetryable should return false for negative status code")
        void isRetryableShouldReturnFalseForNegativeStatusCode() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, -1);

            // When & Then
            assertThat(exception.isRetryable()).isFalse();
        }
    }

    @Nested
    @DisplayName("Formatted Message Tests")
    class FormattedMessageTests {

        @Test
        @DisplayName("getFormattedMessage should return message only when no additional info")
        void getFormattedMessageShouldReturnMessageOnlyWhenNoAdditionalInfo() {
            // Given
            AwsException exception = new AwsException(MESSAGE);

            // When
            String formatted = exception.getFormattedMessage();

            // Then
            assertThat(formatted).isEqualTo(MESSAGE);
        }

        @Test
        @DisplayName("getFormattedMessage should include service name when present")
        void getFormattedMessageShouldIncludeServiceNameWhenPresent() {
            // Given
            AwsException exception = new AwsException(MESSAGE, ERROR_CODE, REQUEST_ID, SERVICE_NAME);

            // When
            String formatted = exception.getFormattedMessage();

            // Then
            assertThat(formatted).isEqualTo(MESSAGE + " [Service: " + SERVICE_NAME + "] [Error Code: " + ERROR_CODE + "] [Request ID: " + REQUEST_ID + "]");
        }

        @Test
        @DisplayName("getFormattedMessage should include status code when present")
        void getFormattedMessageShouldIncludeStatusCodeWhenPresent() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, STATUS_CODE);

            // When
            String formatted = exception.getFormattedMessage();

            // Then
            assertThat(formatted).isEqualTo(MESSAGE + " [Service: " + SERVICE_NAME + "] [Error Code: " + ERROR_CODE + "] [Request ID: " + REQUEST_ID + "] [Status Code: " + STATUS_CODE + "]");
        }

        @Test
        @DisplayName("getFormattedMessage should handle null message")
        void getFormattedMessageShouldHandleNullMessage() {
            // Given
            AwsException exception = new AwsException(null, ERROR_CODE, REQUEST_ID, SERVICE_NAME);

            // When
            String formatted = exception.getFormattedMessage();

            // Then
            assertThat(formatted).isEqualTo("null [Service: " + SERVICE_NAME + "] [Error Code: " + ERROR_CODE + "] [Request ID: " + REQUEST_ID + "]");
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to identical AwsException")
        void shouldBeEqualToIdenticalAwsException() {
            // Given
            AwsException exception1 = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, STATUS_CODE);
            AwsException exception2 = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, STATUS_CODE);

            // When & Then
            assertThat(exception1).isEqualTo(exception2);
            assertThat(exception1.hashCode()).hasSameHashCodeAs(exception2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to different AwsException")
        void shouldNotBeEqualToDifferentAwsException() {
            // Given
            AwsException exception1 = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, STATUS_CODE);
            AwsException exception2 = new AwsException("Different message", CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, STATUS_CODE);

            // When & Then
            assertThat(exception1).isNotEqualTo(exception2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            AwsException exception = new AwsException(MESSAGE);

            // When & Then
            assertThat(exception).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            AwsException exception = new AwsException(MESSAGE);
            String differentType = "not-exception";

            // When & Then
            assertThat(exception).isNotEqualTo(differentType);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString should contain all fields")
        void toStringShouldContainAllFields() {
            // Given
            AwsException exception = new AwsException(MESSAGE, CAUSE, ERROR_CODE, REQUEST_ID, SERVICE_NAME, STATUS_CODE);

            // When
            String string = exception.toString();

            // Then
            assertThat(string).contains("AwsException{")
            .contains("message='" + MESSAGE + "'")
            .contains("errorCode='" + ERROR_CODE + "'")
            .contains("requestId='" + REQUEST_ID + "'")
            .contains("serviceName='" + SERVICE_NAME + "'")
            .contains("statusCode=" + STATUS_CODE)
            .contains("cause=" + CAUSE);
        }

        @Test
        @DisplayName("toString should handle null values")
        void toStringShouldHandleNullValues() {
            // Given
            AwsException exception = new AwsException(null, null, null, null, null, 0);

            // When
            String string = exception.toString();

            // Then
            assertThat(string).contains("message='null'")
            .contains("errorCode='null'")
            .contains("requestId='null'")
            .contains("serviceName='null'")
            .contains("statusCode=0")
            .contains("cause=null");
        }
    }
}
