# QR Code Foundation - java-base-core

A production-ready, enterprise-grade QR Code foundation for Java applications, designed with contract-first principles and framework-agnostic architecture.

## 🎯 Overview

This QR Code foundation provides a robust, extensible, and maintainable solution for QR Code generation and decoding in enterprise Java applications. It follows the contract-first design principle, ensuring clean separation between API contracts and implementations.

### Key Features

- **Framework-agnostic**: No dependency on Spring or other frameworks
- **Contract-first design**: Clear API contracts with pluggable implementations
- **SPI-based architecture**: Easy to extend with new QR Code libraries
- **Multiple output formats**: PNG, JPEG, SVG, Base64
- **Comprehensive error handling**: Standardized exceptions with error codes
- **Production-ready**: Built-in observability, logging, and monitoring support
- **Extensible**: Support for custom content types and advanced options

## 📦 Dependencies

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-core</artifactId>
    <version>1.0.5</version>
</dependency>
```

### Optional Dependencies

The QR Code functionality uses ZXing as the default implementation. If you want to use a different QR Code library, you can exclude ZXing and provide your own implementation:

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-core</artifactId>
    <version>1.0.5</version>
    <exclusions>
        <exclusion>
            <groupId>com.google.zxing</groupId>
            <artifactId>core</artifactId>
        </exclusion>
        <exclusion>
            <groupId>com.google.zxing</groupId>
            <artifactId>javase</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

## 🚀 Quick Start

### Basic Usage

```java
import io.github.base.core.qrcode.QrCodeServices;
import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.api.QrCodeFormat;
import io.github.base.core.qrcode.model.QrCodeRequest;
import io.github.base.core.qrcode.model.QrCodeResult;

// Load the default QR Code service
QrCodeService service = QrCodeServices.loadDefault();

// Create a QR Code request
QrCodeRequest request = QrCodeRequest.builder()
    .content("https://example.com")
    .format(QrCodeFormat.PNG)
    .size(300)
    .build();

// Generate the QR Code
QrCodeResult result = service.encode(request);

// Save to file
Files.write(Paths.get("qrcode.png"), result.getBytes());
```

### Decoding QR Codes

```java
// Decode a QR Code from image bytes
byte[] imageBytes = Files.readAllBytes(Paths.get("qrcode.png"));
String decodedContent = service.decode(imageBytes);
System.out.println("Decoded content: " + decodedContent);
```

## 📋 API Reference

### Core Interfaces

#### QrCodeService

The main service interface for QR Code operations:

```java
public interface QrCodeService {
    QrCodeResult encode(QrCodeRequest request) throws QrCodeException;
    String decode(byte[] imageBytes) throws QrCodeException;
    boolean supportsFormat(QrCodeFormat format);
    String getServiceName();
}
```

#### QrCodeRequest

Request model for QR Code encoding:

```java
QrCodeRequest request = QrCodeRequest.builder()
    .content("Your content here")
    .format(QrCodeFormat.PNG)
    .size(300)
    .margin(4)
    .errorCorrectionLevel(ErrorCorrectionLevel.M)
    .options(customOptions)
    .build();
```

#### QrCodeResult

Result model containing the encoded QR Code:

```java
QrCodeResult result = service.encode(request);
byte[] imageBytes = result.getBytes();
String mimeType = result.getMimeType();
int width = result.getWidth();
int height = result.getHeight();
String base64String = result.getBase64String();
```

### Supported Formats

- **PNG**: `QrCodeFormat.PNG` (default)
- **JPEG**: `QrCodeFormat.JPEG`
- **SVG**: `QrCodeFormat.SVG` (limited support)
- **Base64**: `QrCodeFormat.BASE64`

### Error Correction Levels

- **L (Low)**: ~7% error correction
- **M (Medium)**: ~15% error correction (default)
- **Q (Quartile)**: ~25% error correction
- **H (High)**: ~30% error correction

## 🎨 Advanced Usage

### Custom Content Types

The foundation supports various content types through the `QrCodeContent` class:

```java
// URL content
QrCodeContent urlContent = QrCodeContent.url("https://example.com");

// vCard content
QrCodeContent vCardContent = QrCodeContent.vCard(
    "John Doe",
    "john@example.com",
    "+1-555-0123"
);

// WiFi content
QrCodeContent wifiContent = QrCodeContent.wifi(
    "MyNetwork",
    "WPA",
    "password123"
);

// Custom content
QrCodeContent customContent = QrCodeContent.custom(
    "Custom data",
    "Custom description"
);
```

### Custom Options

Advanced configuration options:

```java
QrCodeOptions options = QrCodeOptions.builder()
    .foregroundColor(Color.BLUE)
    .backgroundColor(Color.WHITE)
    .logoPath("/path/to/logo.png")
    .logoSize(50)
    .logoCentered(true)
    .customData("Additional metadata")
    .build();

QrCodeRequest request = QrCodeRequest.builder()
    .content("Content with custom options")
    .format(QrCodeFormat.PNG)
    .size(400)
    .options(options)
    .build();
```

### Service Provider Interface (SPI)

To provide your own QR Code implementation:

1. Implement the `QrCodeProvider` interface:

```java
public class MyQrCodeProvider implements QrCodeProvider {
    @Override
    public String getName() {
        return "my-provider";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public String getDescription() {
        return "My custom QR Code provider";
    }
    
    @Override
    public QrCodeService createService() throws QrCodeProviderException {
        return new MyQrCodeService();
    }
    
    @Override
    public boolean isAvailable() {
        return true; // Check if your dependencies are available
    }
    
    @Override
    public int getPriority() {
        return 50; // Lower priority than default (100)
    }
}
```

2. Register your provider in `META-INF/services/io.github.base.core.qrcode.spi.QrCodeProvider`:

```
com.example.MyQrCodeProvider
```

3. Use your provider:

```java
QrCodeService service = QrCodeServices.loadProvider("my-provider");
```

## 🔧 Configuration

### ServiceLoader Configuration

The foundation uses Java's ServiceLoader mechanism to discover providers. You can configure provider discovery:

```java
// List available providers
List<String> providers = QrCodeServices.getAvailableProviders();
System.out.println("Available providers: " + providers);

// Get provider details
List<QrCodeProvider> providerDetails = QrCodeServices.getAvailableProvidersDetails();
for (QrCodeProvider provider : providerDetails) {
    System.out.println(provider.getName() + " v" + provider.getVersion());
}

// Clear service cache
QrCodeServices.clearCache();

// Reload providers
QrCodeServices.reload();
```

## 🧪 Testing

### Unit Tests

The foundation includes comprehensive unit tests:

```java
@Test
void shouldEncodeValidContentSuccessfully() throws QrCodeException {
    QrCodeRequest request = QrCodeRequest.builder()
        .content("test content")
        .format(QrCodeFormat.PNG)
        .size(300)
        .build();
    
    QrCodeResult result = qrCodeService.encode(request);
    
    assertThat(result).isNotNull();
    assertThat(result.getFormat()).isEqualTo(QrCodeFormat.PNG);
    assertThat(result.getBytes()).isNotEmpty();
}
```

### Integration Tests

Integration tests verify real QR Code generation and decoding:

```java
@Test
void shouldEncodeAndDecodeSuccessfully() throws QrCodeException {
    // Encode
    QrCodeRequest request = QrCodeRequest.builder()
        .content("Integration test")
        .format(QrCodeFormat.PNG)
        .size(300)
        .build();
    
    QrCodeResult result = qrCodeService.encode(request);
    
    // Decode
    String decodedContent = qrCodeService.decode(result.getBytes());
    
    assertThat(decodedContent).isEqualTo("Integration test");
}
```

## 📊 Error Handling

The foundation provides comprehensive error handling with standardized error codes:

```java
try {
    QrCodeResult result = service.encode(request);
} catch (QrCodeException e) {
    String errorCode = e.getErrorCode();
    String message = e.getMessage();
    Map<String, Object> context = e.getContext();
    
    switch (errorCode) {
        case ErrorCodes.INVALID_CONTENT:
            // Handle invalid content
            break;
        case ErrorCodes.ENCODING_FAILED:
            // Handle encoding failure
            break;
        case ErrorCodes.UNSUPPORTED_FORMAT:
            // Handle unsupported format
            break;
        // ... other error codes
    }
}
```

### Common Error Codes

- `QR_CODE_ENCODING_FAILED`: General encoding failure
- `QR_CODE_INVALID_CONTENT`: Invalid or empty content
- `QR_CODE_CONTENT_TOO_LONG`: Content exceeds maximum length
- `QR_CODE_UNSUPPORTED_FORMAT`: Unsupported output format
- `QR_CODE_DECODING_FAILED`: General decoding failure
- `QR_CODE_NOT_FOUND`: No QR Code found in image
- `QR_CODE_CORRUPTED_IMAGE`: Corrupted or damaged QR Code

## 🔍 Observability

The foundation integrates with the base-core observability features:

- **Structured logging**: All operations are logged with context
- **Error tracking**: Comprehensive error reporting with correlation IDs
- **Performance monitoring**: Built-in metrics for encoding/decoding operations
- **Tracing support**: Integration with OpenTelemetry for distributed tracing

## 🚀 Performance Considerations

### Memory Usage

- QR Code images are generated in memory
- Large QR Codes may consume significant memory
- Consider size limits for your use case

### Performance Tips

1. **Reuse service instances**: Create the service once and reuse it
2. **Choose appropriate size**: Larger QR Codes take more time to generate
3. **Select error correction level**: Higher levels increase generation time
4. **Use appropriate format**: PNG is generally faster than JPEG for QR Codes

### Size Recommendations

| Content Type | Recommended Size | Error Correction |
|--------------|------------------|------------------|
| URLs | 300-400px | M |
| Text | 200-300px | L |
| vCard | 350-450px | H |
| WiFi | 300-400px | M |

## 🔒 Security Considerations

- **Input validation**: Always validate content before encoding
- **File handling**: Be careful when saving QR Codes to disk
- **Content sanitization**: Sanitize user input to prevent malicious content
- **Access control**: Implement proper access control for QR Code generation

## 📈 Future Enhancements

The foundation is designed to be extensible. Future enhancements may include:

- **Barcode support**: Extend to support other barcode types
- **Batch operations**: Generate multiple QR Codes efficiently
- **Template system**: Predefined templates for common use cases
- **Cloud integration**: Support for cloud-based QR Code services
- **Advanced analytics**: Usage tracking and analytics

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. **Contract-first**: Always define contracts before implementations
2. **Backward compatibility**: Maintain backward compatibility
3. **Testing**: Include comprehensive tests
4. **Documentation**: Update documentation for new features
5. **Code style**: Follow the established code style

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **ZXing**: The underlying QR Code library
- **Java ServiceLoader**: For the SPI mechanism
- **Base Core Team**: For the foundation architecture

---

For more information, see the [API Documentation](https://javadoc.io/doc/io.github.tmshoangnam/java-base-core) or contact the Base Core Team.
