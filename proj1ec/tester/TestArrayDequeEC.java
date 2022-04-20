package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void task1Test() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();
        int N = 500;
        for (int i = 0; i < N; i++) {
            /* generate random number from [0, 5). */
            int randomOperationNum = StdRandom.uniform(5);
            if (randomOperationNum == 0) {
                sad1.addFirst(i);
                ads1.addFirst(i);
                System.out.println("sad1 ==> addFirst(" + i + ")");
                System.out.println("ads1 ==> addFirst(" + i + ")");
                assertEquals("deque size not equal", sad1.size(), ads1.size());
            } else if (randomOperationNum == 1) {
                sad1.addLast(i);
                ads1.addLast(i);
                System.out.println("sad1 ==> addLast(" + i + ")");
                System.out.println("ads1 ==> addLast(" + i + ")");
                assertEquals("deque size not equal", sad1.size(), ads1.size());
            } else if (randomOperationNum == 2) {
                if (sad1.size() > 0) {
                    int removedValueStudent = sad1.removeFirst();
                    int removedValueSolution = ads1.removeFirst();
                    System.out.println("sad1 ==> removeFirst(" + removedValueStudent + ")");
                    System.out.println("ads1 ==> removeFirst(" + removedValueSolution + ")");
                    assertEquals("remove value not the same", removedValueStudent, removedValueSolution);
                }
            } else if (randomOperationNum == 3) {
                if (sad1.size() > 0) {
                    int removedValueStudent = sad1.removeLast();
                    int removedValueSolution = ads1.removeLast();
                    System.out.println("sad1 ==> removeLast(" + removedValueStudent + ")");
                    System.out.println("ads1 ==> removeLast(" + removedValueSolution + ")");
                    assertEquals("remove value not the same", removedValueStudent, removedValueSolution);
                }
            }
        }

    }


}
