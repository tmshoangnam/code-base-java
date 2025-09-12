package io.github.base.aws.api.sns;


import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Contract for Amazon SNS operations.
 *
 * <p>This interface defines the standard operations for interacting with Amazon SNS,
 * including topic management, message publishing, and subscription management.
 *
 * <p>All operations are designed to be thread-safe and can be used in concurrent environments.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface SnsOperations {

    /**
     * Publishes a message to a topic.
     *
     * @param topicArn the ARN of the SNS topic
     * @param message the message to publish
     * @return message ID if successful, null if failed
     * @throws IllegalArgumentException if topicArn or message is null or empty
     * @throws io.github.base.aws.api.common.AwsException if publish fails
     */
    String publish(String topicArn, String message);

    /**
     * Publishes a message to a topic with subject.
     *
     * @param topicArn the ARN of the SNS topic
     * @param subject the subject of the message
     * @param message the message to publish
     * @return message ID if successful, null if failed
     * @throws IllegalArgumentException if topicArn or message is null or empty
     * @throws io.github.base.aws.api.common.AwsException if publish fails
     */
    String publish(String topicArn, String subject, String message);

    /**
     * Publishes a message to a topic with message attributes.
     *
     * @param topicArn the ARN of the SNS topic
     * @param message the message to publish
     * @param messageAttributes message attributes
     * @return message ID if successful, null if failed
     * @throws IllegalArgumentException if topicArn or message is null or empty
     * @throws io.github.base.aws.api.common.AwsException if publish fails
     */
    String publish(String topicArn, String message, Map<String, String> messageAttributes);

    /**
     * Publishes a message to a topic with all options.
     *
     * @param topicArn the ARN of the SNS topic
     * @param subject the subject of the message
     * @param message the message to publish
     * @param messageAttributes message attributes
     * @return message ID if successful, null if failed
     * @throws IllegalArgumentException if topicArn or message is null or empty
     * @throws io.github.base.aws.api.common.AwsException if publish fails
     */
    String publish(String topicArn, String subject, String message, Map<String, String> messageAttributes);

    /**
     * Publishes a message to multiple topics.
     *
     * @param topicArns list of topic ARNs
     * @param message the message to publish
     * @return map of topic ARNs to message IDs
     * @throws IllegalArgumentException if topicArns is null or empty, or message is null or empty
     * @throws io.github.base.aws.api.common.AwsException if publish fails
     */
    Map<String, String> publishToMultipleTopics(List<String> topicArns, String message);

    /**
     * Creates a topic.
     *
     * @param topicName the name of the topic to create
     * @return topic information or null if creation failed
     * @throws IllegalArgumentException if topicName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    SnsTopicInfo createTopic(String topicName);

    /**
     * Creates a topic with attributes.
     *
     * @param topicName the name of the topic to create
     * @param attributes topic attributes
     * @return topic information or null if creation failed
     * @throws IllegalArgumentException if topicName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    SnsTopicInfo createTopic(String topicName, Map<String, String> attributes);

    /**
     * Deletes a topic.
     *
     * @param topicArn the ARN of the topic to delete
     * @return true if topic was deleted successfully, false otherwise
     * @throws IllegalArgumentException if topicArn is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteTopic(String topicArn);

    /**
     * Gets topic information.
     *
     * @param topicArn the ARN of the topic
     * @return topic information or null if topic not found
     * @throws IllegalArgumentException if topicArn is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    SnsTopicInfo getTopicInfo(String topicArn);

    /**
     * Gets topic information by name.
     *
     * @param topicName the name of the topic
     * @return topic information or null if topic not found
     * @throws IllegalArgumentException if topicName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    SnsTopicInfo getTopicInfoByName(String topicName);

    /**
     * Lists all topics.
     *
     * @return list of topic information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<SnsTopicInfo> listTopics();

    /**
     * Lists topics with a name prefix.
     *
     * @param prefix the prefix to filter topic names
     * @return list of topic information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<SnsTopicInfo> listTopics(String prefix);

    /**
     * Sets topic attributes.
     *
     * @param topicArn the ARN of the topic
     * @param attributes topic attributes to set
     * @return true if attributes were set successfully, false otherwise
     * @throws IllegalArgumentException if topicArn is null or empty, or attributes is null
     * @throws io.github.base.aws.api.common.AwsException if setting fails
     */
    boolean setTopicAttributes(String topicArn, Map<String, String> attributes);

    /**
     * Gets topic attributes.
     *
     * @param topicArn the ARN of the topic
     * @return map of topic attributes or null if topic not found
     * @throws IllegalArgumentException if topicArn is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    Map<String, String> getTopicAttributes(String topicArn);

    /**
     * Subscribes an endpoint to a topic.
     *
     * @param topicArn the ARN of the topic
     * @param protocol the protocol for the subscription (email, sms, sqs, etc.)
     * @param endpoint the endpoint to subscribe
     * @return subscription information or null if subscription failed
     * @throws IllegalArgumentException if topicArn, protocol, or endpoint is null or empty
     * @throws io.github.base.aws.api.common.AwsException if subscription fails
     */
    SnsSubscriptionInfo subscribe(String topicArn, String protocol, String endpoint);

    /**
     * Subscribes an endpoint to a topic with attributes.
     *
     * @param topicArn the ARN of the topic
     * @param protocol the protocol for the subscription
     * @param endpoint the endpoint to subscribe
     * @param attributes subscription attributes
     * @return subscription information or null if subscription failed
     * @throws IllegalArgumentException if topicArn, protocol, or endpoint is null or empty
     * @throws io.github.base.aws.api.common.AwsException if subscription fails
     */
    SnsSubscriptionInfo subscribe(String topicArn, String protocol, String endpoint, Map<String, String> attributes);

    /**
     * Unsubscribes from a topic.
     *
     * @param subscriptionArn the ARN of the subscription
     * @return true if unsubscription was successful, false otherwise
     * @throws IllegalArgumentException if subscriptionArn is null or empty
     * @throws io.github.base.aws.api.common.AwsException if unsubscription fails
     */
    boolean unsubscribe(String subscriptionArn);

    /**
     * Gets subscription information.
     *
     * @param subscriptionArn the ARN of the subscription
     * @return subscription information or null if subscription not found
     * @throws IllegalArgumentException if subscriptionArn is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    SnsSubscriptionInfo getSubscriptionInfo(String subscriptionArn);

    /**
     * Lists subscriptions for a topic.
     *
     * @param topicArn the ARN of the topic
     * @return list of subscription information
     * @throws IllegalArgumentException if topicArn is null or empty
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<SnsSubscriptionInfo> listSubscriptions(String topicArn);

    /**
     * Lists all subscriptions.
     *
     * @return list of subscription information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<SnsSubscriptionInfo> listSubscriptions();

    /**
     * Confirms a subscription.
     *
     * @param topicArn the ARN of the topic
     * @param token the confirmation token
     * @return true if confirmation was successful, false otherwise
     * @throws IllegalArgumentException if topicArn or token is null or empty
     * @throws io.github.base.aws.api.common.AwsException if confirmation fails
     */
    boolean confirmSubscription(String topicArn, String token);

    /**
     * Sets subscription attributes.
     *
     * @param subscriptionArn the ARN of the subscription
     * @param attributes subscription attributes to set
     * @return true if attributes were set successfully, false otherwise
     * @throws IllegalArgumentException if subscriptionArn is null or empty, or attributes is null
     * @throws io.github.base.aws.api.common.AwsException if setting fails
     */
    boolean setSubscriptionAttributes(String subscriptionArn, Map<String, String> attributes);

    /**
     * Gets subscription attributes.
     *
     * @param subscriptionArn the ARN of the subscription
     * @return map of subscription attributes or null if subscription not found
     * @throws IllegalArgumentException if subscriptionArn is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    Map<String, String> getSubscriptionAttributes(String subscriptionArn);

    /**
     * Asynchronously publishes a message to a topic.
     *
     * @param topicArn the ARN of the SNS topic
     * @param message the message to publish
     * @return CompletableFuture containing message ID
     */
    CompletableFuture<String> publishAsync(String topicArn, String message);

    /**
     * Asynchronously publishes a message to a topic with subject.
     *
     * @param topicArn the ARN of the SNS topic
     * @param subject the subject of the message
     * @param message the message to publish
     * @return CompletableFuture containing message ID
     */
    CompletableFuture<String> publishAsync(String topicArn, String subject, String message);

    /**
     * Gets the default topic ARN.
     *
     * @return the default topic ARN or null if not set
     */
    String getDefaultTopicArn();

    /**
     * Sets the default topic ARN.
     *
     * @param topicArn the topic ARN to set as default
     * @throws IllegalArgumentException if topicArn is null or empty
     */
    void setDefaultTopicArn(String topicArn);

    /**
     * Gets the SNS operations capabilities.
     *
     * @return map of capability flags
     */
    Map<String, Boolean> getCapabilities();
}
