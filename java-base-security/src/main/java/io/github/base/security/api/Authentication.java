package io.github.base.security.api;

import io.github.base.security.model.Claim;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the result of an authentication process.
 * Contains the authenticated principal and additional authentication context.
 */
public interface Authentication {
    
    /**
     * Gets the authenticated principal.
     *
     * @return the authenticated principal, never null
     */
    AuthPrincipal getPrincipal();
    
    /**
     * Gets the authentication method used (e.g., "jwt", "oauth2", "basic").
     *
     * @return the authentication method
     */
    String getMethod();
    
    /**
     * Gets the authorities granted to the authenticated principal.
     * This typically includes roles and permissions.
     *
     * @return a collection of authority strings
     */
    Collection<String> getAuthorities();
    
    /**
     * Gets the claims associated with this authentication.
     *
     * @return a collection of claims
     */
    Collection<Claim> getClaims();
    
    /**
     * Gets a specific claim by name.
     *
     * @param name the claim name
     * @return an optional containing the claim if found
     */
    Optional<Claim> getClaim(String name);
    
    /**
     * Gets a claim value as a string.
     *
     * @param name the claim name
     * @return the claim value as a string, or null if not found
     */
    String getClaimValue(String name);
    
    /**
     * Gets a claim value as a string with a default value.
     *
     * @param name the claim name
     * @param defaultValue the default value to return if claim not found
     * @return the claim value as a string, or the default value if not found
     */
    String getClaimValue(String name, String defaultValue);
    
    /**
     * Checks if this authentication is authenticated (successful).
     *
     * @return true if authenticated, false otherwise
     */
    boolean isAuthenticated();
    
    /**
     * Checks if this authentication has the specified authority.
     *
     * @param authority the authority to check
     * @return true if the authentication has the authority, false otherwise
     */
    boolean hasAuthority(String authority);
    
    /**
     * Checks if this authentication has any of the specified authorities.
     *
     * @param authorities the authorities to check
     * @return true if the authentication has any of the authorities, false otherwise
     */
    boolean hasAnyAuthority(String... authorities);
    
    /**
     * Checks if this authentication has all of the specified authorities.
     *
     * @param authorities the authorities to check
     * @return true if the authentication has all of the authorities, false otherwise
     */
    boolean hasAllAuthorities(String... authorities);
    
    /**
     * Gets additional attributes associated with this authentication.
     *
     * @return a map of additional attributes
     */
    Map<String, Object> getAttributes();
    
    /**
     * Gets a specific attribute by name.
     *
     * @param name the attribute name
     * @return an optional containing the attribute value if found
     */
    Optional<Object> getAttribute(String name);
    
    /**
     * Gets the time when this authentication was created.
     *
     * @return the authentication time
     */
    Instant getAuthenticatedAt();
    
    /**
     * Gets the time when this authentication expires.
     *
     * @return the expiration time, or null if it doesn't expire
     */
    Instant getExpiresAt();
    
    /**
     * Checks if this authentication is expired.
     *
     * @return true if expired, false otherwise
     */
    boolean isExpired();
    
    /**
     * Gets the session ID associated with this authentication.
     *
     * @return the session ID, or null if not applicable
     */
    String getSessionId();
    
    /**
     * Gets the IP address from which this authentication originated.
     *
     * @return the IP address, or null if unknown
     */
    String getClientIpAddress();
    
    /**
     * Gets the user agent from which this authentication originated.
     *
     * @return the user agent, or null if unknown
     */
    String getUserAgent();
}
