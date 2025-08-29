# My Base Core

Core utilities for my-base foundation libraries providing enterprise-grade resilience, logging, validation, and exception handling patterns.

## Features

- **Resilience Patterns**: Circuit Breaker, Retry, Bulkhead, Time Limiter using Resilience4j
- **Logging Utilities**: Structured logging with correlation ID and MDC support
- **Validation Framework**: Jakarta Validation utilities with custom validators
- **Exception Handling**: Base exception classes with error codes and types
- **Configuration Management**: External configuration support for all resilience patterns

## Quick Start

### Maven Dependency

```xml
<dependency>
    <groupId>com.mycompany.base</groupId>
    <artifactId>java-base-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Basic Usage

#### Resilience Patterns

```java
import com.mycompany.base.core.resilience.ResilienceUtils;
import com.mycompany.base.core.resilience.ResilienceProperties;

@Service
public class UserService {
    
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;
    
    public UserService(ResilienceProperties properties) {
        this.circuitBreaker = ResilienceUtils.createCircuitBreaker("user-service", properties);
        this.retry = ResilienceUtils.createRetry("user-service", properties);
    }
    
    public User getUser(String id) {
        return ResilienceUtils.executeWithCircuitBreaker(circuitBreaker, () -> {
            // Your business logic here
            return userRepository.findById(id);
        });
    }
}
```

#### Logging Utilities

```java
import com.mycompany.base.core.logging.LoggingUtils;
import org.slf4j.Logger;

@Service
public class OrderService {
    
    private static final Logger logger = LoggingUtils.getLogger(OrderService.class);
    
    public void processOrder(Order order) {
        String correlationId = LoggingUtils.generateAndSetCorrelationId();
        
        try {
            LoggingUtils.logMethodEntry(logger, "processOrder", order.getId());
            
            // Business logic here
            
            LoggingUtils.logMethodExit(logger, "processOrder", "SUCCESS");
        } finally {
            LoggingUtils.clearContext();
        }
    }
}
```

#### Validation Utilities

```java
import com.mycompany.base.core.validation.ValidationUtils;

@Service
public class ProductService {
    
    public void createProduct(Product product) {
        // Validate and throw exception if invalid
        ValidationUtils.validateOrThrow(product);
        
        // Or get validation errors as string
        String errors = ValidationUtils.validateAndGetErrors(product);
        if (!errors.isEmpty()) {
            logger.warn("Product validation warnings: {}", errors);
        }
        
        // Business logic here
    }
}
```

#### Exception Handling

```java
import com.mycompany.base.core.exception.BaseException;

public class UserNotFoundException extends BaseException {
    
    public UserNotFoundException(String userId) {
        super("User not found with ID: " + userId, "USER_NOT_FOUND", "NOT_FOUND");
    }
    
    public UserNotFoundException(String userId, Throwable cause) {
        super("User not found with ID: " + userId, "USER_NOT_FOUND", "NOT_FOUND", cause);
    }
}
```

## Configuration

### Resilience Configuration

Create `application.yml` in your project:

```yaml
my-base:
  resilience:
    circuit-breaker:
      failure-rate-threshold: 50
      wait-duration-in-open-state: 60s
      sliding-window-size: 100
      minimum-number-of-calls: 10
      slow-call-duration-threshold: 2s
      slow-call-rate-threshold: 100
      automatic-transition-from-open-to-half-open-enabled: true
    
    retry:
      max-attempts: 3
      wait-duration: 100ms
      enable-exponential-backoff: false
      multiplier: 2.0
      max-wait-duration: 10s
    
    bulkhead:
      max-concurrent-calls: 10
      max-wait-duration: 100ms
      max-concurrent-calls-per-thread: 5
    
    time-limiter:
      timeout-duration: 5s
      cancel-running-future: true
```

### Logging Configuration

```yaml
logging:
  level:
    com.mycompany.base.core: DEBUG
    com.mycompany.base.core.resilience: DEBUG
  
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} [%X{correlationId}] - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} [%X{correlationId}] - %msg%n"
```

## Advanced Usage

### Custom Resilience Configuration

```java
@Configuration
public class ResilienceConfig {
    
    @Bean
    public CircuitBreaker userServiceCircuitBreaker(ResilienceProperties properties) {
        return ResilienceUtils.createCircuitBreaker("user-service", properties);
    }
    
    @Bean
    public Retry userServiceRetry(ResilienceProperties properties) {
        return ResilienceUtils.createRetry("user-service", properties);
    }
}
```

### Combined Resilience Patterns

```java
public User getUserWithResilience(String id) {
    return ResilienceUtils.executeWithResilience(
        circuitBreaker,
        retry,
        bulkhead,
        timeLimiter,
        () -> userRepository.findById(id)
    );
}
```

### Custom Validation

```java
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;
        
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*\\d.*") &&
               password.matches(".*[!@#$%^&*].*");
    }
}
```

## Testing

### Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class ResilienceUtilsTest {
    
    @Test
    void createCircuitBreaker_WithCustomConfig_ShouldApplyProperties() {
        // Given
        ResilienceProperties props = new ResilienceProperties();
        props.getCircuitBreaker().setFailureRateThreshold(75);
        
        // When
        CircuitBreaker cb = ResilienceUtils.createCircuitBreaker("test", props);
        
        // Then
        assertThat(cb.getCircuitBreakerConfig().getFailureRateThreshold())
            .isEqualTo(75);
    }
}
```

### Integration Tests

```java
@SpringBootTest
@ActiveProfiles("test")
class ResilienceIntegrationTest {
    
    @Autowired
    private ResilienceProperties properties;
    
    @Test
    void shouldLoadResilienceProperties() {
        assertThat(properties).isNotNull();
        assertThat(properties.getCircuitBreaker().getFailureRateThreshold())
            .isEqualTo(50);
    }
}
```

## Best Practices

### 1. Naming Conventions
- Use descriptive names for resilience instances (e.g., "user-service", "payment-gateway")
- Follow consistent naming patterns across your application

### 2. Configuration Management
- Externalize all configuration values
- Use profiles for different environments (dev, test, prod)
- Validate configuration at startup

### 3. Monitoring and Observability
- Enable resilience4j metrics
- Use correlation IDs for request tracing
- Log resilience state changes

### 4. Error Handling
- Always provide meaningful error messages
- Use appropriate error codes and types
- Include context information in exceptions

### 5. Testing
- Test both success and failure scenarios
- Verify resilience pattern behavior
- Use test containers for integration tests

## Troubleshooting

### Common Issues

1. **Configuration not loaded**: Ensure `@EnableConfigurationProperties` is present
2. **Resilience patterns not working**: Check if Spring Boot auto-configuration is enabled
3. **Validation errors**: Verify Jakarta Validation dependencies are included

### Debug Mode

Enable debug logging:

```yaml
logging:
  level:
    com.mycompany.base.core: DEBUG
    io.github.resilience4j: DEBUG
```

## Contributing

1. Follow the existing code style
2. Add comprehensive tests for new features
3. Update documentation for API changes
4. Ensure all tests pass before submitting PR

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the project repository
- Check the documentation
- Review existing issues and discussions
