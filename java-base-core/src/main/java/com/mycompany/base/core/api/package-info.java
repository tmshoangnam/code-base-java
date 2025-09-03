/**
 * Public API package for java-base-core foundation library.
 * 
 * <p>This package contains the public API interfaces and classes that
 * applications can use directly. It provides a clean, stable interface
 * for the core functionality of the foundation library.
 * 
 * <p><strong>Package Structure:</strong>
 * <ul>
 *   <li>{@code error} - Exception hierarchy and error handling contracts</li>
 *   <li>{@code logging} - Structured logging utilities and correlation ID generation</li>
 *   <li>{@code resilience} - Resilience pattern factories and utilities</li>
 *   <li>{@code util} - Common utility classes for validation, dates, JSON, etc.</li>
 * </ul>
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Framework-agnostic - no Spring or other framework dependencies</li>
 *   <li>Thread-safe - all public methods are thread-safe</li>
 *   <li>Null-safe - proper null handling throughout</li>
 *   <li>Backward compatible - stable API with semantic versioning</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
package com.mycompany.base.core.api;
