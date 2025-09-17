# Hướng dẫn thêm AWS Dependencies

## Vấn đề: "Provider is not available: s3"

Lỗi này xảy ra khi AWS SDK không có trên classpath. Module `java-base-starter-file` cần AWS SDK để hoạt động với S3.

## Giải pháp: Thêm AWS Dependencies

### 1. Thêm vào `pom.xml` (Maven)

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
    
    <!-- Optional: AWS SDK v2 BOM for version management -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>software.amazon.awssdk</groupId>
                <artifactId>bom</artifactId>
                <version>2.20.162</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</dependencies>
```

### 2. Thêm vào `build.gradle` (Gradle)

```gradle
dependencies {
    // Java Base File Storage
    implementation 'io.github.base:java-base-starter-file:1.0.5-SNAPSHOT'
    
    // AWS SDK v2 for S3
    implementation 'software.amazon.awssdk:s3:2.20.162'
    
    // Optional: AWS SDK v2 BOM for version management
    implementation platform('software.amazon.awssdk:bom:2.20.162')
}
```

### 3. Kiểm tra Dependencies

Sau khi thêm dependencies, kiểm tra:

```bash
# Maven
mvn dependency:tree | grep awssdk

# Gradle
./gradlew dependencies | grep awssdk
```

Bạn sẽ thấy output như:
```
[INFO] +- software.amazon.awssdk:s3:jar:2.20.162:compile
[INFO] +- software.amazon.awssdk:aws-core:jar:2.20.162:compile
[INFO] +- software.amazon.awssdk:netty-nio-client:jar:2.20.162:compile
[INFO] +- software.amazon.awssdk:apache-client:jar:2.20.162:compile
```

## Các Dependencies AWS SDK cần thiết

### Core Dependencies (tự động được include)
- `software.amazon.awssdk:aws-core`
- `software.amazon.awssdk:auth`
- `software.amazon.awssdk:regions`
- `software.amazon.awssdk:utils`

### S3 Specific Dependencies (tự động được include)
- `software.amazon.awssdk:s3`
- `software.amazon.awssdk:s3-transfer-manager`

### HTTP Client Dependencies (tự động được include)
- `software.amazon.awssdk:netty-nio-client` (default)
- `software.amazon.awssdk:apache-client` (alternative)

## Troubleshooting

### Lỗi: "ClassNotFoundException: software.amazon.awssdk.services.s3.S3Client"
**Nguyên nhân**: AWS SDK chưa được thêm vào dependencies
**Giải pháp**: Thêm dependency `software.amazon.awssdk:s3` vào `pom.xml` hoặc `build.gradle`

### Lỗi: "NoClassDefFoundError"
**Nguyên nhân**: AWS SDK có trong dependencies nhưng thiếu transitive dependencies
**Giải pháp**: Sử dụng AWS SDK BOM để quản lý versions

### Lỗi: "Provider is not available: s3"
**Nguyên nhân**: `S3FileStorageProvider.isAvailable()` trả về `false`
**Giải pháp**: Kiểm tra AWS SDK có trên classpath không

## Test Dependencies

Để test xem dependencies có đúng không:

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

## Minimal Dependencies

Nếu bạn chỉ cần S3 cơ bản, có thể chỉ cần:

```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.162</version>
</dependency>
```

AWS SDK sẽ tự động include các dependencies cần thiết.

## Kết luận

Sau khi thêm AWS SDK dependencies, lỗi "Provider is not available: s3" sẽ biến mất và S3 storage sẽ hoạt động bình thường.
