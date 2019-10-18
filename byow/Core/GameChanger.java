package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static byow.Core.Engine.*;

public class GameChanger {

    public static final int QUERYWIDTH = WIDTH - XOFFSET;
    public static final int QUERYHEIGHT = HEIGHT - YOFFSET;

    private TETile[][] world;
    private Point position;
    private DrawingPermissions hailMary;

    public GameChanger(long seed, DrawingPermissions a) {
        hailMary = a;
        Builder builder = new Builder();
        world = builder.build(new TETile[QUERYWIDTH][QUERYHEIGHT], seed);
        Random random = new Random(seed);
        List<Point> floors = new ArrayList<>();
        for (int y = 0; y < QUERYHEIGHT; y++) {
            for (int x = 0; x < QUERYWIDTH; x++) {
                if (world[x][y] == Tileset.FLOOR) {
                    floors.add(new Point(x, y));
                }
            }
        }
        position = floors.get(RandomUtils.uniform(random, floors.size()));
        world[(int) position.getX()][(int) position.getY()] = Tileset.TREE;
    }

    public TETile[][] update(char c) {
        switch (c) {
            case 'W':
                moveUp();
                break;
            case 'A':
                moveLeft();
                break;
            case 'S':
                moveDown();
                break;
            case 'D':
                moveRight();
                break;
            default:
                break;
        }
        if (hailMary.canDraw) {
            draw();
        }
        moveWorld();

        return world;
    }
    public TETile[][] update() {
        if (hailMary.canDraw) {
            draw();
        }
        moveWorld();
        return world;
    }

    public void updateGraphics() {
        if (hailMary.canDraw) {
            draw();
        }
    }

    private void draw() {
        //StdDraw.clear(Color.black);
        drawMousePositionInfo();
        drawMission();
        drawInputs();
    }
    
    private void moveWorld() {

    }

    private void drawInputs() {
        StdDraw.setPenColor(Color.white);
        StdDraw.text(8, 1, "(M) Menu  (:Q) Quit/Save");
    }

    private void drawMission() {
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT - 1, "FASTER FASTER FASTER");
    }
    private void drawMousePositionInfo() {
        int xTile = (int) Math.floor(StdDraw.mouseX() - (XOFFSET / 2));
        int yTile = (int) Math.floor(StdDraw.mouseY() - (YOFFSET / 2));
        StdDraw.setPenColor(Color.white);
        if (xTile < 0 || xTile >= QUERYWIDTH || yTile < 0 || yTile >= QUERYHEIGHT) {
            StdDraw.text(WIDTH - 5, 1, "nothing");
        } else {
            StdDraw.text(WIDTH - 5, 1, world[xTile][yTile].description());
        }
    }

    private void moveUp() {
        if (world[(int) position.getX()][(int) position.getY() + 1] == Tileset.FLOOR) {
            world[(int) position.getX()][(int) position.getY()] = Tileset.FLOOR;
            world[(int) position.getX()][(int) position.getY() + 1] = Tileset.TREE;
            position = new Point(position.getX(), position.getY() + 1);
        }
    }

    private void moveLeft() {
        if (world[(int) position.getX() - 1][(int) position.getY()] == Tileset.FLOOR) {
            world[(int) position.getX()][(int) position.getY()] = Tileset.FLOOR;
            world[(int) position.getX() - 1][(int) position.getY()] = Tileset.TREE;
            position = new Point(position.getX() - 1, position.getY());
        }
    }

    private void moveDown() {
        if (world[(int) position.getX()][(int) position.getY() - 1] == Tileset.FLOOR) {
            world[(int) position.getX()][(int) position.getY()] = Tileset.FLOOR;
            world[(int) position.getX()][(int) position.getY() - 1] = Tileset.TREE;
            position = new Point(position.getX(), position.getY() - 1);
        }
    }

    private void moveRight() {
        if (world[(int) position.getX() + 1][(int) position.getY()] == Tileset.FLOOR) {
            world[(int) position.getX()][(int) position.getY()] = Tileset.FLOOR;
            world[(int) position.getX() + 1][(int) position.getY()] = Tileset.TREE;
            position = new Point(position.getX() + 1, position.getY());
        }
    }

    public boolean atDoor() {
        int x = (int) position.getX();
        int y = (int) position.getY();

        if (world[x - 1][y] == Tileset.LOCKED_DOOR ||
                world[x + 1][y] == Tileset.LOCKED_DOOR ||
                world[x][y - 1] == Tileset.LOCKED_DOOR ||
                world[x][y + 1] == Tileset.LOCKED_DOOR) {
            return true;
        }
        return false;
    }
}
