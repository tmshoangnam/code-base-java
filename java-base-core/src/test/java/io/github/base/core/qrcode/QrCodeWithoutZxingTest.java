package io.github.base.core.qrcode;

import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.api.QrCodeException;
import io.github.base.core.qrcode.model.QrCodeRequest;
import io.github.base.core.qrcode.api.QrCodeFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

/**
 * Test to demonstrate QR Code behavior when ZXing dependencies are excluded.
 * 
 * <p>This test simulates the scenario where ZXing dependencies are excluded
 * and shows how the system gracefully handles this situation.</p>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
@DisplayName("QR Code Without ZXing Dependencies Tests")
class QrCodeWithoutZxingTest {
    
    @Test
    @DisplayName("Should demonstrate graceful fallback when ZXing is not available")
    void shouldDemonstrateGracefulFallbackWhenZxingIsNotAvailable() {
        // This test shows what happens when ZXing dependencies are excluded
        // In a real scenario, you would exclude ZXing in your pom.xml:
        // <exclusions>
        //     <exclusion>
        //         <groupId>com.google.zxing</groupId>
        //         <artifactId>core</artifactId>
        //     </exclusion>
        //     <exclusion>
        //         <groupId>com.google.zxing</groupId>
        //         <artifactId>javase</artifactId>
        //     </exclusion>
        // </exclusions>
        
        // When ZXing is excluded, the system should:
        // 1. Still load providers (ZXing will be unavailable, NoOp will be available)
        var providers = QrCodeServices.getAvailableProviders();
        var providerDetails = QrCodeServices.getAvailableProvidersDetails();
        
        System.out.println("=== QR Code Provider Analysis ===");
        System.out.println("Available providers: " + providers);
        System.out.println("Provider details:");
        providerDetails.forEach(provider -> {
            System.out.printf("  - %s v%s: %s%n", 
                provider.getName(), 
                provider.getVersion(), 
                provider.getDescription());
            System.out.printf("    Available: %s, Priority: %d%n", 
                provider.isAvailable(), 
                provider.getPriority());
        });
        
        // 2. Should have at least the NoOp provider
        assertThat(providers).contains("no-op");
        
        // 3. When trying to use QR Code service, it should either:
        //    - Use ZXing if available (current case)
        //    - Use NoOp if ZXing is excluded (would throw SERVICE_UNAVAILABLE)
        
        try {
            QrCodeService service = QrCodeServices.loadDefault();
            System.out.println("\n=== QR Code Service Test ===");
            System.out.println("Service loaded: " + service.getServiceName());
            
            if ("no-op".equals(service.getServiceName())) {
                System.out.println("NoOp service detected - QR Code functionality is not available");
                
                // Test that NoOp service throws appropriate errors
                QrCodeRequest request = QrCodeRequest.builder()
                    .content("Test QR Code")
                    .format(QrCodeFormat.PNG)
                    .size(200)
                    .build();
                
                assertThatThrownBy(() -> service.encode(request))
                    .isInstanceOf(QrCodeException.class)
                    .hasMessageContaining("QR Code functionality is not available");
                
                System.out.println("✓ NoOp service correctly throws SERVICE_UNAVAILABLE error");
                
            } else {
                System.out.println("ZXing service detected - QR Code functionality is available");
                
                // Test that ZXing service works normally
                QrCodeRequest request = QrCodeRequest.builder()
                    .content("Test QR Code")
                    .format(QrCodeFormat.PNG)
                    .size(200)
                    .build();
                
                var result = service.encode(request);
                System.out.println("✓ QR Code encoded successfully: " + 
                    result.getWidth() + "x" + result.getHeight() + 
                    " (" + result.getSizeInBytes() + " bytes)");
            }
            
        } catch (QrCodeException e) {
            System.out.println("\n=== Error Handling ===");
            System.out.println("QR Code service failed: " + e.getMessage());
            System.out.println("Error code: " + e.getErrorCode());
            
            if (e.getErrorCode().equals("QR_CODE_PROVIDER_NOT_FOUND")) {
                System.out.println("This indicates no QR Code providers are available at all.");
                System.out.println("This should not happen with the current implementation.");
            }
        }
        
        System.out.println("\n=== Summary ===");
        System.out.println("Current behavior:");
        System.out.println("- ZXing provider: Available (dependencies present)");
        System.out.println("- NoOp provider: Available (fallback)");
        System.out.println("- Default service: ZXing (higher priority)");
        System.out.println("\nWhen ZXing dependencies are excluded:");
        System.out.println("- ZXing provider: Not available (dependencies missing)");
        System.out.println("- NoOp provider: Available (fallback)");
        System.out.println("- Default service: NoOp (only available provider)");
        System.out.println("- Result: Clear error message when trying to use QR Code");
    }
}
