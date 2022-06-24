package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private class MyHashMapIterator implements Iterator<Node> {
        LinkedList<Node> nodes;
        int wizPos;

        MyHashMapIterator() {

        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Node next() {
            return null;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    /** the size of this HashMap. */
    private int size;
    private double loadFactor;

    /** Constructors */
    public MyHashMap() {
        this.size = 0;
        this.buckets = createTable(16);
        this.loadFactor = 0.75;
    }

    public MyHashMap(int initialSize) {
        this.size = 0;
        this.buckets = createTable(initialSize);
        this.loadFactor = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.size = 0;
        this.buckets = createTable(initialSize);
        this.loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return null;
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear() {
        this.size = 0;
        Arrays.fill(this.buckets, null);
    }

    @Override
    public boolean containsKey(K key) {
        int keyHashCode = key.hashCode();
        int index = Math.floorMod(keyHashCode, buckets.length);
        for (Node tempNode: buckets[index]) {
            if (tempNode.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int keyHashCode = key.hashCode();
        int index = Math.floorMod(keyHashCode, buckets.length);
        for (Node tempNode: buckets[index]) {
            if (tempNode.key.equals(key)) {
                return tempNode.value;
            }
        }
        System.out.println("accidentally lose the key");
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int keyHashCode = key.hashCode();
        int index = Math.floorMod(keyHashCode, buckets.length);
        /** if key is already exists, update the value. */
        if (containsKey(key)) {
            for (Node tempNode: buckets[index]) {
                if (tempNode.key.equals(key)) {
                    tempNode.value = value;
                    break;
                }
            }
        } else {
            /** if key is not exists, create a new node and put it in the right place.
             * remember size ++ and resize the buckets if it's necessary*/
            Node addNode = createNode(key, value);
            size++;
            if ((double)size / buckets.length < loadFactor) {
                buckets[index].add(addNode);
            } else {
                resizeBuckets(2 * buckets.length);
            }
        }
    }

    private void resizeBuckets(int expandSize) {
        MyHashMap<K, V> temp = new MyHashMap<>(expandSize);
        for (int i = 0; i < buckets.length; i++) {
            for (Node oldNode: buckets[i]) {
                temp.put(oldNode.key, oldNode.value);
            }
        }
        this.buckets = temp.buckets;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }
}
