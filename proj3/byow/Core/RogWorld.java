package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Edge;

import java.util.*;

import static byow.Core.DrawUtils.*;
import static byow.Core.RandomUtils.*;

public class RogWorld {
    /** the first random generating room's ldPos.*/
    public static final Position START_RANGE_LDPOS = new Position(0,0);
    //public static final Position START_RANGE_RUPOS = new Position(25, 10);
    public static final Position END_RUPOS = new Position(Engine.WIDTH - 1, Engine.HEIGHT - 1);
    // public static final Room START_RANGE = new Room(START_RANGE_LDPOS, START_RANGE_RUPOS);
    /** Max room edge length.*/
    public static final int MAX_EDGE_LENGTH = 8;
    /** Min room edge length.*/
    public static final int MIN_EDGE_LENGTH = 3;
    /** the whole Room of RogWord to render.*/
    public static final Room WORLD_ROOM = new Room(START_RANGE_LDPOS, END_RUPOS);
    /** probability to create an adjacent room on the edge.*/
    public static final double ADJ_ROOM_P = 0.50;
    /** MAX_TRY_TIMES to generate a room.*/
    public static final int MAX_TRY_TIMES = 5;

    private long seed;
    private Random random;
    private TETile[][] rogTiles;
    private HashSet<Room> rooms;
    private List<Room> unSpreadRooms;

    public RogWorld(String input) {
        this.seed = findTheSeed(input);
        System.out.println("DEBUG: " + seed);
        this.random = new Random(this.seed);
        this.rogTiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        this.fillWorldNull();
        this.rooms = new HashSet<>();
        this.unSpreadRooms = new ArrayList<>();
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
    public boolean createARandomRoomInRange(Room range) {
        Room r = getRandomRoom(this.random, range);
        if (checkRoomValidation(r)) {
            drawARoom(this.rogTiles, r);
            System.out.println("DEBUG: StartRoom" + r.getLdPos().getX() + "," + r.getLdPos().getY());
            this.rooms.add(r);
            this.unSpreadRooms.add(r);
            return true;
        }
        return false;
    }

    /** Create a random room in ldPos, if the room is ok to create, then
     * create it and return ture. Otherwise, return false.*/
    public boolean createARandomRoomInRange(Position ldPos) {
        return createARandomRoomInRange(new Room(ldPos, ldPos));
    }

    /** Create the start room.*/
    private boolean createStartRoom() {
        boolean suc = createARandomRoomInRange(WORLD_ROOM);
        if (suc) {
            return true;
        } else  {
            return createStartRoom();
        }
    }

    /** Create a random room in a startPos on the specific edge e,
     * if the room is ok to create, then
     * create it and return ture. Otherwise, return false.*/
    private boolean createARandomRoom(Position startPos, Room.Edge e) {
        Room r = getRandomRoom(this.random, startPos, e);
        if (r != null) {
            if (checkRoomValidation(r)) {
                drawARoom(this.rogTiles, r);
                this.rooms.add(r);
                this.unSpreadRooms.add(r);
                return true;
            }
        }
        return false;
    }

    /** Try specific times to create a random room in a startPos on the specific edge e,
     * if the room is ok to create, then
     * create it and return ture. Otherwise, return false.*/
    private boolean createARandomRoomNTimes(Position startPos, Room.Edge e, int times) {
        while (times > 0) {
            boolean isSuc = createARandomRoom(startPos, e);
            System.out.println("DEBUG: ++" + startPos + " " + e.edgeIndex);
            times = times - 1;
            if (isSuc) {
                return true;
            }
        }
        return false;
    }


    /** For each edge, ADJ_ROOM_P's probability to pick a random Pos, Try MAX_TRY_TIME times
     * to generate a random room until success.*/
    private void spreadARoom(Room r) {
        for (Room.Edge e: r.getEdges()) {
           boolean toTrySpread = bernoulli(this.random, ADJ_ROOM_P);
           if (toTrySpread) {
               Position openPos = getRandomPos(this.random, e);
               System.out.println("DEBUG: " + "openPos" + openPos.getX() + " " + openPos.getY());
               Position randomRoomEdgePos = e.shiftPosOnTheEdge(openPos);
               System.out.println("DEBUG: " + randomRoomEdgePos.getX() + " " + randomRoomEdgePos.getY());
               if (randomRoomEdgePos != null) {
                    createARandomRoomNTimes(randomRoomEdgePos, e, MAX_TRY_TIMES);
               }
           }
        }
    }

    public void spreadARoom() {
        if (!this.unSpreadRooms.isEmpty()) {
            Room r = this.unSpreadRooms.remove(0);
            System.out.print("DEBUG: ");
            System.out.print(r);
            spreadARoom(r);
        }
    }

    /** own main method for test.*/
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        RogWorld rw = new RogWorld("n124564s");
        rw.createStartRoom();
        rw.spreadARoom();
        ter.renderFrame(rw.rogTiles);
    }
}
