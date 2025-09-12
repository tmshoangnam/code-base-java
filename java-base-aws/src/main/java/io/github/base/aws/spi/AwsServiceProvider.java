package io.github.base.aws.spi;

import io.github.base.aws.api.config.AwsServiceConfig;
import java.util.Collections;

import java.util.Map;

/**
 * Service Provider Interface (SPI) for AWS service implementations.
 *
 * <p>This interface allows different AWS service implementations to be discovered
 * and loaded at runtime using the Java ServiceLoader mechanism. Implementations
 * should be registered in META-INF/services/io.github.base.aws.spi.AwsServiceProvider.
 *
 * <p>Each provider implementation should:
 * <ul>
 *   <li>Implement this interface</li>
 *   <li>Register itself in META-INF/services/io.github.base.aws.spi.AwsServiceProvider</li>
 *   <li>Provide a unique service name</li>
 *   <li>Handle service-specific configuration</li>
 * </ul>
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface AwsServiceProvider {

    /**
     * Gets the name of this AWS service provider.
     *
     * <p>The service name should be unique and descriptive (e.g., "s3", "sqs", "sns", "lambda", "dynamodb", "secrets-manager").
     * This name is used to identify and select the provider when configuring AWS services.
     *
     * @return the service name
     */
    String getServiceName();

    /**
     * Creates a new AWS service instance for this provider.
     *
     * <p>This method should create and configure a new AWS service instance
     * based on the provider's configuration. The configuration should be
     * provided through the provider's constructor or configuration mechanism.
     *
     * @param config the AWS service configuration
     * @return a new AWS service instance
     * @throws IllegalStateException if the provider cannot be initialized
     */
    Object createService(AwsServiceConfig config);

    /**
     * Checks if this provider is available and can be used.
     *
     * <p>This method should check if all required dependencies and configurations
     * are available for this provider to function properly.
     *
     * @return true if the provider is available, false otherwise
     */
    boolean isAvailable();

    /**
     * Gets the priority of this provider.
     *
     * <p>Providers with higher priority values will be preferred when multiple
     * providers are available for the same service. The default priority
     * is 0, and providers can return positive or negative values to indicate
     * their relative priority.
     *
     * @return the provider priority (higher values = higher priority)
     */
    default int getPriority() {
        return 0;
    }

    /**
     * Gets the capabilities of this provider.
     *
     * <p>This method should return a map of capability flags indicating what
     * features this provider supports. Common capabilities include:
     * <ul>
     *   <li>"async-operations" - supports asynchronous operations</li>
     *   <li>"batch-operations" - supports batch operations</li>
     *   <li>"pagination" - supports pagination</li>
     *   <li>"filtering" - supports filtering operations</li>
     *   <li>"sorting" - supports sorting operations</li>
     *   <li>"caching" - supports caching</li>
     *   <li>"monitoring" - supports monitoring and metrics</li>
     *   <li>"logging" - supports structured logging</li>
     * </ul>
     *
     * @return map of capability flags
     */
    default Map<String, Boolean> getCapabilities() {
        return Map.of(
            "async-operations", true,
            "batch-operations", false,
            "pagination", false,
            "filtering", false,
            "sorting", false,
            "caching", false,
            "monitoring", false,
            "logging", false
        );
    }

    /**
     * Gets the supported service versions for this provider.
     *
     * <p>This method should return the service versions that this provider can handle.
     * If null is returned, the provider supports all versions.
     *
     * @return set of supported service versions, or null if all versions are supported
     */
    default java.util.Set<String> getSupportedVersions() {
        return Collections.emptySet(); // Support all versions by default
    }

    /**
     * Gets the configuration requirements for this provider.
     *
     * <p>This method should return a map of required configuration keys and
     * their descriptions. This information can be used to validate provider
     * configuration before initialization.
     *
     * @return map of required configuration keys and their descriptions
     */
    default Map<String, String> getRequiredConfiguration() {
        return Map.of();
    }

    /**
     * Gets the optional configuration keys for this provider.
     *
     * <p>This method should return a map of optional configuration keys and
     * their descriptions. This information can be used to provide configuration
     * hints to users.
     *
     * @return map of optional configuration keys and their descriptions
     */
    default Map<String, String> getOptionalConfiguration() {
        return Map.of();
    }

    /**
     * Validates the configuration for this provider.
     *
     * <p>This method should validate the provided configuration and return
     * a list of validation errors. If the list is empty, the configuration
     * is valid.
     *
     * @param config the configuration to validate
     * @return list of validation errors (empty if valid)
     */
    default java.util.List<String> validateConfiguration(AwsServiceConfig config) {
        return java.util.List.of();
    }

    /**
     * Gets the provider version.
     *
     * @return the provider version
     */
    default String getVersion() {
        return "1.0.0";
    }

    /**
     * Gets the provider description.
     *
     * @return the provider description
     */
    default String getDescription() {
        return "AWS service provider for " + getServiceName();
    }

    /**
     * Gets the provider vendor.
     *
     * @return the provider vendor
     */
    default String getVendor() {
        return "java-base-team";
    }

    /**
     * Gets the provider contact information.
     *
     * @return the provider contact information
     */
    default String getContact() {
        return "https://github.com/java-base-team/java-base-aws";
    }

    /**
     * Gets the provider license information.
     *
     * @return the provider license information
     */
    default String getLicense() {
        return "MIT";
    }

    /**
     * Gets the provider documentation URL.
     *
     * @return the provider documentation URL
     */
    default String getDocumentationUrl() {
        return "https://github.com/java-base-team/java-base-aws/wiki";
    }

    /**
     * Gets the provider support URL.
     *
     * @return the provider support URL
     */
    default String getSupportUrl() {
        return "https://github.com/java-base-team/java-base-aws/issues";
    }
}
