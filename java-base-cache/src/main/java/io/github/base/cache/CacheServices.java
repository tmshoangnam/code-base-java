package io.github.base.cache;

import io.github.base.cache.spi.CacheProvider;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * ServiceLoader helper for runtime discovery of cache providers.
 * <p>
 * This utility class provides convenient methods for discovering and working
 * with cache providers at runtime using the Java ServiceLoader mechanism.
 * <p>
 * <strong>Usage:</strong> This class is primarily used by the starter module
 * for provider discovery and fallback mechanisms when Spring Boot auto-configuration
 * is not available.
 *
 * @since 1.0.0
 */
public final class CacheServices {

    private CacheServices() {
        // Utility class
    }

    /**
     * Discovers all available cache providers using ServiceLoader.
     * <p>
     * This method scans the classpath for implementations of {@link CacheProvider}
     * that are registered in {@code META-INF/services}.
     *
     * @return a list of all discovered cache providers
     */
    public static List<CacheProvider> discoverProviders() {
        ServiceLoader<CacheProvider> serviceLoader = ServiceLoader.load(CacheProvider.class);
        return StreamSupport.stream(serviceLoader.spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Finds a cache provider by name.
     * <p>
     * This method searches through all discovered providers and returns the
     * first one that matches the specified name (case-sensitive).
     *
     * @param name the name of the provider to find
     * @return the provider if found, or null if no provider with the given name exists
     */
    public static CacheProvider findProvider(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        return discoverProviders().stream()
                .filter(provider -> name.equals(provider.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the first available cache provider.
     * <p>
     * This method returns the first provider discovered by ServiceLoader.
     * The order is not guaranteed and may vary between different classpath
     * configurations.
     *
     * @return the first available provider, or null if no providers are found
     */
    public static CacheProvider getDefaultProvider() {
        List<CacheProvider> providers = discoverProviders();
        return providers.isEmpty() ? null : providers.get(0);
    }
}
