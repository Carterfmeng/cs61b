# Build Your Own World(BYOW) Design Document

**Name**: Carter

****

## Classes and Data Structures

****

### Class Main

The main entry point for the program. This class simply parses the command line inputs, and lets the byow.Core.Engine class take over in either keyboard or input string mode.

****

### Class Engine

This is where handle the two input mode, and do the render stuff.

#### Fields

1. `public static final int WIDTH` The render WIDTH, corresponding to x coordinate.
2. `public static final int HEIGHT` The render HEIGHT, corresponding to y coordinate.
3. `TERenderer ter` The render of Engine.
4. `TETile[][] world` The world to rendering for Engine.

---

### Class RandomUtils

A library of static methods to generate pseudo-random numbers from different distributions (bernoulli, uniform, gaussian, discrete, and exponential). Also includes methods for shuffling an array and other randomness related stuff you might want to do.

> Adapted from [StdRandom.java](https://introcs.cs.princeton.edu/java/22library/StdRandom.java.html)

---

### Class RogWorld

The Random generated world, contains the main logic of our random word generation, where process the input string and generate the tile-based world.

#### Fields

##### static final fields:

1. `public static final Position`  the world's init ldPos.

2. `public static final Position END_RUPOS` the the world's ruPos.

3. `public static final int MAX_EDGE_LENGTH` Random room edge's max length.

4. `public static final int MIN_EDGE_LENGTH` Random room edge's min length.

5. `public static final Room WORLD_ROOM` the whole Room of RogWord to render.

6. `public static final int SHRINK_DIS` the shrink distance of RogWord to generate the room (minimum distance to the edge).

7. `public static final Room WORLD_SHRINK_ROOM` the whole range of RogWord to generate rooms.

8. `public static final double ADJ_ROOM_P` probability to create an adjacent room on the edge.

9. `public static final int MAX_TRY_TIMES` MAX_TRY_TIMES to generate a room.

10. `public static final double MAX_AREA_RATE` MAX_AREA_RATE = Rooms Area / WORLD_ROOM Area, to stop spreading rooms.

##### non-static fields:

1. `private long seed` the random seed to generate pseudo randomness.

2. `private Random random` the random object to generate pseudo random number.

3. `private TETile[][] rogTiles` the generated world.

4. `private HashSet<Room> rooms` the already generated rooms to the world.

5. `private List<Room> unSpreadRooms` the list of rooms waiting to generate adjacent rooms.

6. `private int totalRoomsArea` the total areas of the generated rooms.

****

### Class DrawUtils

the helper method for drawing process(make the tiles in a specific index in the world's 2D array).

#### Fields

1. `public static final TETile WALL` the wall Tile.

2. `public static final TETile FLOOR` the floor Tile.

****

### Class Position

Process the position of the tiles' coordinates stuff,.

#### Fields

1. `private int x` position's x coordinate.

2. `private int y` position's y coordinate.

****

### Class Room

The room's property such as corner position, edges and area, which contain the inner class edge.

#### Fields

1. `private Position ldPos` the left down position of the room.

2. `private Position ruPos` the right up(top) position of the room.

3. `private Edge[] edges` use storeEdges() to preserve the edges once the room is drawn to the world.  Otherwise, is null. Indices for edges: bottom: 0, left: 1, top: 2, right: 3.

4. `private int roomArea` the room's area.

### Inner Class  Edge

the one edge of a room, may got 4 different type, corresponding to different edge index: bottom: 0, left: 1, top: 2, right: 3.

#### Fields

1. `int edgeLen` the length of the edge.

2. `Position startPos` the start position of the edge (one of the room's ldPos / ruPos).

3. `Position endPos` the end position of the edge.

4. `boolean isHorizontal` the index 0/2 edge corresponding to the horizontal edge, return true, else return false.

5. `boolean isLdStartEdge` the index 0/1 edge corresponding to the ldStartEdge, return true, else return false.

6. `int edgeIndex` 

****

## Algorithms

****

### Class RogWorld

Contains the main logic of our random word generation, where process the input string and generate the tile-based world.

#### public methods

1. `public RogWorld(String input)` the constructor of RogWorld class.

2. `public long findTheSeed(String input)` Return the seed (positive long) in the input,  if there's no valid seed, return -1.

3. `public boolean checkPosIn(Position p, Room range)` check a Pos is in the range.

4. `public boolean checkPosIn(Position p)` check a Pos is in the world.

5. `public boolean checkARoomIn(Room r, Room range)` check a Room r is in the range.

6. `public boolean checkARoomIn(Room r)` check a Room r is in the shrink world.

7. `public boolean checkARoomNoIntersection(Room r)` check a Room r isn't intersect with existing rooms. it has two steps: 
   
   a. check corner position is not in the exsiting rooms or the existing rooms' corner position not in the room's area. otherwise return false.
   
   b. if a is true, check two rooms no cross intersection (no corner position in each other but intersect)

8. `public boolean checkRoomValidation(Room r)` check a given room is OK to generating: 1) room in the world area; 2) room no intersection with existing rooms.

9. `public boolean createARandomRoomInRange(Room range)` Create a random room in a specific range, if the room is ok to create, then create it and return ture. Otherwise, return false.

10. `public boolean createARandomRoomInRange(Position ldPos)` Create a random room in ldPos, if the room is ok to create, then create it and return ture. Otherwise, return false.

11. `public void spreadARoom()` try to generate rooms from the four edges of a unspread room one time, if the unSpreadRooms is empty, do nothing.

12. `public boolean spreadExistRoomsOneRound()` Try to spread all exist rooms one round, if it's done, return true.

13. `public void generateRogWorld()` the main logic to generate a random world, it contains these subroutines:
    
    a. create a random start room(random postion, random size), mark it unspread;
    
    b. while the existing rooms area don't reach the stop point, spread the unspread rooms one round (continue spread rooms until all the rooms are not unspread).
    
    c. if the existing rooms area still not reach the stop point, remark existing rooms unspread, go back to b. step again.
    
    d. when existing rooms area reach the stop point, stop generating process.

****

### Class DrawUtils

Contains helper methods to draw the rooms in a world with some randomenss. Meanwhile, do some validation check.

#### public methods

1. `public static void drawARoom(TETile[][] world, Room room)` Draw a room with specific LeftDown pos and RightUp pos.

2. `public static Position getRandomPos(Random random, Room room)` Return a random Position in a rectangle area (room range).

3. `public static Position getRandomPos(Random random, Room.Edge e)` Return a random Position on an edge.

4. `public static Room getRandomRoom(Random random, Room range)` Return a random Room in the specific range with random xLen & yLen.

5. `public static Room getRandomRoom(Random random, Position startPos, Room.Edge e)` Return a random Room in the specific position on an edge with random xLen & yLen.

****

### Class Position

The ADT for position (x,y), can easily shift position to get the new one.

#### public methods

1. `public Position(int x, int y)` Constructor of the position class.

2. `public Position shiftPosition(int dx, int dy)` shift the position with dx, dy offsets.

3. `public int getX()` return the x index.

4. `public int getY()` return the y index.

****

### Class Room

Compute and store the useful room properties, such as edges, corner positions, area, etc.

#### public methods

1. `public Room(Position ldPos, Position ruPos)` Constructor of the Room class.

2. `public void storeEdges()`  preserve the edges once the room is created to the world. Otherwise, is null.  Indices for edges: bottom: 0, left: 1, top: 2, right: 3.

3. `public Position getLdPos()` return ldPos.

4. `public Position getRuPos()` return ruPos.

5. `public int getXLen()` return room's x length(horizontal edge).

6. `public int getYLen()` return room's y length(vertical edge).

7. `public Position getLuPos()` return luPos.

8. `public Position getRdPos()` return rdPos.

9. `public int getRoomArea()` return room's area.

10. `public Edge[] getEdges()` return room's edges, indices for edges: bottom: 0, left: 1, top: 2, right: 3.

### Inner Class Edge

Compute the edge attributes and do some edge based operations.

#### methods

1. `Edge(Position startPos, Position endPos)` Constructor of Edge class.

2. `Position shiftPosOnTheEdge(Position toShiftedPos)` shift a edge's position out    for one offset.

3. `Room generateARoomOnEdge(Random random, Position startPos, int xLen, int yLen)` generate a random room at an adjacent position of a room's edge.

***

## Persistence

When the user restarts `byow.Core.Main` and presses L, the world should be in **exactly the same state as it was before the project was terminated**. This state includes the state of the random number generator! More on this in the next section.

***

### Creative Ideas

***

1. w,a,s,d can push the box(create boxes in the world), and box decide to win (maybe need trigger to win, limited times to push a box....etc)
