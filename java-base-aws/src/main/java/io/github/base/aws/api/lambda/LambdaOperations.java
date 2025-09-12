package io.github.base.aws.api.lambda;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Contract for AWS Lambda operations.
 *
 * <p>This interface defines the standard operations for interacting with AWS Lambda,
 * including function invocation, management, and configuration.
 *
 * <p>All operations are designed to be thread-safe and can be used in concurrent environments.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface LambdaOperations {

    /**
     * Invokes a Lambda function synchronously.
     *
     * @param functionName the name of the Lambda function
     * @param payload the payload to send to the function
     * @return invoke result containing response and metadata
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if invocation fails
     */
    LambdaInvokeResult invoke(String functionName, String payload);

    /**
     * Invokes a Lambda function synchronously with qualifier.
     *
     * @param functionName the name of the Lambda function
     * @param qualifier the version or alias of the function
     * @param payload the payload to send to the function
     * @return invoke result containing response and metadata
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if invocation fails
     */
    LambdaInvokeResult invoke(String functionName, String qualifier, String payload);

    /**
     * Invokes a Lambda function asynchronously.
     *
     * @param functionName the name of the Lambda function
     * @param payload the payload to send to the function
     * @return invoke result containing response and metadata
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if invocation fails
     */
    LambdaInvokeResult invokeAsync(String functionName, String payload);

    /**
     * Invokes a Lambda function asynchronously with qualifier.
     *
     * @param functionName the name of the Lambda function
     * @param qualifier the version or alias of the function
     * @param payload the payload to send to the function
     * @return invoke result containing response and metadata
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if invocation fails
     */
    LambdaInvokeResult invokeAsync(String functionName, String qualifier, String payload);

    /**
     * Invokes a Lambda function with custom parameters.
     *
     * @param functionName the name of the Lambda function
     * @param qualifier the version or alias of the function
     * @param payload the payload to send to the function
     * @param invocationType the type of invocation (RequestResponse, Event, etc.)
     * @param logType the type of logging (None, Tail)
     * @return invoke result containing response and metadata
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if invocation fails
     */
    LambdaInvokeResult invoke(String functionName, String qualifier, String payload,
                             String invocationType, String logType);

    /**
     * Creates a Lambda function.
     *
     * @param functionName the name of the function to create
     * @param runtime the runtime for the function
     * @param role the IAM role for the function
     * @param handler the handler for the function
     * @param code the code for the function
     * @return function information or null if creation failed
     * @throws IllegalArgumentException if functionName, runtime, role, handler, or code is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    LambdaFunctionInfo createFunction(String functionName, String runtime, String role,
                                    String handler, Map<String, Object> code);

    /**
     * Creates a Lambda function with all options.
     *
     * @param functionName the name of the function to create
     * @param runtime the runtime for the function
     * @param role the IAM role for the function
     * @param handler the handler for the function
     * @param code the code for the function
     * @param description the description of the function
     * @param timeout the timeout in seconds
     * @param memorySize the memory size in MB
     * @param environment environment variables
     * @return function information or null if creation failed
     * @throws IllegalArgumentException if functionName, runtime, role, handler, or code is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    LambdaFunctionInfo createFunction(String functionName, String runtime, String role,
                                    String handler, Map<String, Object> code, String description,
                                    int timeout, int memorySize, Map<String, String> environment);

    /**
     * Updates a Lambda function.
     *
     * @param functionName the name of the function to update
     * @param code the new code for the function
     * @return function information or null if update failed
     * @throws IllegalArgumentException if functionName is null or empty, or code is null
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    LambdaFunctionInfo updateFunction(String functionName, Map<String, Object> code);

    /**
     * Updates a Lambda function with all options.
     *
     * @param functionName the name of the function to update
     * @param code the new code for the function
     * @param description the new description of the function
     * @param timeout the new timeout in seconds
     * @param memorySize the new memory size in MB
     * @param environment new environment variables
     * @return function information or null if update failed
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    LambdaFunctionInfo updateFunction(String functionName, Map<String, Object> code, String description,
                                    int timeout, int memorySize, Map<String, String> environment);

    /**
     * Deletes a Lambda function.
     *
     * @param functionName the name of the function to delete
     * @return true if function was deleted successfully, false otherwise
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteFunction(String functionName);

    /**
     * Gets function information.
     *
     * @param functionName the name of the function
     * @return function information or null if function not found
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    LambdaFunctionInfo getFunctionInfo(String functionName);

    /**
     * Gets function information with qualifier.
     *
     * @param functionName the name of the function
     * @param qualifier the version or alias of the function
     * @return function information or null if function not found
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    LambdaFunctionInfo getFunctionInfo(String functionName, String qualifier);

    /**
     * Lists all functions.
     *
     * @return list of function information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<LambdaFunctionInfo> listFunctions();

    /**
     * Lists functions with a name prefix.
     *
     * @param prefix the prefix to filter function names
     * @return list of function information
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<LambdaFunctionInfo> listFunctions(String prefix);

    /**
     * Lists function versions.
     *
     * @param functionName the name of the function
     * @return list of function information for all versions
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<LambdaFunctionInfo> listFunctionVersions(String functionName);

    /**
     * Lists function aliases.
     *
     * @param functionName the name of the function
     * @return list of alias information
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<Map<String, Object>> listFunctionAliases(String functionName);

    /**
     * Creates a function alias.
     *
     * @param functionName the name of the function
     * @param aliasName the name of the alias
     * @param functionVersion the version to alias
     * @return alias information or null if creation failed
     * @throws IllegalArgumentException if functionName, aliasName, or functionVersion is null or empty
     * @throws io.github.base.aws.api.common.AwsException if creation fails
     */
    Map<String, Object> createAlias(String functionName, String aliasName, String functionVersion);

    /**
     * Deletes a function alias.
     *
     * @param functionName the name of the function
     * @param aliasName the name of the alias
     * @return true if alias was deleted successfully, false otherwise
     * @throws IllegalArgumentException if functionName or aliasName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean deleteAlias(String functionName, String aliasName);

    /**
     * Publishes a new version of a function.
     *
     * @param functionName the name of the function
     * @param description the description of the version
     * @return function information for the new version or null if publishing failed
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if publishing fails
     */
    LambdaFunctionInfo publishVersion(String functionName, String description);

    /**
     * Gets function configuration.
     *
     * @param functionName the name of the function
     * @return function configuration or null if function not found
     * @throws IllegalArgumentException if functionName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if retrieval fails
     */
    Map<String, Object> getFunctionConfiguration(String functionName);

    /**
     * Updates function configuration.
     *
     * @param functionName the name of the function
     * @param configuration the new configuration
     * @return true if configuration was updated successfully, false otherwise
     * @throws IllegalArgumentException if functionName is null or empty, or configuration is null
     * @throws io.github.base.aws.api.common.AwsException if update fails
     */
    boolean updateFunctionConfiguration(String functionName, Map<String, Object> configuration);

    /**
     * Asynchronously invokes a Lambda function.
     *
     * @param functionName the name of the Lambda function
     * @param payload the payload to send to the function
     * @return CompletableFuture containing invoke result
     */
    CompletableFuture<LambdaInvokeResult> invokeAsyncFuture(String functionName, String payload);

    /**
     * Asynchronously invokes a Lambda function with qualifier.
     *
     * @param functionName the name of the Lambda function
     * @param qualifier the version or alias of the function
     * @param payload the payload to send to the function
     * @return CompletableFuture containing invoke result
     */
    CompletableFuture<LambdaInvokeResult> invokeAsyncFuture(String functionName, String qualifier, String payload);

    /**
     * Gets the default function name.
     *
     * @return the default function name or null if not set
     */
    String getDefaultFunctionName();

    /**
     * Sets the default function name.
     *
     * @param functionName the function name to set as default
     * @throws IllegalArgumentException if functionName is null or empty
     */
    void setDefaultFunctionName(String functionName);

    /**
     * Gets the Lambda operations capabilities.
     *
     * @return map of capability flags
     */
    Map<String, Boolean> getCapabilities();
}
