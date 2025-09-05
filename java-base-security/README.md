# Java Base Security

Enterprise-grade security foundation library providing authentication and authorization contracts, SPI, and utilities.

## Overview

`java-base-security` is a pure Java library that defines security contracts and provides a Service Provider Interface (SPI) for different authentication and authorization implementations. It follows the contract-first design principle and is framework-agnostic.

## Features

- **Contract-First Design**: Clean separation between contracts and implementations
- **SPI Support**: Pluggable security providers via ServiceLoader
- **Framework Agnostic**: No dependencies on Spring or other frameworks
- **Comprehensive API**: Authentication, Authorization, Token Service contracts
- **Extensible**: Easy to add new security providers
- **Type Safe**: Strong typing with proper validation

## Modules

### Core Contracts

- `AuthPrincipal`: Representation of authenticated user
- `Authentication`: Authentication result with context
- `AuthenticationRequest`: Input for authentication process
- `AuthenticationManager`: Contract for authentication logic
- `AuthorizationChecker`: Contract for authorization checks
- `TokenService`: Contract for token operations

### Models

- `Role`: User roles with permissions
- `Permission`: Resource-action permissions
- `Claim`: Token claims with type safety

### SPI

- `SecurityProvider`: Service provider interface
- `SecurityProviderConfiguration`: Configuration contract
- `SecurityServices`: Service loader helper

## Quick Start

### Maven Dependency

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-security</artifactId>
    <version>1.0.4-SNAPSHOT</version>
</dependency>
```

### Basic Usage

```java
// Load security providers
Map<String, SecurityProvider> providers = SecurityServices.getProviders();

// Get a specific provider
SecurityProvider jwtProvider = SecurityServices.getProvider("jwt")
    .orElseThrow(() -> new SecurityException("JWT provider not found"));

// Use the provider
AuthenticationManager authManager = jwtProvider.getAuthenticationManager();
TokenService tokenService = jwtProvider.getTokenService();
AuthorizationChecker authzChecker = jwtProvider.getAuthorizationChecker();
```

### Creating a Principal

```java
// Create roles and permissions
Permission readPermission = new Permission("read", "users", "read");
Role userRole = new Role("USER", Set.of(readPermission));

// Create principal
AuthPrincipal principal = new DefaultAuthPrincipal.Builder()
    .id("user123")
    .username("john.doe")
    .displayName("John Doe")
    .email("john@example.com")
    .addRole(userRole)
    .active(true)
    .build();
```

### Token Operations

```java
// Issue token
String token = tokenService.issueToken(principal);

// Validate token
boolean isValid = tokenService.validateToken(token);

// Parse token
Map<String, Object> claims = tokenService.parseToken(token);

// Get specific claim
String subject = tokenService.getSubject(token);
```

## Configuration

The library uses ServiceLoader to discover security providers. Providers are registered in `META-INF/services/io.github.base.security.spi.SecurityProvider`.

## Error Handling

All security operations throw `SecurityException` with standardized error codes:

- `AUTH_*`: Authentication errors
- `AUTHZ_*`: Authorization errors  
- `TOKEN_*`: Token-related errors
- `PRINCIPAL_*`: Principal-related errors
- `CONFIG_*`: Configuration errors

## Testing

The library includes comprehensive test utilities and examples:

```java
@Test
void shouldCreateRoleWithPermissions() {
    Permission permission = new Permission("read", "users", "read");
    Role role = new Role("admin", Set.of(permission));
    
    assertThat(role.hasPermission("read")).isTrue();
    assertThat(role.hasPermission("write")).isFalse();
}
```

## Extending the Library

### Creating a Custom Security Provider

1. Implement `SecurityProvider` interface
2. Register in `META-INF/services/io.github.base.security.spi.SecurityProvider`
3. Implement required contracts (`AuthenticationManager`, `AuthorizationChecker`, `TokenService`)

```java
public class CustomSecurityProvider implements SecurityProvider {
    // Implementation details
}
```

## Dependencies

- Java 17+
- SLF4J API
- Jakarta Validation API
- Nimbus JOSE JWT (optional)
- OWASP Encoder
- Jackson (optional)

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
