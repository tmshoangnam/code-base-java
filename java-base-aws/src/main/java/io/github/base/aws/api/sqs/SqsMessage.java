package io.github.base.aws.api.sqs;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing an SQS message.
 * 
 * <p>This record contains all the information about an SQS message including
 * its body, attributes, metadata, and receipt handle.
 * 
 * @param messageId unique identifier for the message
 * @param receiptHandle receipt handle for message operations
 * @param body the message body
 * @param attributes message attributes
 * @param messageAttributes custom message attributes
 * @param md5OfBody MD5 hash of the message body
 * @param md5OfMessageAttributes MD5 hash of the message attributes
 * @param sentTimestamp timestamp when the message was sent
 * @param firstReceivedTimestamp timestamp when the message was first received
 * @param approximateReceiveCount approximate number of times the message has been received
 * @param approximateFirstReceiveTimestamp timestamp when the message was first received
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record SqsMessage(
    String messageId,
    String receiptHandle,
    String body,
    Map<String, String> attributes,
    Map<String, String> messageAttributes,
    String md5OfBody,
    String md5OfMessageAttributes,
    Instant sentTimestamp,
    Instant firstReceivedTimestamp,
    int approximateReceiveCount,
    Instant approximateFirstReceiveTimestamp
) {
    
    /**
     * Creates an SQS message with basic information.
     * 
     * @param messageId unique identifier for the message
     * @param receiptHandle receipt handle for message operations
     * @param body the message body
     * @return new SqsMessage instance
     * @throws IllegalArgumentException if messageId, receiptHandle, or body is null or empty
     */
    public static SqsMessage of(String messageId, String receiptHandle, String body) {
        if (messageId == null || messageId.trim().isEmpty()) {
            throw new IllegalArgumentException("Message ID cannot be null or empty");
        }
        if (receiptHandle == null || receiptHandle.trim().isEmpty()) {
            throw new IllegalArgumentException("Receipt handle cannot be null or empty");
        }
        if (body == null) {
            throw new IllegalArgumentException("Message body cannot be null");
        }
        
        return new SqsMessage(
            messageId.trim(),
            receiptHandle.trim(),
            body,
            Map.of(),
            Map.of(),
            null,
            null,
            Instant.now(),
            Instant.now(),
            1,
            Instant.now()
        );
    }
    
    /**
     * Creates an SQS message with all information.
     * 
     * @param messageId unique identifier for the message
     * @param receiptHandle receipt handle for message operations
     * @param body the message body
     * @param attributes message attributes
     * @param messageAttributes custom message attributes
     * @param md5OfBody MD5 hash of the message body
     * @param sentTimestamp timestamp when the message was sent
     * @param approximateReceiveCount approximate number of times the message has been received
     * @return new SqsMessage instance
     * @throws IllegalArgumentException if messageId, receiptHandle, or body is null or empty
     */
    public static SqsMessage of(String messageId, String receiptHandle, String body, 
                               Map<String, String> attributes, Map<String, String> messageAttributes,
                               String md5OfBody, Instant sentTimestamp, int approximateReceiveCount) {
        if (messageId == null || messageId.trim().isEmpty()) {
            throw new IllegalArgumentException("Message ID cannot be null or empty");
        }
        if (receiptHandle == null || receiptHandle.trim().isEmpty()) {
            throw new IllegalArgumentException("Receipt handle cannot be null or empty");
        }
        if (body == null) {
            throw new IllegalArgumentException("Message body cannot be null");
        }
        
        Instant now = Instant.now();
        return new SqsMessage(
            messageId.trim(),
            receiptHandle.trim(),
            body,
            attributes != null ? Map.copyOf(attributes) : Map.of(),
            messageAttributes != null ? Map.copyOf(messageAttributes) : Map.of(),
            md5OfBody,
            null,
            sentTimestamp != null ? sentTimestamp : now,
            now,
            Math.max(approximateReceiveCount, 1),
            now
        );
    }
    
    /**
     * Creates a copy of this SqsMessage with updated attributes.
     * 
     * @param attributes new attributes map
     * @return new SqsMessage instance with updated attributes
     */
    public SqsMessage withAttributes(Map<String, String> attributes) {
        return new SqsMessage(
            this.messageId,
            this.receiptHandle,
            this.body,
            attributes != null ? Map.copyOf(attributes) : Map.of(),
            this.messageAttributes,
            this.md5OfBody,
            this.md5OfMessageAttributes,
            this.sentTimestamp,
            this.firstReceivedTimestamp,
            this.approximateReceiveCount,
            this.approximateFirstReceiveTimestamp
        );
    }
    
    /**
     * Creates a copy of this SqsMessage with updated message attributes.
     * 
     * @param messageAttributes new message attributes map
     * @return new SqsMessage instance with updated message attributes
     */
    public SqsMessage withMessageAttributes(Map<String, String> messageAttributes) {
        return new SqsMessage(
            this.messageId,
            this.receiptHandle,
            this.body,
            this.attributes,
            messageAttributes != null ? Map.copyOf(messageAttributes) : Map.of(),
            this.md5OfBody,
            this.md5OfMessageAttributes,
            this.sentTimestamp,
            this.firstReceivedTimestamp,
            this.approximateReceiveCount,
            this.approximateFirstReceiveTimestamp
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
     * Gets a message attribute value by key.
     * 
     * @param key message attribute key
     * @return message attribute value or null if not found
     */
    public String getMessageAttribute(String key) {
        return messageAttributes != null ? messageAttributes.get(key) : null;
    }
    
    /**
     * Checks if a message attribute exists.
     * 
     * @param key message attribute key
     * @return true if message attribute exists, false otherwise
     */
    public boolean hasMessageAttribute(String key) {
        return messageAttributes != null && messageAttributes.containsKey(key);
    }
    
    /**
     * Checks if this message has been received multiple times.
     * 
     * @return true if approximateReceiveCount > 1, false otherwise
     */
    public boolean hasBeenReceivedMultipleTimes() {
        return approximateReceiveCount > 1;
    }
    
    /**
     * Checks if this message has a valid MD5 hash.
     * 
     * @return true if MD5 hash is present and not empty, false otherwise
     */
    public boolean hasValidMd5Hash() {
        return md5OfBody != null && !md5OfBody.trim().isEmpty();
    }
    
    /**
     * Gets the age of this message in seconds.
     * 
     * @return age in seconds or -1 if sentTimestamp is null
     */
    public long getAgeInSeconds() {
        if (sentTimestamp == null) {
            return -1;
        }
        return Instant.now().getEpochSecond() - sentTimestamp.getEpochSecond();
    }
    
    /**
     * Checks if this message is older than the specified number of seconds.
     * 
     * @param seconds the number of seconds to check against
     * @return true if message is older than specified seconds, false otherwise
     */
    public boolean isOlderThan(int seconds) {
        return getAgeInSeconds() > seconds;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SqsMessage that = (SqsMessage) obj;
        return approximateReceiveCount == that.approximateReceiveCount &&
               Objects.equals(messageId, that.messageId) &&
               Objects.equals(receiptHandle, that.receiptHandle) &&
               Objects.equals(body, that.body) &&
               Objects.equals(attributes, that.attributes) &&
               Objects.equals(messageAttributes, that.messageAttributes) &&
               Objects.equals(md5OfBody, that.md5OfBody) &&
               Objects.equals(md5OfMessageAttributes, that.md5OfMessageAttributes) &&
               Objects.equals(sentTimestamp, that.sentTimestamp) &&
               Objects.equals(firstReceivedTimestamp, that.firstReceivedTimestamp) &&
               Objects.equals(approximateFirstReceiveTimestamp, that.approximateFirstReceiveTimestamp);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(messageId, receiptHandle, body, attributes, messageAttributes, md5OfBody, 
                           md5OfMessageAttributes, sentTimestamp, firstReceivedTimestamp, 
                           approximateReceiveCount, approximateFirstReceiveTimestamp);
    }
    
    @Override
    public String toString() {
        return "SqsMessage{" +
               "messageId='" + messageId + '\'' +
               ", receiptHandle='" + receiptHandle + '\'' +
               ", body='" + body + '\'' +
               ", attributes=" + attributes +
               ", messageAttributes=" + messageAttributes +
               ", md5OfBody='" + md5OfBody + '\'' +
               ", md5OfMessageAttributes='" + md5OfMessageAttributes + '\'' +
               ", sentTimestamp=" + sentTimestamp +
               ", firstReceivedTimestamp=" + firstReceivedTimestamp +
               ", approximateReceiveCount=" + approximateReceiveCount +
               ", approximateFirstReceiveTimestamp=" + approximateFirstReceiveTimestamp +
               '}';
    }
}
