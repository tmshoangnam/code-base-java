package io.github.base.aws.api.dynamodb;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing DynamoDB table information.
 * 
 * <p>This record contains all the information about a DynamoDB table including
 * its name, status, key schema, and configuration.
 * 
 * @param tableName the name of the table
 * @param tableStatus the status of the table
 * @param tableSizeBytes the size of the table in bytes
 * @param itemCount the number of items in the table
     * @param keySchema the key schema of the table
     * @param attributeDefinitions attribute definitions
     * @param localSecondaryIndexes local secondary indexes
     * @param globalSecondaryIndexes global secondary indexes
     * @param provisionedThroughput provisioned throughput
     * @param billingMode the billing mode
     * @param createdTimestamp timestamp when the table was created
     * @param lastModifiedTimestamp timestamp when the table was last modified
     * @param tags table tags
     * 
     * @author java-base-team
     * @since 1.0.5-SNAPSHOT
     */
public record DynamoTableInfo(
    String tableName,
    String tableStatus,
    long tableSizeBytes,
    long itemCount,
    List<Map<String, String>> keySchema,
    List<Map<String, String>> attributeDefinitions,
    List<Map<String, Object>> localSecondaryIndexes,
    List<Map<String, Object>> globalSecondaryIndexes,
    Map<String, Object> provisionedThroughput,
    String billingMode,
    Instant createdTimestamp,
    Instant lastModifiedTimestamp,
    Map<String, String> tags
) {
    
    /**
     * Creates a DynamoDB table info with basic information.
     * 
     * @param tableName the name of the table
     * @param tableStatus the status of the table
     * @param keySchema the key schema of the table
     * @param attributeDefinitions attribute definitions
     * @return new DynamoTableInfo instance
     * @throws IllegalArgumentException if tableName is null or empty, or keySchema is null
     */
    public static DynamoTableInfo of(String tableName, String tableStatus, 
                                   List<Map<String, String>> keySchema, 
                                   List<Map<String, String>> attributeDefinitions) {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalArgumentException("Table name cannot be null or empty");
        }
        if (keySchema == null || keySchema.isEmpty()) {
            throw new IllegalArgumentException("Key schema cannot be null or empty");
        }
        
        Instant now = Instant.now();
        return new DynamoTableInfo(
            tableName.trim(),
            tableStatus != null ? tableStatus : "CREATING",
            0,
            0,
            keySchema,
            attributeDefinitions != null ? attributeDefinitions : List.of(),
            List.of(),
            List.of(),
            Map.of(),
            "PROVISIONED",
            now,
            now,
            Map.of()
        );
    }
    
    /**
     * Creates a DynamoDB table info with all information.
     * 
     * @param tableName the name of the table
     * @param tableStatus the status of the table
     * @param tableSizeBytes the size of the table in bytes
     * @param itemCount the number of items in the table
     * @param keySchema the key schema of the table
     * @param attributeDefinitions attribute definitions
     * @param localSecondaryIndexes local secondary indexes
     * @param globalSecondaryIndexes global secondary indexes
     * @param provisionedThroughput provisioned throughput
     * @param billingMode the billing mode
     * @param createdTimestamp timestamp when the table was created
     * @param lastModifiedTimestamp timestamp when the table was last modified
     * @param tags table tags
     * @return new DynamoTableInfo instance
     * @throws IllegalArgumentException if tableName is null or empty, or keySchema is null
     */
    public static DynamoTableInfo of(String tableName, String tableStatus, long tableSizeBytes, 
                                   long itemCount, List<Map<String, String>> keySchema, 
                                   List<Map<String, String>> attributeDefinitions,
                                   List<Map<String, Object>> localSecondaryIndexes,
                                   List<Map<String, Object>> globalSecondaryIndexes,
                                   Map<String, Object> provisionedThroughput, String billingMode,
                                   Instant createdTimestamp, Instant lastModifiedTimestamp,
                                   Map<String, String> tags) {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalArgumentException("Table name cannot be null or empty");
        }
        if (keySchema == null || keySchema.isEmpty()) {
            throw new IllegalArgumentException("Key schema cannot be null or empty");
        }
        
        return new DynamoTableInfo(
            tableName.trim(),
            tableStatus != null ? tableStatus : "CREATING",
            Math.max(tableSizeBytes, 0),
            Math.max(itemCount, 0),
            keySchema,
            attributeDefinitions != null ? attributeDefinitions : List.of(),
            localSecondaryIndexes != null ? localSecondaryIndexes : List.of(),
            globalSecondaryIndexes != null ? globalSecondaryIndexes : List.of(),
            provisionedThroughput != null ? provisionedThroughput : Map.of(),
            billingMode != null ? billingMode : "PROVISIONED",
            createdTimestamp != null ? createdTimestamp : Instant.now(),
            lastModifiedTimestamp != null ? lastModifiedTimestamp : Instant.now(),
            tags != null ? tags : Map.of()
        );
    }
    
    /**
     * Creates a copy of this DynamoTableInfo with updated status.
     * 
     * @param tableStatus new table status
     * @return new DynamoTableInfo instance with updated status
     */
    public DynamoTableInfo withStatus(String tableStatus) {
        return new DynamoTableInfo(
            this.tableName,
            tableStatus != null ? tableStatus : this.tableStatus,
            this.tableSizeBytes,
            this.itemCount,
            this.keySchema,
            this.attributeDefinitions,
            this.localSecondaryIndexes,
            this.globalSecondaryIndexes,
            this.provisionedThroughput,
            this.billingMode,
            this.createdTimestamp,
            this.lastModifiedTimestamp,
            this.tags
        );
    }
    
    /**
     * Creates a copy of this DynamoTableInfo with updated size and item count.
     * 
     * @param tableSizeBytes new table size in bytes
     * @param itemCount new item count
     * @return new DynamoTableInfo instance with updated size and item count
     */
    public DynamoTableInfo withSizeAndCount(long tableSizeBytes, long itemCount) {
        return new DynamoTableInfo(
            this.tableName,
            this.tableStatus,
            Math.max(tableSizeBytes, 0),
            Math.max(itemCount, 0),
            this.keySchema,
            this.attributeDefinitions,
            this.localSecondaryIndexes,
            this.globalSecondaryIndexes,
            this.provisionedThroughput,
            this.billingMode,
            this.createdTimestamp,
            this.lastModifiedTimestamp,
            this.tags
        );
    }
    
    /**
     * Creates a copy of this DynamoTableInfo with updated tags.
     * 
     * @param tags new tags map
     * @return new DynamoTableInfo instance with updated tags
     */
    public DynamoTableInfo withTags(Map<String, String> tags) {
        return new DynamoTableInfo(
            this.tableName,
            this.tableStatus,
            this.tableSizeBytes,
            this.itemCount,
            this.keySchema,
            this.attributeDefinitions,
            this.localSecondaryIndexes,
            this.globalSecondaryIndexes,
            this.provisionedThroughput,
            this.billingMode,
            this.createdTimestamp,
            this.lastModifiedTimestamp,
            tags != null ? tags : Map.of()
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
     * Checks if this table is active.
     * 
     * @return true if table status is ACTIVE, false otherwise
     */
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(tableStatus);
    }
    
    /**
     * Checks if this table is being created.
     * 
     * @return true if table status is CREATING, false otherwise
     */
    public boolean isCreating() {
        return "CREATING".equalsIgnoreCase(tableStatus);
    }
    
    /**
     * Checks if this table is being deleted.
     * 
     * @return true if table status is DELETING, false otherwise
     */
    public boolean isDeleting() {
        return "DELETING".equalsIgnoreCase(tableStatus);
    }
    
    /**
     * Checks if this table is being updated.
     * 
     * @return true if table status is UPDATING, false otherwise
     */
    public boolean isUpdating() {
        return "UPDATING".equalsIgnoreCase(tableStatus);
    }
    
    /**
     * Checks if this table is provisioned.
     * 
     * @return true if billing mode is PROVISIONED, false otherwise
     */
    public boolean isProvisioned() {
        return "PROVISIONED".equalsIgnoreCase(billingMode);
    }
    
    /**
     * Checks if this table is on-demand.
     * 
     * @return true if billing mode is PAY_PER_REQUEST, false otherwise
     */
    public boolean isOnDemand() {
        return "PAY_PER_REQUEST".equalsIgnoreCase(billingMode);
    }
    
    /**
     * Checks if this table has local secondary indexes.
     * 
     * @return true if local secondary indexes exist, false otherwise
     */
    public boolean hasLocalSecondaryIndexes() {
        return localSecondaryIndexes != null && !localSecondaryIndexes.isEmpty();
    }
    
    /**
     * Checks if this table has global secondary indexes.
     * 
     * @return true if global secondary indexes exist, false otherwise
     */
    public boolean hasGlobalSecondaryIndexes() {
        return globalSecondaryIndexes != null && !globalSecondaryIndexes.isEmpty();
    }
    
    /**
     * Checks if this table has items.
     * 
     * @return true if item count > 0, false otherwise
     */
    public boolean hasItems() {
        return itemCount > 0;
    }
    
    /**
     * Checks if this table is empty.
     * 
     * @return true if item count == 0, false otherwise
     */
    public boolean isEmpty() {
        return itemCount == 0;
    }
    
    /**
     * Gets the age of this table in seconds.
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
     * Checks if this table is older than the specified number of seconds.
     * 
     * @param seconds the number of seconds to check against
     * @return true if table is older than specified seconds, false otherwise
     */
    public boolean isOlderThan(int seconds) {
        return getAgeInSeconds() > seconds;
    }
    
    /**
     * Gets the primary key attribute names.
     * 
     * @return list of primary key attribute names
     */
    public List<String> getPrimaryKeyAttributeNames() {
        if (keySchema == null || keySchema.isEmpty()) {
            return List.of();
        }
        
        return keySchema.stream()
                .map(key -> key.get("AttributeName"))
                .filter(Objects::nonNull)
                .toList();
    }
    
    /**
     * Gets the partition key attribute name.
     * 
     * @return partition key attribute name or null if not found
     */
    public String getPartitionKeyAttributeName() {
        if (keySchema == null || keySchema.isEmpty()) {
            return null;
        }
        
        return keySchema.stream()
                .filter(key -> "HASH".equals(key.get("KeyType")))
                .map(key -> key.get("AttributeName"))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Gets the sort key attribute name.
     * 
     * @return sort key attribute name or null if not found
     */
    public String getSortKeyAttributeName() {
        if (keySchema == null || keySchema.isEmpty()) {
            return null;
        }
        
        return keySchema.stream()
                .filter(key -> "RANGE".equals(key.get("KeyType")))
                .map(key -> key.get("AttributeName"))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Checks if this table has a sort key.
     * 
     * @return true if sort key exists, false otherwise
     */
    public boolean hasSortKey() {
        return getSortKeyAttributeName() != null;
    }
    
    /**
     * Gets the table size in MB.
     * 
     * @return table size in MB
     */
    public double getTableSizeInMB() {
        return tableSizeBytes / (1024.0 * 1024.0);
    }
    
    /**
     * Gets the table size in GB.
     * 
     * @return table size in GB
     */
    public double getTableSizeInGB() {
        return tableSizeBytes / (1024.0 * 1024.0 * 1024.0);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DynamoTableInfo that = (DynamoTableInfo) obj;
        return tableSizeBytes == that.tableSizeBytes &&
               itemCount == that.itemCount &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(tableStatus, that.tableStatus) &&
               Objects.equals(keySchema, that.keySchema) &&
               Objects.equals(attributeDefinitions, that.attributeDefinitions) &&
               Objects.equals(localSecondaryIndexes, that.localSecondaryIndexes) &&
               Objects.equals(globalSecondaryIndexes, that.globalSecondaryIndexes) &&
               Objects.equals(provisionedThroughput, that.provisionedThroughput) &&
               Objects.equals(billingMode, that.billingMode) &&
               Objects.equals(createdTimestamp, that.createdTimestamp) &&
               Objects.equals(lastModifiedTimestamp, that.lastModifiedTimestamp) &&
               Objects.equals(tags, that.tags);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(tableName, tableStatus, tableSizeBytes, itemCount, keySchema, 
                           attributeDefinitions, localSecondaryIndexes, globalSecondaryIndexes, 
                           provisionedThroughput, billingMode, createdTimestamp, lastModifiedTimestamp, tags);
    }
    
    @Override
    public String toString() {
        return "DynamoTableInfo{" +
               "tableName='" + tableName + '\'' +
               ", tableStatus='" + tableStatus + '\'' +
               ", tableSizeBytes=" + tableSizeBytes +
               ", itemCount=" + itemCount +
               ", keySchema=" + keySchema +
               ", attributeDefinitions=" + attributeDefinitions +
               ", localSecondaryIndexes=" + localSecondaryIndexes +
               ", globalSecondaryIndexes=" + globalSecondaryIndexes +
               ", provisionedThroughput=" + provisionedThroughput +
               ", billingMode='" + billingMode + '\'' +
               ", createdTimestamp=" + createdTimestamp +
               ", lastModifiedTimestamp=" + lastModifiedTimestamp +
               ", tags=" + tags +
               '}';
    }
}
