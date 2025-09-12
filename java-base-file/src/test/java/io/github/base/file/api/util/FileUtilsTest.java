package io.github.base.file.api.util;

import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.FileType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for FileUtils.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("FileUtils Tests")
class FileUtilsTest {

    @TempDir
    Path tempDir;

    @Nested
    @DisplayName("Generate File ID Tests")
    class GenerateFileIdTests {

        @Test
        @DisplayName("Should generate unique file ID")
        void shouldGenerateUniqueFileId() {
            // When
            String fileId1 = FileUtils.generateFileId();
            String fileId2 = FileUtils.generateFileId();

            // Then
            assertThat(fileId1).isNotNull();
            assertThat(fileId2).isNotNull();
            assertThat(fileId1).isNotEqualTo(fileId2);
            assertThat(fileId1).startsWith("file_");
        }

        @Test
        @DisplayName("Should generate file ID with custom prefix")
        void shouldGenerateFileIdWithCustomPrefix() {
            // Given
            String prefix = "custom";

            // When
            String fileId = FileUtils.generateFileId(prefix);

            // Then
            assertThat(fileId).isNotNull();
            assertThat(fileId).startsWith(prefix + "_");
        }

        @Test
        @DisplayName("Should throw exception when prefix is null")
        void shouldThrowExceptionWhenPrefixIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.generateFileId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Prefix cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when prefix is empty")
        void shouldThrowExceptionWhenPrefixIsEmpty() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.generateFileId(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Prefix cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when prefix is whitespace")
        void shouldThrowExceptionWhenPrefixIsWhitespace() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.generateFileId("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Prefix cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Calculate Checksum Tests")
    class CalculateChecksumTests {

        @Test
        @DisplayName("Should calculate checksum for byte array")
        void shouldCalculateChecksumForByteArray() {
            // Given
            byte[] data = "Hello World".getBytes();

            // When
            String checksum = FileUtils.calculateChecksum(data);

            // Then
            assertThat(checksum).isNotNull();
            assertThat(checksum).isNotEmpty();
            assertThat(checksum).hasSize(64); // SHA-256 produces 64 hex characters
        }

        @Test
        @DisplayName("Should calculate checksum for file")
        void shouldCalculateChecksumForFile() throws IOException {
            // Given
            File file = tempDir.resolve("test.txt").toFile();
            Files.write(file.toPath(), "Hello World".getBytes());

            // When
            String checksum = FileUtils.calculateChecksum(file);

            // Then
            assertThat(checksum).isNotNull();
            assertThat(checksum).isNotEmpty();
            assertThat(checksum).hasSize(64);
        }

        @Test
        @DisplayName("Should calculate checksum for input stream")
        void shouldCalculateChecksumForInputStream() {
            // Given
            InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

            // When
            String checksum = FileUtils.calculateChecksum(inputStream);

            // Then
            assertThat(checksum).isNotNull();
            assertThat(checksum).isNotEmpty();
            assertThat(checksum).hasSize(64);
        }

        @Test
        @DisplayName("Should throw exception when data is null")
        void shouldThrowExceptionWhenDataIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.calculateChecksum((byte[]) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when file is null")
        void shouldThrowExceptionWhenFileIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.calculateChecksum((File) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when file does not exist")
        void shouldThrowExceptionWhenFileDoesNotExist() {
            // Given
            File file = new File("nonexistent.txt");

            // When & Then
            assertThatThrownBy(() -> FileUtils.calculateChecksum(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File does not exist: " + file.getAbsolutePath());
        }

        @Test
        @DisplayName("Should throw exception when input stream is null")
        void shouldThrowExceptionWhenInputStreamIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.calculateChecksum((InputStream) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("InputStream cannot be null");
        }
    }

    @Nested
    @DisplayName("Create Metadata Tests")
    class CreateMetadataTests {

        @Test
        @DisplayName("Should create metadata from file")
        void shouldCreateMetadataFromFile() throws IOException {
            // Given
            File file = tempDir.resolve("test.txt").toFile();
            Files.write(file.toPath(), "Hello World".getBytes());

            // When
            FileMetadata metadata = FileUtils.createMetadata(file);

            // Then
            assertThat(metadata).isNotNull();
            assertThat(metadata.fileName()).isEqualTo("test.txt");
            assertThat(metadata.size()).isEqualTo(11L);
            assertThat(metadata.createdAt()).isNotNull();
            assertThat(metadata.lastModified()).isNotNull();
        }

        @Test
        @DisplayName("Should create metadata from byte array")
        void shouldCreateMetadataFromByteArray() {
            // Given
            byte[] data = "Hello World".getBytes();
            String fileName = "test.txt";
            String contentType = "text/plain";

            // When
            FileMetadata metadata = FileUtils.createMetadata(data, fileName, contentType);

            // Then
            assertThat(metadata).isNotNull();
            assertThat(metadata.fileName()).isEqualTo(fileName);
            assertThat(metadata.contentType()).isEqualTo(contentType);
            assertThat(metadata.size()).isEqualTo(data.length);
        }

        @Test
        @DisplayName("Should create metadata from input stream")
        void shouldCreateMetadataFromInputStream() {
            // Given
            InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
            String fileName = "test.txt";
            String contentType = "text/plain";
            long size = 11L;

            // When
            FileMetadata metadata = FileUtils.createMetadata(inputStream, fileName, contentType, size);

            // Then
            assertThat(metadata).isNotNull();
            assertThat(metadata.fileName()).isEqualTo(fileName);
            assertThat(metadata.contentType()).isEqualTo(contentType);
            assertThat(metadata.size()).isEqualTo(size);
        }

        @Test
        @DisplayName("Should throw exception when file is null")
        void shouldThrowExceptionWhenFileIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.createMetadata((File) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when file does not exist")
        void shouldThrowExceptionWhenFileDoesNotExist() {
            // Given
            File file = new File("nonexistent.txt");

            // When & Then
            assertThatThrownBy(() -> FileUtils.createMetadata(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File does not exist: " + file.getAbsolutePath());
        }

        @Test
        @DisplayName("Should throw exception when data is null")
        void shouldThrowExceptionWhenDataIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.createMetadata(null, "test.txt", "text/plain"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when fileName is null")
        void shouldThrowExceptionWhenFileNameIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.createMetadata("data".getBytes(), null, "text/plain"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File name cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when fileName is empty")
        void shouldThrowExceptionWhenFileNameIsEmpty() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.createMetadata("data".getBytes(), "", "text/plain"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File name cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when input stream is null")
        void shouldThrowExceptionWhenInputStreamIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.createMetadata(null, "test.txt", "text/plain", 11L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("InputStream cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when size is negative")
        void shouldThrowExceptionWhenSizeIsNegative() {
            // Given
            InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

            // When & Then
            assertThatThrownBy(() -> FileUtils.createMetadata(inputStream, "test.txt", "text/plain", -1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Size cannot be negative");
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate valid file ID")
        void shouldValidateValidFileId() {
            // When & Then
            assertThat(FileUtils.isValidFileId("file_123")).isTrue();
            assertThat(FileUtils.isValidFileId("custom_456")).isTrue();
            assertThat(FileUtils.isValidFileId("a".repeat(255))).isTrue();
        }

        @Test
        @DisplayName("Should reject invalid file ID")
        void shouldRejectInvalidFileId() {
            // When & Then
            assertThat(FileUtils.isValidFileId(null)).isFalse();
            assertThat(FileUtils.isValidFileId("")).isFalse();
            assertThat(FileUtils.isValidFileId("   ")).isFalse();
            assertThat(FileUtils.isValidFileId("a".repeat(256))).isFalse();
        }

        @Test
        @DisplayName("Should validate valid file name")
        void shouldValidateValidFileName() {
            // When & Then
            assertThat(FileUtils.isValidFileName("test.txt")).isTrue();
            assertThat(FileUtils.isValidFileName("document.pdf")).isTrue();
            assertThat(FileUtils.isValidFileName("file_with_underscores.txt")).isTrue();
            assertThat(FileUtils.isValidFileName("file-with-dashes.txt")).isTrue();
        }

        @Test
        @DisplayName("Should reject invalid file name")
        void shouldRejectInvalidFileName() {
            // When & Then
            assertThat(FileUtils.isValidFileName(null)).isFalse();
            assertThat(FileUtils.isValidFileName("")).isFalse();
            assertThat(FileUtils.isValidFileName("   ")).isFalse();
            assertThat(FileUtils.isValidFileName("file/with/slashes.txt")).isFalse();
            assertThat(FileUtils.isValidFileName("file\\with\\backslashes.txt")).isFalse();
            assertThat(FileUtils.isValidFileName("file:with:colons.txt")).isFalse();
            assertThat(FileUtils.isValidFileName("file*with*asterisks.txt")).isFalse();
            assertThat(FileUtils.isValidFileName("file?with?questionmarks.txt")).isFalse();
            assertThat(FileUtils.isValidFileName("file\"with\"quotes.txt")).isFalse();
            assertThat(FileUtils.isValidFileName("file<with>brackets.txt")).isFalse();
            assertThat(FileUtils.isValidFileName("file>with>brackets.txt")).isFalse();
            assertThat(FileUtils.isValidFileName("file|with|pipes.txt")).isFalse();
            assertThat(FileUtils.isValidFileName("file..with..dots.txt")).isFalse();
        }
    }

    @Nested
    @DisplayName("File Name Utility Tests")
    class FileNameUtilityTests {

        @Test
        @DisplayName("Should get file extension")
        void shouldGetFileExtension() {
            // When & Then
            assertThat(FileUtils.getFileExtension("test.txt")).isEqualTo("txt");
            assertThat(FileUtils.getFileExtension("document.pdf")).isEqualTo("pdf");
            assertThat(FileUtils.getFileExtension("image.jpg")).isEqualTo("jpg");
            assertThat(FileUtils.getFileExtension("archive.zip")).isEqualTo("zip");
        }

        @Test
        @DisplayName("Should return empty string for file without extension")
        void shouldReturnEmptyStringForFileWithoutExtension() {
            // When & Then
            assertThat(FileUtils.getFileExtension("test")).isEmpty();
            assertThat(FileUtils.getFileExtension("README")).isEmpty();
        }

        @Test
        @DisplayName("Should handle null file name")
        void shouldHandleNullFileName() {
            // When & Then
            assertThat(FileUtils.getFileExtension(null)).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty file name")
        void shouldHandleEmptyFileName() {
            // When & Then
            assertThat(FileUtils.getFileExtension("")).isEmpty();
        }

        @Test
        @DisplayName("Should handle file name with multiple dots")
        void shouldHandleFileNameWithMultipleDots() {
            // When & Then
            assertThat(FileUtils.getFileExtension("test.backup.txt")).isEqualTo("txt");
            assertThat(FileUtils.getFileExtension("file.2023.12.25.log")).isEqualTo("log");
        }

        @Test
        @DisplayName("Should handle file name starting with dot")
        void shouldHandleFileNameStartingWithDot() {
            // When & Then
            assertThat(FileUtils.getFileExtension(".hidden")).isEqualTo("hidden");
            assertThat(FileUtils.getFileExtension(".gitignore")).isEqualTo("gitignore");
        }

        @Test
        @DisplayName("Should get file name without extension")
        void shouldGetFileNameWithoutExtension() {
            // When & Then
            assertThat(FileUtils.getFileNameWithoutExtension("test.txt")).isEqualTo("test");
            assertThat(FileUtils.getFileNameWithoutExtension("document.pdf")).isEqualTo("document");
            assertThat(FileUtils.getFileNameWithoutExtension("image.jpg")).isEqualTo("image");
        }

        @Test
        @DisplayName("Should return original name for file without extension")
        void shouldReturnOriginalNameForFileWithoutExtension() {
            // When & Then
            assertThat(FileUtils.getFileNameWithoutExtension("test")).isEqualTo("test");
            assertThat(FileUtils.getFileNameWithoutExtension("README")).isEqualTo("README");
        }

        @Test
        @DisplayName("Should handle null file name for getFileNameWithoutExtension")
        void shouldHandleNullFileNameForGetFileNameWithoutExtension() {
            // When & Then
            assertThat(FileUtils.getFileNameWithoutExtension(null)).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty file name for getFileNameWithoutExtension")
        void shouldHandleEmptyFileNameForGetFileNameWithoutExtension() {
            // When & Then
            assertThat(FileUtils.getFileNameWithoutExtension("")).isEmpty();
        }
    }

    @Nested
    @DisplayName("Determine File Type Tests")
    class DetermineFileTypeTests {

        @Test
        @DisplayName("Should determine file type for File object")
        void shouldDetermineFileTypeForFileObject() {
            // Given
            File file = new File("test.txt");

            // When
            FileType fileType = FileUtils.determineFileType(file);

            // Then
            assertThat(fileType).isEqualTo(FileType.FILE);
        }

        @Test
        @DisplayName("Should determine file type for byte array")
        void shouldDetermineFileTypeForByteArray() {
            // Given
            byte[] data = "Hello World".getBytes();

            // When
            FileType fileType = FileUtils.determineFileType(data);

            // Then
            assertThat(fileType).isEqualTo(FileType.BYTE);
        }

        @Test
        @DisplayName("Should determine file type for input stream")
        void shouldDetermineFileTypeForInputStream() {
            // Given
            InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());

            // When
            FileType fileType = FileUtils.determineFileType(inputStream);

            // Then
            assertThat(fileType).isEqualTo(FileType.STREAM);
        }

        @Test
        @DisplayName("Should throw exception when input is null")
        void shouldThrowExceptionWhenInputIsNull() {
            // When & Then
            assertThatThrownBy(() -> FileUtils.determineFileType(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Input cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when input type is unsupported")
        void shouldThrowExceptionWhenInputTypeIsUnsupported() {
            // Given
            String unsupportedInput = "unsupported";

            // When & Then
            assertThatThrownBy(() -> FileUtils.determineFileType(unsupportedInput))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported input type: " + unsupportedInput.getClass().getName());
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("Should get default checksum algorithm")
        void shouldGetDefaultChecksumAlgorithm() {
            // When
            String algorithm = FileUtils.getDefaultChecksumAlgorithm();

            // Then
            assertThat(algorithm).isEqualTo("SHA-256");
        }

        @Test
        @DisplayName("Should get default buffer size")
        void shouldGetDefaultBufferSize() {
            // When
            int bufferSize = FileUtils.getDefaultBufferSize();

            // Then
            assertThat(bufferSize).isEqualTo(8192);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle empty byte array")
        void shouldHandleEmptyByteArray() {
            // Given
            byte[] data = new byte[0];

            // When
            String checksum = FileUtils.calculateChecksum(data);

            // Then
            assertThat(checksum).isNotNull();
            assertThat(checksum).isNotEmpty();
            assertThat(checksum).hasSize(64);
        }

        @Test
        @DisplayName("Should handle large byte array")
        void shouldHandleLargeByteArray() {
            // Given
            byte[] data = new byte[10000];
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) (i % 256);
            }

            // When
            String checksum = FileUtils.calculateChecksum(data);

            // Then
            assertThat(checksum).isNotNull()
                    .isNotEmpty()
                    .hasSize(64);
        }

        @Test
        @DisplayName("Should handle file name with only extension")
        void shouldHandleFileNameWithOnlyExtension() {
            // When & Then
            assertThat(FileUtils.getFileExtension(".txt")).isEqualTo("txt");
            assertThat(FileUtils.getFileNameWithoutExtension(".txt")).isEmpty();
        }

        @Test
        @DisplayName("Should handle file name ending with dot")
        void shouldHandleFileNameEndingWithDot() {
            // When & Then
            assertThat(FileUtils.getFileExtension("test.")).isEmpty();
            assertThat(FileUtils.getFileNameWithoutExtension("test.")).isEqualTo("test");
        }
    }
}
