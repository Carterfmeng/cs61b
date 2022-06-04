package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Formatter;
import java.util.TreeMap;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *  take a snapshot of all the files in the repo, persistent the data in .gitlet directory.
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The commit time.*/
    private Formatter timestamp;
    /** The {name: file's sha1-hash} map, to store the reference of the files/blobs in this commit. */
    private TreeMap<String, String> blobs;
    /** The parent commit.*/
    private Commit parent;

    /* TODO: fill in the rest of this class. */
    public Commit(String message, int time) {
        this.message = message;
        Formatter fmt = new Formatter();
        this.timestamp = fmt.format("%1$ta %1$tb %1$td %1$tT %1$tY", new Date(Long.valueOf(time)));
    }

    public Commit(String message) {
        Date commitDate = new Date();
        this.message = message;
        Formatter fmt = new Formatter();
        this.timestamp = fmt.format("%1$ta %1$tb %1$td %1$tT %1$tY", commitDate);
    }

    public static void main(String[] args) throws InterruptedException {
        Commit testCommit = new Commit("test commit");
        Commit testCommit2 = new Commit("initial commit", 0);

        System.out.println(testCommit2.timestamp);
        System.out.println(testCommit.timestamp);
    }
}
