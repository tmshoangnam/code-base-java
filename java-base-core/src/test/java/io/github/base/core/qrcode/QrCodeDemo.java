package io.github.base.core.qrcode;

import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.api.QrCodeFormat;
import io.github.base.core.qrcode.api.QrCodeException;
import io.github.base.core.qrcode.model.QrCodeRequest;
import io.github.base.core.qrcode.model.QrCodeResult;
import io.github.base.core.qrcode.model.ErrorCorrectionLevel;
import io.github.base.core.qrcode.model.QrCodeContent;
import io.github.base.core.qrcode.model.QrCodeOptions;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Demo class showcasing QR Code functionality.
 * 
 * <p>This class demonstrates various use cases of the QR Code foundation,
 * including different content types, formats, and options.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public class QrCodeDemo {
    
    public static void main(String[] args) {
        try {
            QrCodeDemo demo = new QrCodeDemo();
            demo.runDemo();
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void runDemo() throws QrCodeException, IOException {
        System.out.println("=== QR Code Foundation Demo ===\n");
        
        // Load default QR Code service
        QrCodeService service = QrCodeServices.loadDefault();
        System.out.println("Loaded QR Code service: " + service.getServiceName());
        
        // Demo 1: Simple text QR Code
        demoSimpleText(service);
        
        // Demo 2: URL QR Code
        demoUrl(service);
        
        // Demo 3: vCard QR Code
        demoVCard(service);
        
        // Demo 4: WiFi QR Code
        demoWiFi(service);
        
        // Demo 5: Custom options
        demoCustomOptions(service);
        
        // Demo 6: Different formats
        demoDifferentFormats(service);
        
        // Demo 7: Error correction levels
        demoErrorCorrectionLevels(service);
        
        // Demo 8: Decode QR Code
        demoDecode(service);
        
        System.out.println("\n=== Demo completed successfully! ===");
    }
    
    private void demoSimpleText(QrCodeService service) throws QrCodeException, IOException {
        System.out.println("1. Simple Text QR Code");
        
        QrCodeRequest request = QrCodeRequest.builder()
            .content("Hello, QR Code World!")
            .format(QrCodeFormat.PNG)
            .size(300)
            .build();
        
        QrCodeResult result = service.encode(request);
        saveQrCode(result, "demo-simple-text.png");
        
        System.out.println("   Generated: " + result.getWidth() + "x" + result.getHeight() + " PNG");
        System.out.println("   Size: " + result.getSizeInBytes() + " bytes");
        System.out.println();
    }
    
    private void demoUrl(QrCodeService service) throws QrCodeException, IOException {
        System.out.println("2. URL QR Code");
        
        QrCodeContent urlContent = QrCodeContent.url("https://github.com/tmshoangnam/code-base-java");
        
        QrCodeRequest request = QrCodeRequest.builder()
            .content(urlContent.getContent())
            .format(QrCodeFormat.PNG)
            .size(400)
            .margin(8)
            .errorCorrectionLevel(ErrorCorrectionLevel.M)
            .build();
        
        QrCodeResult result = service.encode(request);
        saveQrCode(result, "demo-url.png");
        
        System.out.println("   Generated: " + result.getWidth() + "x" + result.getHeight() + " PNG");
        System.out.println("   Content: " + urlContent.getDescription());
        System.out.println();
    }
    
    private void demoVCard(QrCodeService service) throws QrCodeException, IOException {
        System.out.println("3. vCard QR Code");
        
        QrCodeContent vCardContent = QrCodeContent.vCard(
            "John Doe",
            "john.doe@example.com",
            "+1-555-0123"
        );
        
        QrCodeRequest request = QrCodeRequest.builder()
            .content(vCardContent.getContent())
            .format(QrCodeFormat.PNG)
            .size(350)
            .errorCorrectionLevel(ErrorCorrectionLevel.H)
            .build();
        
        QrCodeResult result = service.encode(request);
        saveQrCode(result, "demo-vcard.png");
        
        System.out.println("   Generated: " + result.getWidth() + "x" + result.getHeight() + " PNG");
        System.out.println("   Content: " + vCardContent.getDescription());
        System.out.println();
    }
    
    private void demoWiFi(QrCodeService service) throws QrCodeException, IOException {
        System.out.println("4. WiFi QR Code");
        
        QrCodeContent wifiContent = QrCodeContent.wifi(
            "MyHomeNetwork",
            "WPA",
            "securepassword123"
        );
        
        QrCodeRequest request = QrCodeRequest.builder()
            .content(wifiContent.getContent())
            .format(QrCodeFormat.PNG)
            .size(300)
            .build();
        
        QrCodeResult result = service.encode(request);
        saveQrCode(result, "demo-wifi.png");
        
        System.out.println("   Generated: " + result.getWidth() + "x" + result.getHeight() + " PNG");
        System.out.println("   Content: " + wifiContent.getDescription());
        System.out.println();
    }
    
    private void demoCustomOptions(QrCodeService service) throws QrCodeException, IOException {
        System.out.println("5. Custom Options QR Code");
        
        QrCodeOptions options = QrCodeOptions.builder()
            .foregroundColor(Color.BLUE)
            .backgroundColor(Color.YELLOW)
            .customData("Custom QR Code with special colors")
            .build();
        
        QrCodeRequest request = QrCodeRequest.builder()
            .content("Custom colored QR Code")
            .format(QrCodeFormat.PNG)
            .size(300)
            .options(options)
            .build();
        
        QrCodeResult result = service.encode(request);
        saveQrCode(result, "demo-custom-options.png");
        
        System.out.println("   Generated: " + result.getWidth() + "x" + result.getHeight() + " PNG");
        System.out.println("   Custom data: " + options.getCustomData());
        System.out.println();
    }
    
    private void demoDifferentFormats(QrCodeService service) throws QrCodeException, IOException {
        System.out.println("6. Different Formats");
        
        String content = "Multi-format QR Code";
        
        // PNG format
        QrCodeRequest pngRequest = QrCodeRequest.builder()
            .content(content)
            .format(QrCodeFormat.PNG)
            .size(200)
            .build();
        
        QrCodeResult pngResult = service.encode(pngRequest);
        saveQrCode(pngResult, "demo-formats.png");
        
        // JPEG format
        QrCodeRequest jpegRequest = QrCodeRequest.builder()
            .content(content)
            .format(QrCodeFormat.JPEG)
            .size(200)
            .build();
        
        QrCodeResult jpegResult = service.encode(jpegRequest);
        saveQrCode(jpegResult, "demo-formats.jpg");
        
        System.out.println("   Generated PNG: " + pngResult.getSizeInBytes() + " bytes");
        System.out.println("   Generated JPEG: " + jpegResult.getSizeInBytes() + " bytes");
        System.out.println();
    }
    
    private void demoErrorCorrectionLevels(QrCodeService service) throws QrCodeException, IOException {
        System.out.println("7. Error Correction Levels");
        
        String content = "Error correction test";
        
        for (ErrorCorrectionLevel level : ErrorCorrectionLevel.values()) {
            QrCodeRequest request = QrCodeRequest.builder()
                .content(content)
                .format(QrCodeFormat.PNG)
                .size(200)
                .errorCorrectionLevel(level)
                .build();
            
            QrCodeResult result = service.encode(request);
            String filename = "demo-error-correction-" + level.name().toLowerCase() + ".png";
            saveQrCode(result, filename);
            
            System.out.println("   " + level.name() + " (" + level.getErrorCorrectionPercentage() + "%): " + 
                             result.getSizeInBytes() + " bytes");
        }
        System.out.println();
    }
    
    private void demoDecode(QrCodeService service) throws QrCodeException, IOException {
        System.out.println("8. Decode QR Code");
        
        // First, create a QR Code
        QrCodeRequest request = QrCodeRequest.builder()
            .content("This QR Code will be decoded!")
            .format(QrCodeFormat.PNG)
            .size(300)
            .build();
        
        QrCodeResult result = service.encode(request);
        saveQrCode(result, "demo-decode.png");
        
        // Now decode it
        String decodedContent = service.decode(result.getBytes());
        
        System.out.println("   Original content: This QR Code will be decoded!");
        System.out.println("   Decoded content: " + decodedContent);
        System.out.println("   Match: " + decodedContent.equals("This QR Code will be decoded!"));
        System.out.println();
    }
    
    private void saveQrCode(QrCodeResult result, String filename) throws IOException {
        Path outputDir = Paths.get("target", "qrcode-demo");
        Files.createDirectories(outputDir);
        
        Path filePath = outputDir.resolve(filename);
        Files.write(filePath, result.getBytes());
        
        System.out.println("   Saved: " + filePath.toAbsolutePath());
    }
}
