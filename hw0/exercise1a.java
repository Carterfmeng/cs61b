public class exercise1a {
    public static void main(String[] args) {
      int i = 0;
      String line = "*";
      while (i < 5) {
        System.out.println(line);
        line = line + '*';
        i = i + 1;
      }
    }
}