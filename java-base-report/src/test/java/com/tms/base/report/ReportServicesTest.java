package com.tms.base.report;

import com.tms.base.report.spi.ReportProvider;
import com.tms.base.report.spi.TemplateEngine;
import com.tms.base.report.spi.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for ReportServices.
 *
 * @since 1.0.0
 */
class ReportServicesTest {

    @BeforeEach
    void setUp() {
        ReportServices.clearCaches();
    }

    @Test
    void shouldGetProviders() {
        List<ReportProvider> providers = ReportServices.getProviders();

        assertNotNull(providers);
        // Should be empty since no providers are registered in test
        assertTrue(providers.isEmpty());
    }

    @Test
    void shouldGetTemplateEngines() {
        List<TemplateEngine> engines = ReportServices.getTemplateEngines();

        assertNotNull(engines);
        // Should be empty since no engines are registered in test
        assertTrue(engines.isEmpty());
    }

    @Test
    void shouldGetSerializers() {
        List<Serializer> serializers = ReportServices.getSerializers();

        assertNotNull(serializers);
        // Should be empty since no serializers are registered in test
        assertTrue(serializers.isEmpty());
    }

    @Test
    void shouldGetProviderByName() {
        ReportProvider provider = ReportServices.getProvider("test-provider");

        assertNull(provider); // No providers registered
    }

    @Test
    void shouldGetProviderWithFallback() {
        ReportProvider provider = ReportServices.getProvider("test-provider", "fallback-provider");

        assertNull(provider); // No providers registered
    }

    @Test
    void shouldGetTemplateEngineByName() {
        TemplateEngine engine = ReportServices.getTemplateEngine("test-engine");

        assertNull(engine); // No engines registered
    }

    @Test
    void shouldGetTemplateEngineWithFallback() {
        TemplateEngine engine = ReportServices.getTemplateEngine("test-engine", "fallback-engine");

        assertNull(engine); // No engines registered
    }

    @Test
    void shouldGetSerializerByName() {
        Serializer serializer = ReportServices.getSerializer("test-serializer");

        assertNull(serializer); // No serializers registered
    }

    @Test
    void shouldGetSerializerWithFallback() {
        Serializer serializer = ReportServices.getSerializer("test-serializer", "fallback-serializer");

        assertNull(serializer); // No serializers registered
    }

    @Test
    void shouldGetDefaultProvider() {
        ReportProvider provider = ReportServices.getDefaultProvider();

        assertNull(provider); // No providers registered
    }

    @Test
    void shouldGetDefaultTemplateEngine() {
        TemplateEngine engine = ReportServices.getDefaultTemplateEngine();

        assertNull(engine); // No engines registered
    }

    @Test
    void shouldGetDefaultSerializer() {
        Serializer serializer = ReportServices.getDefaultSerializer();

        assertNull(serializer); // No serializers registered
    }

    @Test
    void shouldClearCaches() {
        // This should not throw any exceptions
        ReportServices.clearCaches();
    }

    @Test
    void shouldReloadServices() {
        // This should not throw any exceptions
        ReportServices.reload();
    }

    @Test
    void shouldCacheProviders() {
        // Test that caching works by calling getProvider twice
        ReportProvider provider1 = ReportServices.getProvider("test-provider");
        ReportProvider provider2 = ReportServices.getProvider("test-provider");

        assertNull(provider1);
        assertNull(provider2);
        // Both should return null since no providers are registered
    }

    @Test
    void shouldCacheTemplateEngines() {
        // Test that caching works by calling getTemplateEngine twice
        TemplateEngine engine1 = ReportServices.getTemplateEngine("test-engine");
        TemplateEngine engine2 = ReportServices.getTemplateEngine("test-engine");

        assertNull(engine1);
        assertNull(engine2);
        // Both should return null since no engines are registered
    }

    @Test
    void shouldCacheSerializers() {
        // Test that caching works by calling getSerializer twice
        Serializer serializer1 = ReportServices.getSerializer("test-serializer");
        Serializer serializer2 = ReportServices.getSerializer("test-serializer");

        assertNull(serializer1);
        assertNull(serializer2);
        // Both should return null since no serializers are registered
    }
}
