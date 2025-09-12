package io.github.base.aws.api.lambda;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable record representing the result of a Lambda function invocation.
 *
 * <p>This record contains information about the success or failure of a Lambda
 * function invocation, including the response, logs, and metadata.
 *
 * @param success true if invocation was successful, false otherwise
 * @param response the response from the function
 * @param logs the logs from the function execution
 * @param statusCode the HTTP status code
 * @param executedVersion the version of the function that was executed
 * @param functionName the name of the function that was invoked
 * @param requestId the request ID for the invocation
 * @param errorMessage error message if invocation failed, null if successful
     * @param errorCode error code if invocation failed, null if successful
     * @param invokedAt timestamp when the invocation completed
     * @param duration duration of the invocation in milliseconds
     * @param memoryUsed memory used by the function in MB
     * @param billingDuration billing duration in milliseconds
     *
     * @author java-base-team
     * @since 1.0.5-SNAPSHOT
     */
public record LambdaInvokeResult(
    boolean success,
    String response,
    String logs,
    int statusCode,
    String executedVersion,
    String functionName,
    String requestId,
    String errorMessage,
    String errorCode,
    Instant invokedAt,
    long duration,
    int memoryUsed,
    long billingDuration
) {

    /**
     * Creates a successful Lambda invoke result.
     *
     * @param response the response from the function
     * @param functionName the name of the function that was invoked
     * @param requestId the request ID for the invocation
     * @return successful LambdaInvokeResult
     * @throws IllegalArgumentException if functionName or requestId is null or empty
     */
    public static LambdaInvokeResult success(String response, String functionName, String requestId) {
        if (functionName == null || functionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Function name cannot be null or empty");
        }
        if (requestId == null || requestId.trim().isEmpty()) {
            throw new IllegalArgumentException("Request ID cannot be null or empty");
        }

        return new LambdaInvokeResult(
            true,
            response,
            null,
            200,
            null,
            functionName.trim(),
            requestId.trim(),
            null,
            null,
            Instant.now(),
            0,
            0,
            0
        );
    }

    /**
     * Creates a successful Lambda invoke result with all information.
     *
     * @param response the response from the function
     * @param logs the logs from the function execution
     * @param statusCode the HTTP status code
     * @param executedVersion the version of the function that was executed
     * @param functionName the name of the function that was invoked
     * @param requestId the request ID for the invocation
     * @param duration duration of the invocation in milliseconds
     * @param memoryUsed memory used by the function in MB
     * @param billingDuration billing duration in milliseconds
     * @return successful LambdaInvokeResult
     * @throws IllegalArgumentException if functionName or requestId is null or empty
     */
    public static LambdaInvokeResult success(String response, String logs, int statusCode,
                                           String executedVersion, String functionName, String requestId,
                                           long duration, int memoryUsed, long billingDuration) {
        if (functionName == null || functionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Function name cannot be null or empty");
        }
        if (requestId == null || requestId.trim().isEmpty()) {
            throw new IllegalArgumentException("Request ID cannot be null or empty");
        }

        return new LambdaInvokeResult(
            true,
            response,
            logs,
            statusCode,
            executedVersion,
            functionName.trim(),
            requestId.trim(),
            null,
            null,
            Instant.now(),
            Math.max(duration, 0),
            Math.max(memoryUsed, 0),
            Math.max(billingDuration, 0)
        );
    }

    /**
     * Creates a failed Lambda invoke result.
     *
     * @param functionName the name of the function that was invoked
     * @param requestId the request ID for the invocation
     * @param errorMessage error message describing the failure
     * @param errorCode error code for the failure
     * @return failed LambdaInvokeResult
     * @throws IllegalArgumentException if functionName or requestId is null or empty, or errorMessage is null or empty
     */
    public static LambdaInvokeResult failure(String functionName, String requestId, String errorMessage, String errorCode) {
        if (functionName == null || functionName.trim().isEmpty()) {
            throw new IllegalArgumentException("Function name cannot be null or empty");
        }
        if (requestId == null || requestId.trim().isEmpty()) {
            throw new IllegalArgumentException("Request ID cannot be null or empty");
        }
        if (errorMessage == null || errorMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }

        return new LambdaInvokeResult(
            false,
            null,
            null,
            500,
            null,
            functionName.trim(),
            requestId.trim(),
            errorMessage.trim(),
            errorCode,
            Instant.now(),
            0,
            0,
            0
        );
    }

    /**
     * Creates a failed Lambda invoke result with error code.
     *
     * @param functionName the name of the function that was invoked
     * @param requestId the request ID for the invocation
     * @param errorMessage error message describing the failure
     * @return failed LambdaInvokeResult
     * @throws IllegalArgumentException if functionName or requestId is null or empty, or errorMessage is null or empty
     */
    public static LambdaInvokeResult failure(String functionName, String requestId, String errorMessage) {
        return failure(functionName, requestId, errorMessage, null);
    }

    /**
     * Checks if the invocation was successful.
     *
     * @return true if invocation was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Checks if the invocation failed.
     *
     * @return true if invocation failed, false otherwise
     */
    public boolean isFailure() {
        return !success;
    }

    /**
     * Gets the response from the function.
     *
     * @return response or null if invocation failed
     */
    public String getResponse() {
        return response;
    }

    /**
     * Gets the logs from the function execution.
     *
     * @return logs or null if not available
     */
    public String getLogs() {
        return logs;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the executed version.
     *
     * @return executed version or null if not available
     */
    public String getExecutedVersion() {
        return executedVersion;
    }

    /**
     * Gets the function name.
     *
     * @return function name
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Gets the request ID.
     *
     * @return request ID
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Gets the error message.
     *
     * @return error message or null if invocation was successful
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gets the error code.
     *
     * @return error code or null if invocation was successful
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the invocation timestamp.
     *
     * @return timestamp when invocation completed
     */
    public Instant getInvokedAt() {
        return invokedAt;
    }

    /**
     * Gets the duration of the invocation.
     *
     * @return duration in milliseconds
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Gets the memory used by the function.
     *
     * @return memory used in MB
     */
    public int getMemoryUsed() {
        return memoryUsed;
    }

    /**
     * Gets the billing duration.
     *
     * @return billing duration in milliseconds
     */
    public long getBillingDuration() {
        return billingDuration;
    }

    /**
     * Checks if this invocation has logs.
     *
     * @return true if logs are available, false otherwise
     */
    public boolean hasLogs() {
        return logs != null && !logs.trim().isEmpty();
    }

    /**
     * Checks if this invocation has a response.
     *
     * @return true if response is available, false otherwise
     */
    public boolean hasResponse() {
        return response != null && !response.trim().isEmpty();
    }

    /**
     * Checks if this invocation has an executed version.
     *
     * @return true if executed version is available, false otherwise
     */
    public boolean hasExecutedVersion() {
        return executedVersion != null && !executedVersion.trim().isEmpty();
    }

    /**
     * Checks if this invocation has billing information.
     *
     * @return true if billing duration > 0, false otherwise
     */
    public boolean hasBillingInfo() {
        return billingDuration > 0;
    }

    /**
     * Gets the cost of this invocation in dollars.
     *
     * @param pricePerGBSecond price per GB-second in dollars
     * @param pricePerRequest price per request in dollars
     * @return cost in dollars or -1 if billing information is not available
     */
    public double getCost(double pricePerGBSecond, double pricePerRequest) {
        if (!hasBillingInfo()) {
            return -1;
        }

        double memoryGB = memoryUsed / 1024.0;
        double durationSeconds = billingDuration / 1000.0;
        double computeCost = memoryGB * durationSeconds * pricePerGBSecond;
        double requestCost = pricePerRequest;

        return computeCost + requestCost;
    }

    /**
     * Checks if this invocation was successful based on status code.
     *
     * @return true if status code is 200, false otherwise
     */
    public boolean isSuccessfulStatusCode() {
        return statusCode == 200;
    }

    /**
     * Checks if this invocation was a client error.
     *
     * @return true if status code is 4xx, false otherwise
     */
    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    /**
     * Checks if this invocation was a server error.
     *
     * @return true if status code is 5xx, false otherwise
     */
    public boolean isServerError() {
        return statusCode >= 500;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LambdaInvokeResult that = (LambdaInvokeResult) obj;
        return success == that.success &&
               statusCode == that.statusCode &&
               duration == that.duration &&
               memoryUsed == that.memoryUsed &&
               billingDuration == that.billingDuration &&
               Objects.equals(response, that.response) &&
               Objects.equals(logs, that.logs) &&
               Objects.equals(executedVersion, that.executedVersion) &&
               Objects.equals(functionName, that.functionName) &&
               Objects.equals(requestId, that.requestId) &&
               Objects.equals(errorMessage, that.errorMessage) &&
               Objects.equals(errorCode, that.errorCode) &&
               Objects.equals(invokedAt, that.invokedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, response, logs, statusCode, executedVersion, functionName,
                           requestId, errorMessage, errorCode, invokedAt, duration, memoryUsed, billingDuration);
    }

    @Override
    public String toString() {
        return "LambdaInvokeResult{" +
               "success=" + success +
               ", response='" + response + '\'' +
               ", logs='" + logs + '\'' +
               ", statusCode=" + statusCode +
               ", executedVersion='" + executedVersion + '\'' +
               ", functionName='" + functionName + '\'' +
               ", requestId='" + requestId + '\'' +
               ", errorMessage='" + errorMessage + '\'' +
               ", errorCode='" + errorCode + '\'' +
               ", invokedAt=" + invokedAt +
               ", duration=" + duration +
               ", memoryUsed=" + memoryUsed +
               ", billingDuration=" + billingDuration +
               '}';
    }
}
