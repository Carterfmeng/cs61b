public class exercise1b {
    public static void drawTriangle(int N) {
      int i = 0;
      String line = "*";
      while (i < N) {
        System.out.println(line);
        line = line + '*';
        i = i + 1;
      }
    }
    
    public static void main(String[] args) {
      drawTriangle(10);
    }
}