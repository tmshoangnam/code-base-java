package io.github.base.starter.file.autoconfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

/**
 * Test to verify AWS SDK availability on classpath.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
class AwsSdkAvailabilityTest {

    @Test
    @DisplayName("Should have AWS SDK S3 on classpath")
    void shouldHaveAwsSdkS3OnClasspath() {
        assertThatCode(() -> {
            Class.forName("software.amazon.awssdk.services.s3.S3Client");
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should have AWS SDK Core on classpath")
    void shouldHaveAwsSdkCoreOnClasspath() {
        assertThatCode(() -> {
            Class.forName("software.amazon.awssdk.core.SdkClient");
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should have AWS SDK Auth on classpath")
    void shouldHaveAwsSdkAuthOnClasspath() {
        assertThatCode(() -> {
            Class.forName("software.amazon.awssdk.auth.credentials.AwsCredentialsProvider");
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should have AWS SDK Regions on classpath")
    void shouldHaveAwsSdkRegionsOnClasspath() {
        assertThatCode(() -> {
            Class.forName("software.amazon.awssdk.regions.Region");
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should be able to create S3Client instance")
    void shouldBeAbleToCreateS3ClientInstance() {
        assertThatCode(() -> {
            var s3Client = software.amazon.awssdk.services.s3.S3Client.builder().build();
            assertThat(s3Client).isNotNull();
            s3Client.close();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should have S3FileStorageProvider available")
    void shouldHaveS3FileStorageProviderAvailable() {
        var provider = new io.github.base.starter.file.impl.s3.S3FileStorageProvider();
        assertThat(provider.isAvailable()).isTrue();
    }

    @Test
    @DisplayName("Should have S3ServiceProvider available")
    void shouldHaveS3ServiceProviderAvailable() {
        var provider = new io.github.base.starter.file.aws.s3.S3ServiceProvider();
        assertThat(provider.isAvailable()).isTrue();
    }
}

