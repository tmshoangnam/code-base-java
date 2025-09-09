package io.github.base.starter.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.github.base.security.api.AuthPrincipal;
import io.github.base.security.api.SecurityException;
import io.github.base.security.api.TokenService;
import io.github.base.security.model.Claim;
import io.github.base.security.model.Permission;
import io.github.base.security.model.Role;
import io.github.base.starter.security.autoconfig.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * JWT implementation of TokenService.
 */
public class JwtTokenService implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);

    private final SecurityProperties.JwtProperties jwtProperties;
    private final JWSSigner signer;
    private final JWSVerifier verifier;
    private final JWSAlgorithm algorithm;

    public JwtTokenService(SecurityProperties.JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.algorithm = JWSAlgorithm.parse(jwtProperties.getAlgorithm());

        try {
            this.signer = new MACSigner(jwtProperties.getSecret());
            this.verifier = new MACVerifier(jwtProperties.getSecret());
        } catch (JOSEException e) {
            throw new SecurityException("Failed to initialize JWT signer/verifier", e);
        }
    }

    @Override
    public String issueToken(AuthPrincipal principal) throws SecurityException {
        return issueToken(principal, jwtProperties.getExpiration());
    }

    @Override
    public String issueToken(Map<String, Object> claims) throws SecurityException {
        return issueToken(claims, jwtProperties.getExpiration());
    }

    @Override
    public String issueToken(Map<String, Object> claims, Duration expiration) throws SecurityException {
        try {
            Instant now = Instant.now();
            Instant expiresAt = now.plus(expiration);

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .issuer(jwtProperties.getIssuer())
                    .audience(jwtProperties.getAudience())
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiresAt))
                    .jwtID(UUID.randomUUID().toString());

            // Add custom claims
            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                claimsBuilder.claim(entry.getKey(), entry.getValue());
            }


            JWTClaimsSet jwtClaims = claimsBuilder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(algorithm), jwtClaims);
            signedJWT.sign(signer);

            String token = signedJWT.serialize();
            logger.debug("Issued JWT token for subject: {}", jwtClaims.getSubject());
            return token;
        } catch (JOSEException e) {
            logger.error("Failed to issue JWT token", e);
            throw new SecurityException("Failed to issue JWT token", e);
        }
    }

    @Override
    public String issueToken(AuthPrincipal principal, Duration expiration) throws SecurityException {
        try {
            Instant now = Instant.now();
            Instant expiresAt = now.plus(expiration);

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                    .subject(principal.getId())
                    .issuer(jwtProperties.getIssuer())
                    .audience(jwtProperties.getAudience())
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiresAt))
                    .claim("username", principal.getUsername())
                    .claim("displayName", principal.getDisplayName())
                    .claim("email", principal.getEmail())
                    .claim("active", principal.isActive())
                    .claim("createdAt", principal.getCreatedAt())
                    .claim("updatedAt", principal.getUpdatedAt());

            // Add roles
            if (principal.getRoles() != null && !principal.getRoles().isEmpty()) {
                claimsBuilder.claim("roles", principal.getRoles().stream()
                        .map(Role::getName)
                        .toArray(String[]::new));
            }

            // Add permissions
            if (principal.getRoles() != null) {
                claimsBuilder.claim("permissions", principal.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(Permission::getName)
                        .distinct()
                        .toArray(String[]::new));
            }

            // Add custom claims
            if (principal.getClaims() != null) {
                for (Claim claim : principal.getClaims()) {
                    claimsBuilder.claim(claim.getName(), claim.getValue());
                }
            }

            JWTClaimsSet jwtClaims = claimsBuilder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(algorithm), jwtClaims);
            signedJWT.sign(signer);

            String token = signedJWT.serialize();
            logger.debug("Issued JWT token for principal: {}", principal.getUsername());
            return token;
        } catch (JOSEException e) {
            logger.error("Failed to issue JWT token for principal: {}", principal.getUsername(), e);
            throw new SecurityException("Failed to issue JWT token", e);
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (!StringUtils.hasText(token)) {
                return false;
            }

            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean isValid = signedJWT.verify(verifier);

            if (isValid) {
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                Date expirationTime = claims.getExpirationTime();
                if (expirationTime != null && expirationTime.before(new Date())) {
                    logger.debug("JWT token is expired");
                    return false;
                }
            }

            return isValid;
        } catch (Exception e) {
            logger.debug("JWT token validation failed", e);
            return false;
        }
    }

    @Override
    public Map<String, Object> parseToken(String token) throws SecurityException {
        try {
            if (!StringUtils.hasText(token)) {
                throw new SecurityException("Token cannot be null or empty");
            }

            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(verifier)) {
                throw new SecurityException("Invalid JWT signature");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Date expirationTime = claims.getExpirationTime();
            if (expirationTime != null && expirationTime.before(new Date())) {
                throw new SecurityException("JWT token is expired");
            }

            return claims.getClaims();
        } catch (Exception e) {
            logger.error("Failed to parse JWT token", e);
            throw new SecurityException("Failed to parse JWT token", e);
        }
    }

    @Override
    public Optional<Object> getClaim(String token, String claimName) throws SecurityException {
        Map<String, Object> claims = parseToken(token);
        return Optional.ofNullable(claims.get(claimName));
    }

    @Override
    public String getClaimValue(String token, String claimName) throws SecurityException {
        return getClaim(token, claimName)
                .map(Object::toString)
                .orElse(null);
    }

    @Override
    public String getClaimValue(String token, String claimName, String defaultValue) throws SecurityException {
        return getClaim(token, claimName)
                .map(Object::toString)
                .orElse(defaultValue);
    }

    @Override
    public String getSubject(String token) throws SecurityException {
        return getClaimValue(token, "sub");
    }

    @Override
    public long getExpirationTime(String token) throws SecurityException {
        Map<String, Object> claims = parseToken(token);
        Object exp = claims.get("exp");
        if (exp instanceof Date date) {
            return date.getTime();
        }
        return -1;
    }

    @Override
    public boolean isExpired(String token) throws SecurityException {
        try {
            Map<String, Object> claims = parseToken(token);
            Object exp = claims.get("exp");
            if (exp instanceof Date date) {
                long expirationTime = date.getTime();
                return System.currentTimeMillis() > expirationTime;
            }
            return false;
        } catch (SecurityException e) {
            return true; // If we can't parse it, consider it expired
        }
    }

    @Override
    public String refreshToken(String token) throws SecurityException {
        Map<String, Object> claims = parseToken(token);

        Map<String, Object> claimsCopy = new HashMap<>(claims);

        // Remove time-based claims
        claimsCopy.remove("iat");
        claimsCopy.remove("exp");
        claimsCopy.remove("nbf");
        claimsCopy.remove("jti");

        return issueToken(claimsCopy, jwtProperties.getExpiration());
    }

    @Override
    public void revokeToken(String token) throws SecurityException {
        // JWT tokens are stateless, so revocation is not supported by default
        // This would require a token blacklist or similar mechanism
        logger.warn("JWT token revocation is not supported by default");
    }

    @Override
    public boolean isRevoked(String token) throws SecurityException {
        // JWT tokens are stateless, so revocation checking is not supported by default
        return false;
    }

    @Override
    public String getTokenType() {
        return "JWT";
    }

    @Override
    public Duration getDefaultExpiration() {
        return jwtProperties.getExpiration();
    }
}
