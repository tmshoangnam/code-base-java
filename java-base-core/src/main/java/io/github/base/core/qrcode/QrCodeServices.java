package io.github.base.core.qrcode;

import io.github.base.core.qrcode.api.QrCodeService;
import io.github.base.core.qrcode.api.ErrorCodes;
import io.github.base.core.qrcode.api.QrCodeException;
import io.github.base.core.qrcode.spi.QrCodeProvider;
import io.github.base.core.qrcode.spi.QrCodeProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ServiceLoader helper for QR Code services.
 * 
 * <p>This class provides a convenient way to load and manage QR Code service providers
 * through the Java ServiceLoader mechanism. It supports provider discovery, caching,
 * and fallback mechanisms.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * // Load default provider
 * QrCodeService service = QrCodeServices.loadDefault();
 * 
 * // Load specific provider
 * QrCodeService service = QrCodeServices.loadProvider("zxing");
 * 
 * // List available providers
 * List<String> providers = QrCodeServices.getAvailableProviders();
 * }</pre>
 * 
 * @author Base Core Team
 * @since 1.0.0
 */
public final class QrCodeServices {
    
    private static final Logger logger = LoggerFactory.getLogger(QrCodeServices.class);
    
    private static final Map<String, QrCodeService> serviceCache = new ConcurrentHashMap<>();
    private static final Map<String, QrCodeProvider> providerCache = new ConcurrentHashMap<>();
    private static final List<QrCodeProvider> availableProviders = new ArrayList<>();
    
    static {
        loadProviders();
    }
    
    // Private constructor to prevent instantiation
    private QrCodeServices() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Loads the default QR Code service.
     * 
     * <p>The default service is determined by the highest priority provider
     * that is available. If no providers are available, an exception is thrown.</p>
     * 
     * @return the default QR Code service
     * @throws QrCodeException if no providers are available or service creation fails
     */
    public static QrCodeService loadDefault() throws QrCodeException {
        QrCodeProvider defaultProvider = getDefaultProvider();
        return loadService(defaultProvider);
    }
    
    /**
     * Loads a QR Code service by provider name.
     * 
     * @param providerName the name of the provider
     * @return the QR Code service
     * @throws QrCodeException if the provider is not found or service creation fails
     */
    public static QrCodeService loadProvider(String providerName) throws QrCodeException {
        if (providerName == null || providerName.trim().isEmpty()) {
            throw new QrCodeException(ErrorCodes.PROVIDER_NOT_FOUND, "Provider name cannot be null or empty");
        }
        
        QrCodeProvider provider = providerCache.get(providerName);
        if (provider == null) {
            throw new QrCodeException(ErrorCodes.PROVIDER_NOT_FOUND, "Provider not found: " + providerName);
        }
        
        return loadService(provider);
    }
    
    /**
     * Gets the list of available provider names.
     * 
     * @return the list of available provider names
     */
    public static List<String> getAvailableProviders() {
        return availableProviders.stream()
                .map(QrCodeProvider::getName)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the list of available providers with their details.
     * 
     * @return the list of available providers
     */
    public static List<QrCodeProvider> getAvailableProvidersDetails() {
        return new ArrayList<>(availableProviders);
    }
    
    /**
     * Clears the service cache.
     * 
     * <p>This method clears the internal service cache, forcing new service instances
     * to be created on the next load request.</p>
     */
    public static void clearCache() {
        serviceCache.clear();
        logger.info("QR Code service cache cleared");
    }
    
    /**
     * Reloads all providers.
     * 
     * <p>This method reloads all providers from the ServiceLoader and clears the cache.</p>
     */
    public static void reload() {
        availableProviders.clear();
        providerCache.clear();
        serviceCache.clear();
        loadProviders();
        logger.info("QR Code providers reloaded");
    }
    
    private static void loadProviders() {
        try {
            ServiceLoader<QrCodeProvider> serviceLoader = ServiceLoader.load(QrCodeProvider.class);
            
            for (QrCodeProvider provider : serviceLoader) {
                try {
                    if (provider.isAvailable()) {
                        availableProviders.add(provider);
                        providerCache.put(provider.getName(), provider);
                        logger.info("Loaded QR Code provider: {} v{}", provider.getName(), provider.getVersion());
                    } else {
                        logger.warn("QR Code provider {} is not available", provider.getName());
                    }
                } catch (Exception e) {
                    logger.error("Failed to load QR Code provider: {}", provider.getName(), e);
                }
            }
            
            // Sort providers by priority (highest first)
            availableProviders.sort((p1, p2) -> Integer.compare(p2.getPriority(), p1.getPriority()));
            
            if (availableProviders.isEmpty()) {
                logger.warn("No QR Code providers found");
            } else {
                logger.info("Loaded {} QR Code providers", availableProviders.size());
            }
            
        } catch (Exception e) {
            logger.error("Failed to load QR Code providers", e);
            throw new QrCodeException(ErrorCodes.PROVIDER_INITIALIZATION_FAILED, "Failed to load QR Code providers", e);
        }
    }
    
    private static QrCodeProvider getDefaultProvider() throws QrCodeException {
        if (availableProviders.isEmpty()) {
            String errorMessage = "No QR Code providers available. " +
                    "Please add ZXing dependencies or provide a custom QR Code provider implementation. " +
                    "Add these dependencies to your pom.xml:\n" +
                    "<dependency>\n" +
                    "    <groupId>com.google.zxing</groupId>\n" +
                    "    <artifactId>core</artifactId>\n" +
                    "    <version>3.5.3</version>\n" +
                    "</dependency>\n" +
                    "<dependency>\n" +
                    "    <groupId>com.google.zxing</groupId>\n" +
                    "    <artifactId>javase</artifactId>\n" +
                    "    <version>3.5.3</version>\n" +
                    "</dependency>";
            throw new QrCodeException(ErrorCodes.PROVIDER_NOT_FOUND, errorMessage);
        }
        
        QrCodeProvider defaultProvider = availableProviders.get(0);
        logger.debug("Using default QR Code provider: {}", defaultProvider.getName());
        return defaultProvider;
    }
    
    private static QrCodeService loadService(QrCodeProvider provider) throws QrCodeException {
        String providerName = provider.getName();
        
        // Check cache first
        QrCodeService cachedService = serviceCache.get(providerName);
        if (cachedService != null) {
            return cachedService;
        }
        
        try {
            QrCodeService service = provider.createService();
            serviceCache.put(providerName, service);
            logger.debug("Created QR Code service for provider: {}", providerName);
            return service;
        } catch (QrCodeProviderException e) {
            logger.error("Failed to create QR Code service for provider: {}", providerName, e);
            throw new QrCodeException(ErrorCodes.PROVIDER_INITIALIZATION_FAILED, 
                    "Failed to create QR Code service for provider: " + providerName, e);
        } catch (Exception e) {
            logger.error("Unexpected error creating QR Code service for provider: {}", providerName, e);
            throw new QrCodeException(ErrorCodes.PROVIDER_INITIALIZATION_FAILED, 
                    "Unexpected error creating QR Code service for provider: " + providerName, e);
        }
    }
}
