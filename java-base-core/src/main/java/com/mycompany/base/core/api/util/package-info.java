/**
 * Common utility classes package.
 * 
 * <p>This package provides lightweight utility classes for common
 * operations that are frequently needed in enterprise applications.
 * These utilities are designed to be framework-agnostic and have
 * minimal dependencies.
 * 
 * <p><strong>Key Components:</strong>
 * <ul>
 *   <li>{@code StringUtils} - String manipulation and validation utilities</li>
 *   <li>{@code DateUtils} - Date and time operations using Java 8+ Time API</li>
 *   <li>{@code JsonUtils} - JSON serialization/deserialization using Jackson</li>
 *   <li>{@code ValidationUtils} - Jakarta Bean Validation utilities</li>
 * </ul>
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Thread-safe static methods</li>
 *   <li>Null-safe operations where appropriate</li>
 *   <li>Minimal dependencies - only essential libraries</li>
 *   <li>Lightweight - avoid duplicating Apache Commons functionality</li>
 * </ul>
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use for common operations that don't require full framework features</li>
 *   <li>Prefer these utilities over external libraries for simple operations</li>
 *   <li>All methods are static and can be imported statically</li>
 *   <li>Handle null inputs gracefully</li>
 * </ul>
 * 
 * <p><strong>Example:</strong>
 * <pre>{@code
 * // String operations
 * if (StringUtils.isBlank(userInput)) {
 *     throw new IllegalArgumentException("Input cannot be empty");
 * }
 * 
 * // Date operations
 * String timestamp = DateUtils.formatCurrentTime("yyyy-MM-dd HH:mm:ss");
 * 
 * // JSON operations
 * String json = JsonUtils.toJson(user);
 * User user = JsonUtils.fromJson(json, User.class);
 * 
 * // Validation
 * ValidationUtils.validate(userDto);
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
package com.mycompany.base.core.api.util;
