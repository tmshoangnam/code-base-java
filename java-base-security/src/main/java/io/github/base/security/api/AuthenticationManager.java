package io.github.base.security.api;

import java.util.concurrent.CompletableFuture;

/**
 * Contract for authentication managers.
 * Handles the authentication process for different authentication types.
 */
public interface AuthenticationManager {
    
    /**
     * Authenticates the given authentication request.
     *
     * @param request the authentication request
     * @return the authentication result
     * @throws SecurityException if authentication fails
     */
    Authentication authenticate(AuthenticationRequest request) throws SecurityException;
    
    /**
     * Authenticates the given authentication request asynchronously.
     *
     * @param request the authentication request
     * @return a CompletableFuture containing the authentication result
     */
    CompletableFuture<Authentication> authenticateAsync(AuthenticationRequest request);
    
    /**
     * Checks if this authentication manager supports the given authentication type.
     *
     * @param type the authentication type
     * @return true if supported, false otherwise
     */
    boolean supports(String type);
    
    /**
     * Gets the authentication types supported by this manager.
     *
     * @return an array of supported authentication types
     */
    String[] getSupportedTypes();
    
    /**
     * Validates the given authentication request without performing authentication.
     * This can be used to check if the request is well-formed.
     *
     * @param request the authentication request to validate
     * @return true if the request is valid, false otherwise
     */
    boolean validate(AuthenticationRequest request);
}
