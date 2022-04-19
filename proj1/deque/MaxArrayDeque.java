package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    public static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    public static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    private Comparator<T> firstComparator;

    public MaxArrayDeque() {
        super();
    }

    public MaxArrayDeque(Comparator<T> c) {
        super();
        firstComparator = c;
    }

    public T max() {
        T maxItem;
        if (size() == 0) {
            return null;
        }
        maxItem = this.get(0);
        for (T item : this) {
            if (firstComparator.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }

    public T max(Comparator<T> c) {
        T maxItem;
        if (size() == 0) {
            return null;
        }
        maxItem = this.get(0);
        for (T item : this) {
            if (c.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }
}
