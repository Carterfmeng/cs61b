package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
      AListNoResizing<Integer> lstRight = new AListNoResizing<>();
      BuggyAList<Integer> lstBuggy = new BuggyAList<>();
      lstRight.addLast(4);
      lstRight.addLast(5);
      lstRight.addLast(6);
      lstBuggy.addLast(4);
      lstBuggy.addLast(5);
      lstBuggy.addLast(6);
      assertEquals(lstRight.size(), lstBuggy.size());
      assertEquals(lstRight.removeLast(), lstBuggy.removeLast());
      assertEquals(lstRight.removeLast(), lstBuggy.removeLast());
      assertEquals(lstRight.removeLast(), lstBuggy.removeLast());
    }

    @Test
    public void randomizedTest() {
      AListNoResizing<Integer> L = new AListNoResizing<>();
      BuggyAList<Integer> BL = new BuggyAList<>();

      int N = 5000;
      for (int i = 0; i < N; i += 1) {
        int operationNumber = StdRandom.uniform(0, 4);
        if (operationNumber == 0) {
          // addLast
          int randVal = StdRandom.uniform(0, 100);
          L.addLast(randVal);
          BL.addLast(randVal);
          System.out.println("addLast(" + randVal + ")");
        } else if (operationNumber == 1) {
          // size
          int size = L.size();
          int size2 = BL.size();
          System.out.println("size: " + size);
          assertEquals(size, size2);
        } else if (operationNumber == 2) {
          if (L.size() > 0) {
            int gettedLast = L.getLast();
            int gettedLast2 = BL.getLast();
            System.out.println("getLast(" + gettedLast + ")");
            assertEquals(gettedLast, gettedLast2);
          }
        } else if (operationNumber == 3) {
          if (L.size() > 0) {
            int removedLast = L.removeLast();
            int removedLast2 = BL.removeLast();
            System.out.println("removeLast(" + removedLast + ")");
            assertEquals(removedLast, removedLast2);
          }
        }
      }
    }

}
