# Java Base Starter Security

Spring Boot starter for base-security foundation libraries providing JWT and OAuth2 authentication with auto-configuration.

## Overview

`java-base-starter-security` is a Spring Boot starter that provides concrete implementations of the security contracts defined in `java-base-security`. It includes JWT token service, authentication managers, and Spring Security integration.

## Features

- **Spring Boot Auto-Configuration**: Zero-configuration setup
- **JWT Support**: Complete JWT token service implementation
- **OAuth2 Support**: OAuth2 resource server integration (planned)
- **Spring Security Integration**: Seamless integration with Spring Security
- **Configurable**: Extensive configuration options via application properties
- **Production Ready**: Enterprise-grade security implementation

## Quick Start

### Maven Dependency

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-starter-security</artifactId>
    <version>1.0.4-SNAPSHOT</version>
</dependency>
```

### Basic Configuration

Add to your `application.yml`:

```yaml
base:
  security:
    type: jwt
    jwt:
      secret: your-secret-key-here
      expiration: PT1H
      issuer: your-app
      audience: your-app
    config:
      enabled: true
      jwt-enabled: true
      default-roles: [USER]
      default-permissions: [read:profile]
```

### Using the Services

```java
@RestController
public class AuthController {
    
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    
    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        // Create authentication request
        AuthenticationRequest authRequest = new DefaultAuthenticationRequest.Builder()
            .type("jwt")
            .username(request.getUsername())
            .password(request.getPassword())
            .build();
        
        // Authenticate
        Authentication auth = authenticationManager.authenticate(authRequest);
        
        // Issue token
        String token = tokenService.issueToken(auth.getPrincipal());
        
        return ResponseEntity.ok(token);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> profile(Authentication auth) {
        AuthPrincipal principal = ((CustomAuthentication) auth).getPrincipal();
        
        Map<String, Object> profile = Map.of(
            "id", principal.getId(),
            "username", principal.getUsername(),
            "email", principal.getEmail(),
            "roles", principal.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList())
        );
        
        return ResponseEntity.ok(profile);
    }
}
```

## Configuration Properties

### Security Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `base.security.enabled` | `true` | Enable security auto-configuration |
| `base.security.type` | `jwt` | Security provider type (jwt, oauth2) |

### JWT Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `base.security.jwt.secret` | `changeme` | JWT secret key |
| `base.security.jwt.expiration` | `PT1H` | Token expiration duration |
| `base.security.jwt.issuer` | `base-security` | JWT issuer |
| `base.security.jwt.audience` | `base-security` | JWT audience |
| `base.security.jwt.algorithm` | `HS256` | JWT algorithm |
| `base.security.jwt.refresh-expiration` | `P7D` | Refresh token expiration |

### OAuth2 Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `base.security.oauth2.issuer-uri` | - | OAuth2 issuer URI |
| `base.security.oauth2.client-id` | - | OAuth2 client ID |
| `base.security.oauth2.client-secret` | - | OAuth2 client secret |
| `base.security.oauth2.redirect-uri` | - | OAuth2 redirect URI |
| `base.security.oauth2.scopes` | `[openid, profile, email]` | OAuth2 scopes |
| `base.security.oauth2.audience` | - | OAuth2 audience |

### Security Config

| Property | Default | Description |
|----------|---------|-------------|
| `base.security.config.jwt-enabled` | `true` | Enable JWT support |
| `base.security.config.oauth2-enabled` | `true` | Enable OAuth2 support |
| `base.security.config.spring-security-enabled` | `true` | Enable Spring Security integration |
| `base.security.config.default-roles` | `[USER]` | Default roles for authenticated users |
| `base.security.config.default-permissions` | `[]` | Default permissions for authenticated users |

## Advanced Usage

### Custom Security Provider

```java
@Component
public class CustomSecurityProvider implements SecurityProvider {
    // Implementation details
}
```

### Custom Token Service

```java
@Component
@ConditionalOnMissingBean(TokenService.class)
public class CustomTokenService implements TokenService {
    // Implementation details
}
```

### Spring Security Integration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                )
            )
            .build();
    }
    
    @Bean
    public JwtDecoder jwtDecoder() {
        // JWT decoder configuration
        return NimbusJwtDecoder.withJwkSetUri("https://your-issuer/.well-known/jwks.json")
            .build();
    }
}
```

## Testing

### Unit Tests

```java
@SpringBootTest
class SecurityIntegrationTest {
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Test
    void shouldIssueAndValidateToken() {
        // Test implementation
    }
}
```

### Integration Tests

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldAuthenticateUser() {
        // Test implementation
    }
}
```

## Dependencies

- Spring Boot 3.3+
- Spring Security 6.3+
- Nimbus JOSE JWT
- OAuth2 JOSE
- Jackson
- JUnit 5
- AssertJ
- Mockito

## License

MIT License - see LICENSE file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## Support

For questions and support, please open an issue on GitHub.
