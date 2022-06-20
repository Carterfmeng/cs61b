package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        if (validateNumArgsIs(args, 0)) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgsIs("init", args, 1);
                Repository.initGitLet();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgsIs("add", args, 2);
                String fileName = args[1];
                Repository.add(fileName);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                validateNumArgsIs("commit", args, 2);
                String message = args[1];
                Repository.commit(message);
                break;
            case "rm":
                validateNumArgsIs("rm", args, 2);
                String rmFileName = args[1];
                Repository.rm(rmFileName);
                break;
            case "log":
                validateNumArgsIs("log", args, 1);
                Repository.log();
                break;
            case "global-log":
                validateNumArgsIs("global-log", args, 1);
                Repository.globalLog();
                break;
        }
    }

    public static boolean validateNumArgsIs(String[] args, int num) {
        if (args.length == num) {
            return true;
        }
        return false;
    }

    public static void validateNumArgsIs(String cmd, String[] args, int num) {
        if (args.length != num) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}
