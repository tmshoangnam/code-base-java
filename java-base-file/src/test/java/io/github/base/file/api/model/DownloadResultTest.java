package io.github.base.file.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.io.File;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for DownloadResult record.
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
        @DisplayName("Should create successful DownloadResult with File")
        void shouldCreateSuccessfulDownloadResultWithFile() {
            // Given
            boolean success = true;
            File content = new File("test.txt");
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, Instant.now(), 
                Instant.now(), "abc123", null
            );
            String errorMessage = null;
            String errorCode = null;
            Instant downloadedAt = Instant.now();

            // When
            DownloadResult<File> result = new DownloadResult<>(
                success, content, metadata, errorMessage, errorCode, downloadedAt
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
        @DisplayName("Should create successful DownloadResult with byte array")
        void shouldCreateSuccessfulDownloadResultWithByteArray() {
            // Given
            boolean success = true;
            byte[] content = "Hello World".getBytes();
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", content.length, Instant.now(), 
                Instant.now(), "abc123", null
            );
            String errorMessage = null;
            String errorCode = null;
            Instant downloadedAt = Instant.now();

            // When
            DownloadResult<byte[]> result = new DownloadResult<>(
                success, content, metadata, errorMessage, errorCode, downloadedAt
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
        @DisplayName("Should create failed DownloadResult")
        void shouldCreateFailedDownloadResult() {
            // Given
            boolean success = false;
            File content = null;
            FileMetadata metadata = null;
            String errorMessage = "Download failed";
            String errorCode = "DOWNLOAD_ERROR";
            Instant downloadedAt = Instant.now();

            // When
            DownloadResult<File> result = new DownloadResult<>(
                success, content, metadata, errorMessage, errorCode, downloadedAt
            );

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.content()).isNull();
            assertThat(result.metadata()).isNull();
            assertThat(result.errorMessage()).isEqualTo(errorMessage);
            assertThat(result.errorCode()).isEqualTo(errorCode);
            assertThat(result.downloadedAt()).isEqualTo(downloadedAt);
        }

        @Test
        @DisplayName("Should create DownloadResult with null values")
        void shouldCreateDownloadResultWithNullValues() {
            // When
            DownloadResult<String> result = new DownloadResult<>(
                false, null, null, null, null, null
            );

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.content()).isNull();
            assertThat(result.metadata()).isNull();
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.downloadedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to identical DownloadResult")
        void shouldBeEqualToIdenticalDownloadResult() {
            // Given
            Instant now = Instant.now();
            File content = new File("test.txt");
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, now, now, "abc123", null
            );
            
            DownloadResult<File> result1 = new DownloadResult<>(
                true, content, metadata, null, null, now
            );
            DownloadResult<File> result2 = new DownloadResult<>(
                true, content, metadata, null, null, now
            );

            // When & Then
            assertThat(result1).isEqualTo(result2);
            assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to different DownloadResult")
        void shouldNotBeEqualToDifferentDownloadResult() {
            // Given
            Instant now = Instant.now();
            File content1 = new File("test1.txt");
            File content2 = new File("test2.txt");
            
            DownloadResult<File> result1 = new DownloadResult<>(
                true, content1, null, null, null, now
            );
            DownloadResult<File> result2 = new DownloadResult<>(
                true, content2, null, null, null, now
            );

            // When & Then
            assertThat(result1).isNotEqualTo(result2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            DownloadResult<String> result = new DownloadResult<>(
                true, "content", null, null, null, Instant.now()
            );

            // When & Then
            assertThat(result).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            DownloadResult<String> result = new DownloadResult<>(
                true, "content", null, null, null, Instant.now()
            );

            // When & Then
            assertThat(result).isNotEqualTo("not a DownloadResult");
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should include all fields in toString")
        void shouldIncludeAllFieldsInToString() {
            // Given
            String content = "Hello World";
            String errorMessage = "Download failed";
            String errorCode = "DOWNLOAD_ERROR";
            Instant downloadedAt = Instant.now();
            
            DownloadResult<String> result = new DownloadResult<>(
                true, content, null, errorMessage, errorCode, downloadedAt
            );

            // When
            String toString = result.toString();

            // Then
            assertThat(toString).contains(content);
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
            DownloadResult<String> result = new DownloadResult<>(
                true, "content", null, null, null, Instant.now()
            );

            // When & Then
            assertThat(result.success()).isTrue();
        }

        @Test
        @DisplayName("Should not be successful when success is false")
        void shouldNotBeSuccessfulWhenSuccessIsFalse() {
            // Given
            DownloadResult<String> result = new DownloadResult<>(
                false, null, null, "Download failed", "DOWNLOAD_ERROR", Instant.now()
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
            String errorMessage = "Download failed due to network error";
            DownloadResult<String> result = new DownloadResult<>(
                false, null, null, errorMessage, "NETWORK_ERROR", Instant.now()
            );

            // When & Then
            assertThat(result.errorMessage()).isEqualTo(errorMessage);
        }

        @Test
        @DisplayName("Should have error code when failed")
        void shouldHaveErrorCodeWhenFailed() {
            // Given
            String errorCode = "FILE_NOT_FOUND";
            DownloadResult<String> result = new DownloadResult<>(
                false, null, null, "File not found", errorCode, Instant.now()
            );

            // When & Then
            assertThat(result.errorCode()).isEqualTo(errorCode);
        }

        @Test
        @DisplayName("Should not have content when failed")
        void shouldNotHaveContentWhenFailed() {
            // Given
            DownloadResult<String> result = new DownloadResult<>(
                false, null, null, "Download failed", "DOWNLOAD_ERROR", Instant.now()
            );

            // When & Then
            assertThat(result.content()).isNull();
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should work with different content types")
        void shouldWorkWithDifferentContentTypes() {
            // Given
            Instant now = Instant.now();
            
            // When
            DownloadResult<String> stringResult = new DownloadResult<>(
                true, "string content", null, null, null, now
            );
            DownloadResult<Integer> intResult = new DownloadResult<>(
                true, 42, null, null, null, now
            );
            DownloadResult<Map<String, Object>> mapResult = new DownloadResult<>(
                true, Map.of("key", "value"), null, null, null, now
            );

            // Then
            assertThat(stringResult.content()).isEqualTo("string content");
            assertThat(intResult.content()).isEqualTo(42);
            assertThat(mapResult.content()).containsEntry("key", "value");
        }

        @Test
        @DisplayName("Should maintain type safety")
        void shouldMaintainTypeSafety() {
            // Given
            DownloadResult<String> result = new DownloadResult<>(
                true, "content", null, null, null, Instant.now()
            );

            // When & Then
            assertThat(result.content()).isInstanceOf(String.class);
            assertThat(result.content()).isEqualTo("content");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty content")
        void shouldHandleEmptyContent() {
            // Given
            String emptyContent = "";
            DownloadResult<String> result = new DownloadResult<>(
                true, emptyContent, null, null, null, Instant.now()
            );

            // When & Then
            assertThat(result.content()).isEmpty();
        }

        @Test
        @DisplayName("Should handle very large content")
        void shouldHandleVeryLargeContent() {
            // Given
            String largeContent = "x".repeat(10000);
            DownloadResult<String> result = new DownloadResult<>(
                true, largeContent, null, null, null, Instant.now()
            );

            // When & Then
            assertThat(result.content()).isEqualTo(largeContent);
            assertThat(result.content()).hasSize(10000);
        }

        @Test
        @DisplayName("Should handle very long error message")
        void shouldHandleVeryLongErrorMessage() {
            // Given
            String longErrorMessage = "Error: " + "x".repeat(1000);
            DownloadResult<String> result = new DownloadResult<>(
                false, null, null, longErrorMessage, "LONG_ERROR", Instant.now()
            );

            // When & Then
            assertThat(result.errorMessage()).isEqualTo(longErrorMessage);
            assertThat(result.errorMessage()).hasSize(1007); // "Error: " + 1000 'x's
        }
    }
}
