# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: I tried to draw the tessellation from the top hexagon, or from the central hexagon, and my start position for a hexagon is from the first pixel without Nothing tile.

the same problem can use different direction, like from up to down is difficult to draw the word, but left to right is easy, sometime you just need to tweak a little about your thought.

don't neglect any complexity, like firstly I don't abstract the position, it makes difficult to figure out the new position and do the shift operation.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: rooms and hallways are different hexagons, and tesselation is to generate the random world with these rooms and hallways;

-----

**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: generate random size room, generate random size hallway

-----

**What distinguishes a hallway from a room? How are they similar?**

Answer:
distinguishes: 

1. hallway's width is limited to 1 or 2 tiles, and it's shape must be straight.
2. hallways can have turns, but rooms can't have turns.

similarity:

1. they both consist of walls and tiles.
2. they both reachable.
3. the world the consist together must be closed.