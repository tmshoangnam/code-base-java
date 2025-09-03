package com.mycompany.base.core.api.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.Objects;

/**
 * Utility class for Jakarta Bean Validation.
 * 
 * <p>This class provides convenient methods for validating objects using
 * Jakarta Bean Validation annotations. It handles the creation and
 * management of Validator instances and provides clear error reporting.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Thread-safe operations with static Validator</li>
 *   <li>Clear error messages for validation failures</li>
 *   <li>Null-safe operations where appropriate</li>
 *   <li>Minimal dependencies - only Jakarta Validation API</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Validate object and throw exception on failure
 * ValidationUtils.validate(userDto);
 * 
 * // Validate object and get violations
 * Set<ConstraintViolation<UserDto>> violations = ValidationUtils.validateAndGet(userDto);
 * if (!violations.isEmpty()) {
 *     // Handle validation errors
 * }
 * 
 * // Validate specific property
 * Set<ConstraintViolation<UserDto>> violations = ValidationUtils.validateProperty(userDto, "email");
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public final class ValidationUtils {
    
    private static final Validator VALIDATOR = createValidator();
    
    private ValidationUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Creates a Validator instance with default configuration.
     * 
     * @return configured Validator
     */
    private static Validator createValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            return factory.getValidator();
        }
    }
    
    /**
     * Validates an object and throws IllegalArgumentException if validation fails.
     * 
     * <p>This method performs validation on the entire object and throws
     * an exception with a detailed message if any constraint violations
     * are found.
     * 
     * @param obj the object to validate
     * @param <T> the type of the object
     * @throws IllegalArgumentException if validation fails
     */
    public static <T> void validate(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object to validate cannot be null");
        }
        
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(obj);
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder("Validation failed: ");
            for (ConstraintViolation<T> violation : violations) {
                message.append(violation.getPropertyPath())
                       .append(": ")
                       .append(violation.getMessage())
                       .append("; ");
            }
            throw new IllegalArgumentException(message.toString());
        }
    }
    
    /**
     * Validates an object and returns the set of constraint violations.
     * 
     * <p>This method performs validation on the entire object and returns
     * all constraint violations found. It does not throw an exception,
     * allowing the caller to handle violations as needed.
     * 
     * @param obj the object to validate
     * @param <T> the type of the object
     * @return set of constraint violations (empty if validation passes)
     */
    public static <T> Set<ConstraintViolation<T>> validateAndGet(T obj) {
        if (obj == null) {
            return Set.of();
        }
        return VALIDATOR.validate(obj);
    }
    
    /**
     * Validates a specific property of an object.
     * 
     * <p>This method validates only the specified property of the object,
     * which can be useful for partial validation scenarios.
     * 
     * @param obj the object to validate
     * @param propertyName the name of the property to validate
     * @param <T> the type of the object
     * @return set of constraint violations for the specified property
     */
    public static <T> Set<ConstraintViolation<T>> validateProperty(T obj, String propertyName) {
        if (obj == null) {
            return Set.of();
        }
        Objects.requireNonNull(propertyName, "propertyName cannot be null");
        return VALIDATOR.validateProperty(obj, propertyName);
    }
    
    /**
     * Validates a specific property and throws IllegalArgumentException if validation fails.
     * 
     * @param obj the object to validate
     * @param propertyName the name of the property to validate
     * @param <T> the type of the object
     * @throws IllegalArgumentException if validation fails
     */
    public static <T> void validatePropertyAndThrow(T obj, String propertyName) {
        if (obj == null) {
            throw new IllegalArgumentException("Object to validate cannot be null");
        }
        Objects.requireNonNull(propertyName, "propertyName cannot be null");
        
        Set<ConstraintViolation<T>> violations = VALIDATOR.validateProperty(obj, propertyName);
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder("Validation failed for property '")
                    .append(propertyName)
                    .append("': ");
            for (ConstraintViolation<T> violation : violations) {
                message.append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(message.toString());
        }
    }
    
    /**
     * Checks if an object is valid according to its validation constraints.
     * 
     * @param obj the object to check
     * @param <T> the type of the object
     * @return true if the object is valid, false otherwise
     */
    public static <T> boolean isValid(T obj) {
        if (obj == null) {
            return false;
        }
        return VALIDATOR.validate(obj).isEmpty();
    }
    
    /**
     * Checks if a specific property of an object is valid.
     * 
     * @param obj the object to check
     * @param propertyName the name of the property to check
     * @param <T> the type of the object
     * @return true if the property is valid, false otherwise
     */
    public static <T> boolean isPropertyValid(T obj, String propertyName) {
        if (obj == null || propertyName == null) {
            return false;
        }
        return VALIDATOR.validateProperty(obj, propertyName).isEmpty();
    }
    
    /**
     * Gets the configured Validator instance.
     * 
     * <p>This method provides access to the internal Validator for
     * advanced use cases that require custom validation logic.
     * 
     * @return the configured Validator instance
     */
    public static Validator getValidator() {
        return VALIDATOR;
    }
}
