package com.mycompany.base.core.validation;

import com.mycompany.base.core.exception.ValidationException;
import com.mycompany.base.core.logging.LoggingUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Enhanced utility class for validation operations.
 * Provides enterprise-grade validation with custom validators, validation groups,
 * cross-field validation, caching, and performance metrics.
 * 
 * Features:
 * - Custom validators framework
 * - Validation groups support
 * - Cross-field validation
 * - Validation caching
 * - Performance metrics
 * - Detailed error reporting
 * - Validation result objects
 * 
 * @author mycompany
 * @since 1.0.0
 */
public final class ValidationUtils {
    
    private static final Logger logger = LoggingUtils.getLogger(ValidationUtils.class);
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();
    
    // Validation cache for performance optimization
    private static final Map<String, ValidationResult<?>> VALIDATION_CACHE = new ConcurrentHashMap<>();
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);
    
    // Performance metrics
    private static final Map<String, Long> VALIDATION_TIMES = new ConcurrentHashMap<>();
    private static final Map<String, Long> VALIDATION_COUNTS = new ConcurrentHashMap<>();
    
    private ValidationUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Validates an object and returns validation errors as a string.
     * 
     * @param object the object to validate
     * @return validation errors as comma-separated string, or empty string if valid
     */
    public static String validateAndGetErrors(Object object) {
        return validateAndGetErrors(object, Default.class);
    }
    
    /**
     * Validates an object with specific validation groups and returns validation errors as a string.
     * 
     * @param object the object to validate
     * @param groups the validation groups to use
     * @return validation errors as comma-separated string, or empty string if valid
     */
    public static String validateAndGetErrors(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> violations = VALIDATOR.validate(object, groups);
        
        if (violations.isEmpty()) {
            return "";
        }
        
        return violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }
    
    /**
     * Validates an object and throws exception if validation fails.
     * 
     * @param object the object to validate
     * @throws ValidationException if validation fails
     */
    public static void validateOrThrow(Object object) {
        validateOrThrow(object, Default.class);
    }
    
    /**
     * Validates an object with specific validation groups and throws exception if validation fails.
     * 
     * @param object the object to validate
     * @param groups the validation groups to use
     * @throws ValidationException if validation fails
     */
    public static void validateOrThrow(Object object, Class<?>... groups) {
        ValidationResult<Object> result = validateWithResult(object, groups);
        if (!result.isValid()) {
            throw new ValidationException("Validation failed: " + result.getErrorsAsString(), result.getContext());
        }
    }
    
    /**
     * Checks if an object is valid.
     * 
     * @param object the object to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(Object object) {
        return isValid(object, Default.class);
    }
    
    /**
     * Checks if an object is valid with specific validation groups.
     * 
     * @param object the object to validate
     * @param groups the validation groups to use
     * @return true if valid, false otherwise
     */
    public static boolean isValid(Object object, Class<?>... groups) {
        return VALIDATOR.validate(object, groups).isEmpty();
    }
    
    /**
     * Gets the number of validation violations.
     * 
     * @param object the object to validate
     * @return number of validation violations
     */
    public static int getViolationCount(Object object) {
        return getViolationCount(object, Default.class);
    }
    
    /**
     * Gets the number of validation violations with specific validation groups.
     * 
     * @param object the object to validate
     * @param groups the validation groups to use
     * @return number of validation violations
     */
    public static int getViolationCount(Object object, Class<?>... groups) {
        return VALIDATOR.validate(object, groups).size();
    }
    
    /**
     * Validates an object and returns a detailed validation result.
     * 
     * @param object the object to validate
     * @param <T> the object type
     * @return validation result with detailed information
     */
    public static <T> ValidationResult<T> validateWithResult(T object) {
        return validateWithResult(object, Default.class);
    }
    
    /**
     * Validates an object with specific validation groups and returns a detailed validation result.
     * 
     * @param object the object to validate
     * @param groups the validation groups to use
     * @param <T> the object type
     * @return validation result with detailed information
     */
    public static <T> ValidationResult<T> validateWithResult(T object, Class<?>... groups) {
        Instant startTime = Instant.now();
        String cacheKey = generateCacheKey(object, groups);
        
        try {
            // Check cache first
            ValidationResult<?> cachedResult = VALIDATION_CACHE.get(cacheKey);
            if (cachedResult != null && !isCacheExpired(cachedResult)) {
                updateMetrics("cache_hit", startTime);
                return (ValidationResult<T>) cachedResult;
            }
            
            // Perform validation
            Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object, groups);
            ValidationResult<T> result = new ValidationResult<>(object, violations);
            
            // Cache the result
            VALIDATION_CACHE.put(cacheKey, result);
            
            // Update metrics
            updateMetrics("validation", startTime);
            updateValidationCount("total_validations");
            
            return result;
            
        } catch (Exception e) {
            logger.error("Validation failed with exception", e);
            updateMetrics("validation_error", startTime);
            throw new ValidationException("Validation failed with exception: " + e.getMessage(), e);
        }
    }
    
    /**
     * Performs cross-field validation using custom predicates.
     * 
     * @param object the object to validate
     * @param validators the custom validators to apply
     * @param <T> the object type
     * @return validation result with cross-field validation
     */
    public static <T> ValidationResult<T> validateWithCustomValidators(T object, List<CustomValidator<T>> validators) {
        ValidationResult<T> baseResult = validateWithResult(object);
        List<ValidationError> customErrors = new ArrayList<>();
        
        for (CustomValidator<T> validator : validators) {
            try {
                if (!validator.isValid(object)) {
                    customErrors.add(new ValidationError(
                        validator.getFieldName(),
                        validator.getErrorMessage(),
                        validator.getErrorCode()
                    ));
                }
            } catch (Exception e) {
                logger.warn("Custom validator {} failed for object {}", validator.getClass().getSimpleName(), object, e);
                customErrors.add(new ValidationError(
                    validator.getFieldName(),
                    "Custom validation failed: " + e.getMessage(),
                    "CUSTOM_VALIDATION_ERROR"
                ));
            }
        }
        
        if (!customErrors.isEmpty()) {
            baseResult.addCustomErrors(customErrors);
        }
        
        return baseResult;
    }
    
    /**
     * Validates multiple objects in batch.
     * 
     * @param objects the objects to validate
     * @param <T> the object type
     * @return list of validation results
     */
    public static <T> List<ValidationResult<T>> validateBatch(List<T> objects) {
        return validateBatch(objects, Default.class);
    }
    
    /**
     * Validates multiple objects in batch with specific validation groups.
     * 
     * @param objects the objects to validate
     * @param groups the validation groups to use
     * @param <T> the object type
     * @return list of validation results
     */
    public static <T> List<ValidationResult<T>> validateBatch(List<T> objects, Class<?>... groups) {
        if (objects == null || objects.isEmpty()) {
            return Collections.emptyList();
        }
        
        return objects.parallelStream()
                .map(obj -> validateWithResult(obj, groups))
                .collect(Collectors.toList());
    }
    
    /**
     * Clears the validation cache.
     */
    public static void clearCache() {
        VALIDATION_CACHE.clear();
        logger.info("Validation cache cleared");
    }
    
    /**
     * Gets validation performance statistics.
     * 
     * @return validation statistics
     */
    public static ValidationStatistics getStatistics() {
        return new ValidationStatistics(
            new HashMap<>(VALIDATION_TIMES),
            new HashMap<>(VALIDATION_COUNTS),
            VALIDATION_CACHE.size()
        );
    }
    
    /**
     * Custom validator interface for cross-field validation.
     * 
     * @param <T> the object type to validate
     */
    public interface CustomValidator<T> {
        /**
         * Validates the object.
         * 
         * @param object the object to validate
         * @return true if valid, false otherwise
         */
        boolean isValid(T object);
        
        /**
         * Gets the field name for error reporting.
         * 
         * @return the field name
         */
        String getFieldName();
        
        /**
         * Gets the error message.
         * 
         * @return the error message
         */
        String getErrorMessage();
        
        /**
         * Gets the error code.
         * 
         * @return the error code
         */
        String getErrorCode();
    }
    
    /**
     * Validation result containing detailed validation information.
     * 
     * @param <T> the validated object type
     */
    public static class ValidationResult<T> {
        private final T object;
        private final Set<ConstraintViolation<T>> violations;
        private final List<ValidationError> customErrors;
        private final Instant validationTime;
        private final Map<String, Object> context;
        
        public ValidationResult(T object, Set<ConstraintViolation<T>> violations) {
            this.object = object;
            this.violations = violations != null ? violations : Collections.emptySet();
            this.customErrors = new ArrayList<>();
            this.validationTime = Instant.now();
            this.context = new HashMap<>();
        }
        
        public T getObject() {
            return object;
        }
        
        public Set<ConstraintViolation<T>> getViolations() {
            return Collections.unmodifiableSet(violations);
        }
        
        public List<ValidationError> getCustomErrors() {
            return Collections.unmodifiableList(customErrors);
        }
        
        public Instant getValidationTime() {
            return validationTime;
        }
        
        public Map<String, Object> getContext() {
            return Collections.unmodifiableMap(context);
        }
        
        public boolean isValid() {
            return violations.isEmpty() && customErrors.isEmpty();
        }
        
        public int getViolationCount() {
            return violations.size() + customErrors.size();
        }
        
        public String getErrorsAsString() {
            List<String> allErrors = new ArrayList<>();
            
            // Add constraint violations
            allErrors.addAll(violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.toList()));
            
            // Add custom errors
            allErrors.addAll(customErrors.stream()
                    .map(error -> error.getFieldName() + ": " + error.getMessage())
                    .collect(Collectors.toList()));
            
            return String.join(", ", allErrors);
        }
        
        public void addCustomErrors(List<ValidationError> errors) {
            this.customErrors.addAll(errors);
        }
        
        public void addContext(String key, Object value) {
            this.context.put(key, value);
        }
    }
    
    /**
     * Validation error information.
     */
    public static class ValidationError {
        private final String fieldName;
        private final String message;
        private final String errorCode;
        
        public ValidationError(String fieldName, String message, String errorCode) {
            this.fieldName = fieldName;
            this.message = message;
            this.errorCode = errorCode;
        }
        
        public String getFieldName() {
            return fieldName;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getErrorCode() {
            return errorCode;
        }
    }
    
    /**
     * Validation statistics for monitoring.
     */
    public static class ValidationStatistics {
        private final Map<String, Long> validationTimes;
        private final Map<String, Long> validationCounts;
        private final int cacheSize;
        
        public ValidationStatistics(Map<String, Long> validationTimes, Map<String, Long> validationCounts, int cacheSize) {
            this.validationTimes = validationTimes;
            this.validationCounts = validationCounts;
            this.cacheSize = cacheSize;
        }
        
        public Map<String, Long> getValidationTimes() {
            return Collections.unmodifiableMap(validationTimes);
        }
        
        public Map<String, Long> getValidationCounts() {
            return Collections.unmodifiableMap(validationCounts);
        }
        
        public int getCacheSize() {
            return cacheSize;
        }
    }
    
    // Private helper methods
    
    private static String generateCacheKey(Object object, Class<?>... groups) {
        StringBuilder key = new StringBuilder();
        key.append(object.getClass().getName());
        key.append(":");
        key.append(object.hashCode());
        key.append(":");
        for (Class<?> group : groups) {
            key.append(group.getName()).append(",");
        }
        return key.toString();
    }
    
    private static boolean isCacheExpired(ValidationResult<?> result) {
        return Duration.between(result.getValidationTime(), Instant.now()).compareTo(CACHE_TTL) > 0;
    }
    
    private static void updateMetrics(String operation, Instant startTime) {
        long duration = Duration.between(startTime, Instant.now()).toMillis();
        VALIDATION_TIMES.merge(operation, duration, Long::sum);
    }
    
    private static void updateValidationCount(String metric) {
        VALIDATION_COUNTS.merge(metric, 1L, Long::sum);
    }
}
