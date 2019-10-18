package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static byow.Core.Engine.*;
import static byow.Core.RandomUtils.uniform;
import static java.awt.geom.Point2D.distance;
import static java.util.Collections.max;

/*  Here we go, take two...
    Ima sign this shit fancy cause I thought about this way too long.
    Author: E U G E N E   B R O D S K Y
    */
public class Builder {
    /* The builder class receives an empty 2d TETile array and a seed for the
    pseudorandom number generator. Then it generates a world based on the seed.
     */

    // Utility functions


    Random random;
    TETile[][] tiles;
    List<Point> floors;

    private static final int MINROOMSIZE = 2;
    private static final int MAXROOMSIZE = 7;
    private static final int MINHALLSIZE = 3;
    private static final int MAXHALLSIZE = 11;
    private static final int MYWIDTH = WIDTH - XOFFSET;
    private static final int MYHEIGHT = HEIGHT - YOFFSET;


    public TETile[][] build(TETile[][] inputtiles, long seed) {
        random = new Random(seed);
        tiles = inputtiles;
        floors = new ArrayList<>();
        for (int y = 0; y < MYHEIGHT; y++) {
            for (int x = 0; x < MYWIDTH; x++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        makePath();
        return tiles;
    }

    private void makePath() {
        Point p = new Point(uniform(random, 0, MYWIDTH), uniform(random, 0, MYHEIGHT));
        int firstWidth = uniform(random, MINROOMSIZE, MAXROOMSIZE);
        int firstHeight = uniform(random, MINROOMSIZE, MAXROOMSIZE);
        room(p, firstWidth, firstHeight);
        while (floors.size() / (double) (MYWIDTH * MYHEIGHT) < uniform(random, .5, .9)) {
            placeStructure();
        }
        makeSpaceforWalls();
        buildDATWALLYEEHAW();
        addDoor();
    }

    private void addDoor() {
        int whichWall = uniform(random, 1, 100) % 4;
        if (whichWall == 0) {
            int whereWall = uniform(random, 1, MYWIDTH - 1);
            for (int x = whereWall; x < MYWIDTH; x++) {
                int inward = 0;
                while (tiles[x][inward] != Tileset.WALL) {
                    inward = inward + 1;
                    if (inward > MYHEIGHT - 1) {
                        break;
                    }
                }
                if (inward > MYHEIGHT - 1) {
                    continue;
                }
                if (inward == MYHEIGHT - 1 || tiles[x][inward + 1] == Tileset.WALL) {
                    continue;
                }
                tiles[x][inward] = Tileset.LOCKED_DOOR;
                return;
            }
            addDoor();
        }
        if (whichWall == 1) {
            int whereWall = uniform(random, 1, MYWIDTH - 1);
            for (int x = whereWall; x < MYWIDTH; x++) {
                int inward = MYHEIGHT - 1;
                while (tiles[x][inward] != Tileset.WALL) {
                    inward = inward - 1;
                    if (inward < 0) {
                        break;
                    }
                }
                if (inward < 0) {
                    continue;
                }
                if (inward == 0 || tiles[x][inward - 1] == Tileset.WALL) {
                    continue;
                }
                tiles[x][inward] = Tileset.LOCKED_DOOR;
                return;
            }
            addDoor();
        }
        if (whichWall == 2) {
            int whereWall = uniform(random, 1, MYHEIGHT - 1);
            for (int x = whereWall; x < MYHEIGHT; x++) {
                int inward = MYWIDTH - 1;
                while (tiles[inward][x] != Tileset.WALL) {
                    inward = inward - 1;
                    if (inward < 0) {
                        break;
                    }
                }
                if (inward < 0) {
                    continue;
                }
                if (inward == 0 || tiles[inward - 1][x] == Tileset.WALL) {
                    continue;
                }
                tiles[inward][x] = Tileset.LOCKED_DOOR;
                return;
            }
            addDoor();
        }
        if (whichWall == 3) {
            int whereWall = uniform(random, 1, MYHEIGHT - 1);
            for (int x = whereWall; x < MYHEIGHT; x++) {
                int inward = 0;
                while (tiles[inward][x] != Tileset.WALL) {
                    inward = inward + 1;
                    if (inward > MYWIDTH - 1) {
                        break;
                    }
                }
                if (inward > MYWIDTH - 1) {
                    continue;
                }
                if (inward == MYWIDTH - 1 || tiles[inward + 1][x] == Tileset.WALL) {
                    continue;
                }
                tiles[inward][x] = Tileset.LOCKED_DOOR;
                return;
            }
            addDoor();
        }
    }

    private void buildDATWALLYEEHAW() {
        for (int y = 0; y < MYHEIGHT; y++) {
            for (int x = 0; x < MYWIDTH; x++) {
                if (tiles[x][y] == Tileset.FLOOR) {
                    if (tiles[x - 1][y] == Tileset.NOTHING) {
                        tiles[x - 1][y] = Tileset.WALL;
                    }
                    if (tiles[x][y - 1] == Tileset.NOTHING) {
                        tiles[x][y - 1] = Tileset.WALL;
                    }
                    if (tiles[x + 1][y] == Tileset.NOTHING) {
                        tiles[x + 1][y] = Tileset.WALL;
                    }
                    if (tiles[x][y + 1] == Tileset.NOTHING) {
                        tiles[x][y + 1] = Tileset.WALL;
                    }
                    if (tiles[x - 1][y - 1] == Tileset.NOTHING) {
                        tiles[x - 1][y - 1] = Tileset.WALL;
                    }
                    if (tiles[x - 1][y + 1] == Tileset.NOTHING) {
                        tiles[x - 1][y + 1] = Tileset.WALL;
                    }
                    if (tiles[x + 1][y - 1] == Tileset.NOTHING) {
                        tiles[x + 1][y - 1] = Tileset.WALL;
                    }
                    if (tiles[x + 1][y + 1] == Tileset.NOTHING) {
                        tiles[x + 1][y + 1] = Tileset.WALL;
                    }
                }
            }
        }
    }


    private void makeSpaceforWalls() {
        for (int x = 0; x < MYWIDTH; x++) {
            if (tiles[x][0] == Tileset.FLOOR) {
                tiles[x][1] = Tileset.FLOOR;
            }
            if (tiles[x][MYHEIGHT - 1] == Tileset.FLOOR) {
                tiles[x][MYHEIGHT - 2] = Tileset.FLOOR;
            }
            tiles[x][0] = Tileset.NOTHING;
            tiles[x][MYHEIGHT - 1] = Tileset.NOTHING;
        }

        for (int y = 0; y < MYHEIGHT; y++) {
            if (tiles[0][y] == Tileset.FLOOR) {
                tiles[1][y] = Tileset.FLOOR;
            }
            if (tiles[MYWIDTH - 1][y] == Tileset.FLOOR) {
                tiles[MYWIDTH - 2][y] = Tileset.FLOOR;
            }
            tiles[0][y] = Tileset.NOTHING;
            tiles[MYWIDTH - 1][y] = Tileset.NOTHING;
        }

        for (int y = 1; y < MYHEIGHT - 1; y++) {
            for (int x = 1; x < MYWIDTH - 1; x++) {
                if (tiles[x][y] == Tileset.FLOOR) {
                    if (tiles[x - 1][y - 1] == Tileset.FLOOR
                            && tiles[x - 1][y] == Tileset.NOTHING
                            && tiles[x][y - 1] == Tileset.NOTHING) {
                        tiles[x - 1][y - 1] = Tileset.NOTHING;
                    } else if (tiles[x + 1][y - 1] == Tileset.FLOOR
                            && tiles[x + 1][y] == Tileset.NOTHING
                            && tiles[x][y - 1] == Tileset.NOTHING) {
                        tiles[x + 1][y - 1] = Tileset.NOTHING;

                    }
                }
            }
        }
    }

    private void room(Point p, int width, int height) {
        for (int y = (int) p.getY(); y < p.getY() + height; y++) {
            for (int x = (int) p.getX(); x < p.getX() + width; x++) {
                if (x < 0 || x >= MYWIDTH || y < 0 || y >= MYHEIGHT) {
                    continue;
                }
                tiles[x][y] = Tileset.FLOOR;
                floors.add(new Point(x, y));
            }
        }
    }

    private void roomCorner(Point p, int width, int height, String corner) {
        if (corner.equals("bl")) {
            room(p, width, height);
        }
        if (corner.equals("ur")) {
            room(new Point(p.getX() - width + 1, p.getY() - height + 1), width, height);
        }
        if (corner.equals("br")) {
            room(new Point(p.getX() - width + 1, p.getY()), width, height);
        }
        if (corner.equals("ul")) {
            room(new Point(p.getX(), p.getY() - height + 1), width, height);
        }

    }
    private void hallway(Point p, int length, String direction) {
        if (direction.equals("left")) {
            room(new Point(p.getX() - length, p.getY()), length, 1);
        }
        if (direction.equals("up")) {
            room(p, 1, length);
        }
        if (direction.equals("right")) {
            room(p, length, 1);
        }
        if (direction.equals("down")) {
            room(new Point(p.getX(), p.getY() - length), 1, length);
        }
    }

    private void placeStructure() {
        List<Point> treeFloor = new ArrayList<>(floors);
        KDTree tree = new KDTree(treeFloor);
        HashMap<Double, Point> map = new HashMap<>();
        for (int y = 0; y < MYHEIGHT; y += 2) {
            for (int x = 0; x < MYWIDTH; x += 2) {
                Point a = tree.nearest(x, y);
                map.put(distance(a.getX(), a.getY(), x, y), a);
            }
        }
        Point p = map.get(max(map.keySet()));
        randomizeStructure(p);
    }

    private void randomizeStructure(Point p) {
        int roomORhallway = random.nextInt(100) % 2;
        if (roomORhallway == 0) {
            int corner = random.nextInt(100) % 4;
            String string;
            if (corner == 0) {
                string = "ul";
            } else if (corner == 1) {
                string = "bl";
            } else if (corner == 2) {
                string = "ur";
            } else {
                string = "lr";
            }
            roomCorner(p, uniform(random, MINROOMSIZE, MAXROOMSIZE),
                    uniform(random, MINROOMSIZE, MAXROOMSIZE),  string);

        } else if (roomORhallway == 1) {
            int direction = uniform(random, 0, 19999) % 4;
            String string;
            if (direction == 0) {
                string = "left";
            } else if (direction == 1) {
                string = "up";
            } else if (direction == 2) {
                string = "right";
            } else {
                string = "down";
            }
            hallway(p, uniform(random, MINHALLSIZE, MAXHALLSIZE), string);

        }
    }
}
