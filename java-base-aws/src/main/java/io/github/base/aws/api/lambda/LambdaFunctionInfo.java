package io.github.base.aws.api.lambda;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing Lambda function information.
 * 
 * <p>This record contains all the information about a Lambda function including
 * its name, ARN, runtime, handler, and configuration.
 * 
 * @param functionName the name of the function
 * @param functionArn the ARN of the function
 * @param runtime the runtime for the function
 * @param role the IAM role for the function
 * @param handler the handler for the function
 * @param description the description of the function
 * @param timeout the timeout in seconds
 * @param memorySize the memory size in MB
 * @param codeSize the size of the code in bytes
 * @param lastModified timestamp when the function was last modified
 * @param lastInvocation timestamp when the function was last invoked
 * @param environment environment variables
 * @param tags function tags
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record LambdaFunctionInfo(
    String functionName,
    String functionArn,
    String runtime,
    String role,
    String handler,
    String description,
    int timeout,
    int memorySize,
    long codeSize,
    Instant lastModified,
    Instant lastInvocation,
    Map<String, String> environment,
    Map<String, String> tags
) {
    
    /**
     * Creates a Lambda function info with basic information.
     * 
     * @param functionName the name of the function
     * @param functionArn the ARN of the function
     * @param runtime the runtime for the function
     * @param role the IAM role for the function
     * @param handler the handler for the function
     * @return new LambdaFunctionInfo instance
     * @throws IllegalArgumentException if functionName, functionArn, runtime, role, or handler is null or empty
     */
    public static LambdaFunctionInfo of(String functionName, String functionArn, String runtime, 
                                      String role, String handler) {
        if (functionName == null || functionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Function name cannot be null or empty");
        }
        if (functionArn == null || functionArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Function ARN cannot be null or empty");
        }
        if (runtime == null || runtime.trim().isEmpty()) {
            throw new IllegalArgumentException("Runtime cannot be null or empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (handler == null || handler.trim().isEmpty()) {
            throw new IllegalArgumentException("Handler cannot be null or empty");
        }
        
        Instant now = Instant.now();
        return new LambdaFunctionInfo(
            functionName.trim(),
            functionArn.trim(),
            runtime.trim(),
            role.trim(),
            handler.trim(),
            null,
            3, // Default timeout
            128, // Default memory size
            0,
            now,
            null,
            Map.of(),
            Map.of()
        );
    }
    
    /**
     * Creates a Lambda function info with all information.
     * 
     * @param functionName the name of the function
     * @param functionArn the ARN of the function
     * @param runtime the runtime for the function
     * @param role the IAM role for the function
     * @param handler the handler for the function
     * @param description the description of the function
     * @param timeout the timeout in seconds
     * @param memorySize the memory size in MB
     * @param codeSize the size of the code in bytes
     * @param lastModified timestamp when the function was last modified
     * @param lastInvocation timestamp when the function was last invoked
     * @param environment environment variables
     * @param tags function tags
     * @return new LambdaFunctionInfo instance
     * @throws IllegalArgumentException if functionName, functionArn, runtime, role, or handler is null or empty
     */
    public static LambdaFunctionInfo of(String functionName, String functionArn, String runtime, 
                                      String role, String handler, String description, int timeout, 
                                      int memorySize, long codeSize, Instant lastModified, 
                                      Instant lastInvocation, Map<String, String> environment, 
                                      Map<String, String> tags) {
        if (functionName == null || functionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Function name cannot be null or empty");
        }
        if (functionArn == null || functionArn.trim().isEmpty()) {
            throw new IllegalArgumentException("Function ARN cannot be null or empty");
        }
        if (runtime == null || runtime.trim().isEmpty()) {
            throw new IllegalArgumentException("Runtime cannot be null or empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        if (handler == null || handler.trim().isEmpty()) {
            throw new IllegalArgumentException("Handler cannot be null or empty");
        }
        
        return new LambdaFunctionInfo(
            functionName.trim(),
            functionArn.trim(),
            runtime.trim(),
            role.trim(),
            handler.trim(),
            description,
            Math.max(timeout, 1),
            Math.max(memorySize, 128),
            Math.max(codeSize, 0),
            lastModified != null ? lastModified : Instant.now(),
            lastInvocation,
            environment != null ? Map.copyOf(environment) : Map.of(),
            tags != null ? Map.copyOf(tags) : Map.of()
        );
    }
    
    /**
     * Creates a copy of this LambdaFunctionInfo with updated environment variables.
     * 
     * @param environment new environment variables map
     * @return new LambdaFunctionInfo instance with updated environment variables
     */
    public LambdaFunctionInfo withEnvironment(Map<String, String> environment) {
        return new LambdaFunctionInfo(
            this.functionName,
            this.functionArn,
            this.runtime,
            this.role,
            this.handler,
            this.description,
            this.timeout,
            this.memorySize,
            this.codeSize,
            this.lastModified,
            this.lastInvocation,
            environment != null ? Map.copyOf(environment) : Map.of(),
            this.tags
        );
    }
    
    /**
     * Creates a copy of this LambdaFunctionInfo with updated tags.
     * 
     * @param tags new tags map
     * @return new LambdaFunctionInfo instance with updated tags
     */
    public LambdaFunctionInfo withTags(Map<String, String> tags) {
        return new LambdaFunctionInfo(
            this.functionName,
            this.functionArn,
            this.runtime,
            this.role,
            this.handler,
            this.description,
            this.timeout,
            this.memorySize,
            this.codeSize,
            this.lastModified,
            this.lastInvocation,
            this.environment,
            tags != null ? Map.copyOf(tags) : Map.of()
        );
    }
    
    /**
     * Creates a copy of this LambdaFunctionInfo with updated last invocation timestamp.
     * 
     * @param lastInvocation new last invocation timestamp
     * @return new LambdaFunctionInfo instance with updated last invocation timestamp
     */
    public LambdaFunctionInfo withLastInvocation(Instant lastInvocation) {
        return new LambdaFunctionInfo(
            this.functionName,
            this.functionArn,
            this.runtime,
            this.role,
            this.handler,
            this.description,
            this.timeout,
            this.memorySize,
            this.codeSize,
            this.lastModified,
            lastInvocation,
            this.environment,
            this.tags
        );
    }
    
    /**
     * Gets an environment variable value by key.
     * 
     * @param key environment variable key
     * @return environment variable value or null if not found
     */
    public String getEnvironmentVariable(String key) {
        return environment != null ? environment.get(key) : null;
    }
    
    /**
     * Checks if an environment variable exists.
     * 
     * @param key environment variable key
     * @return true if environment variable exists, false otherwise
     */
    public boolean hasEnvironmentVariable(String key) {
        return environment != null && environment.containsKey(key);
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
     * Checks if this function has been invoked.
     * 
     * @return true if lastInvocation is not null, false otherwise
     */
    public boolean hasBeenInvoked() {
        return lastInvocation != null;
    }
    
    /**
     * Checks if this function has a description.
     * 
     * @return true if description is set, false otherwise
     */
    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }
    
    /**
     * Checks if this function has environment variables.
     * 
     * @return true if environment variables are set, false otherwise
     */
    public boolean hasEnvironmentVariables() {
        return environment != null && !environment.isEmpty();
    }
    
    /**
     * Checks if this function has tags.
     * 
     * @return true if tags are set, false otherwise
     */
    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }
    
    /**
     * Gets the age of this function in seconds.
     * 
     * @return age in seconds or -1 if lastModified is null
     */
    public long getAgeInSeconds() {
        if (lastModified == null) {
            return -1;
        }
        return Instant.now().getEpochSecond() - lastModified.getEpochSecond();
    }
    
    /**
     * Gets the time since last invocation in seconds.
     * 
     * @return time since last invocation in seconds or -1 if never invoked
     */
    public long getTimeSinceLastInvocationInSeconds() {
        if (lastInvocation == null) {
            return -1;
        }
        return Instant.now().getEpochSecond() - lastInvocation.getEpochSecond();
    }
    
    /**
     * Checks if this function is older than the specified number of seconds.
     * 
     * @param seconds the number of seconds to check against
     * @return true if function is older than specified seconds, false otherwise
     */
    public boolean isOlderThan(int seconds) {
        return getAgeInSeconds() > seconds;
    }
    
    /**
     * Checks if this function was invoked more than the specified number of seconds ago.
     * 
     * @param seconds the number of seconds to check against
     * @return true if function was invoked more than specified seconds ago, false otherwise
     */
    public boolean wasInvokedMoreThanSecondsAgo(int seconds) {
        return getTimeSinceLastInvocationInSeconds() > seconds;
    }
    
    /**
     * Gets the function version from the ARN.
     * 
     * @return function version or null if not available
     */
    public String getVersion() {
        if (functionArn == null) {
            return null;
        }
        
        String[] parts = functionArn.split(":");
        if (parts.length >= 8) {
            return parts[7];
        }
        return null;
    }
    
    /**
     * Checks if this function is a specific version.
     * 
     * @param version the version to check
     * @return true if function is the specified version, false otherwise
     */
    public boolean isVersion(String version) {
        String functionVersion = getVersion();
        return functionVersion != null && functionVersion.equals(version);
    }
    
    /**
     * Checks if this function is the latest version.
     * 
     * @return true if function is the latest version, false otherwise
     */
    public boolean isLatestVersion() {
        String version = getVersion();
        return version != null && version.equals("$LATEST");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LambdaFunctionInfo that = (LambdaFunctionInfo) obj;
        return timeout == that.timeout &&
               memorySize == that.memorySize &&
               codeSize == that.codeSize &&
               Objects.equals(functionName, that.functionName) &&
               Objects.equals(functionArn, that.functionArn) &&
               Objects.equals(runtime, that.runtime) &&
               Objects.equals(role, that.role) &&
               Objects.equals(handler, that.handler) &&
               Objects.equals(description, that.description) &&
               Objects.equals(lastModified, that.lastModified) &&
               Objects.equals(lastInvocation, that.lastInvocation) &&
               Objects.equals(environment, that.environment) &&
               Objects.equals(tags, that.tags);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(functionName, functionArn, runtime, role, handler, description, 
                           timeout, memorySize, codeSize, lastModified, lastInvocation, environment, tags);
    }
    
    @Override
    public String toString() {
        return "LambdaFunctionInfo{" +
               "functionName='" + functionName + '\'' +
               ", functionArn='" + functionArn + '\'' +
               ", runtime='" + runtime + '\'' +
               ", role='" + role + '\'' +
               ", handler='" + handler + '\'' +
               ", description='" + description + '\'' +
               ", timeout=" + timeout +
               ", memorySize=" + memorySize +
               ", codeSize=" + codeSize +
               ", lastModified=" + lastModified +
               ", lastInvocation=" + lastInvocation +
               ", environment=" + environment +
               ", tags=" + tags +
               '}';
    }
}
