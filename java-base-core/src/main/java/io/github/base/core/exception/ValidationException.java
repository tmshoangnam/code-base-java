package io.github.base.core.exception;

import java.util.Map;

/**
 * Exception for validation errors and constraint violations.
 * 
 * <p>This exception should be thrown when input validation fails or
 * when data constraints are violated. These errors are typically
 * caused by invalid user input or malformed data.
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use for input validation failures</li>
 *   <li>Use for data constraint violations</li>
 *   <li>Include field-specific error information</li>
 *   <li>Provide clear validation error messages</li>
 *   <li>Include context about which fields failed validation</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * throw new ValidationException("INVALID_EMAIL", "Email format is invalid");
 * 
 * throw new ValidationException("REQUIRED_FIELD", 
 *     "Required field 'name' is missing",
 *     Map.of("field", "name", "value", null));
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public class ValidationException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new ValidationException with error code and message.
     * 
     * @param errorCode the error code identifying the type of validation error
     * @param message the detail message explaining the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * Creates a new ValidationException with error code, message, and context.
     * 
     * @param errorCode the error code identifying the type of validation error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public ValidationException(String errorCode, String message, Map<String, Object> context) {
        super(errorCode, message, context);
    }
    
    /**
     * Creates a new ValidationException with error code, message, and cause.
     * 
     * @param errorCode the error code identifying the type of validation error
     * @param message the detail message explaining the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public ValidationException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * Creates a new ValidationException with error code, message, context, and cause.
     * 
     * @param errorCode the error code identifying the type of validation error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public ValidationException(String errorCode, String message, Map<String, Object> context, Throwable cause) {
        super(errorCode, message, context, cause);
    }
}
