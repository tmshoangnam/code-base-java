# My-Base BOM (Bill of Materials)

## 📋 Tổng quan

`java-base-bom` là module quản lý tất cả dependency versions cho hệ thống `my-base`. Module này đảm bảo tính nhất quán về versions giữa các modules và ứng dụng sử dụng.

## 🎯 Mục đích

- **Version Management**: Khoá tất cả dependency versions để tránh drift
- **Dependency Convergence**: Đảm bảo không có version conflicts
- **Centralized Control**: Tập trung quản lý versions tại một nơi
- **Compatibility**: Đảm bảo tương thích giữa các dependencies

## 🏗️ Kiến trúc

BOM module import `spring-boot-dependencies` và khai báo versions cho:

- **Internal Modules**: java-base-core, java-base-security, java-base-cache, java-base-observability, java-base-starter
- **External Dependencies**: Resilience4j, Nimbus JWT, Caffeine, Bucket4j, Micrometer, OpenTelemetry
- **Spring Boot Starters**: Web, Security, Actuator, Validation, Cache, AOP
- **Testing Dependencies**: JUnit 5, TestContainers, Spring Security Test

## 🚀 Cách sử dụng

### 1. Import BOM vào project

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.tmshoangnam</groupId>
            <artifactId>java-base-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 2. Sử dụng dependencies không cần version

```xml
<dependencies>
    <!-- Không cần chỉ định version -->
    <dependency>
        <groupId>io.github.resilience4j</groupId>
        <artifactId>resilience4j-circuitbreaker</artifactId>
    </dependency>
    
    <dependency>
        <groupId>com.nimbusds</groupId>
        <artifactId>nimbus-jose-jwt</artifactId>
    </dependency>
    
    <dependency>
        <groupId>com.github.ben-manes.caffeine</groupId>
        <artifactId>caffeine</artifactId>
    </dependency>
</dependencies>
```

## 📦 Dependencies được quản lý

### Resilience4j
- `resilience4j-core`: 2.2.0
- `resilience4j-circuitbreaker`: 2.2.0
- `resilience4j-retry`: 2.2.0
- `resilience4j-timelimiter`: 2.2.0
- `resilience4j-bulkhead`: 2.2.0
- `resilience4j-ratelimiter`: 2.2.0

### Security
- `nimbus-jose-jwt`: 9.37.3
- `owasp-encoder`: 1.2.3
- `bucket4j-core`: 8.10.1
- `bucket4j-redis`: 8.10.1

### Cache
- `caffeine`: 3.1.8
- `spring-boot-starter-data-redis`: 3.3.2
- `lettuce-core`: 6.3.1.RELEASE

### Observability
- `micrometer-core`: 1.13.4
- `micrometer-registry-prometheus`: 1.13.4
- `micrometer-tracing-bridge-otel`: 1.3.4
- `opentelemetry-api`: 1.39.0
- `opentelemetry-sdk`: 1.39.0
- `opentelemetry-exporter-otlp`: 1.39.0

### Spring Boot Starters
- `spring-boot-starter`: 3.3.2
- `spring-boot-starter-web`: 3.3.2
- `spring-boot-starter-security`: 3.3.2
- `spring-boot-starter-actuator`: 3.3.2
- `spring-boot-starter-validation`: 3.3.2
- `spring-boot-starter-cache`: 3.3.2
- `spring-boot-starter-aop`: 3.3.2

### Testing
- `spring-boot-starter-test`: 3.3.2
- `spring-security-test`: 6.2.1
- `junit-jupiter`: 5.10.2
- `testcontainers`: 1.19.6

## ⚙️ Cấu hình

### Properties
```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>
```

### Build Plugins
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-enforcer-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

## 🔍 Kiểm tra Effective POM

Để xem effective POM với tất cả dependencies:

```bash
mvn -pl java-base-bom -am -DskipTests help:effective-pom
```

## 📊 Version Compatibility Matrix

| Component | Version | Spring Boot | Java |
|-----------|---------|-------------|------|
| Resilience4j | 2.2.0 | 3.3.2 | 17+ |
| Nimbus JWT | 9.37.3 | 3.3.2 | 17+ |
| Caffeine | 3.1.8 | 3.3.2 | 17+ |
| Bucket4j | 8.10.1 | 3.3.2 | 17+ |
| Micrometer | 1.13.4 | 3.3.2 | 17+ |
| OpenTelemetry | 1.39.0 | 3.3.2 | 17+ |

## 🚨 Lưu ý quan trọng

### Version Locking
- Tất cả versions được khoá cứng để tránh drift
- Không sử dụng version ranges (SNAPSHOT, LATEST)
- Chỉ update versions khi có lý do chính đáng

### Dependency Convergence
- Sử dụng `maven-enforcer-plugin` để đảm bảo convergence
- Ban duplicate classes
- Require dependency convergence

### Spring Boot Compatibility
- BOM import `spring-boot-dependencies` để đồng bộ
- Đảm bảo compatibility với Spring Boot 3.3.2
- Test với different Spring Boot versions

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Effective POM Test
```bash
mvn help:effective-pom
```

## 📚 Tài liệu tham khảo

- [Maven BOM Best Practices](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)
- [Spring Boot Dependencies](https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html)
- [Dependency Convergence](https://maven.apache.org/enforcer/enforcer-rules/dependencyConvergence.html)

## 🔄 Cập nhật Versions

### Quy trình cập nhật
1. **Assessment**: Đánh giá rủi ro của việc update
2. **Testing**: Test compatibility với existing code
3. **Documentation**: Cập nhật changelog và compatibility matrix
4. **Release**: Release new version với proper versioning

### Breaking Changes
- Major version updates có thể breaking
- Minor version updates nên backward compatible
- Patch updates chỉ fix bugs và security issues

---

**Lưu ý**: BOM module này là foundation cho toàn bộ hệ thống my-base. Mọi thay đổi versions cần được test kỹ lưỡng để đảm bảo compatibility.
