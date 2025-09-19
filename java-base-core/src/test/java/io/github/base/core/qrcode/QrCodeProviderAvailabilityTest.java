package io.github.base.core.qrcode;

import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.api.QrCodeException;
import io.github.base.core.qrcode.model.QrCodeRequest;
import io.github.base.core.qrcode.api.QrCodeFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

/**
 * Test to demonstrate QR Code provider availability behavior.
 * 
 * <p>This test shows how the QR Code system behaves when ZXing dependencies
 * are available vs when they are excluded.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
@DisplayName("QR Code Provider Availability Tests")
class QrCodeProviderAvailabilityTest {
    
    @Test
    @DisplayName("Should show available providers")
    void shouldShowAvailableProviders() {
        // When
        var providers = QrCodeServices.getAvailableProviders();
        var providerDetails = QrCodeServices.getAvailableProvidersDetails();
        
        // Then
        System.out.println("Available providers: " + providers);
        System.out.println("Provider details:");
        providerDetails.forEach(provider -> {
            System.out.printf("  - %s v%s: %s (available: %s, priority: %d)%n",
                provider.getName(),
                provider.getVersion(),
                provider.getDescription(),
                provider.isAvailable(),
                provider.getPriority()
            );
        });
        
        // Should have at least one provider (either ZXing or NoOp)
        assertThat(providers).isNotEmpty();
    }
    
    @Test
    @DisplayName("Should demonstrate QR Code service behavior")
    void shouldDemonstrateQrCodeServiceBehavior() {
        try {
            // When
            QrCodeService service = QrCodeServices.loadDefault();
            
            // Then
            System.out.println("QR Code service loaded: " + service.getServiceName());
            System.out.println("Supports PNG: " + service.supportsFormat(QrCodeFormat.PNG));
            System.out.println("Supports JPEG: " + service.supportsFormat(QrCodeFormat.JPEG));
            
            // Try to encode a QR Code
            QrCodeRequest request = QrCodeRequest.builder()
                .content("Hello QR Code!")
                .format(QrCodeFormat.PNG)
                .size(200)
                .build();
            
            var result = service.encode(request);
            System.out.println("QR Code encoded successfully!");
            System.out.println("Result format: " + result.getFormat());
            System.out.println("Result dimensions: " + result.getWidth() + "x" + result.getHeight());
            System.out.println("Result size: " + result.getSizeInBytes() + " bytes");
            
        } catch (QrCodeException e) {
            System.out.println("QR Code service failed: " + e.getMessage());
            System.out.println("Error code: " + e.getErrorCode());
            
            // This is expected when ZXing dependencies are excluded
            if (e.getErrorCode().equals("QR_CODE_SERVICE_UNAVAILABLE")) {
                System.out.println("This is expected when ZXing dependencies are excluded.");
                System.out.println("To fix this, add ZXing dependencies to your pom.xml:");
                System.out.println("<dependency>");
                System.out.println("    <groupId>com.google.zxing</groupId>");
                System.out.println("    <artifactId>core</artifactId>");
                System.out.println("    <version>3.5.3</version>");
                System.out.println("</dependency>");
                System.out.println("<dependency>");
                System.out.println("    <groupId>com.google.zxing</groupId>");
                System.out.println("    <artifactId>javase</artifactId>");
                System.out.println("    <version>3.5.3</version>");
                System.out.println("</dependency>");
            }
        }
    }
}
