package com.mycompany.base.core.api.error;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ServiceExceptionTest {

    @Test
    void testServiceExceptionWithErrorCodeAndMessage() {
        ServiceException exception = new ServiceException("DATABASE_CONNECTION_FAILED", "Unable to connect to database");

        assertThat(exception.getErrorCode()).isEqualTo("DATABASE_CONNECTION_FAILED");
        assertThat(exception.getMessage()).isEqualTo("Unable to connect to database");
        assertThat(exception.getContext()).isEmpty();
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testServiceExceptionWithContext() {
        Map<String, Object> context = Map.of("service", "payment-service", "timeout", "30s");
        ServiceException exception = new ServiceException("EXTERNAL_API_TIMEOUT",
                "Payment service did not respond", context);

        assertThat(exception.getErrorCode()).isEqualTo("EXTERNAL_API_TIMEOUT");
        assertThat(exception.getMessage()).isEqualTo("Payment service did not respond");
        assertThat(exception.getContext()).isEqualTo(context);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testServiceExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Connection timeout");
        ServiceException exception = new ServiceException("CONNECTION_TIMEOUT", "Failed to connect", cause);

        assertThat(exception.getErrorCode()).isEqualTo("CONNECTION_TIMEOUT");
        assertThat(exception.getMessage()).isEqualTo("Failed to connect");
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getContext()).isEmpty();
    }

    @Test
    void testServiceExceptionWithContextAndCause() {
        Map<String, Object> context = Map.of("service", "payment-service");
        RuntimeException cause = new RuntimeException("Connection timeout");
        ServiceException exception = new ServiceException("CONNECTION_TIMEOUT", "Failed to connect", context, cause);

        assertThat(exception.getErrorCode()).isEqualTo("CONNECTION_TIMEOUT");
        assertThat(exception.getMessage()).isEqualTo("Failed to connect");
        assertThat(exception.getContext()).isEqualTo(context);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void testServiceExceptionWithNullErrorCode() {
        assertThatThrownBy(() -> new ServiceException(null, "Message"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("errorCode cannot be null");
    }

    @Test
    void testServiceExceptionWithEmptyErrorCode() {
        assertThatThrownBy(() -> new ServiceException("", "Message"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("errorCode cannot be empty");
    }

    @Test
    void testServiceExceptionWithBlankErrorCode() {
        assertThatThrownBy(() -> new ServiceException("   ", "Message"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("errorCode cannot be empty");
    }

    @Test
    void testServiceExceptionWithNullContext() {
        assertThatThrownBy(() -> new ServiceException("ERROR", "Message", (Map<String, Object>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("context cannot be null");
    }
}
