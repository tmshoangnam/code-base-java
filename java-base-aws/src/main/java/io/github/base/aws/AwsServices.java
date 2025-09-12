package io.github.base.aws;

import io.github.base.aws.api.config.AwsServiceConfig;
import io.github.base.aws.spi.AwsServiceProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Service loader helper for discovering and managing AWS service providers.
 *
 * <p>This class provides a convenient way to discover and load AWS service
 * providers using the Java ServiceLoader mechanism. It maintains a cache of
 * loaded providers and provides methods to access them by name or priority.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public final class AwsServices {

    private static final ConcurrentMap<String, AwsServiceProvider> PROVIDER_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Object> SERVICE_CACHE = new ConcurrentHashMap<>();
    private static List<AwsServiceProvider> PROVIDERS;

    private AwsServices() {
        // Utility class - prevent instantiation
    }

    /**
     * Gets all available AWS service providers.
     *
     * @return list of available providers sorted by priority (highest first)
     */
    public static List<AwsServiceProvider> getProviders() {
        if (PROVIDERS == null) {
            synchronized (AwsServices.class) {
                if (PROVIDERS == null) {
                    PROVIDERS = loadProviders();
                }
            }
        }
        return new ArrayList<>(PROVIDERS);
    }

    /**
     * Gets an AWS service provider by name.
     *
     * @param serviceName the name of the service
     * @return the provider or null if not found
     * @throws IllegalArgumentException if serviceName is null or empty
     */
    public static AwsServiceProvider getProvider(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }

        return PROVIDER_CACHE.computeIfAbsent(serviceName.trim(), name -> {
            for (AwsServiceProvider provider : getProviders()) {
                if (name.equals(provider.getServiceName())) {
                    return provider;
                }
            }
            return null;
        });
    }

    /**
     * Gets an AWS service instance by service name.
     *
     * @param serviceName the name of the service
     * @param config the service configuration
     * @return the service instance or null if provider not found
     * @throws IllegalArgumentException if serviceName is null or empty, or config is null
     * @throws IllegalStateException if provider is not available
     */
    public static Object getService(String serviceName, AwsServiceConfig config) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }

        String key = serviceName.trim() + ":" + config.hashCode();
        return SERVICE_CACHE.computeIfAbsent(key, k -> {
            AwsServiceProvider provider = getProvider(serviceName.trim());
            if (provider == null) {
                return null;
            }

            if (!provider.isAvailable()) {
                throw new IllegalStateException("Provider is not available: " + serviceName);
            }

            return provider.createService(config);
        });
    }

    /**
     * Gets the highest priority available provider for a service.
     *
     * @param serviceName the name of the service
     * @return the highest priority provider or null if none available
     */
    public static AwsServiceProvider getBestProvider(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            return null;
        }

        for (AwsServiceProvider provider : getProviders()) {
            if (serviceName.trim().equals(provider.getServiceName()) && provider.isAvailable()) {
                return provider;
            }
        }
        return null;
    }

    /**
     * Gets the highest priority available AWS service instance.
     *
     * @param serviceName the name of the service
     * @param config the service configuration
     * @return the highest priority service instance or null if none available
     * @throws IllegalStateException if no providers are available
     */
    public static Object getBestService(String serviceName, AwsServiceConfig config) {
        AwsServiceProvider provider = getBestProvider(serviceName);
        if (provider == null) {
            throw new IllegalStateException("No available AWS service providers found for: " + serviceName);
        }

        return getService(serviceName, config);
    }

    /**
     * Gets all available service names.
     *
     * @return list of available service names
     */
    public static List<String> getAvailableServiceNames() {
        List<String> names = new ArrayList<>();
        for (AwsServiceProvider provider : getProviders()) {
            if (provider.isAvailable()) {
                names.add(provider.getServiceName());
            }
        }
        return names;
    }

    /**
     * Checks if a service provider is available.
     *
     * @param serviceName the name of the service
     * @return true if provider is available, false otherwise
     */
    public static boolean isProviderAvailable(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            return false;
        }

        AwsServiceProvider provider = getProvider(serviceName.trim());
        return provider != null && provider.isAvailable();
    }

    /**
     * Gets the capabilities of a service provider.
     *
     * @param serviceName the name of the service
     * @return map of capability flags or empty map if provider not found
     */
    public static java.util.Map<String, Boolean> getProviderCapabilities(String serviceName) {
        AwsServiceProvider provider = getProvider(serviceName);
        return provider != null ? provider.getCapabilities() : java.util.Map.of();
    }

    /**
     * Gets the required configuration for a service provider.
     *
     * @param serviceName the name of the service
     * @return map of required configuration keys or empty map if provider not found
     */
    public static java.util.Map<String, String> getRequiredConfiguration(String serviceName) {
        AwsServiceProvider provider = getProvider(serviceName);
        return provider != null ? provider.getRequiredConfiguration() : java.util.Map.of();
    }

    /**
     * Gets the optional configuration for a service provider.
     *
     * @param serviceName the name of the service
     * @return map of optional configuration keys or empty map if provider not found
     */
    public static java.util.Map<String, String> getOptionalConfiguration(String serviceName) {
        AwsServiceProvider provider = getProvider(serviceName);
        return provider != null ? provider.getOptionalConfiguration() : java.util.Map.of();
    }

    /**
     * Validates the configuration for a service provider.
     *
     * @param serviceName the name of the service
     * @param config the configuration to validate
     * @return list of validation errors (empty if valid)
     */
    public static List<String> validateConfiguration(String serviceName, AwsServiceConfig config) {
        AwsServiceProvider provider = getProvider(serviceName);
        if (provider == null) {
            return List.of("Provider not found: " + serviceName);
        }

        return provider.validateConfiguration(config);
    }

    /**
     * Clears the provider and service caches.
     * This method is primarily useful for testing or when providers are reloaded.
     */
    public static void clearCache() {
        PROVIDER_CACHE.clear();
        SERVICE_CACHE.clear();
        PROVIDERS = null;
    }

    /**
     * Reloads all providers from the classpath.
     * This method clears the cache and reloads all providers.
     */
    public static void reload() {
        clearCache();
        getProviders(); // This will reload the providers
    }

    /**
     * Gets the number of available providers.
     *
     * @return number of available providers
     */
    public static int getProviderCount() {
        return getAvailableServiceNames().size();
    }

    /**
     * Gets the number of available providers for a specific service.
     *
     * @param serviceName the name of the service
     * @return number of available providers for the service
     */
    public static int getProviderCount(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            return 0;
        }

        int count = 0;
        for (AwsServiceProvider provider : getProviders()) {
            if (serviceName.trim().equals(provider.getServiceName()) && provider.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Loads all AWS service providers from the classpath.
     *
     * @return list of providers sorted by priority
     */
    private static List<AwsServiceProvider> loadProviders() {
        List<AwsServiceProvider> providers = new ArrayList<>();

        ServiceLoader<AwsServiceProvider> serviceLoader = ServiceLoader.load(AwsServiceProvider.class);
        for (AwsServiceProvider provider : serviceLoader) {
            providers.add(provider);
        }

        // Sort by priority (highest first)
        providers.sort(Comparator.comparingInt(AwsServiceProvider::getPriority).reversed());

        return providers;
    }
}
