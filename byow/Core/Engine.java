package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import static java.lang.Character.toUpperCase;

public class Engine {
    private TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 35;
    public static final int XOFFSET = 2;
    public static final int YOFFSET = 4;
    public static final int TRIALLENGTH = 3;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT, XOFFSET / 2, YOFFSET / 2);
        InputSource source = new KeyboardInputSource();
        DrawingPermissions hailMary = new DrawingPermissions(true);
        GameController controller = new GameController(hailMary);
        TETile[][] tiles = new TETile[0][0];
        drawMenu();
        StdDraw.show();
        while (source.possibleNextInput()) {
            StdDraw.clear(Color.black);
            tiles = controller.update(source.getNextKey());
            if (tiles == null) {
                System.exit(0);
            }
            ter.renderFrame(tiles);
            StdDraw.show();
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

    public static TETile[][] interactWithInputString(String input) {
        InputSource source = new StringInputDevice(input);
        DrawingPermissions hailMary = new DrawingPermissions(false);
        GameController controller = new GameController(hailMary);
        TETile[][] tiles = new TETile[0][0];
        TETile[][] result = new TETile[0][0];
        while (source.possibleNextInput()) {
            tiles = controller.update(toUpperCase(source.getNextKey()));
            if (tiles == null) {
                break;
            }
            result = tiles;
        }
        return result;
    }

    private void drawMenu() {
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(new Font("Serif", Font.PLAIN, 20));
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 2, "GOTTA GO FAST");
        Font font1 = new Font("Arial", Font.PLAIN, 16);
        StdDraw.setFont(font1);
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 9, "(L) Load Last Game");
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 7, "(Q) Quit");
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 5, "(N) New Game");
        int boxY = (HEIGHT / 4);
        int boxX = WIDTH;
        for (int i = 0; i <= boxX; i++) {
            int color = i % 2;
            for (int j = 0; j <= boxY; j++) {
                if (color == 0) {
                    StdDraw.setPenColor(Color.white);
                    color = 1;
                } else {
                    StdDraw.setPenColor(Color.black);
                    color = 0;
                }
                StdDraw.filledRectangle(i, (HEIGHT / 4) - j + .5, .5, .5);
                StdDraw.filledRectangle(i, ((3 * HEIGHT) / 4) + j + .5, .5, .5);
            }
        }
    }


    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.interactWithKeyboard();

        /*
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT, 1, 2);
        TETile[][] tiles = interactWithInputString("n6370083293206999678swaswawsd");
        ter.renderFrame(tiles);
        StdDraw.show();
        */

    }
}
