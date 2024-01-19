package edu.sakralen.task1.map;

/**
 * Basic interface for a key-value map.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public interface Map<K, V> {

    /**
     * Gets the number of key-value pairs in the map.
     *
     * @return the size of the map
     */
    int size();

    /**
     * Checks if the map is empty.
     *
     * @return true if the map has no entries
     */
    boolean isEmpty();

    /**
     * Checks if the map contains a specific key.
     *
     * @param key the key to check
     * @return true if the key is present in the map
     */
    boolean containsKey(Object key);

    /**
     * Gets the value associated with a key.
     *
     * @param key the key to look up
     * @return the value associated with the key, or null if not found
     */
    V get(Object key);

    /**
     * Associates a value with a key in the map.
     *
     * @param key   the key
     * @param value the value to associate with the key
     * @return the previous value associated with the key, or null if none
     */
    V put(K key, V value);

    /**
     * Removes a key-value pair from the map.
     *
     * @param key the key to remove
     * @return the value associated with the key, or null if not found
     */
    V remove(Object key);

    /**
     * Removes all key-value pairs from the map, making it empty.
     */
    void clear();
}
