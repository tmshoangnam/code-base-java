package io.github.base.file.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for FileMetadata.
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
            Instant createdAt = Instant.now();
            Instant lastModified = Instant.now();
            String checksum = "abc123";
            Map<String, String> properties = Map.of("key1", "value1", "key2", "value2");

            // When
            FileMetadata result = new FileMetadata(
                fileName, contentType, size, createdAt, lastModified, checksum, properties
            );

            // Then
            assertThat(result.fileName()).isEqualTo(fileName);
            assertThat(result.contentType()).isEqualTo(contentType);
            assertThat(result.size()).isEqualTo(size);
            assertThat(result.createdAt()).isEqualTo(createdAt);
            assertThat(result.lastModified()).isEqualTo(lastModified);
            assertThat(result.checksum()).isEqualTo(checksum);
            assertThat(result.properties()).isEqualTo(properties);
        }

        @Test
        @DisplayName("Should create FileMetadata with minimal parameters")
        void shouldCreateFileMetadataWithMinimalParameters() {
            // Given
            String fileName = "test.txt";
            String contentType = "text/plain";
            long size = 1024L;

            // When
            FileMetadata result = new FileMetadata(
                fileName, contentType, size, null, null, null, null
            );

            // Then
            assertThat(result.fileName()).isEqualTo(fileName);
            assertThat(result.contentType()).isEqualTo(contentType);
            assertThat(result.size()).isEqualTo(size);
            assertThat(result.createdAt()).isNull();
            assertThat(result.lastModified()).isNull();
            assertThat(result.checksum()).isNull();
            assertThat(result.properties()).isNull();
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {

        @Test
        @DisplayName("Should create FileMetadata using of method with minimal parameters")
        void shouldCreateFileMetadataUsingOfMethodWithMinimalParameters() {
            // Given
            String fileName = "test.txt";
            String contentType = "text/plain";
            long size = 1024L;

            // When
            FileMetadata result = FileMetadata.of(fileName, contentType, size);

            // Then
            assertThat(result.fileName()).isEqualTo(fileName);
            assertThat(result.contentType()).isEqualTo(contentType);
            assertThat(result.size()).isEqualTo(size);
            assertThat(result.createdAt()).isNotNull();
            assertThat(result.lastModified()).isNotNull();
            assertThat(result.checksum()).isNull();
            assertThat(result.properties()).isNotNull();
            assertThat(result.properties()).isEmpty();
        }

        @Test
        @DisplayName("Should create FileMetadata using of method with timestamps")
        void shouldCreateFileMetadataUsingOfMethodWithTimestamps() {
            // Given
            String fileName = "test.txt";
            String contentType = "text/plain";
            long size = 1024L;
            Instant createdAt = Instant.now().minusSeconds(3600);
            Instant lastModified = Instant.now();

            // When
            FileMetadata result = FileMetadata.of(fileName, contentType, size, createdAt, lastModified);

            // Then
            assertThat(result.fileName()).isEqualTo(fileName);
            assertThat(result.contentType()).isEqualTo(contentType);
            assertThat(result.size()).isEqualTo(size);
            assertThat(result.createdAt()).isEqualTo(createdAt);
            assertThat(result.lastModified()).isEqualTo(lastModified);
            assertThat(result.checksum()).isNull();
            assertThat(result.properties()).isNotNull();
            assertThat(result.properties()).isEmpty();
        }

        @Test
        @DisplayName("Should create FileMetadata using of method with checksum")
        void shouldCreateFileMetadataUsingOfMethodWithChecksum() {
            // Given
            String fileName = "test.txt";
            String contentType = "text/plain";
            long size = 1024L;
            String checksum = "abc123";

            // When
            FileMetadata result = FileMetadata.of(fileName, contentType, size, checksum);

            // Then
            assertThat(result.fileName()).isEqualTo(fileName);
            assertThat(result.contentType()).isEqualTo(contentType);
            assertThat(result.size()).isEqualTo(size);
            assertThat(result.createdAt()).isNotNull();
            assertThat(result.lastModified()).isNotNull();
            assertThat(result.checksum()).isEqualTo(checksum);
            assertThat(result.properties()).isNotNull();
            assertThat(result.properties()).isEmpty();
        }

        @Test
        @DisplayName("Should throw exception when fileName is null")
        void shouldThrowExceptionWhenFileNameIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileMetadata.of(null, "text/plain", 1024L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File name cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when fileName is empty")
        void shouldThrowExceptionWhenFileNameIsEmpty() {
            // When & Then
            assertThatThrownBy(() -> FileMetadata.of("", "text/plain", 1024L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File name cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when fileName is whitespace")
        void shouldThrowExceptionWhenFileNameIsWhitespace() {
            // When & Then
            assertThatThrownBy(() -> FileMetadata.of("   ", "text/plain", 1024L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File name cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when size is negative")
        void shouldThrowExceptionWhenSizeIsNegative() {
            // When & Then
            assertThatThrownBy(() -> FileMetadata.of("test.txt", "text/plain", -1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File size cannot be negative");
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("Should get property value")
        void shouldGetPropertyValue() {
            // Given
            Map<String, String> properties = Map.of("key1", "value1", "key2", "value2");
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, properties
            );

            // When & Then
            assertThat(metadata.getProperty("key1")).isEqualTo("value1");
            assertThat(metadata.getProperty("key2")).isEqualTo("value2");
            assertThat(metadata.getProperty("nonexistent")).isNull();
        }

        @Test
        @DisplayName("Should check if property exists")
        void shouldCheckIfPropertyExists() {
            // Given
            Map<String, String> properties = Map.of("key1", "value1");
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, properties
            );

            // When & Then
            assertThat(metadata.hasProperty("key1")).isTrue();
            assertThat(metadata.hasProperty("nonexistent")).isFalse();
        }

        @Test
        @DisplayName("Should get file extension")
        void shouldGetFileExtension() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, null
            );

            // When & Then
            assertThat(metadata.getFileExtension()).isEqualTo("txt");
        }

        @Test
        @DisplayName("Should return empty string for file without extension")
        void shouldReturnEmptyStringForFileWithoutExtension() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test", "text/plain", 1024L, null, null, null, null
            );

            // When & Then
            assertThat(metadata.getFileExtension()).isEmpty();
        }

        @Test
        @DisplayName("Should check if file has specific extension")
        void shouldCheckIfFileHasSpecificExtension() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, null
            );

            // When & Then
            assertThat(metadata.hasExtension("txt")).isTrue();
            assertThat(metadata.hasExtension("TXT")).isTrue();
            assertThat(metadata.hasExtension("pdf")).isFalse();
        }

        @Test
        @DisplayName("Should get file name without extension")
        void shouldGetFileNameWithoutExtension() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, null
            );

            // When & Then
            assertThat(metadata.getFileNameWithoutExtension()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should get file name without extension for file without extension")
        void shouldGetFileNameWithoutExtensionForFileWithoutExtension() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test", "text/plain", 1024L, null, null, null, null
            );

            // When & Then
            assertThat(metadata.getFileNameWithoutExtension()).isEqualTo("test");
        }
    }

    @Nested
    @DisplayName("With Methods Tests")
    class WithMethodsTests {

        @Test
        @DisplayName("Should create copy with updated properties")
        void shouldCreateCopyWithUpdatedProperties() {
            // Given
            FileMetadata original = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, Map.of("key1", "value1")
            );
            Map<String, String> newProperties = Map.of("key2", "value2", "key3", "value3");

            // When
            FileMetadata updated = original.withProperties(newProperties);

            // Then
            assertThat(updated.fileName()).isEqualTo(original.fileName());
            assertThat(updated.contentType()).isEqualTo(original.contentType());
            assertThat(updated.size()).isEqualTo(original.size());
            assertThat(updated.createdAt()).isEqualTo(original.createdAt());
            assertThat(updated.lastModified()).isEqualTo(original.lastModified());
            assertThat(updated.checksum()).isEqualTo(original.checksum());
            assertThat(updated.properties()).isEqualTo(newProperties);
            assertThat(updated.properties()).isNotSameAs(original.properties());
        }

        @Test
        @DisplayName("Should create copy with updated checksum")
        void shouldCreateCopyWithUpdatedChecksum() {
            // Given
            FileMetadata original = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, "old-checksum", null
            );
            String newChecksum = "new-checksum";

            // When
            FileMetadata updated = original.withChecksum(newChecksum);

            // Then
            assertThat(updated.fileName()).isEqualTo(original.fileName());
            assertThat(updated.contentType()).isEqualTo(original.contentType());
            assertThat(updated.size()).isEqualTo(original.size());
            assertThat(updated.createdAt()).isEqualTo(original.createdAt());
            assertThat(updated.lastModified()).isEqualTo(original.lastModified());
            assertThat(updated.checksum()).isEqualTo(newChecksum);
            assertThat(updated.properties()).isEqualTo(original.properties());
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields are same")
        void shouldBeEqualWhenAllFieldsAreSame() {
            // Given
            String fileName = "test.txt";
            String contentType = "text/plain";
            long size = 1024L;
            Instant createdAt = Instant.now();
            Instant lastModified = Instant.now();
            String checksum = "abc123";
            Map<String, String> properties = Map.of("key1", "value1");

            FileMetadata metadata1 = new FileMetadata(
                fileName, contentType, size, createdAt, lastModified, checksum, properties
            );
            FileMetadata metadata2 = new FileMetadata(
                fileName, contentType, size, createdAt, lastModified, checksum, properties
            );

            // When & Then
            assertThat(metadata1).isEqualTo(metadata2);
            assertThat(metadata1.hashCode()).hasSameHashCodeAs(metadata2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when fileName differs")
        void shouldNotBeEqualWhenFileNameDiffers() {
            // Given
            FileMetadata metadata1 = new FileMetadata(
                "test1.txt", "text/plain", 1024L, null, null, null, null
            );
            FileMetadata metadata2 = new FileMetadata(
                "test2.txt", "text/plain", 1024L, null, null, null, null
            );

            // When & Then
            assertThat(metadata1).isNotEqualTo(metadata2);
        }

        @Test
        @DisplayName("Should not be equal when size differs")
        void shouldNotBeEqualWhenSizeDiffers() {
            // Given
            FileMetadata metadata1 = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, null
            );
            FileMetadata metadata2 = new FileMetadata(
                "test.txt", "text/plain", 2048L, null, null, null, null
            );

            // When & Then
            assertThat(metadata1).isNotEqualTo(metadata2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should include fileName in toString")
        void shouldIncludeFileNameInToString() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, null
            );

            // When
            String result = metadata.toString();

            // Then
            assertThat(result).contains("test.txt");
        }

        @Test
        @DisplayName("Should include all fields in toString")
        void shouldIncludeAllFieldsInToString() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, "abc123", Map.of("key1", "value1")
            );

            // When
            String result = metadata.toString();

            // Then
            assertThat(result).contains("test.txt")
                    .contains("text/plain")
                    .contains("1024")
                    .contains("abc123")
                    .contains("key1=value1");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null properties")
        void shouldHandleNullProperties() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, null
            );

            // When & Then
            assertThat(metadata.getProperty("key1")).isNull();
            assertThat(metadata.hasProperty("key1")).isFalse();
        }

        @Test
        @DisplayName("Should handle empty properties")
        void shouldHandleEmptyProperties() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.txt", "text/plain", 1024L, null, null, null, Map.of()
            );

            // When & Then
            assertThat(metadata.getProperty("key1")).isNull();
            assertThat(metadata.hasProperty("key1")).isFalse();
        }

        @Test
        @DisplayName("Should handle file with multiple dots")
        void shouldHandleFileWithMultipleDots() {
            // Given
            FileMetadata metadata = new FileMetadata(
                "test.backup.txt", "text/plain", 1024L, null, null, null, null
            );

            // When & Then
            assertThat(metadata.getFileExtension()).isEqualTo("txt");
            assertThat(metadata.getFileNameWithoutExtension()).isEqualTo("test.backup");
        }

        @Test
        @DisplayName("Should handle file starting with dot")
        void shouldHandleFileStartingWithDot() {
            // Given
            FileMetadata metadata = new FileMetadata(
                ".hidden", "text/plain", 1024L, null, null, null, null
            );

            // When & Then
            assertThat(metadata.getFileExtension()).isEqualTo("hidden");
            assertThat(metadata.getFileNameWithoutExtension()).isEmpty();
        }
    }
}
