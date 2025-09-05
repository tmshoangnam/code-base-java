package io.github.base.security.api;

/**
 * Standardized error codes for security operations.
 */
public final class ErrorCodes {
    
    // Authentication errors
    public static final String AUTHENTICATION_FAILED = "AUTH_001";
    public static final String INVALID_CREDENTIALS = "AUTH_002";
    public static final String ACCOUNT_LOCKED = "AUTH_003";
    public static final String ACCOUNT_DISABLED = "AUTH_004";
    public static final String ACCOUNT_EXPIRED = "AUTH_005";
    public static final String CREDENTIALS_EXPIRED = "AUTH_006";
    public static final String AUTHENTICATION_METHOD_NOT_SUPPORTED = "AUTH_007";
    public static final String AUTHENTICATION_REQUEST_INVALID = "AUTH_008";
    
    // Authorization errors
    public static final String ACCESS_DENIED = "AUTHZ_001";
    public static final String INSUFFICIENT_PERMISSIONS = "AUTHZ_002";
    public static final String INSUFFICIENT_ROLES = "AUTHZ_003";
    public static final String RESOURCE_ACCESS_DENIED = "AUTHZ_004";
    
    // Token errors
    public static final String TOKEN_INVALID = "TOKEN_001";
    public static final String TOKEN_EXPIRED = "TOKEN_002";
    public static final String TOKEN_MALFORMED = "TOKEN_003";
    public static final String TOKEN_REVOKED = "TOKEN_004";
    public static final String TOKEN_ISSUANCE_FAILED = "TOKEN_005";
    public static final String TOKEN_PARSING_FAILED = "TOKEN_006";
    public static final String TOKEN_REFRESH_FAILED = "TOKEN_007";
    public static final String TOKEN_REVOCATION_FAILED = "TOKEN_008";
    
    // Principal errors
    public static final String PRINCIPAL_NOT_FOUND = "PRINCIPAL_001";
    public static final String PRINCIPAL_INVALID = "PRINCIPAL_002";
    public static final String PRINCIPAL_CREATION_FAILED = "PRINCIPAL_003";
    
    // Permission errors
    public static final String PERMISSION_NOT_FOUND = "PERMISSION_001";
    public static final String PERMISSION_INVALID = "PERMISSION_002";
    public static final String PERMISSION_CREATION_FAILED = "PERMISSION_003";
    
    // Role errors
    public static final String ROLE_NOT_FOUND = "ROLE_001";
    public static final String ROLE_INVALID = "ROLE_002";
    public static final String ROLE_CREATION_FAILED = "ROLE_003";
    
    // Configuration errors
    public static final String CONFIGURATION_INVALID = "CONFIG_001";
    public static final String CONFIGURATION_MISSING = "CONFIG_002";
    public static final String CONFIGURATION_LOADING_FAILED = "CONFIG_003";
    
    // Provider errors
    public static final String PROVIDER_NOT_FOUND = "PROVIDER_001";
    public static final String PROVIDER_INVALID = "PROVIDER_002";
    public static final String PROVIDER_LOADING_FAILED = "PROVIDER_003";
    public static final String PROVIDER_INITIALIZATION_FAILED = "PROVIDER_004";
    
    // Validation errors
    public static final String VALIDATION_FAILED = "VALIDATION_001";
    public static final String REQUIRED_FIELD_MISSING = "VALIDATION_002";
    public static final String FIELD_VALUE_INVALID = "VALIDATION_003";
    public static final String FIELD_FORMAT_INVALID = "VALIDATION_004";
    
    // System errors
    public static final String INTERNAL_ERROR = "SYSTEM_001";
    public static final String SERVICE_UNAVAILABLE = "SYSTEM_002";
    public static final String TIMEOUT = "SYSTEM_003";
    public static final String RATE_LIMIT_EXCEEDED = "SYSTEM_004";
    
    private ErrorCodes() {
        // Utility class
    }
}
