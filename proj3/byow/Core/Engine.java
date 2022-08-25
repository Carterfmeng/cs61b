package byow.Core;

import byow.Input.KeyboardInputSource;
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

    /** helper method for interactWithKeyboard(). if Load(L), seed = -1, if NewGame(N),return seed;
     * if Quit(q), return -2*/
    private Long solicitSeed(KeyboardInputSource beginInput) {
        String seed = "";
        Character nextInput = '_';
        boolean isSavingSeed = false;
        boolean gameBeginOrQuit = false;
        while (beginInput.possibleNextInput() && !nextInput.equals('s') && !gameBeginOrQuit) {
            nextInput = beginInput.getNextKey();
            switch (nextInput) {
                case 'n':
                    isSavingSeed = true;
                    this.ter.drawBeginFrame("");
                    break;
                case 'l':
                    if (!isSavingSeed) {
                        seed = "-1";
                        gameBeginOrQuit = true;
                    }
                    break;
                case 'q':
                    if (!isSavingSeed) {
                        this.ter.renderFrame("");
                        seed = "-2";
                        gameBeginOrQuit = true;
                    }
                    break;
                default:
                    if (isSavingSeed) {
                        if ("0123456789".contains(Character.toString(nextInput))) {
                            seed += nextInput;
                            this.ter.drawBeginFrame(seed);
                        }
                    }
            }
        }
        gameBeginOrQuit = true;
        isSavingSeed = false;
        return Long.parseLong(seed);
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() throws IOException {
        RogWorld rw = new RogWorld();
        boolean gameContinue = true;
        //The logic before enter the game
        this.ter.drawBeginFrame();
        KeyboardInputSource beginInput = new KeyboardInputSource();
        long seed = solicitSeed(beginInput);
        if (seed > 0) {
            rw = new RogWorld(seed);
            rw.generateRogWorld();
        } else if (seed == -2) {
            gameContinue = false;
        } else if (seed == -1) {
            // Java is Pass By Value
            rw = readRogWorld();
        }
        //The game start
        System.out.println(gameContinue);
        System.out.println(seed);
        KeyboardInputSource gameInput = new KeyboardInputSource();
        while (gameContinue) {
            this.ter.renderFrame(rw.getRogTiles(), rw.getPlayer());
            char nextOperation = gameInput.getNextKey();
            System.out.println(nextOperation);
            /** if nextOperation is wasd, move the Avatar, if next operation is :, read next operation, if q is followed, save and quit.*/
            if (operationIsMove(nextOperation)) {
                rw.movePlayer(nextOperation);
            } else if (nextOperation == ':') {
                nextOperation = beginInput.getNextKey();
                if (nextOperation == 'q') {
                    rw.saveRogWorld();
                    this.ter.renderFrame("");
                    gameContinue = false;
                }
            }
        }
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

    public static void main(String[] args) {
        /**
        StringInputSource inputSource = new StringInputSource("12".substring(2));
        System.out.println(inputSource.getInput().length());
        System.out.println(inputSource.getIndex());
         */

    }
}
