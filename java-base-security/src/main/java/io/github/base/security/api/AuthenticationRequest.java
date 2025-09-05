package io.github.base.security.api;

import java.util.Map;
import java.util.Optional;

/**
 * Represents a request for authentication.
 * This is the input to the authentication process.
 */
public interface AuthenticationRequest {
    
    /**
     * Gets the type of authentication request (e.g., "password", "token", "oauth2").
     *
     * @return the authentication type
     */
    String getType();
    
    /**
     * Gets the username for username/password authentication.
     *
     * @return the username, or null if not applicable
     */
    String getUsername();
    
    /**
     * Gets the password for username/password authentication.
     *
     * @return the password, or null if not applicable
     */
    String getPassword();
    
    /**
     * Gets the token for token-based authentication.
     *
     * @return the token, or null if not applicable
     */
    String getToken();
    
    /**
     * Gets the authorization code for OAuth2 authentication.
     *
     * @return the authorization code, or null if not applicable
     */
    String getAuthorizationCode();
    
    /**
     * Gets the redirect URI for OAuth2 authentication.
     *
     * @return the redirect URI, or null if not applicable
     */
    String getRedirectUri();
    
    /**
     * Gets the state parameter for OAuth2 authentication.
     *
     * @return the state parameter, or null if not applicable
     */
    String getState();
    
    /**
     * Gets additional parameters for the authentication request.
     *
     * @return a map of additional parameters
     */
    Map<String, Object> getParameters();
    
    /**
     * Gets a specific parameter by name.
     *
     * @param name the parameter name
     * @return an optional containing the parameter value if found
     */
    Optional<Object> getParameter(String name);
    
    /**
     * Gets a parameter value as a string.
     *
     * @param name the parameter name
     * @return the parameter value as a string, or null if not found
     */
    String getParameterValue(String name);
    
    /**
     * Gets a parameter value as a string with a default value.
     *
     * @param name the parameter name
     * @param defaultValue the default value to return if parameter not found
     * @return the parameter value as a string, or the default value if not found
     */
    String getParameterValue(String name, String defaultValue);
    
    /**
     * Gets the client IP address making this request.
     *
     * @return the client IP address, or null if unknown
     */
    String getClientIpAddress();
    
    /**
     * Gets the user agent making this request.
     *
     * @return the user agent, or null if unknown
     */
    String getUserAgent();
    
    /**
     * Gets the session ID associated with this request.
     *
     * @return the session ID, or null if not applicable
     */
    String getSessionId();
}
