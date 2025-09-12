package io.github.base.file;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.spi.FileStorageProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Service loader helper for discovering and managing file storage providers.
 * 
 * <p>This class provides a convenient way to discover and load file storage
 * providers using the Java ServiceLoader mechanism. It maintains a cache of
 * loaded providers and provides methods to access them by name or priority.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public final class FileServices {
    
    private static final ConcurrentMap<String, FileStorageProvider> PROVIDER_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, FileStorage> STORAGE_CACHE = new ConcurrentHashMap<>();
    private static volatile List<FileStorageProvider> PROVIDERS;
    
    private FileServices() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Gets all available file storage providers.
     * 
     * @return list of available providers sorted by priority (highest first)
     */
    public static List<FileStorageProvider> getProviders() {
        if (PROVIDERS == null) {
            synchronized (FileServices.class) {
                if (PROVIDERS == null) {
                    PROVIDERS = loadProviders();
                }
            }
        }
        return new ArrayList<>(PROVIDERS);
    }
    
    /**
     * Gets a file storage provider by name.
     * 
     * @param providerName the name of the provider
     * @return the provider or null if not found
     * @throws IllegalArgumentException if providerName is null or empty
     */
    public static FileStorageProvider getProvider(String providerName) {
        if (providerName == null || providerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Provider name cannot be null or empty");
        }
        
        return PROVIDER_CACHE.computeIfAbsent(providerName.trim(), name -> {
            for (FileStorageProvider provider : getProviders()) {
                if (name.equals(provider.getProviderName())) {
                    return provider;
                }
            }
            return null;
        });
    }
    
    /**
     * Gets a file storage instance by provider name.
     * 
     * @param providerName the name of the provider
     * @return the file storage instance or null if provider not found
     * @throws IllegalArgumentException if providerName is null or empty
     * @throws IllegalStateException if provider is not available
     */
    public static FileStorage getStorage(String providerName) {
        if (providerName == null || providerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Provider name cannot be null or empty");
        }
        
        String key = providerName.trim();
        return STORAGE_CACHE.computeIfAbsent(key, name -> {
            FileStorageProvider provider = getProvider(name);
            if (provider == null) {
                return null;
            }
            
            if (!provider.isAvailable()) {
                throw new IllegalStateException("Provider is not available: " + name);
            }
            
            return provider.createStorage();
        });
    }
    
    /**
     * Gets the highest priority available provider.
     * 
     * @return the highest priority provider or null if none available
     */
    public static FileStorageProvider getBestProvider() {
        for (FileStorageProvider provider : getProviders()) {
            if (provider.isAvailable()) {
                return provider;
            }
        }
        return null;
    }
    
    /**
     * Gets the highest priority available file storage instance.
     * 
     * @return the highest priority file storage instance or null if none available
     * @throws IllegalStateException if no providers are available
     */
    public static FileStorage getBestStorage() {
        FileStorageProvider provider = getBestProvider();
        if (provider == null) {
            throw new IllegalStateException("No available file storage providers found");
        }
        
        return getStorage(provider.getProviderName());
    }
    
    /**
     * Gets all available provider names.
     * 
     * @return list of available provider names
     */
    public static List<String> getAvailableProviderNames() {
        List<String> names = new ArrayList<>();
        for (FileStorageProvider provider : getProviders()) {
            if (provider.isAvailable()) {
                names.add(provider.getProviderName());
            }
        }
        return names;
    }
    
    /**
     * Checks if a provider is available.
     * 
     * @param providerName the name of the provider
     * @return true if provider is available, false otherwise
     */
    public static boolean isProviderAvailable(String providerName) {
        if (providerName == null || providerName.trim().isEmpty()) {
            return false;
        }
        
        FileStorageProvider provider = getProvider(providerName.trim());
        return provider != null && provider.isAvailable();
    }
    
    /**
     * Clears the provider and storage caches.
     * This method is primarily useful for testing or when providers are reloaded.
     */
    public static void clearCache() {
        PROVIDER_CACHE.clear();
        STORAGE_CACHE.clear();
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
        return getAvailableProviderNames().size();
    }
    
    /**
     * Loads all file storage providers from the classpath.
     * 
     * @return list of providers sorted by priority
     */
    private static List<FileStorageProvider> loadProviders() {
        List<FileStorageProvider> providers = new ArrayList<>();
        
        ServiceLoader<FileStorageProvider> serviceLoader = ServiceLoader.load(FileStorageProvider.class);
        for (FileStorageProvider provider : serviceLoader) {
            providers.add(provider);
        }
        
        // Sort by priority (highest first)
        providers.sort(Comparator.comparingInt(FileStorageProvider::getPriority).reversed());
        
        return providers;
    }
}
