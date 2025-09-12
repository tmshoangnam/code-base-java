package io.github.base.aws.api.common;

import java.util.Objects;

/**
 * Base exception for all AWS service related errors.
 * 
 * <p>This exception serves as the root of the AWS exception hierarchy
 * and provides common functionality for all AWS service exceptions.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class AwsException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String errorCode;
    private final String requestId;
    private final String serviceName;
    private final int statusCode;
    
    /**
     * Constructs a new AwsException with the specified detail message.
     * 
     * @param message the detail message
     */
    public AwsException(String message) {
        super(message);
        this.errorCode = null;
        this.requestId = null;
        this.serviceName = null;
        this.statusCode = 0;
    }
    
    /**
     * Constructs a new AwsException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public AwsException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.requestId = null;
        this.serviceName = null;
        this.statusCode = 0;
    }
    
    /**
     * Constructs a new AwsException with the specified detail message, error code, and request ID.
     * 
     * @param message the detail message
     * @param errorCode the AWS error code
     * @param requestId the AWS request ID
     * @param serviceName the AWS service name
     */
    public AwsException(String message, String errorCode, String requestId, String serviceName) {
        super(message);
        this.errorCode = errorCode;
        this.requestId = requestId;
        this.serviceName = serviceName;
        this.statusCode = 0;
    }
    
    /**
     * Constructs a new AwsException with the specified detail message, cause, error code, and request ID.
     * 
     * @param message the detail message
     * @param cause the cause
     * @param errorCode the AWS error code
     * @param requestId the AWS request ID
     * @param serviceName the AWS service name
     */
    public AwsException(String message, Throwable cause, String errorCode, String requestId, String serviceName) {
        super(message, cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
        this.serviceName = serviceName;
        this.statusCode = 0;
    }
    
    /**
     * Constructs a new AwsException with all information.
     * 
     * @param message the detail message
     * @param cause the cause
     * @param errorCode the AWS error code
     * @param requestId the AWS request ID
     * @param serviceName the AWS service name
     * @param statusCode the HTTP status code
     */
    public AwsException(String message, Throwable cause, String errorCode, String requestId, 
                       String serviceName, int statusCode) {
        super(message, cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
        this.serviceName = serviceName;
        this.statusCode = statusCode;
    }
    
    /**
     * Gets the AWS error code.
     * 
     * @return the error code or null if not specified
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Gets the AWS request ID.
     * 
     * @return the request ID or null if not specified
     */
    public String getRequestId() {
        return requestId;
    }
    
    /**
     * Gets the AWS service name.
     * 
     * @return the service name or null if not specified
     */
    public String getServiceName() {
        return serviceName;
    }
    
    /**
     * Gets the HTTP status code.
     * 
     * @return the status code or 0 if not specified
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Checks if this exception has an error code.
     * 
     * @return true if error code is present, false otherwise
     */
    public boolean hasErrorCode() {
        return errorCode != null && !errorCode.trim().isEmpty();
    }
    
    /**
     * Checks if this exception has a request ID.
     * 
     * @return true if request ID is present, false otherwise
     */
    public boolean hasRequestId() {
        return requestId != null && !requestId.trim().isEmpty();
    }
    
    /**
     * Checks if this exception has a service name.
     * 
     * @return true if service name is present, false otherwise
     */
    public boolean hasServiceName() {
        return serviceName != null && !serviceName.trim().isEmpty();
    }
    
    /**
     * Checks if this exception has a status code.
     * 
     * @return true if status code is present, false otherwise
     */
    public boolean hasStatusCode() {
        return statusCode > 0;
    }
    
    /**
     * Checks if this exception is a client error (4xx status code).
     * 
     * @return true if status code is 4xx, false otherwise
     */
    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }
    
    /**
     * Checks if this exception is a server error (5xx status code).
     * 
     * @return true if status code is 5xx, false otherwise
     */
    public boolean isServerError() {
        return statusCode >= 500;
    }
    
    /**
     * Checks if this exception is a retryable error.
     * 
     * @return true if the error is retryable, false otherwise
     */
    public boolean isRetryable() {
        if (statusCode == 0) {
            return false;
        }
        
        // Retry on 5xx errors and specific 4xx errors
        return isServerError() || 
               statusCode == 408 || // Request Timeout
               statusCode == 429 || // Too Many Requests
               statusCode == 502 || // Bad Gateway
               statusCode == 503 || // Service Unavailable
               statusCode == 504;   // Gateway Timeout
    }
    
    /**
     * Gets a formatted error message including all available information.
     * 
     * @return formatted error message
     */
    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMessage());
        
        if (hasServiceName()) {
            sb.append(" [Service: ").append(serviceName).append("]");
        }
        
        if (hasErrorCode()) {
            sb.append(" [Error Code: ").append(errorCode).append("]");
        }
        
        if (hasRequestId()) {
            sb.append(" [Request ID: ").append(requestId).append("]");
        }
        
        if (hasStatusCode()) {
            sb.append(" [Status Code: ").append(statusCode).append("]");
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "AwsException{" +
               "message='" + getMessage() + '\'' +
               ", errorCode='" + errorCode + '\'' +
               ", requestId='" + requestId + '\'' +
               ", serviceName='" + serviceName + '\'' +
               ", statusCode=" + statusCode +
               ", cause=" + getCause() +
               '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AwsException that = (AwsException) obj;
        return statusCode == that.statusCode &&
               Objects.equals(getMessage(), that.getMessage()) &&
               Objects.equals(errorCode, that.errorCode) &&
               Objects.equals(requestId, that.requestId) &&
               Objects.equals(serviceName, that.serviceName) &&
               Objects.equals(getCause(), that.getCause());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), errorCode, requestId, serviceName, statusCode, getCause());
    }
}
