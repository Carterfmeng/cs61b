package gitlet;

// TODO: any imports you need here
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Formatter;
import java.util.TreeMap;

import static gitlet.Utils.*;

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
    private final String message;
    /** The commit time.*/
    private final String timestamp;
    /** The {name: file's sha1-hash} map, to store the reference of the files/blobs in this commit. */
    private TreeMap<String, String> blobs;
    /** The parent commit.*/
    private final String parentID;
    /** The second parent ID for the merging commit.*/
    private final String secondParentID;

    /* TODO: fill in the rest of this class. */
    public Commit(String message, int time) {
        this.message = message;
        Formatter fmt = new Formatter();
        this.timestamp = fmt.format("%1$ta %1$tb %1$td %1$tT %1$tY", new Date((long) time)).toString();
        this.blobs = new TreeMap<>();
        this.parentID = null;
        this.secondParentID = null;
    }

    public Commit(Commit that, String message) {
        this.message = message;
        Date commitDate = new Date();
        Formatter fmt = new Formatter();
        this.timestamp = fmt.format("%1$ta %1$tb %1$td %1$tT %1$tY", commitDate).toString();
        this.parentID = that.getCommitID();
        this.secondParentID = null;
        /** deep copy of the parent's blobs.*/
        this.blobs = new TreeMap<>(that.getBlobs());
    }

    public Commit(Commit that, String secondParentID, String message) {
        this.message = message;
        Date commitDate = new Date();
        Formatter fmt = new Formatter();
        this.timestamp = fmt.format("%1$ta %1$tb %1$td %1$tT %1$tY", commitDate).toString();
        this.parentID = that.getCommitID();
        this.secondParentID = secondParentID;
        /** deep copy of the parent's blobs.*/
        this.blobs = new TreeMap<>(that.getBlobs());
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

    public String getParentID() { return this.parentID;}

    public String getSecondParentID() { return this.secondParentID; }

    public Commit getParent() {
        return readCommitObject(this.parentID);
    }

    public Commit getSecondParent(String secondParentID) {
        return readCommitObject(secondParentID);
    }

    public String getCommitID() {
        return sha1((Object) serialize(this));
    }

    public static void main(String[] args) throws InterruptedException {
        Commit testCommit2 = new Commit("initial commit", 0);
        System.out.println(testCommit2.timestamp);
    }
}
