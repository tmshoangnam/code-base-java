package io.github.base.core.exception;

import java.util.Map;
import java.util.Objects;

/**
 * Base class for all custom exceptions in the application.
 * 
 * <p>This class provides a common foundation for all application-specific
 * exceptions, including error codes and context information for better
 * error handling and debugging.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>All custom exceptions should extend this class</li>
 *   <li>Error codes provide consistent error identification</li>
 *   <li>Context information aids in debugging and monitoring</li>
 *   <li>Thread-safe and serializable</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * throw new BaseException("VALIDATION_ERROR", "Invalid input data");
 * 
 * throw new BaseException("PROCESSING_ERROR", "Failed to process request",
 *     Map.of("userId", "123", "operation", "update"));
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public class BaseException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String errorCode;
    private final Map<String, Object> context;
    
    /**
     * Creates a new BaseException with error code and message.
     * 
     * @param errorCode the error code identifying the type of error
     * @param message the detail message explaining the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Map.of();
    }
    
    /**
     * Creates a new BaseException with error code, message, and context.
     * 
     * @param errorCode the error code identifying the type of error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public BaseException(String errorCode, String message, Map<String, Object> context) {
        super(message);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }
    
    /**
     * Creates a new BaseException with error code, message, and cause.
     * 
     * @param errorCode the error code identifying the type of error
     * @param message the detail message explaining the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Map.of();
    }
    
    /**
     * Creates a new BaseException with error code, message, context, and cause.
     * 
     * @param errorCode the error code identifying the type of error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public BaseException(String errorCode, String message, Map<String, Object> context, Throwable cause) {
        super(message, cause);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }
    
    /**
     * Returns the error code associated with this exception.
     * 
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Returns the context information associated with this exception.
     * 
     * @return a map containing context information
     */
    public Map<String, Object> getContext() {
        return context;
    }
    
    /**
     * Validates that the error code is not null or empty.
     * 
     * @param errorCode the error code to validate
     * @return the validated error code
     * @throws IllegalArgumentException if errorCode is null or empty
     */
    private static String validateErrorCode(String errorCode) {
        Objects.requireNonNull(errorCode, "errorCode cannot be null");
        if (errorCode.trim().isEmpty()) {
            throw new IllegalArgumentException("errorCode cannot be empty");
        }
        return errorCode;
    }
}
