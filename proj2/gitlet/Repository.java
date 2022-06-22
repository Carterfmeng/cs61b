package gitlet;

import edu.princeton.cs.algs4.StdIn;
import org.eclipse.jetty.http.Http1FieldPreEncoder;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

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
            printFailMsgAndExit("A Gitlet version-control system already exists in the current directory.");
        } else {
            /* Create the directory, and save the initial commit.*/
            GITLET_DIR.mkdir();
            COMMITS_DIR.mkdirs();
            BLOBS_DIR.mkdir();
            /** create the staging area.*/
            STAGED_ADD.createNewFile();
            writeStagingArea(STAGED_ADD, new StagingArea(STAGED_ADD));
            STAGED_REM.createNewFile();
            writeStagingArea(STAGED_REM, new StagingArea(STAGED_REM));
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
            printFailMsgAndExit("File does not exist.");
        }

        StagingArea addArea = readStagingArea(STAGED_ADD);
        /** use the file name & content to create a blob first, then compute the
         *  sha1-hash id of this blob object in the working dir.*/
        String addedBlobContent = readCWDFileContentByName(fileName);
        String addedBlobID = getBlobID(addedBlobContent);

        /** if the added file's corresponding blob doesn't exit, create the new blob file.*/
        if (!blobExist(addedBlobID)) {
            writeBlob(addedBlobContent, addedBlobID);
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
            printFailMsgAndExit("No changes added to the commit.");
        }
        if (message == "" || message == null ) {
            printFailMsgAndExit("Please enter a commit message.");
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
        addArea.dump();
        remArea.dump();

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
        String rmFileContent = readCWDFileContentByName(rmFileName);
        String rmBlobID = getBlobID(rmFileContent);
        /** handle the failure cases.*/
        if (!addArea.getStagedFiles().containsKey(rmFileName) && !HEADCommit.getBlobs().containsKey(rmFileName)) {
            printFailMsgAndExit("No reason to remove the file.");
        }

        if (addArea.getStagedFiles().containsKey(rmFileName)) {
            File deleteBlob = readBlobFile(rmBlobID);
            addArea.getStagedFiles().remove(rmFileName);
            restrictedDelete(deleteBlob);
            writeStagingArea(STAGED_ADD, addArea);
        } else if (HEADCommit.getBlobs().containsKey(rmFileName)) {
            remArea.getStagedFiles().put(rmFileName, rmBlobID);
            writeStagingArea(STAGED_REM, remArea);
            rmFile.delete();
        }
    }

    /** the helper method to print a commit's log.*/
    private static void printCommitLog(Commit toPrintCommit) {
        System.out.println("===");
        System.out.println("commit " + toPrintCommit.getCommitID());
        /** add the Merge info when this is the merge commit.*/
        if (toPrintCommit.getSecondParentID() != null) {
            String firstParentShortID = toPrintCommit.getParentID().substring(0, 7);
            String secondParentShortID = toPrintCommit.getSecondParentID().substring(0, 7);
            System.out.println("Merge: " + firstParentShortID + " " + secondParentShortID);
        }
        System.out.println("Date: " + toPrintCommit.getTimestamp());
        System.out.println(toPrintCommit.getMessage());
        System.out.println();
    }

    public static void log() {
        Commit HEADCommit = readHEADCommit();
        /** while the HEADCommit isn't the initial commit (parent != null), print this commit,
         * then move to the parent commit.*/
        Commit unLogCommit = HEADCommit;
        while (unLogCommit != null) {
            printCommitLog(unLogCommit);
            /** move unLogCommit to the parent commit.*/
            unLogCommit = readCommitObject(unLogCommit.getParentID());
        }
    }

    public static void globalLog() {
        List<String> allCommitsDirs = DIRsIn(COMMITS_DIR);
        for (String dir: allCommitsDirs) {
            List<String> allCommitsInDir = plainFilenamesIn(join(COMMITS_DIR, dir));
            for (String commit: allCommitsInDir) {
                File toLogCommitFile = join(COMMITS_DIR, dir, commit);
                Commit toLogCommit = readObject(toLogCommitFile, Commit.class);
                printCommitLog(toLogCommit);
            }
        }
    }

    public static void find(String findMessage) {
        List<String> allCommitsDirs = DIRsIn(COMMITS_DIR);
        int existCommitNum = 0;
        for (String dir: allCommitsDirs) {
            List<String> allCommitsInDir = plainFilenamesIn(join(COMMITS_DIR, dir));
            for (String commit: allCommitsInDir) {
                File toLogCommitFile = join(COMMITS_DIR, dir, commit);
                Commit toLogCommit = readObject(toLogCommitFile, Commit.class);
                if (toLogCommit.getMessage().contains(findMessage)) {
                    System.out.println(toLogCommit.getCommitID());
                    existCommitNum++;
                }
            }
        }
        /** handle the failure case.*/
        if (existCommitNum == 0) {
            printFailMsgAndExit("Found no commit with that message.");
        }
    }

    public static void status() {
        /** read the persistence files to get the status contents.*/
        /** branches. */
        List<String> branchNames = plainFilenamesIn(BRANCHES_DIR);
        File HEADBranch = readHEADBranch();
        String HEADBranchPath = HEADBranch.toString();
        /** StagingArea .*/
        StagingArea addArea = readStagingArea(STAGED_ADD);
        StagingArea remArea = readStagingArea(STAGED_REM);
        /** TODO: read the Modifications Not Staged For Commit
         *  TODO: read the Untracked Files .*/

        /** print Branches. */
        System.out.println("=== Branches ===");
        for (String branchName: branchNames) {
            /** if the branch is the head branch, print an asterisk. */
            if (HEADBranchPath.contains(branchName)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }
        System.out.println();
        /** print Staged Files. */
        TreeMap<String, String> addStagedFiles = addArea.getStagedFiles();
        System.out.println("=== Staged Files ===");
        for (Map.Entry<String, String> entry: addStagedFiles.entrySet()) {
            System.out.println(entry.getKey());
        }
        System.out.println();
        /** print Removed Files. */
        System.out.println("=== Removed Files ===");
        TreeMap<String, String> remStagedFiles = remArea.getStagedFiles();
        for (Map.Entry<String, String> entry: remStagedFiles.entrySet()) {
            /** remove the entry in the remStagedArea.*/
            System.out.println(entry.getKey());
        }
        System.out.println();
        /** print Modifications Not Staged For Commit. */
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        /** print Untracked Files. */
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /** need three different checkOut() method with different num of args
     * 1. java gitlet.Main checkout -- [file name]. */
    public static void checkoutFile(String checkoutFileName) throws IOException {
        /** read the checkoutBlob from the head Commit.*/
        Commit HEADCommit = readHEADCommit();
        String checkoutBlobID = HEADCommit.getBlobs().get(checkoutFileName);
        /** failure case: if the checkoutBlobID == null, fail.*/
        if (checkoutBlobID == null) {
            printFailMsgAndExit("File does not exist in that commit.");
        }
        File checkoutBlobFile = readBlobFile(checkoutBlobID);
        String checkoutBlobContent = readBlobContent(checkoutBlobFile);
        File workingDirFile = join(CWD, checkoutFileName);
        if (!workingDirFile.exists()) {
            workingDirFile.createNewFile();
        }
        writeContents(workingDirFile, checkoutBlobContent);
    }
    /** 2. java gitlet.Main checkout [commit id] -- [file name]. */
    public static void checkoutFile(String commitID, String checkoutFileName) throws IOException {
        /** Handle failure cases:
         * when read checkoutCommit, handle the failure case when Commit doesn't exist.*/
        Commit checkoutCommit = readCommitObjectMayAbb(commitID);
        String checkoutBlobID = checkoutCommit.getBlobs().get(checkoutFileName);
        if (checkoutBlobID == null) {
            printFailMsgAndExit("File does not exist in that commit.");
        }
        File checkoutBlobFile = readBlobFile(checkoutBlobID);
        String checkoutBlobContent = readBlobContent(checkoutBlobFile);
        File workingDirFile = join(CWD, checkoutFileName);
        if (!workingDirFile.exists()) {
            workingDirFile.createNewFile();
        }
        writeContents(workingDirFile, checkoutBlobContent);
    }

    /** 3. java gitlet.Main checkout [branch name]. */
    public static void checkoutBranch(String branchName) throws IOException {
        /** read current branch. */
        String currBranchName = getCurrBranchName();
        Commit currCommit = readHEADCommit();
        TreeMap<String, String> currCommitBlobs = currCommit.getBlobs();
        /** read checkout branch's commit. */
        String toCheckoutCommitID = readBranch(branchName);
        Commit toCheckoutCommit = readCommitObject(toCheckoutCommitID);
        TreeMap<String, String> toCheckoutCommitBlobs = toCheckoutCommit.getBlobs();
        /** read staging Area. */
        StagingArea addArea = readStagingArea(STAGED_ADD);
        /** handle the failure cases: */
        if (toCheckoutCommitID == null) {
            printFailMsgAndExit("No such branch exists.");
        }
        if (currBranchName == branchName) {
            printFailMsgAndExit("No need to checkout the current branch.");
        }
        Set<String> untrackedFileSet = getUntrackedFilesSet(currCommit);
        if (IsACommitOverWriteUntrackedFile(untrackedFileSet, toCheckoutCommitBlobs)) {
            printFailMsgAndExit("There is an untracked file in the way; delete it, or add and commit it first.");
        }

        /** checkout all the files in the corresponding branch. */
        currCommitBlobs = checkoutFilesInABranch(currCommitBlobs, toCheckoutCommitBlobs);

        writeHEAD(branchName);
        /** TODO: Any files that are tracked in the current branch but are not present in the checked-out branch are deleted. -- use a helper method and reuse it in rm command*/

        addArea.dump();
    }

    /** get the untracked files in the current commit:
     * 1. get all the files' name in the working dir, convert to a Set;
     * 2. remove all the file names show in the STAGED_ADD area;
     * 3. remove all the file names show in the current commit's Blobs;
     * 4. add all the file names show in the STAGED_REM area;
     * if the Set not null, then got untracked files, which are the content of Set. */
    private static Set<String> getUntrackedFilesSet(Commit currCommit) {
        List<String> filesInWorkingDir = plainFilenamesIn(CWD);
        TreeMap<String, String> Blobs = currCommit.getBlobs();
        Set<String> untrackedFileSet = new HashSet<>();
        if (filesInWorkingDir != null) {
            untrackedFileSet = new HashSet<>(filesInWorkingDir);
        }
        /** remove all the file names show in the STAGED_ADD area. */
        StagingArea addArea = readStagingArea(STAGED_ADD);
        TreeMap<String, String> stagedForAddFiles = addArea.getStagedFiles();
        for (Map.Entry<String, String> blobEntry: stagedForAddFiles.entrySet()) {
            untrackedFileSet.remove(blobEntry.getKey());
        }
        /** remove all the file names show in the Commit's Blobs. */
        for (Map.Entry<String, String> blobEntry: Blobs.entrySet()) {
            untrackedFileSet.remove(blobEntry.getKey());
        }
        /** add all the file names show in the STAGED_REM area. */
        StagingArea remArea = readStagingArea(STAGED_REM);
        TreeMap<String, String> stagedForRemFiles = remArea.getStagedFiles();
        for (Map.Entry<String, String> blobEntry: stagedForRemFiles.entrySet()) {
            untrackedFileSet.add(blobEntry.getKey());
        }
        return untrackedFileSet;
    }

    /** create a new branch, but don't switch to it until be checkout. */
    public static void branch(String branchName) throws IOException {
        /** handle the failure cases. */
        List<String> existBranches = plainFilenamesIn(BRANCHES_DIR);
        Set<String > existBranchesSet = new HashSet<>(existBranches);
        if (existBranchesSet.contains(branchName)) {
            printFailMsgAndExit("A branch with that name already exists.");
        }
        /** create a new branch file, and write the head commitID to this file.*/
        Commit HEADCommit = readHEADCommit();
        writeBranch(branchName, HEADCommit.getCommitID());
    }

    /** remove the existing branch. */
    public static void rmBranch(String branchName) {
        /** handle the failure cases. */
        File HEADBranch = readHEADBranch();
        File rmBranchFile = join(BRANCHES_DIR, branchName);
        List<String> existBranches = plainFilenamesIn(BRANCHES_DIR);
        Set<String > existBranchesSet = new HashSet<>(existBranches);
        if (!existBranchesSet.contains(branchName)) {
            printFailMsgAndExit("A branch with that name does not exist.");
        }
        if (HEADBranch == rmBranchFile) {
            printFailMsgAndExit("Cannot remove the current branch.");
        }
        /** remove the branch.*/
        restrictedDelete(rmBranchFile);
    }
}
