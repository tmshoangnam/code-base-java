You are a Senior Software Engineer + System Architect (or an AI coding agent acting like one).
Your task: design and implement a production-ready, enterprise-grade file storage foundation split into two modules:
	• java-base-file — pure Java library (jar) that defines file storage contracts, SPI, and utilities (no Spring, no cloud SDKs).
	• java-base-starter-file — Spring Boot starter that provides concrete storage provider implementations and auto-configuration for Local, AWS S3, Google Cloud, Azure Blob, and demonstrates full runtime wiring (including ServiceLoader fallback).

📦 java-base-file Design Plan

🎯 Overall Objectives
	• Spring Boot Starter Ecosystem
		○ Cung cấp auto-configuration + starter module.
		○ Sẵn sàng plug-and-play trong Spring Boot apps.
	• Contract-First Design
		○ java-base-file định nghĩa contracts & SPI (không phụ thuộc Spring).
		○ java-base-starter-file mới chứa implementation (Local, S3, GCS, Azure).
	• Scalability & Maintainability
		○ Dễ mở rộng (custom storage providers, new file types).
		○ Backward-compatible API.
		○ Đảm bảo dùng dài hạn trong các enterprise projects.

📂 Module Structure

java-base-parent
├── java-base-bom (pom)
├── java-base-core (jar)
├── java-base-cache (jar)
├── java-base-observability (jar)
├── java-base-security (jar)
├── java-base-file (jar)              <-- abstraction + contracts
└── java-base-starter-file (jar)      <-- Spring Boot auto-config & impl

📦 java-base-file (jar, framework-agnostic)
Top-level package: io.github.base.file

io.github.base.file
 ├─ api
 │   ├─ storage
 │   │   ├─ FileStorage.java           // Core file storage contract
 │   │   ├─ FileStorageManager.java    // Multi-storage management
 │   │   └─ FileStorageProvider.java   // SPI for storage providers
 │   ├─ model
 │   │   ├─ FileType.java              // FILE, BYTE, STREAM enum
 │   │   ├─ FileMetadata.java          // File metadata record
 │   │   ├─ UploadResult.java          // Upload operation result
 │   │   └─ DownloadResult.java        // Download operation result
 │   ├─ exception
 │   │   ├─ FileStorageException.java  // Base exception
 │   │   ├─ FileNotFoundException.java
 │   │   └─ FileUploadException.java
 │   └─ util
 │       ├─ FileUtils.java             // File utilities
 │       └─ FileMetadataValidator.java // Validation utilities
 ├─ spi
 │   └─ FileStorageProvider.java       // SPI, loadable via ServiceLoader
 ├─ internal
 │   └─ …                              // helper classes (non-public)
 └─ FileServices.java                  // ServiceLoader helper

✅ Characteristics:
	• Pure Java, không Spring, không cloud SDKs.
	• Định nghĩa chuẩn về FileStorage, FileMetadata, UploadResult.
	• SPI để load provider (Local, S3, GCS, Azure…) bằng ServiceLoader.
	• Support multiple file types: FILE, BYTE, STREAM.

📦 java-base-starter-file (Spring Boot starter)
Top-level package: io.github.base.starter.file

io.github.base.starter.file
 ├─ autoconfig
 │   ├─ FileStorageAutoConfiguration.java
 │   ├─ FileStorageProperties.java
 │   └─ StorageProviderAutoConfiguration.java
 ├─ impl
 │   ├─ local
 │   │   ├─ LocalFileStorageProvider.java
 │   │   ├─ LocalFileStorage.java
 │   │   └─ LocalFileStorageManager.java
 │   ├─ s3
 │   │   ├─ S3FileStorageProvider.java
 │   │   ├─ S3FileStorage.java
 │   │   └─ S3FileStorageManager.java
 │   ├─ gcs
 │   │   ├─ GcsFileStorageProvider.java
 │   │   ├─ GcsFileStorage.java
 │   │   └─ GcsFileStorageManager.java
 │   └─ azure
 │       ├─ AzureFileStorageProvider.java
 │       ├─ AzureFileStorage.java
 │       └─ AzureFileStorageManager.java
 ├─ decorators
 │   ├─ ResilientFileStorage.java      // Resilience patterns
 │   ├─ LoggingFileStorage.java        // Structured logging
 │   ├─ CachedFileStorage.java         // Caching support
 │   ├─ SecureFileStorage.java         // Security integration
 │   └─ MetricsFileStorage.java        // Observability
 └─ resources
     └─ META-INF/services/io.github.base.file.spi.FileStorageProvider

✅ Characteristics:
	• Auto-config cho FileStorage, FileStorageManager.
	• Hỗ trợ chọn provider qua application.yml:

base.file.storage.default-provider: s3
base.file.storage.providers:
  local:
    base-path: /tmp/files
  s3:
    bucket-name: my-bucket
    region: us-east-1
  gcs:
    bucket-name: my-gcs-bucket
    project-id: my-project
  azure:
    container-name: my-container
    connection-string: ${AZURE_STORAGE_CONNECTION_STRING}
	• Tích hợp với java-base-core (resilience, logging).
	• Tích hợp với java-base-cache (metadata caching).
	• Tích hợp với java-base-security (access control, encryption).
	• Tích hợp với java-base-observability (metrics, tracing).

🧩 Key Components

1. File Storage Contract
	• FileStorage: core interface cho file operations (upload, download, delete, exists, list).
	• FileStorageManager: quản lý multiple storage providers.
	• Support cho 3 file types: FILE (java.io.File), BYTE (byte[]), STREAM (InputStream/OutputStream).

2. File Metadata
	• FileMetadata: chứa thông tin file (name, type, size, checksum, tags, timestamps).
	• UploadResult/DownloadResult: kết quả operations với success/error handling.
	• FileType enum: phân loại file content types.

3. Storage Provider SPI
	• FileStorageProvider: loadable via ServiceLoader.
	• Trả về implementation cho FileStorage, FileStorageManager.
	• Support cho Local, AWS S3, Google Cloud Storage, Azure Blob Storage.

4. Integration Layer
	• Decorator pattern cho resilience, logging, caching, security.
	• Tích hợp với existing base modules.
	• Spring Boot auto-configuration.

📖 Example Usage

Core (framework-agnostic):

FileStorageProvider provider = FileServices.findProvider("local");
FileStorage storage = provider.createStorage(Map.of("basePath", "/tmp/files"));

FileMetadata metadata = FileMetadata.of("document.pdf", "application/pdf", 1024);
UploadResult result = storage.upload("file123", content, metadata);

if (result.success()) {
    System.out.println("File uploaded: " + result.location());
}

Spring Boot App:

base.file.storage.default-provider: s3
base.file.storage.providers.s3.bucket-name: my-bucket
base.file.storage.providers.s3.region: us-east-1

@Service
class DocumentService {
    private final FileStorage fileStorage;
    
    DocumentService(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }
    
    @PostMapping("/upload")
    String uploadDocument(@RequestParam MultipartFile file) {
        FileMetadata metadata = FileMetadata.of(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize()
        );
        
        UploadResult result = fileStorage.upload(
            generateFileId(), 
            file.getBytes(), 
            metadata
        );
        
        return result.success() ? result.location() : "Upload failed";
    }
}

🧪 Testing & Quality
	• Unit tests (java-base-file): contracts, SPI loading, exceptions.
	• Integration tests (java-base-starter-file): Local, S3, GCS, Azure providers.
	• Testcontainers cho cloud providers testing.
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
	• java-base-file: abstraction + SPI.
	• java-base-starter-file: Spring Boot auto-config + Local/S3/GCS/Azure providers.
	• README.md: mô tả design, usage, extensibility.
	• Sample demo app dùng multiple storage providers.
