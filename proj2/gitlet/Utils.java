package gitlet;

import org.checkerframework.checker.units.qual.C;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;


/** Assorted utilities.
 *
 * Give this file a good read as it provides several useful utility functions
 * to save you some time.
 *
 *  @author P. N. Hilfinger
 */
class Utils {

    /**
     * The length of a complete SHA-1 UID as a hexadecimal numeral.
     */
    static final int UID_LENGTH = 40;

    /* SHA-1 HASH VALUES. */

    /**
     * Returns the SHA-1 hash of the concatenation of VALS, which may
     * be any mixture of byte arrays and Strings.
     */
    static String sha1(Object... vals) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            for (Object val : vals) {
                if (val instanceof byte[]) {
                    md.update((byte[]) val);
                } else if (val instanceof String) {
                    md.update(((String) val).getBytes(StandardCharsets.UTF_8));
                } else {
                    throw new IllegalArgumentException("improper type to sha1");
                }
            }
            Formatter result = new Formatter();
            for (byte b : md.digest()) {
                result.format("%02x", b);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException excp) {
            throw new IllegalArgumentException("System does not support SHA-1");
        }
    }

    /**
     * Returns the SHA-1 hash of the concatenation of the strings in
     * VALS.
     */
    static String sha1(List<Object> vals) {
        return sha1(vals.toArray(new Object[vals.size()]));
    }

    /* FILE DELETION */

    /**
     * Deletes FILE if it exists and is not a directory.  Returns true
     * if FILE was deleted, and false otherwise.  Refuses to delete FILE
     * and throws IllegalArgumentException unless the directory designated by
     * FILE also contains a directory named .gitlet.
     */
    static boolean restrictedDelete(File file) {
        if (!(new File(file.getParentFile(), ".gitlet")).isDirectory()) {
            throw new IllegalArgumentException("not .gitlet working directory");
        }
        if (!file.isDirectory()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * Deletes the file named FILE if it exists and is not a directory.
     * Returns true if FILE was deleted, and false otherwise.  Refuses
     * to delete FILE and throws IllegalArgumentException unless the
     * directory designated by FILE also contains a directory named .gitlet.
     */
    static boolean restrictedDelete(String file) {
        return restrictedDelete(new File(file));
    }

    /* READING AND WRITING FILE CONTENTS */

    /**
     * Return the entire contents of FILE as a byte array.  FILE must
     * be a normal file.  Throws IllegalArgumentException
     * in case of problems.
     */
    static byte[] readContents(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /**
     * Return the entire contents of FILE as a String.  FILE must
     * be a normal file.  Throws IllegalArgumentException
     * in case of problems.
     */
    static String readContentsAsString(File file) {
        return new String(readContents(file), StandardCharsets.UTF_8);
    }

    /**
     * Write the result of concatenating the bytes in CONTENTS to FILE,
     * creating or overwriting it as needed.  Each object in CONTENTS may be
     * either a String or a byte array.  Throws IllegalArgumentException
     * in case of problems.
     */
    static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /**
     * Return an object of type T read from FILE, casting it to EXPECTEDCLASS.
     * Throws IllegalArgumentException in case of problems.
     */
    static <T extends Serializable> T readObject(File file,
                                                 Class<T> expectedClass) {
        try {
            ObjectInputStream in =
                    new ObjectInputStream(new FileInputStream(file));
            T result = expectedClass.cast(in.readObject());
            in.close();
            return result;
        } catch (IOException | ClassCastException
                | ClassNotFoundException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /**
     * Write OBJ to FILE.
     */
    static void writeObject(File file, Serializable obj) {
        writeContents(file, serialize(obj));
    }

    /* DIRECTORIES */

    /**
     * Filter out all but plain files.
     */
    private static final FilenameFilter PLAIN_FILES =
            new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return new File(dir, name).isFile();
                }
            };

    /**
     * Filter out all but dir.
     */
    private static final FilenameFilter DIR =
            new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return new File(dir, name).isDirectory();
                }
            };

    /**
     * Returns a list of the names of all plain files in the directory DIR, in
     * lexicographic order as Java Strings.  Returns null if DIR does
     * not denote a directory.
     */
    static List<String> plainFilenamesIn(File dir) {
        String[] files = dir.list(PLAIN_FILES);
        if (files == null) {
            return null;
        } else {
            Arrays.sort(files);
            return Arrays.asList(files);
        }
    }

    /**
     * Returns a list of the names of all dirs in the directory DIR, in
     * lexicographic order as Java Strings.  Returns null if DIR does
     * not denote a directory.
     */
    static List<String> DIRsIn(File dir) {
        String[] files = dir.list(DIR);
        if (files == null) {
            return null;
        } else {
            Arrays.sort(files);
            return Arrays.asList(files);
        }
    }


    /**
     * Returns a list of the names of all plain files in the directory DIR, in
     * lexicographic order as Java Strings.  Returns null if DIR does
     * not denote a directory.
     */
    static List<String> plainFilenamesIn(String dir) {
        return plainFilenamesIn(new File(dir));
    }

    /**
     * Returns a list of the names of all dirs in the directory DIR, in
     * lexicographic order as Java Strings.  Returns null if DIR does
     * not denote a directory.
     */
    static List<String> DIRsIn(String dir) {
        return DIRsIn(new File(dir));
    }

    /* OTHER FILE UTILITIES */

    /**
     * Return the concatentation of FIRST and OTHERS into a File designator,
     * analogous to the {@link java.nio.file.Paths.#get(String, String[])}
     * method.
     */
    static File join(String first, String... others) {
        return Paths.get(first, others).toFile();
    }

    /**
     * Return the concatentation of FIRST and OTHERS into a File designator,
     * analogous to the {@link java.nio.file.Paths.#get(String, String[])}
     * method.
     */
    static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }


    /* SERIALIZATION UTILITIES */

    /**
     * Returns a byte array containing the serialized contents of OBJ.
     */
    static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(obj);
            objectStream.close();
            return stream.toByteArray();
        } catch (IOException excp) {
            throw error("Internal error serializing commit.");
        }
    }



    /* MESSAGES AND ERROR REPORTING */

    /**
     * Return a GitletException whose message is composed from MSG and ARGS as
     * for the String.format method.
     */
    static GitletException error(String msg, Object... args) {
        return new GitletException(String.format(msg, args));
    }

    /**
     * Print a message composed from MSG and ARGS as for the String.format
     * method, followed by a newline.
     */
    static void message(String msg, Object... args) {
        System.out.printf(msg, args);
        System.out.println();
    }

    /** personal extra helper method for persistence.*/

    /**
     * write a commit object to the file in the OBJECTS_DIR.
     * if the commit already exist, rewrite it,
     * else make a new file and save it.
     */
    static void writeCommitObject(Commit toWriteCommit) throws IOException {
        String toWriteCommitID = toWriteCommit.getCommitID();
        File COMMIT_DIR = join(Repository.COMMITS_DIR, toWriteCommitID.substring(0, 2));
        File toWriteCommitFile = join(COMMIT_DIR, toWriteCommitID.substring(2));
        if (!COMMIT_DIR.exists()) {
            COMMIT_DIR.mkdir();
        }
        if (!toWriteCommitFile.exists()) {
            toWriteCommitFile.createNewFile();
        }
        writeObject(toWriteCommitFile, toWriteCommit);
    }

    /** need to deal with the abbreviation problem:
     * in the (likely) event that no other object exists with a SHA-1
     * identifier that starts with the same six digits. You should arrange
     * for the same thing to happen for commit ids that contain fewer than
     * 40 characters.*/
    static Commit readCommitObjectMayAbb(String toReadCommitID) {
        if (toReadCommitID == null) {
            return null;
        }
        File COMMIT_DIR = join(Repository.COMMITS_DIR, toReadCommitID.substring(0, 2));
        String toReadCommitFileNameAbb = toReadCommitID.substring(2);
        List<String> fileNames = plainFilenamesIn(COMMIT_DIR);
        File toReadCommitFile = null;
        int findCommitNum = 0;
        for (String fileName: fileNames) {
            if (fileName.contains(toReadCommitFileNameAbb)) {
                findCommitNum ++;
                toReadCommitFile = join(COMMIT_DIR, fileName);
            }
        }
        /** handle the failure case. */
        if (findCommitNum == 0) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        } else if (findCommitNum >= 2) {
            throw new GitletException("Multi commit with that abbreviate id.");
        }
        return readObject(toReadCommitFile, Commit.class);
    }

    static Commit readCommitObject(String toReadCommitID) {
        if (toReadCommitID == null) {
            return null;
        }
        File COMMIT_DIR = join(Repository.COMMITS_DIR, toReadCommitID.substring(0, 2));
        File toReadCommitFile = join(COMMIT_DIR, toReadCommitID.substring(2));
        if (!toReadCommitFile.exists()) {
            throw new GitletException("No commit with that id exists.");
        }
        return readObject(toReadCommitFile, Commit.class);
    }

    static void writeBranch(String branchName, String commitID) throws IOException {
        File branchFile = join(Repository.BRANCHES_DIR, branchName);
        if (!branchFile.exists()) {
            branchFile.createNewFile();
        }
        writeContents(branchFile, commitID);
    }

    static String readBranch(String branchName, String commitID) {
        File branchFile = join(Repository.BRANCHES_DIR, branchName);
        if (branchFile.exists()) {
            return readContentsAsString(branchFile);
        }
        return null;
    }

    static void writeHEAD(String branchName) throws IOException {
        File HEADFile = join(Repository.GITLET_DIR, "HEAD");
        if (!HEADFile.exists()) {
            HEADFile.createNewFile();
        }
        File branchFile = join(Repository.BRANCHES_DIR, branchName);
        String branchRef = branchFile.toString();
        writeContents(HEADFile, branchRef);
    }

    static Commit readHEADCommit() {
        File HEADFile = join(Repository.GITLET_DIR, "HEAD");
        if (!HEADFile.exists()) {
            throw new GitletException("The HEAD file doesn't exist");
        }
        File branchFile = new File(readContentsAsString(HEADFile));
        String headCommitID = readContentsAsString(branchFile);
        File HEAD_COMMIT_DIR = join(Repository.COMMITS_DIR, headCommitID.substring(0,2));
        File headCommitFile = join(HEAD_COMMIT_DIR, headCommitID.substring(2));
        return readObject(headCommitFile, Commit.class);
    }

    static File readHEADBranch() {
        File HEADFile = join(Repository.GITLET_DIR, "HEAD");
        if (!HEADFile.exists()) {
            throw new GitletException("The HEAD file doesn't exist");
        }
        File branchFile = new File(readContentsAsString(HEADFile));
        return branchFile;
    }

    static void writeFileContent(String fileName, Object... contents) {
        File thisFile = join(Repository.CWD, fileName);
        writeContents(thisFile, contents);
    }

    static String readCWDFileContentByName(String fileName) {
        File thisFile = join(Repository.CWD, fileName);
        return readContentsAsString(thisFile);
    }

    static void writeStagingArea(File indexFile, StagingArea stagingArea) {
        writeObject(indexFile, stagingArea);
    }

    static StagingArea readStagingArea(File indexFile) {
        return readObject(indexFile, StagingArea.class);
    }

    static boolean blobExist(String blobID) {
        File blob = join(Repository.BLOBS_DIR, blobID.substring(0, 2), blobID.substring(2));
        if (blob.exists()) {
            return true;
        }
        return false;
    }

    static String getBlobID(String blobContent) {
        return sha1(blobContent);
    }

    static void writeBlob(String blobContent) throws IOException {
        String toWriteBlobID = sha1(blobContent);
        File BLOB_DIR = join(Repository.BLOBS_DIR, toWriteBlobID.substring(0, 2));
        File toWriteBlobFile = join(BLOB_DIR, toWriteBlobID.substring(2));
        if (!BLOB_DIR.exists()) {
            BLOB_DIR.mkdir();
        }
        if (!toWriteBlobFile.exists()) {
            toWriteBlobFile.createNewFile();
        }
        writeContents(toWriteBlobFile, blobContent);
    }

    static void writeBlob(String blobContent, String blobID) throws IOException {
        String toWriteBlobID = blobID;
        File BLOB_DIR = join(Repository.BLOBS_DIR, toWriteBlobID.substring(0, 2));
        File toWriteBlobFile = join(BLOB_DIR, toWriteBlobID.substring(2));
        if (!BLOB_DIR.exists()) {
            BLOB_DIR.mkdir();
        }
        if (!toWriteBlobFile.exists()) {
            toWriteBlobFile.createNewFile();
        }
        writeContents(toWriteBlobFile, blobContent);
    }

    static File readBlobFile(String toReadBlobID) {
        File BLOB_DIR = join(Repository.BLOBS_DIR, toReadBlobID.substring(0, 2));
        File toReadBlobFile = join(BLOB_DIR, toReadBlobID.substring(2));
        if (!toReadBlobFile.exists()) {
            throw new GitletException("The Blob doesn't exit");
        }
        return new File(readContentsAsString(toReadBlobFile));
    }

    static String readBlobContent(File toReadBlobFile) {
        return readContentsAsString(toReadBlobFile);
    }

    static boolean isAddFileExists(String fileName) {
        File thisFile = join(Repository.CWD, fileName);
        if (thisFile.exists()) {
            return true;
        }
        return false;
    }

    static String readObjectPathFromID(String ID, File DIR) {
        File OBJECT_DIR = join(DIR, ID.substring(0, 2));
        File toReadOBJECTFile = join(OBJECT_DIR, ID.substring(2));
        return toReadOBJECTFile.toString();
    }

    static File readObjectFileFromID(String ID, File DIR) {
        File OBJECT_DIR = join(DIR, ID.substring(0, 2));
        File toReadOBJECTFile = join(OBJECT_DIR, ID.substring(2));
        return toReadOBJECTFile;
    }
}
