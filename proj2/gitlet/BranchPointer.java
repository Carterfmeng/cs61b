package gitlet;

public class BranchPointer {
    /** The name of the Branch.*/
    private String name;
    /** The ID of the last commit in this branch. */
    private String pointedID;

    public BranchPointer(String name, String pointedID) {
        this.name = name;
        this.pointedID = pointedID;
    }

    public String getName() {
        return name;
    }

    public String getPointedID() {
        return pointedID;
    }
}
