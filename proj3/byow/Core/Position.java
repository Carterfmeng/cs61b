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

    public Position shiftPosition(int dx, int dy) {
        return new Position(this.x + dx, this.y + dy);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
