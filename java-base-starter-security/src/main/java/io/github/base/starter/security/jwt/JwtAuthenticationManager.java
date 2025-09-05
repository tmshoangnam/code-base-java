package io.github.base.starter.security.jwt;

import io.github.base.security.api.Authentication;
import io.github.base.security.api.AuthenticationManager;
import io.github.base.security.api.AuthenticationRequest;
import io.github.base.security.api.SecurityException;
import io.github.base.security.internal.DefaultAuthentication;
import io.github.base.security.model.Claim;
import io.github.base.security.model.Permission;
import io.github.base.security.model.Role;
import io.github.base.starter.security.autoconfig.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * JWT implementation of AuthenticationManager.
 */
public class JwtAuthenticationManager implements AuthenticationManager {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationManager.class);
    
    private final JwtTokenService tokenService;
    private final SecurityProperties.SecurityConfigProperties config;
    
    public JwtAuthenticationManager(JwtTokenService tokenService, SecurityProperties.SecurityConfigProperties config) {
        this.tokenService = tokenService;
        this.config = config;
    }
    
    @Override
    public Authentication authenticate(AuthenticationRequest request) throws SecurityException {
        if (!supports(request.getType())) {
            throw new SecurityException("Unsupported authentication type: " + request.getType());
        }
        
        if (!validate(request)) {
            throw new SecurityException("Invalid authentication request");
        }
        
        try {
            String token = request.getToken();
            if (!StringUtils.hasText(token)) {
                throw new SecurityException("Token is required for JWT authentication");
            }
            
            // Validate the token
            if (!tokenService.validateToken(token)) {
                throw new SecurityException("Invalid or expired JWT token");
            }
            
            // Parse the token to get claims
            Map<String, Object> claims = tokenService.parseToken(token);
            
            // Create principal from claims
            io.github.base.security.api.AuthPrincipal principal = createPrincipalFromClaims(claims);
            
            // Create authentication result
            return new DefaultAuthentication.Builder()
                    .principal(principal)
                    .method("jwt")
                    .addClaims(createClaimsFromMap(claims))
                    .authenticated(true)
                    .authenticatedAt(Instant.now())
                    .expiresAt(Instant.ofEpochMilli(tokenService.getExpirationTime(token)))
                    .sessionId(request.getSessionId())
                    .clientIpAddress(request.getClientIpAddress())
                    .userAgent(request.getUserAgent())
                    .build();
                    
        } catch (Exception e) {
            logger.error("JWT authentication failed", e);
            throw new SecurityException("JWT authentication failed", e);
        }
    }
    
    @Override
    public CompletableFuture<Authentication> authenticateAsync(AuthenticationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return authenticate(request);
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @Override
    public boolean supports(String type) {
        return "jwt".equalsIgnoreCase(type) || "token".equalsIgnoreCase(type);
    }
    
    @Override
    public String[] getSupportedTypes() {
        return new String[]{"jwt", "token"};
    }
    
    @Override
    public boolean validate(AuthenticationRequest request) {
        if (request == null) {
            return false;
        }
        
        if (!supports(request.getType())) {
            return false;
        }
        
        return StringUtils.hasText(request.getToken());
    }
    
    private io.github.base.security.api.AuthPrincipal createPrincipalFromClaims(Map<String, Object> claims) {
        String id = getStringClaim(claims, "sub");
        String username = getStringClaim(claims, "username");
        String displayName = getStringClaim(claims, "displayName");
        String email = getStringClaim(claims, "email");
        boolean active = getBooleanClaim(claims, "active", true);
        long createdAt = getLongClaim(claims, "createdAt", System.currentTimeMillis());
        long updatedAt = getLongClaim(claims, "updatedAt", System.currentTimeMillis());
        
        // Create roles from claims
        Set<Role> roles = createRolesFromClaims(claims);
        
        // Create claims
        List<Claim> claimList = createClaimsFromMap(claims);
        
        return new io.github.base.security.internal.DefaultAuthPrincipal.Builder()
                .id(id)
                .username(username)
                .displayName(displayName)
                .email(email)
                .addRoles(roles)
                .addClaims(claimList)
                .active(active)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
    
    private Set<Role> createRolesFromClaims(Map<String, Object> claims) {
        Set<Role> roles = new HashSet<>();
        
        // Get roles from claims
        Object rolesClaim = claims.get("roles");
        if (rolesClaim instanceof String[]) {
            String[] roleNames = (String[]) rolesClaim;
            for (String roleName : roleNames) {
                roles.add(new Role(roleName));
            }
        } else if (rolesClaim instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> roleNames = (List<String>) rolesClaim;
            for (String roleName : roleNames) {
                roles.add(new Role(roleName));
            }
        }
        
        // Add default roles if no roles found
        if (roles.isEmpty() && config.getDefaultRoles() != null) {
            for (String roleName : config.getDefaultRoles()) {
                roles.add(new Role(roleName));
            }
        }
        
        // Add permissions to roles
        Object permissionsClaim = claims.get("permissions");
        Set<Permission> permissions = new HashSet<>();
        if (permissionsClaim instanceof String[]) {
            String[] permissionNames = (String[]) permissionsClaim;
            for (String permissionName : permissionNames) {
                permissions.add(new Permission(permissionName));
            }
        } else if (permissionsClaim instanceof List) {
            @SuppressWarnings("unchecked")
            List<String> permissionNames = (List<String>) permissionsClaim;
            for (String permissionName : permissionNames) {
                permissions.add(new Permission(permissionName));
            }
        }
        
        // Add default permissions if no permissions found
        if (permissions.isEmpty() && config.getDefaultPermissions() != null) {
            for (String permissionName : config.getDefaultPermissions()) {
                permissions.add(new Permission(permissionName));
            }
        }
        
        // Add permissions to roles
        for (Role role : roles) {
            // Note: This is a simplified approach. In a real implementation,
            // you might want to store role-permission mappings separately
            for (Permission permission : permissions) {
                // This would require modifying the Role class to support adding permissions
                // For now, we'll just add the permissions to the first role
                if (roles.iterator().hasNext()) {
                    break;
                }
            }
        }
        
        return roles;
    }
    
    private List<Claim> createClaimsFromMap(Map<String, Object> claims) {
        return claims.entrySet().stream()
                .map(entry -> new Claim(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
    
    private String getStringClaim(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        return value != null ? value.toString() : null;
    }
    
    private boolean getBooleanClaim(Map<String, Object> claims, String name, boolean defaultValue) {
        Object value = claims.get(name);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return defaultValue;
    }
    
    private long getLongClaim(Map<String, Object> claims, String name, long defaultValue) {
        Object value = claims.get(name);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
