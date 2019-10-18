package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    public static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void addHex(TETile[][] tiles, TETile texture, int size, Position p) {
        if (size < 2) {
            throw new IllegalArgumentException("hexagon too small, must be > 1");
        }

        // Position is bottom left corner of hexagon
        int start = p.x;
        int goal = p.x + size;
        int y = p.y;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                tiles[j][i] = Tileset.NOTHING;
            }
        }
        for (int i = p.y; i < y + size; i++) {
            if (y >= HEIGHT) {
                continue;
            }
            for (int j = start; j < goal; j++) {
                if (j < 0 || j > WIDTH) {
                    continue;
                }
                tiles[j][i] = texture;
            }
            start -= 1;
            goal += 1;
        }
        start += 1;
        goal -= 1;
        for (int i = p.y + size; i < y + size + size; i++) {
            if (y >= HEIGHT) {
                continue;
            }
            for (int j = start; j < goal; j++) {
                if (j < 0 || j > WIDTH) {
                    continue;
                }
                tiles[j][i] = texture;
            }
            start += 1;
            goal -= 1;
        }
    }

    public static void layDownHex(int size, Position p) {
    }




    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile flower = Tileset.GRASS;
        Position p = new Position(25, 25);

        TETile[][] hexPattern = new TETile[WIDTH][HEIGHT];
        addHex(hexPattern, flower, 5, p);


        ter.renderFrame(hexPattern);
    }
}
