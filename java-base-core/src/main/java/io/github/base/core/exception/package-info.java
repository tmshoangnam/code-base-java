/**
 * Exception handling and error management package.
 * 
 * <p>This package provides a comprehensive exception hierarchy and error
 * management system for enterprise applications. It includes base exceptions,
 * specialized exception types, and error code management.
 * 
 * <p><strong>Key Components:</strong>
 * <ul>
 *   <li>{@code BaseException} - Base class for all custom exceptions</li>
 *   <li>{@code BusinessException} - For business rule violations</li>
 *   <li>{@code ValidationException} - For validation errors and constraint violations</li>
 *   <li>{@code SystemException} - For system-level errors and infrastructure failures</li>
 *   <li>{@code ErrorCodeRegistry} - Centralized error code management</li>
 * </ul>
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Consistent error code system across the application</li>
 *   <li>Context information for better debugging and monitoring</li>
 *   <li>Thread-safe error code registry</li>
 *   <li>Immutable exception objects</li>
 * </ul>
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use BusinessException for domain-specific validation failures</li>
 *   <li>Use ValidationException for input validation errors</li>
 *   <li>Use SystemException for infrastructure or external service failures</li>
 *   <li>Register error codes in ErrorCodeRegistry for consistency</li>
 *   <li>Include meaningful context information when available</li>
 * </ul>
 * 
 * <p><strong>Example:</strong>
 * <pre>{@code
 * // Register error codes
 * ErrorCodeRegistry.register("USER_NOT_FOUND", "User does not exist");
 * 
 * // Business rule violation
 * throw new BusinessException("INSUFFICIENT_BALANCE", 
 *     "Account balance is insufficient for this transaction");
 * 
 * // Validation error
 * throw new ValidationException("INVALID_EMAIL", 
 *     "Email format is invalid",
 *     Map.of("field", "email", "value", "invalid-email"));
 * 
 * // System error
 * throw new SystemException("DATABASE_CONNECTION_FAILED", 
 *     "Unable to connect to the database");
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
package io.github.base.core.exception;
