/**
 * Error handling and exception management package.
 * 
 * <p>This package provides a comprehensive error handling framework
 * that follows enterprise best practices and RFC 7807 standards for
 * problem details in HTTP APIs.
 * 
 * <p><strong>Key Components:</strong>
 * <ul>
 *   <li>{@code Problem} - RFC 7807 compliant problem model</li>
 *   <li>{@code BusinessException} - Base class for business rule violations</li>
 *   <li>{@code ServiceException} - Base class for service/infrastructure failures</li>
 *   <li>{@code ProblemMapper} - SPI for mapping exceptions to Problem objects</li>
 * </ul>
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use BusinessException for domain-specific validation failures</li>
 *   <li>Use ServiceException for infrastructure or external service failures</li>
 *   <li>Include meaningful error codes for client handling</li>
 *   <li>Provide context information when available</li>
 *   <li>Implement ProblemMapper for framework integration</li>
 * </ul>
 * 
 * <p><strong>Example:</strong>
 * <pre>{@code
 * // Business rule violation
 * throw new BusinessException("INSUFFICIENT_BALANCE", 
 *     "Account balance is insufficient for this transaction");
 * 
 * // Service failure
 * throw new ServiceException("DATABASE_CONNECTION_FAILED", 
 *     "Unable to connect to the database");
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
package com.mycompany.base.core.api.error;
