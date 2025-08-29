package com.mycompany.base.core.validation;

import com.mycompany.base.core.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link ValidationUtils}.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class ValidationUtilsTest {
    
    @Test
    void isValid_WithValidObject_ShouldReturnTrue() {
        // Given
        TestObject validObject = new TestObject("valid", 25);
        
        // When
        boolean isValid = ValidationUtils.isValid(validObject);
        
        // Then
        assertThat(isValid).isTrue();
    }
    
    @Test
    void isValid_WithInvalidObject_ShouldReturnFalse() {
        // Given
        TestObject invalidObject = new TestObject("", -1);
        
        // When
        boolean isValid = ValidationUtils.isValid(invalidObject);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void getViolationCount_WithInvalidObject_ShouldReturnCorrectCount() {
        // Given
        TestObject invalidObject = new TestObject("", -1);
        
        // When
        int violationCount = ValidationUtils.getViolationCount(invalidObject);
        
        // Then
        assertThat(violationCount).isGreaterThan(0);
    }
    
    @Test
    void validateWithResult_WithValidObject_ShouldReturnValidResult() {
        // Given
        TestObject validObject = new TestObject("valid", 25);
        
        // When
        ValidationResult<TestObject> result = ValidationUtils.validateWithResult(validObject);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isTrue();
        assertThat(result.getViolationCount()).isEqualTo(0);
        assertThat(result.getObject()).isEqualTo(validObject);
    }
    
    @Test
    void validateWithResult_WithInvalidObject_ShouldReturnInvalidResult() {
        // Given
        TestObject invalidObject = new TestObject("", -1);
        
        // When
        ValidationResult<TestObject> result = ValidationUtils.validateWithResult(invalidObject);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.isValid()).isFalse();
        assertThat(result.getViolationCount()).isGreaterThan(0);
        assertThat(result.getObject()).isEqualTo(invalidObject);
    }
    
    @Test
    void validateOrThrow_WithValidObject_ShouldNotThrowException() {
        // Given
        TestObject validObject = new TestObject("valid", 25);
        
        // When & Then
        ValidationUtils.validateOrThrow(validObject);
        assertThat(true).isTrue(); // Should not throw exception
    }
    
    @Test
    void validateOrThrow_WithInvalidObject_ShouldThrowValidationException() {
        // Given
        TestObject invalidObject = new TestObject("", -1);
        
        // When & Then
        assertThatThrownBy(() -> ValidationUtils.validateOrThrow(invalidObject))
            .isInstanceOf(ValidationException.class);
    }
    
    @Test
    void validateBatch_WithValidObjects_ShouldReturnValidResults() {
        // Given
        List<TestObject> validObjects = Arrays.asList(
            new TestObject("valid1", 25),
            new TestObject("valid2", 30)
        );
        
        // When
        List<ValidationResult<TestObject>> results = ValidationUtils.validateBatch(validObjects);
        
        // Then
        assertThat(results).hasSize(2);
        results.forEach(result -> assertThat(result.isValid()).isTrue());
    }
    
    @Test
    void clearCache_ShouldNotThrowException() {
        // When & Then
        ValidationUtils.clearCache();
        assertThat(true).isTrue(); // Should not throw exception
    }
    
    @Test
    void getStatistics_ShouldReturnStatistics() {
        // When
        ValidationStatistics stats = ValidationUtils.getStatistics();
        
        // Then
        assertThat(stats).isNotNull();
        assertThat(stats.getCacheSize()).isGreaterThanOrEqualTo(0);
    }
    
    // Test object with validation annotations
    public static class TestObject {
        private String name;
        private int age;
        
        public TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getAge() {
            return age;
        }
        
        public void setAge(int age) {
            this.age = age;
        }
    }
}
