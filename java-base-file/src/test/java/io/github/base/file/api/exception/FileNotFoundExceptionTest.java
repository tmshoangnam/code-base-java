package io.github.base.file.api.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for FileNotFoundException.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("FileNotFoundException Tests")
class FileNotFoundExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message only")
        void shouldCreateExceptionWithMessageOnly() {
            // Given
            String message = "File not found: test.txt";

            // When
            FileNotFoundException exception = new FileNotFoundException("file-123", message);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            // Given
            String message = "File not found: test.txt";
            RuntimeException cause = new RuntimeException("Underlying error");

            // When
            FileNotFoundException exception = new FileNotFoundException("file-123", message, cause);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Should create exception with null message")
        void shouldCreateExceptionWithNullMessage() {
            // When
            FileNotFoundException exception = new FileNotFoundException("file-123", (String) null);

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
            FileNotFoundException exception = new FileNotFoundException("file-123", "Test message", cause);

            // Then
            assertThat(exception.getMessage()).isEqualTo("Test message");
            assertThat(exception.getCause()).isEqualTo(cause);
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should be instance of FileStorageException")
        void shouldBeInstanceOfFileStorageException() {
            // Given
            FileNotFoundException exception = new FileNotFoundException("Test message");

            // When & Then
            assertThat(exception).isInstanceOf(FileStorageException.class);
        }

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Given
            FileNotFoundException exception = new FileNotFoundException("Test message");

            // When & Then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should be instance of Exception")
        void shouldBeInstanceOfException() {
            // Given
            FileNotFoundException exception = new FileNotFoundException("Test message");

            // When & Then
            assertThat(exception).isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should be instance of Throwable")
        void shouldBeInstanceOfThrowable() {
            // Given
            FileNotFoundException exception = new FileNotFoundException("Test message");

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
            String originalMessage = "File not found: important.txt";
            FileNotFoundException exception = new FileNotFoundException("file-123", originalMessage);

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
            FileNotFoundException exception = new FileNotFoundException("file-123", emptyMessage);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).isEmpty();
        }

        @Test
        @DisplayName("Should handle file path in message")
        void shouldHandleFilePathInMessage() {
            // Given
            String filePath = "/path/to/file.txt";
            String message = "File not found: " + filePath;
            FileNotFoundException exception = new FileNotFoundException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(filePath);
            assertThat(retrievedMessage).contains("File not found");
        }
    }

    @Nested
    @DisplayName("Cause Tests")
    class CauseTests {

        @Test
        @DisplayName("Should preserve original cause")
        void shouldPreserveOriginalCause() {
            // Given
            String message = "File not found: test.txt";
            RuntimeException originalCause = new RuntimeException("Underlying error");
            FileNotFoundException exception = new FileNotFoundException("file-123", message, originalCause);

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
            String message = "File not found: test.txt";
            RuntimeException nestedCause = new RuntimeException("Nested error");
            RuntimeException originalCause = new RuntimeException("Underlying error", nestedCause);
            FileNotFoundException exception = new FileNotFoundException("file-123", message, originalCause);

            // When
            Throwable retrievedCause = exception.getCause();

            // Then
            assertThat(retrievedCause).isEqualTo(originalCause);
            assertThat(retrievedCause.getCause()).isEqualTo(nestedCause);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to identical FileNotFoundException")
        void shouldBeEqualToIdenticalFileNotFoundException() {
            // Given
            String message = "File not found: test.txt";
            FileNotFoundException exception1 = new FileNotFoundException(message);
            FileNotFoundException exception2 = new FileNotFoundException(message);

            // When & Then
            assertThat(exception1.getMessage()).isEqualTo(exception2.getMessage());
            assertThat(exception1).isNotEqualTo(exception2); // Different instances
        }

        @Test
        @DisplayName("Should not be equal to different exception type")
        void shouldNotBeEqualToDifferentExceptionType() {
            // Given
            String message = "File not found: test.txt";
            FileNotFoundException fileNotFoundException = new FileNotFoundException(message);
            FileStorageException fileStorageException = new FileStorageException(message);

            // When & Then
            assertThat(fileNotFoundException).isNotEqualTo(fileStorageException);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle special characters in file path")
        void shouldHandleSpecialCharactersInFilePath() {
            // Given
            String specialPath = "/path/with/special-chars/文件.txt";
            String message = "File not found: " + specialPath;
            FileNotFoundException exception = new FileNotFoundException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(specialPath);
        }

        @Test
        @DisplayName("Should handle very long file path")
        void shouldHandleVeryLongFilePath() {
            // Given
            String longPath = "/very/long/path/" + "x".repeat(1000) + ".txt";
            String message = "File not found: " + longPath;
            FileNotFoundException exception = new FileNotFoundException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(longPath);
            assertThat(retrievedMessage).hasSize(1036); // "File not found: " + "/very/long/path/" + 1000 'x's + ".txt"
        }

        @Test
        @DisplayName("Should handle null file path")
        void shouldHandleNullFilePath() {
            // Given
            String message = "File not found: null";
            FileNotFoundException exception = new FileNotFoundException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains("null");
        }
    }

    @Nested
    @DisplayName("Common Usage Patterns Tests")
    class CommonUsagePatternsTests {

        @Test
        @DisplayName("Should work with file ID")
        void shouldWorkWithFileId() {
            // Given
            String fileId = "file-123-456";
            String message = "File not found with ID: " + fileId;
            FileNotFoundException exception = new FileNotFoundException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(fileId);
        }

        @Test
        @DisplayName("Should work with file name")
        void shouldWorkWithFileName() {
            // Given
            String fileName = "document.pdf";
            String message = "File not found: " + fileName;
            FileNotFoundException exception = new FileNotFoundException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(fileName);
        }

        @Test
        @DisplayName("Should work with full path")
        void shouldWorkWithFullPath() {
            // Given
            String fullPath = "/uploads/documents/report.pdf";
            String message = "File not found at path: " + fullPath;
            FileNotFoundException exception = new FileNotFoundException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(fullPath);
        }
    }
}
