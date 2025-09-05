package io.github.base.cache.api;

/**
 * Strategy for generating cache keys from multiple parameters.
 * <p>
 * This functional interface provides a way to generate consistent cache keys
 * from multiple input parameters. It's particularly useful for method-level
 * caching where cache keys need to be generated from method parameters.
 * <p>
 * <strong>Key Requirements:</strong>
 * <ul>
 *   <li>Keys should be consistent for the same input parameters</li>
 *   <li>Keys should be unique for different input parameters</li>
 *   <li>Keys should be reasonably short to avoid memory overhead</li>
 *   <li>Keys should not contain characters that are problematic for the cache provider</li>
 * </ul>
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface KeyGenerator {

    /**
     * Generates a cache key from the given parameters.
     * <p>
     * The implementation should ensure that:
     * <ul>
     *   <li>The same parameters always produce the same key</li>
     *   <li>Different parameters produce different keys</li>
     *   <li>Null parameters are handled appropriately</li>
     * </ul>
     *
     * @param params the parameters to generate the key from
     * @return the generated cache key, never null
     * @throws IllegalArgumentException if the parameters are invalid
     */
    String generate(Object... params);
}
