package byow.Core;

import byow.Input.StringInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.io.IOException;
import java.util.Locale;

import static byow.Core.Utils.*;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    TERenderer ter = new TERenderer();
    TETile[][] world;

    public Engine() {
        this.ter.initialize(WIDTH, HEIGHT);
        this.world = new TETile[WIDTH][HEIGHT];
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) throws IOException {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        TETile[][] finalWorldFrame = null;
        input = input.toLowerCase(Locale.ENGLISH);
        int operationIndex = 0;
        RogWorld rw;
        switch (input.charAt(0)) {
            case 'n':
                rw = new RogWorld(input);
                rw.generateRogWorld();
                operationIndex = input.indexOf("s") + 1;
                break;
            case 'l':
                rw = readRogWorld();
                operationIndex = 1;
                break;
            default:
                throw new ByowException("the input string must start with 'N' or 'L'.");
        }
        StringInputSource inputSource = new StringInputSource(input.substring(operationIndex));
        while (inputSource.possibleNextInput()) {
            char nextOperation = inputSource.getNextKey();
            /** if nextOperation is wasd, move the Avatar, if next operation is :, read next operation, if q is followed, save the RogWorld object to the file.*/
            if (operationIsMove(nextOperation)) {
                rw.movePlayer(nextOperation);
            } else if (nextOperation == ':') {
                nextOperation = inputSource.getNextKey();
                if (nextOperation == 'q') {
                    // persist the RogWorld
                    rw.saveRogWorld();
                }
            }
        }
        this.ter.renderFrame(rw.getRogTiles());
        finalWorldFrame = rw.getRogTiles();
        return finalWorldFrame;
    }

    private boolean operationIsMove(char operation) {
        if ("wasd".contains(Character.toString(operation))) {
            return true;
        }
        return false;
    }

    /** used to Game Sharing.*/
    public void interactWithRemoteClient(String input) {

    }
}
