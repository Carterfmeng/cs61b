package deque;

import net.sf.saxon.om.Item;


public class ArrayDeque<ItemType> {
    public ItemType[] items;
    public int size;
    public int nextFirst;
    public int nextLast;

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

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

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

    public ItemType get(int index) {
        int first = (nextFirst + 1) % items.length;
        if (index >= size) {
            return null;
        }
        return items[(first + index) % items.length];
    }




}
