package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Locale;
import java.util.Random;

public class RogWorld {
    private long seed;
    private Random random;
    private TETile[][] rogTiles;

    public RogWorld(String input) {
        this.seed = findTheSeed(input);
        System.out.println(seed);
        this.random = new Random(this.seed);
        this.rogTiles = new TETile[Engine.WIDTH][Engine.HEIGHT];
        this.fillWorldNull();
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

    /** own main method for test.*/
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        RogWorld rw = new RogWorld("n123S");
        DrawUnit.drawARoom(rw.rogTiles, new Position(1,1), new Position(5,7));
        ter.renderFrame(rw.rogTiles);
    }
}
