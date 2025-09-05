package io.github.base.security.api;

import io.github.base.security.model.Claim;
import io.github.base.security.model.Role;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Represents an authenticated principal in the security system.
 * This is the core representation of a user after successful authentication.
 */
public interface AuthPrincipal {
    
    /**
     * Gets the unique identifier of the principal.
     *
     * @return the principal ID
     */
    String getId();
    
    /**
     * Gets the username of the principal.
     *
     * @return the username
     */
    String getUsername();
    
    /**
     * Gets the display name of the principal.
     *
     * @return the display name, may be the same as username
     */
    String getDisplayName();
    
    /**
     * Gets the email address of the principal.
     *
     * @return the email address, may be null
     */
    String getEmail();
    
    /**
     * Gets the roles assigned to this principal.
     *
     * @return a collection of roles
     */
    Collection<Role> getRoles();
    
    /**
     * Gets the claims associated with this principal.
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
     * Checks if this principal has the specified role.
     *
     * @param roleName the role name to check
     * @return true if the principal has the role, false otherwise
     */
    boolean hasRole(String roleName);
    
    /**
     * Checks if this principal has any of the specified roles.
     *
     * @param roleNames the role names to check
     * @return true if the principal has any of the roles, false otherwise
     */
    boolean hasAnyRole(String... roleNames);
    
    /**
     * Checks if this principal has all of the specified roles.
     *
     * @param roleNames the role names to check
     * @return true if the principal has all of the roles, false otherwise
     */
    boolean hasAllRoles(String... roleNames);
    
    /**
     * Checks if this principal has the specified permission.
     *
     * @param permissionName the permission name to check
     * @return true if the principal has the permission, false otherwise
     */
    boolean hasPermission(String permissionName);
    
    /**
     * Checks if this principal has any of the specified permissions.
     *
     * @param permissionNames the permission names to check
     * @return true if the principal has any of the permissions, false otherwise
     */
    boolean hasAnyPermission(String... permissionNames);
    
    /**
     * Checks if this principal has all of the specified permissions.
     *
     * @param permissionNames the permission names to check
     * @return true if the principal has all of the permissions, false otherwise
     */
    boolean hasAllPermissions(String... permissionNames);
    
    /**
     * Gets additional attributes associated with this principal.
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
     * Checks if this principal is active (not disabled, locked, etc.).
     *
     * @return true if the principal is active, false otherwise
     */
    boolean isActive();
    
    /**
     * Checks if this principal is expired.
     *
     * @return true if the principal is expired, false otherwise
     */
    boolean isExpired();
    
    /**
     * Gets the time when this principal was created.
     *
     * @return the creation time in milliseconds since epoch, or -1 if unknown
     */
    long getCreatedAt();
    
    /**
     * Gets the time when this principal was last updated.
     *
     * @return the last update time in milliseconds since epoch, or -1 if unknown
     */
    long getUpdatedAt();
}
