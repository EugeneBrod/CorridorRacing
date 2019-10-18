package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.Draw;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class timeDisplayer {

    TETile[][] dummy = new TETile[0][0];
    double speed;
    DrawingPermissions hailMary;

    public timeDisplayer(double time, DrawingPermissions a) {
        speed = time;
        hailMary = a;
    }

    public TETile[][] update(char c) {
        if (hailMary.canDraw) {
            draw();
        }
        return dummy;
    }

    private void draw() {
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(new Font("Ariel", Font.PLAIN, 30));
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 2, Double.toString(speed));
        StdDraw.setFont(new Font("Serif", Font.PLAIN, 20));
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 9, "(M) Menu");
        drawChecker();
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
}
