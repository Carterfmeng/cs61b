package byow.Core;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    /** shift the position with dx, dy offsets.*/
    public Position shiftPosition(int dx, int dy) {
        return new Position(this.x + dx, this.y + dy);
    }

    /** return the x index.*/
    public int getX() {
        return this.x;
    }
    /** return the y index.*/
    public int getY() {
        return this.y;
    }
}
