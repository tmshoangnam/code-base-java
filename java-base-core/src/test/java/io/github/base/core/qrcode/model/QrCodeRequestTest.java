package io.github.base.core.qrcode.model;

import io.github.base.core.qrcode.api.QrCodeFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for QrCodeRequest model.
 *
 * @author Base Core Team
 * @since 1.0.0
 */
@DisplayName("QrCodeRequest Tests")
class QrCodeRequestTest {

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build valid request with all fields")
        void shouldBuildValidRequestWithAllFields() {
            // Given
            QrCodeOptions options = QrCodeOptions.builder()
                .foregroundColor(java.awt.Color.BLACK)
                .backgroundColor(java.awt.Color.WHITE)
                .build();

            // When
            QrCodeRequest request = QrCodeRequest.builder()
                .content("https://example.com")
                .format(QrCodeFormat.PNG)
                .size(300)
                .margin(4)
                .errorCorrectionLevel(ErrorCorrectionLevel.M)
                .options(options)
                .build();

            // Then
            assertThat(request.getContent()).isEqualTo("https://example.com");
            assertThat(request.getFormat()).isEqualTo(QrCodeFormat.PNG);
            assertThat(request.getSize()).isEqualTo(300);
            assertThat(request.getMargin()).isEqualTo(4);
            assertThat(request.getErrorCorrectionLevel()).isEqualTo(ErrorCorrectionLevel.M);
            assertThat(request.getOptions()).isEqualTo(options);
        }

        @Test
        @DisplayName("Should build valid request with minimal fields")
        void shouldBuildValidRequestWithMinimalFields() {
            // When
            QrCodeRequest request = QrCodeRequest.builder()
                .content("test content")
                .build();

            // Then
            assertThat(request.getContent()).isEqualTo("test content");
            assertThat(request.getFormat()).isEqualTo(QrCodeFormat.PNG); // default
            assertThat(request.getSize()).isEqualTo(300); // default
            assertThat(request.getMargin()).isEqualTo(4); // default
            assertThat(request.getErrorCorrectionLevel()).isEqualTo(ErrorCorrectionLevel.M); // default
            assertThat(request.getOptions()).isNull();
        }

        @Test
        @DisplayName("Should throw exception for null content")
        void shouldThrowExceptionForNullContent() {
            // When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder().content(null).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception for empty content")
        void shouldThrowExceptionForEmptyContent() {
            // When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder().content("   ").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception for null format")
        void shouldThrowExceptionForNullFormat() {
            // When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder()
                .content("test")
                .format(null)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Format cannot be null");
        }

        @Test
        @DisplayName("Should throw exception for zero size")
        void shouldThrowExceptionForZeroSize() {
            // When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder()
                .content("test")
                .size(0)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Size must be positive");
        }

        @Test
        @DisplayName("Should throw exception for negative size")
        void shouldThrowExceptionForNegativeSize() {
            // When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder()
                .content("test")
                .size(-1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Size must be positive");
        }

        @Test
        @DisplayName("Should throw exception for negative margin")
        void shouldThrowExceptionForNegativeMargin() {
            // When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder()
                .content("test")
                .margin(-1)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Margin cannot be negative");
        }

        @Test
        @DisplayName("Should throw exception for null error correction level")
        void shouldThrowExceptionForNullErrorCorrectionLevel() {
            // When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder()
                .content("test")
                .errorCorrectionLevel(null)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error correction level cannot be null");
        }

        @Test
        @DisplayName("Should throw exception when content is not set")
        void shouldThrowExceptionWhenContentIsNotSet() {
            // When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder().build())
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields are same")
        void shouldBeEqualWhenAllFieldsAreSame() {
            // Given
            QrCodeRequest request1 = QrCodeRequest.builder()
                .content("test")
                .format(QrCodeFormat.PNG)
                .size(300)
                .margin(4)
                .errorCorrectionLevel(ErrorCorrectionLevel.M)
                .build();

            QrCodeRequest request2 = QrCodeRequest.builder()
                .content("test")
                .format(QrCodeFormat.PNG)
                .size(300)
                .margin(4)
                .errorCorrectionLevel(ErrorCorrectionLevel.M)
                .build();

            // When & Then
            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).hasSameHashCodeAs(request2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when content is different")
        void shouldNotBeEqualWhenContentIsDifferent() {
            // Given
            QrCodeRequest request1 = QrCodeRequest.builder()
                .content("test1")
                .build();

            QrCodeRequest request2 = QrCodeRequest.builder()
                .content("test2")
                .build();

            // When & Then
            assertThat(request1).isNotEqualTo(request2);
        }

        @Test
        @DisplayName("Should not be equal when format is different")
        void shouldNotBeEqualWhenFormatIsDifferent() {
            // Given
            QrCodeRequest request1 = QrCodeRequest.builder()
                .content("test")
                .format(QrCodeFormat.PNG)
                .build();

            QrCodeRequest request2 = QrCodeRequest.builder()
                .content("test")
                .format(QrCodeFormat.JPEG)
                .build();

            // When & Then
            assertThat(request1).isNotEqualTo(request2);
        }
    }

    @Test
    @DisplayName("Should have meaningful toString")
    void shouldHaveMeaningfulToString() {
        // Given
        QrCodeRequest request = QrCodeRequest.builder()
            .content("test")
            .format(QrCodeFormat.PNG)
            .size(300)
            .build();

        // When
        String toString = request.toString();

        // Then
        assertThat(toString).contains("QrCodeRequest")
        .contains("content='test'")
        .contains("format=PNG")
        .contains("size=300");
    }
}
