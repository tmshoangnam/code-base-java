package io.github.base.file.api.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for FileStorageException.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("FileStorageException Tests")
class FileStorageExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message only")
        void shouldCreateExceptionWithMessageOnly() {
            // Given
            String message = "File storage operation failed";

            // When
            FileStorageException exception = new FileStorageException(message);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            // Given
            String message = "File storage operation failed";
            RuntimeException cause = new RuntimeException("Underlying error");

            // When
            FileStorageException exception = new FileStorageException(message, cause);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Should create exception with null message")
        void shouldCreateExceptionWithNullMessage() {
            // When
            FileStorageException exception = new FileStorageException((String) null);

            // Then
            assertThat(exception.getMessage()).isNull();
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should create exception with null message and cause")
        void shouldCreateExceptionWithNullMessageAndCause() {
            // Given
            RuntimeException cause = new RuntimeException("Underlying error");

            // When
            FileStorageException exception = new FileStorageException(null, cause);

            // Then
            assertThat(exception.getMessage()).isNull();
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Should create exception with null cause")
        void shouldCreateExceptionWithNullCause() {
            // Given
            String message = "File storage operation failed";

            // When
            FileStorageException exception = new FileStorageException(message, null);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isNull();
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Given
            FileStorageException exception = new FileStorageException("Test message");

            // When & Then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should be instance of Exception")
        void shouldBeInstanceOfException() {
            // Given
            FileStorageException exception = new FileStorageException("Test message");

            // When & Then
            assertThat(exception).isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should be instance of Throwable")
        void shouldBeInstanceOfThrowable() {
            // Given
            FileStorageException exception = new FileStorageException("Test message");

            // When & Then
            assertThat(exception).isInstanceOf(Throwable.class);
        }
    }

    @Nested
    @DisplayName("Message Tests")
    class MessageTests {

        @Test
        @DisplayName("Should preserve original message")
        void shouldPreserveOriginalMessage() {
            // Given
            String originalMessage = "Original error message";
            FileStorageException exception = new FileStorageException(originalMessage);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).isEqualTo(originalMessage);
        }

        @Test
        @DisplayName("Should handle empty message")
        void shouldHandleEmptyMessage() {
            // Given
            String emptyMessage = "";
            FileStorageException exception = new FileStorageException(emptyMessage);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).isEmpty();
        }

        @Test
        @DisplayName("Should handle very long message")
        void shouldHandleVeryLongMessage() {
            // Given
            String longMessage = "Error: " + "x".repeat(1000);
            FileStorageException exception = new FileStorageException(longMessage);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).isEqualTo(longMessage);
            assertThat(retrievedMessage).hasSize(1007); // "Error: " + 1000 'x's
        }
    }

    @Nested
    @DisplayName("Cause Tests")
    class CauseTests {

        @Test
        @DisplayName("Should preserve original cause")
        void shouldPreserveOriginalCause() {
            // Given
            String message = "File storage operation failed";
            RuntimeException originalCause = new RuntimeException("Underlying error");
            FileStorageException exception = new FileStorageException(message, originalCause);

            // When
            Throwable retrievedCause = exception.getCause();

            // Then
            assertThat(retrievedCause).isEqualTo(originalCause);
            assertThat(retrievedCause).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle nested causes")
        void shouldHandleNestedCauses() {
            // Given
            String message = "File storage operation failed";
            RuntimeException nestedCause = new RuntimeException("Nested error");
            RuntimeException originalCause = new RuntimeException("Underlying error", nestedCause);
            FileStorageException exception = new FileStorageException(message, originalCause);

            // When
            Throwable retrievedCause = exception.getCause();

            // Then
            assertThat(retrievedCause).isEqualTo(originalCause);
            assertThat(retrievedCause.getCause()).isEqualTo(nestedCause);
        }

        @Test
        @DisplayName("Should handle null cause")
        void shouldHandleNullCause() {
            // Given
            String message = "File storage operation failed";
            FileStorageException exception = new FileStorageException(message, null);

            // When
            Throwable retrievedCause = exception.getCause();

            // Then
            assertThat(retrievedCause).isNull();
        }
    }

    @Nested
    @DisplayName("StackTrace Tests")
    class StackTraceTests {

        @Test
        @DisplayName("Should have stack trace")
        void shouldHaveStackTrace() {
            // Given
            FileStorageException exception = new FileStorageException("Test message");

            // When
            StackTraceElement[] stackTrace = exception.getStackTrace();

            // Then
            assertThat(stackTrace).isNotEmpty();
            assertThat(stackTrace[0].getClassName()).contains("FileStorageExceptionTest");
        }

        @Test
        @DisplayName("Should have stack trace with cause")
        void shouldHaveStackTraceWithCause() {
            // Given
            RuntimeException cause = new RuntimeException("Underlying error");
            FileStorageException exception = new FileStorageException("Test message", cause);

            // When
            StackTraceElement[] stackTrace = exception.getStackTrace();
            StackTraceElement[] causeStackTrace = cause.getStackTrace();

            // Then
            assertThat(stackTrace).isNotEmpty();
            assertThat(causeStackTrace).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle special characters in message")
        void shouldHandleSpecialCharactersInMessage() {
            // Given
            String specialMessage = "Error: \n\t\r\"'\\";
            FileStorageException exception = new FileStorageException(specialMessage);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).isEqualTo(specialMessage);
        }

        @Test
        @DisplayName("Should handle unicode characters in message")
        void shouldHandleUnicodeCharactersInMessage() {
            // Given
            String unicodeMessage = "Error: 中文测试 🚀 émojis";
            FileStorageException exception = new FileStorageException(unicodeMessage);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).isEqualTo(unicodeMessage);
        }

        @Test
        @DisplayName("Should handle multiple exceptions with same message")
        void shouldHandleMultipleExceptionsWithSameMessage() {
            // Given
            String message = "Same error message";
            FileStorageException exception1 = new FileStorageException(message);
            FileStorageException exception2 = new FileStorageException(message);

            // When & Then
            assertThat(exception1.getMessage()).isEqualTo(exception2.getMessage());
            assertThat(exception1).isNotEqualTo(exception2); // Different instances
        }
    }
}
