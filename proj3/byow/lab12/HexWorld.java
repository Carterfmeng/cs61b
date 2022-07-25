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

    public static void addHexagon(TETile[][] world, int s, int xCor, int yCor, TETile t) {
        drawRectangle(world, s, xCor, yCor, t);
        int leftTriangleXCor = xCor - 1;
        int leftTriangleYCor = yCor + 1;
        int rightTriangleXCor = xCor + s;
        int rightTriangleYCor = yCor + 1;
        drawTriangle(world, s, leftTriangleXCor, leftTriangleYCor, t, -1);
        drawTriangle(world, s, rightTriangleXCor, rightTriangleYCor, t, 1);
    }

    private static void drawRectangle(TETile[][] world, int s, int xCor, int yCor, TETile t) {
        for (int x = xCor; x < xCor + s; x += 1) {
            for (int y = yCor; y < yCor + s * 2; y += 1) {
                world[x][y] = t;
            }
        }
    }

    private static void drawTriangle(TETile[][] world, int s, int xTCor, int yTCor, TETile t, int direction) {
        int x = xTCor;
        for (int offSet = 0; Math.abs(offSet) < s; offSet += direction) {
            for (int y = yTCor + Math.abs(offSet); y < yTCor + s * 2 - 2 - Math.abs(offSet); y += 1) {
                x = xTCor + offSet;
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
        addHexagon(world, 5, 10, 10, Tileset.FLOWER);
        ter.renderFrame(world);
    }
}
