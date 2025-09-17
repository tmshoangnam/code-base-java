package io.github.base.starter.file.autoconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link FileStorageProperties}.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("FileStorageProperties Tests")
class FileStoragePropertiesTest {

    private FileStorageProperties properties;

    @BeforeEach
    void setUp() {
        properties = new FileStorageProperties();
    }

    @Nested
    @DisplayName("Main Properties Tests")
    class MainPropertiesTests {

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertThat(properties.getDefaultProvider()).isEqualTo("local");
            assertThat(properties.isEnabled()).isTrue();
            assertThat(properties.isMetricsEnabled()).isTrue();
            assertThat(properties.isCacheEnabled()).isTrue();
            assertThat(properties.getCacheTtlSeconds()).isEqualTo(300L);
            assertThat(properties.getMaxCacheSize()).isEqualTo(1000);
            assertThat(properties.getProviders()).isNull();
        }

        @Test
        @DisplayName("Should set and get default provider")
        void shouldSetAndGetDefaultProvider() {
            String provider = "s3";
            properties.setDefaultProvider(provider);
            assertThat(properties.getDefaultProvider()).isEqualTo(provider);
        }

        @Test
        @DisplayName("Should set and get enabled flag")
        void shouldSetAndGetEnabledFlag() {
            properties.setEnabled(false);
            assertThat(properties.isEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should set and get metrics enabled flag")
        void shouldSetAndGetMetricsEnabledFlag() {
            properties.setMetricsEnabled(false);
            assertThat(properties.isMetricsEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should set and get cache enabled flag")
        void shouldSetAndGetCacheEnabledFlag() {
            properties.setCacheEnabled(false);
            assertThat(properties.isCacheEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should set and get cache TTL seconds")
        void shouldSetAndGetCacheTtlSeconds() {
            long ttlSeconds = 600L;
            properties.setCacheTtlSeconds(ttlSeconds);
            assertThat(properties.getCacheTtlSeconds()).isEqualTo(ttlSeconds);
        }

        @Test
        @DisplayName("Should set and get max cache size")
        void shouldSetAndGetMaxCacheSize() {
            int maxCacheSize = 2000;
            properties.setMaxCacheSize(maxCacheSize);
            assertThat(properties.getMaxCacheSize()).isEqualTo(maxCacheSize);
        }

        @Test
        @DisplayName("Should set and get providers")
        void shouldSetAndGetProviders() {
            Map<String, FileStorageProperties.ProviderConfig> providers = new HashMap<>();
            FileStorageProperties.ProviderConfig config = new FileStorageProperties.ProviderConfig();
            config.setType("s3");
            config.setEnabled(true);
            config.setPriority(1);
            providers.put("s3", config);
            
            properties.setProviders(providers);
            assertThat(properties.getProviders()).isEqualTo(providers);
        }
    }

    @Nested
    @DisplayName("ProviderConfig Tests")
    class ProviderConfigTests {

        private FileStorageProperties.ProviderConfig providerConfig;

        @BeforeEach
        void setUp() {
            providerConfig = new FileStorageProperties.ProviderConfig();
        }

        @Test
        @DisplayName("Should have default values")
        void shouldHaveDefaultValues() {
            assertThat(providerConfig.getType()).isNull();
            assertThat(providerConfig.getProperties()).isNull();
            assertThat(providerConfig.isEnabled()).isTrue();
            assertThat(providerConfig.getPriority()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should set and get type")
        void shouldSetAndGetType() {
            String type = "s3";
            providerConfig.setType(type);
            assertThat(providerConfig.getType()).isEqualTo(type);
        }

        @Test
        @DisplayName("Should set and get properties")
        void shouldSetAndGetProperties() {
            Map<String, String> properties = new HashMap<>();
            properties.put("bucket", "my-bucket");
            properties.put("region", "us-east-1");
            
            providerConfig.setProperties(properties);
            assertThat(providerConfig.getProperties()).isEqualTo(properties);
        }

        @Test
        @DisplayName("Should set and get enabled flag")
        void shouldSetAndGetEnabledFlag() {
            providerConfig.setEnabled(false);
            assertThat(providerConfig.isEnabled()).isFalse();
        }

        @Test
        @DisplayName("Should set and get priority")
        void shouldSetAndGetPriority() {
            int priority = 5;
            providerConfig.setPriority(priority);
            assertThat(providerConfig.getPriority()).isEqualTo(priority);
        }

        @Test
        @DisplayName("Should handle null properties")
        void shouldHandleNullProperties() {
            providerConfig.setProperties(null);
            assertThat(providerConfig.getProperties()).isNull();
        }

        @Test
        @DisplayName("Should handle empty properties")
        void shouldHandleEmptyProperties() {
            Map<String, String> emptyProperties = new HashMap<>();
            providerConfig.setProperties(emptyProperties);
            assertThat(providerConfig.getProperties()).isEqualTo(emptyProperties);
            assertThat(providerConfig.getProperties()).isEmpty();
        }

        @Test
        @DisplayName("Should handle negative priority")
        void shouldHandleNegativePriority() {
            int negativePriority = -1;
            providerConfig.setPriority(negativePriority);
            assertThat(providerConfig.getPriority()).isEqualTo(negativePriority);
        }

        @Test
        @DisplayName("Should handle zero priority")
        void shouldHandleZeroPriority() {
            int zeroPriority = 0;
            providerConfig.setPriority(zeroPriority);
            assertThat(providerConfig.getPriority()).isEqualTo(zeroPriority);
        }

        @Test
        @DisplayName("Should handle high priority")
        void shouldHandleHighPriority() {
            int highPriority = 100;
            providerConfig.setPriority(highPriority);
            assertThat(providerConfig.getPriority()).isEqualTo(highPriority);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should create complete configuration")
        void shouldCreateCompleteConfiguration() {
            // Create main properties
            FileStorageProperties mainProperties = new FileStorageProperties();
            mainProperties.setDefaultProvider("s3");
            mainProperties.setEnabled(true);
            mainProperties.setMetricsEnabled(true);
            mainProperties.setCacheEnabled(true);
            mainProperties.setCacheTtlSeconds(600L);
            mainProperties.setMaxCacheSize(2000);

            // Create provider configurations
            Map<String, FileStorageProperties.ProviderConfig> providers = new HashMap<>();
            
            // S3 provider
            FileStorageProperties.ProviderConfig s3Config = new FileStorageProperties.ProviderConfig();
            s3Config.setType("s3");
            s3Config.setEnabled(true);
            s3Config.setPriority(1);
            Map<String, String> s3Properties = new HashMap<>();
            s3Properties.put("bucket", "my-s3-bucket");
            s3Properties.put("region", "us-west-2");
            s3Config.setProperties(s3Properties);
            providers.put("s3", s3Config);

            // Local provider
            FileStorageProperties.ProviderConfig localConfig = new FileStorageProperties.ProviderConfig();
            localConfig.setType("local");
            localConfig.setEnabled(true);
            localConfig.setPriority(0);
            Map<String, String> localProperties = new HashMap<>();
            localProperties.put("basePath", "/tmp/files");
            localConfig.setProperties(localProperties);
            providers.put("local", localConfig);

            mainProperties.setProviders(providers);

            // Verify main properties
            assertThat(mainProperties.getDefaultProvider()).isEqualTo("s3");
            assertThat(mainProperties.isEnabled()).isTrue();
            assertThat(mainProperties.isMetricsEnabled()).isTrue();
            assertThat(mainProperties.isCacheEnabled()).isTrue();
            assertThat(mainProperties.getCacheTtlSeconds()).isEqualTo(600L);
            assertThat(mainProperties.getMaxCacheSize()).isEqualTo(2000);

            // Verify providers
            assertThat(mainProperties.getProviders()).hasSize(2);
            assertThat(mainProperties.getProviders()).containsKey("s3");
            assertThat(mainProperties.getProviders()).containsKey("local");

            // Verify S3 provider
            FileStorageProperties.ProviderConfig retrievedS3Config = mainProperties.getProviders().get("s3");
            assertThat(retrievedS3Config.getType()).isEqualTo("s3");
            assertThat(retrievedS3Config.isEnabled()).isTrue();
            assertThat(retrievedS3Config.getPriority()).isEqualTo(1);
            assertThat(retrievedS3Config.getProperties()).containsEntry("bucket", "my-s3-bucket");
            assertThat(retrievedS3Config.getProperties()).containsEntry("region", "us-west-2");

            // Verify local provider
            FileStorageProperties.ProviderConfig retrievedLocalConfig = mainProperties.getProviders().get("local");
            assertThat(retrievedLocalConfig.getType()).isEqualTo("local");
            assertThat(retrievedLocalConfig.isEnabled()).isTrue();
            assertThat(retrievedLocalConfig.getPriority()).isEqualTo(0);
            assertThat(retrievedLocalConfig.getProperties()).containsEntry("basePath", "/tmp/files");
        }

        @Test
        @DisplayName("Should handle null providers")
        void shouldHandleNullProviders() {
            properties.setProviders(null);
            assertThat(properties.getProviders()).isNull();
        }

        @Test
        @DisplayName("Should handle empty providers map")
        void shouldHandleEmptyProvidersMap() {
            Map<String, FileStorageProperties.ProviderConfig> emptyProviders = new HashMap<>();
            properties.setProviders(emptyProviders);
            assertThat(properties.getProviders()).isEqualTo(emptyProviders);
            assertThat(properties.getProviders()).isEmpty();
        }
    }
}
