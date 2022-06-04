package gitlet;

public class test {
    static void message(String msg, Object... args) {
        System.out.printf(msg, args);
        System.out.println();
    }

    public static void main(String[] args) {
        boolean adult = true;
        message("%b%n", adult);
    }
}
