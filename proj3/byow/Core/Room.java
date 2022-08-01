package byow.Core;


public class Room {
    private Position ldPos;
    private Position ruPos;

    public Room(Position ldPos, Position ruPos) {
        this.ldPos = ldPos;
        this.ruPos = ruPos;
    }

    public Position getLdPos() {
        return this.ldPos;
    }

    public Position getRuPos() {
        return this.ruPos;
    }

}
