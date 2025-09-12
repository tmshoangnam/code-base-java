package io.github.base.aws.api.secrets;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing Secrets Manager secret information.
 * 
 * <p>This record contains all the information about a secret in AWS Secrets Manager
 * including its name, ARN, description, and metadata.
 * 
 * @param secretName the name of the secret
 * @param secretArn the ARN of the secret
 * @param description the description of the secret
 * @param kmsKeyId the KMS key ID used for encryption
 * @param secretValue the value of the secret (if available)
 * @param versionId the current version ID
 * @param versionStages list of version stages
 * @param createdTimestamp timestamp when the secret was created
 * @param lastChangedTimestamp timestamp when the secret was last changed
 * @param lastAccessedTimestamp timestamp when the secret was last accessed
 * @param nextRotationTimestamp timestamp when the secret will next be rotated
 * @param rotationEnabled whether rotation is enabled
 * @param rotationLambdaArn the ARN of the rotation lambda function
 * @param rotationRules rotation rules
 * @param tags secret tags
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record SecretsManagerSecretInfo(
    String secretName,
    String secretArn,
    String description,
    String kmsKeyId,
    String secretValue,
    String versionId,
    List<String> versionStages,
    Instant createdTimestamp,
    Instant lastChangedTimestamp,
    Instant lastAccessedTimestamp,
    Instant nextRotationTimestamp,
    boolean rotationEnabled,
    String rotationLambdaArn,
    Map<String, Object> rotationRules,
    Map<String, String> tags
) {
    
    /**
     * Creates a Secrets Manager secret info with basic information.
     * 
     * @param secretName the name of the secret
     * @param secretArn the ARN of the secret
     * @return new SecretsManagerSecretInfo instance
     * @throws IllegalArgumentException if secretName or secretArn is null or empty
     */
    public static SecretsManagerSecretInfo of(String secretName, String secretArn) {
        if (secretName == null || secretName.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret name cannot be null or empty");
        }
        if (secretArn == null || secretArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret ARN cannot be null or empty");
        }
        
        Instant now = Instant.now();
        return new SecretsManagerSecretInfo(
            secretName.trim(),
            secretArn.trim(),
            null,
            null,
            null,
            null,
            List.of(),
            now,
            now,
            null,
            null,
            false,
            null,
            Map.of(),
            Map.of()
        );
    }
    
    /**
     * Creates a Secrets Manager secret info with all information.
     * 
     * @param secretName the name of the secret
     * @param secretArn the ARN of the secret
     * @param description the description of the secret
     * @param kmsKeyId the KMS key ID used for encryption
     * @param secretValue the value of the secret
     * @param versionId the current version ID
     * @param versionStages list of version stages
     * @param createdTimestamp timestamp when the secret was created
     * @param lastChangedTimestamp timestamp when the secret was last changed
     * @param lastAccessedTimestamp timestamp when the secret was last accessed
     * @param nextRotationTimestamp timestamp when the secret will next be rotated
     * @param rotationEnabled whether rotation is enabled
     * @param rotationLambdaArn the ARN of the rotation lambda function
     * @param rotationRules rotation rules
     * @param tags secret tags
     * @return new SecretsManagerSecretInfo instance
     * @throws IllegalArgumentException if secretName or secretArn is null or empty
     */
    public static SecretsManagerSecretInfo of(String secretName, String secretArn, String description,
                                             String kmsKeyId, String secretValue, String versionId,
                                             List<String> versionStages, Instant createdTimestamp,
                                             Instant lastChangedTimestamp, Instant lastAccessedTimestamp,
                                             Instant nextRotationTimestamp, boolean rotationEnabled,
                                             String rotationLambdaArn, Map<String, Object> rotationRules,
                                             Map<String, String> tags) {
        if (secretName == null || secretName.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret name cannot be null or empty");
        }
        if (secretArn == null || secretArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret ARN cannot be null or empty");
        }
        
        return new SecretsManagerSecretInfo(
            secretName.trim(),
            secretArn.trim(),
            description,
            kmsKeyId,
            secretValue,
            versionId,
            versionStages != null ? versionStages : List.of(),
            createdTimestamp != null ? createdTimestamp : Instant.now(),
            lastChangedTimestamp != null ? lastChangedTimestamp : Instant.now(),
            lastAccessedTimestamp,
            nextRotationTimestamp,
            rotationEnabled,
            rotationLambdaArn,
            rotationRules != null ? rotationRules : Map.of(),
            tags != null ? tags : Map.of()
        );
    }
    
    /**
     * Creates a copy of this SecretsManagerSecretInfo with updated secret value.
     * 
     * @param secretValue new secret value
     * @return new SecretsManagerSecretInfo instance with updated secret value
     */
    public SecretsManagerSecretInfo withSecretValue(String secretValue) {
        return new SecretsManagerSecretInfo(
            this.secretName,
            this.secretArn,
            this.description,
            this.kmsKeyId,
            secretValue,
            this.versionId,
            this.versionStages,
            this.createdTimestamp,
            this.lastChangedTimestamp,
            this.lastAccessedTimestamp,
            this.nextRotationTimestamp,
            this.rotationEnabled,
            this.rotationLambdaArn,
            this.rotationRules,
            this.tags
        );
    }
    
    /**
     * Creates a copy of this SecretsManagerSecretInfo with updated version information.
     * 
     * @param versionId new version ID
     * @param versionStages new version stages
     * @return new SecretsManagerSecretInfo instance with updated version information
     */
    public SecretsManagerSecretInfo withVersionInfo(String versionId, List<String> versionStages) {
        return new SecretsManagerSecretInfo(
            this.secretName,
            this.secretArn,
            this.description,
            this.kmsKeyId,
            this.secretValue,
            versionId,
            versionStages != null ? versionStages : List.of(),
            this.createdTimestamp,
            this.lastChangedTimestamp,
            this.lastAccessedTimestamp,
            this.nextRotationTimestamp,
            this.rotationEnabled,
            this.rotationLambdaArn,
            this.rotationRules,
            this.tags
        );
    }
    
    /**
     * Creates a copy of this SecretsManagerSecretInfo with updated tags.
     * 
     * @param tags new tags map
     * @return new SecretsManagerSecretInfo instance with updated tags
     */
    public SecretsManagerSecretInfo withTags(Map<String, String> tags) {
        return new SecretsManagerSecretInfo(
            this.secretName,
            this.secretArn,
            this.description,
            this.kmsKeyId,
            this.secretValue,
            this.versionId,
            this.versionStages,
            this.createdTimestamp,
            this.lastChangedTimestamp,
            this.lastAccessedTimestamp,
            this.nextRotationTimestamp,
            this.rotationEnabled,
            this.rotationLambdaArn,
            this.rotationRules,
            tags != null ? tags : Map.of()
        );
    }
    
    /**
     * Creates a copy of this SecretsManagerSecretInfo with updated rotation information.
     * 
     * @param rotationEnabled whether rotation is enabled
     * @param rotationLambdaArn the ARN of the rotation lambda function
     * @param rotationRules rotation rules
     * @return new SecretsManagerSecretInfo instance with updated rotation information
     */
    public SecretsManagerSecretInfo withRotationInfo(boolean rotationEnabled, String rotationLambdaArn, 
                                                    Map<String, Object> rotationRules) {
        return new SecretsManagerSecretInfo(
            this.secretName,
            this.secretArn,
            this.description,
            this.kmsKeyId,
            this.secretValue,
            this.versionId,
            this.versionStages,
            this.createdTimestamp,
            this.lastChangedTimestamp,
            this.lastAccessedTimestamp,
            this.nextRotationTimestamp,
            rotationEnabled,
            rotationLambdaArn,
            rotationRules != null ? rotationRules : Map.of(),
            this.tags
        );
    }
    
    /**
     * Gets a tag value by key.
     * 
     * @param key tag key
     * @return tag value or null if not found
     */
    public String getTag(String key) {
        return tags != null ? tags.get(key) : null;
    }
    
    /**
     * Checks if a tag exists.
     * 
     * @param key tag key
     * @return true if tag exists, false otherwise
     */
    public boolean hasTag(String key) {
        return tags != null && tags.containsKey(key);
    }
    
    /**
     * Checks if this secret has a description.
     * 
     * @return true if description is set, false otherwise
     */
    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }
    
    /**
     * Checks if this secret has a KMS key ID.
     * 
     * @return true if KMS key ID is set, false otherwise
     */
    public boolean hasKmsKeyId() {
        return kmsKeyId != null && !kmsKeyId.trim().isEmpty();
    }
    
    /**
     * Checks if this secret has a value.
     * 
     * @return true if secret value is set, false otherwise
     */
    public boolean hasSecretValue() {
        return secretValue != null && !secretValue.trim().isEmpty();
    }
    
    /**
     * Checks if this secret has version information.
     * 
     * @return true if version ID is set, false otherwise
     */
    public boolean hasVersionInfo() {
        return versionId != null && !versionId.trim().isEmpty();
    }
    
    /**
     * Checks if this secret has version stages.
     * 
     * @return true if version stages are set, false otherwise
     */
    public boolean hasVersionStages() {
        return versionStages != null && !versionStages.isEmpty();
    }
    
    /**
     * Checks if this secret has been accessed.
     * 
     * @return true if last accessed timestamp is set, false otherwise
     */
    public boolean hasBeenAccessed() {
        return lastAccessedTimestamp != null;
    }
    
    /**
     * Checks if this secret has rotation enabled.
     * 
     * @return true if rotation is enabled, false otherwise
     */
    public boolean hasRotationEnabled() {
        return rotationEnabled;
    }
    
    /**
     * Checks if this secret has a rotation lambda function.
     * 
     * @return true if rotation lambda ARN is set, false otherwise
     */
    public boolean hasRotationLambda() {
        return rotationLambdaArn != null && !rotationLambdaArn.trim().isEmpty();
    }
    
    /**
     * Checks if this secret has rotation rules.
     * 
     * @return true if rotation rules are set, false otherwise
     */
    public boolean hasRotationRules() {
        return rotationRules != null && !rotationRules.isEmpty();
    }
    
    /**
     * Checks if this secret has tags.
     * 
     * @return true if tags are set, false otherwise
     */
    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }
    
    /**
     * Gets the age of this secret in seconds.
     * 
     * @return age in seconds or -1 if createdTimestamp is null
     */
    public long getAgeInSeconds() {
        if (createdTimestamp == null) {
            return -1;
        }
        return Instant.now().getEpochSecond() - createdTimestamp.getEpochSecond();
    }
    
    /**
     * Gets the time since last change in seconds.
     * 
     * @return time since last change in seconds or -1 if lastChangedTimestamp is null
     */
    public long getTimeSinceLastChangeInSeconds() {
        if (lastChangedTimestamp == null) {
            return -1;
        }
        return Instant.now().getEpochSecond() - lastChangedTimestamp.getEpochSecond();
    }
    
    /**
     * Gets the time since last access in seconds.
     * 
     * @return time since last access in seconds or -1 if lastAccessedTimestamp is null
     */
    public long getTimeSinceLastAccessInSeconds() {
        if (lastAccessedTimestamp == null) {
            return -1;
        }
        return Instant.now().getEpochSecond() - lastAccessedTimestamp.getEpochSecond();
    }
    
    /**
     * Gets the time until next rotation in seconds.
     * 
     * @return time until next rotation in seconds or -1 if nextRotationTimestamp is null
     */
    public long getTimeUntilNextRotationInSeconds() {
        if (nextRotationTimestamp == null) {
            return -1;
        }
        return nextRotationTimestamp.getEpochSecond() - Instant.now().getEpochSecond();
    }
    
    /**
     * Checks if this secret is older than the specified number of seconds.
     * 
     * @param seconds the number of seconds to check against
     * @return true if secret is older than specified seconds, false otherwise
     */
    public boolean isOlderThan(int seconds) {
        return getAgeInSeconds() > seconds;
    }
    
    /**
     * Checks if this secret was changed more than the specified number of seconds ago.
     * 
     * @param seconds the number of seconds to check against
     * @return true if secret was changed more than specified seconds ago, false otherwise
     */
    public boolean wasChangedMoreThanSecondsAgo(int seconds) {
        return getTimeSinceLastChangeInSeconds() > seconds;
    }
    
    /**
     * Checks if this secret was accessed more than the specified number of seconds ago.
     * 
     * @param seconds the number of seconds to check against
     * @return true if secret was accessed more than specified seconds ago, false otherwise
     */
    public boolean wasAccessedMoreThanSecondsAgo(int seconds) {
        return getTimeSinceLastAccessInSeconds() > seconds;
    }
    
    /**
     * Checks if this secret needs rotation.
     * 
     * @return true if next rotation timestamp is in the past, false otherwise
     */
    public boolean needsRotation() {
        if (nextRotationTimestamp == null) {
            return false;
        }
        return nextRotationTimestamp.isBefore(Instant.now());
    }
    
    /**
     * Checks if this secret is in a specific version stage.
     * 
     * @param stage the version stage to check
     * @return true if secret is in the specified stage, false otherwise
     */
    public boolean isInVersionStage(String stage) {
        if (stage == null || versionStages == null) {
            return false;
        }
        return versionStages.contains(stage);
    }
    
    /**
     * Checks if this secret is in the AWSCURRENT stage.
     * 
     * @return true if secret is in AWSCURRENT stage, false otherwise
     */
    public boolean isCurrent() {
        return isInVersionStage("AWSCURRENT");
    }
    
    /**
     * Checks if this secret is in the AWSPENDING stage.
     * 
     * @return true if secret is in AWSPENDING stage, false otherwise
     */
    public boolean isPending() {
        return isInVersionStage("AWSPENDING");
    }
    
    /**
     * Checks if this secret is in the AWSPREVIOUS stage.
     * 
     * @return true if secret is in AWSPREVIOUS stage, false otherwise
     */
    public boolean isPrevious() {
        return isInVersionStage("AWSPREVIOUS");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecretsManagerSecretInfo that = (SecretsManagerSecretInfo) obj;
        return rotationEnabled == that.rotationEnabled &&
               Objects.equals(secretName, that.secretName) &&
               Objects.equals(secretArn, that.secretArn) &&
               Objects.equals(description, that.description) &&
               Objects.equals(kmsKeyId, that.kmsKeyId) &&
               Objects.equals(secretValue, that.secretValue) &&
               Objects.equals(versionId, that.versionId) &&
               Objects.equals(versionStages, that.versionStages) &&
               Objects.equals(createdTimestamp, that.createdTimestamp) &&
               Objects.equals(lastChangedTimestamp, that.lastChangedTimestamp) &&
               Objects.equals(lastAccessedTimestamp, that.lastAccessedTimestamp) &&
               Objects.equals(nextRotationTimestamp, that.nextRotationTimestamp) &&
               Objects.equals(rotationLambdaArn, that.rotationLambdaArn) &&
               Objects.equals(rotationRules, that.rotationRules) &&
               Objects.equals(tags, that.tags);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(secretName, secretArn, description, kmsKeyId, secretValue, versionId, 
                           versionStages, createdTimestamp, lastChangedTimestamp, lastAccessedTimestamp, 
                           nextRotationTimestamp, rotationEnabled, rotationLambdaArn, rotationRules, tags);
    }
    
    @Override
    public String toString() {
        return "SecretsManagerSecretInfo{" +
               "secretName='" + secretName + '\'' +
               ", secretArn='" + secretArn + '\'' +
               ", description='" + description + '\'' +
               ", kmsKeyId='" + kmsKeyId + '\'' +
               ", secretValue='" + (secretValue != null ? "[REDACTED]" : null) + '\'' +
               ", versionId='" + versionId + '\'' +
               ", versionStages=" + versionStages +
               ", createdTimestamp=" + createdTimestamp +
               ", lastChangedTimestamp=" + lastChangedTimestamp +
               ", lastAccessedTimestamp=" + lastAccessedTimestamp +
               ", nextRotationTimestamp=" + nextRotationTimestamp +
               ", rotationEnabled=" + rotationEnabled +
               ", rotationLambdaArn='" + rotationLambdaArn + '\'' +
               ", rotationRules=" + rotationRules +
               ", tags=" + tags +
               '}';
    }
}
