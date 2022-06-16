package gitlet;

import java.time.temporal.Temporal;
import java.util.TreeMap;

public class StagedForAdditionArea {
    /** The file's {name: blobID} mapping, which is Staged For Addition*/
    private TreeMap<String, String> stagedAdditionFiles;

    public StagedForAdditionArea() {
        this.stagedAdditionFiles = new TreeMap<>();
    }
}
