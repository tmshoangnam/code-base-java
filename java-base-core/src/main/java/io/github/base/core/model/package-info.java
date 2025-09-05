/**
 * Common data models and response structures package.
 * 
 * <p>This package provides standard data models and response structures
 * that are commonly used across enterprise applications. It ensures
 * consistency in API responses and data handling.
 * 
 * <p><strong>Key Components:</strong>
 * <ul>
 *   <li>{@code CommonResponse} - Standard API response model</li>
 * </ul>
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Immutable response objects</li>
 *   <li>Consistent structure across all APIs</li>
 *   <li>Support for both success and error responses</li>
 *   <li>Extensible metadata support</li>
 * </ul>
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use CommonResponse for all API responses</li>
 *   <li>Use static factory methods for common response types</li>
 *   <li>Include relevant metadata for debugging and monitoring</li>
 *   <li>Use builder pattern for complex responses</li>
 * </ul>
 * 
 * <p><strong>Example:</strong>
 * <pre>{@code
 * // Success response
 * CommonResponse<User> response = CommonResponse.success(user);
 * 
 * // Error response
 * CommonResponse<Void> response = CommonResponse.error("USER_NOT_FOUND", "User does not exist");
 * 
 * // Custom response with metadata
 * CommonResponse<String> response = CommonResponse.builder()
 *     .status("success")
 *     .message("Operation completed")
 *     .code("OK")
 *     .data("result")
 *     .metadata(Map.of("processingTime", "150ms"))
 *     .build();
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
package io.github.base.core.model;
