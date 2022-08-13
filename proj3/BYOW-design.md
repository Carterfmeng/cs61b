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

4.  `private HashSet<Room> rooms` the already generated rooms to the world.

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

### Class DrawUtils

contains helper methods to draw the rooms in a world with some randomenss. Meanwhile, do some validation check.

#### public methods

1. `public static void drawARoom(TETile[][] world, Room room)` Draw a room with specific LeftDown pos and RightUp pos.

2. `public static Position getRandomPos(Random random, Room room)` Return a random Position in a rectangle area (room range).

3. `public static Position getRandomPos(Random random, Room.Edge e)` Return a random Position on an edge.

4. `public static Room getRandomRoom(Random random, Room range)` Return a random Room in the specific range with random xLen & yLen.

5. `public static Room getRandomRoom(Random random, Position startPos, Room.Edge e)` Return a random Room in the specific position on an edge with random xLen & yLen.

****















***

## Persistence

***
