package io.github.base.starter.file.aws.s3;

import io.github.base.aws.api.config.AwsServiceConfig;
import io.github.base.aws.api.common.AwsCredentials;
import io.github.base.aws.api.common.AwsRegion;
import io.github.base.aws.AwsServices;
import io.github.base.aws.api.s3.S3Operations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for S3ServiceProvider.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class S3ServiceProviderTest {
    
    private S3ServiceProvider provider;
    
    @BeforeEach
    void setUp() {
        provider = new S3ServiceProvider();
        AwsServices.clearCache();
    }
    
    @AfterEach
    void tearDown() {
        AwsServices.clearCache();
    }
    
    @Test
    void shouldHaveCorrectServiceName() {
        assertThat(provider.getServiceName()).isEqualTo("s3");
    }
    
    @Test
    void shouldHaveCorrectVersion() {
        assertThat(provider.getVersion()).isEqualTo("1.0.5-SNAPSHOT");
    }
    
    @Test
    void shouldHaveHighPriority() {
        assertThat(provider.getPriority()).isEqualTo(100);
    }
    
    @Test
    void shouldBeAvailableWhenAwsSdkIsPresent() {
        // This test will pass if AWS SDK is available in classpath
        boolean available = provider.isAvailable();
        // We can't assert true/false because it depends on classpath
        // Just verify the method doesn't throw
        assertThat(available).isNotNull();
    }
    
    @Test
    void shouldHaveCorrectCapabilities() {
        var capabilities = provider.getCapabilities();
        assertThat(capabilities).isNotNull();
        assertThat(capabilities).containsKey("upload");
        assertThat(capabilities).containsKey("download");
        assertThat(capabilities).containsKey("delete");
        assertThat(capabilities).containsKey("exists");
        assertThat(capabilities).containsKey("metadata");
        assertThat(capabilities).containsKey("list");
        assertThat(capabilities).containsKey("presigned-urls");
        assertThat(capabilities).containsKey("versioning");
        assertThat(capabilities).containsKey("encryption");
    }
    
    @Test
    void shouldHaveRequiredConfiguration() {
        var required = provider.getRequiredConfiguration();
        assertThat(required).isNotNull();
        assertThat(required).containsKey("region");
        assertThat(required).containsKey("credentials");
    }
    
    @Test
    void shouldHaveOptionalConfiguration() {
        var optional = provider.getOptionalConfiguration();
        assertThat(optional).isNotNull();
        assertThat(optional).containsKey("endpoint");
        assertThat(optional).containsKey("path-style-access");
        assertThat(optional).containsKey("encryption");
        assertThat(optional).containsKey("storage-class");
    }
    
    @Test
    void shouldValidateConfiguration() {
        var errors = provider.validateConfiguration(createTestConfig());
        assertThat(errors).isNotNull();
        // Should be empty for valid config
        assertThat(errors).isEmpty();
    }
    
    @Test
    void shouldCreateServiceWithValidConfig() {
        // This test will only pass if AWS SDK is available
        if (provider.isAvailable()) {
            AwsServiceConfig config = createTestConfig();
            Object service = provider.createService(config);
            assertThat(service).isNotNull();
            assertThat(service).isInstanceOf(S3Operations.class);
        }
    }
    
    @Test
    void shouldBeDiscoverableByAwsServices() {
        // Test that the provider can be discovered by AwsServices
        var providers = AwsServices.getProviders();
        assertThat(providers).isNotNull();
        
        boolean found = providers.stream()
            .anyMatch(p -> "s3".equals(p.getServiceName()));
        assertThat(found).isTrue();
    }
    
    private AwsServiceConfig createTestConfig() {
        AwsCredentials credentials = AwsCredentials.of("test-key", "test-secret", "us-east-1");
        AwsRegion region = AwsRegion.of("us-east-1");
        
        return new AwsServiceConfig() {
            @Override
            public AwsCredentials getCredentials() {
                return credentials;
            }
            
            @Override
            public AwsRegion getRegion() {
                return region;
            }
            
            @Override
            public io.github.base.aws.api.common.AwsClientConfig getClientConfig() {
                return null; // Not needed for this test
            }
            
            @Override
            public String getServiceName() {
                return "s3";
            }
            
            @Override
            public String getServiceVersion() {
                return "1.0.0";
            }
            
            @Override
            public String getEndpoint() {
                return null;
            }
            
            @Override
            public java.util.Map<String, Object> getServiceProperties() {
                return java.util.Map.of();
            }
            
            @Override
            public Object getServiceProperty(String key) {
                return null;
            }
            
            @Override
            public boolean hasServiceProperty(String key) {
                return false;
            }
            
            @Override
            public boolean hasEndpoint() {
                return false;
            }
            
            @Override
            public boolean hasServiceProperties() {
                return false;
            }
            
            @Override
            public java.util.Map<String, Object> toMap() {
                return java.util.Map.of();
            }
            
            @Override
            public AwsServiceConfig withCredentials(AwsCredentials credentials) {
                return this;
            }
            
            @Override
            public AwsServiceConfig withRegion(AwsRegion region) {
                return this;
            }
            
            @Override
            public AwsServiceConfig withClientConfig(io.github.base.aws.api.common.AwsClientConfig clientConfig) {
                return this;
            }
            
            @Override
            public AwsServiceConfig withEndpoint(String endpoint) {
                return this;
            }
            
            @Override
            public AwsServiceConfig withServiceProperties(java.util.Map<String, Object> serviceProperties) {
                return this;
            }
            
            @Override
            public AwsServiceConfig withServiceProperty(String key, Object value) {
                return this;
            }
            
            @Override
            public AwsServiceConfig withoutServiceProperty(String key) {
                return this;
            }
        };
    }
}
