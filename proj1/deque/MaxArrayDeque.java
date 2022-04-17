package deque;

import net.sf.saxon.functions.Minimax;

import java.util.Comparator;

public class MaxArrayDeque<ItemType> extends ArrayDeque <ItemType> {
    public static class integerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    public static class stringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    public Comparator<ItemType> firstComparator;

    public MaxArrayDeque() {
        super();
    }

    public MaxArrayDeque(Comparator<ItemType> c) {
        super();
        firstComparator = c;
    }

    public ItemType max() {
        ItemType maxItem;
        if (size == 0) {
            return null;
        }
        maxItem = this.get(0);
        for (ItemType item : this) {
            if (firstComparator.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }

    public ItemType max(Comparator<ItemType> c) {
        ItemType maxItem;
        if (size == 0) {
            return null;
        }
        maxItem = this.get(0);
        for (ItemType item : this) {
            if (c.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }
}
