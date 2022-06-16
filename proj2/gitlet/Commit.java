package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Formatter;
import java.util.TreeMap;

import static gitlet.Utils.serialize;
import static gitlet.Utils.sha1;

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
    private String timestamp;
    /** The {name: file's sha1-hash} map, to store the reference of the files/blobs in this commit. */
    private TreeMap<String, String> blobs;
    /** The parent commit.*/
    private Commit parent;

    /* TODO: fill in the rest of this class. */
    public Commit(String message, int time) {
        this.message = message;
        Formatter fmt = new Formatter();
        this.timestamp = fmt.format("%1$ta %1$tb %1$td %1$tT %1$tY", new Date(Long.valueOf(time))).toString();
        this.blobs = new TreeMap<>();
        this.parent = null;
    }

    public Commit(String message, Commit parent) {
        Date commitDate = new Date();
        this.message = message;
        Formatter fmt = new Formatter();
        this.timestamp = fmt.format("%1$ta %1$tb %1$td %1$tT %1$tY", commitDate).toString();
        this.parent = parent;
        this.blobs = parent.blobs;

    }

    public String getMessage() {
        return this.message;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public TreeMap<String, String> getBlobs() {
        return this.blobs;
    }

    public Commit getParent() {
        return this.parent;
    }

    public String getCommitID() {
        return sha1(serialize(this));
    }


    public static void main(String[] args) throws InterruptedException {
        Commit testCommit2 = new Commit("initial commit", 0);
        Commit testCommit = new Commit("test commit", testCommit2);
        System.out.println(testCommit2.timestamp);
        System.out.println(testCommit.timestamp);
    }
}
