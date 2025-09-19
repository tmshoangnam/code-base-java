package io.github.base.core.qrcode.api;

import io.github.base.core.exception.BaseException;

/**
 * Exception thrown when QR Code operations fail.
 * 
 * <p>This exception provides detailed error information for QR Code encoding and decoding failures.
 * It extends the base exception framework to ensure consistent error handling across the application.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public class QrCodeException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new QR Code exception with the specified error code and message.
     * 
     * @param errorCode the error code
     * @param message the detail message
     */
    public QrCodeException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * Constructs a new QR Code exception with the specified error code, message, and cause.
     * 
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public QrCodeException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * Constructs a new QR Code exception with the specified error code, message, cause, and context.
     * 
     * @param errorCode the error code
     * @param message the detail message
     * @param cause the cause of this exception
     * @param context additional context information
     */
    public QrCodeException(String errorCode, String message, Throwable cause, Object context) {
        super(errorCode, message, java.util.Map.of("context", context), cause);
    }
}
