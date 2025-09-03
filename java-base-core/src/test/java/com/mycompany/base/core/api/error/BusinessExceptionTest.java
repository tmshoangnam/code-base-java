package com.mycompany.base.core.api.error;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BusinessExceptionTest {

    @Test
    void testBusinessExceptionWithErrorCodeAndMessage() {
        BusinessException exception = new BusinessException("USER_NOT_FOUND", "User does not exist");

        assertThat(exception.getErrorCode()).isEqualTo("USER_NOT_FOUND");
        assertThat(exception.getMessage()).isEqualTo("User does not exist");
        assertThat(exception.getContext()).isEmpty();
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testBusinessExceptionWithContext() {
        Map<String, Object> context = Map.of("userId", "123", "attempts", 3);
        BusinessException exception = new BusinessException("INSUFFICIENT_BALANCE",
                "Account balance is insufficient", context);

        assertThat(exception.getErrorCode()).isEqualTo("INSUFFICIENT_BALANCE");
        assertThat(exception.getMessage()).isEqualTo("Account balance is insufficient");
        assertThat(exception.getContext()).isEqualTo(context);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testBusinessExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Database error");
        BusinessException exception = new BusinessException("DATABASE_ERROR", "Failed to save user", cause);

        assertThat(exception.getErrorCode()).isEqualTo("DATABASE_ERROR");
        assertThat(exception.getMessage()).isEqualTo("Failed to save user");
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getContext()).isEmpty();
    }

    @Test
    void testBusinessExceptionWithContextAndCause() {
        Map<String, Object> context = Map.of("userId", "123");
        RuntimeException cause = new RuntimeException("Database error");
        BusinessException exception = new BusinessException("DATABASE_ERROR", "Failed to save user", context, cause);

        assertThat(exception.getErrorCode()).isEqualTo("DATABASE_ERROR");
        assertThat(exception.getMessage()).isEqualTo("Failed to save user");
        assertThat(exception.getContext()).isEqualTo(context);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void testBusinessExceptionWithNullErrorCode() {
        assertThatThrownBy(() -> new BusinessException(null, "Message"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("errorCode cannot be null");
    }

    @Test
    void testBusinessExceptionWithEmptyErrorCode() {
        assertThatThrownBy(() -> new BusinessException("", "Message"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("errorCode cannot be empty");
    }

    @Test
    void testBusinessExceptionWithBlankErrorCode() {
        assertThatThrownBy(() -> new BusinessException("   ", "Message"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("errorCode cannot be empty");
    }

    @Test
    void testBusinessExceptionWithNullContext() {
        assertThatThrownBy(() -> new BusinessException("ERROR", "Message", (Map<String, Object>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("context cannot be null");
    }
}
