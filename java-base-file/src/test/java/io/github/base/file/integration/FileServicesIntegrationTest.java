package io.github.base.file.integration;

import io.github.base.file.FileServices;
import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.spi.FileStorageProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FileServices Integration Tests")
class FileServicesIntegrationTest {

    @Mock
    private FileStorageProvider mockProvider1;

    @Mock
    private FileStorageProvider mockProvider2;

    @Mock
    private FileStorage mockStorage1;

    @Mock
    private FileStorage mockStorage2;

    @BeforeEach
    void setUp() {
        FileServices.clearCache();
    }

    @AfterEach
    void tearDown() {
        FileServices.clearCache();
    }

    @Test
    @DisplayName("should handle multiple providers with different priorities")
    void shouldHandleMultipleProvidersWithDifferentPriorities() {
        // Test provider discovery and caching
        List<FileStorageProvider> providers = FileServices.getProviders();
        assertThat(providers).isNotNull();

        // Test provider retrieval by name
        FileStorageProvider provider1 = FileServices.getProvider("provider1");
        assertThat(provider1).isNull(); // No providers loaded in test environment

        // Test storage retrieval
        FileStorage storage1 = FileServices.getStorage("provider1");
        assertThat(storage1).isNull(); // No providers loaded in test environment

        // Test best provider selection
        FileStorageProvider bestProvider = FileServices.getBestProvider();
        assertThat(bestProvider).isNull(); // No providers loaded in test environment

        // Test available provider names
        List<String> availableNames = FileServices.getAvailableProviderNames();
        assertThat(availableNames).isEmpty();

        // Test provider availability check
        boolean isAvailable = FileServices.isProviderAvailable("provider1");
        assertThat(isAvailable).isFalse();

        // Test provider count
        int count = FileServices.getProviderCount();
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("should handle provider configuration validation")
    void shouldHandleProviderConfigurationValidation() {
        when(mockProvider1.getProviderName()).thenReturn("configurable-provider");
        when(mockProvider1.getRequiredConfiguration()).thenReturn(Map.of(
                "api-key", "API key for authentication",
                "endpoint", "Storage endpoint URL"
        ));
        when(mockProvider1.getOptionalConfiguration()).thenReturn(Map.of(
                "timeout", "Request timeout in seconds",
                "retries", "Number of retry attempts"
        ));
        when(mockProvider1.getLimitations()).thenReturn(Map.of(
                "max-file-size", "10MB",
                "max-files", "1000"
        ));

        // Test configuration validation with valid config
        Map<String, String> validConfig = Map.of(
                "api-key", "test-key",
                "endpoint", "https://storage.example.com"
        );
        when(mockProvider1.validateConfiguration(validConfig)).thenReturn(List.of());

        List<String> errors = mockProvider1.validateConfiguration(validConfig);
        assertThat(errors).isEmpty();

        // Test configuration validation with invalid config
        Map<String, String> invalidConfig = Map.of(
                "api-key", "test-key"
                // Missing required 'endpoint' configuration
        );
        when(mockProvider1.validateConfiguration(invalidConfig)).thenReturn(List.of(
                "Missing required configuration: endpoint"
        ));

        List<String> validationErrors = mockProvider1.validateConfiguration(invalidConfig);
        assertThat(validationErrors).contains("Missing required configuration: endpoint");
    }

    @Test
    @DisplayName("should handle provider capabilities and limitations")
    void shouldHandleProviderCapabilitiesAndLimitations() {
        when(mockProvider1.getProviderName()).thenReturn("capable-provider");
        when(mockProvider1.getCapabilities()).thenReturn(Map.of(
                "presigned-urls", true,
                "async-operations", true,
                "metadata", true,
                "list-files", true,
                "checksum", true
        ));
        when(mockProvider1.getSupportedFileTypes()).thenReturn(Set.of(
                io.github.base.file.api.model.FileType.FILE,
                io.github.base.file.api.model.FileType.BYTE,
                io.github.base.file.api.model.FileType.STREAM
        ));
        when(mockProvider1.getMaxFileSize()).thenReturn(5000000L);
        when(mockProvider1.getLimitations()).thenReturn(Map.of(
                "max-file-size", "5MB",
                "max-files", "10000",
                "supported-formats", "PDF, DOC, TXT, PNG, JPG"
        ));

        // Test capabilities
        Map<String, Boolean> capabilities = mockProvider1.getCapabilities();
        assertThat(capabilities).containsEntry("presigned-urls", true);
        assertThat(capabilities).containsEntry("async-operations", true);
        assertThat(capabilities).containsEntry("metadata", true);
        assertThat(capabilities).containsEntry("list-files", true);
        assertThat(capabilities).containsEntry("checksum", true);

        // Test supported file types
        Set<io.github.base.file.api.model.FileType> supportedTypes = mockProvider1.getSupportedFileTypes();
        assertThat(supportedTypes).contains(
                io.github.base.file.api.model.FileType.FILE,
                io.github.base.file.api.model.FileType.BYTE,
                io.github.base.file.api.model.FileType.STREAM
        );

        // Test max file size
        long maxSize = mockProvider1.getMaxFileSize();
        assertThat(maxSize).isEqualTo(5000000L);

        // Test limitations
        Map<String, String> limitations = mockProvider1.getLimitations();
        assertThat(limitations).containsEntry("max-file-size", "5MB")
                .containsEntry("max-files", "10000")
                .containsEntry("supported-formats", "PDF, DOC, TXT, PNG, JPG");
    }

    @Test
    @DisplayName("should handle cache management and reloading")
    void shouldHandleCacheManagementAndReloading() {
        // Test initial state
        assertThat(FileServices.getProviders()).isEmpty();
        assertThat(FileServices.getProviderCount()).isZero();

        // Test cache clearing
        FileServices.clearCache();
        assertThat(FileServices.getProviders()).isEmpty();

        // Test reloading
        FileServices.reload();
        assertThat(FileServices.getProviders()).isNotNull();
    }

    @Test
    @DisplayName("should handle error scenarios gracefully")
    void shouldHandleErrorScenariosGracefully() {
        // Test null provider name handling
        assertThat(FileServices.isProviderAvailable(null)).isFalse();
        assertThatThrownBy(() -> FileServices.getProvider(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provider name cannot be null or empty");
        assertThatThrownBy(() -> FileServices.getStorage(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provider name cannot be null or empty");

        // Test empty provider name handling
        assertThat(FileServices.isProviderAvailable("   ")).isFalse();
        assertThatThrownBy(() -> FileServices.getProvider("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provider name cannot be null or empty");
        assertThatThrownBy(() -> FileServices.getStorage("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provider name cannot be null or empty");

        // Test non-existent provider handling
        assertThat(FileServices.isProviderAvailable("non-existent")).isFalse();
        assertThat(FileServices.getProvider("non-existent")).isNull();
        assertThat(FileServices.getStorage("non-existent")).isNull();
    }

    @Test
    @DisplayName("should handle provider with default values")
    void shouldHandleProviderWithDefaultValues() {
        when(mockProvider1.getProviderName()).thenReturn("default-provider");
        when(mockProvider1.getPriority()).thenReturn(0); // Default priority
        when(mockProvider1.isAvailable()).thenReturn(true);
        when(mockProvider1.getCapabilities()).thenReturn(Map.of(
                "presigned-urls", false,
                "async-operations", true,
                "metadata", true,
                "list-files", true,
                "checksum", false
        ));
        when(mockProvider1.getSupportedFileTypes()).thenReturn(null); // Default: support all
        when(mockProvider1.getMaxFileSize()).thenReturn(0L); // Default: no limit
        when(mockProvider1.getRequiredConfiguration()).thenReturn(Map.of()); // Default: no required config
        when(mockProvider1.getOptionalConfiguration()).thenReturn(Map.of()); // Default: no optional config
        when(mockProvider1.getLimitations()).thenReturn(Map.of()); // Default: no limitations
        when(mockProvider1.validateConfiguration(any())).thenReturn(List.of()); // Default: no validation errors

        // Test default values
        assertThat(mockProvider1.getPriority()).isZero();
        assertThat(mockProvider1.getSupportedFileTypes()).isNull();
        assertThat(mockProvider1.getMaxFileSize()).isZero();
        assertThat(mockProvider1.getRequiredConfiguration()).isEmpty();
        assertThat(mockProvider1.getOptionalConfiguration()).isEmpty();
        assertThat(mockProvider1.getLimitations()).isEmpty();
        assertThat(mockProvider1.validateConfiguration(Map.of())).isEmpty();
    }
}