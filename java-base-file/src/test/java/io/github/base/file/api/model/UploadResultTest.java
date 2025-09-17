package io.github.base.file.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for UploadResult record.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("UploadResult Tests")
class UploadResultTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create successful UploadResult")
        void shouldCreateSuccessfulUploadResult() {
            // Given
            boolean success = true;
            String fileId = "test-file-123";
            String location = "/uploads/test-file-123";
            String errorMessage = null;
            String errorCode = null;
            Instant uploadedAt = Instant.now();
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, Instant.now(), 
                Instant.now(), "abc123", null
            );

            // When
            UploadResult result = new UploadResult(
                success, fileId, location, errorMessage, errorCode, uploadedAt, metadata
            );

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.fileId()).isEqualTo(fileId);
            assertThat(result.location()).isEqualTo(location);
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isEqualTo(uploadedAt);
            assertThat(result.metadata()).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should create failed UploadResult")
        void shouldCreateFailedUploadResult() {
            // Given
            boolean success = false;
            String fileId = "test-file-123";
            String location = null;
            String errorMessage = "Upload failed";
            String errorCode = "UPLOAD_ERROR";
            Instant uploadedAt = Instant.now();
            FileMetadata metadata = null;

            // When
            UploadResult result = new UploadResult(
                success, fileId, location, errorMessage, errorCode, uploadedAt, metadata
            );

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.fileId()).isEqualTo(fileId);
            assertThat(result.location()).isNull();
            assertThat(result.errorMessage()).isEqualTo(errorMessage);
            assertThat(result.errorCode()).isEqualTo(errorCode);
            assertThat(result.uploadedAt()).isEqualTo(uploadedAt);
            assertThat(result.metadata()).isNull();
        }

        @Test
        @DisplayName("Should create UploadResult with null values")
        void shouldCreateUploadResultWithNullValues() {
            // When
            UploadResult result = new UploadResult(
                false, null, null, null, null, null, null
            );

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.fileId()).isNull();
            assertThat(result.location()).isNull();
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isNull();
            assertThat(result.metadata()).isNull();
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to identical UploadResult")
        void shouldBeEqualToIdenticalUploadResult() {
            // Given
            Instant now = Instant.now();
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, now, now, "abc123", null
            );
            
            UploadResult result1 = new UploadResult(
                true, "file-123", "/uploads/file-123", null, null, now, metadata
            );
            UploadResult result2 = new UploadResult(
                true, "file-123", "/uploads/file-123", null, null, now, metadata
            );

            // When & Then
            assertThat(result1).isEqualTo(result2);
            assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to different UploadResult")
        void shouldNotBeEqualToDifferentUploadResult() {
            // Given
            Instant now = Instant.now();
            
            UploadResult result1 = new UploadResult(
                true, "file-123", "/uploads/file-123", null, null, now, null
            );
            UploadResult result2 = new UploadResult(
                false, "file-123", "/uploads/file-123", null, null, now, null
            );

            // When & Then
            assertThat(result1).isNotEqualTo(result2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            UploadResult result = new UploadResult(
                true, "file-123", "/uploads/file-123", null, null, Instant.now(), null
            );

            // When & Then
            assertThat(result).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            UploadResult result = new UploadResult(
                true, "file-123", "/uploads/file-123", null, null, Instant.now(), null
            );

            // When & Then
            assertThat(result).isNotEqualTo("not an UploadResult");
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should include all fields in toString")
        void shouldIncludeAllFieldsInToString() {
            // Given
            String fileId = "test-file-123";
            String location = "/uploads/test-file-123";
            String errorMessage = "Upload failed";
            String errorCode = "UPLOAD_ERROR";
            Instant uploadedAt = Instant.now();
            
            UploadResult result = new UploadResult(
                true, fileId, location, errorMessage, errorCode, uploadedAt, null
            );

            // When
            String toString = result.toString();

            // Then
            assertThat(toString).contains(fileId);
            assertThat(toString).contains(location);
            assertThat(toString).contains(errorMessage);
            assertThat(toString).contains(errorCode);
        }
    }

    @Nested
    @DisplayName("Success State Tests")
    class SuccessStateTests {

        @Test
        @DisplayName("Should be successful when success is true")
        void shouldBeSuccessfulWhenSuccessIsTrue() {
            // Given
            UploadResult result = new UploadResult(
                true, "file-123", "/uploads/file-123", null, null, Instant.now(), null
            );

            // When & Then
            assertThat(result.success()).isTrue();
        }

        @Test
        @DisplayName("Should not be successful when success is false")
        void shouldNotBeSuccessfulWhenSuccessIsFalse() {
            // Given
            UploadResult result = new UploadResult(
                false, "file-123", null, "Upload failed", "UPLOAD_ERROR", Instant.now(), null
            );

            // When & Then
            assertThat(result.success()).isFalse();
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should have error message when failed")
        void shouldHaveErrorMessageWhenFailed() {
            // Given
            String errorMessage = "Upload failed due to network error";
            UploadResult result = new UploadResult(
                false, "file-123", null, errorMessage, "NETWORK_ERROR", Instant.now(), null
            );

            // When & Then
            assertThat(result.errorMessage()).isEqualTo(errorMessage);
        }

        @Test
        @DisplayName("Should have error code when failed")
        void shouldHaveErrorCodeWhenFailed() {
            // Given
            String errorCode = "STORAGE_FULL";
            UploadResult result = new UploadResult(
                false, "file-123", null, "Storage is full", errorCode, Instant.now(), null
            );

            // When & Then
            assertThat(result.errorCode()).isEqualTo(errorCode);
        }

        @Test
        @DisplayName("Should not have location when failed")
        void shouldNotHaveLocationWhenFailed() {
            // Given
            UploadResult result = new UploadResult(
                false, "file-123", null, "Upload failed", "UPLOAD_ERROR", Instant.now(), null
            );

            // When & Then
            assertThat(result.location()).isNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty fileId")
        void shouldHandleEmptyFileId() {
            // Given
            UploadResult result = new UploadResult(
                true, "", "/uploads/", null, null, Instant.now(), null
            );

            // When & Then
            assertThat(result.fileId()).isEmpty();
        }

        @Test
        @DisplayName("Should handle very long fileId")
        void shouldHandleVeryLongFileId() {
            // Given
            String longFileId = "a".repeat(1000);
            UploadResult result = new UploadResult(
                true, longFileId, "/uploads/" + longFileId, null, null, Instant.now(), null
            );

            // When & Then
            assertThat(result.fileId()).isEqualTo(longFileId);
            assertThat(result.fileId()).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle very long error message")
        void shouldHandleVeryLongErrorMessage() {
            // Given
            String longErrorMessage = "Error: " + "x".repeat(1000);
            UploadResult result = new UploadResult(
                false, "file-123", null, longErrorMessage, "LONG_ERROR", Instant.now(), null
            );

            // When & Then
            assertThat(result.errorMessage()).isEqualTo(longErrorMessage);
            assertThat(result.errorMessage()).hasSize(1007); // "Error: " + 1000 'x's
        }
    }
}
