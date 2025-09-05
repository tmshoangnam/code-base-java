package io.github.base.starter.security.demo;

import io.github.base.security.api.*;
import io.github.base.security.internal.DefaultAuthentication;
import io.github.base.security.model.Claim;
import io.github.base.security.model.Permission;
import io.github.base.security.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Demo controller showing how to use the security services.
 */
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public DemoController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Generate a demo token for testing.
     */
    @GetMapping("/token")
    public ResponseEntity<Map<String, Object>> generateToken() {
        try {
            // Create demo principal
            AuthPrincipal principal = createDemoPrincipal();

            // Issue token
            String token = tokenService.issueToken(principal);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", tokenService.getTokenType());
            response.put("expiresIn", tokenService.getDefaultExpiration().getSeconds());

            logger.info("Generated demo token for user: {}", principal.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to generate token", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to generate token"));
        }
    }

    /**
     * Validate a token.
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Token is required"));
            }

            boolean isValid = tokenService.validateToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);

            if (isValid) {
                response.put("subject", tokenService.getSubject(token));
                response.put("expiresAt", tokenService.getExpirationTime(token));
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to validate token", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to validate token"));
        }
    }

    /**
     * Authenticate with username/password (demo implementation).
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Username and password are required"));
            }

            // Create authentication request
//            AuthenticationRequest.Builder authRequestBuilder = new DefaultAuthenticationRequest.Builder()
//                    .type("jwt")
//                    .username(username)
//                    .password(password);

            // For demo purposes, we'll create a token directly
            // In a real implementation, you would validate credentials first
            AuthPrincipal principal = createDemoPrincipal();
            String token = tokenService.issueToken(principal);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                    "id", principal.getId(),
                    "username", principal.getUsername(),
                    "displayName", principal.getDisplayName(),
                    "email", principal.getEmail()
            ));

            logger.info("Authenticated user: {}", username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Authentication failed", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Authentication failed"));
        }
    }

    /**
     * Get token claims.
     */
    @PostMapping("/claims")
    public ResponseEntity<Map<String, Object>> getClaims(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Token is required"));
            }

            Map<String, Object> claims = tokenService.parseToken(token);

            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            logger.error("Failed to parse token", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to parse token"));
        }
    }

    /**
     * Refresh a token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Token is required"));
            }

            String refreshedToken = tokenService.refreshToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("token", refreshedToken);
            response.put("type", tokenService.getTokenType());

            logger.info("Refreshed token");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to refresh token", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to refresh token"));
        }
    }

    private AuthPrincipal createDemoPrincipal() {
        // Create demo permissions
        Permission readPermission = new Permission("read", "users", "read");
        Permission writePermission = new Permission("write", "users", "write");
        Permission adminPermission = new Permission("admin", "system", "manage");

        // Create demo roles
        Role userRole = new Role("USER", "Regular user role", Set.of(readPermission), Set.of());
        Role adminRole = new Role("ADMIN", "Administrator role", Set.of(readPermission, writePermission, adminPermission), Set.of());

        // Create demo claims
        Claim departmentClaim = new Claim("department", "IT");
        Claim locationClaim = new Claim("location", "New York");

        // Create demo principal
        return new io.github.base.security.internal.DefaultAuthPrincipal.Builder()
                .id("demo-user-123")
                .username("demo.user")
                .displayName("Demo User")
                .email("demo@example.com")
                .addRole(userRole)
                .addRole(adminRole)
                .addClaim(departmentClaim)
                .addClaim(locationClaim)
                .active(true)
                .build();
    }
}
