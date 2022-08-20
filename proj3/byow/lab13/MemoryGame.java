package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import javax.swing.plaf.PanelUI;
import java.awt.Color;
import java.awt.Font;
import java.security.PublicKey;
import java.util.Random;

public class MemoryGame {
    public static final long VISIBLE_TIME = 1000;
    public static final long BLANK_TIME = 500;

    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String randomString = "";
        int randomCharIndex;
        for (int i = 0; i < n; i++) {
            randomCharIndex = rand.nextInt(CHARACTERS.length);
            randomString = randomString + CHARACTERS[randomCharIndex];
        }
        return randomString;
    }

    private void setFrameBar() {
        StdDraw.textLeft(0, 38.5, "Round: " + this.round);
        StdDraw.line(0, 37, 40, 37 );
        if (this.playerTurn) {
            StdDraw.text(this.width * 1.0 / 2, 38.5, "Type!");
        } else {
            StdDraw.text(this.width * 1.0 /2, 38.5, "Watch!");
        }
        int randomIndex = rand.nextInt(ENCOURAGEMENT.length);
        String encouragement = ENCOURAGEMENT[randomIndex];
        StdDraw.textRight(40, 38.5, encouragement);
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        StdDraw.clear(Color.black);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(this.width * 1.0 / 2, this.height * 1.0
                / 2, s);
        //TODO: If game is not over, display relevant game information at the top of the screen
        setFrameBar();
        StdDraw.show();
    }

    private void drawFrameBlank(String s) {
        StdDraw.clear(Color.black);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(this.width * 1.0 / 2, this.height * 1.0
                / 2, s);
        StdDraw.show();
    }

    private void drawFrameDefaultTime(String s) throws InterruptedException {
        drawFrame(s, VISIBLE_TIME, BLANK_TIME);
    }

    private void drawFrame(String s, long visibleTime, long blankTime) throws InterruptedException {
        drawFrame(s);
        Thread.sleep(visibleTime);
        StdDraw.clear(Color.black);
        setFrameBar();
        StdDraw.show();
        Thread.sleep(blankTime);
    }

    private void drawFrame(char c, long visibleTime, long blankTime) throws InterruptedException {
        drawFrame(Character.toString(c), visibleTime, blankTime);
    }

    public void flashSequence(String letters) throws InterruptedException {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        if (letters.length() > 0) {
            for (int i = 0; i < letters.length(); i++) {
                drawFrame(letters.charAt(i), VISIBLE_TIME, BLANK_TIME);
            }
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String inputString = "";
        drawFrame(inputString);
        while (inputString.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                inputString = inputString + StdDraw.nextKeyTyped();
                drawFrame(inputString);
            }
        }
        return inputString;
    }

    public void startGame() throws InterruptedException {
        //TODO: Set any relevant variables before the game starts
        this.round = 1;
        String randomString;
        String inputString;
        this.gameOver = false;
        this.playerTurn = false;
        //TODO: Establish Engine loop
        while (!this.gameOver) {
            drawFrameDefaultTime("Round: " + this.round);
            randomString = generateRandomString(this.round);
            flashSequence(randomString);
            this.playerTurn = true;
            inputString = solicitNCharsInput(this.round);
            if (inputString.equals(randomString)) {
                this.round += 1;
            } else {
                this.gameOver = true;
            }
            this.playerTurn = false;
        }
        drawFrameBlank("Game Over! You made it to round: " + this.round);
    }
}
