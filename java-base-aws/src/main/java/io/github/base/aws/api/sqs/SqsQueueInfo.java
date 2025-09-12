package io.github.base.aws.api.sqs;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing SQS queue information.
 * 
 * <p>This record contains all the information about an SQS queue including
 * its URL, name, attributes, and statistics.
 * 
 * @param queueName the name of the queue
 * @param queueUrl the URL of the queue
 * @param attributes queue attributes
 * @param approximateNumberOfMessages approximate number of messages in the queue
 * @param approximateNumberOfMessagesNotVisible approximate number of messages not visible
 * @param approximateNumberOfMessagesDelayed approximate number of delayed messages
 * @param createdTimestamp timestamp when the queue was created
 * @param lastModifiedTimestamp timestamp when the queue was last modified
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record SqsQueueInfo(
    String queueName,
    String queueUrl,
    Map<String, String> attributes,
    int approximateNumberOfMessages,
    int approximateNumberOfMessagesNotVisible,
    int approximateNumberOfMessagesDelayed,
    Instant createdTimestamp,
    Instant lastModifiedTimestamp
) {
    
    /**
     * Creates an SQS queue info with basic information.
     * 
     * @param queueName the name of the queue
     * @param queueUrl the URL of the queue
     * @return new SqsQueueInfo instance
     * @throws IllegalArgumentException if queueName or queueUrl is null or empty
     */
    public static SqsQueueInfo of(String queueName, String queueUrl) {
        if (queueName == null || queueName.trim().isEmpty()) {
            throw new IllegalArgumentException("Queue name cannot be null or empty");
        }
        if (queueUrl == null || queueUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Queue URL cannot be null or empty");
        }
        
        Instant now = Instant.now();
        return new SqsQueueInfo(
            queueName.trim(),
            queueUrl.trim(),
            Map.of(),
            0,
            0,
            0,
            now,
            now
        );
    }
    
    /**
     * Creates an SQS queue info with all information.
     * 
     * @param queueName the name of the queue
     * @param queueUrl the URL of the queue
     * @param attributes queue attributes
     * @param approximateNumberOfMessages approximate number of messages in the queue
     * @param approximateNumberOfMessagesNotVisible approximate number of messages not visible
     * @param approximateNumberOfMessagesDelayed approximate number of delayed messages
     * @param createdTimestamp timestamp when the queue was created
     * @param lastModifiedTimestamp timestamp when the queue was last modified
     * @return new SqsQueueInfo instance
     * @throws IllegalArgumentException if queueName or queueUrl is null or empty
     */
    public static SqsQueueInfo of(String queueName, String queueUrl, Map<String, String> attributes,
                                 int approximateNumberOfMessages, int approximateNumberOfMessagesNotVisible,
                                 int approximateNumberOfMessagesDelayed, Instant createdTimestamp,
                                 Instant lastModifiedTimestamp) {
        if (queueName == null || queueName.trim().isEmpty()) {
            throw new IllegalArgumentException("Queue name cannot be null or empty");
        }
        if (queueUrl == null || queueUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Queue URL cannot be null or empty");
        }
        
        return new SqsQueueInfo(
            queueName.trim(),
            queueUrl.trim(),
            attributes != null ? Map.copyOf(attributes) : Map.of(),
            Math.max(approximateNumberOfMessages, 0),
            Math.max(approximateNumberOfMessagesNotVisible, 0),
            Math.max(approximateNumberOfMessagesDelayed, 0),
            createdTimestamp != null ? createdTimestamp : Instant.now(),
            lastModifiedTimestamp != null ? lastModifiedTimestamp : Instant.now()
        );
    }
    
    /**
     * Creates a copy of this SqsQueueInfo with updated attributes.
     * 
     * @param attributes new attributes map
     * @return new SqsQueueInfo instance with updated attributes
     */
    public SqsQueueInfo withAttributes(Map<String, String> attributes) {
        return new SqsQueueInfo(
            this.queueName,
            this.queueUrl,
            attributes != null ? Map.copyOf(attributes) : Map.of(),
            this.approximateNumberOfMessages,
            this.approximateNumberOfMessagesNotVisible,
            this.approximateNumberOfMessagesDelayed,
            this.createdTimestamp,
            this.lastModifiedTimestamp
        );
    }
    
    /**
     * Creates a copy of this SqsQueueInfo with updated message counts.
     * 
     * @param approximateNumberOfMessages approximate number of messages in the queue
     * @param approximateNumberOfMessagesNotVisible approximate number of messages not visible
     * @param approximateNumberOfMessagesDelayed approximate number of delayed messages
     * @return new SqsQueueInfo instance with updated message counts
     */
    public SqsQueueInfo withMessageCounts(int approximateNumberOfMessages, 
                                         int approximateNumberOfMessagesNotVisible,
                                         int approximateNumberOfMessagesDelayed) {
        return new SqsQueueInfo(
            this.queueName,
            this.queueUrl,
            this.attributes,
            Math.max(approximateNumberOfMessages, 0),
            Math.max(approximateNumberOfMessagesNotVisible, 0),
            Math.max(approximateNumberOfMessagesDelayed, 0),
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
     * Gets the visibility timeout in seconds.
     * 
     * @return visibility timeout in seconds or -1 if not set
     */
    public int getVisibilityTimeout() {
        String value = getAttribute("VisibilityTimeoutSeconds");
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Ignore parsing error
            }
        }
        return -1;
    }
    
    /**
     * Gets the message retention period in seconds.
     * 
     * @return message retention period in seconds or -1 if not set
     */
    public int getMessageRetentionPeriod() {
        String value = getAttribute("MessageRetentionPeriod");
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Ignore parsing error
            }
        }
        return -1;
    }
    
    /**
     * Gets the maximum message size in bytes.
     * 
     * @return maximum message size in bytes or -1 if not set
     */
    public int getMaximumMessageSize() {
        String value = getAttribute("MaximumMessageSize");
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Ignore parsing error
            }
        }
        return -1;
    }
    
    /**
     * Gets the delay in seconds.
     * 
     * @return delay in seconds or -1 if not set
     */
    public int getDelaySeconds() {
        String value = getAttribute("DelaySeconds");
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Ignore parsing error
            }
        }
        return -1;
    }
    
    /**
     * Gets the receive message wait time in seconds.
     * 
     * @return receive message wait time in seconds or -1 if not set
     */
    public int getReceiveMessageWaitTime() {
        String value = getAttribute("ReceiveMessageWaitTimeSeconds");
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Ignore parsing error
            }
        }
        return -1;
    }
    
    /**
     * Checks if this queue is a FIFO queue.
     * 
     * @return true if queue name ends with .fifo, false otherwise
     */
    public boolean isFifoQueue() {
        return queueName != null && queueName.endsWith(".fifo");
    }
    
    /**
     * Checks if this queue has content-based deduplication enabled.
     * 
     * @return true if content-based deduplication is enabled, false otherwise
     */
    public boolean hasContentBasedDeduplication() {
        String value = getAttribute("ContentBasedDeduplication");
        return "true".equalsIgnoreCase(value);
    }
    
    /**
     * Gets the total approximate number of messages.
     * 
     * @return total approximate number of messages
     */
    public int getTotalApproximateMessages() {
        return approximateNumberOfMessages + approximateNumberOfMessagesNotVisible + approximateNumberOfMessagesDelayed;
    }
    
    /**
     * Checks if this queue is empty.
     * 
     * @return true if queue has no messages, false otherwise
     */
    public boolean isEmpty() {
        return getTotalApproximateMessages() == 0;
    }
    
    /**
     * Gets the age of this queue in seconds.
     * 
     * @return age in seconds or -1 if createdTimestamp is null
     */
    public long getAgeInSeconds() {
        if (createdTimestamp == null) {
            return -1;
        }
        return Instant.now().getEpochSecond() - createdTimestamp.getEpochSecond();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SqsQueueInfo that = (SqsQueueInfo) obj;
        return approximateNumberOfMessages == that.approximateNumberOfMessages &&
               approximateNumberOfMessagesNotVisible == that.approximateNumberOfMessagesNotVisible &&
               approximateNumberOfMessagesDelayed == that.approximateNumberOfMessagesDelayed &&
               Objects.equals(queueName, that.queueName) &&
               Objects.equals(queueUrl, that.queueUrl) &&
               Objects.equals(attributes, that.attributes) &&
               Objects.equals(createdTimestamp, that.createdTimestamp) &&
               Objects.equals(lastModifiedTimestamp, that.lastModifiedTimestamp);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(queueName, queueUrl, attributes, approximateNumberOfMessages, 
                           approximateNumberOfMessagesNotVisible, approximateNumberOfMessagesDelayed, 
                           createdTimestamp, lastModifiedTimestamp);
    }
    
    @Override
    public String toString() {
        return "SqsQueueInfo{" +
               "queueName='" + queueName + '\'' +
               ", queueUrl='" + queueUrl + '\'' +
               ", attributes=" + attributes +
               ", approximateNumberOfMessages=" + approximateNumberOfMessages +
               ", approximateNumberOfMessagesNotVisible=" + approximateNumberOfMessagesNotVisible +
               ", approximateNumberOfMessagesDelayed=" + approximateNumberOfMessagesDelayed +
               ", createdTimestamp=" + createdTimestamp +
               ", lastModifiedTimestamp=" + lastModifiedTimestamp +
               '}';
    }
}
