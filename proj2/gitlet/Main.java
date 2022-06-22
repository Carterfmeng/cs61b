package gitlet;

import java.io.IOException;
import static gitlet.Utils.*;

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
            printFailMsgAndExit("Please enter a command.");
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
            case "find":
                validateNumArgsIs("find", args, 2);
                String findMessage = args[1];
                Repository.find(findMessage);
                break;
            case "status":
                validateNumArgsIs("status", args, 1);
                Repository.status();
                break;
            case "checkout":
                validateMultiNumArgsAre("checkout", args, 2, 4);
                if (args.length == 3) {
                    String checkoutFileName = args[2];
                    Repository.checkoutFile(checkoutFileName);
                } else if (args.length == 4) {
                    String commitID = args[1];
                    String checkoutFileName = args[3];
                    Repository.checkoutFile(commitID, checkoutFileName);
                } else {
                    String branchName = args[1];
                    Repository.checkoutBranch(branchName);
                }
                break;
            case "branch":
                validateNumArgsIs("branch", args, 2);
                String addBranchName = args[1];
                Repository.branch(addBranchName);
                break;
            case "rm-branch":
                validateNumArgsIs("rm-branch", args, 2);
                String rmBranchName = args[1];
                Repository.rmBranch(rmBranchName);
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
            printFailMsgAndExit("Incorrect operands.");
        }
    }

    public static void validateMultiNumArgsAre(String cmd, String[] args, int minNum, int maxNum) {
        if (args.length < minNum || args.length > maxNum) {
            printFailMsgAndExit("Incorrect operands.");
        }
    }
}
