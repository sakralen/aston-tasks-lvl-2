package edu.sakralen.task1.map.hashmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HashMapTest {
    protected static HashMap<Integer, Integer> hashMap;
    protected static final int BASIC_CAPACITY = 1 << 16 - 1;

    @BeforeEach
    void init() {
        hashMap = new HashMap<>();
    }

    @Test
    void whenPutIntegerValues_thenReturnsCorrectSize() {
        for (int i = 0; i < BASIC_CAPACITY; i++) {
            hashMap.put(i, i);
        }

        assertEquals(BASIC_CAPACITY, hashMap.size());
    }

    @Test
    void whenExceedsMaxCapacity_thenThrowsIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i < 1_000_000; i++) {
                hashMap.put(i, i);
            }
        });
    }

    @Test
    void givenKeyValue_whenPutSameKeyNewValue_thenGetReturnsNewValue() {
        for (int i = 0; i < BASIC_CAPACITY; i++) {
            hashMap.put(i, i);
        }

        for (int i = 0; i < BASIC_CAPACITY; i++) {
            hashMap.put(i, BASIC_CAPACITY + i);
        }

        assertAll(() -> {
            for (int i = 0; i < BASIC_CAPACITY; i++) {
                assertEquals(BASIC_CAPACITY + i, hashMap.get(i));
            }
        });
    }

    @Test
    void givenKey_whenPutNullValue_thenGetReturnsNull() {
        int key = 1;

        hashMap.put(key, null);

        assertNull(hashMap.get(key));
    }

    @Test
    void givenNullKey_whenPut_thenGetReturnsCorrectValue() {
        int value = 1;

        hashMap.put(null, value);

        assertEquals(value, hashMap.get(null));
    }

    @Test
    void whenPutKeysAndValues_thenContainsKeys() {
        for (int i = 0; i < BASIC_CAPACITY; i++) {
            hashMap.put(i, BASIC_CAPACITY + i);
        }

        assertAll(() -> {
            for (int i = 0; i < BASIC_CAPACITY; i++) {
                assertTrue(hashMap.containsKey(i));
            }
        });
    }

    @Test
    void givenEmptyHashMap_thenIsEmptyReturnsTrue() {
        assertTrue(hashMap.isEmpty());
    }

    @Test
    void givenNonEmptyHashMap_thenIsEmptyReturnsFalse() {
        for (int i = 0; i < BASIC_CAPACITY - 1; i++) {
            hashMap.put(i, i + 1);
        }

        assertFalse(hashMap.isEmpty());
    }

    @Test
    void givenContainedPair_whenRemoved_thenGetReturnRemovedValue() {
        for (int i = 0; i < BASIC_CAPACITY - 1; i++) {
            hashMap.put(i, i + 1);
        }

        assertAll(() -> {
            for (int i = 0; i < BASIC_CAPACITY - 1; i++) {
                assertEquals(i + 1, hashMap.remove(i));
            }
        });
    }

    @Test
    void givenNotContainedPair_whenRemoved_thenGetReturnsNull() {
        assertAll(() -> {
            for (int i = 0; i < BASIC_CAPACITY - 1; i++) {
                assertNull(hashMap.remove(i));
            }
        });
    }

    @Test
    void givenContainedPair_whenRemoved_thenSizeDecreases() {
        for (int i = 0; i < BASIC_CAPACITY; i++) {
            hashMap.put(i, i);
        }

        hashMap.remove(0);

        assertEquals(BASIC_CAPACITY - 1, hashMap.size());
    }

    @Test
    void givenNonEmptyHashMap_whenCleared_thenSizeIsZero() {
        for (int i = 0; i < BASIC_CAPACITY; i++) {
            hashMap.put(i, i);
        }

        hashMap.clear();

        assertEquals(0, hashMap.size());
    }
}
