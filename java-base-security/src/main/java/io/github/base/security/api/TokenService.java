package io.github.base.security.api;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Contract for token services.
 * Handles the creation, validation, and parsing of security tokens.
 */
public interface TokenService {
    
    /**
     * Issues a new token for the given principal.
     *
     * @param principal the authenticated principal
     * @return the issued token
     * @throws SecurityException if token issuance fails
     */
    String issueToken(AuthPrincipal principal) throws SecurityException;
    
    /**
     * Issues a new token with the given claims.
     *
     * @param claims the claims to include in the token
     * @return the issued token
     * @throws SecurityException if token issuance fails
     */
    String issueToken(Map<String, Object> claims) throws SecurityException;
    
    /**
     * Issues a new token with the given claims and expiration duration.
     *
     * @param claims the claims to include in the token
     * @param expiration the token expiration duration
     * @return the issued token
     * @throws SecurityException if token issuance fails
     */
    String issueToken(Map<String, Object> claims, Duration expiration) throws SecurityException;
    
    /**
     * Issues a new token for the given principal with custom expiration duration.
     *
     * @param principal the authenticated principal
     * @param expiration the token expiration duration
     * @return the issued token
     * @throws SecurityException if token issuance fails
     */
    String issueToken(AuthPrincipal principal, Duration expiration) throws SecurityException;
    
    /**
     * Validates the given token.
     *
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
    
    /**
     * Parses the given token and returns the claims.
     *
     * @param token the token to parse
     * @return a map of claims from the token
     * @throws SecurityException if token parsing fails
     */
    Map<String, Object> parseToken(String token) throws SecurityException;
    
    /**
     * Gets a specific claim from the given token.
     *
     * @param token the token to parse
     * @param claimName the name of the claim to retrieve
     * @return an optional containing the claim value if found
     * @throws SecurityException if token parsing fails
     */
    Optional<Object> getClaim(String token, String claimName) throws SecurityException;
    
    /**
     * Gets a specific claim value as a string from the given token.
     *
     * @param token the token to parse
     * @param claimName the name of the claim to retrieve
     * @return the claim value as a string, or null if not found
     * @throws SecurityException if token parsing fails
     */
    String getClaimValue(String token, String claimName) throws SecurityException;
    
    /**
     * Gets a specific claim value as a string from the given token with a default value.
     *
     * @param token the token to parse
     * @param claimName the name of the claim to retrieve
     * @param defaultValue the default value to return if claim not found
     * @return the claim value as a string, or the default value if not found
     * @throws SecurityException if token parsing fails
     */
    String getClaimValue(String token, String claimName, String defaultValue) throws SecurityException;
    
    /**
     * Gets the subject (user ID) from the given token.
     *
     * @param token the token to parse
     * @return the subject, or null if not found
     * @throws SecurityException if token parsing fails
     */
    String getSubject(String token) throws SecurityException;
    
    /**
     * Gets the expiration time from the given token.
     *
     * @param token the token to parse
     * @return the expiration time in milliseconds since epoch, or -1 if not found
     * @throws SecurityException if token parsing fails
     */
    long getExpirationTime(String token) throws SecurityException;
    
    /**
     * Checks if the given token is expired.
     *
     * @param token the token to check
     * @return true if the token is expired, false otherwise
     * @throws SecurityException if token parsing fails
     */
    boolean isExpired(String token) throws SecurityException;
    
    /**
     * Refreshes the given token if it's refreshable.
     *
     * @param token the token to refresh
     * @return the refreshed token
     * @throws SecurityException if token refresh fails or is not supported
     */
    String refreshToken(String token) throws SecurityException;
    
    /**
     * Revokes the given token if revocation is supported.
     *
     * @param token the token to revoke
     * @throws SecurityException if token revocation fails or is not supported
     */
    void revokeToken(String token) throws SecurityException;
    
    /**
     * Checks if the given token is revoked.
     *
     * @param token the token to check
     * @return true if the token is revoked, false otherwise
     * @throws SecurityException if token checking fails
     */
    boolean isRevoked(String token) throws SecurityException;
    
    /**
     * Gets the token type (e.g., "JWT", "OAuth2").
     *
     * @return the token type
     */
    String getTokenType();
    
    /**
     * Gets the default token expiration duration.
     *
     * @return the default expiration duration
     */
    Duration getDefaultExpiration();
}
