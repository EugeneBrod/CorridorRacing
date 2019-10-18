package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import static byow.Core.Engine.*;

public class SeedChanger {
    String seed;
    TETile[][] dummySeedScreen;
    DrawingPermissions hailMary;

    public SeedChanger(DrawingPermissions a) {
        this.hailMary = a;
        dummySeedScreen = new TETile[0][0];
        seed = "";
    }

    public TETile[][] update(char c) {
        switch (c) {
            case '0' :
                seed += c;
                break;
            case '1' :
                seed += c;
                break;
            case '2' :
                seed += c;
                break;
            case '3' :
                seed += c;
                break;
            case '4' :
                seed += c;
                break;
            case '5' :
                seed += c;
                break;
            case '6' :
                seed += c;
                break;
            case '7' :
                seed += c;
                break;
            case '8' :
                seed += c;
                break;
            case '9' :
                seed += c;
                break;
            default:
                break;
        }
        if (this.hailMary.canDraw) {
            draw();
        }
        return dummySeedScreen;
    }

    private void draw() {
        drawChecker();
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(new Font("Serif", Font.PLAIN, 20));
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 2, seed);
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 16));
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 9, "(B) Back");
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 7, "(T) Time Trials");
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 5, "(S) Shakedown");

    }

    private void drawChecker() {
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

    public boolean isGoodSeed() {
        return seed.length() > 0;
    }

    public long getSeed() {
        return Long.parseLong(seed);
    }
}
