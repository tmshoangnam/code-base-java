package io.github.base.file.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.io.File;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for DownloadResult.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("DownloadResult Tests")
class DownloadResultTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create successful DownloadResult with all parameters")
        void shouldCreateSuccessfulDownloadResultWithAllParameters() {
            // Given
            String content = "Hello World";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            String errorMessage = null;
            String errorCode = null;
            Instant downloadedAt = Instant.now();

            // When
            DownloadResult<String> result = new DownloadResult<>(
                true, content, metadata, errorMessage, errorCode, downloadedAt
            );

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.content()).isEqualTo(content);
            assertThat(result.metadata()).isEqualTo(metadata);
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.downloadedAt()).isEqualTo(downloadedAt);
        }

        @Test
        @DisplayName("Should create failed DownloadResult with all parameters")
        void shouldCreateFailedDownloadResultWithAllParameters() {
            // Given
            String content = null;
            FileMetadata metadata = null;
            String errorMessage = "Download failed due to file not found";
            String errorCode = "FILE_NOT_FOUND";
            Instant downloadedAt = Instant.now();

            // When
            DownloadResult<String> result = new DownloadResult<>(
                false, content, metadata, errorMessage, errorCode, downloadedAt
            );

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.content()).isNull();
            assertThat(result.metadata()).isNull();
            assertThat(result.errorMessage()).isEqualTo(errorMessage);
            assertThat(result.errorCode()).isEqualTo(errorCode);
            assertThat(result.downloadedAt()).isEqualTo(downloadedAt);
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {

        @Test
        @DisplayName("Should create success result using success method with metadata")
        void shouldCreateSuccessResultUsingSuccessMethodWithMetadata() {
            // Given
            String content = "Hello World";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);

            // When
            DownloadResult<String> result = DownloadResult.success(content, metadata);

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.content()).isEqualTo(content);
            assertThat(result.metadata()).isEqualTo(metadata);
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.downloadedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should create success result using success method without metadata")
        void shouldCreateSuccessResultUsingSuccessMethodWithoutMetadata() {
            // Given
            String content = "Hello World";

            // When
            DownloadResult<String> result = DownloadResult.success(content);

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.content()).isEqualTo(content);
            assertThat(result.metadata()).isNull();
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.downloadedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should create failure result using failure method with error code")
        void shouldCreateFailureResultUsingFailureMethodWithErrorCode() {
            // Given
            String errorMessage = "Download failed";
            String errorCode = "DOWNLOAD_ERROR";

            // When
            DownloadResult<String> result = DownloadResult.failure(errorMessage, errorCode);

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.content()).isNull();
            assertThat(result.metadata()).isNull();
            assertThat(result.errorMessage()).isEqualTo(errorMessage);
            assertThat(result.errorCode()).isEqualTo(errorCode);
            assertThat(result.downloadedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should create failure result using failure method without error code")
        void shouldCreateFailureResultUsingFailureMethodWithoutErrorCode() {
            // Given
            String errorMessage = "Download failed";

            // When
            DownloadResult<String> result = DownloadResult.failure(errorMessage);

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.content()).isNull();
            assertThat(result.metadata()).isNull();
            assertThat(result.errorMessage()).isEqualTo(errorMessage);
            assertThat(result.errorCode()).isNull();
            assertThat(result.downloadedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should throw exception when content is null in success method")
        void shouldThrowExceptionWhenContentIsNullInSuccessMethod() {
            // When & Then
            assertThatThrownBy(() -> DownloadResult.success((String) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when errorMessage is null in failure method")
        void shouldThrowExceptionWhenErrorMessageIsNullInFailureMethod() {
            // When & Then
            assertThatThrownBy(() -> DownloadResult.failure(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Error message cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when errorMessage is empty in failure method")
        void shouldThrowExceptionWhenErrorMessageIsEmptyInFailureMethod() {
            // When & Then
            assertThatThrownBy(() -> DownloadResult.failure(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Error message cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should work with String content")
        void shouldWorkWithStringContent() {
            // Given
            String content = "Hello World";

            // When
            DownloadResult<String> result = DownloadResult.success(content);

            // Then
            assertThat(result.content()).isInstanceOf(String.class);
            assertThat(result.content()).isEqualTo(content);
        }

        @Test
        @DisplayName("Should work with byte array content")
        void shouldWorkWithByteArrayContent() {
            // Given
            byte[] content = "Hello World".getBytes();

            // When
            DownloadResult<byte[]> result = DownloadResult.success(content);

            // Then
            assertThat(result.content()).isInstanceOf(byte[].class);
            assertThat(result.content()).isEqualTo(content);
        }

        @Test
        @DisplayName("Should work with File content")
        void shouldWorkWithFileContent() {
            // Given
            File content = new File("test.txt");

            // When
            DownloadResult<File> result = DownloadResult.success(content);

            // Then
            assertThat(result.content()).isInstanceOf(File.class);
            assertThat(result.content()).isEqualTo(content);
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("Should check if download was successful")
        void shouldCheckIfDownloadWasSuccessful() {
            // Given
            DownloadResult<String> successResult = DownloadResult.success("content");
            DownloadResult<String> failureResult = DownloadResult.failure("error");

            // When & Then
            assertThat(successResult.isSuccess()).isTrue();
            assertThat(failureResult.isSuccess()).isFalse();
        }

        @Test
        @DisplayName("Should check if download failed")
        void shouldCheckIfDownloadFailed() {
            // Given
            DownloadResult<String> successResult = DownloadResult.success("content");
            DownloadResult<String> failureResult = DownloadResult.failure("error");

            // When & Then
            assertThat(successResult.isFailure()).isFalse();
            assertThat(failureResult.isFailure()).isTrue();
        }

        @Test
        @DisplayName("Should get content")
        void shouldGetContent() {
            // Given
            String content = "Hello World";
            DownloadResult<String> result = DownloadResult.success(content);

            // When & Then
            assertThat(result.getContent()).isEqualTo(content);
        }

        @Test
        @DisplayName("Should get metadata")
        void shouldGetMetadata() {
            // Given
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            DownloadResult<String> result = DownloadResult.success("content", metadata);

            // When & Then
            assertThat(result.getMetadata()).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should get error message")
        void shouldGetErrorMessage() {
            // Given
            DownloadResult<String> result = DownloadResult.failure("Download failed");

            // When & Then
            assertThat(result.getErrorMessage()).isEqualTo("Download failed");
        }

        @Test
        @DisplayName("Should get error code")
        void shouldGetErrorCode() {
            // Given
            DownloadResult<String> result = DownloadResult.failure("Download failed", "ERROR_CODE");

            // When & Then
            assertThat(result.getErrorCode()).isEqualTo("ERROR_CODE");
        }

        @Test
        @DisplayName("Should get downloaded at timestamp")
        void shouldGetDownloadedAtTimestamp() {
            // Given
            DownloadResult<String> result = DownloadResult.success("content");

            // When & Then
            assertThat(result.getDownloadedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should check if content is available")
        void shouldCheckIfContentIsAvailable() {
            // Given
            DownloadResult<String> successResult = DownloadResult.success("content");
            DownloadResult<String> failureResult = DownloadResult.failure("error");

            // When & Then
            assertThat(successResult.hasContent()).isTrue();
            assertThat(failureResult.hasContent()).isFalse();
        }

        @Test
        @DisplayName("Should check if metadata is available")
        void shouldCheckIfMetadataIsAvailable() {
            // Given
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            DownloadResult<String> successResult = DownloadResult.success("content", metadata);
            DownloadResult<String> failureResult = DownloadResult.failure("error");

            // When & Then
            assertThat(successResult.hasMetadata()).isTrue();
            assertThat(failureResult.hasMetadata()).isFalse();
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields are same")
        void shouldBeEqualWhenAllFieldsAreSame() {
            // Given
            String content = "Hello World";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            Instant downloadedAt = Instant.now();

            DownloadResult<String> result1 = new DownloadResult<>(
                true, content, metadata, null, null, downloadedAt
            );
            DownloadResult<String> result2 = new DownloadResult<>(
                true, content, metadata, null, null, downloadedAt
            );

            // When & Then
            assertThat(result1).isEqualTo(result2)
                    .hasSameHashCodeAs(result2);
        }

        @Test
        @DisplayName("Should not be equal when success differs")
        void shouldNotBeEqualWhenSuccessDiffers() {
            // Given
            DownloadResult<String> success = DownloadResult.success("content");
            DownloadResult<String> failure = DownloadResult.failure("error");

            // When & Then
            assertThat(success).isNotEqualTo(failure);
        }

        @Test
        @DisplayName("Should not be equal when content differs")
        void shouldNotBeEqualWhenContentDiffers() {
            // Given
            DownloadResult<String> result1 = DownloadResult.success("content1");
            DownloadResult<String> result2 = DownloadResult.success("content2");

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
            DownloadResult<String> success = DownloadResult.success("content");
            DownloadResult<String> failure = DownloadResult.failure("error");

            // When
            String successString = success.toString();
            String failureString = failure.toString();

            // Then
            assertThat(successString).contains("success=true");
            assertThat(failureString).contains("success=false");
        }

        @Test
        @DisplayName("Should include content in toString")
        void shouldIncludeContentInToString() {
            // Given
            DownloadResult<String> result = DownloadResult.success("Hello World");

            // When
            String resultString = result.toString();

            // Then
            assertThat(resultString).contains("Hello World");
        }

        @Test
        @DisplayName("Should include error message in toString for failure")
        void shouldIncludeErrorMessageInToStringForFailure() {
            // Given
            DownloadResult<String> result = DownloadResult.failure("Download failed");

            // When
            String resultString = result.toString();

            // Then
            assertThat(resultString).contains("Download failed");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle empty content")
        void shouldHandleEmptyContent() {
            // Given
            String content = "";

            // When
            DownloadResult<String> result = DownloadResult.success(content);

            // Then
            assertThat(result.content()).isEmpty();
            assertThat(result.success()).isTrue();
        }

        @Test
        @DisplayName("Should handle whitespace in error message")
        void shouldHandleWhitespaceInErrorMessage() {
            // Given
            String errorMessage = "  Download failed  ";

            // When
            DownloadResult<String> result = DownloadResult.failure(errorMessage);

            // Then
            assertThat(result.errorMessage()).isEqualTo("Download failed");
        }

        @Test
        @DisplayName("Should handle null error code")
        void shouldHandleNullErrorCode() {
            // Given
            String errorMessage = "Download failed";

            // When
            DownloadResult<String> result = DownloadResult.failure(errorMessage, null);

            // Then
            assertThat(result.errorCode()).isNull();
        }
    }
}
