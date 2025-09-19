package io.github.base.core.qrcode.internal;

import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.spi.QrCodeProvider;
import io.github.base.core.qrcode.spi.QrCodeProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * No-operation QR Code provider for cases where no QR Code libraries are available.
 * 
 * <p>This provider serves as a fallback when no other QR Code providers are available.
 * It provides a clear error message indicating that QR Code functionality is not available.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public class NoOpQrCodeProvider implements QrCodeProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(NoOpQrCodeProvider.class);
    
    @Override
    public String getName() {
        return "no-op";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public String getDescription() {
        return "No-operation QR Code provider - indicates QR Code functionality is not available";
    }
    
    @Override
    public QrCodeService createService() throws QrCodeProviderException {
        logger.warn("Creating NoOp QR Code service - QR Code functionality is not available");
        return new NoOpQrCodeService();
    }
    
    @Override
    public boolean isAvailable() {
        // This provider is always available as a fallback
        return true;
    }
    
    @Override
    public int getPriority() {
        return 0; // Lowest priority - only used when no other providers are available
    }
}
