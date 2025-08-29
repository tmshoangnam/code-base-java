package com.mycompany.base.core.exception;

import java.util.Map;

/**
 * Exception thrown when validation fails.
 * Extends BaseException with validation-specific context.
 * 
 * @author mycompany
 * @since 1.0.0
 */
public class ValidationException extends BaseException {
    
    private static final long serialVersionUID = 1L;
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR", "VALIDATION", ErrorCategory.VALIDATION, 
              ErrorSeverity.MEDIUM, "The provided data is invalid. Please check your input and try again.", null);
    }
    
    public ValidationException(String message, Map<String, Object> validationContext) {
        super(message, "VALIDATION_ERROR", "VALIDATION", ErrorCategory.VALIDATION, 
              ErrorSeverity.MEDIUM, "The provided data is invalid. Please check your input and try again.", null);
        if (validationContext != null) {
            this.addContext(validationContext);
        }
    }
    
    public ValidationException(String message, String field, Object value) {
        super(message, "VALIDATION_ERROR", "VALIDATION", ErrorCategory.VALIDATION, 
              ErrorSeverity.MEDIUM, "The field '" + field + "' contains invalid data.", null);
        this.addContext("field", field);
        this.addContext("value", value);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, "VALIDATION_ERROR", "VALIDATION", ErrorCategory.VALIDATION, 
              ErrorSeverity.MEDIUM, "The provided data is invalid. Please check your input and try again.", cause);
    }
}
