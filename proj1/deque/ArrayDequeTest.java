package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.Math;


/** Performs some basic linked list tests. */
public class ArrayDequeTest {
    @Test
    /** Adds a few things to the list use addLast() method, check the item one by one **/
    public void addLast() {
        ArrayDeque<Integer> lst = new ArrayDeque<>();
        lst.addLast(1);
        lst.addLast(2);
        lst.addLast(3);
        assertEquals((int) lst.get(0), 1);
        assertEquals((int) lst.get(1), 2);
        assertEquals((int) lst.get(2), 3);
    }

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        ArrayDeque<String> lld1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeFirst();
        // should be empty
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {
        ArrayDeque<String> lld1 = new ArrayDeque<>();
        ArrayDeque<Double>  lld2 = new ArrayDeque<>();
        ArrayDeque<Boolean> lld3 = new ArrayDeque<>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    public void randomAddTest() {
        int N = 1000;
        int dequeSize = 0;
        ArrayDeque<Integer> ard1 = new ArrayDeque<>();

        for (int i = 0; i < N; i++) {
            /* generate random number from 0 - 3 .*/
            int randomOperation = (int) (Math.random() * 4) ;
            if (randomOperation == 0) {
                ard1.addFirst(i);
                dequeSize += 1;
                assertEquals("Should equal to size", dequeSize, ard1.size());
            } else if (randomOperation == 1) {
                ard1.addLast(i);
                dequeSize += 1;
                assertEquals("Should equal to size", dequeSize, ard1.size());
            } else if (randomOperation == 2) {
                if (!ard1.isEmpty()){
                    dequeSize -= 1;
                }
                ard1.removeFirst();
                assertEquals("Should equal to size", dequeSize, ard1.size());
            } else if (randomOperation == 3) {
                if (!ard1.isEmpty()) {
                    dequeSize -= 1;
                }
                ard1.removeLast();
                assertEquals("Should equal to size", dequeSize, ard1.size());
            }
        }
    }

    @Test
    public void getLastIndex() {
        ArrayDeque<Integer> ard1 = new ArrayDeque<>();
        ard1.addFirst(0);
        ard1.removeFirst();
        ard1.addLast(2);
        ard1.addLast(3);
        ard1.addLast(4);
        ard1.addLast(5);
        ard1.addFirst(6);
        ard1.addLast(7);
        ard1.addLast(8);
        ard1.addLast(9);
        ard1.addLast(10);
        ard1.removeLast();
        ard1.removeLast();
        ard1.addFirst(13);
        ard1.removeLast();
        ard1.removeLast();
        ard1.removeLast();
        ard1.removeFirst();
        ard1.addFirst(18);
        ard1.removeLast();
        ard1.removeLast();
    }
}
