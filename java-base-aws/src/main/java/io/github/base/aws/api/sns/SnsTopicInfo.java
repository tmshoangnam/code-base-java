package io.github.base.aws.api.sns;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing SNS topic information.
 * 
 * <p>This record contains all the information about an SNS topic including
 * its ARN, name, attributes, and statistics.
 * 
 * @param topicName the name of the topic
 * @param topicArn the ARN of the topic
 * @param attributes topic attributes
 * @param subscriptionCount number of subscriptions
 * @param createdTimestamp timestamp when the topic was created
 * @param lastModifiedTimestamp timestamp when the topic was last modified
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record SnsTopicInfo(
    String topicName,
    String topicArn,
    Map<String, String> attributes,
    int subscriptionCount,
    Instant createdTimestamp,
    Instant lastModifiedTimestamp
) {
    
    /**
     * Creates an SNS topic info with basic information.
     * 
     * @param topicName the name of the topic
     * @param topicArn the ARN of the topic
     * @return new SnsTopicInfo instance
     * @throws IllegalArgumentException if topicName or topicArn is null or empty
     */
    public static SnsTopicInfo of(String topicName, String topicArn) {
        if (topicName == null || topicName.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic name cannot be null or empty");
        }
        if (topicArn == null || topicArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic ARN cannot be null or empty");
        }
        
        Instant now = Instant.now();
        return new SnsTopicInfo(
            topicName.trim(),
            topicArn.trim(),
            Map.of(),
            0,
            now,
            now
        );
    }
    
    /**
     * Creates an SNS topic info with all information.
     * 
     * @param topicName the name of the topic
     * @param topicArn the ARN of the topic
     * @param attributes topic attributes
     * @param subscriptionCount number of subscriptions
     * @param createdTimestamp timestamp when the topic was created
     * @param lastModifiedTimestamp timestamp when the topic was last modified
     * @return new SnsTopicInfo instance
     * @throws IllegalArgumentException if topicName or topicArn is null or empty
     */
    public static SnsTopicInfo of(String topicName, String topicArn, Map<String, String> attributes,
                                 int subscriptionCount, Instant createdTimestamp, Instant lastModifiedTimestamp) {
        if (topicName == null || topicName.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic name cannot be null or empty");
        }
        if (topicArn == null || topicArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic ARN cannot be null or empty");
        }
        
        return new SnsTopicInfo(
            topicName.trim(),
            topicArn.trim(),
            attributes != null ? Map.copyOf(attributes) : Map.of(),
            Math.max(subscriptionCount, 0),
            createdTimestamp != null ? createdTimestamp : Instant.now(),
            lastModifiedTimestamp != null ? lastModifiedTimestamp : Instant.now()
        );
    }
    
    /**
     * Creates a copy of this SnsTopicInfo with updated attributes.
     * 
     * @param attributes new attributes map
     * @return new SnsTopicInfo instance with updated attributes
     */
    public SnsTopicInfo withAttributes(Map<String, String> attributes) {
        return new SnsTopicInfo(
            this.topicName,
            this.topicArn,
            attributes != null ? Map.copyOf(attributes) : Map.of(),
            this.subscriptionCount,
            this.createdTimestamp,
            this.lastModifiedTimestamp
        );
    }
    
    /**
     * Creates a copy of this SnsTopicInfo with updated subscription count.
     * 
     * @param subscriptionCount new subscription count
     * @return new SnsTopicInfo instance with updated subscription count
     */
    public SnsTopicInfo withSubscriptionCount(int subscriptionCount) {
        return new SnsTopicInfo(
            this.topicName,
            this.topicArn,
            this.attributes,
            Math.max(subscriptionCount, 0),
            this.createdTimestamp,
            this.lastModifiedTimestamp
        );
    }
    
    /**
     * Gets an attribute value by key.
     * 
     * @param key attribute key
     * @return attribute value or null if not found
     */
    public String getAttribute(String key) {
        return attributes != null ? attributes.get(key) : null;
    }
    
    /**
     * Checks if an attribute exists.
     * 
     * @param key attribute key
     * @return true if attribute exists, false otherwise
     */
    public boolean hasAttribute(String key) {
        return attributes != null && attributes.containsKey(key);
    }
    
    /**
     * Gets the display name of the topic.
     * 
     * @return display name or null if not set
     */
    public String getDisplayName() {
        return getAttribute("DisplayName");
    }
    
    /**
     * Gets the delivery policy of the topic.
     * 
     * @return delivery policy or null if not set
     */
    public String getDeliveryPolicy() {
        return getAttribute("DeliveryPolicy");
    }
    
    /**
     * Gets the effective delivery policy of the topic.
     * 
     * @return effective delivery policy or null if not set
     */
    public String getEffectiveDeliveryPolicy() {
        return getAttribute("EffectiveDeliveryPolicy");
    }
    
    /**
     * Gets the KMS master key ID for the topic.
     * 
     * @return KMS master key ID or null if not set
     */
    public String getKmsMasterKeyId() {
        return getAttribute("KmsMasterKeyId");
    }
    
    /**
     * Gets the policy of the topic.
     * 
     * @return policy or null if not set
     */
    public String getPolicy() {
        return getAttribute("Policy");
    }
    
    /**
     * Gets the signature version of the topic.
     * 
     * @return signature version or null if not set
     */
    public String getSignatureVersion() {
        return getAttribute("SignatureVersion");
    }
    
    /**
     * Gets the tracing config of the topic.
     * 
     * @return tracing config or null if not set
     */
    public String getTracingConfig() {
        return getAttribute("TracingConfig");
    }
    
    /**
     * Checks if this topic has a display name.
     * 
     * @return true if display name is set, false otherwise
     */
    public boolean hasDisplayName() {
        return getDisplayName() != null && !getDisplayName().trim().isEmpty();
    }
    
    /**
     * Checks if this topic has a delivery policy.
     * 
     * @return true if delivery policy is set, false otherwise
     */
    public boolean hasDeliveryPolicy() {
        return getDeliveryPolicy() != null && !getDeliveryPolicy().trim().isEmpty();
    }
    
    /**
     * Checks if this topic has a policy.
     * 
     * @return true if policy is set, false otherwise
     */
    public boolean hasPolicy() {
        return getPolicy() != null && !getPolicy().trim().isEmpty();
    }
    
    /**
     * Checks if this topic has KMS encryption enabled.
     * 
     * @return true if KMS master key ID is set, false otherwise
     */
    public boolean hasKmsEncryption() {
        return getKmsMasterKeyId() != null && !getKmsMasterKeyId().trim().isEmpty();
    }
    
    /**
     * Checks if this topic has tracing enabled.
     * 
     * @return true if tracing config is set, false otherwise
     */
    public boolean hasTracing() {
        return getTracingConfig() != null && !getTracingConfig().trim().isEmpty();
    }
    
    /**
     * Checks if this topic has subscriptions.
     * 
     * @return true if subscription count > 0, false otherwise
     */
    public boolean hasSubscriptions() {
        return subscriptionCount > 0;
    }
    
    /**
     * Gets the age of this topic in seconds.
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
     * Checks if this topic is older than the specified number of seconds.
     * 
     * @param seconds the number of seconds to check against
     * @return true if topic is older than specified seconds, false otherwise
     */
    public boolean isOlderThan(int seconds) {
        return getAgeInSeconds() > seconds;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SnsTopicInfo that = (SnsTopicInfo) obj;
        return subscriptionCount == that.subscriptionCount &&
               Objects.equals(topicName, that.topicName) &&
               Objects.equals(topicArn, that.topicArn) &&
               Objects.equals(attributes, that.attributes) &&
               Objects.equals(createdTimestamp, that.createdTimestamp) &&
               Objects.equals(lastModifiedTimestamp, that.lastModifiedTimestamp);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(topicName, topicArn, attributes, subscriptionCount, createdTimestamp, lastModifiedTimestamp);
    }
    
    @Override
    public String toString() {
        return "SnsTopicInfo{" +
               "topicName='" + topicName + '\'' +
               ", topicArn='" + topicArn + '\'' +
               ", attributes=" + attributes +
               ", subscriptionCount=" + subscriptionCount +
               ", createdTimestamp=" + createdTimestamp +
               ", lastModifiedTimestamp=" + lastModifiedTimestamp +
               '}';
    }
}
