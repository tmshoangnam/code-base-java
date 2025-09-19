package io.github.base.core.qrcode.api;

/**
 * Common error codes for QR Code operations.
 * 
 * <p>This class defines standard error codes used throughout QR Code operations
 * to provide consistent error reporting and handling.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public final class ErrorCodes {
    
    // Private constructor to prevent instantiation
    private ErrorCodes() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // Encoding errors
    public static final String ENCODING_FAILED = "QR_CODE_ENCODING_FAILED";
    public static final String INVALID_CONTENT = "QR_CODE_INVALID_CONTENT";
    public static final String CONTENT_TOO_LONG = "QR_CODE_CONTENT_TOO_LONG";
    public static final String UNSUPPORTED_FORMAT = "QR_CODE_UNSUPPORTED_FORMAT";
    public static final String INVALID_SIZE = "QR_CODE_INVALID_SIZE";
    public static final String INVALID_MARGIN = "QR_CODE_INVALID_MARGIN";
    public static final String INVALID_ERROR_CORRECTION_LEVEL = "QR_CODE_INVALID_ERROR_CORRECTION_LEVEL";
    
    // Decoding errors
    public static final String DECODING_FAILED = "QR_CODE_DECODING_FAILED";
    public static final String INVALID_IMAGE_DATA = "QR_CODE_INVALID_IMAGE_DATA";
    public static final String NO_QR_CODE_FOUND = "QR_CODE_NOT_FOUND";
    public static final String CORRUPTED_IMAGE = "QR_CODE_CORRUPTED_IMAGE";
    public static final String UNSUPPORTED_IMAGE_FORMAT = "QR_CODE_UNSUPPORTED_IMAGE_FORMAT";
    
    // Service errors
    public static final String SERVICE_UNAVAILABLE = "QR_CODE_SERVICE_UNAVAILABLE";
    public static final String PROVIDER_NOT_FOUND = "QR_CODE_PROVIDER_NOT_FOUND";
    public static final String PROVIDER_INITIALIZATION_FAILED = "QR_CODE_PROVIDER_INITIALIZATION_FAILED";
    
    // Configuration errors
    public static final String INVALID_CONFIGURATION = "QR_CODE_INVALID_CONFIGURATION";
    public static final String MISSING_REQUIRED_OPTION = "QR_CODE_MISSING_REQUIRED_OPTION";
}
