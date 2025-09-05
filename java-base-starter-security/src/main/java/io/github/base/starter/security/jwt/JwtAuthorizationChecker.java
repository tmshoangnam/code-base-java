package io.github.base.starter.security.jwt;

import io.github.base.security.api.AuthPrincipal;
import io.github.base.security.api.AuthorizationChecker;
import io.github.base.security.model.Permission;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JWT implementation of AuthorizationChecker.
 */
public class JwtAuthorizationChecker implements AuthorizationChecker {
    
    @Override
    public boolean hasPermission(AuthPrincipal principal, Permission permission) {
        if (principal == null || permission == null) {
            return false;
        }
        
        return principal.hasPermission(permission.getName());
    }
    
    @Override
    public boolean hasPermission(AuthPrincipal principal, String permissionName) {
        if (principal == null || permissionName == null) {
            return false;
        }
        
        return principal.hasPermission(permissionName);
    }
    
    @Override
    public boolean hasAnyPermission(AuthPrincipal principal, Permission... permissions) {
        if (principal == null || permissions == null || permissions.length == 0) {
            return false;
        }
        
        for (Permission permission : permissions) {
            if (principal.hasPermission(permission.getName())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean hasAnyPermission(AuthPrincipal principal, String... permissionNames) {
        if (principal == null || permissionNames == null || permissionNames.length == 0) {
            return false;
        }
        
        return principal.hasAnyPermission(permissionNames);
    }
    
    @Override
    public boolean hasAllPermissions(AuthPrincipal principal, Permission... permissions) {
        if (principal == null || permissions == null || permissions.length == 0) {
            return false;
        }
        
        for (Permission permission : permissions) {
            if (!principal.hasPermission(permission.getName())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean hasAllPermissions(AuthPrincipal principal, String... permissionNames) {
        if (principal == null || permissionNames == null || permissionNames.length == 0) {
            return false;
        }
        
        return principal.hasAllPermissions(permissionNames);
    }
    
    @Override
    public boolean canAccess(AuthPrincipal principal, String resource, String action) {
        if (principal == null || resource == null || action == null) {
            return false;
        }
        
        // Check if principal has any permission that matches the resource and action
        return principal.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.matches(resource, action));
    }
    
    @Override
    public Collection<Permission> getPermissions(AuthPrincipal principal) {
        if (principal == null) {
            return Set.of();
        }
        
        return principal.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .collect(Collectors.toSet());
    }
    
    @Override
    public boolean hasRole(AuthPrincipal principal, String roleName) {
        if (principal == null || roleName == null) {
            return false;
        }
        
        return principal.hasRole(roleName);
    }
    
    @Override
    public boolean hasAnyRole(AuthPrincipal principal, String... roleNames) {
        if (principal == null || roleNames == null || roleNames.length == 0) {
            return false;
        }
        
        return principal.hasAnyRole(roleNames);
    }
    
    @Override
    public boolean hasAllRoles(AuthPrincipal principal, String... roleNames) {
        if (principal == null || roleNames == null || roleNames.length == 0) {
            return false;
        }
        
        return principal.hasAllRoles(roleNames);
    }
}
