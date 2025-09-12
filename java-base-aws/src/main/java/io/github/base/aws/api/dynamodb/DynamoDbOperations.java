package io.github.base.aws.api.dynamodb;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Contract for Amazon DynamoDB operations.
 *
 * <p>This interface defines the standard operations for interacting with Amazon DynamoDB,
 * including table management, item operations, and queries.
 *
 * <p>All operations are designed to be thread-safe and can be used in concurrent environments.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface DynamoDbOperations {

    /**
     * Puts an item into a table.
     *
     * @param tableName the name of the table
     * @param item the item to put
     * @return true if item was put successfully, false otherwise
     * @throws IllegalArgumentException if tableName is null or empty, or item is null
     * @throws io.github.base.aws.api.common.AwsException if put fails
     */
    boolean putItem(String tableName, Map<String, Object> item);

    /**
     * Puts an item into a table with conditions.
     *
     * @param tableName the name of the table
     * @param item the item to put
     * @param conditionExpression the condition expression
     * @return true if item was put successfully, false otherwise
     * @throws IllegalArgumentException if tableName is null or empty, or item is null
     * @throws io.github.base.aws.api.common.AwsException if put fails
     */
    boolean putItem(String tableName, Map<String, Object> item, String conditionExpression);

    /**
     * Gets an item from a table.
     *
     * @param tableName the name of the table
     * @param key the key of the item
     * @return the item or null if not found
     * @throws IllegalArgumentException if tableName is null or empty, or key is null
     * @throws io.github.base.aws.api.common.AwsException if get fails
     */
    Map<String, Object> getItem(String tableName, Map<String, Object> key);

    /**
     * Gets an item from a table with attributes.
     *
     * @param tableName the name of the table
     * @param key the key of the item
     * @param attributesToGet list of attributes to get
     * @return the item or null if not found
     * @throws IllegalArgumentException if tableName is null or empty, or key is null
     * @throws io.github.base.aws.api.common.AwsException if get fails
     */
    Map<String, Object> getItem(String tableName, Map<String, Object> key, List<String> attributesToGet);

    /**
     * Updates an item in a table.
     *
     * @param tableName the name of the table
     * @param key the key of the item
     * @param updateExpression the update expression
     * @param expressionAttributeValues expression attribute values
     * @return true if item was updated successfully, false otherwise
     * @throws IllegalArgumentException if tableName is null or empty, or key is null
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateItem(String tableName, Map<String, Object> key, String updateExpression,
                      Map<String, Object> expressionAttributeValues);

    /**
     * Updates an item in a table with conditions.
     *
     * @param tableName the name of the table
     * @param key the key of the item
     * @param updateExpression the update expression
     * @param expressionAttributeValues expression attribute values
     * @param conditionExpression the condition expression
     * @return true if item was updated successfully, false otherwise
     * @throws IllegalArgumentException if tableName is null or empty, or key is null
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateItem(String tableName, Map<String, Object> key, String updateExpression,
                      Map<String, Object> expressionAttributeValues, String conditionExpression);

    /**
     * Deletes an item from a table.
     *
     * @param tableName the name of the table
     * @param key the key of the item
     * @return true if item was deleted successfully, false otherwise
     * @throws IllegalArgumentException if tableName is null or empty, or key is null
     * @throws io.github.base.aws.api.common.AwsException if delete fails
     */
    boolean deleteItem(String tableName, Map<String, Object> key);

    /**
     * Deletes an item from a table with conditions.
     *
     * @param tableName the name of the table
     * @param key the key of the item
     * @param conditionExpression the condition expression
     * @return true if item was deleted successfully, false otherwise
     * @throws IllegalArgumentException if tableName is null or empty, or key is null
     * @throws io.github.base.aws.api.common.AwsException if delete fails
     */
    boolean deleteItem(String tableName, Map<String, Object> key, String conditionExpression);

    /**
     * Queries a table.
     *
     * @param tableName the name of the table
     * @param queryConfig the query configuration
     * @return list of items matching the query
     * @throws IllegalArgumentException if tableName is null or empty, or queryConfig is null
     * @throws io.github.base.aws.api.common.AwsException if query fails
     */
    List<Map<String, Object>> query(String tableName, DynamoQueryConfig queryConfig);

    /**
     * Queries a table with pagination.
     *
     * @param tableName the name of the table
     * @param queryConfig the query configuration
     * @param limit maximum number of items to return
     * @param exclusiveStartKey exclusive start key for pagination
     * @return map containing items and last evaluated key
     * @throws IllegalArgumentException if tableName is null or empty, or queryConfig is null
     * @throws io.github.base.aws.api.common.AwsException if query fails
     */
    Map<String, Object> query(String tableName, DynamoQueryConfig queryConfig, int limit,
                             Map<String, Object> exclusiveStartKey);

    /**
     * Scans a table.
     *
     * @param tableName the name of the table
     * @param filterExpression the filter expression
     * @param expressionAttributeValues expression attribute values
     * @return list of items matching the scan
     * @throws IllegalArgumentException if tableName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if scan fails
     */
    List<Map<String, Object>> scan(String tableName, String filterExpression,
                                  Map<String, Object> expressionAttributeValues);

    /**
     * Scans a table with pagination.
     *
     * @param tableName the name of the table
     * @param filterExpression the filter expression
     * @param expressionAttributeValues expression attribute values
     * @param limit maximum number of items to return
     * @param exclusiveStartKey exclusive start key for pagination
     * @return map containing items and last evaluated key
     * @throws IllegalArgumentException if tableName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if scan fails
     */
    Map<String, Object> scan(String tableName, String filterExpression,
                            Map<String, Object> expressionAttributeValues, int limit,
                            Map<String, Object> exclusiveStartKey);

    /**
     * Creates a table.
     *
     * @param tableName the name of the table to create
     * @param keySchema the key schema for the table
     * @param attributeDefinitions attribute definitions
     * @param provisionedThroughput provisioned throughput
     * @return table information or null if creation failed
     * @throws IllegalArgumentException if tableName is null or empty, or keySchema is null
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    DynamoTableInfo createTable(String tableName, List<Map<String, String>> keySchema,
                               List<Map<String, String>> attributeDefinitions,
                               Map<String, Object> provisionedThroughput);

    /**
     * Creates a table with all options.
     *
     * @param tableName the name of the table to create
     * @param keySchema the key schema for the table
     * @param attributeDefinitions attribute definitions
     * @param provisionedThroughput provisioned throughput
     * @param localSecondaryIndexes local secondary indexes
     * @param globalSecondaryIndexes global secondary indexes
     * @param tags table tags
     * @return table information or null if creation failed
     * @throws IllegalArgumentException if tableName is null or empty, or keySchema is null
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    DynamoTableInfo createTable(String tableName, List<Map<String, String>> keySchema,
                               List<Map<String, String>> attributeDefinitions,
                               Map<String, Object> provisionedThroughput,
                               List<Map<String, Object>> localSecondaryIndexes,
                               List<Map<String, Object>> globalSecondaryIndexes,
                               Map<String, String> tags);

    /**
     * Deletes a table.
     *
     * @param tableName the name of the table to delete
     * @return true if table was deleted successfully, false otherwise
     * @throws IllegalArgumentException if tableName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteTable(String tableName);

    /**
     * Gets table information.
     *
     * @param tableName the name of the table
     * @return table information or null if table not found
     * @throws IllegalArgumentException if tableName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    DynamoTableInfo getTableInfo(String tableName);

    /**
     * Lists all tables.
     *
     * @return list of table information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<DynamoTableInfo> listTables();

    /**
     * Lists tables with a name prefix.
     *
     * @param prefix the prefix to filter table names
     * @return list of table information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<DynamoTableInfo> listTables(String prefix);

    /**
     * Updates table throughput.
     *
     * @param tableName the name of the table
     * @param provisionedThroughput new provisioned throughput
     * @return true if throughput was updated successfully, false otherwise
     * @throws IllegalArgumentException if tableName is null or empty, or provisionedThroughput is null
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateTableThroughput(String tableName, Map<String, Object> provisionedThroughput);

    /**
     * Updates table global secondary indexes.
     *
     * @param tableName the name of the table
     * @param globalSecondaryIndexes new global secondary indexes
     * @return true if indexes were updated successfully, false otherwise
     * @throws IllegalArgumentException if tableName is null or empty, or globalSecondaryIndexes is null
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateTableGlobalSecondaryIndexes(String tableName, List<Map<String, Object>> globalSecondaryIndexes);

    /**
     * Waits for table to be active.
     *
     * @param tableName the name of the table
     * @param maxWaitTimeSeconds maximum time to wait in seconds
     * @return true if table became active, false if timeout
     * @throws IllegalArgumentException if tableName is null or empty, or maxWaitTimeSeconds <= 0
     * @throws io.github.base.aws.api.common.AwsException if wait fails
     */
    boolean waitForTableActive(String tableName, int maxWaitTimeSeconds);

    /**
     * Waits for table to be deleted.
     *
     * @param tableName the name of the table
     * @param maxWaitTimeSeconds maximum time to wait in seconds
     * @return true if table was deleted, false if timeout
     * @throws IllegalArgumentException if tableName is null or empty, or maxWaitTimeSeconds <= 0
     * @throws io.github.base.aws.api.common.AwsException if wait fails
     */
    boolean waitForTableDeleted(String tableName, int maxWaitTimeSeconds);

    /**
     * Asynchronously puts an item into a table.
     *
     * @param tableName the name of the table
     * @param item the item to put
     * @return CompletableFuture containing success status
     */
    CompletableFuture<Boolean> putItemAsync(String tableName, Map<String, Object> item);

    /**
     * Asynchronously gets an item from a table.
     *
     * @param tableName the name of the table
     * @param key the key of the item
     * @return CompletableFuture containing the item
     */
    CompletableFuture<Map<String, Object>> getItemAsync(String tableName, Map<String, Object> key);

    /**
     * Asynchronously queries a table.
     *
     * @param tableName the name of the table
     * @param queryConfig the query configuration
     * @return CompletableFuture containing list of items
     */
    CompletableFuture<List<Map<String, Object>>> queryAsync(String tableName, DynamoQueryConfig queryConfig);

    /**
     * Gets the default table name.
     *
     * @return the default table name or null if not set
     */
    String getDefaultTableName();

    /**
     * Sets the default table name.
     *
     * @param tableName the table name to set as default
     * @throws IllegalArgumentException if tableName is null or empty
     */
    void setDefaultTableName(String tableName);

    /**
     * Gets the DynamoDB operations capabilities.
     *
     * @return map of capability flags
     */
    Map<String, Boolean> getCapabilities();
}
