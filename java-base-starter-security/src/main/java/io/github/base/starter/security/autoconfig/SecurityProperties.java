package io.github.base.starter.security.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * Configuration properties for base security.
 */
@ConfigurationProperties(prefix = "base.security")
public class SecurityProperties {
    
    /**
     * The security provider type to use.
     */
    private String type = "jwt";
    
    /**
     * JWT configuration.
     */
    private JwtProperties jwt = new JwtProperties();
    
    /**
     * OAuth2 configuration.
     */
    private OAuth2Properties oauth2 = new OAuth2Properties();
    
    /**
     * Security configuration.
     */
    private SecurityConfigProperties config = new SecurityConfigProperties();
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public JwtProperties getJwt() {
        return jwt;
    }
    
    public void setJwt(JwtProperties jwt) {
        this.jwt = jwt;
    }
    
    public OAuth2Properties getOauth2() {
        return oauth2;
    }
    
    public void setOauth2(OAuth2Properties oauth2) {
        this.oauth2 = oauth2;
    }
    
    public SecurityConfigProperties getConfig() {
        return config;
    }
    
    public void setConfig(SecurityConfigProperties config) {
        this.config = config;
    }
    
    /**
     * JWT configuration properties.
     */
    public static class JwtProperties {
        
        /**
         * JWT secret key.
         */
        private String secret = "changeme";
        
        /**
         * JWT expiration duration.
         */
        private Duration expiration = Duration.ofHours(1);
        
        /**
         * JWT issuer.
         */
        private String issuer = "base-security";
        
        /**
         * JWT audience.
         */
        private String audience = "base-security";
        
        /**
         * JWT algorithm.
         */
        private String algorithm = "HS256";
        
        /**
         * JWT refresh token expiration duration.
         */
        private Duration refreshExpiration = Duration.ofDays(7);
        
        public String getSecret() {
            return secret;
        }
        
        public void setSecret(String secret) {
            this.secret = secret;
        }
        
        public Duration getExpiration() {
            return expiration;
        }
        
        public void setExpiration(Duration expiration) {
            this.expiration = expiration;
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
        
        public String getAlgorithm() {
            return algorithm;
        }
        
        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }
        
        public Duration getRefreshExpiration() {
            return refreshExpiration;
        }
        
        public void setRefreshExpiration(Duration refreshExpiration) {
            this.refreshExpiration = refreshExpiration;
        }
    }
    
    /**
     * OAuth2 configuration properties.
     */
    public static class OAuth2Properties {
        
        /**
         * OAuth2 issuer URI.
         */
        private String issuerUri;
        
        /**
         * OAuth2 client ID.
         */
        private String clientId;
        
        /**
         * OAuth2 client secret.
         */
        private String clientSecret;
        
        /**
         * OAuth2 redirect URI.
         */
        private String redirectUri;
        
        /**
         * OAuth2 scopes.
         */
        private List<String> scopes = List.of("openid", "profile", "email");
        
        /**
         * OAuth2 audience.
         */
        private String audience;
        
        public String getIssuerUri() {
            return issuerUri;
        }
        
        public void setIssuerUri(String issuerUri) {
            this.issuerUri = issuerUri;
        }
        
        public String getClientId() {
            return clientId;
        }
        
        public void setClientId(String clientId) {
            this.clientId = clientId;
        }
        
        public String getClientSecret() {
            return clientSecret;
        }
        
        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }
        
        public String getRedirectUri() {
            return redirectUri;
        }
        
        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }
        
        public List<String> getScopes() {
            return scopes;
        }
        
        public void setScopes(List<String> scopes) {
            this.scopes = scopes;
        }
        
        public String getAudience() {
            return audience;
        }
        
        public void setAudience(String audience) {
            this.audience = audience;
        }
    }
    
    /**
     * Security configuration properties.
     */
    public static class SecurityConfigProperties {
        
        /**
         * Enable security auto-configuration.
         */
        private boolean enabled = true;
        
        /**
         * Enable JWT support.
         */
        private boolean jwtEnabled = true;
        
        /**
         * Enable OAuth2 support.
         */
        private boolean oauth2Enabled = true;
        
        /**
         * Enable Spring Security integration.
         */
        private boolean springSecurityEnabled = true;
        
        /**
         * Default roles for authenticated users.
         */
        private List<String> defaultRoles = List.of("USER");
        
        /**
         * Default permissions for authenticated users.
         */
        private List<String> defaultPermissions = List.of();
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public boolean isJwtEnabled() {
            return jwtEnabled;
        }
        
        public void setJwtEnabled(boolean jwtEnabled) {
            this.jwtEnabled = jwtEnabled;
        }
        
        public boolean isOauth2Enabled() {
            return oauth2Enabled;
        }
        
        public void setOauth2Enabled(boolean oauth2Enabled) {
            this.oauth2Enabled = oauth2Enabled;
        }
        
        public boolean isSpringSecurityEnabled() {
            return springSecurityEnabled;
        }
        
        public void setSpringSecurityEnabled(boolean springSecurityEnabled) {
            this.springSecurityEnabled = springSecurityEnabled;
        }
        
        public List<String> getDefaultRoles() {
            return defaultRoles;
        }
        
        public void setDefaultRoles(List<String> defaultRoles) {
            this.defaultRoles = defaultRoles;
        }
        
        public List<String> getDefaultPermissions() {
            return defaultPermissions;
        }
        
        public void setDefaultPermissions(List<String> defaultPermissions) {
            this.defaultPermissions = defaultPermissions;
        }
    }
}
