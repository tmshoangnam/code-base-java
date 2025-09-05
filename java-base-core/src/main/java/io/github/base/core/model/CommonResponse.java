package io.github.base.core.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Standard response model for API responses.
 * 
 * <p>This class provides a consistent structure for API responses across
 * the application, including status information, data, and metadata.
 * It follows common API response patterns and provides static factory
 * methods for easy response creation.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Immutable response objects</li>
 *   <li>Consistent structure across all APIs</li>
 *   <li>Support for both success and error responses</li>
 *   <li>Extensible metadata support</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Success response
 * CommonResponse<User> response = CommonResponse.success(user);
 * 
 * // Error response
 * CommonResponse<Void> response = CommonResponse.error("USER_NOT_FOUND", "User does not exist");
 * 
 * // Custom response
 * CommonResponse<String> response = CommonResponse.builder()
 *     .status("success")
 *     .message("Operation completed")
 *     .code("OK")
 *     .data("result")
 *     .build();
 * }</pre>
 * 
 * @param <T> the type of data in the response
 * @since 1.0.0
 * @author java-base-core
 */
public final class CommonResponse<T> {
    
    private final String status;
    private final String message;
    private final String code;
    private final T data;
    private final Instant timestamp;
    private final Map<String, Object> metadata;
    
    /**
     * Creates a new CommonResponse with all fields.
     * 
     * @param status the status of the response
     * @param message the message describing the response
     * @param code the response code
     * @param data the response data
     * @param timestamp when the response was created
     * @param metadata additional metadata
     */
    private CommonResponse(String status, String message, String code, T data, 
                          Instant timestamp, Map<String, Object> metadata) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.data = data;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }
    
    /**
     * Creates a success response with data.
     * 
     * @param data the response data
     * @param <T> the type of data
     * @return a success response
     */
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>("success", "Operation completed successfully", "OK", 
                                  data, Instant.now(), Map.of());
    }
    
    /**
     * Creates a success response with data and custom message.
     * 
     * @param data the response data
     * @param message the success message
     * @param <T> the type of data
     * @return a success response
     */
    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>("success", message, "OK", data, Instant.now(), Map.of());
    }
    
    /**
     * Creates an error response with error code and message.
     * 
     * @param code the error code
     * @param message the error message
     * @param <T> the type of data
     * @return an error response
     */
    public static <T> CommonResponse<T> error(String code, String message) {
        return new CommonResponse<>("error", message, code, null, Instant.now(), Map.of());
    }
    
    /**
     * Creates an error response with error code, message, and metadata.
     * 
     * @param code the error code
     * @param message the error message
     * @param metadata additional error metadata
     * @param <T> the type of data
     * @return an error response
     */
    public static <T> CommonResponse<T> error(String code, String message, Map<String, Object> metadata) {
        return new CommonResponse<>("error", message, code, null, Instant.now(), 
                                  metadata != null ? Map.copyOf(metadata) : Map.of());
    }
    
    /**
     * Creates a warning response with data and message.
     * 
     * @param data the response data
     * @param message the warning message
     * @param <T> the type of data
     * @return a warning response
     */
    public static <T> CommonResponse<T> warning(T data, String message) {
        return new CommonResponse<>("warning", message, "WARNING", data, Instant.now(), Map.of());
    }
    
    /**
     * Creates a builder for custom responses.
     * 
     * @param <T> the type of data
     * @return a new builder instance
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
    
    /**
     * Returns the status of the response.
     * 
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Returns the message of the response.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Returns the code of the response.
     * 
     * @return the code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Returns the data of the response.
     * 
     * @return the data
     */
    public T getData() {
        return data;
    }
    
    /**
     * Returns the timestamp of the response.
     * 
     * @return the timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }
    
    /**
     * Returns the metadata of the response.
     * 
     * @return the metadata
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    /**
     * Checks if the response is successful.
     * 
     * @return true if the response status is "success", false otherwise
     */
    public boolean isSuccess() {
        return "success".equals(status);
    }
    
    /**
     * Checks if the response is an error.
     * 
     * @return true if the response status is "error", false otherwise
     */
    public boolean isError() {
        return "error".equals(status);
    }
    
    /**
     * Checks if the response is a warning.
     * 
     * @return true if the response status is "warning", false otherwise
     */
    public boolean isWarning() {
        return "warning".equals(status);
    }
    
    /**
     * Builder class for creating CommonResponse instances.
     * 
     * @param <T> the type of data
     */
    public static final class Builder<T> {
        private String status;
        private String message;
        private String code;
        private T data;
        private Instant timestamp;
        private Map<String, Object> metadata;
        
        private Builder() {
            this.timestamp = Instant.now();
            this.metadata = Map.of();
        }
        
        /**
         * Sets the status of the response.
         * 
         * @param status the status
         * @return this builder
         */
        public Builder<T> status(String status) {
            this.status = status;
            return this;
        }
        
        /**
         * Sets the message of the response.
         * 
         * @param message the message
         * @return this builder
         */
        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }
        
        /**
         * Sets the code of the response.
         * 
         * @param code the code
         * @return this builder
         */
        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }
        
        /**
         * Sets the data of the response.
         * 
         * @param data the data
         * @return this builder
         */
        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }
        
        /**
         * Sets the timestamp of the response.
         * 
         * @param timestamp the timestamp
         * @return this builder
         */
        public Builder<T> timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        /**
         * Sets the metadata of the response.
         * 
         * @param metadata the metadata
         * @return this builder
         */
        public Builder<T> metadata(Map<String, Object> metadata) {
            this.metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
            return this;
        }
        
        /**
         * Builds the CommonResponse instance.
         * 
         * @return the built CommonResponse
         * @throws IllegalStateException if required fields are not set
         */
        public CommonResponse<T> build() {
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalStateException("status is required");
            }
            if (message == null || message.trim().isEmpty()) {
                throw new IllegalStateException("message is required");
            }
            if (code == null || code.trim().isEmpty()) {
                throw new IllegalStateException("code is required");
            }
            
            return new CommonResponse<>(status, message, code, data, timestamp, metadata);
        }
    }
}
