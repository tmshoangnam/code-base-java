package io.github.base.aws.api.common;

import java.util.Objects;

/**
 * Immutable record representing AWS credentials.
 * 
 * <p>This record contains the essential information for authenticating with AWS services,
 * including access key ID, secret access key, and session token.
 * 
 * @param accessKeyId the AWS access key ID
 * @param secretAccessKey the AWS secret access key
 * @param sessionToken the AWS session token (for temporary credentials)
 * @param region the AWS region
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record AwsCredentials(
    String accessKeyId,
    String secretAccessKey,
    String sessionToken,
    String region
) {
    
    /**
     * Creates AWS credentials with access key ID and secret access key.
     * 
     * @param accessKeyId the AWS access key ID
     * @param secretAccessKey the AWS secret access key
     * @return new AwsCredentials instance
     * @throws IllegalArgumentException if accessKeyId or secretAccessKey is null or empty
     */
    public static AwsCredentials of(String accessKeyId, String secretAccessKey) {
        if (accessKeyId == null || accessKeyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Access key ID cannot be null or empty");
        }
        if (secretAccessKey == null || secretAccessKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret access key cannot be null or empty");
        }
        
        return new AwsCredentials(
            accessKeyId.trim(),
            secretAccessKey.trim(),
            null,
            null
        );
    }
    
    /**
     * Creates AWS credentials with access key ID, secret access key, and region.
     * 
     * @param accessKeyId the AWS access key ID
     * @param secretAccessKey the AWS secret access key
     * @param region the AWS region
     * @return new AwsCredentials instance
     * @throws IllegalArgumentException if accessKeyId or secretAccessKey is null or empty
     */
    public static AwsCredentials of(String accessKeyId, String secretAccessKey, String region) {
        if (accessKeyId == null || accessKeyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Access key ID cannot be null or empty");
        }
        if (secretAccessKey == null || secretAccessKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret access key cannot be null or empty");
        }
        
        return new AwsCredentials(
            accessKeyId.trim(),
            secretAccessKey.trim(),
            null,
            region
        );
    }
    
    /**
     * Creates AWS credentials with all information.
     * 
     * @param accessKeyId the AWS access key ID
     * @param secretAccessKey the AWS secret access key
     * @param sessionToken the AWS session token
     * @param region the AWS region
     * @return new AwsCredentials instance
     * @throws IllegalArgumentException if accessKeyId or secretAccessKey is null or empty
     */
    public static AwsCredentials of(String accessKeyId, String secretAccessKey, String sessionToken, String region) {
        if (accessKeyId == null || accessKeyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Access key ID cannot be null or empty");
        }
        if (secretAccessKey == null || secretAccessKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret access key cannot be null or empty");
        }
        
        return new AwsCredentials(
            accessKeyId.trim(),
            secretAccessKey.trim(),
            sessionToken,
            region
        );
    }
    
    /**
     * Creates a copy of this AwsCredentials with updated region.
     * 
     * @param region new region
     * @return new AwsCredentials instance with updated region
     */
    public AwsCredentials withRegion(String region) {
        return new AwsCredentials(
            this.accessKeyId,
            this.secretAccessKey,
            this.sessionToken,
            region
        );
    }
    
    /**
     * Creates a copy of this AwsCredentials with updated session token.
     * 
     * @param sessionToken new session token
     * @return new AwsCredentials instance with updated session token
     */
    public AwsCredentials withSessionToken(String sessionToken) {
        return new AwsCredentials(
            this.accessKeyId,
            this.secretAccessKey,
            sessionToken,
            this.region
        );
    }
    
    /**
     * Checks if this credentials object has a session token.
     * 
     * @return true if session token is set, false otherwise
     */
    public boolean hasSessionToken() {
        return sessionToken != null && !sessionToken.trim().isEmpty();
    }
    
    /**
     * Checks if this credentials object has a region.
     * 
     * @return true if region is set, false otherwise
     */
    public boolean hasRegion() {
        return region != null && !region.trim().isEmpty();
    }
    
    /**
     * Checks if this credentials object represents temporary credentials.
     * 
     * @return true if session token is present, false otherwise
     */
    public boolean isTemporary() {
        return hasSessionToken();
    }
    
    /**
     * Checks if this credentials object represents permanent credentials.
     * 
     * @return true if session token is not present, false otherwise
     */
    public boolean isPermanent() {
        return !hasSessionToken();
    }
    
    /**
     * Gets the masked access key ID for logging purposes.
     * 
     * @return masked access key ID
     */
    public String getMaskedAccessKeyId() {
        if (accessKeyId == null || accessKeyId.length() < 8) {
            return "****";
        }
        return accessKeyId.substring(0, 4) + "****" + accessKeyId.substring(accessKeyId.length() - 4);
    }
    
    /**
     * Gets the masked secret access key for logging purposes.
     * 
     * @return masked secret access key
     */
    public String getMaskedSecretAccessKey() {
        if (secretAccessKey == null || secretAccessKey.length() < 8) {
            return "****";
        }
        return secretAccessKey.substring(0, 4) + "****" + secretAccessKey.substring(secretAccessKey.length() - 4);
    }
    
    /**
     * Gets the masked session token for logging purposes.
     * 
     * @return masked session token
     */
    public String getMaskedSessionToken() {
        if (sessionToken == null || sessionToken.length() < 8) {
            return "****";
        }
        return sessionToken.substring(0, 4) + "****" + sessionToken.substring(sessionToken.length() - 4);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AwsCredentials that = (AwsCredentials) obj;
        return Objects.equals(accessKeyId, that.accessKeyId) &&
               Objects.equals(secretAccessKey, that.secretAccessKey) &&
               Objects.equals(sessionToken, that.sessionToken) &&
               Objects.equals(region, that.region);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accessKeyId, secretAccessKey, sessionToken, region);
    }
    
    @Override
    public String toString() {
        return "AwsCredentials{" +
               "accessKeyId='" + getMaskedAccessKeyId() + '\'' +
               ", secretAccessKey='" + getMaskedSecretAccessKey() + '\'' +
               ", sessionToken='" + (hasSessionToken() ? getMaskedSessionToken() : "null") + '\'' +
               ", region='" + region + '\'' +
               '}';
    }
}
