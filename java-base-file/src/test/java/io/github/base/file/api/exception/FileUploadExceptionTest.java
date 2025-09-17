package io.github.base.file.api.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for FileUploadException.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("FileUploadException Tests")
class FileUploadExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message only")
        void shouldCreateExceptionWithMessageOnly() {
            // Given
            String message = "File upload failed: test.txt";

            // When
            FileUploadException exception = new FileUploadException("file-123", message);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            // Given
            String message = "File upload failed: test.txt";
            RuntimeException cause = new RuntimeException("Underlying error");

            // When
            FileUploadException exception = new FileUploadException("file-123", message, cause);

            // Then
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Should create exception with null message")
        void shouldCreateExceptionWithNullMessage() {
            // When
            FileUploadException exception = new FileUploadException("file-123", (String) null);

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
            FileUploadException exception = new FileUploadException("file-123", null, cause);

            // Then
            assertThat(exception.getMessage()).isNull();
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
            FileUploadException exception = new FileUploadException("file-123", "Test message");

            // When & Then
            assertThat(exception).isInstanceOf(FileStorageException.class);
        }

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Given
            FileUploadException exception = new FileUploadException("file-123", "Test message");

            // When & Then
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should be instance of Exception")
        void shouldBeInstanceOfException() {
            // Given
            FileUploadException exception = new FileUploadException("file-123", "Test message");

            // When & Then
            assertThat(exception).isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should be instance of Throwable")
        void shouldBeInstanceOfThrowable() {
            // Given
            FileUploadException exception = new FileUploadException("file-123", "Test message");

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
            String originalMessage = "File upload failed: important.txt";
            FileUploadException exception = new FileUploadException("file-123", originalMessage);

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
            FileUploadException exception = new FileUploadException("file-123", emptyMessage);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).isEmpty();
        }

        @Test
        @DisplayName("Should handle file name in message")
        void shouldHandleFileNameInMessage() {
            // Given
            String fileName = "document.pdf";
            String message = "File upload failed: " + fileName;
            FileUploadException exception = new FileUploadException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(fileName);
            assertThat(retrievedMessage).contains("File upload failed");
        }
    }

    @Nested
    @DisplayName("Cause Tests")
    class CauseTests {

        @Test
        @DisplayName("Should preserve original cause")
        void shouldPreserveOriginalCause() {
            // Given
            String message = "File upload failed: test.txt";
            RuntimeException originalCause = new RuntimeException("Underlying error");
            FileUploadException exception = new FileUploadException("file-123", message, originalCause);

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
            String message = "File upload failed: test.txt";
            RuntimeException nestedCause = new RuntimeException("Nested error");
            RuntimeException originalCause = new RuntimeException("Underlying error", nestedCause);
            FileUploadException exception = new FileUploadException("file-123", message, originalCause);

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
        @DisplayName("Should be equal to identical FileUploadException")
        void shouldBeEqualToIdenticalFileUploadException() {
            // Given
            String message = "File upload failed: test.txt";
            FileUploadException exception1 = new FileUploadException("file-123", message);
            FileUploadException exception2 = new FileUploadException("file-123", message);

            // When & Then
            assertThat(exception1.getMessage()).isEqualTo(exception2.getMessage());
            assertThat(exception1).isNotEqualTo(exception2); // Different instances
        }

        @Test
        @DisplayName("Should not be equal to different exception type")
        void shouldNotBeEqualToDifferentExceptionType() {
            // Given
            String message = "File upload failed: test.txt";
            FileUploadException fileUploadException = new FileUploadException("file-123", message);
            FileStorageException fileStorageException = new FileStorageException(message);

            // When & Then
            assertThat(fileUploadException).isNotEqualTo(fileStorageException);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle special characters in file name")
        void shouldHandleSpecialCharactersInFileName() {
            // Given
            String specialName = "文件 with spaces & symbols.txt";
            String message = "File upload failed: " + specialName;
            FileUploadException exception = new FileUploadException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(specialName);
        }

        @Test
        @DisplayName("Should handle very long file name")
        void shouldHandleVeryLongFileName() {
            // Given
            String longName = "very-long-file-name-" + "x".repeat(1000) + ".txt";
            FileUploadException exception = new FileUploadException("file-123", longName);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).isEqualTo(longName);
            assertThat(retrievedMessage).hasSize(1024); // "very-long-file-name-" + 1000 'x's + ".txt"
        }

        @Test
        @DisplayName("Should handle null file name")
        void shouldHandleNullFileName() {
            // Given
            String message = "File upload failed: null";
            FileUploadException exception = new FileUploadException("file-123", message);

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
            String message = "File upload failed for ID: " + fileId;
            FileUploadException exception = new FileUploadException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(fileId);
        }

        @Test
        @DisplayName("Should work with file size")
        void shouldWorkWithFileSize() {
            // Given
            long fileSize = 1024L * 1024L * 100L; // 100MB
            String message = "File upload failed: file too large (" + fileSize + " bytes)";
            FileUploadException exception = new FileUploadException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(String.valueOf(fileSize));
        }

        @Test
        @DisplayName("Should work with content type")
        void shouldWorkWithContentType() {
            // Given
            String contentType = "application/pdf";
            String message = "File upload failed: unsupported content type " + contentType;
            FileUploadException exception = new FileUploadException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(contentType);
        }

        @Test
        @DisplayName("Should work with error code")
        void shouldWorkWithErrorCode() {
            // Given
            String errorCode = "UPLOAD_SIZE_EXCEEDED";
            String message = "File upload failed: " + errorCode;
            FileUploadException exception = new FileUploadException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains(errorCode);
        }
    }

    @Nested
    @DisplayName("Upload Error Scenarios Tests")
    class UploadErrorScenariosTests {

        @Test
        @DisplayName("Should handle network error")
        void shouldHandleNetworkError() {
            // Given
            String message = "File upload failed: network timeout";
            RuntimeException networkError = new RuntimeException("Connection timeout");
            FileUploadException exception = new FileUploadException("file-123", message, networkError);

            // When
            String retrievedMessage = exception.getMessage();
            Throwable retrievedCause = exception.getCause();

            // Then
            assertThat(retrievedMessage).contains("network timeout");
            assertThat(retrievedCause).isEqualTo(networkError);
        }

        @Test
        @DisplayName("Should handle storage full error")
        void shouldHandleStorageFullError() {
            // Given
            String message = "File upload failed: storage quota exceeded";
            FileUploadException exception = new FileUploadException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains("storage quota exceeded");
        }

        @Test
        @DisplayName("Should handle permission error")
        void shouldHandlePermissionError() {
            // Given
            String message = "File upload failed: insufficient permissions";
            FileUploadException exception = new FileUploadException("file-123", message);

            // When
            String retrievedMessage = exception.getMessage();

            // Then
            assertThat(retrievedMessage).contains("insufficient permissions");
        }
    }
}
