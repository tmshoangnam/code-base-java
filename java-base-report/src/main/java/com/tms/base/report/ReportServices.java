package com.tms.base.report;

import com.tms.base.report.spi.ReportProvider;
import com.tms.base.report.spi.TemplateEngine;
import com.tms.base.report.spi.Serializer;
import java.util.ServiceLoader;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServiceLoader helper for report providers and services.
 *
 * @since 1.0.0
 */
public class ReportServices {

    private ReportServices() {
        // Prevent instantiation
    }

    private static final ServiceLoader<ReportProvider> PROVIDERS =
        ServiceLoader.load(ReportProvider.class);
    private static final ServiceLoader<TemplateEngine> TEMPLATE_ENGINES =
        ServiceLoader.load(TemplateEngine.class);
    private static final ServiceLoader<Serializer> SERIALIZERS =
        ServiceLoader.load(Serializer.class);

    private static final Map<String, ReportProvider> PROVIDER_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, TemplateEngine> TEMPLATE_ENGINE_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Serializer> SERIALIZER_CACHE = new ConcurrentHashMap<>();

    /**
     * Get all available report providers.
     *
     * @return List of report providers sorted by priority
     */
    public static List<ReportProvider> getProviders() {
        List<ReportProvider> providers = new ArrayList<>();
        for (ReportProvider provider : PROVIDERS) {
            providers.add(provider);
        }
        providers.sort(Comparator.comparingInt(ReportProvider::getPriority).reversed());
        return providers;
    }

    /**
     * Get provider by name.
     *
     * @param name Provider name
     * @return Report provider or null if not found
     */
    public static ReportProvider getProvider(String name) {
        return PROVIDER_CACHE.computeIfAbsent(name, key -> getProviders().stream()
                .filter(provider -> provider.getName().equals(key))
                .findFirst()
                .orElse(null)
        );
    }

    /**
     * Get provider by name with fallback.
     *
     * @param name Provider name
     * @param fallbackName Fallback provider name
     * @return Report provider or fallback provider
     */
    public static ReportProvider getProvider(String name, String fallbackName) {
        ReportProvider provider = getProvider(name);
        if (provider == null) {
            provider = getProvider(fallbackName);
        }
        return provider;
    }

    /**
     * Get all available template engines.
     *
     * @return List of template engines sorted by priority
     */
    public static List<TemplateEngine> getTemplateEngines() {
        List<TemplateEngine> engines = new ArrayList<>();
        for (TemplateEngine engine : TEMPLATE_ENGINES) {
            engines.add(engine);
        }
        engines.sort(Comparator.comparingInt(TemplateEngine::getPriority).reversed());
        return engines;
    }

    /**
     * Get template engine by name.
     *
     * @param name Engine name
     * @return Template engine or null if not found
     */
    public static TemplateEngine getTemplateEngine(String name) {
        return TEMPLATE_ENGINE_CACHE.computeIfAbsent(name, key -> getTemplateEngines().stream()
                .filter(engine -> engine.getName().equals(key))
                .findFirst()
                .orElse(null)
        );
    }

    /**
     * Get template engine by name with fallback.
     *
     * @param name Engine name
     * @param fallbackName Fallback engine name
     * @return Template engine or fallback engine
     */
    public static TemplateEngine getTemplateEngine(String name, String fallbackName) {
        TemplateEngine engine = getTemplateEngine(name);
        if (engine == null) {
            engine = getTemplateEngine(fallbackName);
        }
        return engine;
    }

    /**
     * Get all available serializers.
     *
     * @return List of serializers sorted by priority
     */
    public static List<Serializer> getSerializers() {
        List<Serializer> serializers = new ArrayList<>();
        for (Serializer serializer : SERIALIZERS) {
            serializers.add(serializer);
        }
        serializers.sort(Comparator.comparingInt(Serializer::getPriority).reversed());
        return serializers;
    }

    /**
     * Get serializer by name.
     *
     * @param name Serializer name
     * @return Serializer or null if not found
     */
    public static Serializer getSerializer(String name) {
        return SERIALIZER_CACHE.computeIfAbsent(name, key -> getSerializers().stream()
                .filter(serializer -> serializer.getName().equals(key))
                .findFirst()
                .orElse(null)
        );
    }

    /**
     * Get serializer by name with fallback.
     *
     * @param name Serializer name
     * @param fallbackName Fallback serializer name
     * @return Serializer or fallback serializer
     */
    public static Serializer getSerializer(String name, String fallbackName) {
        Serializer serializer = getSerializer(name);
        if (serializer == null) {
            serializer = getSerializer(fallbackName);
        }
        return serializer;
    }

    /**
     * Get default provider (highest priority).
     *
     * @return Default provider or null if none available
     */
    public static ReportProvider getDefaultProvider() {
        List<ReportProvider> providers = getProviders();
        return providers.isEmpty() ? null : providers.get(0);
    }

    /**
     * Get default template engine (highest priority).
     *
     * @return Default template engine or null if none available
     */
    public static TemplateEngine getDefaultTemplateEngine() {
        List<TemplateEngine> engines = getTemplateEngines();
        return engines.isEmpty() ? null : engines.get(0);
    }

    /**
     * Get default serializer (highest priority).
     *
     * @return Default serializer or null if none available
     */
    public static Serializer getDefaultSerializer() {
        List<Serializer> serializers = getSerializers();
        return serializers.isEmpty() ? null : serializers.get(0);
    }

    /**
     * Clear all caches.
     */
    public static void clearCaches() {
        PROVIDER_CACHE.clear();
        TEMPLATE_ENGINE_CACHE.clear();
        SERIALIZER_CACHE.clear();
    }

    /**
     * Reload all services.
     */
    public static void reload() {
        clearCaches();
        PROVIDERS.reload();
        TEMPLATE_ENGINES.reload();
        SERIALIZERS.reload();
    }
}
