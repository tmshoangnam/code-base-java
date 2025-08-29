package com.mycompany.base.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
import java.util.List;

/**
 * Configuration properties for security module.
 * Provides comprehensive security configuration for authentication, authorization,
 * JWT, CORS, and other security features.
 * 
 * @author mycompany
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "my-base.security")
public class SecurityProperties {
    
    @NestedConfigurationProperty
    private Authentication authentication = new Authentication();
    
    @NestedConfigurationProperty
    private Authorization authorization = new Authorization();
    
    @NestedConfigurationProperty
    private Jwt jwt = new Jwt();
    
    @NestedConfigurationProperty
    private Cors cors = new Cors();
    
    @NestedConfigurationProperty
    private RateLimit rateLimit = new RateLimit();
    
    @NestedConfigurationProperty
    private Audit audit = new Audit();
    
    // Getters and Setters
    public Authentication getAuthentication() {
        return authentication;
    }
    
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
    
    public Authorization getAuthorization() {
        return authorization;
    }
    
    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }
    
    public Jwt getJwt() {
        return jwt;
    }
    
    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }
    
    public Cors getCors() {
        return cors;
    }
    
    public void setCors(Cors cors) {
        this.cors = cors;
    }
    
    public RateLimit getRateLimit() {
        return rateLimit;
    }
    
    public void setRateLimit(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }
    
    public Audit getAudit() {
        return audit;
    }
    
    public void setAudit(Audit audit) {
        this.audit = audit;
    }
    
    /**
     * Authentication configuration properties.
     */
    public static class Authentication {
        private boolean enabled = true;
        private String defaultSuccessUrl = "/dashboard";
        private String defaultFailureUrl = "/login?error";
        private int maxLoginAttempts = 5;
        private Duration lockoutDuration = Duration.ofMinutes(15);
        private boolean rememberMeEnabled = true;
        private Duration rememberMeValidity = Duration.ofDays(30);
        private String passwordEncoder = "bcrypt";
        private int passwordEncoderStrength = 12;
        private boolean requirePasswordChange = false;
        private Duration passwordExpiry = Duration.ofDays(90);
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getDefaultSuccessUrl() {
            return defaultSuccessUrl;
        }
        
        public void setDefaultSuccessUrl(String defaultSuccessUrl) {
            this.defaultSuccessUrl = defaultSuccessUrl;
        }
        
        public String getDefaultFailureUrl() {
            return defaultFailureUrl;
        }
        
        public void setDefaultFailureUrl(String defaultFailureUrl) {
            this.defaultFailureUrl = defaultFailureUrl;
        }
        
        public int getMaxLoginAttempts() {
            return maxLoginAttempts;
        }
        
        public void setMaxLoginAttempts(int maxLoginAttempts) {
            this.maxLoginAttempts = maxLoginAttempts;
        }
        
        public Duration getLockoutDuration() {
            return lockoutDuration;
        }
        
        public void setLockoutDuration(Duration lockoutDuration) {
            this.lockoutDuration = lockoutDuration;
        }
        
        public boolean isRememberMeEnabled() {
            return rememberMeEnabled;
        }
        
        public void setRememberMeEnabled(boolean rememberMeEnabled) {
            this.rememberMeEnabled = rememberMeEnabled;
        }
        
        public Duration getRememberMeValidity() {
            return rememberMeValidity;
        }
        
        public void setRememberMeValidity(Duration rememberMeValidity) {
            this.rememberMeValidity = rememberMeValidity;
        }
        
        public String getPasswordEncoder() {
            return passwordEncoder;
        }
        
        public void setPasswordEncoder(String passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
        }
        
        public int getPasswordEncoderStrength() {
            return passwordEncoderStrength;
        }
        
        public void setPasswordEncoderStrength(int passwordEncoderStrength) {
            this.passwordEncoderStrength = passwordEncoderStrength;
        }
        
        public boolean isRequirePasswordChange() {
            return requirePasswordChange;
        }
        
        public void setRequirePasswordChange(boolean requirePasswordChange) {
            this.requirePasswordChange = requirePasswordChange;
        }
        
        public Duration getPasswordExpiry() {
            return passwordExpiry;
        }
        
        public void setPasswordExpiry(Duration passwordExpiry) {
            this.passwordExpiry = passwordExpiry;
        }
    }
    
    /**
     * Authorization configuration properties.
     */
    public static class Authorization {
        private boolean enabled = true;
        private String defaultRole = "USER";
        private List<String> adminRoles = List.of("ADMIN", "SUPER_ADMIN");
        private List<String> publicEndpoints = List.of("/public/**", "/health/**", "/actuator/**");
        private List<String> adminEndpoints = List.of("/admin/**", "/management/**");
        private boolean methodSecurityEnabled = true;
        private boolean webSecurityEnabled = true;
        private boolean csrfEnabled = true;
        private boolean sessionManagementEnabled = true;
        private int maxSessions = 1;
        private boolean preventLogin = false;
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getDefaultRole() {
            return defaultRole;
        }
        
        public void setDefaultRole(String defaultRole) {
            this.defaultRole = defaultRole;
        }
        
        public List<String> getAdminRoles() {
            return adminRoles;
        }
        
        public void setAdminRoles(List<String> adminRoles) {
            this.adminRoles = adminRoles;
        }
        
        public List<String> getPublicEndpoints() {
            return publicEndpoints;
        }
        
        public void setPublicEndpoints(List<String> publicEndpoints) {
            this.publicEndpoints = publicEndpoints;
        }
        
        public List<String> getAdminEndpoints() {
            return adminEndpoints;
        }
        
        public void setAdminEndpoints(List<String> adminEndpoints) {
            this.adminEndpoints = adminEndpoints;
        }
        
        public boolean isMethodSecurityEnabled() {
            return methodSecurityEnabled;
        }
        
        public void setMethodSecurityEnabled(boolean methodSecurityEnabled) {
            this.methodSecurityEnabled = methodSecurityEnabled;
        }
        
        public boolean isWebSecurityEnabled() {
            return webSecurityEnabled;
        }
        
        public void setWebSecurityEnabled(boolean webSecurityEnabled) {
            this.webSecurityEnabled = webSecurityEnabled;
        }
        
        public boolean isCsrfEnabled() {
            return csrfEnabled;
        }
        
        public void setCsrfEnabled(boolean csrfEnabled) {
            this.csrfEnabled = csrfEnabled;
        }
        
        public boolean isSessionManagementEnabled() {
            return sessionManagementEnabled;
        }
        
        public void setSessionManagementEnabled(boolean sessionManagementEnabled) {
            this.sessionManagementEnabled = sessionManagementEnabled;
        }
        
        public int getMaxSessions() {
            return maxSessions;
        }
        
        public void setMaxSessions(int maxSessions) {
            this.maxSessions = maxSessions;
        }
        
        public boolean isPreventLogin() {
            return preventLogin;
        }
        
        public void setPreventLogin(boolean preventLogin) {
            this.preventLogin = preventLogin;
        }
    }
    
    /**
     * JWT configuration properties.
     */
    public static class Jwt {
        private boolean enabled = true;
        private String secret = "default-secret-key-change-in-production";
        private Duration accessTokenValidity = Duration.ofMinutes(30);
        private Duration refreshTokenValidity = Duration.ofDays(7);
        private String issuer = "mycompany";
        private String audience = "mycompany-users";
        private String headerName = "Authorization";
        private String headerPrefix = "Bearer ";
        private boolean enableRefreshToken = true;
        private int refreshTokenRotationThreshold = 3;
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getSecret() {
            return secret;
        }
        
        public void setSecret(String secret) {
            this.secret = secret;
        }
        
        public Duration getAccessTokenValidity() {
            return accessTokenValidity;
        }
        
        public void setAccessTokenValidity(Duration accessTokenValidity) {
            this.accessTokenValidity = accessTokenValidity;
        }
        
        public Duration getRefreshTokenValidity() {
            return refreshTokenValidity;
        }
        
        public void setRefreshTokenValidity(Duration refreshTokenValidity) {
            this.refreshTokenValidity = refreshTokenValidity;
        }
        
        public String getIssuer() {
            return issuer;
        }
        
        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }
        
        public String getAudience() {
            return audience;
        }
        
        public void setAudience(String audience) {
            this.audience = audience;
        }
        
        public String getHeaderName() {
            return headerName;
        }
        
        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }
        
        public String getHeaderPrefix() {
            return headerPrefix;
        }
        
        public void setHeaderPrefix(String headerPrefix) {
            this.headerPrefix = headerPrefix;
        }
        
        public boolean isEnableRefreshToken() {
            return enableRefreshToken;
        }
        
        public void setEnableRefreshToken(boolean enableRefreshToken) {
            this.enableRefreshToken = enableRefreshToken;
        }
        
        public int getRefreshTokenRotationThreshold() {
            return refreshTokenRotationThreshold;
        }
        
        public void setRefreshTokenRotationThreshold(int refreshTokenRotationThreshold) {
            this.refreshTokenRotationThreshold = refreshTokenRotationThreshold;
        }
    }
    
    /**
     * CORS configuration properties.
     */
    public static class Cors {
        private boolean enabled = true;
        private List<String> allowedOrigins = List.of("*");
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
        private List<String> allowedHeaders = List.of("*");
        private List<String> exposedHeaders = List.of("Authorization", "Content-Type");
        private boolean allowCredentials = true;
        private Duration maxAge = Duration.ofHours(1);
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }
        
        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
        
        public List<String> getAllowedMethods() {
            return allowedMethods;
        }
        
        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }
        
        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }
        
        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }
        
        public List<String> getExposedHeaders() {
            return exposedHeaders;
        }
        
        public void setExposedHeaders(List<String> exposedHeaders) {
            this.exposedHeaders = exposedHeaders;
        }
        
        public boolean isAllowCredentials() {
            return allowCredentials;
        }
        
        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }
        
        public Duration getMaxAge() {
            return maxAge;
        }
        
        public void setMaxAge(Duration maxAge) {
            this.maxAge = maxAge;
        }
    }
    
    /**
     * Rate limiting configuration properties.
     */
    public static class RateLimit {
        private boolean enabled = true;
        private int maxRequestsPerMinute = 100;
        private int maxRequestsPerHour = 1000;
        private int maxRequestsPerDay = 10000;
        private Duration windowSize = Duration.ofMinutes(1);
        private boolean enableIpBasedLimiting = true;
        private boolean enableUserBasedLimiting = true;
        private List<String> excludedPaths = List.of("/health/**", "/actuator/**");
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public int getMaxRequestsPerMinute() {
            return maxRequestsPerMinute;
        }
        
        public void setMaxRequestsPerMinute(int maxRequestsPerMinute) {
            this.maxRequestsPerMinute = maxRequestsPerMinute;
        }
        
        public int getMaxRequestsPerHour() {
            return maxRequestsPerHour;
        }
        
        public void setMaxRequestsPerHour(int maxRequestsPerHour) {
            this.maxRequestsPerHour = maxRequestsPerHour;
        }
        
        public int getMaxRequestsPerDay() {
            return maxRequestsPerDay;
        }
        
        public void setMaxRequestsPerDay(int maxRequestsPerDay) {
            this.maxRequestsPerDay = maxRequestsPerDay;
        }
        
        public Duration getWindowSize() {
            return windowSize;
        }
        
        public void setWindowSize(Duration windowSize) {
            this.windowSize = windowSize;
        }
        
        public boolean isEnableIpBasedLimiting() {
            return enableIpBasedLimiting;
        }
        
        public void setEnableIpBasedLimiting(boolean enableIpBasedLimiting) {
            this.enableIpBasedLimiting = enableIpBasedLimiting;
        }
        
        public boolean isEnableUserBasedLimiting() {
            return enableUserBasedLimiting;
        }
        
        public void setEnableUserBasedLimiting(boolean enableUserBasedLimiting) {
            this.enableUserBasedLimiting = enableUserBasedLimiting;
        }
        
        public List<String> getExcludedPaths() {
            return excludedPaths;
        }
        
        public void setExcludedPaths(List<String> excludedPaths) {
            this.excludedPaths = excludedPaths;
        }
    }
    
    /**
     * Audit configuration properties.
     */
    public static class Audit {
        private boolean enabled = true;
        private boolean logLoginAttempts = true;
        private boolean logLogoutEvents = true;
        private boolean logAuthorizationFailures = true;
        private boolean logPasswordChanges = true;
        private boolean logRoleChanges = true;
        private List<String> sensitiveOperations = List.of("DELETE", "UPDATE_ROLE", "CHANGE_PASSWORD");
        private Duration retentionPeriod = Duration.ofDays(365);
        
        // Getters and Setters
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public boolean isLogLoginAttempts() {
            return logLoginAttempts;
        }
        
        public void setLogLoginAttempts(boolean logLoginAttempts) {
            this.logLoginAttempts = logLoginAttempts;
        }
        
        public boolean isLogLogoutEvents() {
            return logLogoutEvents;
        }
        
        public void setLogLogoutEvents(boolean logLogoutEvents) {
            this.logLogoutEvents = logLogoutEvents;
        }
        
        public boolean isLogAuthorizationFailures() {
            return logAuthorizationFailures;
        }
        
        public void setLogAuthorizationFailures(boolean logAuthorizationFailures) {
            this.logAuthorizationFailures = logAuthorizationFailures;
        }
        
        public boolean isLogPasswordChanges() {
            return logPasswordChanges;
        }
        
        public void setLogPasswordChanges(boolean logPasswordChanges) {
            this.logPasswordChanges = logPasswordChanges;
        }
        
        public boolean isLogRoleChanges() {
            return logRoleChanges;
        }
        
        public void setLogRoleChanges(boolean logRoleChanges) {
            this.logRoleChanges = logRoleChanges;
        }
        
        public List<String> getSensitiveOperations() {
            return sensitiveOperations;
        }
        
        public void setSensitiveOperations(List<String> sensitiveOperations) {
            this.sensitiveOperations = sensitiveOperations;
        }
        
        public Duration getRetentionPeriod() {
            return retentionPeriod;
        }
        
        public void setRetentionPeriod(Duration retentionPeriod) {
            this.retentionPeriod = retentionPeriod;
        }
    }
}
