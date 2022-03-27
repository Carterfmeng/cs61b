package deque;

public class ArrayDeque<ItemType> {
    public ItemType[] items;
    public int size;

    public ArrayDeque() {
        items = (ItemType[]) new Object[8];
        size = 0;
    }

}
