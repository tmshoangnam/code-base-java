package io.github.base.aws.api.secrets;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Contract for AWS Secrets Manager operations.
 *
 * <p>This interface defines the standard operations for interacting with AWS Secrets Manager,
 * including secret creation, retrieval, update, and deletion.
 *
 * <p>All operations are designed to be thread-safe and can be used in concurrent environments.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface SecretsManagerOperations {

    /**
     * Creates a secret.
     *
     * @param secretName the name of the secret
     * @param secretValue the value of the secret
     * @return secret information or null if creation failed
     * @throws IllegalArgumentException if secretName or secretValue is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    SecretsManagerSecretInfo createSecret(String secretName, String secretValue);

    /**
     * Creates a secret with description.
     *
     * @param secretName the name of the secret
     * @param secretValue the value of the secret
     * @param description the description of the secret
     * @return secret information or null if creation failed
     * @throws IllegalArgumentException if secretName or secretValue is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    SecretsManagerSecretInfo createSecret(String secretName, String secretValue, String description);

    /**
     * Creates a secret with all options.
     *
     * @param secretName the name of the secret
     * @param secretValue the value of the secret
     * @param description the description of the secret
     * @param kmsKeyId the KMS key ID for encryption
     * @param tags tags for the secret
     * @return secret information or null if creation failed
     * @throws IllegalArgumentException if secretName or secretValue is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    SecretsManagerSecretInfo createSecret(String secretName, String secretValue, String description,
                                         String kmsKeyId, Map<String, String> tags);

    /**
     * Gets a secret value.
     *
     * @param secretName the name of the secret
     * @return the secret value or null if not found
     * @throws IllegalArgumentException if secretName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    String getSecretValue(String secretName);

    /**
     * Gets a secret value by version ID.
     *
     * @param secretName the name of the secret
     * @param versionId the version ID of the secret
     * @return the secret value or null if not found
     * @throws IllegalArgumentException if secretName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    String getSecretValue(String secretName, String versionId);

    /**
     * Gets a secret value by version stage.
     *
     * @param secretName the name of the secret
     * @param versionStage the version stage of the secret
     * @return the secret value or null if not found
     * @throws IllegalArgumentException if secretName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    String getSecretValueByVersionStage(String secretName, String versionStage);

    /**
     * Updates a secret value.
     *
     * @param secretName the name of the secret
     * @param secretValue the new value of the secret
     * @return true if secret was updated successfully, false otherwise
     * @throws IllegalArgumentException if secretName or secretValue is null or empty
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateSecretValue(String secretName, String secretValue);

    /**
     * Updates a secret value with version ID.
     *
     * @param secretName the name of the secret
     * @param secretValue the new value of the secret
     * @param versionId the version ID of the secret
     * @return true if secret was updated successfully, false otherwise
     * @throws IllegalArgumentException if secretName or secretValue is null or empty
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateSecretValue(String secretName, String secretValue, String versionId);

    /**
     * Updates a secret value with all options.
     *
     * @param secretName the name of the secret
     * @param secretValue the new value of the secret
     * @param versionId the version ID of the secret
     * @param description the new description of the secret
     * @param kmsKeyId the new KMS key ID for encryption
     * @return true if secret was updated successfully, false otherwise
     * @throws IllegalArgumentException if secretName or secretValue is null or empty
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateSecretValue(String secretName, String secretValue, String versionId,
                             String description, String kmsKeyId);

    /**
     * Deletes a secret.
     *
     * @param secretName the name of the secret
     * @return true if secret was deleted successfully, false otherwise
     * @throws IllegalArgumentException if secretName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteSecret(String secretName);

    /**
     * Deletes a secret with recovery window.
     *
     * @param secretName the name of the secret
     * @param recoveryWindowInDays recovery window in days
     * @return true if secret was deleted successfully, false otherwise
     * @throws IllegalArgumentException if secretName is null or empty, or recoveryWindowInDays < 0
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteSecret(String secretName, int recoveryWindowInDays);

    /**
     * Deletes a secret immediately.
     *
     * @param secretName the name of the secret
     * @return true if secret was deleted successfully, false otherwise
     * @throws IllegalArgumentException if secretName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteSecretImmediately(String secretName);

    /**
     * Restores a deleted secret.
     *
     * @param secretName the name of the secret
     * @return true if secret was restored successfully, false otherwise
     * @throws IllegalArgumentException if secretName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if restoration fails
     */
    boolean restoreSecret(String secretName);

    /**
     * Gets secret information.
     *
     * @param secretName the name of the secret
     * @return secret information or null if secret not found
     * @throws IllegalArgumentException if secretName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    SecretsManagerSecretInfo getSecretInfo(String secretName);

    /**
     * Lists all secrets.
     *
     * @return list of secret information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<SecretsManagerSecretInfo> listSecrets();

    /**
     * Lists secrets with a name prefix.
     *
     * @param prefix the prefix to filter secret names
     * @return list of secret information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<SecretsManagerSecretInfo> listSecrets(String prefix);

    /**
     * Lists secrets with pagination.
     *
     * @param maxResults maximum number of results to return
     * @param nextToken token for pagination
     * @return map containing secret information list and next token
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    Map<String, Object> listSecrets(int maxResults, String nextToken);

    /**
     * Lists secret versions.
     *
     * @param secretName the name of the secret
     * @return list of version information
     * @throws IllegalArgumentException if secretName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<Map<String, Object>> listSecretVersions(String secretName);

    /**
     * Creates a new version of a secret.
     *
     * @param secretName the name of the secret
     * @param secretValue the new value of the secret
     * @return version information or null if creation failed
     * @throws IllegalArgumentException if secretName or secretValue is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    Map<String, Object> createSecretVersion(String secretName, String secretValue);

    /**
     * Creates a new version of a secret with version stages.
     *
     * @param secretName the name of the secret
     * @param secretValue the new value of the secret
     * @param versionStages list of version stages
     * @return version information or null if creation failed
     * @throws IllegalArgumentException if secretName or secretValue is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    Map<String, Object> createSecretVersion(String secretName, String secretValue, List<String> versionStages);

    /**
     * Updates a secret version.
     *
     * @param secretName the name of the secret
     * @param versionId the version ID of the secret
     * @param versionStages list of version stages
     * @return true if version was updated successfully, false otherwise
     * @throws IllegalArgumentException if secretName or versionId is null or empty
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateSecretVersion(String secretName, String versionId, List<String> versionStages);

    /**
     * Deletes a secret version.
     *
     * @param secretName the name of the secret
     * @param versionId the version ID of the secret
     * @return true if version was deleted successfully, false otherwise
     * @throws IllegalArgumentException if secretName or versionId is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteSecretVersion(String secretName, String versionId);

    /**
     * Gets secret tags.
     *
     * @param secretName the name of the secret
     * @return map of secret tags or null if secret not found
     * @throws IllegalArgumentException if secretName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    Map<String, String> getSecretTags(String secretName);

    /**
     * Updates secret tags.
     *
     * @param secretName the name of the secret
     * @param tags new tags for the secret
     * @return true if tags were updated successfully, false otherwise
     * @throws IllegalArgumentException if secretName is null or empty, or tags is null
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateSecretTags(String secretName, Map<String, String> tags);

    /**
     * Deletes secret tags.
     *
     * @param secretName the name of the secret
     * @param tagKeys list of tag keys to delete
     * @return true if tags were deleted successfully, false otherwise
     * @throws IllegalArgumentException if secretName is null or empty, or tagKeys is null
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteSecretTags(String secretName, List<String> tagKeys);

    /**
     * Asynchronously gets a secret value.
     *
     * @param secretName the name of the secret
     * @return CompletableFuture containing the secret value
     */
    CompletableFuture<String> getSecretValueAsync(String secretName);

    /**
     * Asynchronously creates a secret.
     *
     * @param secretName the name of the secret
     * @param secretValue the value of the secret
     * @return CompletableFuture containing secret information
     */
    CompletableFuture<SecretsManagerSecretInfo> createSecretAsync(String secretName, String secretValue);

    /**
     * Asynchronously updates a secret value.
     *
     * @param secretName the name of the secret
     * @param secretValue the new value of the secret
     * @return CompletableFuture containing success status
     */
    CompletableFuture<Boolean> updateSecretValueAsync(String secretName, String secretValue);

    /**
     * Asynchronously deletes a secret.
     *
     * @param secretName the name of the secret
     * @return CompletableFuture containing success status
     */
    CompletableFuture<Boolean> deleteSecretAsync(String secretName);

    /**
     * Gets the Secrets Manager operations capabilities.
     *
     * @return map of capability flags
     */
    Map<String, Boolean> getCapabilities();
}
