package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
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
    /** the objects directory, which save the commits and files/blobs in two respective dir. */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    /** the commits dir .*/
    public static final File COMMITS_DIR = join(OBJECTS_DIR, "commits");
    /** the blobs dir .*/
    public static final  File BLOBS_DIR = join(OBJECTS_DIR, "blobs");
    /** the branch directory, where save the different branches' name and lastest commitID.*/
    public static final File BRANCHES_DIR = join(GITLET_DIR,"heads");
    /** The Staging area file. */
    public static final File STAGED_ADD = join(GITLET_DIR, "addIndex");
    public static final File STAGED_REM = join(GITLET_DIR, "remIndex");

    /* TODO: fill in the rest of this class. */

    public static void initGitLet() throws IOException {
        Commit initialCommit = new Commit("initial commit", 0);

        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        } else {
            /* Create the directory, and save the initial commit.*/
            GITLET_DIR.mkdir();
            COMMITS_DIR.mkdirs();
            BLOBS_DIR.mkdir();
            /** create the staging area.*/
            STAGED_ADD.createNewFile();
            writeStagingArea(STAGED_ADD, new StagingArea());
            STAGED_REM.createNewFile();
            writeStagingArea(STAGED_REM, new StagingArea());
            /** compute the sha1-hash of initial commit.*/
            writeCommitObject(initialCommit);
            /** create the BRANCHES_DIR: ref/heads directory.*/
            BRANCHES_DIR.mkdir();
            writeBranch("main", initialCommit.getCommitID());
            /** create the HEAD file, and save the ref to the corresponding branch file.*/
            writeHEAD("main");
        }
    }


    public static void add(String fileName) throws IOException {
        /** if add fileName don't exist, exit.*/
        if (!isAddFileExists(fileName)) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        StagingArea addArea = readStagingArea(STAGED_ADD);
        /** use the file name & content to create a blob first, then compute the
         *  sha1-hash id of this blob object in the working dir.*/
        String addedBlobContent = readFileContent(fileName);
        Blob addedBlob = new Blob(fileName, addedBlobContent);
        String addedBlobID = addedBlob.getBlobID();

        /** if the added file's corresponding blob doesn't exit, create the new blob file.*/
        if (!Blob.blobExists(addedBlobID)) {
            addedBlob.save();
            addArea.getStagedFiles().put(fileName, addedBlobID);
        } else {
            /** if the blob is not same as the head commit, update the blobID to the middle version.*/
            Commit headCommit = readHEADCommit();
            /** get the same fileName's blob in last commit, if it's not in the head commit, get null .*/
            TreeMap<String, String> headCommitBlobs = headCommit.getBlobs();
            Boolean containFile = headCommitBlobs.containsKey(fileName);
            if (!containFile) {
                addArea.getStagedFiles().put(fileName, addedBlobID);
            } else {
                String headCommitBlobID = headCommitBlobs.get(fileName);
                /** if the blob is same as last commit, remove it from the stage area.*/
                if (headCommitBlobID == addedBlobID) {
                    addArea.getStagedFiles().remove(fileName);
                }
            }
        }
        /** save the STAGED_ADD file.*/
        writeStagingArea(STAGED_ADD, addArea);
    }

    public static void commit(String message) throws IOException {
        /** handle the failure cases.*/
        StagingArea addArea = readStagingArea(STAGED_ADD);
        StagingArea remArea = readStagingArea(STAGED_REM);
        if (addArea.getStagedFiles().size() == 0 && remArea.getStagedFiles().size() == 0 ) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        if (message == "" || message == null ) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        /** deep copy the commit from the HEAD commit.*/
        Commit headCommit = readHEADCommit();
        Commit freshCommit = new Commit(headCommit, message);
        TreeMap<String, String> addStagedFiles = addArea.getStagedFiles();
        TreeMap<String, String> remStagedFiles = remArea.getStagedFiles();
        for (Map.Entry<String, String> entry: addStagedFiles.entrySet()) {
            /** change the blob's sha1-ID in the addStagedArea.*/
            freshCommit.getBlobs().put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry: remStagedFiles.entrySet()) {
            /** remove the entry in the remStagedArea.*/
            freshCommit.getBlobs().remove(entry.getKey());
        }

        /** save the freshCommit.*/
        writeCommitObject(freshCommit);
        /** clear and save the staging area.*/
        addArea.getStagedFiles().clear();
        remArea.getStagedFiles().clear();
        writeStagingArea(STAGED_ADD, addArea);
        writeStagingArea(STAGED_REM, remArea);

        /** change the head's branch pointer to the fresh commit and save.*/
        File headBranch = readHEADBranch();
        writeContents(headBranch, freshCommit.getCommitID());
    }

    public static void rm(String rmFileName) {
        StagingArea addArea = readStagingArea(STAGED_ADD);
        StagingArea remArea = readStagingArea(STAGED_REM);
        File rmFile = join(CWD, rmFileName);
        Commit HEADCommit = readHEADCommit();
        /** use the file name & content to create a blob first, then compute the
         *  sha1-hash id of this blob object in the working dir.*/
        String rmFileContent = readFileContent(rmFileName);
        Blob rmBlob = new Blob(rmFileName, rmFileContent);
        String rmBlobID = rmBlob.getBlobID();
        /** handle the failure cases.*/
        if (!addArea.getStagedFiles().containsKey(rmFileName) && !HEADCommit.getBlobs().containsKey(rmFileName)) {
            System.out.println("No reason to remove the file.");
            System.out.println(HEADCommit.getBlobs());
            System.exit(0);
        }

        if (addArea.getStagedFiles().containsKey(rmFileName)) {
            File deleteBlob = readObjectFileFromID(rmBlobID, BLOBS_DIR);
            addArea.getStagedFiles().remove(rmFileName);
            restrictedDelete(deleteBlob);
            writeStagingArea(STAGED_ADD, addArea);
        } else if (HEADCommit.getBlobs().containsKey(rmFileName)) {
            remArea.getStagedFiles().put(rmFileName, rmBlobID);
            writeStagingArea(STAGED_REM, remArea);
            rmFile.delete();
        }
    }

    public static void log() {
        Commit HEADCommit = readHEADCommit();
        /** while the HEADCommit isn't the initial commit (parent != null), print this commit,
         * then move to the parent commit.*/
        Commit unLogCommit = HEADCommit;
        while (unLogCommit != null) {
            System.out.println("===");
            System.out.println("commit " + unLogCommit.getCommitID());
            /** add the Merge info when this is the merge commit.*/
            if (unLogCommit.getSecondParentID() != null) {
                String firstParentShortID = unLogCommit.getParentID().substring(0, 7);
                String secondParentShortID = unLogCommit.getSecondParentID().substring(0, 7);
                System.out.println("Merge: " + firstParentShortID + " " + secondParentShortID);
            }
            System.out.println("Date: " + unLogCommit.getTimestamp());
            System.out.println(unLogCommit.getMessage());
            System.out.println();
            /** move unLogCommit to the parent commit.*/
            unLogCommit = readCommitObject(unLogCommit.getParentID());
        }
    }
}
