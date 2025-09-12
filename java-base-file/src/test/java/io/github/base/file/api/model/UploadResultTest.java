package io.github.base.file.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for UploadResult.
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
        @DisplayName("Should create successful UploadResult with all parameters")
        void shouldCreateSuccessfulUploadResultWithAllParameters() {
            // Given
            String fileId = "test-file-123";
            String location = "https://example.com/files/test-file-123";
            String errorMessage = null;
            String errorCode = null;
            Instant uploadedAt = Instant.now();
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);

            // When
            UploadResult result = new UploadResult(
                true, fileId, location, errorMessage, errorCode, uploadedAt, metadata
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
        @DisplayName("Should create failed UploadResult with all parameters")
        void shouldCreateFailedUploadResultWithAllParameters() {
            // Given
            String fileId = "test-file-123";
            String location = null;
            String errorMessage = "Upload failed due to insufficient permissions";
            String errorCode = "PERMISSION_DENIED";
            Instant uploadedAt = Instant.now();
            FileMetadata metadata = null;

            // When
            UploadResult result = new UploadResult(
                false, fileId, location, errorMessage, errorCode, uploadedAt, metadata
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
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {

        @Test
        @DisplayName("Should create success result using success method with metadata")
        void shouldCreateSuccessResultUsingSuccessMethodWithMetadata() {
            // Given
            String fileId = "test-file-123";
            String location = "https://example.com/files/test-file-123";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);

            // When
            UploadResult result = UploadResult.success(fileId, location, metadata);

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.fileId()).isEqualTo(fileId);
            assertThat(result.location()).isEqualTo(location);
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isNotNull();
            assertThat(result.metadata()).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should create success result using success method without metadata")
        void shouldCreateSuccessResultUsingSuccessMethodWithoutMetadata() {
            // Given
            String fileId = "test-file-123";
            String location = "https://example.com/files/test-file-123";

            // When
            UploadResult result = UploadResult.success(fileId, location);

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.fileId()).isEqualTo(fileId);
            assertThat(result.location()).isEqualTo(location);
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isNotNull();
            assertThat(result.metadata()).isNull();
        }

        @Test
        @DisplayName("Should create failure result using failure method with error code")
        void shouldCreateFailureResultUsingFailureMethodWithErrorCode() {
            // Given
            String fileId = "test-file-123";
            String errorMessage = "Upload failed";
            String errorCode = "UPLOAD_ERROR";

            // When
            UploadResult result = UploadResult.failure(fileId, errorMessage, errorCode);

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.fileId()).isEqualTo(fileId);
            assertThat(result.location()).isNull();
            assertThat(result.errorMessage()).isEqualTo(errorMessage);
            assertThat(result.errorCode()).isEqualTo(errorCode);
            assertThat(result.uploadedAt()).isNotNull();
            assertThat(result.metadata()).isNull();
        }

        @Test
        @DisplayName("Should create failure result using failure method without error code")
        void shouldCreateFailureResultUsingFailureMethodWithoutErrorCode() {
            // Given
            String fileId = "test-file-123";
            String errorMessage = "Upload failed";

            // When
            UploadResult result = UploadResult.failure(fileId, errorMessage);

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.fileId()).isEqualTo(fileId);
            assertThat(result.location()).isNull();
            assertThat(result.errorMessage()).isEqualTo(errorMessage);
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isNotNull();
            assertThat(result.metadata()).isNull();
        }

        @Test
        @DisplayName("Should throw exception when fileId is null in success method")
        void shouldThrowExceptionWhenFileIdIsNullInSuccessMethod() {
            // When & Then
            assertThatThrownBy(() -> UploadResult.success(null, "location"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File ID cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when fileId is empty in success method")
        void shouldThrowExceptionWhenFileIdIsEmptyInSuccessMethod() {
            // When & Then
            assertThatThrownBy(() -> UploadResult.success("", "location"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File ID cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when location is null in success method")
        void shouldThrowExceptionWhenLocationIsNullInSuccessMethod() {
            // When & Then
            assertThatThrownBy(() -> UploadResult.success("file-123", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when location is empty in success method")
        void shouldThrowExceptionWhenLocationIsEmptyInSuccessMethod() {
            // When & Then
            assertThatThrownBy(() -> UploadResult.success("file-123", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Location cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when fileId is null in failure method")
        void shouldThrowExceptionWhenFileIdIsNullInFailureMethod() {
            // When & Then
            assertThatThrownBy(() -> UploadResult.failure(null, "error"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File ID cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when errorMessage is null in failure method")
        void shouldThrowExceptionWhenErrorMessageIsNullInFailureMethod() {
            // When & Then
            assertThatThrownBy(() -> UploadResult.failure("file-123", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Error message cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when errorMessage is empty in failure method")
        void shouldThrowExceptionWhenErrorMessageIsEmptyInFailureMethod() {
            // When & Then
            assertThatThrownBy(() -> UploadResult.failure("file-123", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Error message cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("Should check if upload was successful")
        void shouldCheckIfUploadWasSuccessful() {
            // Given
            UploadResult successResult = UploadResult.success("file-123", "location");
            UploadResult failureResult = UploadResult.failure("file-123", "error");

            // When & Then
            assertThat(successResult.isSuccess()).isTrue();
            assertThat(failureResult.isSuccess()).isFalse();
        }

        @Test
        @DisplayName("Should check if upload failed")
        void shouldCheckIfUploadFailed() {
            // Given
            UploadResult successResult = UploadResult.success("file-123", "location");
            UploadResult failureResult = UploadResult.failure("file-123", "error");

            // When & Then
            assertThat(successResult.isFailure()).isFalse();
            assertThat(failureResult.isFailure()).isTrue();
        }

        @Test
        @DisplayName("Should get file ID")
        void shouldGetFileId() {
            // Given
            UploadResult result = UploadResult.success("file-123", "location");

            // When & Then
            assertThat(result.getFileId()).isEqualTo("file-123");
        }

        @Test
        @DisplayName("Should get location")
        void shouldGetLocation() {
            // Given
            UploadResult result = UploadResult.success("file-123", "location");

            // When & Then
            assertThat(result.getLocation()).isEqualTo("location");
        }

        @Test
        @DisplayName("Should get error message")
        void shouldGetErrorMessage() {
            // Given
            UploadResult result = UploadResult.failure("file-123", "error message");

            // When & Then
            assertThat(result.getErrorMessage()).isEqualTo("error message");
        }

        @Test
        @DisplayName("Should get error code")
        void shouldGetErrorCode() {
            // Given
            UploadResult result = UploadResult.failure("file-123", "error message", "ERROR_CODE");

            // When & Then
            assertThat(result.getErrorCode()).isEqualTo("ERROR_CODE");
        }

        @Test
        @DisplayName("Should get uploaded at timestamp")
        void shouldGetUploadedAtTimestamp() {
            // Given
            UploadResult result = UploadResult.success("file-123", "location");

            // When & Then
            assertThat(result.getUploadedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should get metadata")
        void shouldGetMetadata() {
            // Given
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            UploadResult result = UploadResult.success("file-123", "location", metadata);

            // When & Then
            assertThat(result.getMetadata()).isEqualTo(metadata);
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields are same")
        void shouldBeEqualWhenAllFieldsAreSame() {
            // Given
            String fileId = "test-file-123";
            String location = "https://example.com/files/test-file-123";
            Instant uploadedAt = Instant.now();
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);

            UploadResult result1 = new UploadResult(
                true, fileId, location, null, null, uploadedAt, metadata
            );
            UploadResult result2 = new UploadResult(
                true, fileId, location, null, null, uploadedAt, metadata
            );

            // When & Then
            assertThat(result1).isEqualTo(result2);
            assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when success differs")
        void shouldNotBeEqualWhenSuccessDiffers() {
            // Given
            UploadResult success = UploadResult.success("file-123", "location");
            UploadResult failure = UploadResult.failure("file-123", "error");

            // When & Then
            assertThat(success).isNotEqualTo(failure);
        }

        @Test
        @DisplayName("Should not be equal when fileId differs")
        void shouldNotBeEqualWhenFileIdDiffers() {
            // Given
            UploadResult result1 = UploadResult.success("file-123", "location");
            UploadResult result2 = UploadResult.success("file-456", "location");

            // When & Then
            assertThat(result1).isNotEqualTo(result2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should include success status in toString")
        void shouldIncludeSuccessStatusInToString() {
            // Given
            UploadResult success = UploadResult.success("file-123", "location");
            UploadResult failure = UploadResult.failure("file-123", "error");

            // When
            String successString = success.toString();
            String failureString = failure.toString();

            // Then
            assertThat(successString).contains("success=true");
            assertThat(failureString).contains("success=false");
        }

        @Test
        @DisplayName("Should include fileId in toString")
        void shouldIncludeFileIdInToString() {
            // Given
            UploadResult result = UploadResult.success("file-123", "location");

            // When
            String resultString = result.toString();

            // Then
            assertThat(resultString).contains("file-123");
        }

        @Test
        @DisplayName("Should include error message in toString for failure")
        void shouldIncludeErrorMessageInToStringForFailure() {
            // Given
            UploadResult result = UploadResult.failure("file-123", "Upload failed");

            // When
            String resultString = result.toString();

            // Then
            assertThat(resultString).contains("Upload failed");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle whitespace in fileId and location")
        void shouldHandleWhitespaceInFileIdAndLocation() {
            // Given
            String fileId = "  file-123  ";
            String location = "  https://example.com/files/file-123  ";

            // When
            UploadResult result = UploadResult.success(fileId, location);

            // Then
            assertThat(result.fileId()).isEqualTo("file-123");
            assertThat(result.location()).isEqualTo("https://example.com/files/file-123");
        }

        @Test
        @DisplayName("Should handle whitespace in error message")
        void shouldHandleWhitespaceInErrorMessage() {
            // Given
            String fileId = "file-123";
            String errorMessage = "  Upload failed  ";

            // When
            UploadResult result = UploadResult.failure(fileId, errorMessage);

            // Then
            assertThat(result.errorMessage()).isEqualTo("Upload failed");
        }

        @Test
        @DisplayName("Should handle null error code")
        void shouldHandleNullErrorCode() {
            // Given
            String fileId = "file-123";
            String errorMessage = "Upload failed";

            // When
            UploadResult result = UploadResult.failure(fileId, errorMessage, null);

            // Then
            assertThat(result.errorCode()).isNull();
        }
    }
}
