package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Edge;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static byow.Core.DrawUtils.*;
import static byow.Core.RandomUtils.*;
import static byow.Core.Utils.*;

public class RogWorld implements Serializable {
    /** the world's init ldPos.*/
    public static final Position START_RANGE_LDPOS = new Position(0,0);
    //public static final Position START_RANGE_RUPOS = new Position(25, 10);
    public static final Position END_RUPOS = new Position(Engine.WIDTH - 1, Engine.HEIGHT - 1);
    // public static final Room START_RANGE = new Room(START_RANGE_LDPOS, START_RANGE_RUPOS);
    /** Random room edge's max length.*/
    public static final int MAX_EDGE_LENGTH = 8;
    /** Random room edge's min length.*/
    public static final int MIN_EDGE_LENGTH = 3;
    /** the whole Room of RogWord to render.*/
    public static final Room WORLD_ROOM = new Room(START_RANGE_LDPOS, END_RUPOS);
    /** the shrink distance of RogWord to generate the room (minimum distance to the edge).*/
    public static final int SHRINK_DIS = 3;
    /** the whole Room of RogWord to generate rooms.*/
    public static final Room WORLD_SHRINK_ROOM = new Room(START_RANGE_LDPOS.shiftPosition(SHRINK_DIS,SHRINK_DIS), END_RUPOS.shiftPosition(-SHRINK_DIS, -SHRINK_DIS));
    /** probability to create an adjacent room on the edge.*/
    public static final double ADJ_ROOM_P = 0.50;
    /** MAX_TRY_TIMES to generate a room.*/
    public static final int MAX_TRY_TIMES = 5;
    /** MAX_AREA_RATE = Rooms Area / WORLD_ROOM Area, to stop spreading rooms.*/
    public static final double MAX_AREA_RATE = 0.5;

    /** the random seed to generate pseudo randomness.*/
    private long seed;
    /** the random object to generate pseudo random number.*/
    private Random random;
    /** the generated world.*/
    private TETile[][] rogTiles;
    /** the already generated rooms to the world */
    private ArrayList<Room> rooms;
    /** the list of rooms waiting to generate adjacent rooms. */
    private ArrayList<Room> unSpreadRooms;
    /** the total areas of the generated rooms.*/
    private int totalRoomsArea;
    /** the Player Avatar.*/
    private Avatar player;

    public RogWorld(String input) {
        this.seed = findTheSeed(input);
        //System.out.println("DEBUG: " + seed);
        this.random = new Random(this.seed);
        this.rogTiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        this.fillWorldNull();
        this.rooms = new ArrayList<>();
        this.unSpreadRooms = new ArrayList<>();
        this.totalRoomsArea = 0;
        this.player = null;
    }

    public RogWorld() {
    }

    public RogWorld(long seed) {
        this.seed = seed;
        //System.out.println("DEBUG: " + seed);
        this.random = new Random(this.seed);
        this.rogTiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        this.fillWorldNull();
        this.rooms = new ArrayList<>();
        this.unSpreadRooms = new ArrayList<>();
        this.totalRoomsArea = 0;
        this.player = null;
    }

    public void createPlayer(Position pos) {
        this.player = new Avatar(pos);
    }

    /** get a random room from the RogWorld.*/
    private Room getARandomRoom() {
        int roomIndex = uniform(this.random, rooms.toArray().length);
        return this.rooms.get(roomIndex);
    }

    public void createPlayer() {
        Room avatarRoom = getARandomRoom();
        Position playerPos = getRandomPos(this.random, avatarRoom.shrinkRoom(1));
        createPlayer(playerPos);
    }

    /** move player one step, if hit the wall, don't move.*/
    public void movePlayer(char operation) {
        Position playerTargetPos = this.player.getMoveToPos(operation);
        if (posIsFloor(playerTargetPos)) {
            this.player.move(operation);
        }
    }

    private TETile getTileInPos(Position pos) {
        int tileX = pos.getX();
        int tileY = pos.getY();
        return this.rogTiles[tileX][tileY];
    }

    private boolean posIsFloor(Position pos) {
        if (getTileInPos(pos).character() == '·') {
            return true;
        }
        return false;
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

    /** check a Room r is in the shrink world. */
    public boolean checkARoomIn(Room r) {
        return checkARoomIn(r, WORLD_SHRINK_ROOM);
    }

    private boolean checkRoomPosesIn(Room r, Room range) {
        boolean checkLPos = checkPosIn(r.getLdPos(), range) || checkPosIn(r.getLuPos(), range);
        boolean checkRPos = checkPosIn(r.getRdPos(), range) || checkPosIn(r.getRuPos(), range);
        return checkLPos || checkRPos;
    }

    /** Check if two rooms intersecting, but with no vertexes in both area .*/
    private boolean checkTwoRoomCrossIntersection(Room r, Room range) {
        boolean firstCheckLdX = r.getLdPos().getX() > range.getLdPos().getX();
        boolean firstCheckRuX = r.getRuPos().getX() < range.getRuPos().getX();
        boolean firstCheckLdY = r.getLdPos().getY() > range.getLdPos().getY();
        boolean firstCheckRuY = r.getRuPos().getY() < range.getRuPos().getY();
        if (firstCheckLdX && firstCheckRuX) {
            boolean secondCheckLdY = r.getLdPos().getY() < range.getLdPos().getY();
            boolean secondCheckRuY = r.getRuPos().getY() > range.getRuPos().getY();
            if (secondCheckLdY && secondCheckRuY) {
                return true;
            }
        } else if (firstCheckLdY && firstCheckRuY) {
            boolean secondCheckLdX = r.getLdPos().getX() < range.getLdPos().getX();
            boolean secondCheckRuX = r.getRuPos().getX() > range.getRuPos().getX();
            if (secondCheckLdX && secondCheckRuX) {
                return true;
            }
        }
        return false;
    }
    /** check a Room r isn't intersect with existing rooms.*/
    public boolean checkARoomNoIntersection(Room r) {
        for (Room temp : this.rooms) {
            if (checkRoomPosesIn(temp, r) || checkRoomPosesIn(r, temp)) {
                return false;
            }
            /** Check if two rooms intersecting, but with no vertexes in both area.*/
            else if (checkTwoRoomCrossIntersection(r, temp)) {
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
            this.rooms.add(r);
            this.totalRoomsArea += r.getRoomArea();
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
                this.totalRoomsArea += r.getRoomArea();
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
            times = times - 1;
            if (isSuc) {
                return true;
            }
        }
        return false;
    }

    private void openATile(Position p){
        this.rogTiles[p.getX()][p.getY()] = FLOOR;
    }


    /** For each edge, ADJ_ROOM_P's probability to pick a random Pos, Try MAX_TRY_TIME times
     * to generate a random room until success.*/
    private void spreadARoom(Room r) {
        for (Room.Edge e: r.getEdges()) {
           boolean toTrySpread = bernoulli(this.random, ADJ_ROOM_P);
           if (toTrySpread) {
               Position openPos = getRandomPos(this.random, e);
               Position randomRoomEdgePos = e.shiftPosOnTheEdge(openPos);
               if (randomRoomEdgePos != null) {
                    boolean isNeedOpen = createARandomRoomNTimes(randomRoomEdgePos, e, MAX_TRY_TIMES);
                    if (isNeedOpen) {
                        openATile(openPos);
                        openATile(randomRoomEdgePos);
                    }
               }
           }
        }
    }

    public void spreadARoom() {
        if (!this.unSpreadRooms.isEmpty()) {
            Room r = this.unSpreadRooms.remove(0);
            spreadARoom(r);
        }
    }

    /** Try to spread all exist rooms one round, if it's done, return true.*/
    public boolean spreadExistRoomsOneRound() {
        while (!this.unSpreadRooms.isEmpty()) {
            this.spreadARoom();
        }
        if (this.unSpreadRooms.isEmpty()) {
            return true;
        }
        return false;
    }

    /** remark all the spread rooms, and ready to start another round.*/
    private void remarkAllExistRooms() {
        this.unSpreadRooms.addAll(this.rooms);
    }

    public void generateRogWorld() {
        createStartRoom();
        while ((this.totalRoomsArea * 1.0 / WORLD_ROOM.getRoomArea()) < MAX_AREA_RATE) {
            boolean oneRoundFinish = this.spreadExistRoomsOneRound();
            if (oneRoundFinish) {
                remarkAllExistRooms();
            }
        }
        this.unSpreadRooms.clear();
        createPlayer();
    }

    public TETile[][] getRogTiles() {
        return this.rogTiles;
    }

    public Avatar getPlayer() {
        return this.player;
    }

    public void saveRogWorld() throws IOException {
        writeRogWorld(this);
    }

    /** own main method for test.*/
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        RogWorld rw = new RogWorld("n5233123s");
        rw.generateRogWorld();
        ter.renderFrame(rw.rogTiles, rw.player);
        System.out.println(rw.player.getCurrPos());
    }
}
