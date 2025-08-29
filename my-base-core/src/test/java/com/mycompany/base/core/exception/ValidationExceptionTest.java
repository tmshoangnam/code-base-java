package com.mycompany.base.core.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ValidationException}.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class ValidationExceptionTest {
    
    @Test
    void constructor_WithMessage_ShouldCreateException() {
        // Given
        String message = "Validation failed";
        
        // When
        ValidationException exception = new ValidationException(message);
        
        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getErrorCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(exception.getErrorType()).isEqualTo("VALIDATION");
        assertThat(exception.getErrorCategory()).isEqualTo(BaseException.ErrorCategory.VALIDATION);
        assertThat(exception.getSeverity()).isEqualTo(BaseException.ErrorSeverity.MEDIUM);
        assertThat(exception.getUserMessage()).contains("invalid");
    }
    
    @Test
    void constructor_WithMessageAndContext_ShouldCreateException() {
        // Given
        String message = "Validation failed";
        Map<String, Object> context = new HashMap<>();
        context.put("field", "email");
        context.put("value", "invalid-email");
        
        // When
        ValidationException exception = new ValidationException(message, context);
        
        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getContext()).containsEntry("field", "email");
        assertThat(exception.getContext()).containsEntry("value", "invalid-email");
    }
    
    @Test
    void constructor_WithMessageFieldAndValue_ShouldCreateException() {
        // Given
        String message = "Field validation failed";
        String field = "age";
        Object value = -5;
        
        // When
        ValidationException exception = new ValidationException(message, field, value);
        
        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getContext()).containsEntry("field", field);
        assertThat(exception.getContext()).containsEntry("value", value);
        assertThat(exception.getUserMessage()).contains(field);
    }
    
    @Test
    void constructor_WithMessageAndCause_ShouldCreateException() {
        // Given
        String message = "Validation failed";
        Throwable cause = new RuntimeException("Root cause");
        
        // When
        ValidationException exception = new ValidationException(message, cause);
        
        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
    
    @Test
    void errorCode_ShouldBeValidationError() {
        // When
        ValidationException exception = new ValidationException("Test");
        
        // Then
        assertThat(exception.getErrorCode()).isEqualTo("VALIDATION_ERROR");
    }
    
    @Test
    void errorType_ShouldBeValidation() {
        // When
        ValidationException exception = new ValidationException("Test");
        
        // Then
        assertThat(exception.getErrorType()).isEqualTo("VALIDATION");
    }
    
    @Test
    void errorCategory_ShouldBeValidation() {
        // When
        ValidationException exception = new ValidationException("Test");
        
        // Then
        assertThat(exception.getErrorCategory()).isEqualTo(BaseException.ErrorCategory.VALIDATION);
    }
    
    @Test
    void severity_ShouldBeMedium() {
        // When
        ValidationException exception = new ValidationException("Test");
        
        // Then
        assertThat(exception.getSeverity()).isEqualTo(BaseException.ErrorSeverity.MEDIUM);
    }
    
    @Test
    void userMessage_ShouldBeUserFriendly() {
        // When
        ValidationException exception = new ValidationException("Test");
        
        // Then
        assertThat(exception.getUserMessage()).isNotNull();
        assertThat(exception.getUserMessage()).contains("invalid");
        assertThat(exception.getUserMessage()).contains("check");
    }
}
