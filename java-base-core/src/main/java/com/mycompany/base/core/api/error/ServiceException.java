package com.mycompany.base.core.api.error;

import java.util.Map;
import java.util.Objects;

/**
 * Base class for service-related exceptions.
 * 
 * <p>Service exceptions represent errors that occur during service operations,
 * such as external API calls, database operations, or other infrastructure
 * failures. These exceptions typically indicate temporary or system-level issues.
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use for infrastructure failures (database, external APIs)</li>
 *   <li>Use for temporary service unavailability</li>
 *   <li>Include service identification for debugging</li>
 *   <li>Provide context about the failed operation</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * throw new ServiceException("DATABASE_CONNECTION_FAILED", 
 *     "Unable to connect to the database");
 * 
 * throw new ServiceException("EXTERNAL_API_TIMEOUT", 
 *     "Payment service did not respond within timeout",
 *     Map.of("service", "payment-service", "timeout", "30s"));
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public class ServiceException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String errorCode;
    private final Map<String, Object> context;
    
    /**
     * Creates a new ServiceException with error code and message.
     * 
     * @param errorCode the error code identifying the type of service error
     * @param message the detail message explaining the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public ServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Map.of();
    }
    
    /**
     * Creates a new ServiceException with error code, message, and context.
     * 
     * @param errorCode the error code identifying the type of service error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public ServiceException(String errorCode, String message, Map<String, Object> context) {
        super(message);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }
    
    /**
     * Creates a new ServiceException with error code, message, and cause.
     * 
     * @param errorCode the error code identifying the type of service error
     * @param message the detail message explaining the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public ServiceException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Map.of();
    }
    
    /**
     * Creates a new ServiceException with error code, message, context, and cause.
     * 
     * @param errorCode the error code identifying the type of service error
     * @param message the detail message explaining the error
     * @param context additional context information about the error
     * @param cause the cause of this exception
     * @throws IllegalArgumentException if errorCode or message is null or empty
     */
    public ServiceException(String errorCode, String message, Map<String, Object> context, Throwable cause) {
        super(message, cause);
        this.errorCode = validateErrorCode(errorCode);
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }
    
    /**
     * Returns the error code associated with this service exception.
     * 
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Returns the context information associated with this service exception.
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
