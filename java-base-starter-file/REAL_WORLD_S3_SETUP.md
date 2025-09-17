# Hướng dẫn setup S3 trong dự án thực tế

## Vấn đề: "Provider is not available: s3"

Lỗi này xảy ra khi AWS SDK không có trên classpath trong dự án thực tế của bạn.

## Giải pháp hoàn chỉnh

### 1. Thêm AWS Dependencies

#### Maven (`pom.xml`)
```xml
<dependencies>
    <!-- Java Base File Storage -->
    <dependency>
        <groupId>io.github.base</groupId>
        <artifactId>java-base-starter-file</artifactId>
        <version>1.0.5-SNAPSHOT</version>
    </dependency>
    
    <!-- AWS SDK v2 for S3 -->
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>s3</artifactId>
        <version>2.20.162</version>
    </dependency>
</dependencies>
```

#### Gradle (`build.gradle`)
```gradle
dependencies {
    implementation 'io.github.base:java-base-starter-file:1.0.5-SNAPSHOT'
    implementation 'software.amazon.awssdk:s3:2.20.162'
}
```

### 2. Cấu hình trong `application.yml`

```yaml
base:
  file:
    storage:
      enabled: true
      default-provider: s3
      providers:
        s3:
          type: s3
          enabled: true
          priority: 100
          properties:
            bucket-name: ${AWS_S3_BUCKET_NAME:your-bucket-name}
            region: ${AWS_REGION:ap-northeast-1}
            access-key-id: ${AWS_ACCESS_KEY_ID:your-access-key}
            secret-access-key: ${AWS_SECRET_ACCESS_KEY:your-secret-key}
```

### 3. Set Environment Variables (tùy chọn)

```bash
export AWS_S3_BUCKET_NAME=your-bucket-name
export AWS_REGION=ap-northeast-1
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
```

### 4. Sử dụng trong Service

```java
@Service
public class FileService {
    
    @Autowired
    private FileStorage fileStorage;
    
    @Autowired
    private FileStorageManager fileStorageManager;
    
    public void uploadFile(File file) {
        // Sử dụng fileStorage để upload files
        fileStorage.upload("file-id", file, metadata);
    }
    
    public FileStorage getS3Storage() {
        // Lấy S3 storage cụ thể
        return fileStorageManager.getStorage("s3");
    }
}
```

## Kiểm tra Dependencies

### Maven
```bash
mvn dependency:tree | grep awssdk
```

### Gradle
```bash
./gradlew dependencies | grep awssdk
```

Bạn sẽ thấy output như:
```
[INFO] +- software.amazon.awssdk:s3:jar:2.20.162:compile
[INFO] +- software.amazon.awssdk:aws-core:jar:2.20.162:compile
[INFO] +- software.amazon.awssdk:netty-nio-client:jar:2.20.162:compile
```

## Test Dependencies

Tạo test để kiểm tra AWS SDK:

```java
@Test
void shouldHaveAwsSdkOnClasspath() {
    try {
        Class.forName("software.amazon.awssdk.services.s3.S3Client");
        System.out.println("✅ AWS SDK S3 is available");
    } catch (ClassNotFoundException e) {
        fail("❌ AWS SDK S3 is not available: " + e.getMessage());
    }
}
```

## Troubleshooting

### Lỗi: "Provider is not available: s3"
**Nguyên nhân**: AWS SDK chưa được thêm vào dependencies
**Giải pháp**: Thêm dependency `software.amazon.awssdk:s3` vào `pom.xml` hoặc `build.gradle`

### Lỗi: "Failed to get S3 operations"
**Nguyên nhân**: S3ServiceProvider không được load
**Giải pháp**: Kiểm tra file `META-INF/services/io.github.base.aws.spi.AwsServiceProvider` có chứa `io.github.base.starter.file.aws.s3.S3ServiceProvider`

### Lỗi: "S3FileStorage requires configuration"
**Nguyên nhân**: Configuration không được load đúng
**Giải pháp**: Kiểm tra `application.yml` có đúng format không

## Logs để Debug

Khi có vấn đề, hãy kiểm tra logs:

```
INFO  - S3 provider detected without configuration, attempting to use environment variables as fallback
WARN  - Failed to get default storage 's3': S3FileStorage requires configuration...
INFO  - Using fallback storage provider: local
```

## Kết luận

Sau khi thêm AWS SDK dependencies và cấu hình đúng, S3 storage sẽ hoạt động bình thường và không còn fallback sang Local storage nữa.
