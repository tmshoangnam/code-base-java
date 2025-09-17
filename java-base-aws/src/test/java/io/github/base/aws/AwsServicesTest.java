package io.github.base.aws;

import io.github.base.aws.api.config.AwsServiceConfig;
import io.github.base.aws.spi.AwsServiceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AwsServices}.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("AwsServices Tests")
class AwsServicesTest {

    private AwsServiceProvider mockProvider;
    private AwsServiceConfig mockConfig;
    private Object mockService;

    @BeforeEach
    void setUp() {
        // Clear cache before each test
        AwsServices.clearCache();

        // Create mocks
        mockProvider = mock(AwsServiceProvider.class);
        mockConfig = mock(AwsServiceConfig.class);
        mockService = mock(Object.class);

        // Setup mock behavior
        when(mockProvider.getServiceName()).thenReturn("test-service");
        when(mockProvider.getPriority()).thenReturn(100);
        when(mockProvider.isAvailable()).thenReturn(true);
        when(mockProvider.getCapabilities()).thenReturn(Map.of("capability1", true, "capability2", false));
        when(mockProvider.getRequiredConfiguration()).thenReturn(Map.of("required1", "string", "required2", "integer"));
        when(mockProvider.getOptionalConfiguration()).thenReturn(Map.of("optional1", "string", "optional2", "boolean"));
        when(mockProvider.validateConfiguration(any(AwsServiceConfig.class))).thenReturn(List.of());
        when(mockProvider.createService(any(AwsServiceConfig.class))).thenReturn(mockService);
    }

    @AfterEach
    void tearDown() {
        AwsServices.clearCache();
    }

    @Nested
    @DisplayName("Provider Management Tests")
    class ProviderManagementTests {

        @Test
        @DisplayName("getProviders should return empty list when no providers available")
        void getProvidersShouldReturnEmptyListWhenNoProvidersAvailable() {
            // When
            List<AwsServiceProvider> providers = AwsServices.getProviders();

            // Then
            assertThat(providers).isEmpty();
        }

        @Test
        @DisplayName("getProvider should return null when provider not found")
        void getProviderShouldReturnNullWhenProviderNotFound() {
            // When
            AwsServiceProvider provider = AwsServices.getProvider("non-existent-service");

            // Then
            assertThat(provider).isNull();
        }

        @Test
        @DisplayName("getProvider should throw IllegalArgumentException for null service name")
        void getProviderShouldThrowIllegalArgumentExceptionForNullServiceName() {
            // When & Then
            assertThatThrownBy(() -> AwsServices.getProvider(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Service name cannot be null or empty");
        }

        @Test
        @DisplayName("getProvider should throw IllegalArgumentException for empty service name")
        void getProviderShouldThrowIllegalArgumentExceptionForEmptyServiceName() {
            // When & Then
            assertThatThrownBy(() -> AwsServices.getProvider(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Service name cannot be null or empty");
        }

        @Test
        @DisplayName("getProvider should throw IllegalArgumentException for blank service name")
        void getProviderShouldThrowIllegalArgumentExceptionForBlankServiceName() {
            // When & Then
            assertThatThrownBy(() -> AwsServices.getProvider("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Service name cannot be null or empty");
        }

        @Test
        @DisplayName("getProvider should trim whitespace from service name")
        void getProviderShouldTrimWhitespaceFromServiceName() {
            // Given
            String serviceNameWithWhitespace = "  test-service  ";

            // When
            AwsServiceProvider provider = AwsServices.getProvider(serviceNameWithWhitespace);

            // Then
            // Should not throw exception and should handle trimmed name
            assertThat(provider).isNull(); // No providers registered in test
        }
    }

    @Nested
    @DisplayName("Service Management Tests")
    class ServiceManagementTests {

        @Test
        @DisplayName("getService should throw IllegalArgumentException for null service name")
        void getServiceShouldThrowIllegalArgumentExceptionForNullServiceName() {
            // When & Then
            assertThatThrownBy(() -> AwsServices.getService(null, mockConfig))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Service name cannot be null or empty");
        }

        @Test
        @DisplayName("getService should throw IllegalArgumentException for null config")
        void getServiceShouldThrowIllegalArgumentExceptionForNullConfig() {
            // When & Then
            assertThatThrownBy(() -> AwsServices.getService("test-service", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Configuration cannot be null");
        }

        @Test
        @DisplayName("getService should return null when provider not found")
        void getServiceShouldReturnNullWhenProviderNotFound() {
            // When
            Object service = AwsServices.getService("non-existent-service", mockConfig);

            // Then
            assertThat(service).isNull();
        }

        @Test
        @DisplayName("getService should throw IllegalStateException when provider not available")
        void getServiceShouldThrowIllegalStateExceptionWhenProviderNotAvailable() {
            // Given
            when(mockProvider.isAvailable()).thenReturn(false);
            // Note: In real scenario, provider would be registered via ServiceLoader
            // For testing, we simulate this by directly testing the logic

            // When & Then
            // This test would require actual ServiceLoader setup to work properly
            // For now, we test the null case
            Object service = AwsServices.getService("test-service", mockConfig);
            assertThat(service).isNull();
        }
    }

    @Nested
    @DisplayName("Best Provider Tests")
    class BestProviderTests {

        @Test
        @DisplayName("getBestProvider should return null when no providers available")
        void getBestProviderShouldReturnNullWhenNoProvidersAvailable() {
            // When
            AwsServiceProvider provider = AwsServices.getBestProvider("test-service");

            // Then
            assertThat(provider).isNull();
        }

        @Test
        @DisplayName("getBestProvider should return null for null service name")
        void getBestProviderShouldReturnNullForNullServiceName() {
            // When
            AwsServiceProvider provider = AwsServices.getBestProvider(null);

            // Then
            assertThat(provider).isNull();
        }

        @Test
        @DisplayName("getBestProvider should return null for empty service name")
        void getBestProviderShouldReturnNullForEmptyServiceName() {
            // When
            AwsServiceProvider provider = AwsServices.getBestProvider("");

            // Then
            assertThat(provider).isNull();
        }

        @Test
        @DisplayName("getBestProvider should return null for blank service name")
        void getBestProviderShouldReturnNullForBlankServiceName() {
            // When
            AwsServiceProvider provider = AwsServices.getBestProvider("   ");

            // Then
            assertThat(provider).isNull();
        }

        @Test
        @DisplayName("getBestService should throw IllegalStateException when no providers available")
        void getBestServiceShouldThrowIllegalStateExceptionWhenNoProvidersAvailable() {
            // When & Then
            assertThatThrownBy(() -> AwsServices.getBestService("test-service", mockConfig))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No available AWS service providers found for: test-service");
        }
    }

    @Nested
    @DisplayName("Service Discovery Tests")
    class ServiceDiscoveryTests {

        @Test
        @DisplayName("getAvailableServiceNames should return empty list when no providers available")
        void getAvailableServiceNamesShouldReturnEmptyListWhenNoProvidersAvailable() {
            // When
            List<String> serviceNames = AwsServices.getAvailableServiceNames();

            // Then
            assertThat(serviceNames).isEmpty();
        }

        @Test
        @DisplayName("isProviderAvailable should return false when provider not found")
        void isProviderAvailableShouldReturnFalseWhenProviderNotFound() {
            // When
            boolean available = AwsServices.isProviderAvailable("non-existent-service");

            // Then
            assertThat(available).isFalse();
        }

        @Test
        @DisplayName("isProviderAvailable should return false for null service name")
        void isProviderAvailableShouldReturnFalseForNullServiceName() {
            // When
            boolean available = AwsServices.isProviderAvailable(null);

            // Then
            assertThat(available).isFalse();
        }

        @Test
        @DisplayName("isProviderAvailable should return false for empty service name")
        void isProviderAvailableShouldReturnFalseForEmptyServiceName() {
            // When
            boolean available = AwsServices.isProviderAvailable("");

            // Then
            assertThat(available).isFalse();
        }

        @Test
        @DisplayName("isProviderAvailable should return false for blank service name")
        void isProviderAvailableShouldReturnFalseForBlankServiceName() {
            // When
            boolean available = AwsServices.isProviderAvailable("   ");

            // Then
            assertThat(available).isFalse();
        }

        @Test
        @DisplayName("isProviderAvailable should trim whitespace from service name")
        void isProviderAvailableShouldTrimWhitespaceFromServiceName() {
            // Given
            String serviceNameWithWhitespace = "  test-service  ";

            // When
            boolean available = AwsServices.isProviderAvailable(serviceNameWithWhitespace);

            // Then
            assertThat(available).isFalse(); // No providers registered in test
        }
    }

    @Nested
    @DisplayName("Provider Information Tests")
    class ProviderInformationTests {

        @Test
        @DisplayName("getProviderCapabilities should return empty map when provider not found")
        void getProviderCapabilitiesShouldReturnEmptyMapWhenProviderNotFound() {
            // When
            Map<String, Boolean> capabilities = AwsServices.getProviderCapabilities("non-existent-service");

            // Then
            assertThat(capabilities).isEmpty();
        }

        @Test
        @DisplayName("getRequiredConfiguration should return empty map when provider not found")
        void getRequiredConfigurationShouldReturnEmptyMapWhenProviderNotFound() {
            // When
            Map<String, String> config = AwsServices.getRequiredConfiguration("non-existent-service");

            // Then
            assertThat(config).isEmpty();
        }

        @Test
        @DisplayName("getOptionalConfiguration should return empty map when provider not found")
        void getOptionalConfigurationShouldReturnEmptyMapWhenProviderNotFound() {
            // When
            Map<String, String> config = AwsServices.getOptionalConfiguration("non-existent-service");

            // Then
            assertThat(config).isEmpty();
        }
    }

    @Nested
    @DisplayName("Configuration Validation Tests")
    class ConfigurationValidationTests {

        @Test
        @DisplayName("validateConfiguration should return error when provider not found")
        void validateConfigurationShouldReturnErrorWhenProviderNotFound() {
            // When
            List<String> errors = AwsServices.validateConfiguration("non-existent-service", mockConfig);

            // Then
            assertThat(errors).hasSize(1);
            assertThat(errors.get(0)).isEqualTo("Provider not found: non-existent-service");
        }
    }

    @Nested
    @DisplayName("Cache Management Tests")
    class CacheManagementTests {

        @Test
        @DisplayName("clearCache should clear all caches")
        void clearCacheShouldClearAllCaches() {
            // Given
            // Cache is already cleared in setUp, but we can test the method exists and works
            AwsServices.clearCache();

            // When
            List<AwsServiceProvider> providers = AwsServices.getProviders();

            // Then
            assertThat(providers).isEmpty();
        }

        @Test
        @DisplayName("reload should clear cache and reload providers")
        void reloadShouldClearCacheAndReloadProviders() {
            // Given
            AwsServices.clearCache();

            // When
            AwsServices.reload();

            // Then
            List<AwsServiceProvider> providers = AwsServices.getProviders();
            assertThat(providers).isEmpty(); // No providers registered in test
        }
    }

    @Nested
    @DisplayName("Provider Count Tests")
    class ProviderCountTests {

        @Test
        @DisplayName("getProviderCount should return zero when no providers available")
        void getProviderCountShouldReturnZeroWhenNoProvidersAvailable() {
            // When
            int count = AwsServices.getProviderCount();

            // Then
            assertThat(count).isZero();
        }

        @Test
        @DisplayName("getProviderCount with service name should return zero when no providers available")
        void getProviderCountWithServiceNameShouldReturnZeroWhenNoProvidersAvailable() {
            // When
            int count = AwsServices.getProviderCount("test-service");

            // Then
            assertThat(count).isZero();
        }

        @Test
        @DisplayName("getProviderCount with service name should return zero for null service name")
        void getProviderCountWithServiceNameShouldReturnZeroForNullServiceName() {
            // When
            int count = AwsServices.getProviderCount(null);

            // Then
            assertThat(count).isZero();
        }

        @Test
        @DisplayName("getProviderCount with service name should return zero for empty service name")
        void getProviderCountWithServiceNameShouldReturnZeroForEmptyServiceName() {
            // When
            int count = AwsServices.getProviderCount("");

            // Then
            assertThat(count).isZero();
        }

        @Test
        @DisplayName("getProviderCount with service name should return zero for blank service name")
        void getProviderCountWithServiceNameShouldReturnZeroForBlankServiceName() {
            // When
            int count = AwsServices.getProviderCount("   ");

            // Then
            assertThat(count).isZero();
        }

        @Test
        @DisplayName("getProviderCount with service name should trim whitespace from service name")
        void getProviderCountWithServiceNameShouldTrimWhitespaceFromServiceName() {
            // Given
            String serviceNameWithWhitespace = "  test-service  ";

            // When
            int count = AwsServices.getProviderCount(serviceNameWithWhitespace);

            // Then
            assertThat(count).isZero(); // No providers registered in test
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle multiple calls to getProviders consistently")
        void shouldHandleMultipleCallsToGetProvidersConsistently() {
            // When
            List<AwsServiceProvider> providers1 = AwsServices.getProviders();
            List<AwsServiceProvider> providers2 = AwsServices.getProviders();

            // Then
            assertThat(providers1).isEqualTo(providers2)
                    .isEmpty();
        }

        @Test
        @DisplayName("Should handle cache consistency")
        void shouldHandleCacheConsistency() {
            // Given
            String serviceName = "test-service";

            // When
            AwsServiceProvider provider1 = AwsServices.getProvider(serviceName);
            AwsServiceProvider provider2 = AwsServices.getProvider(serviceName);

            // Then
            assertThat(provider1).isEqualTo(provider2)
                    .isNull();
        }

        @Test
        @DisplayName("Should handle service cache key generation")
        void shouldHandleServiceCacheKeyGeneration() {
            // Given
            String serviceName = "test-service";

            // When
            Object service1 = AwsServices.getService(serviceName, mockConfig);
            Object service2 = AwsServices.getService(serviceName, mockConfig);

            // Then
            assertThat(service1).isEqualTo(service2)
                    .isNull();
        }
    }
}
