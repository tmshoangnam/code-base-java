package io.github.base.core.qrcode.internal;

import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.spi.QrCodeProvider;
import io.github.base.core.qrcode.spi.QrCodeProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZXing-based QR Code provider implementation.
 * 
 * <p>This provider uses the ZXing library for QR Code generation and decoding.
 * It provides high-quality QR Code generation with support for multiple formats
 * and error correction levels.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public class ZxingQrCodeProvider implements QrCodeProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(ZxingQrCodeProvider.class);
    
    @Override
    public String getName() {
        return "zxing";
    }
    
    @Override
    public String getVersion() {
        return "3.5.3";
    }
    
    @Override
    public String getDescription() {
        return "ZXing-based QR Code provider with support for multiple formats and error correction levels";
    }
    
    @Override
    public QrCodeService createService() throws QrCodeProviderException {
        try {
            logger.debug("Creating ZXing QR Code service");
            return new ZxingQrCodeService();
        } catch (Exception e) {
            logger.error("Failed to create ZXing QR Code service", e);
            throw new QrCodeProviderException("ZXING_SERVICE_CREATION_FAILED", 
                    "Failed to create ZXing QR Code service", e);
        }
    }
    
    @Override
    public boolean isAvailable() {
        try {
            // Check if ZXing classes are available
            Class.forName("com.google.zxing.qrcode.QRCodeWriter");
            Class.forName("com.google.zxing.MultiFormatReader");
            Class.forName("com.google.zxing.client.j2se.MatrixToImageWriter");
            logger.debug("ZXing QR Code provider is available");
            return true;
        } catch (ClassNotFoundException e) {
            logger.debug("ZXing classes not available - provider will be skipped", e);
            return false;
        } catch (Exception e) {
            logger.debug("Error checking ZXing availability - provider will be skipped", e);
            return false;
        }
    }
    
    @Override
    public int getPriority() {
        return 100; // High priority as it's the default implementation
    }
}
