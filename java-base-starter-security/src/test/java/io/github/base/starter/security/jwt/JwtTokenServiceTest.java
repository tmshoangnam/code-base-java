package io.github.base.starter.security.jwt;

import io.github.base.security.api.AuthPrincipal;
import io.github.base.security.api.SecurityException;
import io.github.base.security.internal.DefaultAuthPrincipal;
import io.github.base.security.model.Claim;
import io.github.base.security.model.Permission;
import io.github.base.security.model.Role;
import io.github.base.starter.security.autoconfig.SecurityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenServiceTest {

    private JwtTokenService tokenService;
    private SecurityProperties.JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new SecurityProperties.JwtProperties();
        jwtProperties.setSecret("test-secret-key-that-is-long-enough-for-hs256");
        jwtProperties.setExpiration(Duration.ofHours(1));
        jwtProperties.setIssuer("test-issuer");
        jwtProperties.setAudience("test-audience");

        tokenService = new JwtTokenService(jwtProperties);
    }

    @Test
    void shouldIssueTokenForPrincipal() throws SecurityException {
        AuthPrincipal principal = createTestPrincipal();

        String token = tokenService.issueToken(principal);

        assertThat(token).isNotNull();
        assertThat(tokenService.validateToken(token)).isTrue();
        assertThat(tokenService.getSubject(token)).isEqualTo("user123");
    }

    @Test
    void shouldIssueTokenWithClaims() throws SecurityException {
        Map<String, Object> claims = Map.of(
                "sub", "user123",
                "name", "John Doe",
                "email", "john@example.com"
        );

        String token = tokenService.issueToken(claims);

        assertThat(token).isNotNull();
        assertThat(tokenService.validateToken(token)).isTrue();
        assertThat(tokenService.getSubject(token)).isEqualTo("user123");
        assertThat(tokenService.getClaimValue(token, "name")).isEqualTo("John Doe");
    }

    @Test
    void shouldIssueTokenWithCustomExpiration() throws SecurityException {
        Map<String, Object> claims = Map.of("sub", "user123");
        Duration expiration = Duration.ofMinutes(30);

        String token = tokenService.issueToken(claims, expiration);

        assertThat(token).isNotNull();
        assertThat(tokenService.validateToken(token)).isTrue();
    }

    @Test
    void shouldValidateToken() throws SecurityException {
        String token = tokenService.issueToken(Map.of("sub", "user123"));

        assertThat(tokenService.validateToken(token)).isTrue();
        assertThat(tokenService.validateToken("invalid-token")).isFalse();
        assertThat(tokenService.validateToken(null)).isFalse();
        assertThat(tokenService.validateToken("")).isFalse();
    }

    @Test
    void shouldParseToken() throws SecurityException {
        Map<String, Object> claims = Map.of(
                "sub", "user123",
                "name", "John Doe",
                "email", "john@example.com"
        );

        String token = tokenService.issueToken(claims);
        Map<String, Object> parsedClaims = tokenService.parseToken(token);

        assertThat(parsedClaims).containsEntry("sub", "user123");
        assertThat(parsedClaims).containsEntry("name", "John Doe");
        assertThat(parsedClaims).containsEntry("email", "john@example.com");
    }

    @Test
    void shouldGetClaimValue() throws SecurityException {
        String token = tokenService.issueToken(Map.of("sub", "user123", "name", "John Doe"));

        assertThat(tokenService.getClaimValue(token, "sub")).isEqualTo("user123");
        assertThat(tokenService.getClaimValue(token, "name")).isEqualTo("John Doe");
        assertThat(tokenService.getClaimValue(token, "nonexistent")).isNull();
    }

    @Test
    void shouldGetClaimValueWithDefault() throws SecurityException {
        String token = tokenService.issueToken(Map.of("sub", "user123"));

        assertThat(tokenService.getClaimValue(token, "sub", "default")).isEqualTo("user123");
        assertThat(tokenService.getClaimValue(token, "nonexistent", "default")).isEqualTo("default");
    }

    @Test
    void shouldGetSubject() throws SecurityException {
        String token = tokenService.issueToken(Map.of("sub", "user123"));

        assertThat(tokenService.getSubject(token)).isEqualTo("user123");
    }

    @Test
    void shouldGetExpirationTime() throws SecurityException {
        String token = tokenService.issueToken(Map.of("sub", "user123"));

        long expirationTime = tokenService.getExpirationTime(token);
        assertThat(expirationTime).isGreaterThan(System.currentTimeMillis());
    }

    @Test
    void shouldCheckIfExpired() throws SecurityException {
        String token = tokenService.issueToken(Map.of("sub", "user123"));

        assertThat(tokenService.isExpired(token)).isFalse();
    }

    @Test
    void shouldRefreshToken() throws SecurityException {
        String originalToken = tokenService.issueToken(Map.of("sub", "user123", "name", "John Doe"));
        String refreshedToken = tokenService.refreshToken(originalToken);

        assertThat(refreshedToken).isNotNull();
        assertThat(refreshedToken).isNotEqualTo(originalToken);
        assertThat(tokenService.getSubject(refreshedToken)).isEqualTo("user123");
        assertThat(tokenService.getClaimValue(refreshedToken, "name")).isEqualTo("John Doe");
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        assertThatThrownBy(() -> tokenService.parseToken("invalid-token"))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void shouldThrowExceptionForNullToken() {
        assertThatThrownBy(() -> tokenService.parseToken(null))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void shouldGetTokenType() {
        assertThat(tokenService.getTokenType()).isEqualTo("JWT");
    }

    @Test
    void shouldGetDefaultExpiration() {
        assertThat(tokenService.getDefaultExpiration()).isEqualTo(Duration.ofHours(1));
    }

    private AuthPrincipal createTestPrincipal() {
        Permission readPermission = new Permission("read", "users", "read");
        Permission writePermission = new Permission("write", "users", "write");
        Role userRole = new Role("USER", Set.of(readPermission));
        Role adminRole = new Role("ADMIN", Set.of(readPermission, writePermission));

        return new DefaultAuthPrincipal.Builder()
                .id("user123")
                .username("john.doe")
                .displayName("John Doe")
                .email("john@example.com")
                .addRole(userRole)
                .addRole(adminRole)
                .addClaim(new Claim("department", "IT"))
                .active(true)
                .build();
    }
}
