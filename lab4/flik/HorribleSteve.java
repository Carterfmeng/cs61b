package flik;

public class HorribleSteve {

    public static int compareNum(int num) throws Exception{
        int i = 0;
        int j = 0;
        while (i < 500) {
            if (!Flik.isSameNumber(i, j)) {
                throw new Exception(
                        String.format("i:%d not same as j:%d ??", i, j));
            }
            i++;
            j++;
        }
        return i;
    }

    public static void main(String [] args) throws Exception {
        int i = 0;
        try {
            i = compareNum(500);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        System.out.println("i is " + i);
    }
}
