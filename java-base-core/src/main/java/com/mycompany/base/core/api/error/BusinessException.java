package com.mycompany.base.core.api.error;

import java.util.Map;
import java.util.Objects;

/**
 * Base class for business-related exceptions.
 * 
 * <p>Business exceptions represent errors that occur due to business rule
 * violations or domain-specific constraints. These exceptions should be
 * handled gracefully and converted to appropriate HTTP responses.
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use for domain-specific validation failures</li>
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
public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String errorCode;
    private final Map<String, Object> context;
    
    /**
     * Creates a new BusinessException with error code and message.
     * 
     * @param errorCode the error code identifying the type of business error
     * @param message the detail message explaining the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Map.of();
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
        super(message);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Objects.requireNonNull(context, "context cannot be null");
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
        super(message, cause);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Map.of();
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
        super(message, cause);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }
    
    /**
     * Returns the error code associated with this business exception.
     * 
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Returns the context information associated with this business exception.
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
