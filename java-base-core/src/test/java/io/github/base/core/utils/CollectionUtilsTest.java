package io.github.base.core.utils;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CollectionUtilsTest {

    @Test
    void testEmptyIfNullCollection() {
        List<String> nullList = null;
        List<String> emptyList = new ArrayList<>();
        List<String> nonEmptyList = Arrays.asList("a", "b", "c");

        assertThat(CollectionUtils.emptyIfNull(nullList)).isEmpty();
        assertThat(CollectionUtils.emptyIfNull(emptyList)).isEmpty();
        assertThat(CollectionUtils.emptyIfNull(nonEmptyList)).containsExactly("a", "b", "c");
    }

    @Test
    void testEmptyIfNullList() {
        List<String> nullList = null;
        List<String> emptyList = new ArrayList<>();
        List<String> nonEmptyList = Arrays.asList("a", "b", "c");

        assertThat(CollectionUtils.emptyIfNull(nullList)).isEmpty();
        assertThat(CollectionUtils.emptyIfNull(emptyList)).isEmpty();
        assertThat(CollectionUtils.emptyIfNull(nonEmptyList)).containsExactly("a", "b", "c");
    }

    @Test
    void testEmptyIfNullSet() {
        Set<String> nullSet = null;
        Set<String> emptySet = new HashSet<>();
        Set<String> nonEmptySet = new HashSet<>(Arrays.asList("a", "b", "c"));

        assertThat(CollectionUtils.emptyIfNull(nullSet)).isEmpty();
        assertThat(CollectionUtils.emptyIfNull(emptySet)).isEmpty();
        assertThat(CollectionUtils.emptyIfNull(nonEmptySet)).containsExactlyInAnyOrder("a", "b", "c");
    }

    @Test
    void testEmptyIfNullMap() {
        Map<String, String> nullMap = null;
        Map<String, String> emptyMap = new HashMap<>();
        Map<String, String> nonEmptyMap = Map.of("key1", "value1", "key2", "value2");

        assertThat(CollectionUtils.emptyIfNull(nullMap)).isEmpty();
        assertThat(CollectionUtils.emptyIfNull(emptyMap)).isEmpty();
        assertThat(CollectionUtils.emptyIfNull(nonEmptyMap)).hasSize(2);
    }

    @Test
    void testIsEmpty() {
        assertThat(CollectionUtils.isEmpty((Collection<?>) null)).isTrue();
        assertThat(CollectionUtils.isEmpty(new ArrayList<>())).isTrue();
        assertThat(CollectionUtils.isEmpty(Arrays.asList("a"))).isFalse();
    }

    @Test
    void testIsNotEmpty() {
        assertThat(CollectionUtils.isNotEmpty((Collection<?>) null)).isFalse();
        assertThat(CollectionUtils.isNotEmpty(new ArrayList<>())).isFalse();
        assertThat(CollectionUtils.isNotEmpty(Arrays.asList("a"))).isTrue();
    }

    @Test
    void testIsEmptyMap() {
        assertThat(CollectionUtils.isEmpty((Map<?, ?>) null)).isTrue();
        assertThat(CollectionUtils.isEmpty(new HashMap<>())).isTrue();
        assertThat(CollectionUtils.isEmpty(Map.of("key", "value"))).isFalse();
    }

    @Test
    void testIsNotEmptyMap() {
        assertThat(CollectionUtils.isNotEmpty((Map<?, ?>) null)).isFalse();
        assertThat(CollectionUtils.isNotEmpty(new HashMap<>())).isFalse();
        assertThat(CollectionUtils.isNotEmpty(Map.of("key", "value"))).isTrue();
    }

    @Test
    void testToList() {
        List<String> original = Arrays.asList("a", "b", "c");
        List<String> result = CollectionUtils.toList(original);

        assertThat(result).containsExactly("a", "b", "c");
        assertThat(result).isNotSameAs(original);
    }

    @Test
    void testToListWithNull() {
        List<String> result = CollectionUtils.toList(null);
        assertThat(result).isEmpty();
    }

    @Test
    void testToSet() {
        List<String> original = Arrays.asList("a", "b", "c", "a");
        Set<String> result = CollectionUtils.toSet(original);

        assertThat(result).containsExactlyInAnyOrder("a", "b", "c");
    }

    @Test
    void testToSetWithNull() {
        Set<String> result = CollectionUtils.toSet(null);
        assertThat(result).isEmpty();
    }

    @Test
    void testToMap() {
        List<String> items = Arrays.asList("apple", "banana", "cherry");
        Map<String, String> result = CollectionUtils.toMap(items, String::toUpperCase);

        assertThat(result).containsEntry("APPLE", "apple");
        assertThat(result).containsEntry("BANANA", "banana");
        assertThat(result).containsEntry("CHERRY", "cherry");
    }

    @Test
    void testToMapWithNull() {
        Map<String, String> result = CollectionUtils.toMap(null, String::toUpperCase);
        assertThat(result).isEmpty();
    }

    @Test
    void testToMapWithKeyAndValueMapper() {
        List<String> items = Arrays.asList("apple", "banana", "cherry");
        Map<String, Integer> result = CollectionUtils.toMap(items, String::toUpperCase, String::length);

        assertThat(result).containsEntry("APPLE", 5);
        assertThat(result).containsEntry("BANANA", 6);
        assertThat(result).containsEntry("CHERRY", 6);
    }

    @Test
    void testGroupBy() {
        List<String> items = Arrays.asList("apple", "banana", "cherry", "apricot");
        Map<String, List<String>> result = CollectionUtils.groupBy(items, s -> s.substring(0, 1));

        assertThat(result.get("a")).containsExactlyInAnyOrder("apple", "apricot");
        assertThat(result.get("b")).containsExactly("banana");
        assertThat(result.get("c")).containsExactly("cherry");
    }

    @Test
    void testGroupByWithNull() {
        Map<String, List<String>> result = CollectionUtils.groupBy(null, String::toUpperCase);
        assertThat(result).isEmpty();
    }

    @Test
    void testPartition() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Map<Boolean, List<Integer>> result = CollectionUtils.partition(numbers, n -> n % 2 == 0);

        assertThat(result.get(true)).containsExactly(2, 4, 6);
        assertThat(result.get(false)).containsExactly(1, 3, 5);
    }

    @Test
    void testPartitionWithNull() {
        Map<Boolean, List<String>> result = CollectionUtils.partition(null, String::isEmpty);
        assertThat(result).isEmpty();
    }

    @Test
    void testFlatten() {
        List<List<String>> nested = Arrays.asList(
            Arrays.asList("a", "b"),
            Arrays.asList("c", "d"),
            Arrays.asList("e", "f")
        );
        List<String> result = CollectionUtils.flatten(nested);

        assertThat(result).containsExactly("a", "b", "c", "d", "e", "f");
    }

    @Test
    void testFlattenWithNull() {
        List<String> result = CollectionUtils.flatten(null);
        assertThat(result).isEmpty();
    }

    @Test
    void testMerge() {
        Map<String, String> map1 = Map.of("key1", "value1", "key2", "value2");
        Map<String, String> map2 = Map.of("key2", "newValue2", "key3", "value3");
        Map<String, String> result = CollectionUtils.merge(map1, map2);

        assertThat(result).containsEntry("key1", "value1");
        assertThat(result).containsEntry("key2", "newValue2");
        assertThat(result).containsEntry("key3", "value3");
    }

    @Test
    void testMergeWithNull() {
        Map<String, String> map1 = Map.of("key1", "value1");
        Map<String, String> result = CollectionUtils.merge(map1, null);

        assertThat(result).containsEntry("key1", "value1");
    }

    @Test
    void testMergeWithConflictResolver() {
        Map<String, String> map1 = Map.of("key1", "value1", "key2", "value2");
        Map<String, String> map2 = Map.of("key2", "newValue2", "key3", "value3");
        Map<String, String> result = CollectionUtils.merge(map1, map2, v -> "resolved_" + v);

        assertThat(result).containsEntry("key1", "value1");
        assertThat(result).containsEntry("key2", "resolved_newValue2");
        assertThat(result).containsEntry("key3", "value3");
    }
}
