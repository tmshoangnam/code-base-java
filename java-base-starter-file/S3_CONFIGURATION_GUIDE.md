# Hướng dẫn cấu hình S3 cho dự án thực tế

## Vấn đề đã được khắc phục

Lỗi **"S3FileStorage requires configuration"** đã được khắc phục hoàn toàn. Module hiện tại có thể:

1. ✅ Tự động fallback sang Local storage khi S3 không có configuration
2. ✅ Sử dụng environment variables làm fallback cho S3
3. ✅ Xử lý configuration từ `application.yml` một cách chính xác

## Cách cấu hình trong dự án thực tế

### 1. Cấu hình trong `application.yml`

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

### 2. Cấu hình Environment Variables (tùy chọn)

Nếu không muốn hardcode credentials trong `application.yml`, có thể sử dụng environment variables:

```bash
export AWS_S3_BUCKET_NAME=your-bucket-name
export AWS_REGION=ap-northeast-1
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
```

### 3. Sử dụng trong Service

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

## Các trường hợp xử lý

### Trường hợp 1: Có configuration đầy đủ
- ✅ S3 sẽ hoạt động bình thường với configuration từ `application.yml`
- ✅ Tất cả S3 operations sẽ hoạt động

### Trường hợp 2: Không có configuration
- ✅ Hệ thống sẽ tự động fallback sang Local storage
- ✅ Application vẫn khởi động được
- ✅ Log warning để thông báo về việc fallback

### Trường hợp 3: Có environment variables
- ✅ S3 sẽ sử dụng environment variables làm fallback
- ✅ Không cần configuration trong `application.yml`

## Troubleshooting

### Lỗi: "S3FileStorage requires configuration"
**Nguyên nhân**: Không có configuration cho S3 và không có environment variables
**Giải pháp**: 
1. Thêm configuration vào `application.yml` (khuyến nghị)
2. Hoặc set environment variables
3. Hoặc để hệ thống fallback sang Local storage

### Lỗi: "No file storage providers available"
**Nguyên nhân**: Tất cả storage providers đều fail
**Giải pháp**: Kiểm tra dependencies và configuration

### Lỗi: "AWS service provider not found"
**Nguyên nhân**: Thiếu AWS SDK dependencies
**Giải pháp**: Thêm AWS SDK vào `pom.xml`:

```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.162</version>
</dependency>
```

## Dependencies cần thiết

```xml
<dependency>
    <groupId>io.github.base</groupId>
    <artifactId>java-base-starter-file</artifactId>
    <version>1.0.5-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.162</version>
</dependency>
```

## Logs để debug

Khi có vấn đề, hãy kiểm tra logs:

```
INFO  - S3 provider detected without configuration, attempting to use environment variables as fallback
WARN  - Failed to get default storage 's3': S3FileStorage requires configuration...
INFO  - Using fallback storage provider: local
```

## Kết luận

Module đã được cải thiện để xử lý tất cả các trường hợp:
- ✅ Configuration đầy đủ → S3 hoạt động
- ✅ Không có configuration → Fallback sang Local
- ✅ Có environment variables → S3 sử dụng env vars
- ✅ Application luôn khởi động được

Bạn có thể yên tâm sử dụng module này trong dự án thực tế!
