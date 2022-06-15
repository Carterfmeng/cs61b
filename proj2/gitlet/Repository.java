package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.TreeMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The repo file which save the serialized repo object. */
    public static final File REPO = join(GITLET_DIR, "repo");
    /** the objects directory, which save the commits and files/blobs together. */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final Commit initialCommit = new Commit("initial commit", 0);

    private TreeMap<String, String> branches;
    private String HEAD;
    private TreeMap<String, String> stagedForAddition;
    private TreeMap<String, String> stagedForRemoval;


    /* TODO: fill in the rest of this class. */
    public Repository() {
        branches = new TreeMap<>();
        branches.put("master", null);
        stagedForAddition = new TreeMap<>();
        stagedForRemoval = new TreeMap<>();
    }

    public static void setupPersistence() throws IOException {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        } else {
            /** Create the directory, and save the initial commit*/
            GITLET_DIR.mkdir();
            OBJECTS_DIR.mkdir();
            /** compute the sha1-hash of initial commit.*/
            String initialCommitID = sha1(serialize(initialCommit));
            File commitDIR = join(OBJECTS_DIR, initialCommitID.substring(0, 2));
            File initialCommitFile = join(commitDIR, initialCommitID.substring(2));
            commitDIR.mkdir();
            initialCommitFile.createNewFile();
            writeObject(initialCommitFile, initialCommit);
            /** create the repo instance*/
            Repository repo = new Repository();
            repo.branches.put("master", initialCommitID);
            repo.HEAD = initialCommitID;
            REPO.createNewFile();
            writeObject(REPO, repo);
        }
    }

    public static void initGitLet() throws IOException {
        setupPersistence();
    }

    private static boolean isAddFileExists(String fileName) {
        File thisFile = join(CWD, fileName);
        if (thisFile.exists()) {
            return true;
        }
        return false;
    }

    private static String readFileContent(String fileName) {
        File thisFile = join(CWD, fileName);
        return readContentsAsString(thisFile);
    }

    private static Repository getRepo() {
        Repository repo = readObject(REPO, Repository.class);
        return repo;
    }

    public static void add(String fileName) throws IOException {
        if (!isAddFileExists(fileName)) {
            System.out.println("File does not exist.");
            System.exit(0);
        } else {
            Repository repo = getRepo();
            String blobContent = readFileContent(fileName);
            /** the sha1-hash id of this file in the working dir.*/
            String blobID = sha1(blobContent);
            if (!Blob.blobExists(blobID)) {
                Blob fileBlob = new Blob(fileName, blobID, blobContent);
                fileBlob.save();
                repo.stagedForAddition.put(fileName, blobID);
            } else {
                /** if the blob is same as last commit, then remove it from
                 * the stage area, else update the blobID to the middle version;
                 */
                File lastCommitDIR = join(OBJECTS_DIR, repo.HEAD.substring(0, 2));
                File lastCommitFile = join(lastCommitDIR, repo.HEAD.substring(2));
                Commit lastCommit = readObject(lastCommitFile, Commit.class);
                /** get the same fileName's blob in last commit, if it's not in the last commit, get null .*/
                TreeMap<String, String> lastCommitBlobs = lastCommit.getBlobs();
                Boolean containFile = lastCommitBlobs.containsKey(fileName);
                if (!containFile) {
                    repo.stagedForAddition.put(fileName, blobID);
                } else {
                    String lastCommitBlobID = lastCommitBlobs.get(fileName);
                    File lastBlobDIR = join(OBJECTS_DIR, lastCommitBlobID.substring(0, 2));
                    File lastBlobFile = join(lastBlobDIR, lastCommitBlobID.substring(2));
                    Blob lastBlob = readObject(lastBlobFile, Blob.class);
                    String lastBlobID = sha1(lastBlob.getBlobContent());
                    /** if the blob is same as last commit, remove it from the stage area.*/
                    if (lastBlobID == blobID) {
                        repo.stagedForAddition.remove(fileName);
                    }
                }
            }
        }
    }
}
