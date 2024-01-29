package edu.sakralen.task1.map.hashmap;

import edu.sakralen.task1.map.Map;

import java.util.Arrays;
import java.util.Objects;

/**
 * A basic hash map implementation that uses separate chaining based on singly linked list.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final int MAX_CAPACITY = 1 << 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Node)) {
                return false;
            }

            @SuppressWarnings("unchecked")
            Node<K, V> node = (Node<K, V>) o;

            return hash == node.hash
                    && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public V replaceValue(V newValue) {
            V oldValue = value;
            value = newValue;

            return oldValue;
        }

        @Override
        public String toString() {
            return key + ": " + value;
        }
    }

    private int capacity;
    private final float loadFactor;
    private int size = 0;
    private Node<K, V>[] table;

    /**
     * Constructs a hash map with the default capacity and load factor.
     */
    public HashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a hash map with the specified capacity and the default load factor.
     *
     * @param capacity the initial capacity of the hash map
     */
    public HashMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a hash map with the specified capacity and load factor.
     *
     * @param capacity   the initial capacity of the hash map
     * @param loadFactor the load factor threshold for resizing the hash map
     * @throws IllegalArgumentException if the specified capacity is negative or if the load factor is not in (0, 1)
     */
    @SuppressWarnings("unchecked")
    public HashMap(int capacity, float loadFactor) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + capacity);
        }

        this.capacity = Math.min(capacity, MAX_CAPACITY);

        if (Float.compare(loadFactor, 0f) <= 0 || Float.compare(loadFactor, 1f) >= 0)
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

        this.loadFactor = loadFactor;

        table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public V get(Object key) {
        if (size == 0) {
            return null;
        }

        int hash = hash(key);
        Node<K, V> node = table[hash];

        // Traversing bucket until node with required hash is found or tail is reached.
        while (node != null) {
            if (node.hash == hash) {
                return node.value;
            }

            node = node.next;
        }

        return null;
    }

    private int hash(Object key) {
        return Math.abs(Objects.hashCode(key)) % capacity;
    }

    /**
     * Associates the specified key with the given value in the hash table.
     * If the key already exists, updates the associated value.
     * Resizes the table if the current size exceeds the load factor threshold.
     *
     * @param key   The key to be associated with the value.
     * @param value The value to be associated with the key.
     * @return      The previous value associated with the key, or null if the key is new.
     * @throws IllegalStateException if the maximum capacity is reached.
     */
    @Override
    public V put(K key, V value) {
        if (size == MAX_CAPACITY) {
            throw new IllegalStateException("Maximum capacity is reached.");
        }

        if (size == (int) (capacity * loadFactor)) {
            resize();
        }

        return putPair(key, value);
    }

    /**
     * Resizes the hash table when the number of elements exceeds a certain threshold.
     * The capacity of the table is doubled, up to the maximum capacity, and the elements
     * are rehashed to the new table.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        this.capacity = Math.min(capacity * 2, MAX_CAPACITY);

        Node<K, V>[] oldTable = Arrays.copyOf(table, table.length);
        table = (Node<K, V>[]) new Node[capacity];

        size = 0;

        for (var bucket : oldTable) {
            if (bucket == null) {
                continue;
            }

            Node<K, V> node = bucket;

            while (node != null) {
                put(node.key, node.value);

                node = node.next;
            }
        }
    }

    private V putPair(K key, V value) {
        int hash = hash(key);

        // Trivial case of empty bucket.
        if (table[hash] == null) {
            table[hash] = new Node<>(hash, key, value, null);
            size++;
            return null;
        }

        Node<K, V> node = table[hash];
        Node<K, V> prevNode = node;

        while (node != null) {
            if (node.hash == hash) {
                return node.replaceValue(value);
            }

            prevNode = node;
            node = node.next;
        }

        prevNode.next = new Node<>(hash, key, value, null);

        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        if (size == 0) {
            return null;
        }

        int hash = hash(key);

        // Trivial case of empty bucket.
        if (table[hash] == null) {
            return null;
        }

        Node<K, V> node = table[hash];

        // Removing node if it is the head one.
        if (node.hash == hash) {
            table[hash] = node.next;
            size--;
            return node.value;
        }

        Node<K, V> prevNode = node;
        node = node.next;

        while (node != null) {
            if (node.hash == hash) {
                prevNode.next = node.next;
                size--;
                return node.value;
            }

            node = node.next;
        }

        return null;
    }

    @Override
    public void clear() {
        if (size == 0) {
            return;
        }

        size = 0;
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
    }
}
