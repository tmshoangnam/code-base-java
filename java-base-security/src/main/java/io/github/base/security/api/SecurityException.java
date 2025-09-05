package io.github.base.security.api;

/**
 * Base exception for all security-related errors.
 */
public class SecurityException extends RuntimeException {
    
    private final String errorCode;
    private final Object[] args;
    
    /**
     * Creates a new security exception with the specified message.
     *
     * @param message the exception message
     */
    public SecurityException(String message) {
        super(message);
        this.errorCode = null;
        this.args = null;
    }
    
    /**
     * Creates a new security exception with the specified message and cause.
     *
     * @param message the exception message
     * @param cause the cause of the exception
     */
    public SecurityException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.args = null;
    }
    
    /**
     * Creates a new security exception with the specified error code and message.
     *
     * @param errorCode the error code
     * @param message the exception message
     */
    public SecurityException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.args = null;
    }
    
    /**
     * Creates a new security exception with the specified error code, message, and cause.
     *
     * @param errorCode the error code
     * @param message the exception message
     * @param cause the cause of the exception
     */
    public SecurityException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = null;
    }
    
    /**
     * Creates a new security exception with the specified error code, message, and arguments.
     *
     * @param errorCode the error code
     * @param message the exception message
     * @param args the message arguments
     */
    public SecurityException(String errorCode, String message, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }
    
    /**
     * Creates a new security exception with the specified error code, message, cause, and arguments.
     *
     * @param errorCode the error code
     * @param message the exception message
     * @param cause the cause of the exception
     * @param args the message arguments
     */
    public SecurityException(String errorCode, String message, Throwable cause, Object... args) {
        super(message, cause);
        this.errorCode = errorCode;
        this.args = args;
    }
    
    /**
     * Gets the error code.
     *
     * @return the error code, or null if not set
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Gets the message arguments.
     *
     * @return the message arguments, or null if not set
     */
    public Object[] getArgs() {
        return args;
    }
    
    /**
     * Checks if this exception has an error code.
     *
     * @return true if this exception has an error code, false otherwise
     */
    public boolean hasErrorCode() {
        return errorCode != null;
    }
    
    /**
     * Checks if this exception has message arguments.
     *
     * @return true if this exception has message arguments, false otherwise
     */
    public boolean hasArgs() {
        return args != null && args.length > 0;
    }
}
