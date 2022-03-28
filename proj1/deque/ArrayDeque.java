package deque;

import net.sf.saxon.om.Item;

/** invariants:
 * 1.First = nextFirst + 1, nextFirst is always negative;
 * 2.Last = nextLast - 1, next Last is always positive; **/
public class ArrayDeque<ItemType> {
    public ItemType[] items;
    public int size;
    public int nextFirst;
    public int nextLast;

    public ArrayDeque() {
        items = (ItemType[]) new Object[8];
        size = 0;
        nextFirst = -1; // first index == nextFirst + 1;
        nextLast = 0;     // last index == nextLast - 1;
    }

    /** resize this ArrayDeque to the capacity size. **/
    private void resize(int capacity) {
        ItemType[] newItems = (ItemType[]) new Object[capacity];
        System.arraycopy(items, nextFirst + 1, newItems, 0, size);
        items = newItems;
        nextFirst = -1;
        nextLast = size;
    }

    public void addFirst(ItemType item) {
        if (size == items.length) {
            resize(size * 2);
        } else {
            items[nextFirst] = item;
            size = size + 1;
            nextFirst = nextFirst - 1;
        }
    }

    public void addLast(ItemType item) {
        if (size >= items.length) {
            resize(size * 2);
        } else {
            items[nextLast] = item;
            size = size + 1;
            nextLast = nextLast + 1;
        }
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int first = nextFirst + 1;
        for (int printedNum = 0; printedNum < size; printedNum++) {
            if (first + printedNum >= size) {
                first = -printedNum;
            }
            System.out.print(items[first + printedNum]);
            System.out.print(" ");
        }
    }

    public ItemType removeFirst() {
        int first = nextFirst + 1;
        ItemType removedValue;
        if (size == 0) {
            return null;
        }
        removedValue = items[first];
        items[first] = null;
        nextFirst = nextFirst + 1;
        size = size - 1;
        if (size < items.length / 4 && items.length < 16) {
            resize(items.length / 2);
        }
        return removedValue;
    }

    public ItemType removeLast() {
        int last = nextLast - 1;
        ItemType removedValue;
        if (size == 0) {
            return null;
        }
        removedValue = items[last];
        items[last] = null;
        nextLast = nextLast - 1;
        size = size - 1;
        if (size < items.length / 4 && items.length < 16) {
            resize(items.length / 2);
        }
        return removedValue;
    }

    public ItemType get(int index) {
        int first = nextFirst + 1;
        if (index >= size) {
            return null;
        }
        return items[first + index];
    }




}
