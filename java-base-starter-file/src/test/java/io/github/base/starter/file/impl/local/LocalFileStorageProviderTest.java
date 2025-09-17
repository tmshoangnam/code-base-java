package io.github.base.starter.file.impl.local;

import io.github.base.file.api.storage.FileStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link LocalFileStorageProvider}.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LocalFileStorageProvider Tests")
class LocalFileStorageProviderTest {

    private LocalFileStorageProvider provider;

    @BeforeEach
    void setUp() {
        provider = new LocalFileStorageProvider();
    }

    @Nested
    @DisplayName("Basic Properties Tests")
    class BasicPropertiesTests {

        @Test
        @DisplayName("Should return correct provider name")
        void shouldReturnCorrectProviderName() {
            assertThat(provider.getProviderName()).isEqualTo("local");
        }

        @Test
        @DisplayName("Should return correct provider description")
        void shouldReturnCorrectProviderDescription() {
            assertThat(provider.getProviderDescription()).isEqualTo("Local filesystem storage provider");
        }

        @Test
        @DisplayName("Should return correct provider version")
        void shouldReturnCorrectProviderVersion() {
            assertThat(provider.getProviderVersion()).isEqualTo("1.0.5-SNAPSHOT");
        }

        @Test
        @DisplayName("Should return correct priority")
        void shouldReturnCorrectPriority() {
            assertThat(provider.getPriority()).isEqualTo(100);
        }
    }

    @Nested
    @DisplayName("Availability Tests")
    class AvailabilityTests {

        @Test
        @DisplayName("Should be available when filesystem is accessible")
        void shouldBeAvailableWhenFilesystemIsAccessible() {
            // This test assumes the filesystem is accessible
            // In a real test environment, you might want to mock the filesystem operations
            assertThat(provider.isAvailable()).isTrue();
        }
    }

    @Nested
    @DisplayName("Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Should return required configuration")
        void shouldReturnRequiredConfiguration() {
            Map<String, String> requiredConfig = provider.getRequiredConfiguration();
            
            assertThat(requiredConfig).hasSize(1);
            assertThat(requiredConfig).containsKey("base-path");
            assertThat(requiredConfig.get("base-path")).isEqualTo("Base path for file storage (e.g., /tmp/files)");
        }

        @Test
        @DisplayName("Should return optional configuration")
        void shouldReturnOptionalConfiguration() {
            Map<String, String> optionalConfig = provider.getOptionalConfiguration();
            
            assertThat(optionalConfig).hasSize(3);
            assertThat(optionalConfig).containsKey("create-directories");
            assertThat(optionalConfig).containsKey("file-permissions");
            assertThat(optionalConfig).containsKey("directory-permissions");
            
            assertThat(optionalConfig.get("create-directories")).isEqualTo("Whether to create directories automatically (default: true)");
            assertThat(optionalConfig.get("file-permissions")).isEqualTo("File permissions (default: 644)");
            assertThat(optionalConfig.get("directory-permissions")).isEqualTo("Directory permissions (default: 755)");
        }
    }

    @Nested
    @DisplayName("Capabilities Tests")
    class CapabilitiesTests {

        @Test
        @DisplayName("Should return correct capabilities")
        void shouldReturnCorrectCapabilities() {
            Map<String, Boolean> capabilities = provider.getCapabilities();
            
            assertThat(capabilities).hasSize(8);
            assertThat(capabilities).containsEntry("upload", true);
            assertThat(capabilities).containsEntry("download", true);
            assertThat(capabilities).containsEntry("delete", true);
            assertThat(capabilities).containsEntry("exists", true);
            assertThat(capabilities).containsEntry("metadata", true);
            assertThat(capabilities).containsEntry("list", true);
            assertThat(capabilities).containsEntry("streaming", true);
            assertThat(capabilities).containsEntry("concurrent", true);
        }
    }

    @Nested
    @DisplayName("Limitations Tests")
    class LimitationsTests {

        @Test
        @DisplayName("Should return correct limitations")
        void shouldReturnCorrectLimitations() {
            Map<String, String> limitations = provider.getLimitations();
            
            assertThat(limitations).hasSize(5);
            assertThat(limitations).containsEntry("max-file-size", "Limited by available disk space");
            assertThat(limitations).containsEntry("max-files", "Limited by available disk space");
            assertThat(limitations).containsEntry("replication", "false");
            assertThat(limitations).containsEntry("encryption", "false");
            assertThat(limitations).containsEntry("versioning", "false");
        }
    }

    @Nested
    @DisplayName("Storage Creation Tests")
    class StorageCreationTests {

        @Test
        @DisplayName("Should create storage with default configuration")
        void shouldCreateStorageWithDefaultConfiguration() {
            FileStorage storage = provider.createStorage();
            
            assertThat(storage).isNotNull();
            assertThat(storage).isInstanceOf(LocalFileStorage.class);
        }
    }

    @Nested
    @DisplayName("Configuration Validation Tests")
    class ConfigurationValidationTests {

        @Test
        @DisplayName("Should validate null configuration")
        void shouldValidateNullConfiguration() {
            List<String> errors = provider.validateConfiguration(null);
            
            assertThat(errors).isEmpty();
        }

        @Test
        @DisplayName("Should validate empty configuration")
        void shouldValidateEmptyConfiguration() {
            List<String> errors = provider.validateConfiguration(Map.of());
            
            assertThat(errors).isEmpty();
        }

        @Test
        @DisplayName("Should validate valid base path")
        void shouldValidateValidBasePath() {
            String tempDir = System.getProperty("java.io.tmpdir");
            Map<String, String> config = Map.of("base-path", tempDir);
            
            List<String> errors = provider.validateConfiguration(config);
            
            assertThat(errors).isEmpty();
        }

        @Test
        @DisplayName("Should validate non-existent base path with writable parent")
        void shouldValidateNonExistentBasePathWithWritableParent() {
            String tempDir = System.getProperty("java.io.tmpdir");
            String nonExistentPath = tempDir + "/file-storage-test";
            Map<String, String> config = Map.of("base-path", nonExistentPath);
            
            List<String> errors = provider.validateConfiguration(config);
            
            assertThat(errors).isEmpty();
        }

        @Test
        @DisplayName("Should validate invalid base path")
        void shouldValidateInvalidBasePath() {
            Map<String, String> config = Map.of("base-path", "/invalid/path/that/does/not/exist");
            
            List<String> errors = provider.validateConfiguration(config);
            
            assertThat(errors).isNotEmpty();
            assertThat(errors).anyMatch(error -> error.contains("Base path parent directory does not exist"));
        }

        @Test
        @DisplayName("Should validate file that is not a directory")
        void shouldValidateFileThatIsNotADirectory() throws Exception {
            Path tempFile = Files.createTempFile("test", ".txt");
            try {
                Map<String, String> config = Map.of("base-path", tempFile.toString());
                
                List<String> errors = provider.validateConfiguration(config);
                
                assertThat(errors).isNotEmpty();
                assertThat(errors).anyMatch(error -> error.contains("Base path is not a directory"));
            } finally {
                Files.deleteIfExists(tempFile);
            }
        }

        @Test
        @DisplayName("Should validate valid file permissions")
        void shouldValidateValidFilePermissions() {
            Map<String, String> config = Map.of(
                "file-permissions", "644",
                "directory-permissions", "755"
            );
            
            List<String> errors = provider.validateConfiguration(config);
            
            assertThat(errors).isEmpty();
        }

        @Test
        @DisplayName("Should validate invalid file permissions")
        void shouldValidateInvalidFilePermissions() {
            Map<String, String> config = Map.of(
                "file-permissions", "999",
                "directory-permissions", "abc"
            );
            
            List<String> errors = provider.validateConfiguration(config);
            
            assertThat(errors).hasSize(2);
            assertThat(errors).anyMatch(error -> error.contains("Invalid file permissions"));
            assertThat(errors).anyMatch(error -> error.contains("Invalid directory permissions"));
        }

        @Test
        @DisplayName("Should validate permissions with wrong length")
        void shouldValidatePermissionsWithWrongLength() {
            Map<String, String> config = Map.of(
                "file-permissions", "64",
                "directory-permissions", "7555"
            );
            
            List<String> errors = provider.validateConfiguration(config);
            
            assertThat(errors).hasSize(2);
            assertThat(errors).anyMatch(error -> error.contains("Invalid file permissions"));
            assertThat(errors).anyMatch(error -> error.contains("Invalid directory permissions"));
        }

        @Test
        @DisplayName("Should validate permissions with invalid characters")
        void shouldValidatePermissionsWithInvalidCharacters() {
            Map<String, String> config = Map.of(
                "file-permissions", "6a4",
                "directory-permissions", "7b5"
            );
            
            List<String> errors = provider.validateConfiguration(config);
            
            assertThat(errors).hasSize(2);
            assertThat(errors).anyMatch(error -> error.contains("Invalid file permissions"));
            assertThat(errors).anyMatch(error -> error.contains("Invalid directory permissions"));
        }

        @Test
        @DisplayName("Should validate permissions with out of range values")
        void shouldValidatePermissionsWithOutOfRangeValues() {
            Map<String, String> config = Map.of(
                "file-permissions", "844",
                "directory-permissions", "955"
            );
            
            List<String> errors = provider.validateConfiguration(config);
            
            assertThat(errors).hasSize(2);
            assertThat(errors).anyMatch(error -> error.contains("Invalid file permissions"));
            assertThat(errors).anyMatch(error -> error.contains("Invalid directory permissions"));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle configuration with null values")
        void shouldHandleConfigurationWithNullValues() {
            Map<String, String> config = new java.util.HashMap<>();
            config.put("base-path", null);
            config.put("file-permissions", null);
            config.put("directory-permissions", null);
            
            List<String> errors = provider.validateConfiguration(config);
            
            // Should not throw exception and should handle null values gracefully
            assertThat(errors).isNotNull();
        }

        @Test
        @DisplayName("Should handle configuration with empty strings")
        void shouldHandleConfigurationWithEmptyStrings() {
            Map<String, String> config = Map.of(
                "base-path", "",
                "file-permissions", "",
                "directory-permissions", ""
            );
            
            List<String> errors = provider.validateConfiguration(config);
            
            // Should handle empty strings gracefully
            assertThat(errors).isNotNull();
        }

        @Test
        @DisplayName("Should handle configuration with whitespace-only strings")
        void shouldHandleConfigurationWithWhitespaceOnlyStrings() {
            Map<String, String> config = Map.of(
                "base-path", "   ",
                "file-permissions", "   ",
                "directory-permissions", "   "
            );
            
            List<String> errors = provider.validateConfiguration(config);
            
            // Should handle whitespace-only strings gracefully
            assertThat(errors).isNotNull();
        }
    }
}
