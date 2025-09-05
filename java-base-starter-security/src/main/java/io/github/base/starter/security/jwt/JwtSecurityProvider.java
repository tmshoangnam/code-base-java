package io.github.base.starter.security.jwt;

import io.github.base.security.api.AuthenticationManager;
import io.github.base.security.api.AuthorizationChecker;
import io.github.base.security.api.SecurityException;
import io.github.base.security.api.TokenService;
import io.github.base.security.spi.SecurityProvider;
import io.github.base.security.spi.SecurityProviderConfiguration;
import io.github.base.starter.security.autoconfig.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JWT implementation of SecurityProvider.
 */
public class JwtSecurityProvider implements SecurityProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtSecurityProvider.class);
    
    private final SecurityProperties.JwtProperties jwtProperties;
    private final SecurityProperties.SecurityConfigProperties configProperties;
    
    private JwtTokenService tokenService;
    private JwtAuthenticationManager authenticationManager;
    private JwtAuthorizationChecker authorizationChecker;
    
    private boolean initialized = false;
    
    public JwtSecurityProvider(SecurityProperties.JwtProperties jwtProperties, 
                              SecurityProperties.SecurityConfigProperties configProperties) {
        this.jwtProperties = jwtProperties;
        this.configProperties = configProperties;
    }
    
    @Override
    public String getName() {
        return "jwt";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public String getDescription() {
        return "JWT-based security provider";
    }
    
    @Override
    public AuthenticationManager getAuthenticationManager() {
        ensureInitialized();
        return authenticationManager;
    }
    
    @Override
    public AuthorizationChecker getAuthorizationChecker() {
        ensureInitialized();
        return authorizationChecker;
    }
    
    @Override
    public TokenService getTokenService() {
        ensureInitialized();
        return tokenService;
    }
    
    @Override
    public boolean isAvailable() {
        return initialized && 
               tokenService != null && 
               authenticationManager != null && 
               authorizationChecker != null;
    }
    
    @Override
    public void initialize(SecurityProviderConfiguration configuration) throws SecurityException {
        try {
            logger.info("Initializing JWT security provider");
            
            // Initialize token service
            this.tokenService = new JwtTokenService(jwtProperties);
            
            // Initialize authentication manager
            this.authenticationManager = new JwtAuthenticationManager(tokenService, configProperties);
            
            // Initialize authorization checker
            this.authorizationChecker = new JwtAuthorizationChecker();
            
            this.initialized = true;
            logger.info("JWT security provider initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize JWT security provider", e);
            throw new SecurityException("Failed to initialize JWT security provider", e);
        }
    }
    
    @Override
    public void shutdown() {
        logger.info("Shutting down JWT security provider");
        this.initialized = false;
        this.tokenService = null;
        this.authenticationManager = null;
        this.authorizationChecker = null;
    }
    
    private void ensureInitialized() {
        if (!initialized) {
            try {
                // Initialize with default configuration if not already initialized
                initialize(new DefaultSecurityProviderConfiguration());
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to initialize JWT security provider", e);
            }
        }
    }
    
    /**
     * Default security provider configuration for JWT.
     */
    private static class DefaultSecurityProviderConfiguration implements SecurityProviderConfiguration {
        
        @Override
        public String getProviderName() {
            return "jwt";
        }
        
        @Override
        public java.util.Optional<Object> getProperty(String name) {
            return java.util.Optional.empty();
        }
        
        @Override
        public String getStringProperty(String name) {
            return null;
        }
        
        @Override
        public String getStringProperty(String name, String defaultValue) {
            return defaultValue;
        }
        
        @Override
        public boolean getBooleanProperty(String name) {
            return false;
        }
        
        @Override
        public boolean getBooleanProperty(String name, boolean defaultValue) {
            return defaultValue;
        }
        
        @Override
        public int getIntProperty(String name) {
            return 0;
        }
        
        @Override
        public int getIntProperty(String name, int defaultValue) {
            return defaultValue;
        }
        
        @Override
        public long getLongProperty(String name) {
            return 0L;
        }
        
        @Override
        public long getLongProperty(String name, long defaultValue) {
            return defaultValue;
        }
        
        @Override
        public java.util.Map<String, Object> getAllProperties() {
            return java.util.Collections.emptyMap();
        }
        
        @Override
        public boolean hasProperty(String name) {
            return false;
        }
        
        @Override
        public boolean isValid() {
            return true;
        }
    }
}
