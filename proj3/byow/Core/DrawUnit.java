package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class DrawUnit {
    public static final TETile WALL = Tileset.WALL;
    public static final TETile FLOOR = Tileset.FLOOR;
    public static final TETile NOTHING = Tileset.NOTHING;

    /** Draw a row from left start point with specific tile.*/
    public static void drawARowFromLeft(TETile[][] world, Position pos, int len, TETile t) {
        for (int dx = 0; dx < len; dx++) {
            world[pos.getX() + dx][pos.getY()] = t;
        }
    }
    /** Draw a row from right start point with specific tile.*/
    public static void drawARowFromRight(TETile[][] world, Position pos, int len, TETile t) {
        for (int dx = 0; dx < len; dx++) {
            world[pos.getX() - dx][pos.getY()] = t;
        }
    }
    /** Draw a column from bottom start point with specific tile.*/
    public static void drawAColumnFromBottom(TETile[][] world, Position pos, int len, TETile t) {
        for (int dy = 0; dy < len; dy++) {
            world[pos.getX()][pos.getY() + dy] = t;
        }
    }
    /** Draw a column from top start point with specific tile.*/
    public static void drawAColumnFromTop(TETile[][] world, Position pos, int len, TETile t) {
        for (int dy = 0; dy < len; dy++) {
            world[pos.getX()][pos.getY() - dy] = t;
        }
    }

    public static void fillARoom(TETile[][] world, Position ldPos, Position ruPos) {
        ldPos = ldPos.shiftPosition(1,1);
        ruPos = ruPos.shiftPosition(-1, -1);
        int xLen = ruPos.getX() - ldPos.getX() + 1;
        int yLen = ruPos.getY() - ldPos.getY() + 1;
        for (int dy = 0; dy < yLen; dy++) {
            drawARowFromLeft(world, new Position(ldPos.getX(), ldPos.getY() + dy), xLen, FLOOR);
        }
    }

    /** Draw a room with specific LeftDown pos and RightUp pos.*/
    public static void drawARoom(TETile[][] world, Position ldPos, Position ruPos) {
        int xLen = ruPos.getX() - ldPos.getX() + 1;
        int yLen = ruPos.getY() - ldPos.getY() + 1;
        drawARowFromLeft(world, ldPos, xLen, WALL);
        drawARowFromRight(world, ruPos, xLen, WALL);
        drawAColumnFromBottom(world, ldPos, yLen, WALL);
        drawAColumnFromTop(world, ruPos, yLen, WALL);
        fillARoom(world, ldPos, ruPos);
    }
}
