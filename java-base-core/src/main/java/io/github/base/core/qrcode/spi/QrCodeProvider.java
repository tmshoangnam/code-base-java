package io.github.base.core.qrcode.spi;

import io.github.base.core.qrcode.api.QrCodeService;

/**
 * Service Provider Interface for QR Code implementations.
 * 
 * <p>This interface allows different QR Code libraries to be plugged into the system
 * through the Java ServiceLoader mechanism. Implementations should be registered
 * in META-INF/services/io.github.base.core.qrcode.spi.QrCodeProvider.</p>
 * 
 * <p>Example implementation registration:</p>
 * <pre>{@code
 * // In META-INF/services/io.github.base.core.qrcode.spi.QrCodeProvider
 * com.example.qrcode.ZXingQrCodeProvider
 * }</pre>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public interface QrCodeProvider {
    
    /**
     * Gets the name of this QR Code provider.
     * 
     * <p>The name should be unique and descriptive, e.g., "zxing", "qrgen", "custom".</p>
     * 
     * @return the provider name
     */
    String getName();
    
    /**
     * Gets the version of this QR Code provider.
     * 
     * @return the provider version
     */
    String getVersion();
    
    /**
     * Gets the description of this QR Code provider.
     * 
     * @return the provider description
     */
    String getDescription();
    
    /**
     * Creates a new QR Code service instance.
     * 
     * <p>This method should return a new instance of the QR Code service.
     * The implementation should be thread-safe and stateless if possible.</p>
     * 
     * @return a new QR Code service instance
     * @throws QrCodeProviderException if the service cannot be created
     */
    QrCodeService createService() throws QrCodeProviderException;
    
    /**
     * Checks if this provider is available and can be used.
     * 
     * <p>This method should check if all required dependencies are available
     * and the provider can function properly.</p>
     * 
     * @return true if the provider is available, false otherwise
     */
    boolean isAvailable();
    
    /**
     * Gets the priority of this provider.
     * 
     * <p>Higher priority providers will be preferred when multiple providers
     * are available. The default priority is 0.</p>
     * 
     * @return the provider priority
     */
    default int getPriority() {
        return 0;
    }
}
