package io.github.base.core.exception;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;

/**
 * Registry for managing error codes and their descriptions.
 * 
 * <p>This class provides a centralized way to manage error codes
 * and their associated descriptions, ensuring consistency across
 * the application and making it easier to maintain error codes.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Thread-safe operations</li>
 *   <li>Immutable error code definitions</li>
 *   <li>Centralized error code management</li>
 *   <li>Support for error code hierarchies</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Register error codes
 * ErrorCodeRegistry.register("USER_NOT_FOUND", "User does not exist");
 * ErrorCodeRegistry.register("INSUFFICIENT_BALANCE", "Account balance is insufficient");
 * 
 * // Get error description
 * String description = ErrorCodeRegistry.getDescription("USER_NOT_FOUND");
 * 
 * // Check if error code exists
 * boolean exists = ErrorCodeRegistry.exists("USER_NOT_FOUND");
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public final class ErrorCodeRegistry {
    
    private static final Map<String, String> ERROR_CODES = new ConcurrentHashMap<>();
    
    private ErrorCodeRegistry() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Registers an error code with its description.
     * 
     * @param errorCode the error code to register
     * @param description the description of the error code
     * @throws IllegalArgumentException if errorCode or description is null or empty
     */
    public static void register(String errorCode, String description) {
        validateErrorCode(errorCode);
        Objects.requireNonNull(description, "description cannot be null");
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("description cannot be empty");
        }
        
        ERROR_CODES.put(errorCode, description);
    }
    
    /**
     * Registers multiple error codes at once.
     * 
     * @param errorCodes a map of error codes to descriptions
     * @throws IllegalArgumentException if errorCodes is null or contains invalid entries
     */
    public static void registerAll(Map<String, String> errorCodes) {
        Objects.requireNonNull(errorCodes, "errorCodes cannot be null");
        
        for (Map.Entry<String, String> entry : errorCodes.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * Gets the description for an error code.
     * 
     * @param errorCode the error code to look up
     * @return the description of the error code, or null if not found
     */
    public static String getDescription(String errorCode) {
        if (errorCode == null) {
            return null;
        }
        return ERROR_CODES.get(errorCode);
    }
    
    /**
     * Checks if an error code is registered.
     * 
     * @param errorCode the error code to check
     * @return true if the error code is registered, false otherwise
     */
    public static boolean exists(String errorCode) {
        if (errorCode == null) {
            return false;
        }
        return ERROR_CODES.containsKey(errorCode);
    }
    
    /**
     * Gets all registered error codes.
     * 
     * @return a map of all registered error codes and their descriptions
     */
    public static Map<String, String> getAllErrorCodes() {
        return Map.copyOf(ERROR_CODES);
    }
    
    /**
     * Clears all registered error codes.
     * 
     * <p><strong>Warning:</strong> This method should only be used in tests
     * or during application shutdown. Clearing error codes during runtime
     * may cause issues with error handling.
     */
    public static void clear() {
        ERROR_CODES.clear();
    }
    
    /**
     * Gets the number of registered error codes.
     * 
     * @return the number of registered error codes
     */
    public static int size() {
        return ERROR_CODES.size();
    }
    
    /**
     * Validates that the error code is not null or empty.
     * 
     * @param errorCode the error code to validate
     * @throws IllegalArgumentException if errorCode is null or empty
     */
    private static void validateErrorCode(String errorCode) {
        Objects.requireNonNull(errorCode, "errorCode cannot be null");
        if (errorCode.trim().isEmpty()) {
            throw new IllegalArgumentException("errorCode cannot be empty");
        }
    }
}
