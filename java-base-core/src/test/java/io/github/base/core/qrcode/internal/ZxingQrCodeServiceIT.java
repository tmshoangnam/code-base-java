package io.github.base.core.qrcode.internal;

import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.api.QrCodeFormat;
import io.github.base.core.qrcode.api.QrCodeException;
import io.github.base.core.qrcode.model.QrCodeRequest;
import io.github.base.core.qrcode.model.QrCodeResult;
import io.github.base.core.qrcode.model.ErrorCorrectionLevel;
import io.github.base.core.qrcode.model.QrCodeOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.awt.Color;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for ZxingQrCodeService.
 * 
 * <p>These tests verify the actual functionality of the ZXing-based QR Code service
 * with real QR Code generation and decoding operations.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
@DisplayName("ZxingQrCodeService Integration Tests")
class ZxingQrCodeServiceIT {
    
    private QrCodeService qrCodeService;
    
    @BeforeEach
    void setUp() {
        qrCodeService = new ZxingQrCodeService();
    }
    
    @Nested
    @DisplayName("Encode Tests")
    class EncodeTests {
        
        @Test
        @DisplayName("Should encode simple text to PNG")
        void shouldEncodeSimpleTextToPng() throws QrCodeException {
            // Given
            QrCodeRequest request = QrCodeRequest.builder()
                .content("Hello, World!")
                .format(QrCodeFormat.PNG)
                .size(300)
                .build();
            
            // When
            QrCodeResult result = qrCodeService.encode(request);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFormat()).isEqualTo(QrCodeFormat.PNG);
            assertThat(result.getMimeType()).isEqualTo("image/png");
            assertThat(result.getWidth()).isEqualTo(300);
            assertThat(result.getHeight()).isEqualTo(300);
            assertThat(result.getBytes()).isNotEmpty();
            assertThat(result.getBase64String()).isNotNull();
        }
        
        @Test
        @DisplayName("Should encode URL to JPEG")
        void shouldEncodeUrlToJpeg() throws QrCodeException {
            // Given
            QrCodeRequest request = QrCodeRequest.builder()
                .content("https://example.com")
                .format(QrCodeFormat.JPEG)
                .size(400)
                .margin(8)
                .errorCorrectionLevel(ErrorCorrectionLevel.H)
                .build();
            
            // When
            QrCodeResult result = qrCodeService.encode(request);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFormat()).isEqualTo(QrCodeFormat.JPEG);
            assertThat(result.getMimeType()).isEqualTo("image/jpeg");
            assertThat(result.getWidth()).isEqualTo(400);
            assertThat(result.getHeight()).isEqualTo(400);
            assertThat(result.getBytes()).isNotEmpty();
        }
        
        @Test
        @DisplayName("Should encode with custom options")
        void shouldEncodeWithCustomOptions() throws QrCodeException {
            // Given
            QrCodeOptions options = QrCodeOptions.builder()
                .foregroundColor(Color.BLUE)
                .backgroundColor(Color.YELLOW)
                .build();
            
            QrCodeRequest request = QrCodeRequest.builder()
                .content("Custom colored QR Code")
                .format(QrCodeFormat.PNG)
                .size(300)
                .options(options)
                .build();
            
            // When
            QrCodeResult result = qrCodeService.encode(request);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFormat()).isEqualTo(QrCodeFormat.PNG);
            assertThat(result.getBytes()).isNotEmpty();
        }
        
        @Test
        @DisplayName("Should throw exception for empty content")
        void shouldThrowExceptionForEmptyContent() {
            // Given
            QrCodeRequest request = QrCodeRequest.builder()
                .content("")
                .format(QrCodeFormat.PNG)
                .size(300)
                .build();
            
            // When & Then
            assertThatThrownBy(() -> qrCodeService.encode(request))
                .isInstanceOf(QrCodeException.class)
                .hasMessageContaining("Content cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception for unsupported format")
        void shouldThrowExceptionForUnsupportedFormat() {
            // Given
            QrCodeRequest request = QrCodeRequest.builder()
                .content("test")
                .format(QrCodeFormat.SVG)
                .size(300)
                .build();
            
            // When & Then
            assertThatThrownBy(() -> qrCodeService.encode(request))
                .isInstanceOf(QrCodeException.class)
                .hasMessageContaining("Unsupported format");
        }
    }
    
    @Nested
    @DisplayName("Decode Tests")
    class DecodeTests {
        
        @Test
        @DisplayName("Should decode QR Code successfully")
        void shouldDecodeQrCodeSuccessfully() throws QrCodeException {
            // Given - First encode a QR Code
            QrCodeRequest request = QrCodeRequest.builder()
                .content("Decode test content")
                .format(QrCodeFormat.PNG)
                .size(300)
                .build();
            
            QrCodeResult encodedResult = qrCodeService.encode(request);
            
            // When - Decode the QR Code
            String decodedContent = qrCodeService.decode(encodedResult.getBytes());
            
            // Then
            assertThat(decodedContent).isEqualTo("Decode test content");
        }
        
        @Test
        @DisplayName("Should throw exception for invalid image data")
        void shouldThrowExceptionForInvalidImageData() {
            // Given
            byte[] invalidImageData = "not-an-image".getBytes();
            
            // When & Then
            assertThatThrownBy(() -> qrCodeService.decode(invalidImageData))
                .isInstanceOf(QrCodeException.class)
                .hasMessageContaining("Invalid image data");
        }
        
        @Test
        @DisplayName("Should throw exception for null image data")
        void shouldThrowExceptionForNullImageData() {
            // When & Then
            assertThatThrownBy(() -> qrCodeService.decode(null))
                .isInstanceOf(QrCodeException.class)
                .hasMessageContaining("Image bytes cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception for empty image data")
        void shouldThrowExceptionForEmptyImageData() {
            // When & Then
            assertThatThrownBy(() -> qrCodeService.decode(new byte[0]))
                .isInstanceOf(QrCodeException.class)
                .hasMessageContaining("Image bytes cannot be null or empty");
        }
    }
    
    @Nested
    @DisplayName("Format Support Tests")
    class FormatSupportTests {
        
        @Test
        @DisplayName("Should support PNG format")
        void shouldSupportPngFormat() {
            // When & Then
            assertThat(qrCodeService.supportsFormat(QrCodeFormat.PNG)).isTrue();
        }
        
        @Test
        @DisplayName("Should support JPEG format")
        void shouldSupportJpegFormat() {
            // When & Then
            assertThat(qrCodeService.supportsFormat(QrCodeFormat.JPEG)).isTrue();
        }
        
        @Test
        @DisplayName("Should support BASE64 format")
        void shouldSupportBase64Format() {
            // When & Then
            assertThat(qrCodeService.supportsFormat(QrCodeFormat.BASE64)).isTrue();
        }
        
        @Test
        @DisplayName("Should not support SVG format")
        void shouldNotSupportSvgFormat() {
            // When & Then
            assertThat(qrCodeService.supportsFormat(QrCodeFormat.SVG)).isFalse();
        }
    }
    
    @Nested
    @DisplayName("Error Correction Level Tests")
    class ErrorCorrectionLevelTests {
        
        @Test
        @DisplayName("Should encode with low error correction level")
        void shouldEncodeWithLowErrorCorrectionLevel() throws QrCodeException {
            // Given
            QrCodeRequest request = QrCodeRequest.builder()
                .content("Low error correction")
                .format(QrCodeFormat.PNG)
                .size(300)
                .errorCorrectionLevel(ErrorCorrectionLevel.L)
                .build();
            
            // When
            QrCodeResult result = qrCodeService.encode(request);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getBytes()).isNotEmpty();
        }
        
        @Test
        @DisplayName("Should encode with high error correction level")
        void shouldEncodeWithHighErrorCorrectionLevel() throws QrCodeException {
            // Given
            QrCodeRequest request = QrCodeRequest.builder()
                .content("High error correction")
                .format(QrCodeFormat.PNG)
                .size(300)
                .errorCorrectionLevel(ErrorCorrectionLevel.H)
                .build();
            
            // When
            QrCodeResult result = qrCodeService.encode(request);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getBytes()).isNotEmpty();
        }
    }
    
    @Test
    @DisplayName("Should return correct service name")
    void shouldReturnCorrectServiceName() {
        // When & Then
        assertThat(qrCodeService.getServiceName()).isEqualTo("zxing");
    }
    
    @Test
    @DisplayName("Should handle large content")
    void shouldHandleLargeContent() throws QrCodeException {
        // Given
        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeContent.append("This is a very long content that should be handled by the QR Code service. ");
        }
        
        QrCodeRequest request = QrCodeRequest.builder()
            .content(largeContent.toString())
            .format(QrCodeFormat.PNG)
            .size(500)
            .build();
        
        // When
        QrCodeResult result = qrCodeService.encode(request);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBytes()).isNotEmpty();
        
        // Verify we can decode it back
        String decodedContent = qrCodeService.decode(result.getBytes());
        assertThat(decodedContent).isEqualTo(largeContent.toString());
    }
}
