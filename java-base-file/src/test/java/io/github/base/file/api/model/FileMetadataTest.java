package io.github.base.file.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.time.Instant;
import java.util.Map;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for FileMetadata record.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("FileMetadata Tests")
class FileMetadataTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create FileMetadata with all parameters")
        void shouldCreateFileMetadataWithAllParameters() {
            // Given
            String fileName = "test.txt";
            String contentType = "text/plain";
            long size = 1024L;
            Instant creationTime = Instant.now();
            Instant lastModifiedTime = Instant.now();
            String checksum = "abc123";
            Map<String, String> additionalMetadata = Map.of("key1", "value1");

            // When
            FileMetadata metadata = new FileMetadata(
                fileName, contentType, size, creationTime, 
                lastModifiedTime, checksum, additionalMetadata
            );

            // Then
            assertThat(metadata.fileName()).isEqualTo(fileName);
            assertThat(metadata.contentType()).isEqualTo(contentType);
            assertThat(metadata.size()).isEqualTo(size);
            assertThat(metadata.createdAt()).isEqualTo(creationTime);
            assertThat(metadata.lastModified()).isEqualTo(lastModifiedTime);
            assertThat(metadata.checksum()).isEqualTo(checksum);
            assertThat(metadata.properties()).isEqualTo(additionalMetadata);
        }

        @Test
        @DisplayName("Should create FileMetadata with null values")
        void shouldCreateFileMetadataWithNullValues() {
            // When
            FileMetadata metadata = new FileMetadata(
                null, null, 0L, null, null, null, null
            );

            // Then
            assertThat(metadata.fileName()).isNull();
            assertThat(metadata.contentType()).isNull();
            assertThat(metadata.size()).isEqualTo(0L);
            assertThat(metadata.createdAt()).isNull();
            assertThat(metadata.lastModified()).isNull();
            assertThat(metadata.checksum()).isNull();
            assertThat(metadata.properties()).isNull();
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to identical FileMetadata")
        void shouldBeEqualToIdenticalFileMetadata() {
            // Given
            Instant now = Instant.now();
            Map<String, String> additionalMetadata = Map.of("key", "value");
            
            FileMetadata metadata1 = new FileMetadata(
                "test.txt", "text/plain", 1024L, now, now, "abc123", additionalMetadata
            );
            FileMetadata metadata2 = new FileMetadata(
                "test.txt", "text/plain", 1024L, now, now, "abc123", additionalMetadata
            );

            // When & Then
            assertThat(metadata1).isEqualTo(metadata2);
            assertThat(metadata1.hashCode()).isEqualTo(metadata2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to different FileMetadata")
        void shouldNotBeEqualToDifferentFileMetadata() {
            // Given
            Instant now = Instant.now();
            
            FileMetadata metadata1 = new FileMetadata(
                "test1.txt", "text/plain", 1024L, now, now, "abc123", null
            );
            FileMetadata metadata2 = new FileMetadata(
                "test2.txt", "text/plain", 1024L, now, now, "abc123", null
            );

            // When & Then
            assertThat(metadata1).isNotEqualTo(metadata2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, Instant.now(), Instant.now(), "abc123", null
            );

            // When & Then
            assertThat(metadata).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, Instant.now(), Instant.now(), "abc123", null
            );

            // When & Then
            assertThat(metadata).isNotEqualTo("not a FileMetadata");
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should include all fields in toString")
        void shouldIncludeAllFieldsInToString() {
            // Given
            String fileName = "test.txt";
            String contentType = "text/plain";
            long size = 1024L;
            Instant creationTime = Instant.now();
            Instant lastModifiedTime = Instant.now();
            String checksum = "abc123";
            Map<String, String> additionalMetadata = Map.of("key", "value");
            
            FileMetadata metadata = new FileMetadata(
                fileName, contentType, size, creationTime, 
                lastModifiedTime, checksum, additionalMetadata
            );

            // When
            String toString = metadata.toString();

            // Then
            assertThat(toString).contains(fileName);
            assertThat(toString).contains(contentType);
            assertThat(toString).contains(String.valueOf(size));
            assertThat(toString).contains(checksum);
        }
    }

    @Nested
    @DisplayName("Additional Metadata Tests")
    class AdditionalMetadataTests {

        @Test
        @DisplayName("Should handle empty additional metadata")
        void shouldHandleEmptyAdditionalMetadata() {
            // Given
            Map<String, String> emptyMetadata = new HashMap<>();
            
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, Instant.now(), 
                Instant.now(), "abc123", emptyMetadata
            );

            // When & Then
            assertThat(metadata.properties()).isEqualTo(emptyMetadata);
            assertThat(metadata.properties()).isEmpty();
        }

        @Test
        @DisplayName("Should handle multiple additional metadata entries")
        void shouldHandleMultipleAdditionalMetadataEntries() {
            // Given
            Map<String, String> additionalMetadata = new HashMap<>();
            additionalMetadata.put("key1", "value1");
            additionalMetadata.put("key2", "value2");
            additionalMetadata.put("key3", "value3");
            
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, Instant.now(), 
                Instant.now(), "abc123", additionalMetadata
            );

            // When & Then
            assertThat(metadata.properties()).hasSize(3);
            assertThat(metadata.properties()).containsEntry("key1", "value1");
            assertThat(metadata.properties()).containsEntry("key2", "value2");
            assertThat(metadata.properties()).containsEntry("key3", "value3");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle zero size")
        void shouldHandleZeroSize() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "empty.txt", "text/plain", 0L, Instant.now(), 
                Instant.now(), "abc123", null
            );

            // When & Then
            assertThat(metadata.size()).isEqualTo(0L);
        }

        @Test
        @DisplayName("Should handle negative size")
        void shouldHandleNegativeSize() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", -1L, Instant.now(), 
                Instant.now(), "abc123", null
            );

            // When & Then
            assertThat(metadata.size()).isEqualTo(-1L);
        }

        @Test
        @DisplayName("Should handle very large size")
        void shouldHandleVeryLargeSize() {
            // Given
            long largeSize = Long.MAX_VALUE;
            FileMetadata metadata = new FileMetadata(
                "large.txt", "application/octet-stream", largeSize, 
                Instant.now(), Instant.now(), "abc123", null
            );

            // When & Then
            assertThat(metadata.size()).isEqualTo(largeSize);
        }
    }
}
