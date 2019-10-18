package byow.Core;


import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class MenuChanger {
    TETile[][] dummyMenu;
    DrawingPermissions hailMary;


    public MenuChanger(DrawingPermissions a) {
        hailMary = a;
        dummyMenu = new TETile[0][0];
    }

    public TETile[][] update() {
        if (hailMary.canDraw) {
            draw();
        }
        return dummyMenu;
    }

    private void draw() {
        drawChecker();
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(new Font("Serif", Font.PLAIN, 20));
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 2, "GOTTA GO FAST");
        Font font1 = new Font("Arial", Font.PLAIN, 16);
        StdDraw.setFont(font1);
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 9, "(L) Load Last Game");
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 7, "(Q) Quit");
        StdDraw.text(WIDTH / 2, ((HEIGHT * 2) / 3) - 5, "(N) New Game");
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
