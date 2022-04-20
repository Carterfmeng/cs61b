package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> firstComparator;

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
