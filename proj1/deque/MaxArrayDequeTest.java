package deque;

import java.util.Comparator;

public class MaxArrayDequeTest {
    private static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    private static IntegerComparator getIntegerComparator() {
        return new IntegerComparator();
    }

    private static StringComparator getStringComparator() {
        return new StringComparator();
    }

    public static void main(String[] args) {
        Comparator<Integer> c = MaxArrayDequeTest.getIntegerComparator();
        Comparator<String> c2 = MaxArrayDequeTest.getStringComparator();

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
