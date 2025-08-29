package com.mycompany.base.core.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for enhanced {@link BaseException}.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class BaseExceptionTest {
    
    @Test
    void builder_WithAllFields_ShouldCreateException() {
        // Given
        String message = "Test error message";
        String errorCode = "TEST_ERROR";
        String errorType = "TEST_TYPE";
        BaseException.ErrorCategory category = BaseException.ErrorCategory.VALIDATION;
        BaseException.ErrorSeverity severity = BaseException.ErrorSeverity.HIGH;
        Throwable cause = new RuntimeException("Root cause");
        Instant timestamp = Instant.now();
        Map<String, Object> context = Map.of("key1", "value1", "key2", "value2");
        String userMessage = "User friendly message";
        String technicalDetails = "Technical details for developers";
        
        // When
        BaseException exception = new BaseException.Builder(message)
                .errorCode(errorCode)
                .errorType(errorType)
                .errorCategory(category)
                .severity(severity)
                .cause(cause)
                .timestamp(timestamp)
                .context(context)
                .userMessage(userMessage)
                .technicalDetails(technicalDetails)
                .build();
        
        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getErrorCode()).isEqualTo(errorCode);
        assertThat(exception.getErrorType()).isEqualTo(errorType);
        assertThat(exception.getErrorCategory()).isEqualTo(category);
        assertThat(exception.getSeverity()).isEqualTo(severity);
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getTimestamp()).isEqualTo(timestamp);
        assertThat(exception.getContext()).containsAllEntriesOf(context);
        assertThat(exception.getUserMessage()).isEqualTo(userMessage);
        assertThat(exception.getTechnicalDetails()).isEqualTo(technicalDetails);
    }
    
    @Test
    void builder_WithDefaultValues_ShouldUseDefaults() {
        // When
        BaseException exception = new BaseException.Builder("Test message").build();
        
        // Then
        assertThat(exception.getErrorCode()).isEqualTo("BASE_ERROR");
        assertThat(exception.getErrorType()).isEqualTo("GENERAL");
        assertThat(exception.getErrorCategory()).isEqualTo(BaseException.ErrorCategory.TECHNICAL);
        assertThat(exception.getSeverity()).isEqualTo(BaseException.ErrorSeverity.MEDIUM);
        assertThat(exception.getTimestamp()).isNotNull();
        assertThat(exception.getContext()).isEmpty();
    }
    
    @Test
    void addContext_WithKeyValue_ShouldAddToContext() {
        // Given
        BaseException exception = new BaseException.Builder("Test message").build();
        
        // When
        BaseException result = exception.addContext("testKey", "testValue");
        
        // Then
        assertThat(result).isSameAs(exception);
        assertThat(exception.getContext()).containsEntry("testKey", "testValue");
    }
    
    @Test
    void addContext_WithMap_ShouldAddAllEntries() {
        // Given
        BaseException exception = new BaseException.Builder("Test message").build();
        Map<String, Object> contextMap = Map.of("key1", "value1", "key2", "value2");
        
        // When
        BaseException result = exception.addContext(contextMap);
        
        // Then
        assertThat(result).isSameAs(exception);
        assertThat(exception.getContext()).containsAllEntriesOf(contextMap);
    }
    
    @Test
    void getContextValue_WithExistingKey_ShouldReturnValue() {
        // Given
        BaseException exception = new BaseException.Builder("Test message")
                .context("testKey", "testValue")
                .build();
        
        // When
        String value = exception.getContextValue("testKey");
        
        // Then
        assertThat(value).isEqualTo("testValue");
    }
    
    @Test
    void getContextValue_WithNonExistingKey_ShouldReturnNull() {
        // Given
        BaseException exception = new BaseException.Builder("Test message").build();
        
        // When
        String value = exception.getContextValue("nonExistingKey");
        
        // Then
        assertThat(value).isNull();
    }
    
    @Test
    void hasContext_WithExistingKey_ShouldReturnTrue() {
        // Given
        BaseException exception = new BaseException.Builder("Test message")
                .context("testKey", "testValue")
                .build();
        
        // When
        boolean hasContext = exception.hasContext("testKey");
        
        // Then
        assertThat(hasContext).isTrue();
    }
    
    @Test
    void hasContext_WithNonExistingKey_ShouldReturnFalse() {
        // Given
        BaseException exception = new BaseException.Builder("Test message").build();
        
        // When
        boolean hasContext = exception.hasContext("nonExistingKey");
        
        // Then
        assertThat(hasContext).isFalse();
    }
    
    @Test
    void getFullMessage_WithContext_ShouldIncludeContext() {
        // Given
        BaseException exception = new BaseException.Builder("Test message")
                .context("key1", "value1")
                .context("key2", "value2")
                .build();
        
        // When
        String fullMessage = exception.getFullMessage();
        
        // Then
        assertThat(fullMessage).contains("Test message");
        assertThat(fullMessage).contains("key1=value1");
        assertThat(fullMessage).contains("key2=value2");
    }
    
    @Test
    void getFullMessage_WithoutContext_ShouldReturnOriginalMessage() {
        // Given
        BaseException exception = new BaseException.Builder("Test message").build();
        
        // When
        String fullMessage = exception.getFullMessage();
        
        // Then
        assertThat(fullMessage).isEqualTo("Test message");
    }
    
    @Test
    void convenienceMethods_ShouldCreateExceptions() {
        // When
        BaseException exception1 = BaseException.of("Message 1");
        BaseException exception2 = BaseException.of("Message 2", "CODE2", "TYPE2");
        BaseException exception3 = BaseException.of("Message 3", "CODE3", "TYPE3", new RuntimeException("Cause"));
        
        // Then
        assertThat(exception1.getMessage()).isEqualTo("Message 1");
        assertThat(exception2.getErrorCode()).isEqualTo("CODE2");
        assertThat(exception2.getErrorType()).isEqualTo("TYPE2");
        assertThat(exception3.getCause()).isNotNull();
        assertThat(exception3.getCause().getMessage()).isEqualTo("Cause");
    }
    
    @Test
    void context_IsImmutable_ShouldNotAllowModification() {
        // Given
        BaseException exception = new BaseException.Builder("Test message")
                .context("key1", "value1")
                .build();
        
        // When & Then
        assertThatThrownBy(() -> {
            exception.getContext().put("key2", "value2");
        }).isInstanceOf(UnsupportedOperationException.class);
    }
    
    @Test
    void equals_WithSameValues_ShouldReturnTrue() {
        // Given
        BaseException exception1 = new BaseException.Builder("Test message")
                .errorCode("TEST_CODE")
                .errorType("TEST_TYPE")
                .build();
        
        BaseException exception2 = new BaseException.Builder("Test message")
                .errorCode("TEST_CODE")
                .errorType("TEST_TYPE")
                .build();
        
        // When & Then
        assertThat(exception1).isEqualTo(exception2);
        assertThat(exception1.hashCode()).isEqualTo(exception2.hashCode());
    }
    
    @Test
    void equals_WithDifferentValues_ShouldReturnFalse() {
        // Given
        BaseException exception1 = new BaseException.Builder("Test message")
                .errorCode("TEST_CODE1")
                .build();
        
        BaseException exception2 = new BaseException.Builder("Test message")
                .errorCode("TEST_CODE2")
                .build();
        
        // When & Then
        assertThat(exception1).isNotEqualTo(exception2);
    }
    
    @Test
    void toString_ShouldIncludeAllFields() {
        // Given
        BaseException exception = new BaseException.Builder("Test message")
                .errorCode("TEST_CODE")
                .errorType("TEST_TYPE")
                .errorCategory(BaseException.ErrorCategory.VALIDATION)
                .severity(BaseException.ErrorSeverity.HIGH)
                .build();
        
        // When
        String toString = exception.toString();
        
        // Then
        assertThat(toString).contains("Test message");
        assertThat(toString).contains("TEST_CODE");
        assertThat(toString).contains("TEST_TYPE");
        assertThat(toString).contains("VALIDATION");
        assertThat(toString).contains("HIGH");
    }
}
