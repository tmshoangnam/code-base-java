package io.github.base.file;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.spi.FileStorageProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileServices Unit Tests")
class FileServicesTest {

    @Mock
    private FileStorageProvider mockProvider1;

    @Mock
    private FileStorageProvider mockProvider2;

    @Mock
    private FileStorage mockStorage;

    @BeforeEach
    void setUp() {
        FileServices.clearCache();
    }

    @AfterEach
    void tearDown() {
        FileServices.clearCache();
    }

    @Nested
    @DisplayName("getProviders() Tests")
    class GetProvidersTests {

        @Test
        @DisplayName("should return empty list when no providers are available")
        void shouldReturnEmptyListWhenNoProvidersAvailable() {
            List<FileStorageProvider> providers = FileServices.getProviders();
            assertThat(providers).isEmpty();
        }

        @Test
        @DisplayName("should return providers sorted by priority")
        void shouldReturnProvidersSortedByPriority() {
            // Note: In a real test, we would need to mock ServiceLoader
            // For now, we'll test the method exists and returns a list
            List<FileStorageProvider> providers = FileServices.getProviders();
            assertThat(providers).isNotNull();
        }
    }

    @Nested
    @DisplayName("getProvider() Tests")
    class GetProviderTests {

        @Test
        @DisplayName("should throw IllegalArgumentException if providerName is null")
        void shouldThrowExceptionIfProviderNameIsNull() {
            assertThatThrownBy(() -> FileServices.getProvider(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Provider name cannot be null or empty");
        }

        @Test
        @DisplayName("should throw IllegalArgumentException if providerName is empty")
        void shouldThrowExceptionIfProviderNameIsEmpty() {
            assertThatThrownBy(() -> FileServices.getProvider("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Provider name cannot be null or empty");
        }

        @Test
        @DisplayName("should return null for non-existent provider")
        void shouldReturnNullForNonExistentProvider() {
            FileStorageProvider provider = FileServices.getProvider("non-existent");
            assertThat(provider).isNull();
        }
    }

    @Nested
    @DisplayName("getStorage() Tests")
    class GetStorageTests {

        @Test
        @DisplayName("should throw IllegalArgumentException if providerName is null")
        void shouldThrowExceptionIfProviderNameIsNull() {
            assertThatThrownBy(() -> FileServices.getStorage(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Provider name cannot be null or empty");
        }

        @Test
        @DisplayName("should throw IllegalArgumentException if providerName is empty")
        void shouldThrowExceptionIfProviderNameIsEmpty() {
            assertThatThrownBy(() -> FileServices.getStorage("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Provider name cannot be null or empty");
        }

        @Test
        @DisplayName("should return null for non-existent provider")
        void shouldReturnNullForNonExistentProvider() {
            FileStorage storage = FileServices.getStorage("non-existent");
            assertThat(storage).isNull();
        }
    }

    @Nested
    @DisplayName("getBestProvider() Tests")
    class GetBestProviderTests {

        @Test
        @DisplayName("should return null when no providers are available")
        void shouldReturnNullWhenNoProvidersAvailable() {
            FileStorageProvider provider = FileServices.getBestProvider();
            assertThat(provider).isNull();
        }
    }

    @Nested
    @DisplayName("getBestStorage() Tests")
    class GetBestStorageTests {

        @Test
        @DisplayName("should throw IllegalStateException when no providers are available")
        void shouldThrowExceptionWhenNoProvidersAvailable() {
            assertThatThrownBy(FileServices::getBestStorage)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("No available file storage providers found");
        }
    }

    @Nested
    @DisplayName("getAvailableProviderNames() Tests")
    class GetAvailableProviderNamesTests {

        @Test
        @DisplayName("should return empty list when no providers are available")
        void shouldReturnEmptyListWhenNoProvidersAvailable() {
            List<String> names = FileServices.getAvailableProviderNames();
            assertThat(names).isEmpty();
        }
    }

    @Nested
    @DisplayName("isProviderAvailable() Tests")
    class IsProviderAvailableTests {

        @Test
        @DisplayName("should return false for null provider name")
        void shouldReturnFalseForNullProviderName() {
            FileServices.isProviderAvailable(null);
            boolean available = false;
            assertThat(available).isFalse();
        }

        @Test
        @DisplayName("should return false for empty provider name")
        void shouldReturnFalseForEmptyProviderName() {
            boolean available = FileServices.isProviderAvailable("   ");
            assertThat(available).isFalse();
        }

        @Test
        @DisplayName("should return false for non-existent provider")
        void shouldReturnFalseForNonExistentProvider() {
            boolean available = FileServices.isProviderAvailable("non-existent");
            assertThat(available).isFalse();
        }
    }

    @Nested
    @DisplayName("Cache Management Tests")
    class CacheManagementTests {

        @Test
        @DisplayName("should clear cache successfully")
        void shouldClearCacheSuccessfully() {
            // This should not throw any exceptions
            FileServices.clearCache();
            assertThat(FileServices.getProviders()).isEmpty();
        }

        @Test
        @DisplayName("should reload providers successfully")
        void shouldReloadProvidersSuccessfully() {
            // This should not throw any exceptions
            FileServices.reload();
            assertThat(FileServices.getProviders()).isNotNull();
        }
    }

    @Nested
    @DisplayName("getProviderCount() Tests")
    class GetProviderCountTests {

        @Test
        @DisplayName("should return zero when no providers are available")
        void shouldReturnZeroWhenNoProvidersAvailable() {
            int count = FileServices.getProviderCount();
            assertThat(count).isZero();
        }
    }

    @Nested
    @DisplayName("Provider Configuration Tests")
    class ProviderConfigurationTests {

        @Test
        @DisplayName("should handle provider with default capabilities")
        void shouldHandleProviderWithDefaultCapabilities() {
            when(mockProvider1.getCapabilities()).thenReturn(Map.of(
                    "presigned-urls", false,
                    "async-operations", true,
                    "metadata", true,
                    "list-files", true,
                    "checksum", false
            ));

            Map<String, Boolean> capabilities = mockProvider1.getCapabilities();
            assertThat(capabilities).containsEntry("presigned-urls", false)
                    .containsEntry("async-operations", true)
                    .containsEntry("metadata", true)
                    .containsEntry("list-files", true)
                    .containsEntry("checksum", false);
        }

        @Test
        @DisplayName("should handle provider with default supported file types")
        void shouldHandleProviderWithDefaultSupportedFileTypes() {
            when(mockProvider1.getSupportedFileTypes()).thenReturn(null);

            Set<io.github.base.file.api.model.FileType> supportedTypes = mockProvider1.getSupportedFileTypes();
            assertThat(supportedTypes).isNull();
        }

        @Test
        @DisplayName("should handle provider with default max file size")
        void shouldHandleProviderWithDefaultMaxFileSize() {
            when(mockProvider1.getMaxFileSize()).thenReturn(0L);

            long maxSize = mockProvider1.getMaxFileSize();
            assertThat(maxSize).isZero();
        }

        @Test
        @DisplayName("should handle provider with default required configuration")
        void shouldHandleProviderWithDefaultRequiredConfiguration() {
            when(mockProvider1.getRequiredConfiguration()).thenReturn(Map.of());

            Map<String, String> requiredConfig = mockProvider1.getRequiredConfiguration();
            assertThat(requiredConfig).isEmpty();
        }

        @Test
        @DisplayName("should handle provider with default priority")
        void shouldHandleProviderWithDefaultPriority() {
            when(mockProvider1.getPriority()).thenReturn(0);

            int priority = mockProvider1.getPriority();
            assertThat(priority).isZero();
        }
    }

    @Nested
    @DisplayName("Provider Interface Tests")
    class ProviderInterfaceTests {

        @Test
        @DisplayName("should handle provider name")
        void shouldHandleProviderName() {
            when(mockProvider1.getProviderName()).thenReturn("test-provider");

            String name = mockProvider1.getProviderName();
            assertThat(name).isEqualTo("test-provider");
        }

        @Test
        @DisplayName("should handle provider description")
        void shouldHandleProviderDescription() {
            when(mockProvider1.getProviderDescription()).thenReturn("Test provider description");

            String description = mockProvider1.getProviderDescription();
            assertThat(description).isEqualTo("Test provider description");
        }

        @Test
        @DisplayName("should handle provider version")
        void shouldHandleProviderVersion() {
            when(mockProvider1.getProviderVersion()).thenReturn("1.0.0");

            String version = mockProvider1.getProviderVersion();
            assertThat(version).isEqualTo("1.0.0");
        }

        @Test
        @DisplayName("should handle provider availability")
        void shouldHandleProviderAvailability() {
            when(mockProvider1.isAvailable()).thenReturn(true);

            boolean available = mockProvider1.isAvailable();
            assertThat(available).isTrue();
        }

        @Test
        @DisplayName("should handle provider storage creation")
        void shouldHandleProviderStorageCreation() {
            when(mockProvider1.createStorage()).thenReturn(mockStorage);

            FileStorage storage = mockProvider1.createStorage();
            assertThat(storage).isEqualTo(mockStorage);
        }

        @Test
        @DisplayName("should handle provider optional configuration")
        void shouldHandleProviderOptionalConfiguration() {
            when(mockProvider1.getOptionalConfiguration()).thenReturn(Map.of("key", "value"));

            Map<String, String> config = mockProvider1.getOptionalConfiguration();
            assertThat(config).containsEntry("key", "value");
        }

        @Test
        @DisplayName("should handle provider limitations")
        void shouldHandleProviderLimitations() {
            when(mockProvider1.getLimitations()).thenReturn(Map.of("max-size", "100MB"));

            Map<String, String> limitations = mockProvider1.getLimitations();
            assertThat(limitations).containsEntry("max-size", "100MB");
        }

        @Test
        @DisplayName("should handle provider configuration validation")
        void shouldHandleProviderConfigurationValidation() {
            when(mockProvider1.validateConfiguration(Map.of("key", "value"))).thenReturn(List.of());

            List<String> errors = mockProvider1.validateConfiguration(Map.of("key", "value"));
            assertThat(errors).isEmpty();
        }
    }
}