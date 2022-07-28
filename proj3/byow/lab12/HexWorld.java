package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 23333;
    private static final Random RANDOM = new Random(SEED);

    private static class Position {
        private int x;
        private int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Position shiftPosition(int dx, int dy) {
            return new Position(this.x + dx, this.y + dy);
        }
    }

    private static void addHexagonWithRandomTile(TETile[][] world, int s, Position p) {
        int tileNum = RANDOM.nextInt(3);
        TETile t = Tileset.NOTHING;
        switch (tileNum) {
            case 0:
                t = Tileset.GRASS;
                break;
            case 1:
                t = Tileset.FLOWER;
                break;
            case 2:
                t = Tileset.WATER;
                break;

        }
        addHexagon(world, s, p, t);
    }

    public static void addHexagon(TETile[][] world, int s, Position p, TETile t) {
        drawRectangle(world, s, p, t);
        Position leftTrianglePosition = p.shiftPosition(-1, 1);
        Position rightTrianglePosition = p.shiftPosition(s, 1);
        drawTriangle(world, s, leftTrianglePosition, t, -1);
        drawTriangle(world, s, rightTrianglePosition, t, 1);
    }

    private static void drawRectangle(TETile[][] world, int s, Position p, TETile t) {
        for (int x = p.x; x < p.x + s; x += 1) {
            for (int y = p.y; y < p.y + s * 2; y += 1) {
                world[x][y] = t;
            }
        }
    }

    private static void drawTriangle(TETile[][] world, int s, Position p, TETile t, int direction) {
        int px = p.x;
        int py = p.y;
        int x = px;
        for (int offSet = 0; Math.abs(offSet) < s; offSet += direction) {
            for (int y = py + Math.abs(offSet); y < py + s * 2 - 2 - Math.abs(offSet); y += 1) {
                x = px + offSet;
                world[x][y] = t;
            }
        }

    }

    private static void fillWorldNull(TETile[][] world) {
        int width = world.length;
        int height = world[0].length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWorldNull(world);
        Position p1 = new Position(10, 10);
        Position p2 = new Position(20, 20);
        addHexagonWithRandomTile(world, 3, p1);
        addHexagonWithRandomTile(world, 3, p2);
        ter.renderFrame(world);
    }
}
