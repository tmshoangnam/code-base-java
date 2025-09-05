package io.github.base.core.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BaseExceptionTest {

    @Test
    void testBaseExceptionWithErrorCodeAndMessage() {
        BaseException exception = new BaseException("TEST_ERROR", "Test error message");

        assertThat(exception.getErrorCode()).isEqualTo("TEST_ERROR");
        assertThat(exception.getMessage()).isEqualTo("Test error message");
        assertThat(exception.getContext()).isEmpty();
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testBaseExceptionWithContext() {
        Map<String, Object> context = Map.of("key1", "value1", "key2", 123);
        BaseException exception = new BaseException("TEST_ERROR", "Test error message", context);

        assertThat(exception.getErrorCode()).isEqualTo("TEST_ERROR");
        assertThat(exception.getMessage()).isEqualTo("Test error message");
        assertThat(exception.getContext()).isEqualTo(context);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testBaseExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Root cause");
        BaseException exception = new BaseException("TEST_ERROR", "Test error message", cause);

        assertThat(exception.getErrorCode()).isEqualTo("TEST_ERROR");
        assertThat(exception.getMessage()).isEqualTo("Test error message");
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getContext()).isEmpty();
    }

    @Test
    void testBaseExceptionWithContextAndCause() {
        Map<String, Object> context = Map.of("key1", "value1");
        RuntimeException cause = new RuntimeException("Root cause");
        BaseException exception = new BaseException("TEST_ERROR", "Test error message", context, cause);

        assertThat(exception.getErrorCode()).isEqualTo("TEST_ERROR");
        assertThat(exception.getMessage()).isEqualTo("Test error message");
        assertThat(exception.getContext()).isEqualTo(context);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void testBaseExceptionWithNullErrorCode() {
        assertThatThrownBy(() -> new BaseException(null, "Message"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("errorCode cannot be null");
    }

    @Test
    void testBaseExceptionWithEmptyErrorCode() {
        assertThatThrownBy(() -> new BaseException("", "Message"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("errorCode cannot be empty");
    }

    @Test
    void testBaseExceptionWithBlankErrorCode() {
        assertThatThrownBy(() -> new BaseException("   ", "Message"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("errorCode cannot be empty");
    }

    @Test
    void testBaseExceptionWithNullContext() {
        assertThatThrownBy(() -> new BaseException("ERROR", "Message", (Map<String, Object>) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("context cannot be null");
    }
}
