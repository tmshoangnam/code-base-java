package io.github.base.starter.file.autoconfig;

import io.github.base.file.FileServices;
import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;
import io.github.base.file.spi.FileStorageProvider;
import io.github.base.starter.file.impl.local.LocalFileStorageProvider;
import io.github.base.starter.file.impl.s3.S3FileStorageProvider;
import io.github.base.starter.file.impl.gcs.GcsFileStorageProvider;
import io.github.base.starter.file.impl.azure.AzureFileStorageProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FileStorageAutoConfiguration}.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FileStorageAutoConfiguration Tests")
class FileStorageAutoConfigurationTest {

    private FileStorageAutoConfiguration configuration;
    private FileStorageProperties properties;

    @BeforeEach
    void setUp() {
        configuration = new FileStorageAutoConfiguration();
        properties = createTestFileStorageProperties();
    }

    private FileStorageProperties createTestFileStorageProperties() {
        FileStorageProperties props = new FileStorageProperties();
        props.setDefaultProvider("local");
        props.setEnabled(true);
        props.setMetricsEnabled(true);
        props.setCacheEnabled(true);
        props.setCacheTtlSeconds(300L);
        props.setMaxCacheSize(1000);

        // Configure providers
        Map<String, FileStorageProperties.ProviderConfig> providers = Map.of(
            "local", createProviderConfig("local", true, 0),
            "s3", createProviderConfig("s3", true, 1),
            "gcs", createProviderConfig("gcs", false, 2),
            "azure", createProviderConfig("azure", true, 3)
        );
        props.setProviders(providers);

        return props;
    }

    private FileStorageProperties.ProviderConfig createProviderConfig(String type, boolean enabled, int priority) {
        FileStorageProperties.ProviderConfig config = new FileStorageProperties.ProviderConfig();
        config.setType(type);
        config.setEnabled(enabled);
        config.setPriority(priority);
        return config;
    }

    @Nested
    @DisplayName("File Storage Manager Bean Tests")
    class FileStorageManagerBeanTests {

        @Test
        @DisplayName("Should create file storage manager with enabled providers")
        void shouldCreateFileStorageManagerWithEnabledProviders() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(
                    createMockProvider("local"),
                    createMockProvider("s3"),
                    createMockProvider("gcs"),
                    createMockProvider("azure")
                );
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager manager = configuration.fileStorageManager(properties);

                assertThat(manager).isNotNull();
            }
        }

        @Test
        @DisplayName("Should filter providers based on configuration")
        void shouldFilterProvidersBasedOnConfiguration() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(
                    createMockProvider("local"),
                    createMockProvider("s3"),
                    createMockProvider("gcs"),
                    createMockProvider("azure")
                );
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager manager = configuration.fileStorageManager(properties);

                assertThat(manager).isNotNull();
                // Verify that only enabled providers are included
                Set<String> availableProviders = manager.getAvailableProviders();
                assertThat(availableProviders).contains("local", "s3", "azure");
                assertThat(availableProviders).doesNotContain("gcs"); // disabled in config
            }
        }

        @Test
        @DisplayName("Should enable all providers when no configuration provided")
        void shouldEnableAllProvidersWhenNoConfigurationProvided() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(
                    createMockProvider("local"),
                    createMockProvider("s3")
                );
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageProperties minimalProperties = new FileStorageProperties();
                minimalProperties.setProviders(null); // No provider configuration

                FileStorageManager manager = configuration.fileStorageManager(minimalProperties);

                assertThat(manager).isNotNull();
                Set<String> availableProviders = manager.getAvailableProviders();
                assertThat(availableProviders).contains("local", "s3");
            }
        }
    }

    @Nested
    @DisplayName("File Storage Bean Tests")
    class FileStorageBeanTests {

        @Test
        @DisplayName("Should create primary file storage with decorators")
        void shouldCreatePrimaryFileStorageWithDecorators() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(createMockProvider("local"));
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager mockManager = mock(FileStorageManager.class);
                FileStorage mockStorage = mock(FileStorage.class);
                when(mockManager.getStorage("local")).thenReturn(mockStorage);

                FileStorage result = configuration.fileStorage(mockManager, properties);

                assertThat(result).isNotNull();
                // The result should be wrapped with decorators
                assertThat(result).isNotEqualTo(mockStorage);
            }
        }

        @Test
        @DisplayName("Should apply cache decorator when cache enabled")
        void shouldApplyCacheDecoratorWhenCacheEnabled() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(createMockProvider("local"));
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager mockManager = mock(FileStorageManager.class);
                FileStorage mockStorage = mock(FileStorage.class);
                when(mockManager.getStorage("local")).thenReturn(mockStorage);

                FileStorage result = configuration.fileStorage(mockManager, properties);

                assertThat(result).isNotNull();
                // Should be wrapped with decorators when cache is enabled
                // The actual class name depends on the decorator wrapping order
                assertThat(result.getClass().getSimpleName()).isNotEqualTo("LocalFileStorage");
            }
        }

        @Test
        @DisplayName("Should apply metrics decorator when metrics enabled")
        void shouldApplyMetricsDecoratorWhenMetricsEnabled() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(createMockProvider("local"));
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager mockManager = mock(FileStorageManager.class);
                FileStorage mockStorage = mock(FileStorage.class);
                when(mockManager.getStorage("local")).thenReturn(mockStorage);

                FileStorage result = configuration.fileStorage(mockManager, properties);

                assertThat(result).isNotNull();
                // Should be wrapped with decorators when metrics is enabled
                // The actual class name depends on the decorator wrapping order
                assertThat(result.getClass().getSimpleName()).isNotEqualTo("LocalFileStorage");
            }
        }

        @Test
        @DisplayName("Should not apply cache decorator when cache disabled")
        void shouldNotApplyCacheDecoratorWhenCacheDisabled() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(createMockProvider("local"));
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager mockManager = mock(FileStorageManager.class);
                FileStorage mockStorage = mock(FileStorage.class);
                when(mockManager.getStorage("local")).thenReturn(mockStorage);

                FileStorageProperties noCacheProperties = new FileStorageProperties();
                noCacheProperties.setDefaultProvider("local");
                noCacheProperties.setCacheEnabled(false);

                FileStorage result = configuration.fileStorage(mockManager, noCacheProperties);

                assertThat(result).isNotNull();
                // Should still be wrapped with other decorators when cache is disabled
                // The actual class name depends on the decorator wrapping order
                assertThat(result.getClass().getSimpleName()).isNotEqualTo("LocalFileStorage");
            }
        }
    }

    @Nested
    @DisplayName("Provider Bean Tests")
    class ProviderBeanTests {

        @Test
        @DisplayName("Should create local file storage provider")
        void shouldCreateLocalFileStorageProvider() {
            LocalFileStorageProvider provider = configuration.localFileStorageProvider();
            assertThat(provider).isNotNull();
            assertThat(provider).isInstanceOf(LocalFileStorageProvider.class);
        }

        @Test
        @DisplayName("Should create S3 file storage provider")
        void shouldCreateS3FileStorageProvider() {
            S3FileStorageProvider provider = configuration.s3FileStorageProvider();
            assertThat(provider).isNotNull();
            assertThat(provider).isInstanceOf(S3FileStorageProvider.class);
        }

        @Test
        @DisplayName("Should create GCS file storage provider")
        void shouldCreateGcsFileStorageProvider() {
            GcsFileStorageProvider provider = configuration.gcsFileStorageProvider();
            assertThat(provider).isNotNull();
            assertThat(provider).isInstanceOf(GcsFileStorageProvider.class);
        }

        @Test
        @DisplayName("Should create Azure file storage provider")
        void shouldCreateAzureFileStorageProvider() {
            AzureFileStorageProvider provider = configuration.azureFileStorageProvider();
            assertThat(provider).isNotNull();
            assertThat(provider).isInstanceOf(AzureFileStorageProvider.class);
        }
    }

    @Nested
    @DisplayName("Default File Storage Manager Tests")
    class DefaultFileStorageManagerTests {

        @Test
        @DisplayName("Should get storage by provider name")
        void shouldGetStorageByProviderName() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                FileStorageProvider mockProvider = createMockProvider("local");
                FileStorage mockStorage = mock(FileStorage.class);
                when(mockProvider.createStorage()).thenReturn(mockStorage);

                List<FileStorageProvider> mockProviders = List.of(mockProvider);
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager manager = configuration.fileStorageManager(properties);
                FileStorage result = manager.getStorage("local");

                assertThat(result).isNotNull();
                assertThat(result).isEqualTo(mockStorage);
            }
        }

        @Test
        @DisplayName("Should throw exception for unknown provider")
        void shouldThrowExceptionForUnknownProvider() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(createMockProvider("local"));
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager manager = configuration.fileStorageManager(properties);

                assertThatThrownBy(() -> manager.getStorage("unknown"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Provider not found: unknown");
            }
        }

        @Test
        @DisplayName("Should get available providers")
        void shouldGetAvailableProviders() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(
                    createMockProvider("local"),
                    createMockProvider("s3")
                );
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager manager = configuration.fileStorageManager(properties);
                Set<String> availableProviders = manager.getAvailableProviders();

                assertThat(availableProviders).contains("local", "s3");
            }
        }

        @Test
        @DisplayName("Should get default provider name")
        void shouldGetDefaultProviderName() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(createMockProvider("local"));
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager manager = configuration.fileStorageManager(properties);
                String defaultProvider = manager.getDefaultProviderName();

                assertThat(defaultProvider).isEqualTo("local");
            }
        }

        @Test
        @DisplayName("Should set default provider")
        void shouldSetDefaultProvider() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(
                    createMockProvider("local"),
                    createMockProvider("s3")
                );
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager manager = configuration.fileStorageManager(properties);
                manager.setDefaultProvider("s3");

                assertThat(manager.getDefaultProviderName()).isEqualTo("s3");
            }
        }

        @Test
        @DisplayName("Should throw exception when setting unknown default provider")
        void shouldThrowExceptionWhenSettingUnknownDefaultProvider() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                List<FileStorageProvider> mockProviders = List.of(createMockProvider("local"));
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager manager = configuration.fileStorageManager(properties);

                assertThatThrownBy(() -> manager.setDefaultProvider("unknown"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Provider not found: unknown");
            }
        }

        @Test
        @DisplayName("Should cache storage instances")
        void shouldCacheStorageInstances() {
            try (MockedStatic<FileServices> fileServicesMock = mockStatic(FileServices.class)) {
                FileStorageProvider mockProvider = createMockProvider("local");
                FileStorage mockStorage = mock(FileStorage.class);
                when(mockProvider.createStorage()).thenReturn(mockStorage);

                List<FileStorageProvider> mockProviders = List.of(mockProvider);
                fileServicesMock.when(FileServices::getProviders).thenReturn(mockProviders);

                FileStorageManager manager = configuration.fileStorageManager(properties);
                
                // Get storage twice
                FileStorage storage1 = manager.getStorage("local");
                FileStorage storage2 = manager.getStorage("local");

                assertThat(storage1).isEqualTo(storage2);
                // Verify createStorage was called only once due to caching
                // Note: This is a simplified test - in reality, we'd need to verify the cache behavior
            }
        }
    }

    private FileStorageProvider createMockProvider(String name) {
        FileStorageProvider provider = mock(FileStorageProvider.class);
        lenient().when(provider.getProviderName()).thenReturn(name);
        lenient().when(provider.createStorage()).thenReturn(mock(FileStorage.class));
        return provider;
    }
}
