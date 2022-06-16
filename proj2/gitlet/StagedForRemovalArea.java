package gitlet;

import java.util.TreeMap;

public class StagedForRemovalArea {
    /** The files' {name: blobID} mapping, which is Staged For Removal.*/
    private TreeMap<String, String> stagedRemovalFiles;

    public StagedForRemovalArea() {
        this.stagedRemovalFiles = new TreeMap<>();
    }

}
