package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;
import static gitlet.Utils.*;

public class StagingArea implements Serializable, Dumpable {
    /** The file's {name: blobID} mapping, which is Staged For Addition*/
    private TreeMap<String, String> stagedFiles;
    private File stagedPath;

    public StagingArea(File stagedPath) {
        this.stagedFiles = new TreeMap<>();
        this.stagedPath = stagedPath;
    }

    public TreeMap<String, String> getStagedFiles() {
        return stagedFiles;
    }

    @Override
    public void dump() {
        this.getStagedFiles().clear();;
        writeStagingArea(this.stagedPath, this);
    }
}
