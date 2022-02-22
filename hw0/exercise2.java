public class exercise2 {
    public static int max(int[] m) {
       int len = m.length;
       int i = 0;
       int maxNum = 0;
       while (i < len) {
         if (m[i] > maxNum) {
           maxNum = m[i];
         }
         i = i + 1;
       }
       return maxNum;
    }
    public static void main(String[] args) {
       int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};   
       max(numbers);
    }
 }