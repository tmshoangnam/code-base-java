package io.github.base.core.exception;

import java.util.Map;

/**
 * Exception for business rule violations and domain-specific errors.
 * 
 * <p>This exception should be thrown when business rules are violated
 * or when domain-specific constraints are not met. These errors are
 * typically recoverable and should be handled gracefully by the application.
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use for domain-specific validation failures</li>
 *   <li>Use for business rule violations</li>
 *   <li>Include meaningful error codes for client handling</li>
 *   <li>Provide clear, actionable error messages</li>
 *   <li>Include context information when available</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * throw new BusinessException("USER_NOT_FOUND", "User with ID 123 does not exist");
 * 
 * throw new BusinessException("INSUFFICIENT_BALANCE", 
 *     "Account balance is insufficient for this transaction",
 *     Map.of("currentBalance", 100.0, "requiredAmount", 150.0));
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public class BusinessException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new BusinessException with error code and message.
     * 
     * @param errorCode the error code identifying the type of business error
     * @param message the detail message explaining the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * Creates a new BusinessException with error code, message, and context.
     * 
     * @param errorCode the error code identifying the type of business error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public BusinessException(String errorCode, String message, Map<String, Object> context) {
        super(errorCode, message, context);
    }
    
    /**
     * Creates a new BusinessException with error code, message, and cause.
     * 
     * @param errorCode the error code identifying the type of business error
     * @param message the detail message explaining the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * Creates a new BusinessException with error code, message, context, and cause.
     * 
     * @param errorCode the error code identifying the type of business error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public BusinessException(String errorCode, String message, Map<String, Object> context, Throwable cause) {
        super(errorCode, message, context, cause);
    }
}
