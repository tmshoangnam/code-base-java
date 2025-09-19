package io.github.base.core.qrcode.model;

import io.github.base.core.qrcode.api.QrCodeFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for QrCodeResult model.
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
@DisplayName("QrCodeResult Tests")
class QrCodeResultTest {
    
    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {
        
        @Test
        @DisplayName("Should build valid result with all fields")
        void shouldBuildValidResultWithAllFields() {
            // Given
            byte[] testBytes = "test-qr-code-data".getBytes();
            
            // When
            QrCodeResult result = QrCodeResult.builder()
                .bytes(testBytes)
                .format(QrCodeFormat.PNG)
                .dimensions(300, 300)
                .build();
            
            // Then
            assertThat(result.getBytes()).isEqualTo(testBytes);
            assertThat(result.getFormat()).isEqualTo(QrCodeFormat.PNG);
            assertThat(result.getMimeType()).isEqualTo("image/png");
            assertThat(result.getWidth()).isEqualTo(300);
            assertThat(result.getHeight()).isEqualTo(300);
            assertThat(result.getSizeInBytes()).isEqualTo(testBytes.length);
            assertThat(result.getBase64String()).isNotNull();
        }
        
        @Test
        @DisplayName("Should build valid result with custom MIME type")
        void shouldBuildValidResultWithCustomMimeType() {
            // Given
            byte[] testBytes = "test-qr-code-data".getBytes();
            
            // When
            QrCodeResult result = QrCodeResult.builder()
                .bytes(testBytes)
                .format(QrCodeFormat.PNG)
                .mimeType("image/png-custom")
                .dimensions(300, 300)
                .build();
            
            // Then
            assertThat(result.getMimeType()).isEqualTo("image/png-custom");
        }
        
        @Test
        @DisplayName("Should throw exception for null bytes")
        void shouldThrowExceptionForNullBytes() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .bytes(null)
                .format(QrCodeFormat.PNG)
                .dimensions(300, 300)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bytes cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception for empty bytes")
        void shouldThrowExceptionForEmptyBytes() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .bytes(new byte[0])
                .format(QrCodeFormat.PNG)
                .dimensions(300, 300)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bytes cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception for null format")
        void shouldThrowExceptionForNullFormat() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(null)
                .dimensions(300, 300)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Format cannot be null");
        }
        
        @Test
        @DisplayName("Should throw exception for zero width")
        void shouldThrowExceptionForZeroWidth() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(QrCodeFormat.PNG)
                .dimensions(0, 300)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Width must be positive");
        }
        
        @Test
        @DisplayName("Should throw exception for zero height")
        void shouldThrowExceptionForZeroHeight() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(QrCodeFormat.PNG)
                .dimensions(300, 0)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Height must be positive");
        }
        
        @Test
        @DisplayName("Should throw exception for null MIME type")
        void shouldThrowExceptionForNullMimeType() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(QrCodeFormat.PNG)
                .mimeType(null)
                .dimensions(300, 300)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("MIME type cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception for empty MIME type")
        void shouldThrowExceptionForEmptyMimeType() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(QrCodeFormat.PNG)
                .mimeType("   ")
                .dimensions(300, 300)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("MIME type cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception when bytes not set")
        void shouldThrowExceptionWhenBytesNotSet() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .format(QrCodeFormat.PNG)
                .dimensions(300, 300)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Bytes are required");
        }
        
        @Test
        @DisplayName("Should throw exception when format not set")
        void shouldThrowExceptionWhenFormatNotSet() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .bytes("test".getBytes())
                .dimensions(300, 300)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Format is required");
        }
        
        @Test
        @DisplayName("Should throw exception when dimensions not set")
        void shouldThrowExceptionWhenDimensionsNotSet() {
            // When & Then
            assertThatThrownBy(() -> QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(QrCodeFormat.PNG)
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Valid dimensions are required");
        }
    }
    
    @Nested
    @DisplayName("Format Tests")
    class FormatTests {
        
        @Test
        @DisplayName("Should set correct MIME type for PNG format")
        void shouldSetCorrectMimeTypeForPngFormat() {
            // When
            QrCodeResult result = QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(QrCodeFormat.PNG)
                .dimensions(300, 300)
                .build();
            
            // Then
            assertThat(result.getMimeType()).isEqualTo("image/png");
        }
        
        @Test
        @DisplayName("Should set correct MIME type for JPEG format")
        void shouldSetCorrectMimeTypeForJpegFormat() {
            // When
            QrCodeResult result = QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(QrCodeFormat.JPEG)
                .dimensions(300, 300)
                .build();
            
            // Then
            assertThat(result.getMimeType()).isEqualTo("image/jpeg");
        }
        
        @Test
        @DisplayName("Should set correct MIME type for SVG format")
        void shouldSetCorrectMimeTypeForSvgFormat() {
            // When
            QrCodeResult result = QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(QrCodeFormat.SVG)
                .dimensions(300, 300)
                .build();
            
            // Then
            assertThat(result.getMimeType()).isEqualTo("image/svg+xml");
        }
        
        @Test
        @DisplayName("Should set correct MIME type for BASE64 format")
        void shouldSetCorrectMimeTypeForBase64Format() {
            // When
            QrCodeResult result = QrCodeResult.builder()
                .bytes("test".getBytes())
                .format(QrCodeFormat.BASE64)
                .dimensions(300, 300)
                .build();
            
            // Then
            assertThat(result.getMimeType()).isEqualTo("text/plain");
        }
    }
    
    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {
        
        @Test
        @DisplayName("Should be equal when all fields are same")
        void shouldBeEqualWhenAllFieldsAreSame() {
            // Given
            byte[] testBytes = "test".getBytes();
            
            QrCodeResult result1 = QrCodeResult.builder()
                .bytes(testBytes)
                .format(QrCodeFormat.PNG)
                .dimensions(300, 300)
                .build();
            
            QrCodeResult result2 = QrCodeResult.builder()
                .bytes(testBytes)
                .format(QrCodeFormat.PNG)
                .dimensions(300, 300)
                .build();
            
            // When & Then
            assertThat(result1).isEqualTo(result2);
            assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
        }
        
        @Test
        @DisplayName("Should not be equal when format is different")
        void shouldNotBeEqualWhenFormatIsDifferent() {
            // Given
            byte[] testBytes = "test".getBytes();
            
            QrCodeResult result1 = QrCodeResult.builder()
                .bytes(testBytes)
                .format(QrCodeFormat.PNG)
                .dimensions(300, 300)
                .build();
            
            QrCodeResult result2 = QrCodeResult.builder()
                .bytes(testBytes)
                .format(QrCodeFormat.JPEG)
                .dimensions(300, 300)
                .build();
            
            // When & Then
            assertThat(result1).isNotEqualTo(result2);
        }
        
        @Test
        @DisplayName("Should not be equal when dimensions are different")
        void shouldNotBeEqualWhenDimensionsAreDifferent() {
            // Given
            byte[] testBytes = "test".getBytes();
            
            QrCodeResult result1 = QrCodeResult.builder()
                .bytes(testBytes)
                .format(QrCodeFormat.PNG)
                .dimensions(300, 300)
                .build();
            
            QrCodeResult result2 = QrCodeResult.builder()
                .bytes(testBytes)
                .format(QrCodeFormat.PNG)
                .dimensions(400, 400)
                .build();
            
            // When & Then
            assertThat(result1).isNotEqualTo(result2);
        }
    }
    
    @Test
    @DisplayName("Should return defensive copy of bytes")
    void shouldReturnDefensiveCopyOfBytes() {
        // Given
        byte[] originalBytes = "test".getBytes();
        QrCodeResult result = QrCodeResult.builder()
            .bytes(originalBytes)
            .format(QrCodeFormat.PNG)
            .dimensions(300, 300)
            .build();
        
        // When
        byte[] returnedBytes = result.getBytes();
        returnedBytes[0] = 'X'; // Modify the returned array
        
        // Then
        assertThat(originalBytes[0]).isEqualTo((byte) 't'); // Original should be unchanged
        assertThat(result.getBytes()[0]).isEqualTo((byte) 't'); // Result should be unchanged
    }
    
    @Test
    @DisplayName("Should have meaningful toString")
    void shouldHaveMeaningfulToString() {
        // Given
        QrCodeResult result = QrCodeResult.builder()
            .bytes("test".getBytes())
            .format(QrCodeFormat.PNG)
            .dimensions(300, 300)
            .build();
        
        // When
        String toString = result.toString();
        
        // Then
        assertThat(toString).contains("QrCodeResult");
        assertThat(toString).contains("format=PNG");
        assertThat(toString).contains("width=300");
        assertThat(toString).contains("height=300");
        assertThat(toString).contains("sizeInBytes=4");
    }
}
