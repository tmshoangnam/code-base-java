# Quick Start Guide

## 🚀 Getting Started in 5 Minutes

### 1. Add Dependency

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-starter</artifactId>
    <version>1.0.3-SNAPSHOT</version>
</dependency>
```

### 2. Configure Cache (Choose One)

#### Option A: Caffeine (Default - No Redis Required)
```yaml
# application.yml
base:
  cache:
    provider: caffeine
    caffeine:
      ttl-seconds: 300
      max-size: 1000
```

#### Option B: Redis (Requires Redis Server)
```yaml
# application.yml
base:
  cache:
    provider: redis
    redis:
      enabled: true
      url: redis://localhost:6379
      namespace: myapp
    caffeine:
      ttl-seconds: 300  # Fallback configuration
      max-size: 1000
```

#### Option C: Redis with Fallback (Recommended for Development)
```yaml
# application.yml
base:
  cache:
    provider: redis
    redis:
      enabled: false  # Disable Redis, use Caffeine fallback
    caffeine:
      ttl-seconds: 300
      max-size: 1000
```

### 3. Use Cache in Your Code

```java
@Service
public class UserService {
    
    private final CacheManager cacheManager;
    
    public UserService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    public User findUser(String userId) {
        Cache<String, User> userCache = cacheManager.getCache("users");
        
        return userCache.get(userId)
            .orElseGet(() -> {
                User user = loadUserFromDatabase(userId);
                userCache.put(userId, user);
                return user;
            });
    }
    
    private User loadUserFromDatabase(String userId) {
        // Your database logic here
        return new User(userId, "John Doe");
    }
}
```

### 4. Run Your Application

```bash
mvn spring-boot:run
```

## 🔧 Common Issues & Solutions

### Issue: Redis Connection Refused
```
Connection refused: localhost/127.0.0.1:6379
```

**Solution**: Disable Redis and use Caffeine:
```yaml
base:
  cache:
    provider: redis
    redis:
      enabled: false  # This will use Caffeine instead
```

### Issue: ClassNotFoundException for Redis
```
java.lang.ClassNotFoundException: io.lettuce.core.RedisClient
```

**Solution**: Add Redis dependency or use Caffeine:
```xml
<!-- Add this if you want Redis support -->
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
</dependency>
```

### Issue: Cache Not Working
**Solution**: Check your configuration and ensure CacheManager is injected:
```java
@Autowired
private CacheManager cacheManager;  // Make sure this is injected
```

## 🎯 Advanced Usage

### Async Operations (Redis Only)
```java
AsyncCache<String, User> asyncCache = (AsyncCache<String, User>) cache;

CompletableFuture<Optional<User>> userFuture = asyncCache.getAsync("user123");
userFuture.thenAccept(user -> {
    if (user.isPresent()) {
        System.out.println("Found user: " + user.get());
    }
});
```

### Custom Key Generation
```java
KeyGenerator keyGenerator = new SimpleKeyGenerator();
String cacheKey = keyGenerator.generate("user", userId, "profile");
cache.put(cacheKey, userProfile);
```

### Multiple Cache Instances
```java
Cache<String, User> userCache = cacheManager.getCache("users");
Cache<String, Product> productCache = cacheManager.getCache("products");
Cache<String, Order> orderCache = cacheManager.getCache("orders");
```

## 📋 Configuration Reference

### Caffeine Properties
| Property | Default | Description |
|----------|---------|-------------|
| `base.cache.caffeine.ttl-seconds` | 60 | Time-to-live in seconds |
| `base.cache.caffeine.max-size` | 10000 | Maximum number of entries |

### Redis Properties
| Property | Default | Description |
|----------|---------|-------------|
| `base.cache.redis.enabled` | true | Whether Redis is enabled |
| `base.cache.redis.url` | `redis://localhost:6379` | Redis server URL |
| `base.cache.redis.namespace` | `base` | Namespace prefix |

## 🚀 Next Steps

1. **Add Metrics**: Integrate with Micrometer for cache monitoring
2. **Add Health Checks**: Monitor Redis connectivity
3. **Custom Serializers**: Implement custom serialization for complex objects
4. **Custom Providers**: Create your own cache provider implementations

## 📚 More Information

- [Full Documentation](README.md)
- [API Reference](docs/api.md)
- [Configuration Examples](examples/)
- [Troubleshooting Guide](README.md#troubleshooting)
