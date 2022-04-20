package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;

        ArrayDequeIterator() {
            wizPos = 0;
        }

        @Override
        public boolean hasNext() {
            return wizPos < size;
        }

        @Override
        public T next() {
            T returnItem = get(wizPos);
            wizPos = wizPos + 1;
            return returnItem;
        }
    }

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4; // first index == nextFirst + 1;
        nextLast = 5;  // last index == nextLast - 1;
    }

    /** resize this ArrayDeque to the capacity size. **/
    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        int firstIndex = nextFirst + 1;
        int copyNum;
        if (nextFirst == items.length - 1) {
            firstIndex = 0;
        }
        if (capacity > items.length) {
            copyNum = size - firstIndex;
            System.arraycopy(items, firstIndex, newItems, 0, copyNum);
            if (firstIndex != 0) {
                System.arraycopy(items, 0, newItems, copyNum, firstIndex);
            }
        } else {
            copyNum = items.length - firstIndex;
            if (size <= copyNum) {
                System.arraycopy(items, firstIndex, newItems, 0, size);
            } else {
                System.arraycopy(items, firstIndex, newItems, 0, size - copyNum);
                System.arraycopy(items, 0, newItems, size - copyNum, copyNum);
            }
        }
        items = newItems;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        size = size + 1;
        if (nextFirst > 0) {
            nextFirst = nextFirst - 1;
        } else {
            nextFirst = items.length - 1;
        }
    }

    @Override
    public void addLast(T item) {
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
    public T removeFirst() {
        int firstIndex = nextFirst + 1;
        T removedValue;
        if (nextFirst == items.length - 1) {
            firstIndex = 0;
        }
        if (isEmpty()) {
            return null;
        }
        removedValue = items[firstIndex];
        items[firstIndex] = null;
        nextFirst = (nextFirst + 1) % items.length;
        size = size - 1;
        if (size < items.length / 4 && items.length >= 16) {
            resize(items.length / 2);
        }
        return removedValue;
    }

    @Override
    public T removeLast() {
        int lastIndex = nextLast - 1;
        T removedValue;
        if (nextLast == 0) {
            lastIndex = items.length - 1;
        }
        if (isEmpty()) {
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
        if (size < items.length / 4 && items.length >= 16) {
            resize(items.length / 2);
        }
        return removedValue;
    }

    @Override
    public T get(int index) {
        int first = (nextFirst + 1) % items.length;
        if (index >= size) {
            return null;
        }
        return items[(first + index) % items.length];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        ArrayDeque<T> other = (ArrayDeque<T>) o;

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
