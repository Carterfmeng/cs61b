package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;

public class Blob implements Dumpable{
    /** Represents a blob object, which is the middle state of a file.
     * The same file may have different blobs, or called different versions*/
    private String fileName;
    private String blobContent;

    public Blob(String fileName, String blobContent) {
        this.fileName = fileName;
        this.blobContent = blobContent;
    }

    public String getBlobID(){ return sha1(serialize(this));
    }
    public String getFileName() {
        return this.fileName;
    }

    public String getBlobContent() {
        return this.blobContent;
    }

    /** if a blobExists, it must be the same, because the content is the same with
     * the same sha1-hash.*/
    public static boolean blobExists(String blobID) {
        File blob = join(Repository.OBJECTS_DIR, blobID.substring(0,2), blobID.substring(2));
        if (blob.exists()) {
            return true;
        }
        return false;
    }

    public void save() throws IOException {
        File blobDIR = join (Repository.OBJECTS_DIR, getBlobID().substring(0, 2));
        File blob = join(blobDIR, getBlobID().substring(2));
        if (!blobDIR.exists()) {
            blobDIR.mkdir();
        }
        blob.createNewFile();
        writeObject(blob, this);
    }

    @Override
    public void dump() {

    }
}
