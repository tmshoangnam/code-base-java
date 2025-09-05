package io.github.base.security.internal;

import io.github.base.security.api.AuthPrincipal;
import io.github.base.security.model.Claim;
import io.github.base.security.model.Role;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of AuthPrincipal.
 */
public final class DefaultAuthPrincipal implements AuthPrincipal {
    
    private final String id;
    private final String username;
    private final String displayName;
    private final String email;
    private final Set<Role> roles;
    private final Map<String, Claim> claims;
    private final Map<String, Object> attributes;
    private final boolean active;
    private final boolean expired;
    private final long createdAt;
    private final long updatedAt;
    
    private DefaultAuthPrincipal(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.displayName = builder.displayName;
        this.email = builder.email;
        this.roles = Set.copyOf(builder.roles);
        this.claims = Map.copyOf(builder.claims);
        this.attributes = Map.copyOf(builder.attributes);
        this.active = builder.active;
        this.expired = builder.expired;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String getEmail() {
        return email;
    }
    
    @Override
    public Collection<Role> getRoles() {
        return roles;
    }
    
    @Override
    public Collection<Claim> getClaims() {
        return claims.values();
    }
    
    @Override
    public Optional<Claim> getClaim(String name) {
        return Optional.ofNullable(claims.get(name));
    }
    
    @Override
    public String getClaimValue(String name) {
        return getClaim(name).map(Claim::getStringValue).orElse(null);
    }
    
    @Override
    public String getClaimValue(String name, String defaultValue) {
        return getClaim(name).map(Claim::getStringValue).orElse(defaultValue);
    }
    
    @Override
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }
    
    @Override
    public boolean hasAnyRole(String... roleNames) {
        Set<String> roleNameSet = Set.of(roleNames);
        return roles.stream().anyMatch(role -> roleNameSet.contains(role.getName()));
    }
    
    @Override
    public boolean hasAllRoles(String... roleNames) {
        Set<String> roleNameSet = Set.of(roleNames);
        return roleNameSet.stream().allMatch(this::hasRole);
    }
    
    @Override
    public boolean hasPermission(String permissionName) {
        return roles.stream()
                .anyMatch(role -> role.hasPermission(permissionName));
    }
    
    @Override
    public boolean hasAnyPermission(String... permissionNames) {
        Set<String> permissionNameSet = Set.of(permissionNames);
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permissionNameSet.contains(permission.getName()));
    }
    
    @Override
    public boolean hasAllPermissions(String... permissionNames) {
        Set<String> permissionNameSet = Set.of(permissionNames);
        Set<String> userPermissions = roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getName())
                .collect(Collectors.toSet());
        return permissionNameSet.stream().allMatch(userPermissions::contains);
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    @Override
    public Optional<Object> getAttribute(String name) {
        return Optional.ofNullable(attributes.get(name));
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public boolean isExpired() {
        return expired;
    }
    
    @Override
    public long getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public long getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultAuthPrincipal that = (DefaultAuthPrincipal) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "DefaultAuthPrincipal{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", active=" + active +
                ", expired=" + expired +
                '}';
    }
    
    /**
     * Builder for DefaultAuthPrincipal.
     */
    public static final class Builder {
        private String id;
        private String username;
        private String displayName;
        private String email;
        private final Set<Role> roles = new HashSet<>();
        private final Map<String, Claim> claims = new HashMap<>();
        private final Map<String, Object> attributes = new HashMap<>();
        private boolean active = true;
        private boolean expired = false;
        private long createdAt = System.currentTimeMillis();
        private long updatedAt = System.currentTimeMillis();
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder addRole(Role role) {
            this.roles.add(role);
            return this;
        }
        
        public Builder addRoles(Collection<Role> roles) {
            this.roles.addAll(roles);
            return this;
        }
        
        public Builder addClaim(Claim claim) {
            this.claims.put(claim.getName(), claim);
            return this;
        }
        
        public Builder addClaims(Collection<Claim> claims) {
            for (Claim claim : claims) {
                this.claims.put(claim.getName(), claim);
            }
            return this;
        }
        
        public Builder addAttribute(String name, Object value) {
            this.attributes.put(name, value);
            return this;
        }
        
        public Builder addAttributes(Map<String, Object> attributes) {
            this.attributes.putAll(attributes);
            return this;
        }
        
        public Builder active(boolean active) {
            this.active = active;
            return this;
        }
        
        public Builder expired(boolean expired) {
            this.expired = expired;
            return this;
        }
        
        public Builder createdAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public DefaultAuthPrincipal build() {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("ID is required");
            }
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username is required");
            }
            if (displayName == null) {
                displayName = username;
            }
            return new DefaultAuthPrincipal(this);
        }
    }
}
