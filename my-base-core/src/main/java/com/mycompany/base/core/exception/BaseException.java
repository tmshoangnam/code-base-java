package com.mycompany.base.core.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base exception for my-base core module.
 * All custom exceptions should extend this class.
 * 
 * Features:
 * - Serialization support for distributed systems
 * - Context information for debugging
 * - Error categorization system
 * - Timestamp tracking
 * - Builder pattern for easy construction
 * 
 * @author mycompany
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseException extends RuntimeException implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("errorCode")
    private final String errorCode;
    
    @JsonProperty("errorType")
    private final String errorType;
    
    @JsonProperty("errorCategory")
    private final ErrorCategory errorCategory;
    
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private final Instant timestamp;
    
    @JsonProperty("context")
    private final Map<String, Object> context;
    
    @JsonProperty("severity")
    private final ErrorSeverity severity;
    
    @JsonProperty("userMessage")
    private final String userMessage;
    
    @JsonProperty("technicalDetails")
    private final String technicalDetails;
    
    /**
     * Error categories for better classification.
     */
    public enum ErrorCategory {
        VALIDATION("VALIDATION"),
        BUSINESS_LOGIC("BUSINESS_LOGIC"),
        TECHNICAL("TECHNICAL"),
        SECURITY("SECURITY"),
        INTEGRATION("INTEGRATION"),
        DATA("DATA"),
        CONFIGURATION("CONFIGURATION"),
        NETWORK("NETWORK"),
        TIMEOUT("TIMEOUT"),
        RESOURCE("RESOURCE");
        
        private final String value;
        
        ErrorCategory(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    /**
     * Error severity levels.
     */
    public enum ErrorSeverity {
        LOW("LOW"),
        MEDIUM("MEDIUM"),
        HIGH("HIGH"),
        CRITICAL("CRITICAL");
        
        private final String value;
        
        ErrorSeverity(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    // Private constructor - use builder pattern
    private BaseException(Builder builder) {
        super(builder.message, builder.cause);
        this.errorCode = builder.errorCode;
        this.errorType = builder.errorType;
        this.errorCategory = builder.errorCategory;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.context = builder.context != null ? new HashMap<>(builder.context) : new HashMap<>();
        this.severity = builder.severity;
        this.userMessage = builder.userMessage;
        this.technicalDetails = builder.technicalDetails;
    }
    
    // Getters
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorType() {
        return errorType;
    }
    
    public ErrorCategory getErrorCategory() {
        return errorCategory;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public Map<String, Object> getContext() {
        return Collections.unmodifiableMap(context);
    }
    
    public ErrorSeverity getSeverity() {
        return severity;
    }
    
    public String getUserMessage() {
        return userMessage;
    }
    
    public String getTechnicalDetails() {
        return technicalDetails;
    }
    
    /**
     * Adds context information.
     * 
     * @param key the context key
     * @param value the context value
     * @return this exception for chaining
     */
    public BaseException addContext(String key, Object value) {
        if (key != null && value != null) {
            this.context.put(key, value);
        }
        return this;
    }
    
    /**
     * Adds multiple context entries.
     * 
     * @param contextMap the context map to add
     * @return this exception for chaining
     */
    public BaseException addContext(Map<String, Object> contextMap) {
        if (contextMap != null) {
            this.context.putAll(contextMap);
        }
        return this;
    }
    
    /**
     * Gets context value by key.
     * 
     * @param key the context key
     * @return the context value, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getContextValue(String key) {
        return (T) context.get(key);
    }
    
    /**
     * Checks if context contains a key.
     * 
     * @param key the context key
     * @return true if context contains the key
     */
    public boolean hasContext(String key) {
        return context.containsKey(key);
    }
    
    /**
     * Gets the full error message including context.
     * 
     * @return formatted error message with context
     */
    public String getFullMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMessage());
        
        if (!context.isEmpty()) {
            sb.append(" [Context: ");
            context.forEach((key, value) -> 
                sb.append(key).append("=").append(value).append(", "));
            sb.setLength(sb.length() - 2); // Remove last ", "
            sb.append("]");
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return String.format("BaseException{errorCode='%s', errorType='%s', errorCategory=%s, message='%s', timestamp=%s, severity=%s}",
                errorCode, errorType, errorCategory, getMessage(), timestamp, severity);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseException that = (BaseException) o;
        return Objects.equals(errorCode, that.errorCode) &&
               Objects.equals(errorType, that.errorType) &&
               errorCategory == that.errorCategory &&
               Objects.equals(timestamp, that.timestamp) &&
               Objects.equals(context, that.context) &&
               severity == that.severity;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(errorCode, errorType, errorCategory, timestamp, context, severity);
    }
    
    /**
     * Builder for BaseException.
     */
    public static class Builder {
        private String message;
        private String errorCode = "BASE_ERROR";
        private String errorType = "GENERAL";
        private ErrorCategory errorCategory = ErrorCategory.TECHNICAL;
        private ErrorSeverity severity = ErrorSeverity.MEDIUM;
        private Throwable cause;
        private Instant timestamp;
        private Map<String, Object> context;
        private String userMessage;
        private String technicalDetails;
        
        public Builder(String message) {
            this.message = message;
        }
        
        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }
        
        public Builder errorType(String errorType) {
            this.errorType = errorType;
            return this;
        }
        
        public Builder errorCategory(ErrorCategory errorCategory) {
            this.errorCategory = errorCategory;
            return this;
        }
        
        public Builder severity(ErrorSeverity severity) {
            this.severity = severity;
            return this;
        }
        
        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }
        
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder context(String key, Object value) {
            if (this.context == null) {
                this.context = new HashMap<>();
            }
            this.context.put(key, value);
            return this;
        }
        
        public Builder context(Map<String, Object> context) {
            this.context = context;
            return this;
        }
        
        public Builder userMessage(String userMessage) {
            this.userMessage = userMessage;
            return this;
        }
        
        public Builder technicalDetails(String technicalDetails) {
            this.technicalDetails = technicalDetails;
            return this;
        }
        
        public BaseException build() {
            return new BaseException(this);
        }
    }
    
    // Convenience static methods for backward compatibility
    public static BaseException of(String message) {
        return new Builder(message).build();
    }
    
    public static BaseException of(String message, String errorCode, String errorType) {
        return new Builder(message)
                .errorCode(errorCode)
                .errorType(errorType)
                .build();
    }
    
    public static BaseException of(String message, String errorCode, String errorType, Throwable cause) {
        return new Builder(message)
                .errorCode(errorCode)
                .errorType(errorType)
                .cause(cause)
                .build();
    }
}
