package io.github.base.security.internal;

import io.github.base.security.api.Authentication;
import io.github.base.security.api.AuthPrincipal;
import io.github.base.security.model.Claim;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of Authentication.
 */
public final class DefaultAuthentication implements Authentication {
    
    private final AuthPrincipal principal;
    private final String method;
    private final Set<String> authorities;
    private final Map<String, Claim> claims;
    private final Map<String, Object> attributes;
    private final boolean authenticated;
    private final Instant authenticatedAt;
    private final Instant expiresAt;
    private final String sessionId;
    private final String clientIpAddress;
    private final String userAgent;
    
    private DefaultAuthentication(Builder builder) {
        this.principal = builder.principal;
        this.method = builder.method;
        this.authorities = Set.copyOf(builder.authorities);
        this.claims = Map.copyOf(builder.claims);
        this.attributes = Map.copyOf(builder.attributes);
        this.authenticated = builder.authenticated;
        this.authenticatedAt = builder.authenticatedAt;
        this.expiresAt = builder.expiresAt;
        this.sessionId = builder.sessionId;
        this.clientIpAddress = builder.clientIpAddress;
        this.userAgent = builder.userAgent;
    }
    
    @Override
    public AuthPrincipal getPrincipal() {
        return principal;
    }
    
    @Override
    public String getMethod() {
        return method;
    }
    
    @Override
    public Collection<String> getAuthorities() {
        return authorities;
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
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    @Override
    public boolean hasAuthority(String authority) {
        return authorities.contains(authority);
    }
    
    @Override
    public boolean hasAnyAuthority(String... authorities) {
        Set<String> authoritySet = Set.of(authorities);
        return this.authorities.stream().anyMatch(authoritySet::contains);
    }
    
    @Override
    public boolean hasAllAuthorities(String... authorities) {
        Set<String> authoritySet = Set.of(authorities);
        return authoritySet.stream().allMatch(this.authorities::contains);
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
    public Instant getAuthenticatedAt() {
        return authenticatedAt;
    }
    
    @Override
    public Instant getExpiresAt() {
        return expiresAt;
    }
    
    @Override
    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }
    
    @Override
    public String getSessionId() {
        return sessionId;
    }
    
    @Override
    public String getClientIpAddress() {
        return clientIpAddress;
    }
    
    @Override
    public String getUserAgent() {
        return userAgent;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultAuthentication that = (DefaultAuthentication) o;
        return Objects.equals(principal, that.principal) &&
                Objects.equals(method, that.method) &&
                Objects.equals(sessionId, that.sessionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(principal, method, sessionId);
    }
    
    @Override
    public String toString() {
        return "DefaultAuthentication{" +
                "principal=" + principal +
                ", method='" + method + '\'' +
                ", authorities=" + authorities +
                ", authenticated=" + authenticated +
                ", authenticatedAt=" + authenticatedAt +
                ", expiresAt=" + expiresAt +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
    
    /**
     * Builder for DefaultAuthentication.
     */
    public static final class Builder {
        private AuthPrincipal principal;
        private String method;
        private final Set<String> authorities = new HashSet<>();
        private final Map<String, Claim> claims = new HashMap<>();
        private final Map<String, Object> attributes = new HashMap<>();
        private boolean authenticated = true;
        private Instant authenticatedAt = Instant.now();
        private Instant expiresAt;
        private String sessionId;
        private String clientIpAddress;
        private String userAgent;
        
        public Builder principal(AuthPrincipal principal) {
            this.principal = principal;
            return this;
        }
        
        public Builder method(String method) {
            this.method = method;
            return this;
        }
        
        public Builder addAuthority(String authority) {
            this.authorities.add(authority);
            return this;
        }
        
        public Builder addAuthorities(Collection<String> authorities) {
            this.authorities.addAll(authorities);
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
        
        public Builder authenticated(boolean authenticated) {
            this.authenticated = authenticated;
            return this;
        }
        
        public Builder authenticatedAt(Instant authenticatedAt) {
            this.authenticatedAt = authenticatedAt;
            return this;
        }
        
        public Builder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        
        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }
        
        public Builder clientIpAddress(String clientIpAddress) {
            this.clientIpAddress = clientIpAddress;
            return this;
        }
        
        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }
        
        public DefaultAuthentication build() {
            if (principal == null) {
                throw new IllegalArgumentException("Principal is required");
            }
            if (method == null || method.trim().isEmpty()) {
                throw new IllegalArgumentException("Method is required");
            }
            
            // Add role-based authorities
            if (principal.getRoles() != null) {
                Set<String> roleAuthorities = principal.getRoles().stream()
                        .map(role -> "ROLE_" + role.getName().toUpperCase())
                        .collect(Collectors.toSet());
                authorities.addAll(roleAuthorities);
            }
            
            // Add permission-based authorities
            if (principal.getRoles() != null) {
                Set<String> permissionAuthorities = principal.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(permission -> permission.getName().toUpperCase())
                        .collect(Collectors.toSet());
                authorities.addAll(permissionAuthorities);
            }
            
            return new DefaultAuthentication(this);
        }
    }
}
