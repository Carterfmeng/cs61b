package deque;

import java.util.Comparator;

public class MaxArrayDequeTest {

    public static void main(String[] args) {
        Comparator<Integer> c = new MaxArrayDeque.IntegerComparator();
        Comparator<String> c2 = new MaxArrayDeque.StringComparator();

        MaxArrayDeque<Integer> integerArrayDeque = new MaxArrayDeque<>(c);
        integerArrayDeque.addFirst(1);
        integerArrayDeque.addLast(2);
        integerArrayDeque.addFirst(0);
        integerArrayDeque.addLast(2);
        System.out.println(integerArrayDeque.max());
        System.out.println(integerArrayDeque.max(c));

        MaxArrayDeque<String> stringArrayDeque = new MaxArrayDeque<>(c2);
        stringArrayDeque.addFirst("Hello");
        stringArrayDeque.addLast("ayo");
        stringArrayDeque.addFirst("boy");
        stringArrayDeque.addLast("zoo");
        System.out.println(stringArrayDeque.max());
        System.out.println(stringArrayDeque.max(c2));
    }
}
