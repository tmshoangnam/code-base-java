package io.github.base.core.qrcode.api;

import io.github.base.core.qrcode.model.QrCodeRequest;
import io.github.base.core.qrcode.model.QrCodeResult;

/**
 * Contract for QR Code encoding and decoding operations.
 * 
 * <p>This interface defines the standard API for QR Code operations in a framework-agnostic way.
 * Implementations should be provided through the SPI mechanism via {@link io.github.base.core.qrcode.spi.QrCodeProvider}.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * QrCodeRequest request = QrCodeRequest.builder()
 *     .content("https://example.com")
 *     .size(300)
 *     .build();
 * 
 * QrCodeService service = QrCodeServices.loadDefault();
 * QrCodeResult result = service.encode(request);
 * }</pre>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public interface QrCodeService {
    
    /**
     * Encodes the given content into a QR Code image.
     * 
     * @param request the QR Code request containing content and options
     * @return the encoded QR Code result with image data
     * @throws QrCodeException if encoding fails due to invalid input or internal error
     * @throws IllegalArgumentException if request is null or invalid
     */
    QrCodeResult encode(QrCodeRequest request) throws QrCodeException;
    
    /**
     * Decodes a QR Code from the given image bytes.
     * 
     * @param imageBytes the image bytes containing the QR Code
     * @return the decoded text content from the QR Code
     * @throws QrCodeException if decoding fails due to invalid image or internal error
     * @throws IllegalArgumentException if imageBytes is null or empty
     */
    String decode(byte[] imageBytes) throws QrCodeException;
    
    /**
     * Checks if this service supports the given format for encoding.
     * 
     * @param format the format to check
     * @return true if the format is supported, false otherwise
     */
    boolean supportsFormat(QrCodeFormat format);
    
    /**
     * Gets the name of this QR Code service implementation.
     * 
     * @return the service name
     */
    String getServiceName();
}
