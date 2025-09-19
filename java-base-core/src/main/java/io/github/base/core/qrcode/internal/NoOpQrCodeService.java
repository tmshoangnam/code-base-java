package io.github.base.core.qrcode.internal;

import io.github.base.core.qrcode.api.QrCodeException;
import io.github.base.core.qrcode.api.QrCodeFormat;
import io.github.base.core.qrcode.api.ErrorCodes;
import io.github.base.core.qrcode.model.QrCodeRequest;
import io.github.base.core.qrcode.model.QrCodeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * No-operation QR Code service implementation.
 * 
 * <p>This service provides a fallback implementation that throws clear error messages
 * when QR Code functionality is not available due to missing dependencies.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public class NoOpQrCodeService implements io.github.base.core.qrcode.api.QrCodeService {
    
    private static final Logger logger = LoggerFactory.getLogger(NoOpQrCodeService.class);
    
    private static final String ERROR_MESSAGE = "QR Code functionality is not available. " +
            "Please add ZXing dependencies or provide a custom QR Code provider implementation.";
    
    @Override
    public QrCodeResult encode(QrCodeRequest request) throws QrCodeException {
        logger.error("QR Code encoding requested but no QR Code provider is available");
        throw new QrCodeException(ErrorCodes.SERVICE_UNAVAILABLE, ERROR_MESSAGE);
    }
    
    @Override
    public String decode(byte[] imageBytes) throws QrCodeException {
        logger.error("QR Code decoding requested but no QR Code provider is available");
        throw new QrCodeException(ErrorCodes.SERVICE_UNAVAILABLE, ERROR_MESSAGE);
    }
    
    @Override
    public boolean supportsFormat(QrCodeFormat format) {
        return false; // No formats are supported
    }
    
    @Override
    public String getServiceName() {
        return "no-op";
    }
}
