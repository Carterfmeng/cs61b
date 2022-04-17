package deque;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import net.sf.saxon.functions.ConstantFunction;
import net.sf.saxon.om.Item;
import java.util.Iterator;

public class LinkedListDeque<ItemType> implements Iterable<ItemType> {
    public class Node {
        public Node prev;
        public ItemType item;
        public Node next;

        public Node(Node prevNode, ItemType nodeItem, Node nextNode) {
            prev = prevNode;
            item = nodeItem;
            next = nextNode;
        }
    }
    private class LinkedListDequeIterator implements Iterator<ItemType> {
        private int wizPos;
        public LinkedListDequeIterator() {
            wizPos = 0;
        }
        @Override
        public boolean hasNext() {
            return wizPos < size;
        }

        @Override
        public ItemType next() {
            ItemType returnItem = get(wizPos);
            wizPos = wizPos + 1;
            return returnItem;
        }
    }

    public int size;
    public Node sentinel;

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel.next;
    }

    public void addFirst(ItemType item) {
        Node temp = sentinel.next;
        sentinel.next = new Node(sentinel, item, temp);
        temp.prev = sentinel.next;
        size = size + 1;
    }

    public void addLast(ItemType item) {
        Node temp = sentinel.prev;
        sentinel.prev.next = new Node(temp, item, sentinel);
        sentinel.prev = temp.next;
        size = size + 1;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int index = 0;
        Node temp = sentinel.next;
        while (index < size) {
            System.out.print(temp.item);
            index = index + 1;
            if (index < size) {
                System.out.print(" ");
                temp = temp.next;
            }
        }
        System.out.println();
    }

    public ItemType removeFirst() {
        if (size == 0) {
            return null;
        } else {
            ItemType first = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size = size - 1;
            return first;
        }
    }

    public ItemType removeLast() {
        if (size == 0 ) {
            return null;
        } else {
            ItemType last = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size = size - 1;
            return last;
        }
    }

    public ItemType get(int index) {
        if (index >= size) {
            return null;
        } else {
            Node tempPointer = sentinel.next;
            for (int i = 0; i < index; i++) {
                tempPointer = tempPointer.next;
            }
            return tempPointer.item;
        }
    }

    public ItemType getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursive(index, sentinel.next);
    }

    private ItemType getRecursive(int leftIndex, Node tempPointer) {
        if (leftIndex == 0) {
            return tempPointer.item;
        }
        return getRecursive(leftIndex-1, tempPointer.next);
    }

    public Iterator<ItemType> iterator() {
        return new LinkedListDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        LinkedListDeque<ItemType> other = (LinkedListDeque<ItemType>) o;
        if (this.size != other.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }



    public static void main(String[] args) {
    }
}
