package io.github.base.security.api;

import io.github.base.security.model.Permission;

import java.util.Collection;

/**
 * Contract for authorization checkers.
 * Handles the authorization process to determine if a principal has the required permissions.
 */
public interface AuthorizationChecker {
    
    /**
     * Checks if the given principal has the specified permission.
     *
     * @param principal the authenticated principal
     * @param permission the permission to check
     * @return true if the principal has the permission, false otherwise
     */
    boolean hasPermission(AuthPrincipal principal, Permission permission);
    
    /**
     * Checks if the given principal has the specified permission by name.
     *
     * @param principal the authenticated principal
     * @param permissionName the permission name to check
     * @return true if the principal has the permission, false otherwise
     */
    boolean hasPermission(AuthPrincipal principal, String permissionName);
    
    /**
     * Checks if the given principal has any of the specified permissions.
     *
     * @param principal the authenticated principal
     * @param permissions the permissions to check
     * @return true if the principal has any of the permissions, false otherwise
     */
    boolean hasAnyPermission(AuthPrincipal principal, Permission... permissions);
    
    /**
     * Checks if the given principal has any of the specified permissions by name.
     *
     * @param principal the authenticated principal
     * @param permissionNames the permission names to check
     * @return true if the principal has any of the permissions, false otherwise
     */
    boolean hasAnyPermission(AuthPrincipal principal, String... permissionNames);
    
    /**
     * Checks if the given principal has all of the specified permissions.
     *
     * @param principal the authenticated principal
     * @param permissions the permissions to check
     * @return true if the principal has all of the permissions, false otherwise
     */
    boolean hasAllPermissions(AuthPrincipal principal, Permission... permissions);
    
    /**
     * Checks if the given principal has all of the specified permissions by name.
     *
     * @param principal the authenticated principal
     * @param permissionNames the permission names to check
     * @return true if the principal has all of the permissions, false otherwise
     */
    boolean hasAllPermissions(AuthPrincipal principal, String... permissionNames);
    
    /**
     * Checks if the given principal can perform the specified action on the specified resource.
     *
     * @param principal the authenticated principal
     * @param resource the resource to check
     * @param action the action to check
     * @return true if the principal can perform the action on the resource, false otherwise
     */
    boolean canAccess(AuthPrincipal principal, String resource, String action);
    
    /**
     * Gets all permissions for the given principal.
     *
     * @param principal the authenticated principal
     * @return a collection of permissions
     */
    Collection<Permission> getPermissions(AuthPrincipal principal);
    
    /**
     * Checks if the given principal has the specified role.
     *
     * @param principal the authenticated principal
     * @param roleName the role name to check
     * @return true if the principal has the role, false otherwise
     */
    boolean hasRole(AuthPrincipal principal, String roleName);
    
    /**
     * Checks if the given principal has any of the specified roles.
     *
     * @param principal the authenticated principal
     * @param roleNames the role names to check
     * @return true if the principal has any of the roles, false otherwise
     */
    boolean hasAnyRole(AuthPrincipal principal, String... roleNames);
    
    /**
     * Checks if the given principal has all of the specified roles.
     *
     * @param principal the authenticated principal
     * @param roleNames the role names to check
     * @return true if the principal has all of the roles, false otherwise
     */
    boolean hasAllRoles(AuthPrincipal principal, String... roleNames);
}
