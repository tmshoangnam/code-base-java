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
        super(new BaseException.Builder(message)
                .errorCode("VALIDATION_ERROR")
                .errorType("VALIDATION")
                .errorCategory(ErrorCategory.VALIDATION)
                .severity(ErrorSeverity.MEDIUM)
                .userMessage("The provided data is invalid. Please check your input and try again.")
                .build());
    }
    
    public ValidationException(String message, Map<String, Object> validationContext) {
        super(new BaseException.Builder(message)
                .errorCode("VALIDATION_ERROR")
                .errorType("VALIDATION")
                .errorCategory(ErrorCategory.VALIDATION)
                .severity(ErrorSeverity.MEDIUM)
                .userMessage("The provided data is invalid. Please check your input and try again.")
                .context(validationContext)
                .build());
    }
    
    public ValidationException(String message, String field, Object value) {
        super(new BaseException.Builder(message)
                .errorCode("VALIDATION_ERROR")
                .errorType("VALIDATION")
                .errorCategory(ErrorCategory.VALIDATION)
                .severity(ErrorSeverity.MEDIUM)
                .userMessage("The field '" + field + "' contains invalid data.")
                .context("field", field)
                .context("value", value)
                .build());
    }
    
    public ValidationException(String message, Throwable cause) {
        super(new BaseException.Builder(message)
                .errorCode("VALIDATION_ERROR")
                .errorType("VALIDATION")
                .errorCategory(ErrorCategory.VALIDATION)
                .severity(ErrorSeverity.MEDIUM)
                .userMessage("The provided data is invalid. Please check your input and try again.")
                .cause(cause)
                .build());
    }
}
