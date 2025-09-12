package io.github.base.aws.api.dynamodb;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing DynamoDB query configuration.
 * 
 * <p>This record contains all the configuration options for querying a DynamoDB table,
 * including key conditions, filters, and pagination settings.
 * 
 * @param keyConditionExpression the key condition expression
 * @param filterExpression the filter expression
 * @param expressionAttributeNames expression attribute names
 * @param expressionAttributeValues expression attribute values
 * @param indexName the name of the index to query
 * @param scanIndexForward whether to scan index forward
 * @param consistentRead whether to use consistent read
 * @param returnConsumedCapacity whether to return consumed capacity
 * @param projectionExpression the projection expression
 * @param attributesToGet list of attributes to get
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record DynamoQueryConfig(
    String keyConditionExpression,
    String filterExpression,
    Map<String, String> expressionAttributeNames,
    Map<String, Object> expressionAttributeValues,
    String indexName,
    boolean scanIndexForward,
    boolean consistentRead,
    boolean returnConsumedCapacity,
    String projectionExpression,
    List<String> attributesToGet
) {
    
    /**
     * Creates a DynamoDB query config with key condition expression.
     * 
     * @param keyConditionExpression the key condition expression
     * @return new DynamoQueryConfig instance
     * @throws IllegalArgumentException if keyConditionExpression is null or empty
     */
    public static DynamoQueryConfig of(String keyConditionExpression) {
        if (keyConditionExpression == null || keyConditionExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("Key condition expression cannot be null or empty");
        }
        
        return new DynamoQueryConfig(
            keyConditionExpression.trim(),
            null,
            Map.of(),
            Map.of(),
            null,
            true,
            false,
            false,
            null,
            List.of()
        );
    }
    
    /**
     * Creates a DynamoDB query config with key condition expression and values.
     * 
     * @param keyConditionExpression the key condition expression
     * @param expressionAttributeValues expression attribute values
     * @return new DynamoQueryConfig instance
     * @throws IllegalArgumentException if keyConditionExpression is null or empty
     */
    public static DynamoQueryConfig of(String keyConditionExpression, Map<String, Object> expressionAttributeValues) {
        if (keyConditionExpression == null || keyConditionExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("Key condition expression cannot be null or empty");
        }
        
        return new DynamoQueryConfig(
            keyConditionExpression.trim(),
            null,
            Map.of(),
            expressionAttributeValues != null ? expressionAttributeValues : Map.of(),
            null,
            true,
            false,
            false,
            null,
            List.of()
        );
    }
    
    /**
     * Creates a DynamoDB query config with all options.
     * 
     * @param keyConditionExpression the key condition expression
     * @param filterExpression the filter expression
     * @param expressionAttributeNames expression attribute names
     * @param expressionAttributeValues expression attribute values
     * @param indexName the name of the index to query
     * @param scanIndexForward whether to scan index forward
     * @param consistentRead whether to use consistent read
     * @param returnConsumedCapacity whether to return consumed capacity
     * @param projectionExpression the projection expression
     * @param attributesToGet list of attributes to get
     * @return new DynamoQueryConfig instance
     * @throws IllegalArgumentException if keyConditionExpression is null or empty
     */
    public static DynamoQueryConfig of(String keyConditionExpression, String filterExpression,
                                      Map<String, String> expressionAttributeNames,
                                      Map<String, Object> expressionAttributeValues,
                                      String indexName, boolean scanIndexForward, boolean consistentRead,
                                      boolean returnConsumedCapacity, String projectionExpression,
                                      List<String> attributesToGet) {
        if (keyConditionExpression == null || keyConditionExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("Key condition expression cannot be null or empty");
        }
        
        return new DynamoQueryConfig(
            keyConditionExpression.trim(),
            filterExpression,
            expressionAttributeNames != null ? expressionAttributeNames : Map.of(),
            expressionAttributeValues != null ? expressionAttributeValues : Map.of(),
            indexName,
            scanIndexForward,
            consistentRead,
            returnConsumedCapacity,
            projectionExpression,
            attributesToGet != null ? attributesToGet : List.of()
        );
    }
    
    /**
     * Creates a copy of this DynamoQueryConfig with updated filter expression.
     * 
     * @param filterExpression new filter expression
     * @return new DynamoQueryConfig instance with updated filter expression
     */
    public DynamoQueryConfig withFilterExpression(String filterExpression) {
        return new DynamoQueryConfig(
            this.keyConditionExpression,
            filterExpression,
            this.expressionAttributeNames,
            this.expressionAttributeValues,
            this.indexName,
            this.scanIndexForward,
            this.consistentRead,
            this.returnConsumedCapacity,
            this.projectionExpression,
            this.attributesToGet
        );
    }
    
    /**
     * Creates a copy of this DynamoQueryConfig with updated expression attribute names.
     * 
     * @param expressionAttributeNames new expression attribute names
     * @return new DynamoQueryConfig instance with updated expression attribute names
     */
    public DynamoQueryConfig withExpressionAttributeNames(Map<String, String> expressionAttributeNames) {
        return new DynamoQueryConfig(
            this.keyConditionExpression,
            this.filterExpression,
            expressionAttributeNames != null ? expressionAttributeNames : Map.of(),
            this.expressionAttributeValues,
            this.indexName,
            this.scanIndexForward,
            this.consistentRead,
            this.returnConsumedCapacity,
            this.projectionExpression,
            this.attributesToGet
        );
    }
    
    /**
     * Creates a copy of this DynamoQueryConfig with updated expression attribute values.
     * 
     * @param expressionAttributeValues new expression attribute values
     * @return new DynamoQueryConfig instance with updated expression attribute values
     */
    public DynamoQueryConfig withExpressionAttributeValues(Map<String, Object> expressionAttributeValues) {
        return new DynamoQueryConfig(
            this.keyConditionExpression,
            this.filterExpression,
            this.expressionAttributeNames,
            expressionAttributeValues != null ? expressionAttributeValues : Map.of(),
            this.indexName,
            this.scanIndexForward,
            this.consistentRead,
            this.returnConsumedCapacity,
            this.projectionExpression,
            this.attributesToGet
        );
    }
    
    /**
     * Creates a copy of this DynamoQueryConfig with updated index name.
     * 
     * @param indexName new index name
     * @return new DynamoQueryConfig instance with updated index name
     */
    public DynamoQueryConfig withIndexName(String indexName) {
        return new DynamoQueryConfig(
            this.keyConditionExpression,
            this.filterExpression,
            this.expressionAttributeNames,
            this.expressionAttributeValues,
            indexName,
            this.scanIndexForward,
            this.consistentRead,
            this.returnConsumedCapacity,
            this.projectionExpression,
            this.attributesToGet
        );
    }
    
    /**
     * Creates a copy of this DynamoQueryConfig with updated scan index forward setting.
     * 
     * @param scanIndexForward new scan index forward setting
     * @return new DynamoQueryConfig instance with updated scan index forward setting
     */
    public DynamoQueryConfig withScanIndexForward(boolean scanIndexForward) {
        return new DynamoQueryConfig(
            this.keyConditionExpression,
            this.filterExpression,
            this.expressionAttributeNames,
            this.expressionAttributeValues,
            this.indexName,
            scanIndexForward,
            this.consistentRead,
            this.returnConsumedCapacity,
            this.projectionExpression,
            this.attributesToGet
        );
    }
    
    /**
     * Creates a copy of this DynamoQueryConfig with updated consistent read setting.
     * 
     * @param consistentRead new consistent read setting
     * @return new DynamoQueryConfig instance with updated consistent read setting
     */
    public DynamoQueryConfig withConsistentRead(boolean consistentRead) {
        return new DynamoQueryConfig(
            this.keyConditionExpression,
            this.filterExpression,
            this.expressionAttributeNames,
            this.expressionAttributeValues,
            this.indexName,
            this.scanIndexForward,
            consistentRead,
            this.returnConsumedCapacity,
            this.projectionExpression,
            this.attributesToGet
        );
    }
    
    /**
     * Creates a copy of this DynamoQueryConfig with updated return consumed capacity setting.
     * 
     * @param returnConsumedCapacity new return consumed capacity setting
     * @return new DynamoQueryConfig instance with updated return consumed capacity setting
     */
    public DynamoQueryConfig withReturnConsumedCapacity(boolean returnConsumedCapacity) {
        return new DynamoQueryConfig(
            this.keyConditionExpression,
            this.filterExpression,
            this.expressionAttributeNames,
            this.expressionAttributeValues,
            this.indexName,
            this.scanIndexForward,
            this.consistentRead,
            returnConsumedCapacity,
            this.projectionExpression,
            this.attributesToGet
        );
    }
    
    /**
     * Creates a copy of this DynamoQueryConfig with updated projection expression.
     * 
     * @param projectionExpression new projection expression
     * @return new DynamoQueryConfig instance with updated projection expression
     */
    public DynamoQueryConfig withProjectionExpression(String projectionExpression) {
        return new DynamoQueryConfig(
            this.keyConditionExpression,
            this.filterExpression,
            this.expressionAttributeNames,
            this.expressionAttributeValues,
            this.indexName,
            this.scanIndexForward,
            this.consistentRead,
            this.returnConsumedCapacity,
            projectionExpression,
            this.attributesToGet
        );
    }
    
    /**
     * Creates a copy of this DynamoQueryConfig with updated attributes to get.
     * 
     * @param attributesToGet new attributes to get list
     * @return new DynamoQueryConfig instance with updated attributes to get
     */
    public DynamoQueryConfig withAttributesToGet(List<String> attributesToGet) {
        return new DynamoQueryConfig(
            this.keyConditionExpression,
            this.filterExpression,
            this.expressionAttributeNames,
            this.expressionAttributeValues,
            this.indexName,
            this.scanIndexForward,
            this.consistentRead,
            this.returnConsumedCapacity,
            this.projectionExpression,
            attributesToGet != null ? attributesToGet : List.of()
        );
    }
    
    /**
     * Checks if this query config has a filter expression.
     * 
     * @return true if filter expression is set, false otherwise
     */
    public boolean hasFilterExpression() {
        return filterExpression != null && !filterExpression.trim().isEmpty();
    }
    
    /**
     * Checks if this query config has expression attribute names.
     * 
     * @return true if expression attribute names are set, false otherwise
     */
    public boolean hasExpressionAttributeNames() {
        return expressionAttributeNames != null && !expressionAttributeNames.isEmpty();
    }
    
    /**
     * Checks if this query config has expression attribute values.
     * 
     * @return true if expression attribute values are set, false otherwise
     */
    public boolean hasExpressionAttributeValues() {
        return expressionAttributeValues != null && !expressionAttributeValues.isEmpty();
    }
    
    /**
     * Checks if this query config has an index name.
     * 
     * @return true if index name is set, false otherwise
     */
    public boolean hasIndexName() {
        return indexName != null && !indexName.trim().isEmpty();
    }
    
    /**
     * Checks if this query config has a projection expression.
     * 
     * @return true if projection expression is set, false otherwise
     */
    public boolean hasProjectionExpression() {
        return projectionExpression != null && !projectionExpression.trim().isEmpty();
    }
    
    /**
     * Checks if this query config has attributes to get.
     * 
     * @return true if attributes to get are set, false otherwise
     */
    public boolean hasAttributesToGet() {
        return attributesToGet != null && !attributesToGet.isEmpty();
    }
    
    /**
     * Checks if this query config is using an index.
     * 
     * @return true if index name is set, false otherwise
     */
    public boolean isUsingIndex() {
        return hasIndexName();
    }
    
    /**
     * Checks if this query config is using consistent read.
     * 
     * @return true if consistent read is enabled, false otherwise
     */
    public boolean isUsingConsistentRead() {
        return consistentRead;
    }
    
    /**
     * Checks if this query config is scanning index forward.
     * 
     * @return true if scanning index forward, false otherwise
     */
    public boolean isScanningIndexForward() {
        return scanIndexForward;
    }
    
    /**
     * Checks if this query config is returning consumed capacity.
     * 
     * @return true if returning consumed capacity, false otherwise
     */
    public boolean isReturningConsumedCapacity() {
        return returnConsumedCapacity;
    }
    
    /**
     * Gets an expression attribute name by placeholder.
     * 
     * @param placeholder the placeholder (e.g., "#name")
     * @return the attribute name or null if not found
     */
    public String getExpressionAttributeName(String placeholder) {
        return expressionAttributeNames != null ? expressionAttributeNames.get(placeholder) : null;
    }
    
    /**
     * Gets an expression attribute value by placeholder.
     * 
     * @param placeholder the placeholder (e.g., ":value")
     * @return the attribute value or null if not found
     */
    public Object getExpressionAttributeValue(String placeholder) {
        return expressionAttributeValues != null ? expressionAttributeValues.get(placeholder) : null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DynamoQueryConfig that = (DynamoQueryConfig) obj;
        return scanIndexForward == that.scanIndexForward &&
               consistentRead == that.consistentRead &&
               returnConsumedCapacity == that.returnConsumedCapacity &&
               Objects.equals(keyConditionExpression, that.keyConditionExpression) &&
               Objects.equals(filterExpression, that.filterExpression) &&
               Objects.equals(expressionAttributeNames, that.expressionAttributeNames) &&
               Objects.equals(expressionAttributeValues, that.expressionAttributeValues) &&
               Objects.equals(indexName, that.indexName) &&
               Objects.equals(projectionExpression, that.projectionExpression) &&
               Objects.equals(attributesToGet, that.attributesToGet);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(keyConditionExpression, filterExpression, expressionAttributeNames, 
                           expressionAttributeValues, indexName, scanIndexForward, consistentRead, 
                           returnConsumedCapacity, projectionExpression, attributesToGet);
    }
    
    @Override
    public String toString() {
        return "DynamoQueryConfig{" +
               "keyConditionExpression='" + keyConditionExpression + '\'' +
               ", filterExpression='" + filterExpression + '\'' +
               ", expressionAttributeNames=" + expressionAttributeNames +
               ", expressionAttributeValues=" + expressionAttributeValues +
               ", indexName='" + indexName + '\'' +
               ", scanIndexForward=" + scanIndexForward +
               ", consistentRead=" + consistentRead +
               ", returnConsumedCapacity=" + returnConsumedCapacity +
               ", projectionExpression='" + projectionExpression + '\'' +
               ", attributesToGet=" + attributesToGet +
               '}';
    }
}
