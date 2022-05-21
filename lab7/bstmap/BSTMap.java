package bstmap;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left;
        private BSTNode right;
        private int size;

        /** constructor without branch*/
        BSTNode(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }

        BSTNode get(K k) {
            int cmp = k.compareTo(key);
            if (cmp == 0) {
                return this;
            } else if (cmp < 0) {
                if (left == null) {
                    return null;
                }
                return left.get(k);
            } else {
                if (right == null) {
                    return null;
                }
                return right.get(k);
            }
        }

        void printInOrder() {
            if (this.left != null) {
                this.left.printInOrder();
            }
            System.out.print(this.key);
            System.out.print("  ");
            System.out.println(this.value);
            if (this.right != null) {
                this.right.printInOrder();
            }
        }
    }

    private class BSTMapIterator implements Iterator<K> {
        LinkedList<K> keys;
        int wizPos;

        BSTMapIterator(BSTNode node) {
            keys = new LinkedList<K>();
            addBSTKeysToList(root);
            wizPos = 0;
        }

        void addBSTKeysToList(BSTNode node) {
            if (node != null) {
                keys.add(node.key);
            }
            if (node.left != null) {
                addBSTKeysToList(node.left);
            }
            if (node.right != null) {
                addBSTKeysToList(node.right);
            }
        }

        @Override
        public boolean hasNext() {
            return wizPos < keys.size();
        }

        @Override
        public K next() {
            K returnItem = keys.get(wizPos);
            wizPos++;
            return returnItem;
        }
    }

    public BSTMap() {
        root = null;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        if (root == null) {
            return false;
        }
        return root.get(key) != null;
    }

    @Override
    public V get(K key) {
        return get(key, root);
    }

    private V get(K key, BSTNode node) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node.value;
        } else if (cmp < 0) {
            return get(key, node.left);
        } else {
            return get(key, node.right);
        }
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(BSTNode node) {
        if (node == null) {
            return 0;
        } else {
            return node.size;
        }

    }

    @Override
    public void put(K key, V value) {
        root = put(key, value, root);
    }

    private BSTNode put(K key, V value, BSTNode node) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        if (node == null) {
            return new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(key, value, node.left);
            node.size ++;
        } else if (cmp > 0) {
            node.right = put(key, value, node.right);
            node.size++;
        } else {
            node.value = value;
        }
        return node;
    }

    public void printInOrder() {
        if (root == null) {
            System.out.println(root);
        } else {
            root.printInOrder();
        }
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
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
        if (root == null) {
            return null;
        } else {
            return new BSTMapIterator(root);
        }
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> r = new BSTMap<>();
        r = null;
        System.out.println(r);
    }
}


