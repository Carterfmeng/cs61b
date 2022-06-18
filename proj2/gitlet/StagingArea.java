package gitlet;

import java.io.Serializable;
import java.time.temporal.Temporal;
import java.util.TreeMap;

public class StagingArea implements Serializable {
    /** The file's {name: blobID} mapping, which is Staged For Addition*/
    private TreeMap<String, String> stagedFiles;

    public StagingArea() {
        this.stagedFiles = new TreeMap<>();
    }

    public TreeMap<String, String> getStagedFiles() {
        return stagedFiles;
    }
}
