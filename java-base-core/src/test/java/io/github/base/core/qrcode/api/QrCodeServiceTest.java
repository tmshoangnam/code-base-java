package io.github.base.core.qrcode.api;

import io.github.base.core.qrcode.model.QrCodeRequest;
import io.github.base.core.qrcode.model.QrCodeResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for QrCodeService interface.
 * 
 * <p>This test class verifies the contract behavior of QrCodeService implementations.
 * It uses a mock implementation to test the interface contract.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
@DisplayName("QrCodeService Contract Tests")
class QrCodeServiceTest {
    
    private QrCodeService qrCodeService;
    
    @BeforeEach
    void setUp() {
        // Create a mock implementation for testing
        qrCodeService = new QrCodeService() {
            @Override
            public QrCodeResult encode(QrCodeRequest request) throws QrCodeException {
                if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                    throw new QrCodeException(ErrorCodes.INVALID_CONTENT, "Content cannot be null or empty");
                }
                
                return QrCodeResult.builder()
                    .bytes("mock-qr-code-data".getBytes())
                    .format(request.getFormat())
                    .dimensions(300, 300)
                    .build();
            }
            
            @Override
            public String decode(byte[] imageBytes) throws QrCodeException {
                if (imageBytes == null || imageBytes.length == 0) {
                    throw new QrCodeException(ErrorCodes.INVALID_IMAGE_DATA, "Image bytes cannot be null or empty");
                }
                
                return "decoded-content";
            }
            
            @Override
            public boolean supportsFormat(QrCodeFormat format) {
                return format == QrCodeFormat.PNG || format == QrCodeFormat.JPEG;
            }
            
            @Override
            public String getServiceName() {
                return "mock-service";
            }
        };
    }
    
    @Nested
    @DisplayName("Encode Tests")
    class EncodeTests {
        
        @Test
        @DisplayName("Should encode valid content successfully")
        void shouldEncodeValidContentSuccessfully() throws QrCodeException {
            // Given
            QrCodeRequest request = QrCodeRequest.builder()
                .content("https://example.com")
                .format(QrCodeFormat.PNG)
                .size(300)
                .build();
            
            // When
            QrCodeResult result = qrCodeService.encode(request);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFormat()).isEqualTo(QrCodeFormat.PNG);
            assertThat(result.getWidth()).isEqualTo(300);
            assertThat(result.getHeight()).isEqualTo(300);
            assertThat(result.getBytes()).isNotEmpty();
        }
        
        @Test
        @DisplayName("Should throw exception for null content")
        void shouldThrowExceptionForNullContent() {
            // Given & When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder()
                .content(null)
                .format(QrCodeFormat.PNG)
                .size(300)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception for empty content")
        void shouldThrowExceptionForEmptyContent() {
            // Given & When & Then
            assertThatThrownBy(() -> QrCodeRequest.builder()
                .content("   ")
                .format(QrCodeFormat.PNG)
                .size(300)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Content cannot be null or empty");
        }
    }
    
    @Nested
    @DisplayName("Decode Tests")
    class DecodeTests {
        
        @Test
        @DisplayName("Should decode valid image bytes successfully")
        void shouldDecodeValidImageBytesSuccessfully() throws QrCodeException {
            // Given
            byte[] imageBytes = "mock-image-data".getBytes();
            
            // When
            String result = qrCodeService.decode(imageBytes);
            
            // Then
            assertThat(result).isEqualTo("decoded-content");
        }
        
        @Test
        @DisplayName("Should throw exception for null image bytes")
        void shouldThrowExceptionForNullImageBytes() {
            // When & Then
            assertThatThrownBy(() -> qrCodeService.decode(null))
                .isInstanceOf(QrCodeException.class)
                .hasMessageContaining("Image bytes cannot be null or empty");
        }
        
        @Test
        @DisplayName("Should throw exception for empty image bytes")
        void shouldThrowExceptionForEmptyImageBytes() {
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
        @DisplayName("Should not support SVG format")
        void shouldNotSupportSvgFormat() {
            // When & Then
            assertThat(qrCodeService.supportsFormat(QrCodeFormat.SVG)).isFalse();
        }
        
        @Test
        @DisplayName("Should not support BASE64 format")
        void shouldNotSupportBase64Format() {
            // When & Then
            assertThat(qrCodeService.supportsFormat(QrCodeFormat.BASE64)).isFalse();
        }
    }
    
    @Test
    @DisplayName("Should return service name")
    void shouldReturnServiceName() {
        // When & Then
        assertThat(qrCodeService.getServiceName()).isEqualTo("mock-service");
    }
}
