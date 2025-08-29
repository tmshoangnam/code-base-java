# My-Base: Enterprise Foundation Libraries

[![CI/CD Pipeline](https://github.com/mycompany/my-base/workflows/CI%2FCD%20Pipeline/badge.svg)](https://github.com/mycompany/my-base/actions)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=mycompany_my-base&metric=alert_status)](https://sonarcloud.io/dashboard?id=mycompany_my-base)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mycompany_my-base&metric=coverage)](https://sonarcloud.io/dashboard?id=mycompany_my-base)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=mycompany_my-base&metric=security_rating)](https://sonarcloud.io/dashboard?id=mycompany_my-base)

## 📋 Tổng quan

My-Base là hệ thống foundation libraries enterprise-grade cho Spring Boot applications, cung cấp:

- **Core Utilities**: Resilience patterns, error handling, structured logging
- **Security**: JWT authentication, rate limiting, input validation
- **Caching**: Multi-level caching với Redis và Caffeine
- **Observability**: Health checks, metrics, distributed tracing
- **Zero Configuration**: Auto-configuration với sensible defaults

## 🏗️ Kiến trúc

```
java-base-parent (pom)
├── java-base-bom (pom) - Dependency management
├── java-base-core (jar) - Core utilities
├── java-base-security (jar) - Security contracts
├── java-base-cache (jar) - Cache abstraction
├── java-base-observability (jar) - Monitoring contracts
└── java-base-starter (jar) - Auto-configuration
```

## 🚀 Bắt đầu nhanh

### 1. Thêm BOM dependency

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.mycompany.base</groupId>
            <artifactId>java-base-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 2. Thêm Starter dependency

```xml
<dependencies>
    <dependency>
        <groupId>com.mycompany.base</groupId>
        <artifactId>java-base-starter</artifactId>
    </dependency>
</dependencies>
```

### 3. Cấu hình cơ bản

```yaml
base:
  enabled: true
  core:
    enabled: true
  security:
    enabled: true
  cache:
    enabled: true
  observability:
    enabled: true
```

## 📦 Modules

### java-base-core
- **Resilience**: Circuit breaker, retry, timeout, bulkhead
- **Error Handling**: Exception hierarchy, ProblemDetail mapping
- **Logging**: Structured logging với correlation ID
- **Utilities**: Common helpers, validation

### java-base-security
- **JWT**: Token validation với Nimbus, key rotation
- **Rate Limiting**: Request throttling, brute force protection
- **Input Validation**: XSS protection, SQL injection prevention
- **Audit Logging**: Security events tracking

### java-base-cache
- **Abstraction**: Unified interface cho multiple backends
- **Multi-level**: Local + distributed caching
- **Cache Warming**: Preload critical data
- **Performance**: Hit/miss ratio monitoring

### java-base-observability
- **Health Checks**: Application và dependency health
- **Metrics**: Micrometer integration, custom KPIs
- **Tracing**: OpenTelemetry support
- **Logging**: Structured logging, correlation

### java-base-starter
- **Auto-configuration**: Conditional loading
- **Zero Configuration**: Sensible defaults
- **Bean Override**: Allow application customization
- **Integration**: Seamless Spring Boot integration

## ⚙️ Cấu hình

### Master Switch
```yaml
base:
  enabled: true  # Master switch cho tất cả features
```

### Conditional Loading
```yaml
base:
  core:
    enabled: true      # Always enabled
  security:
    enabled: true      # Conditional on Spring Security
  cache:
    enabled: true      # Conditional on cache backend
  observability:
    enabled: true      # Conditional on Actuator
```

### Security Configuration
```yaml
base:
  security:
    jwt:
      issuer: mycorp
      audience: myapps
      jwks-uri: ${JWT_JWKS_URI}
    rate-limiting:
      enabled: true
      default-limit: 100
      default-window: 1m
```

### Cache Configuration
```yaml
base:
  cache:
    type: redis  # hoặc caffeine, hybrid
    redis:
      cluster:
        enabled: false
        nodes: ["redis1:6379", "redis2:6379"]
    specs:
      userById:
        ttl-seconds: 600
        max-size: 10000
```

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Quality Analysis
```bash
mvn -P quality clean verify
```

### Security Scan
```bash
mvn org.owasp:dependency-check-maven:check
```

## 📊 Quality Gates

- **Code Coverage**: >80% (JaCoCo)
- **Security Rating**: A (SonarQube)
- **Vulnerabilities**: 0 high/critical (OWASP)
- **Duplications**: <3%
- **Technical Debt**: <5%

## 🚀 CI/CD Pipeline

### Workflows
1. **Quality Check**: OWASP scan, SonarQube analysis
2. **Build & Test**: Compilation, unit tests, coverage
3. **Integration Test**: End-to-end testing
4. **Performance Test**: Load testing với JMeter
5. **Security Scan**: Trivy, Snyk vulnerability scanning
6. **Deploy**: Snapshot (develop), Release (main)

### Environments
- **Snapshot**: `develop` branch → Nexus snapshots
- **Release**: `main` branch → Nexus releases

## 📚 Documentation

- [API Documentation](docs/api/README.md)
- [Configuration Guide](docs/configuration/README.md)
- [Migration Guide](docs/migration/README.md)
- [Sample Applications](samples/README.md)

## 🔧 Development

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker (for integration tests)

### Build
```bash
mvn clean install
```

### Run Tests
```bash
mvn test
mvn verify
```

### Quality Check
```bash
mvn -P quality clean verify
```

### Deploy
```bash
mvn deploy  # Snapshot
mvn -P release deploy  # Release
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

### Development Guidelines
- Follow [Conventional Commits](https://conventionalcommits.org/)
- Maintain code coverage >80%
- Pass all quality gates
- Update documentation
- Add tests for new features

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Issues**: [GitHub Issues](https://github.com/mycompany/my-base/issues)
- **Documentation**: [Wiki](https://github.com/mycompany/my-base/wiki)
- **Discussions**: [GitHub Discussions](https://github.com/mycompany/my-base/discussions)

## 🔗 Links

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Micrometer Documentation](https://micrometer.io/)
- [OpenTelemetry Documentation](https://opentelemetry.io/)
- [OWASP Security Guidelines](https://owasp.org/)

---

**Made with ❤️ by MyCompany Team**
