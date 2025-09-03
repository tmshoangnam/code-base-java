package com.mycompany.base.core.api.error;

/**
 * SPI (Service Provider Interface) for mapping exceptions to Problem objects.
 * 
 * <p>This interface defines the contract for converting exceptions into
 * standardized Problem objects that follow RFC 7807. Implementations
 * should be provided by the application layer or framework integration.
 * 
 * <p><strong>Design Notes:</strong>
 * <ul>
 *   <li>Core only defines the contract - no Spring dependency</li>
 *   <li>Actual Spring integration is implemented in java-base-starter</li>
 *   <li>Allows for flexible error handling strategies</li>
 *   <li>Supports correlation ID tracking for request tracing</li>
 * </ul>
 * 
 * <p><strong>Example Implementation:</strong>
 * <pre>{@code
 * public class DefaultProblemMapper implements ProblemMapper {
 *     public Problem mapBusinessException(BusinessException ex, String correlationId) {
 *         return new Problem(
 *             "https://api.example.com/problems/business-error",
 *             "Business Rule Violation",
 *             400,
 *             ex.getMessage(),
 *             "/api/current-request",
 *             correlationId,
 *             Instant.now(),
 *             Map.of("errorCode", ex.getErrorCode(), "context", ex.getContext())
 *         );
 *     }
 *     
 *     public Problem mapServiceUnavailable(Throwable ex, String correlationId) {
 *         return new Problem(
 *             "https://api.example.com/problems/service-unavailable",
 *             "Service Unavailable",
 *             503,
 *             "The requested service is temporarily unavailable",
 *             "/api/current-request",
 *             correlationId,
 *             Instant.now()
 *         );
 *     }
 * }
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public interface ProblemMapper {
    
    /**
     * Maps a BusinessException to a Problem object.
     * 
     * <p>This method should create a Problem that represents the business
     * rule violation in a standardized format. The correlation ID should
     * be included for request tracing purposes.
     * 
     * @param ex the business exception to map
     * @param correlationId the correlation ID for request tracing
     * @return a Problem object representing the business error
     * @throws IllegalArgumentException if ex or correlationId is null
     */
    Problem mapBusinessException(BusinessException ex, String correlationId);
    
    /**
     * Maps a service unavailability exception to a Problem object.
     * 
     * <p>This method should create a Problem that represents service
     * unavailability or infrastructure failures. The correlation ID should
     * be included for request tracing purposes.
     * 
     * @param ex the exception that caused service unavailability
     * @param correlationId the correlation ID for request tracing
     * @return a Problem object representing the service error
     * @throws IllegalArgumentException if ex or correlationId is null
     */
    Problem mapServiceUnavailable(Throwable ex, String correlationId);
}
