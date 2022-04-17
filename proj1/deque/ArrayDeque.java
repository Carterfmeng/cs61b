package deque;

import afu.org.checkerframework.checker.oigj.qual.O;
import net.sf.saxon.om.Item;
import java.util.Iterator;

public class ArrayDeque<ItemType> implements Deque<ItemType>, Iterable<ItemType> {
    public ItemType[] items;
    public int size;
    public int nextFirst;
    public int nextLast;

    private class ArrayDequeIterator implements Iterator<ItemType> {
        private int wizPos;

        public ArrayDequeIterator() {
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

    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        items = (ItemType[]) new Object[8];
        size = 0;
        nextFirst = 4; // first index == nextFirst + 1 (if out of boundary, first index = 0);
        nextLast = 5;     // last index == nextLast - 1 (if out of boundary, last index = length - 1));
    }

    /** resize this ArrayDeque to the capacity size. **/
    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        ItemType[] newItems = (ItemType[]) new Object[capacity];
        int firstIndex = nextFirst + 1;
        int firstCopyNum;
        if (nextFirst == items.length - 1) {
            firstIndex = 0;
        }
        firstCopyNum = size - firstIndex;
        System.arraycopy(items, firstIndex, newItems, 0, firstCopyNum);
        System.arraycopy(items, 0, newItems, firstCopyNum, firstIndex);
        items = newItems;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(ItemType item) {
        if (size == items.length) {
            resize(size * 2);
        } else {
            items[nextFirst] = item;
            size = size + 1;
            if (nextFirst > 0) {
                nextFirst = nextFirst - 1;
            } else {
                nextFirst = items.length - 1;
            }
        }
    }

    @Override
    public void addLast(ItemType item) {
        if (size >= items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        size = size + 1;
        if (nextLast < items.length - 1) {
            nextLast = nextLast + 1;
        } else {
            nextLast = 0;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int firstIndex = nextFirst + 1;
        if (nextFirst == items.length - 1) {
            firstIndex = 0;
        }
        for (int printedNum = 0; printedNum < size; printedNum++) {
            if (firstIndex + printedNum > items.length - 1) {
                firstIndex = -printedNum;
            }
            System.out.print(items[firstIndex + printedNum]);
            System.out.print(" ");
        }
    }

    @Override
    public ItemType removeFirst() {
        int firstIndex = nextFirst + 1;
        ItemType removedValue;
        if (nextFirst == items.length - 1) {
            firstIndex = 0;
        }
        if (size == 0) {
            return null;
        }
        removedValue = items[firstIndex];
        items[firstIndex] = null;
        nextFirst = (nextFirst + 1) % items.length;
        size = size - 1;
        if (size < items.length / 4 && items.length < 16) {
            resize(items.length / 2);
        }
        return removedValue;
    }

    @Override
    public ItemType removeLast() {
        int lastIndex = nextLast - 1;
        ItemType removedValue;
        if (nextLast == 0) {
            lastIndex = items.length - 1;
        }
        if (size == 0) {
            return null;
        }
        removedValue = items[lastIndex];
        items[lastIndex] = null;
        if (nextLast > 0) {
            nextLast = nextLast - 1;
        } else {
            nextLast = items.length - 1;
        }
        size = size - 1;
        if (size < items.length / 4 && items.length < 16) {
            resize(items.length / 2);
        }
        return removedValue;
    }

    @Override
    public ItemType get(int index) {
        int first = (nextFirst + 1) % items.length;
        if (index >= size) {
            return null;
        }
        return items[(first + index) % items.length];
    }

    @Override
    public Iterator<ItemType> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        ArrayDeque<ItemType> other = (ArrayDeque<ItemType>) o;

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
}
