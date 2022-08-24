package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;

public class Avatar implements Serializable {
    private Position currPos;
    private TETile avatarTile;
    private ArrayList<Character> movements;

    public Avatar(Position pos) {
        this.currPos = pos;
        this.avatarTile = Tileset.AVATAR;
        this.movements = new ArrayList<>();
    }

    public Position getCurrPos() {
        return currPos;
    }

    public TETile getAvatarTile() {
        return avatarTile;
    }

    public Position getMoveToPos(char operation) {
        switch (operation) {
            case 'w':
                return this.currPos.shiftPosition(0, 1);
            case 'a':
                return this.currPos.shiftPosition(-1, 0);
            case 's':
                return this.currPos.shiftPosition(0, -1);
            case 'd':
                return this.currPos.shiftPosition(1, 0);
        }
        return null;
    }

    public void move(char operation) {
        switch (operation) {
            case 'w':
                this.currPos = this.currPos.shiftPosition(0, 1);
                break;
            case 'a':
                this.currPos = this.currPos.shiftPosition(-1, 0);
                break;
            case 's':
                this.currPos = this.currPos.shiftPosition(0, -1);
                break;
            case 'd':
                this.currPos = this.currPos.shiftPosition(1, 0);
                break;
        }
        this.movements.add(operation);
    }
}
