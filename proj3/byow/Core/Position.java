package byow.Core;

import java.io.Serializable;

public class Position implements Serializable {
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

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Position.class) {
            return false;
        }
        if (this.x == ((Position) obj).getX() && this.y == ((Position) obj).getY()) {
            return true;
        }
        return false;
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
