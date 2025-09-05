/**
 * Test fixtures and data generation utilities package.
 * 
 * <p>This package provides utilities for generating test data that are commonly
 * needed in unit tests. It helps reduce boilerplate code in tests and
 * provides consistent test data generation across modules.
 * 
 * <p><strong>Key Components:</strong>
 * <ul>
 *   <li>{@code FixtureUtils} - Test data generation utilities</li>
 * </ul>
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Static methods for easy import</li>
 *   <li>Realistic test data generation</li>
 *   <li>Thread-safe operations</li>
 *   <li>No external dependencies</li>
 * </ul>
 * 
 * <p><strong>Usage Guidelines:</strong>
 * <ul>
 *   <li>Use FixtureUtils for generating test data</li>
 *   <li>Import methods statically for cleaner test code</li>
 *   <li>Use meaningful test data that reflects real-world scenarios</li>
 *   <li>Can be used across different modules</li>
 * </ul>
 * 
 * <p><strong>Example:</strong>
 * <pre>{@code
 * // Test data generation
 * String email = FixtureUtils.randomEmail();
 * LocalDate birthDate = FixtureUtils.randomBirthDate();
 * List<String> names = FixtureUtils.randomList(5, FixtureUtils::randomName);
 * 
 * // Generate collections
 * List<String> names = FixtureUtils.randomList(5, FixtureUtils::randomName);
 * Set<String> emails = FixtureUtils.randomSet(3, FixtureUtils::randomEmail);
 * }</pre>
 * 
 * <p><strong>Note:</strong> For custom assertions, use {@code AssertUtils} 
 * in the test scope package {@code io.github.base.core.test}.
 * 
 * @since 1.0.0
 * @author java-base-core
 */
package io.github.base.core.test;
