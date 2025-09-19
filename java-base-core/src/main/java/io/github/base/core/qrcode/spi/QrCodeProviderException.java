package io.github.base.core.qrcode.spi;

import io.github.base.core.exception.BaseException;

/**
 * Exception thrown when QR Code provider operations fail.
 * 
 * <p>This exception is thrown when there are issues with QR Code provider
 * initialization, configuration, or service creation.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public class QrCodeProviderException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new QR Code provider exception with the specified error code and message.
     * 
     * @param errorCode the error code
     * @param message the detail message
     */
    public QrCodeProviderException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * Constructs a new QR Code provider exception with the specified error code, message, and cause.
     * 
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public QrCodeProviderException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * Constructs a new QR Code provider exception with the specified error code, message, cause, and context.
     * 
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public QrCodeProviderException(String errorCode, String message, Throwable cause, Object context) {
        super(errorCode, message, java.util.Map.of("context", context), cause);
    }
}
