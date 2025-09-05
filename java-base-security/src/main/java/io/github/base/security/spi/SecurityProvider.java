package io.github.base.security.spi;

import io.github.base.security.api.AuthenticationManager;
import io.github.base.security.api.AuthorizationChecker;
import io.github.base.security.api.TokenService;

/**
 * Service Provider Interface for security implementations.
 * This interface allows different security providers to be loaded dynamically.
 */
public interface SecurityProvider {
    
    /**
     * Gets the name of this security provider.
     *
     * @return the provider name
     */
    String getName();
    
    /**
     * Gets the version of this security provider.
     *
     * @return the provider version
     */
    String getVersion();
    
    /**
     * Gets the description of this security provider.
     *
     * @return the provider description
     */
    String getDescription();
    
    /**
     * Gets the authentication manager for this provider.
     *
     * @return the authentication manager
     */
    AuthenticationManager getAuthenticationManager();
    
    /**
     * Gets the authorization checker for this provider.
     *
     * @return the authorization checker
     */
    AuthorizationChecker getAuthorizationChecker();
    
    /**
     * Gets the token service for this provider.
     *
     * @return the token service
     */
    TokenService getTokenService();
    
    /**
     * Checks if this provider is available and properly configured.
     *
     * @return true if the provider is available, false otherwise
     */
    boolean isAvailable();
    
    /**
     * Initializes this provider with the given configuration.
     *
     * @param configuration the provider configuration
     * @throws SecurityException if initialization fails
     */
    void initialize(SecurityProviderConfiguration configuration) throws SecurityException;
    
    /**
     * Shuts down this provider and releases any resources.
     */
    void shutdown();
}
