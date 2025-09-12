package io.github.base.aws.api.sns;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing SNS subscription information.
 * 
 * <p>This record contains all the information about an SNS subscription including
 * its ARN, protocol, endpoint, and attributes.
 * 
 * @param subscriptionArn the ARN of the subscription
 * @param topicArn the ARN of the topic
 * @param protocol the protocol for the subscription
 * @param endpoint the endpoint of the subscription
 * @param attributes subscription attributes
 * @param createdTimestamp timestamp when the subscription was created
 * @param lastModifiedTimestamp timestamp when the subscription was last modified
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record SnsSubscriptionInfo(
    String subscriptionArn,
    String topicArn,
    String protocol,
    String endpoint,
    Map<String, String> attributes,
    Instant createdTimestamp,
    Instant lastModifiedTimestamp
) {
    
    /**
     * Creates an SNS subscription info with basic information.
     * 
     * @param subscriptionArn the ARN of the subscription
     * @param topicArn the ARN of the topic
     * @param protocol the protocol for the subscription
     * @param endpoint the endpoint of the subscription
     * @return new SnsSubscriptionInfo instance
     * @throws IllegalArgumentException if subscriptionArn, topicArn, protocol, or endpoint is null or empty
     */
    public static SnsSubscriptionInfo of(String subscriptionArn, String topicArn, String protocol, String endpoint) {
        if (subscriptionArn == null || subscriptionArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Subscription ARN cannot be null or empty");
        }
        if (topicArn == null || topicArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic ARN cannot be null or empty");
        }
        if (protocol == null || protocol.trim().isEmpty()) {
            throw new IllegalArgumentException("Protocol cannot be null or empty");
        }
        if (endpoint == null || endpoint.trim().isEmpty()) {
            throw new IllegalArgumentException("Endpoint cannot be null or empty");
        }
        
        Instant now = Instant.now();
        return new SnsSubscriptionInfo(
            subscriptionArn.trim(),
            topicArn.trim(),
            protocol.trim(),
            endpoint.trim(),
            Map.of(),
            now,
            now
        );
    }
    
    /**
     * Creates an SNS subscription info with all information.
     * 
     * @param subscriptionArn the ARN of the subscription
     * @param topicArn the ARN of the topic
     * @param protocol the protocol for the subscription
     * @param endpoint the endpoint of the subscription
     * @param attributes subscription attributes
     * @param createdTimestamp timestamp when the subscription was created
     * @param lastModifiedTimestamp timestamp when the subscription was last modified
     * @return new SnsSubscriptionInfo instance
     * @throws IllegalArgumentException if subscriptionArn, topicArn, protocol, or endpoint is null or empty
     */
    public static SnsSubscriptionInfo of(String subscriptionArn, String topicArn, String protocol, String endpoint,
                                        Map<String, String> attributes, Instant createdTimestamp, Instant lastModifiedTimestamp) {
        if (subscriptionArn == null || subscriptionArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Subscription ARN cannot be null or empty");
        }
        if (topicArn == null || topicArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic ARN cannot be null or empty");
        }
        if (protocol == null || protocol.trim().isEmpty()) {
            throw new IllegalArgumentException("Protocol cannot be null or empty");
        }
        if (endpoint == null || endpoint.trim().isEmpty()) {
            throw new IllegalArgumentException("Endpoint cannot be null or empty");
        }
        
        return new SnsSubscriptionInfo(
            subscriptionArn.trim(),
            topicArn.trim(),
            protocol.trim(),
            endpoint.trim(),
            attributes != null ? Map.copyOf(attributes) : Map.of(),
            createdTimestamp != null ? createdTimestamp : Instant.now(),
            lastModifiedTimestamp != null ? lastModifiedTimestamp : Instant.now()
        );
    }
    
    /**
     * Creates a copy of this SnsSubscriptionInfo with updated attributes.
     * 
     * @param attributes new attributes map
     * @return new SnsSubscriptionInfo instance with updated attributes
     */
    public SnsSubscriptionInfo withAttributes(Map<String, String> attributes) {
        return new SnsSubscriptionInfo(
            this.subscriptionArn,
            this.topicArn,
            this.protocol,
            this.endpoint,
            attributes != null ? Map.copyOf(attributes) : Map.of(),
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
     * Gets the confirmation was authenticated flag.
     * 
     * @return true if confirmation was authenticated, false otherwise
     */
    public boolean isConfirmationWasAuthenticated() {
        String value = getAttribute("ConfirmationWasAuthenticated");
        return "true".equalsIgnoreCase(value);
    }
    
    /**
     * Gets the delivery policy of the subscription.
     * 
     * @return delivery policy or null if not set
     */
    public String getDeliveryPolicy() {
        return getAttribute("DeliveryPolicy");
    }
    
    /**
     * Gets the effective delivery policy of the subscription.
     * 
     * @return effective delivery policy or null if not set
     */
    public String getEffectiveDeliveryPolicy() {
        return getAttribute("EffectiveDeliveryPolicy");
    }
    
    /**
     * Gets the filter policy of the subscription.
     * 
     * @return filter policy or null if not set
     */
    public String getFilterPolicy() {
        return getAttribute("FilterPolicy");
    }
    
    /**
     * Gets the pending confirmation flag.
     * 
     * @return true if subscription is pending confirmation, false otherwise
     */
    public boolean isPendingConfirmation() {
        String value = getAttribute("PendingConfirmation");
        return "true".equalsIgnoreCase(value);
    }
    
    /**
     * Gets the raw message delivery flag.
     * 
     * @return true if raw message delivery is enabled, false otherwise
     */
    public boolean isRawMessageDelivery() {
        String value = getAttribute("RawMessageDelivery");
        return "true".equalsIgnoreCase(value);
    }
    
    /**
     * Gets the redrive policy of the subscription.
     * 
     * @return redrive policy or null if not set
     */
    public String getRedrivePolicy() {
        return getAttribute("RedrivePolicy");
    }
    
    /**
     * Gets the subscription role ARN.
     * 
     * @return subscription role ARN or null if not set
     */
    public String getSubscriptionRoleArn() {
        return getAttribute("SubscriptionRoleArn");
    }
    
    /**
     * Checks if this subscription is confirmed.
     * 
     * @return true if subscription is confirmed, false otherwise
     */
    public boolean isConfirmed() {
        return !isPendingConfirmation();
    }
    
    /**
     * Checks if this subscription has a delivery policy.
     * 
     * @return true if delivery policy is set, false otherwise
     */
    public boolean hasDeliveryPolicy() {
        return getDeliveryPolicy() != null && !getDeliveryPolicy().trim().isEmpty();
    }
    
    /**
     * Checks if this subscription has a filter policy.
     * 
     * @return true if filter policy is set, false otherwise
     */
    public boolean hasFilterPolicy() {
        return getFilterPolicy() != null && !getFilterPolicy().trim().isEmpty();
    }
    
    /**
     * Checks if this subscription has a redrive policy.
     * 
     * @return true if redrive policy is set, false otherwise
     */
    public boolean hasRedrivePolicy() {
        return getRedrivePolicy() != null && !getRedrivePolicy().trim().isEmpty();
    }
    
    /**
     * Checks if this subscription has a subscription role.
     * 
     * @return true if subscription role ARN is set, false otherwise
     */
    public boolean hasSubscriptionRole() {
        return getSubscriptionRoleArn() != null && !getSubscriptionRoleArn().trim().isEmpty();
    }
    
    /**
     * Checks if this subscription is an email subscription.
     * 
     * @return true if protocol is email, false otherwise
     */
    public boolean isEmailSubscription() {
        return "email".equalsIgnoreCase(protocol);
    }
    
    /**
     * Checks if this subscription is an SMS subscription.
     * 
     * @return true if protocol is sms, false otherwise
     */
    public boolean isSmsSubscription() {
        return "sms".equalsIgnoreCase(protocol);
    }
    
    /**
     * Checks if this subscription is an SQS subscription.
     * 
     * @return true if protocol is sqs, false otherwise
     */
    public boolean isSqsSubscription() {
        return "sqs".equalsIgnoreCase(protocol);
    }
    
    /**
     * Checks if this subscription is an HTTP/HTTPS subscription.
     * 
     * @return true if protocol is http or https, false otherwise
     */
    public boolean isHttpSubscription() {
        return "http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(protocol);
    }
    
    /**
     * Checks if this subscription is a Lambda subscription.
     * 
     * @return true if protocol is lambda, false otherwise
     */
    public boolean isLambdaSubscription() {
        return "lambda".equalsIgnoreCase(protocol);
    }
    
    /**
     * Gets the age of this subscription in seconds.
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
     * Checks if this subscription is older than the specified number of seconds.
     * 
     * @param seconds the number of seconds to check against
     * @return true if subscription is older than specified seconds, false otherwise
     */
    public boolean isOlderThan(int seconds) {
        return getAgeInSeconds() > seconds;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SnsSubscriptionInfo that = (SnsSubscriptionInfo) obj;
        return Objects.equals(subscriptionArn, that.subscriptionArn) &&
               Objects.equals(topicArn, that.topicArn) &&
               Objects.equals(protocol, that.protocol) &&
               Objects.equals(endpoint, that.endpoint) &&
               Objects.equals(attributes, that.attributes) &&
               Objects.equals(createdTimestamp, that.createdTimestamp) &&
               Objects.equals(lastModifiedTimestamp, that.lastModifiedTimestamp);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(subscriptionArn, topicArn, protocol, endpoint, attributes, createdTimestamp, lastModifiedTimestamp);
    }
    
    @Override
    public String toString() {
        return "SnsSubscriptionInfo{" +
               "subscriptionArn='" + subscriptionArn + '\'' +
               ", topicArn='" + topicArn + '\'' +
               ", protocol='" + protocol + '\'' +
               ", endpoint='" + endpoint + '\'' +
               ", attributes=" + attributes +
               ", createdTimestamp=" + createdTimestamp +
               ", lastModifiedTimestamp=" + lastModifiedTimestamp +
               '}';
    }
}
