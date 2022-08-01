package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Random;

import static byow.Core.DrawUtils.*;

public class RogWorld {
    /** the first random generating room's ldPos.*/
    public static final Position START_RANGE_LDPOS = new Position(0,0);
    public static final Position START_RANGE_RUPOS = new Position(25, 10);
    public static final Position END_RUPOS = new Position(Engine.WIDTH - 1, Engine.HEIGHT - 1);
    public static final Room START_RANGE = new Room(START_RANGE_LDPOS, START_RANGE_RUPOS);
    /** Max room edge length.*/
    public static final int MAX_EDGE_LENGTH = 10;
    /** the whole Room of RogWord to render.*/
    public static final Room WORLD_ROOM = new Room(START_RANGE_LDPOS, END_RUPOS);

    private long seed;
    private Random random;
    private TETile[][] rogTiles;
    private Position startPos;
    private HashSet<Room> rooms;
    private PriorityQueue<Room> unSpreadRooms;

    public RogWorld(String input) {
        this.seed = findTheSeed(input);
        System.out.println(seed);
        this.random = new Random(this.seed);
        this.rogTiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        this.fillWorldNull();
        this.startPos = getRandomPos(this.random, START_RANGE);
        this.rooms = new HashSet<>();
        this.unSpreadRooms = new PriorityQueue<>();
    }

    /** Return the seed (positive long) in the input,
     * if there's no valid seed, return -1.*/
    public long findTheSeed(String input) {
        input = input.toLowerCase(Locale.ENGLISH);
        int startIndex = input.indexOf("n");
        int endIndex = input.indexOf("s");
        long seed = Long.parseLong(input.substring(startIndex + 1, endIndex));
        if (startIndex > -1 && endIndex > startIndex) {
            return seed;
        }
        return -1;
    }

    /** fill the whole world with Tileset.NOTHING.*/
    private void fillWorldNull() {
        int width = this.rogTiles.length;
        int height = this.rogTiles[0].length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                this.rogTiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** check a Pos is in the range. */
    public boolean checkPosIn(Position p, Room range) {
        if (p.getX() < range.getLdPos().getX() || p.getX() > range.getRuPos().getX()) {
            return false;
        }
        if (p.getY() < range.getLdPos().getY() || p.getY() > range.getRuPos().getY()) {
            return false;
        }
        return true;
    }

    /** check a Pos is in the world. */
    public boolean checkPosIn(Position p) {
        return checkPosIn(p, WORLD_ROOM);
    }

    /** check a Room r is in the range. */
    public boolean checkARoomIn(Room r, Room range) {
        if (checkPosIn(r.getLdPos(), range) && checkPosIn(r.getRuPos(), range)) {
            return true;
        }
        return false;
    }

    /** check a Room r is in the world. */
    public boolean checkARoomIn(Room r) {
        return checkARoomIn(r, WORLD_ROOM);
    }

    /** check a Room r isn't intersect with existing rooms.*/
    public boolean checkARoomNoIntersection(Room r) {
        for (Room temp : this.rooms) {
            if (checkPosIn(temp.getLdPos(), r) || checkPosIn(temp.getRuPos(), r)) {
                return false;
            }
        }
        return true;
    }

    /** check a given room is OK to generating. */
    public boolean checkRoomValidation(Room r) {
        if (checkARoomIn(r) && checkARoomNoIntersection(r)) {
            return true;
        }
        return false;
    }

    /** Create a random room in a specific range, if the room is ok to create, then
     * create it and return ture. Otherwise, return false.*/
    public boolean createARandomRoom(Room range) {
        Room r = getRandomRoom(this.random, range);
        if (checkRoomValidation(r)) {
            drawARoom(this.rogTiles, r);
            this.rooms.add(r);
            this.unSpreadRooms.add(r);
            return true;
        }
        return false;
    }
    /** Create a random room in a specific Pos, if the room is ok to create, then
     * create it and return ture. Otherwise, return false.*/
    private boolean createARandomRoom(Position ldPos) {
        return createARandomRoom(new Room(ldPos, ldPos));
    }

    /** own main method for test.*/
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        RogWorld rw = new RogWorld("n4545125785664S");
        Position startPos = getRandomPos(rw.random, START_RANGE);
        Room startRoom = getRandomRoom(rw.random, startPos);
        DrawUtils.drawARoom(rw.rogTiles, startRoom);
        ter.renderFrame(rw.rogTiles);
    }
}
