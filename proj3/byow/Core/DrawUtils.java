package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Random;

import static byow.Core.RandomUtils.*;

public class DrawUtils {
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

    /** Fill a Room's floor tiles.*/
    public static void fillARoom(TETile[][] world, Position ldPos, Position ruPos) {
        ldPos = ldPos.shiftPosition(1,1);
        ruPos = ruPos.shiftPosition(-1, -1);
        int xLen = ruPos.getX() - ldPos.getX() + 1;
        int yLen = ruPos.getY() - ldPos.getY() + 1;
        for (int dy = 0; dy < yLen; dy++) {
            drawARowFromLeft(world, new Position(ldPos.getX(), ldPos.getY() + dy), xLen, FLOOR);
        }
    }

    public static void fillARoom(TETile[][] world, Room room) {
        fillARoom(world, room.getLdPos(), room.getRuPos());
    }

    /** check the given pos is in the given room/range. */
    public static boolean checkPosValidation(Position pos, Room room) {
        boolean xValidation = pos.getX() <= room.getRuPos().getX() && pos.getX() >= room.getLdPos().getX();
        boolean yValidation = pos.getY() <= room.getRuPos().getY() && pos.getY() >= room.getLdPos().getY();
        if (xValidation && xValidation) {
            return true;
        }
        return false;
    }

    /** check the given pos is in the given room/range. */
    public static boolean checkPosValidation(Position pos) {
        return checkPosValidation(pos, RogWorld.WORLD_ROOM);
    }

    /** Draw a room with specific LeftDown pos and RightUp pos.*/
    private static void drawARoom(TETile[][] world, Position ldPos, Position ruPos) {
        int xLen = ruPos.getX() - ldPos.getX() + 1;
        int yLen = ruPos.getY() - ldPos.getY() + 1;
        drawARowFromLeft(world, ldPos, xLen, WALL);
        drawARowFromRight(world, ruPos, xLen, WALL);
        drawAColumnFromBottom(world, ldPos, yLen, WALL);
        drawAColumnFromTop(world, ruPos, yLen, WALL);
        fillARoom(world, ldPos, ruPos);
    }

    /** Draw a room with specific LeftDown pos and RightUp pos.*/
    public static void drawARoom(TETile[][] world, Room room) {
        drawARoom(world, room.getLdPos(), room.getRuPos());
        room.storeEdges();
    }


    /** Below are all random generating methods. */
    /** return a random length in [a, b]. */
    public static int getRandomLength(Random random, int a, int b) {
        return uniform(random, a, b + 1);
    }

    /** return a random Position in a rectangle area.*/
    public static Position getRandomPos(Random random, Position ldPos, Position ruPos) {
        int x = uniform(random, ldPos.getX(), ruPos.getX() + 1);
        int y = uniform(random, ldPos.getY(), ruPos.getY() + 1);
        return new Position(x, y);
    }

    public static Position getRandomPos(Random random, Room room) {
        return getRandomPos(random, room.getLdPos(), room.getRuPos());
    }

    public static Position getRandomPos(Random random, Room.Edge e) {
        Position ldPos = e.startPos;
        Position ruPos = e.endPos;
        if (e.startPos.getX() > e.endPos.getX() || e.startPos.getY() > e.endPos.getY()) {
            ldPos = e.endPos;
            ruPos = e.startPos;
        }
        return getRandomPos(random, ldPos, ruPos);
    }

    /** return a random Room in the specific ldPos with random xLen & yLen. */
    public static Room getRandomRoom(Random random, Position ldPos) {
        int xLen = getRandomLength(random, RogWorld.MIN_EDGE_LENGTH, RogWorld.MAX_EDGE_LENGTH);
        int yLen = getRandomLength(random, RogWorld.MIN_EDGE_LENGTH, RogWorld.MAX_EDGE_LENGTH);
        int ruPosX = ldPos.getX() + xLen - 1;
        int ruPosY = ldPos.getY() + yLen - 1;
        Position ruPos = new Position(ruPosX, ruPosY);
        return new Room(ldPos,ruPos);
    }
    public static Room getRandomRoom(Random random, Room range) {
        Position ldPos = getRandomPos(random, range);
        return getRandomRoom(random, ldPos);
    }

    public static Room getRandomRoom(Random random, Position startPos, Room.Edge e) {
        int xLen = getRandomLength(random, RogWorld.MIN_EDGE_LENGTH, RogWorld.MAX_EDGE_LENGTH);
        int yLen = getRandomLength(random, RogWorld.MIN_EDGE_LENGTH, RogWorld.MAX_EDGE_LENGTH);
        Room r = e.generateARoomOnEdge(random, startPos, xLen, yLen);
        return r;
    }
}
