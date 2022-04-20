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
        String errorMessage = "";
        for (int i = 0; i < N; i++) {
            /* generate random number from [0, 5). */
            int randomOperationNum = StdRandom.uniform(5);
            if (randomOperationNum == 0) {
                sad1.addFirst(i);
                ads1.addFirst(i);
                errorMessage += "addFirst(" + i + ")\n";
            } else if (randomOperationNum == 1) {
                sad1.addLast(i);
                ads1.addLast(i);
                errorMessage += "addLast(" + i + ")\n";
            } else if (randomOperationNum == 2) {
                if (sad1.size() > 0) {
                    Integer removedValueStudent = sad1.removeFirst();
                    Integer removedValueSolution = ads1.removeFirst();
                    errorMessage += "removeFirst()\n";
                    assertEquals(errorMessage, removedValueStudent, removedValueSolution);
                }
            } else if (randomOperationNum == 3) {
                if (sad1.size() > 0) {
                    Integer removedValueStudent = sad1.removeLast();
                    Integer removedValueSolution = ads1.removeLast();
                    errorMessage += "removeLast()\n";
                    assertEquals(errorMessage, removedValueStudent, removedValueSolution);
                }
            }
        }

    }


}
