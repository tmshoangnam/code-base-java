package io.github.base.file.spi;

import io.github.base.file.api.storage.FileStorage;
import java.util.Collections;

import java.util.List;
import java.util.Map;

/**
 * Service Provider Interface (SPI) for file storage implementations.
 *
 * <p>This interface allows different file storage implementations to be discovered
 * and loaded at runtime using the Java ServiceLoader mechanism. Implementations
 * should be registered in META-INF/services/io.github.base.file.spi.FileStorageProvider.
 *
 * <p>Each provider implementation should:
 * <ul>
 *   <li>Implement this interface</li>
 *   <li>Register itself in META-INF/services/io.github.base.file.spi.FileStorageProvider</li>
 *   <li>Provide a unique provider name</li>
 *   <li>Handle provider-specific configuration</li>
 * </ul>
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface FileStorageProvider {

    /**
     * Gets the name of this storage provider.
     *
     * <p>The provider name should be unique and descriptive (e.g., "local", "s3", "gcs", "azure").
     * This name is used to identify and select the provider when configuring storage.
     *
     * @return the provider name
     */
    String getProviderName();

    Map<String, String> getOptionalConfiguration();

    /**
     * Creates a new FileStorage instance for this provider.
     *
     * <p>This method should create and configure a new FileStorage instance
     * based on the provider's configuration. The configuration should be
     * provided through the provider's constructor or configuration mechanism.
     *
     * @return a new FileStorage instance
     * @throws IllegalStateException if the provider cannot be initialized
     */
    FileStorage createStorage();

    String getProviderDescription();

    String getProviderVersion();

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
     * providers are available for the same functionality. The default priority
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
     *   <li>"presigned-urls" - supports presigned URL generation</li>
     *   <li>"async-operations" - supports asynchronous operations</li>
     *   <li>"metadata" - supports file metadata operations</li>
     *   <li>"list-files" - supports file listing operations</li>
     *   <li>"checksum" - supports file checksum verification</li>
     * </ul>
     *
     * @return map of capability flags
     */
    default java.util.Map<String, Boolean> getCapabilities() {
        return java.util.Map.of(
            "presigned-urls", false,
            "async-operations", true,
            "metadata", true,
            "list-files", true,
            "checksum", false
        );
    }

    /**
     * Gets the supported file types for this provider.
     *
     * <p>This method should return the file types that this provider can handle.
     * If null is returned, the provider supports all file types.
     *
     * @return set of supported file types, or null if all types are supported
     */
    default java.util.Set<io.github.base.file.api.model.FileType> getSupportedFileTypes() {
        return Collections.emptySet(); // Support all file types by default
    }

    /**
     * Gets the maximum file size supported by this provider.
     *
     * <p>This method should return the maximum file size in bytes that this
     * provider can handle. If 0 is returned, there is no size limit.
     *
     * @return maximum file size in bytes, or 0 if no limit
     */
    default long getMaxFileSize() {
        return 0; // No size limit by default
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
    default java.util.Map<String, String> getRequiredConfiguration() {
        return java.util.Map.of();
    }

    Map<String, String> getLimitations();

    List<String> validateConfiguration(Map<String, String> configuration);
}
