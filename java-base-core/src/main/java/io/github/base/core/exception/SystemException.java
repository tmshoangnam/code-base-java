package io.github.base.core.exception;

import java.util.Map;

/**
 * Exception for system-level errors and infrastructure failures.
 * 
 * <p>This exception should be thrown when system-level errors occur,
 * such as database connection failures, external service unavailability,
 * or other infrastructure-related issues.
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use for infrastructure failures (database, external APIs)</li>
 *   <li>Use for system-level errors</li>
 *   <li>Include service identification for debugging</li>
 *   <li>Provide context about the failed operation</li>
 *   <li>These errors may be temporary and retryable</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * throw new SystemException("DATABASE_CONNECTION_FAILED", 
 *     "Unable to connect to the database");
 * 
 * throw new SystemException("EXTERNAL_API_TIMEOUT", 
 *     "Payment service did not respond within timeout",
 *     Map.of("service", "payment-service", "timeout", "30s"));
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public class SystemException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new SystemException with error code and message.
     * 
     * @param errorCode the error code identifying the type of system error
     * @param message the detail message explaining the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public SystemException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * Creates a new SystemException with error code, message, and context.
     * 
     * @param errorCode the error code identifying the type of system error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public SystemException(String errorCode, String message, Map<String, Object> context) {
        super(errorCode, message, context);
    }
    
    /**
     * Creates a new SystemException with error code, message, and cause.
     * 
     * @param errorCode the error code identifying the type of system error
     * @param message the detail message explaining the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public SystemException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * Creates a new SystemException with error code, message, context, and cause.
     * 
     * @param errorCode the error code identifying the type of system error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public SystemException(String errorCode, String message, Map<String, Object> context, Throwable cause) {
        super(errorCode, message, context, cause);
    }
}
