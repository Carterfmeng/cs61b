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

1. `public static final int WIDTH'` The render WIDTH, corresponding to x coordinate.
2. `public static final int HEIGHT` The render HEIGHT, corresponding to y coordinate.
3. `TERenderer ter` The render of Engine.
4. `TETile[][] world` The world to rendering for Engine.

****

### Class RogWorld

The main logic of our random word generation, where process the input string and generate the tile-based world.

#### Fields

1. `private long seed` the random seed to generate pseudo randomness.

2. `private Random random` the random object to generate pseudo random number.

3. `private TETile[][] rogTiles` the generated world.

****

### Class Position

Process the position of the tiles' coordinates stuff,.

#### Fields

1. `private int x` position's x coordinate.

2. `private int y` position's y coordinate.

****

### Class RandomUtils

A library of static methods to generate pseudo-random numbers from different distributions (bernoulli, uniform, gaussian, discrete, and exponential). Also includes methods for shuffling an array and other randomness related stuff you might want to do.

> Adapted from https://introcs.cs.princeton.edu/java/22library/StdRandom.java.html

****

## Algorithms

****

***

## Persistence

***

## Temp Problematic Test Case:

1. 
