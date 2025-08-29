package com.mycompany.base.core.validation.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link StrongPasswordValidator}.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class StrongPasswordValidatorTest {
    
    private final StrongPasswordValidator validator = new StrongPasswordValidator();
    
    @Test
    void isValid_WithStrongPassword_ShouldReturnTrue() {
        // Given
        String strongPassword = "StrongPass123!";
        
        // When
        boolean isValid = validator.isValid(strongPassword);
        
        // Then
        assertThat(isValid).isTrue();
    }
    
    @Test
    void isValid_WithWeakPassword_ShouldReturnFalse() {
        // Given
        String weakPassword = "weak";
        
        // When
        boolean isValid = validator.isValid(weakPassword);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void isValid_WithNullPassword_ShouldReturnFalse() {
        // When
        boolean isValid = validator.isValid(null);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void isValid_WithEmptyPassword_ShouldReturnFalse() {
        // Given
        String emptyPassword = "";
        
        // When
        boolean isValid = validator.isValid(emptyPassword);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void isValid_WithWhitespaceOnlyPassword_ShouldReturnFalse() {
        // Given
        String whitespacePassword = "   ";
        
        // When
        boolean isValid = validator.isValid(whitespacePassword);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void isValid_WithPasswordMissingUppercase_ShouldReturnFalse() {
        // Given
        String password = "strongpass123!";
        
        // When
        boolean isValid = validator.isValid(password);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void isValid_WithPasswordMissingLowercase_ShouldReturnFalse() {
        // Given
        String password = "STRONGPASS123!";
        
        // When
        boolean isValid = validator.isValid(password);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void isValid_WithPasswordMissingDigit_ShouldReturnFalse() {
        // Given
        String password = "StrongPass!";
        
        // When
        boolean isValid = validator.isValid(password);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void isValid_WithPasswordMissingSpecialCharacter_ShouldReturnFalse() {
        // Given
        String password = "StrongPass123";
        
        // When
        boolean isValid = validator.isValid(password);
        
        // Then
        assertThat(isValid).isFalse();
    }
    
    @Test
    void isValid_WithCommonWeakPassword_ShouldReturnFalse() {
        // Given
        String[] weakPasswords = {
            "password", "123456", "qwerty", "abc123", "admin"
        };
        
        // When & Then
        for (String weakPassword : weakPasswords) {
            boolean isValid = validator.isValid(weakPassword);
            assertThat(isValid).as("Password: " + weakPassword).isFalse();
        }
    }
    
    @Test
    void getFieldName_ShouldReturnPassword() {
        // When
        String fieldName = validator.getFieldName();
        
        // Then
        assertThat(fieldName).isEqualTo("password");
    }
    
    @Test
    void getErrorMessage_ShouldContainRequirements() {
        // When
        String errorMessage = validator.getErrorMessage();
        
        // Then
        assertThat(errorMessage).contains("8");
        assertThat(errorMessage).contains("128");
        assertThat(errorMessage).contains("uppercase");
        assertThat(errorMessage).contains("lowercase");
        assertThat(errorMessage).contains("digit");
        assertThat(errorMessage).contains("special character");
    }
    
    @Test
    void getErrorCode_ShouldReturnCorrectCode() {
        // When
        String errorCode = validator.getErrorCode();
        
        // Then
        assertThat(errorCode).isEqualTo("STRONG_PASSWORD_REQUIRED");
    }
}
