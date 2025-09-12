package io.github.base.file.api.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for FileType.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("FileType Tests")
class FileTypeTest {

    @Nested
    @DisplayName("Value Tests")
    class ValueTests {

        @Test
        @DisplayName("Should have correct enum values")
        void shouldHaveCorrectEnumValues() {
            // When
            FileType[] values = FileType.values();

            // Then
            assertThat(values).containsExactly(
                FileType.FILE,
                FileType.BYTE,
                FileType.STREAM
            );
        }

        @Test
        @DisplayName("Should have correct string representations")
        void shouldHaveCorrectStringRepresentations() {
            // When & Then
            assertThat(FileType.FILE.toString()).isEqualTo("FILE")
                    .isEqualTo("BYTE")
                    .isEqualTo("STREAM");
        }
    }

    @Nested
    @DisplayName("ValueOf Tests")
    class ValueOfTests {

        @Test
        @DisplayName("Should return correct enum for valid string")
        void shouldReturnCorrectEnumForValidString() {
            // When & Then
            assertThat(FileType.valueOf("FILE")).isEqualTo(FileType.FILE);
            assertThat(FileType.valueOf("BYTE")).isEqualTo(FileType.BYTE);
            assertThat(FileType.valueOf("STREAM")).isEqualTo(FileType.STREAM);
        }

        @Test
        @DisplayName("Should throw exception for invalid string")
        void shouldThrowExceptionForInvalidString() {
            // When & Then
            assertThatThrownBy(() -> FileType.valueOf("INVALID"))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw exception for null string")
        void shouldThrowExceptionForNullString() {
            // When & Then
            assertThatThrownBy(() -> FileType.valueOf(null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            // When & Then
            assertThat(FileType.FILE).isEqualTo(FileType.FILE);
            assertThat(FileType.BYTE).isEqualTo(FileType.BYTE);
            assertThat(FileType.STREAM).isEqualTo(FileType.STREAM);
        }

        @Test
        @DisplayName("Should not be equal to different enum")
        void shouldNotBeEqualToDifferentEnum() {
            // When & Then
            assertThat(FileType.FILE).isNotEqualTo(FileType.BYTE);
            assertThat(FileType.BYTE).isNotEqualTo(FileType.STREAM);
            assertThat(FileType.STREAM).isNotEqualTo(FileType.FILE);
        }
    }

    @Nested
    @DisplayName("HashCode Tests")
    class HashCodeTests {

        @Test
        @DisplayName("Should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            // When
            int hashCode1 = FileType.FILE.hashCode();
            int hashCode2 = FileType.FILE.hashCode();

            // Then
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        @DisplayName("Should have different hashCode for different enums")
        void shouldHaveDifferentHashCodeForDifferentEnums() {
            // When
            int fileHashCode = FileType.FILE.hashCode();
            int byteHashCode = FileType.BYTE.hashCode();
            int streamHashCode = FileType.STREAM.hashCode();

            // Then
            assertThat(fileHashCode).isNotEqualTo(byteHashCode);
            assertThat(byteHashCode).isNotEqualTo(streamHashCode);
            assertThat(streamHashCode).isNotEqualTo(fileHashCode);
        }
    }

    @Nested
    @DisplayName("Ordinal Tests")
    class OrdinalTests {

        @Test
        @DisplayName("Should have correct ordinal values")
        void shouldHaveCorrectOrdinalValues() {
            // When & Then
            assertThat(FileType.FILE.ordinal()).isZero();
            assertThat(FileType.BYTE.ordinal()).isEqualTo(1);
            assertThat(FileType.STREAM.ordinal()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Name Tests")
    class NameTests {

        @Test
        @DisplayName("Should have correct name")
        void shouldHaveCorrectName() {
            // When & Then
            assertThat(FileType.FILE.name()).isEqualTo("FILE");
            assertThat(FileType.BYTE.name()).isEqualTo("BYTE");
            assertThat(FileType.STREAM.name()).isEqualTo("STREAM");
        }
    }
}
