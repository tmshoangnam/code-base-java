package com.mycompany.base.core.validation.validators;

import com.mycompany.base.core.validation.ValidationUtils;

/**
 * Custom validator for strong password validation.
 * Implements the CustomValidator interface for cross-field validation.
 * 
 * @author mycompany
 * @since 1.0.0
 */
public class StrongPasswordValidator implements ValidationUtils.CustomValidator<String> {
    
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    
    @Override
    public boolean isValid(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        String trimmedPassword = password.trim();
        
        // Check length
        if (trimmedPassword.length() < MIN_LENGTH || trimmedPassword.length() > MAX_LENGTH) {
            return false;
        }
        
        // Check for at least one uppercase letter
        if (!trimmedPassword.matches(".*[A-Z].*")) {
            return false;
        }
        
        // Check for at least one lowercase letter
        if (!trimmedPassword.matches(".*[a-z].*")) {
            return false;
        }
        
        // Check for at least one digit
        if (!trimmedPassword.matches(".*\\d.*")) {
            return false;
        }
        
        // Check for at least one special character
        if (!trimmedPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }
        
        // Check for common weak passwords
        if (isCommonWeakPassword(trimmedPassword)) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String getFieldName() {
        return "password";
    }
    
    @Override
    public String getErrorMessage() {
        return String.format("Password must be between %d and %d characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character", 
                MIN_LENGTH, MAX_LENGTH);
    }
    
    @Override
    public String getErrorCode() {
        return "STRONG_PASSWORD_REQUIRED";
    }
    
    /**
     * Checks if the password is a common weak password.
     * 
     * @param password the password to check
     * @return true if it's a common weak password
     */
    private boolean isCommonWeakPassword(String password) {
        String[] commonPasswords = {
            "password", "123456", "123456789", "qwerty", "abc123",
            "password123", "admin", "letmein", "welcome", "monkey",
            "dragon", "master", "sunshine", "princess", "qwerty123"
        };
        
        String lowerPassword = password.toLowerCase();
        for (String common : commonPasswords) {
            if (lowerPassword.equals(common) || lowerPassword.contains(common)) {
                return true;
            }
        }
        
        return false;
    }
}
