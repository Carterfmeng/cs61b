package flik;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestFlik {
    @Test
    public void testFlik() {
        int a = 1;
        int b = 2;
        int a2 = 3;
        int b2 = 3;
        assertTrue("a should not equal b",!Flik.isSameNumber(a, b));
        assertTrue("a should equal b", Flik.isSameNumber(a2, b2));
    }
}
