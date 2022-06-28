package gitlet;

import java.util.Map;
import java.util.TreeMap;
import static gitlet.Utils.*;

public class BreadFirstPaths {
    /** <commitID, distance>
     * save the distance from ancestor commit to the branch commit.
     */
    private TreeMap<String, Integer> distTo;
    private String startCommitID;

    public BreadFirstPaths(String startCommitID) {
        this.distTo = new TreeMap<>();
        this.startCommitID = startCommitID;
        distTo.put(this.startCommitID, 0);
        bfs(this.startCommitID, 1);
    }

    private void bfs(String startCommitID, int currDist) {
        Commit startCommit = readCommitObject(startCommitID);
        /** call bfs to the first parent commit.*/
        String firstParentCommitID = startCommit.getParentID();
        if (firstParentCommitID != null) {
            this.distTo.put(firstParentCommitID, currDist);
            bfs(firstParentCommitID, currDist + 1);
        }
        /** call bfs to the second parent commit.*/
        String secondParentCommitID = startCommit.getSecondParentID();
        if (secondParentCommitID != null) {
            this.distTo.put(secondParentCommitID, currDist);
            bfs(secondParentCommitID, currDist + 1);
        }
    }

    public TreeMap<String, Integer> getDistTo() {
        return distTo;
    }

    public String getStartCommitID() {
        return startCommitID;
    }

    /** give two BreadFirstPaths, return the sub map of two TreeMaps.*/
    public static String getSplitPointID(BreadFirstPaths currBranchPaths, BreadFirstPaths givenBranchPaths) {
        TreeMap<String, Integer> currBranchDistTo = currBranchPaths.getDistTo();
        TreeMap<String, Integer> commonAncestors = new TreeMap<>();
        String splitPointID = null;
        for (Map.Entry<String, Integer> entry: givenBranchPaths.getDistTo().entrySet()) {
            if (currBranchDistTo.containsKey(entry.getKey())) {
                splitPointID = entry.getKey();
            }
        }
        return splitPointID;
    }

    }

