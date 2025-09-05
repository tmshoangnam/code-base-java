package io.github.base.core.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for collection and map operations.
 * 
 * <p>This class provides null-safe collection operations and common
 * transformations that are frequently needed in enterprise applications.
 * It helps reduce boilerplate code and prevents null pointer exceptions.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Null-safe operations throughout</li>
 *   <li>Thread-safe static methods</li>
 *   <li>Minimal dependencies - pure Java implementation</li>
 *   <li>Functional programming support</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Null-safe operations
 * List<String> safeList = CollectionUtils.emptyIfNull(possiblyNullList);
 * 
 * // Convert between collection types
 * Set<String> set = CollectionUtils.toSet(list);
 * Map<String, User> map = CollectionUtils.toMap(users, User::getId);
 * 
 * // Grouping and partitioning
 * Map<String, List<User>> grouped = CollectionUtils.groupBy(users, User::getDepartment);
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public final class CollectionUtils {
    
    private CollectionUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Returns an empty collection if the input is null, otherwise returns the input.
     * 
     * @param collection the collection to check
     * @param <T> the type of elements
     * @return empty collection if input is null, otherwise the input
     */
    public static <T> Collection<T> emptyIfNull(Collection<T> collection) {
        return collection == null ? Collections.emptyList() : collection;
    }
    
    /**
     * Returns an empty list if the input is null, otherwise returns the input.
     * 
     * @param list the list to check
     * @param <T> the type of elements
     * @return empty list if input is null, otherwise the input
     */
    public static <T> List<T> emptyIfNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }
    
    /**
     * Returns an empty set if the input is null, otherwise returns the input.
     * 
     * @param set the set to check
     * @param <T> the type of elements
     * @return empty set if input is null, otherwise the input
     */
    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }
    
    /**
     * Returns an empty map if the input is null, otherwise returns the input.
     * 
     * @param map the map to check
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return empty map if input is null, otherwise the input
     */
    public static <K, V> Map<K, V> emptyIfNull(Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }
    
    /**
     * Checks if a collection is null or empty.
     * 
     * @param collection the collection to check
     * @return true if the collection is null or empty, false otherwise
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    /**
     * Checks if a collection is not null and not empty.
     * 
     * @param collection the collection to check
     * @return true if the collection is not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    /**
     * Checks if a map is null or empty.
     * 
     * @param map the map to check
     * @return true if the map is null or empty, false otherwise
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    
    /**
     * Checks if a map is not null and not empty.
     * 
     * @param map the map to check
     * @return true if the map is not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    /**
     * Converts a collection to a list.
     * 
     * @param collection the collection to convert
     * @param <T> the type of elements
     * @return a new list containing the elements, or empty list if input is null
     */
    public static <T> List<T> toList(Collection<T> collection) {
        if (collection == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(collection);
    }
    
    /**
     * Converts a collection to a set.
     * 
     * @param collection the collection to convert
     * @param <T> the type of elements
     * @return a new set containing the elements, or empty set if input is null
     */
    public static <T> Set<T> toSet(Collection<T> collection) {
        if (collection == null) {
            return new HashSet<>();
        }
        return new HashSet<>(collection);
    }
    
    /**
     * Converts a collection to a map using the provided key mapper.
     * 
     * @param collection the collection to convert
     * @param keyMapper the function to extract keys
     * @param <T> the type of elements
     * @param <K> the type of keys
     * @return a new map with elements as values and mapped keys
     */
    public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<T, K> keyMapper) {
        if (collection == null || keyMapper == null) {
            return new HashMap<>();
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(keyMapper, Function.identity(), (existing, replacement) -> replacement));
    }
    
    /**
     * Converts a collection to a map using the provided key and value mappers.
     * 
     * @param collection the collection to convert
     * @param keyMapper the function to extract keys
     * @param valueMapper the function to extract values
     * @param <T> the type of elements
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return a new map with mapped keys and values
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (collection == null || keyMapper == null || valueMapper == null) {
            return new HashMap<>();
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(keyMapper, valueMapper, (existing, replacement) -> replacement));
    }
    
    /**
     * Groups elements by the provided classifier function.
     * 
     * @param collection the collection to group
     * @param classifier the function to classify elements
     * @param <T> the type of elements
     * @param <K> the type of keys
     * @return a map with grouped elements
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> classifier) {
        if (collection == null || classifier == null) {
            return new HashMap<>();
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(classifier));
    }
    
    /**
     * Partitions elements based on the provided predicate.
     * 
     * @param collection the collection to partition
     * @param predicate the predicate to test elements
     * @param <T> the type of elements
     * @return a map with true/false keys and corresponding element lists
     */
    public static <T> Map<Boolean, List<T>> partition(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null || predicate == null) {
            return new HashMap<>();
        }
        return collection.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.partitioningBy(predicate));
    }
    
    /**
     * Flattens a collection of collections into a single list.
     * 
     * @param collections the collection of collections to flatten
     * @param <T> the type of elements
     * @return a flattened list of all elements
     */
    public static <T> List<T> flatten(Collection<? extends Collection<T>> collections) {
        if (collections == null) {
            return new ArrayList<>();
        }
        return collections.stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * Merges two maps, using the provided conflict resolver for duplicate keys.
     * 
     * @param map1 the first map
     * @param map2 the second map
     * @param conflictResolver the function to resolve conflicts
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return a new map containing all entries from both maps
     */
    public static <K, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2, Function<V, V> conflictResolver) {
        Map<K, V> result = new HashMap<>(emptyIfNull(map1));
        
        if (map2 != null) {
            map2.forEach((key, value) -> {
                if (value != null) {
                    result.merge(key, value, (existing, newValue) -> 
                        conflictResolver != null ? conflictResolver.apply(newValue) : newValue);
                }
            });
        }
        
        return result;
    }
    
    /**
     * Merges two maps, keeping the value from the second map for duplicate keys.
     * 
     * @param map1 the first map
     * @param map2 the second map
     * @param <K> the type of keys
     * @param <V> the type of values
     * @return a new map containing all entries from both maps
     */
    public static <K, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2) {
        return merge(map1, map2, null);
    }
}
