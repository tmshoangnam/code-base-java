package io.github.base.security;

import io.github.base.security.api.AuthenticationManager;
import io.github.base.security.api.AuthorizationChecker;
import io.github.base.security.api.TokenService;
import io.github.base.security.spi.SecurityProvider;
import io.github.base.security.spi.SecurityProviderConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service loader helper for security providers.
 * This class provides a convenient way to load and manage security providers.
 */
public final class SecurityServices {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityServices.class);
    
    private static final Map<String, SecurityProvider> providers = new ConcurrentHashMap<>();
    private static final Map<String, SecurityProviderConfiguration> configurations = new ConcurrentHashMap<>();
    private static volatile boolean initialized = false;
    
    private SecurityServices() {
        // Utility class
    }
    
    /**
     * Initializes the security services by loading all available providers.
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }
        
        try {
            ServiceLoader<SecurityProvider> serviceLoader = ServiceLoader.load(SecurityProvider.class);
            for (SecurityProvider provider : serviceLoader) {
                try {
                    String name = provider.getName();
                    if (providers.containsKey(name)) {
                        logger.warn("Duplicate security provider found: {}", name);
                        continue;
                    }
                    
                    providers.put(name, provider);
                    logger.info("Loaded security provider: {} v{}", name, provider.getVersion());
                } catch (Exception e) {
                    logger.error("Failed to load security provider: {}", provider.getClass().getName(), e);
                }
            }
            
            initialized = true;
            logger.info("Security services initialized with {} providers", providers.size());
        } catch (Exception e) {
            logger.error("Failed to initialize security services", e);
            throw new RuntimeException("Failed to initialize security services", e);
        }
    }
    
    /**
     * Gets all loaded security providers.
     *
     * @return a map of provider names to providers
     */
    public static Map<String, SecurityProvider> getProviders() {
        ensureInitialized();
        return new HashMap<>(providers);
    }
    
    /**
     * Gets a specific security provider by name.
     *
     * @param name the provider name
     * @return an optional containing the provider if found
     */
    public static Optional<SecurityProvider> getProvider(String name) {
        ensureInitialized();
        return Optional.ofNullable(providers.get(name));
    }
    
    /**
     * Gets the default security provider.
     * The default provider is the first available provider.
     *
     * @return an optional containing the default provider if available
     */
    public static Optional<SecurityProvider> getDefaultProvider() {
        ensureInitialized();
        return providers.values().stream()
                .filter(SecurityProvider::isAvailable)
                .findFirst();
    }
    
    /**
     * Gets the authentication manager for the specified provider.
     *
     * @param providerName the provider name
     * @return the authentication manager
     * @throws SecurityException if the provider is not found or not available
     */
    public static AuthenticationManager getAuthenticationManager(String providerName) throws SecurityException {
        SecurityProvider provider = getProvider(providerName)
                .orElseThrow(() -> new SecurityException("Security provider not found: " + providerName));
        
        if (!provider.isAvailable()) {
            throw new SecurityException("Security provider is not available: " + providerName);
        }
        
        return provider.getAuthenticationManager();
    }
    
    /**
     * Gets the authorization checker for the specified provider.
     *
     * @param providerName the provider name
     * @return the authorization checker
     * @throws SecurityException if the provider is not found or not available
     */
    public static AuthorizationChecker getAuthorizationChecker(String providerName) throws SecurityException {
        SecurityProvider provider = getProvider(providerName)
                .orElseThrow(() -> new SecurityException("Security provider not found: " + providerName));
        
        if (!provider.isAvailable()) {
            throw new SecurityException("Security provider is not available: " + providerName);
        }
        
        return provider.getAuthorizationChecker();
    }
    
    /**
     * Gets the token service for the specified provider.
     *
     * @param providerName the provider name
     * @return the token service
     * @throws SecurityException if the provider is not found or not available
     */
    public static TokenService getTokenService(String providerName) throws SecurityException {
        SecurityProvider provider = getProvider(providerName)
                .orElseThrow(() -> new SecurityException("Security provider not found: " + providerName));
        
        if (!provider.isAvailable()) {
            throw new SecurityException("Security provider is not available: " + providerName);
        }
        
        return provider.getTokenService();
    }
    
    /**
     * Gets the authentication manager for the default provider.
     *
     * @return the authentication manager
     * @throws SecurityException if no default provider is available
     */
    public static AuthenticationManager getDefaultAuthenticationManager() throws SecurityException {
        SecurityProvider provider = getDefaultProvider()
                .orElseThrow(() -> new SecurityException("No default security provider available"));
        
        return provider.getAuthenticationManager();
    }
    
    /**
     * Gets the authorization checker for the default provider.
     *
     * @return the authorization checker
     * @throws SecurityException if no default provider is available
     */
    public static AuthorizationChecker getDefaultAuthorizationChecker() throws SecurityException {
        SecurityProvider provider = getDefaultProvider()
                .orElseThrow(() -> new SecurityException("No default security provider available"));
        
        return provider.getAuthorizationChecker();
    }
    
    /**
     * Gets the token service for the default provider.
     *
     * @return the token service
     * @throws SecurityException if no default provider is available
     */
    public static TokenService getDefaultTokenService() throws SecurityException {
        SecurityProvider provider = getDefaultProvider()
                .orElseThrow(() -> new SecurityException("No default security provider available"));
        
        return provider.getTokenService();
    }
    
    /**
     * Configures a security provider with the given configuration.
     *
     * @param providerName the provider name
     * @param configuration the provider configuration
     * @throws SecurityException if the provider is not found or configuration fails
     */
    public static void configureProvider(String providerName, SecurityProviderConfiguration configuration) throws SecurityException {
        SecurityProvider provider = getProvider(providerName)
                .orElseThrow(() -> new SecurityException("Security provider not found: " + providerName));
        
        try {
            provider.initialize(configuration);
            configurations.put(providerName, configuration);
            logger.info("Configured security provider: {}", providerName);
        } catch (Exception e) {
            logger.error("Failed to configure security provider: {}", providerName, e);
            throw new SecurityException("Failed to configure security provider: " + providerName, e);
        }
    }
    
    /**
     * Gets the configuration for a security provider.
     *
     * @param providerName the provider name
     * @return an optional containing the configuration if found
     */
    public static Optional<SecurityProviderConfiguration> getProviderConfiguration(String providerName) {
        return Optional.ofNullable(configurations.get(providerName));
    }
    
    /**
     * Shuts down all security providers.
     */
    public static synchronized void shutdown() {
        for (SecurityProvider provider : providers.values()) {
            try {
                provider.shutdown();
            } catch (Exception e) {
                logger.error("Failed to shutdown security provider: {}", provider.getName(), e);
            }
        }
        
        providers.clear();
        configurations.clear();
        initialized = false;
        logger.info("Security services shutdown");
    }
    
    /**
     * Gets the names of all available security providers.
     *
     * @return a set of provider names
     */
    public static Set<String> getAvailableProviderNames() {
        ensureInitialized();
        return providers.values().stream()
                .filter(SecurityProvider::isAvailable)
                .map(SecurityProvider::getName)
                .collect(Collectors.toSet());
    }
    
    private static void ensureInitialized() {
        if (!initialized) {
            initialize();
        }
    }
}
