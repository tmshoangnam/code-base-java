package io.github.base.aws.api.sqs;

import io.github.base.aws.api.sqs.SqsMessage;
import io.github.base.aws.api.sqs.SqsQueueInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Contract for Amazon SQS operations.
 * 
 * <p>This interface defines the standard operations for interacting with Amazon SQS,
 * including message sending, receiving, deletion, and queue management.
 * 
 * <p>All operations are designed to be thread-safe and can be used in concurrent environments.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface SqsOperations {
    
    /**
     * Sends a message to a queue.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param messageBody the message body
     * @return message ID if successful, null if failed
     * @throws IllegalArgumentException if queueUrl or messageBody is null or empty
     * @throws io.github.base.aws.api.common.AwsException if send fails
     */
    String sendMessage(String queueUrl, String messageBody);
    
    /**
     * Sends a message to a queue with message attributes.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param messageBody the message body
     * @param messageAttributes message attributes
     * @return message ID if successful, null if failed
     * @throws IllegalArgumentException if queueUrl or messageBody is null or empty
     * @throws io.github.base.aws.api.common.AwsException if send fails
     */
    String sendMessage(String queueUrl, String messageBody, Map<String, String> messageAttributes);
    
    /**
     * Sends a message to a queue with delay.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param messageBody the message body
     * @param delaySeconds delay in seconds before the message becomes available
     * @return message ID if successful, null if failed
     * @throws IllegalArgumentException if queueUrl or messageBody is null or empty, or delaySeconds < 0
     * @throws io.github.base.aws.api.common.AwsException if send fails
     */
    String sendMessage(String queueUrl, String messageBody, int delaySeconds);
    
    /**
     * Sends a message to a queue with all options.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param messageBody the message body
     * @param messageAttributes message attributes
     * @param delaySeconds delay in seconds before the message becomes available
     * @return message ID if successful, null if failed
     * @throws IllegalArgumentException if queueUrl or messageBody is null or empty, or delaySeconds < 0
     * @throws io.github.base.aws.api.common.AwsException if send fails
     */
    String sendMessage(String queueUrl, String messageBody, Map<String, String> messageAttributes, int delaySeconds);
    
    /**
     * Sends multiple messages to a queue in a batch.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param messages list of messages to send
     * @return map of message IDs to success status
     * @throws IllegalArgumentException if queueUrl is null or empty, or messages is null
     * @throws io.github.base.aws.api.common.AwsException if batch send fails
     */
    Map<String, Boolean> sendMessageBatch(String queueUrl, List<SqsMessage> messages);
    
    /**
     * Receives messages from a queue.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param maxMessages maximum number of messages to receive (1-10)
     * @param waitTimeSeconds maximum time to wait for messages (0-20)
     * @return list of received messages
     * @throws IllegalArgumentException if queueUrl is null or empty, or maxMessages < 1 or > 10, or waitTimeSeconds < 0 or > 20
     * @throws io.github.base.aws.api.common.AwsException if receive fails
     */
    List<SqsMessage> receiveMessages(String queueUrl, int maxMessages, int waitTimeSeconds);
    
    /**
     * Receives messages from a queue with default settings.
     * 
     * @param queueUrl the URL of the SQS queue
     * @return list of received messages
     * @throws IllegalArgumentException if queueUrl is null or empty
     * @throws io.github.base.aws.api.common.AwsException if receive fails
     */
    List<SqsMessage> receiveMessages(String queueUrl);
    
    /**
     * Deletes a message from a queue.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param receiptHandle the receipt handle of the message to delete
     * @return true if message was deleted successfully, false otherwise
     * @throws IllegalArgumentException if queueUrl or receiptHandle is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteMessage(String queueUrl, String receiptHandle);
    
    /**
     * Deletes multiple messages from a queue in a batch.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param receiptHandles list of receipt handles to delete
     * @return map of receipt handles to deletion success status
     * @throws IllegalArgumentException if queueUrl is null or empty, or receiptHandles is null
     * @throws io.github.base.aws.api.common.AwsException if batch deletion fails
     */
    Map<String, Boolean> deleteMessageBatch(String queueUrl, List<String> receiptHandles);
    
    /**
     * Changes the visibility timeout of a message.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param receiptHandle the receipt handle of the message
     * @param visibilityTimeoutSeconds new visibility timeout in seconds
     * @return true if visibility timeout was changed successfully, false otherwise
     * @throws IllegalArgumentException if queueUrl or receiptHandle is null or empty, or visibilityTimeoutSeconds < 0
     * @throws io.github.base.aws.api.common.AwsException if change fails
     */
    boolean changeMessageVisibility(String queueUrl, String receiptHandle, int visibilityTimeoutSeconds);
    
    /**
     * Gets queue information.
     * 
     * @param queueUrl the URL of the SQS queue
     * @return queue information or null if queue not found
     * @throws IllegalArgumentException if queueUrl is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    SqsQueueInfo getQueueInfo(String queueUrl);
    
    /**
     * Gets queue information by name.
     * 
     * @param queueName the name of the SQS queue
     * @return queue information or null if queue not found
     * @throws IllegalArgumentException if queueName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    SqsQueueInfo getQueueInfoByName(String queueName);
    
    /**
     * Creates a queue.
     * 
     * @param queueName the name of the queue to create
     * @param attributes queue attributes
     * @return queue information or null if creation failed
     * @throws IllegalArgumentException if queueName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    SqsQueueInfo createQueue(String queueName, Map<String, String> attributes);
    
    /**
     * Creates a queue with default attributes.
     * 
     * @param queueName the name of the queue to create
     * @return queue information or null if creation failed
     * @throws IllegalArgumentException if queueName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    SqsQueueInfo createQueue(String queueName);
    
    /**
     * Deletes a queue.
     * 
     * @param queueUrl the URL of the SQS queue
     * @return true if queue was deleted successfully, false otherwise
     * @throws IllegalArgumentException if queueUrl is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteQueue(String queueUrl);
    
    /**
     * Lists all queues.
     * 
     * @return list of queue information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<SqsQueueInfo> listQueues();
    
    /**
     * Lists queues with a name prefix.
     * 
     * @param prefix the prefix to filter queue names
     * @return list of queue information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<SqsQueueInfo> listQueues(String prefix);
    
    /**
     * Gets the approximate number of messages in a queue.
     * 
     * @param queueUrl the URL of the SQS queue
     * @return approximate number of messages or -1 if retrieval failed
     * @throws IllegalArgumentException if queueUrl is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    int getApproximateNumberOfMessages(String queueUrl);
    
    /**
     * Asynchronously sends a message to a queue.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param messageBody the message body
     * @return CompletableFuture containing message ID
     */
    CompletableFuture<String> sendMessageAsync(String queueUrl, String messageBody);
    
    /**
     * Asynchronously receives messages from a queue.
     * 
     * @param queueUrl the URL of the SQS queue
     * @param maxMessages maximum number of messages to receive
     * @param waitTimeSeconds maximum time to wait for messages
     * @return CompletableFuture containing list of messages
     */
    CompletableFuture<List<SqsMessage>> receiveMessagesAsync(String queueUrl, int maxMessages, int waitTimeSeconds);
    
    /**
     * Gets the default queue URL.
     * 
     * @return the default queue URL or null if not set
     */
    String getDefaultQueueUrl();
    
    /**
     * Sets the default queue URL.
     * 
     * @param queueUrl the queue URL to set as default
     * @throws IllegalArgumentException if queueUrl is null or empty
     */
    void setDefaultQueueUrl(String queueUrl);
    
    /**
     * Gets the SQS operations capabilities.
     * 
     * @return map of capability flags
     */
    Map<String, Boolean> getCapabilities();
}
