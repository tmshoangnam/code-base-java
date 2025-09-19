You are a Senior Software Engineer + System Architect (or an AI coding agent acting like one).
Your task: design and implement a production-ready, enterprise-grade cache foundation split into two modules:
	• java-base-cache — pure Java library (jar) that defines  security contracts, SPI, and utilities (no Spring, no client libs).
	• java-base-starter-security — Spring Boot starter that provides concrete provider implementations and auto-configuration for JWT, OAuth2, Spring Security integration, and demonstrates full runtime wiring (including ServiceLoader fallback).
📦 java-base-security Design Plan
🎯 Overall Objectives
	• Spring Boot Starter Ecosystem
		○ Cung cấp auto-configuration + starter module.
		○ Sẵn sàng plug-and-play trong Spring Boot apps.
	• Contract-First Design
		○ java-base-security định nghĩa contracts & SPI (không phụ thuộc Spring).
		○ java-base-starter-security mới chứa implementation (JWT, OAuth2, Spring Security integration).
	• Scalability & Maintainability
		○ Dễ mở rộng (custom auth providers, multi-tenant, SSO).
		○ Backward-compatible API.
		○ Đảm bảo dùng dài hạn trong các enterprise projects.

📂 Module Structure

java-base-parent
├── java-base-bom (pom)
├── java-base-core (jar)
├── java-base-cache (jar)
├── java-base-observability (jar)
├── java-base-security (jar)           <-- abstraction + contracts
└── java-base-starter-security (jar)   <-- Spring Boot auto-config & impl


📦 java-base-security (jar, framework-agnostic)
Top-level package: io.github.base.security

io.github.base.security
 ├─ api
 │   ├─ AuthPrincipal.java          // Representation of authenticated user
 │   ├─ Authentication.java         // Authentication result contract
 │   ├─ AuthenticationRequest.java  // Input request (username/pwd, token…)
 │   ├─ AuthenticationManager.java  // Contract for authentication logic
 │   ├─ AuthorizationChecker.java   // Contract for RBAC/ABAC check
 │   ├─ TokenService.java           // Contract for issuing/verifying tokens
 │   ├─ SecurityException.java
 │   └─ ErrorCodes.java             // Standardized error codes
 ├─ spi
 │   └─ SecurityProvider.java       // SPI, loadable via ServiceLoader
 ├─ model
 │   ├─ Role.java
 │   ├─ Permission.java
 │   └─ Claim.java
 ├─ internal
 │   └─ …                           // helper classes (non-public)
 └─ SecurityServices.java            // ServiceLoader helper

✅ Characteristics:
	• Pure Java, không Spring.
	• Định nghĩa chuẩn về AuthPrincipal, TokenService, AuthorizationChecker.
	• SPI để load provider (JWT, OAuth2, Keycloak…) bằng ServiceLoader.

📦 java-base-starter-security (Spring Boot starter)
Top-level package: io.github.base.starter.security

io.github.base.starter.security
 ├─ autoconfig
 │   ├─ SecurityAutoConfiguration.java
 │   ├─ JwtProperties.java
 │   └─ OAuth2Properties.java
 ├─ jwt
 │   ├─ JwtTokenService.java         // JWT impl of TokenService
 │   ├─ JwtAuthenticationManager.java
 │   └─ JwtSecurityProvider.java
 ├─ oauth2
 │   ├─ OAuth2TokenService.java
 │   ├─ OAuth2AuthenticationManager.java
 │   └─ OAuth2SecurityProvider.java
 └─ resources
     └─ META-INF/services/io.github.base.security.spi.SecurityProvider

✅ Characteristics:
	• Auto-config cho TokenService, AuthenticationManager.
	• Hỗ trợ chọn provider qua application.yml:

base.security.type: jwt
base.security.jwt.secret: changeme
base.security.jwt.expiration: 3600
# hoặc
base.security.type: oauth2
base.security.oauth2.issuer-uri: https://idp.example.com
	• Tích hợp Spring Security (adapter để map AuthPrincipal → Authentication object).
	• Sẵn sàng mở rộng thêm Keycloak, SAML, LDAP provider.

🧩 Key Components
1. Authentication
	• AuthenticationRequest: input (username/password, token…).
	• Authentication: result (principal + authorities + context).
	• AuthenticationManager: xử lý logic auth (contract).
2. Authorization
	• AuthorizationChecker: kiểm tra quyền dựa trên Role/Permission.
	• Có thể extend sang ABAC (attribute-based).
3. Token Service
	• TokenService: contract để issue/verify token.
	• Starter cung cấp JwtTokenService mặc định.
4. Security Provider SPI
	• SecurityProvider: loadable via ServiceLoader.
	• Trả về implementation cho AuthenticationManager, TokenService.

📖 Example Usage
Core (framework-agnostic):

AuthenticationRequest request = new AuthenticationRequest("alice", "secret");
AuthenticationManager manager = SecurityServices.loadProviders()
                                                .get("jwt")
                                                .authenticationManager();
Authentication auth = manager.authenticate(request);
System.out.println("User: " + auth.principal().getName());
Spring Boot App:

base.security.type: jwt
base.security.jwt.secret: my-secret
base.security.jwt.expiration: 3600

@RestController
class DemoController {
    private final TokenService tokenService;
DemoController(TokenService tokenService) {
        this.tokenService = tokenService;
    }
@GetMapping("/token")
    String token() {
        return tokenService.issueToken(Map.of("sub", "alice"));
    }
}

🧪 Testing & Quality
	• Unit tests (java-base-security): contracts, SPI loading, exceptions.
	• Integration tests (java-base-starter-security): JWT validation, Spring Security integration.
	• Code coverage ≥ 80% (Jacoco).
	• Backward compatibility check (Japicmp / Revapi).

✅ Standards
	• Java 21+
	• Javadoc đầy đủ cho public API.
	• Static utils: final class + private constructor.
	• Không phụ thuộc Spring trong core.
	• Package spi chỉ để load provider.
	• Follows SOLID, KISS, DRY.

🚀 Deliverables
	• java-base-security: abstraction + SPI.
	• java-base-starter-security: Spring Boot auto-config + JWT/OAuth2 providers.
	• README.md: mô tả design, usage, extensibility.
	• Sample demo app dùng JWT.
