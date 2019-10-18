package byow.Core;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.Stopwatch;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static byow.Core.Engine.TRIALLENGTH;

public class GameController {

    private String gameState;
    private GameChanger gameworld;
    private MenuChanger menuworld;
    private SeedChanger seedworld;
    private int numTrials;
    private Stopwatch timer;
    private Random random;

    private boolean lastWasColon;
    private List<Character> keysPressed;
    private DrawingPermissions hailMary;
    timeDisplayer showworld;

    public GameController(DrawingPermissions a) {
        this.hailMary = a;
        menuworld = new MenuChanger(this.hailMary);
        seedworld = new SeedChanger(hailMary);
        gameState = "menu";
        lastWasColon = false;
        keysPressed = new ArrayList<>();
        numTrials = 0;

    }

    // Delegates tile update to appropriate method based on gamestate.
    public TETile[][] update(char c) {
        //if (c != '|') {
        keysPressed.add(c);
        //}
        setlastWasColon(c);

        switch (gameState) {
            case "game":
                return updateGame(c);
            case "load":
                return updateLoad(c);
            case "seed":
                return updateSeed(c);
            case "trials" :
                return updateTrials(c);
            case "showtime" :
                return showtimeUpdate(c);
            default:
                return updateMenu(c);
        }
    }

    private TETile[][] showtimeUpdate(char c) {
        switch(c) {
            case 'M':
                gameState = "menu";
                return menuworld.update();
            default:
                return showworld.update(c);
        }
    }

    private TETile[][] updateTrials(char c) {
        switch(c) {
            case 'Q':
                if (lastWasColon) {
                    saveGame();
                    return null;
                }
                break;
            case 'M':
                keysPressed.add('M');
                saveGame();
                gameState = "menu";
                return menuworld.update();
            default:
                break;
        }
        while (!gameworld.atDoor()) {
            return gameworld.update(c);
        }
        if (numTrials > TRIALLENGTH) {
            gameState = "showtime";
            numTrials = 0;
            showworld = new timeDisplayer(timer.elapsedTime(), hailMary);
            return showworld.update(c);
        }
        numTrials += 1;
        gameworld = new GameChanger(random.nextLong(), this.hailMary);
        return gameworld.update(c);
    }

    // Updates menu based on valid char inputs (N,L,Q)
    private TETile[][] updateMenu(char c) {
        switch (c) {
            case 'N':
                gameState = "seed";
                return seedworld.update(c);
            case 'L':
                gameState = "game";
                return updateLoad(c);
            case 'Q':
                return null;
            default:
                break;
        }
        return menuworld.update();
    }

    private TETile[][] updateGame(char c) {
        switch (c) {
            case 'Q':
                if (lastWasColon) {
                    saveGame();
                    return null;
                }
                break;
            case 'M':
                keysPressed.add('M');
                saveGame();
                gameState = "menu";
                return menuworld.update();
            default:
                break;
        }
        return gameworld.update(c);
    }

    private TETile[][] updateSeed(char c) {
        switch (c) {
            case 'S':
                TETile[][] result;
                if (seedworld.isGoodSeed()) {
                    gameworld = new GameChanger(seedworld.getSeed(), this.hailMary);
                    gameState = "game";
                    result = gameworld.update(c);
                } else {
                    gameState = "menu";
                    result = menuworld.update();
                }
                seedworld = new SeedChanger(this.hailMary);
                return result;
            case 'T':
                if (seedworld.isGoodSeed()) {
                    timer = new Stopwatch();
                    gameState = "trials";
                    random =  new Random(seedworld.getSeed());
                    gameworld = new GameChanger(random.nextLong(), this.hailMary);
                    result = gameworld.update(c);
                } else {
                    gameState = "seed";
                    result = seedworld.update(c);
                }
                seedworld = new SeedChanger(this.hailMary);
                return result;
            case 'B':
                gameState = "menu";
                TETile[][] result1 = menuworld.update();
                seedworld = new SeedChanger(this.hailMary);
                return result1;
            default:
                return seedworld.update(c);
        }
    }

    // Uses data from last save to create same world.
    // Creates new game controller and updates new controller with the data,
    // then all of the attributes of the new game controller are copied to this one.
    private TETile[][] updateLoad(char c) {
        List<Character> data = loadData();
        if (data.size() == 0) {
            gameState = "menu";
            return menuworld.update();
        } else {
            GameController temp = new GameController(this.hailMary);
            TETile[][] tiles =  menuworld.update();
            for (int i = 0; i < data.size() - 2; i++) {
                tiles = temp.update(data.get(i));
            }
            if (this.hailMary.canDraw) {
                StdDraw.clear(Color.black);
                temp.gameworld.updateGraphics();

            }
            copyGameController(temp);
            return tiles;
        }

    }

    // Copies attributes of input game controller to this one.
    private void copyGameController(GameController t) {
        this.menuworld = t.menuworld;
        this.gameState = t.gameState;
        this.keysPressed = t.keysPressed;
        this.lastWasColon = t.lastWasColon;
        this.seedworld = t.seedworld;
        this.gameworld = t.gameworld;
        this.timer = t.timer;
        this.hailMary = t.hailMary;
        this.showworld = t.showworld;
        this.numTrials = t.numTrials;
        this.random = t.random;
    }

    // Fetches data from file, if no file exists creates empty data.
    private List<Character> loadData() {
        File f = new File("./save_data.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (ArrayList<Character>) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                return new ArrayList<Character>();
            } catch (IOException e) {
                System.out.println(e);
                return new ArrayList<Character>();
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                return new ArrayList<Character>();
            }
        }

        /* In the case no list has been saved yet, we return a new one. */
        return new ArrayList<Character>();
    }

    // Creates a file to store keysPressed data.
    private void saveGame() {
        File f = new File("./save_data.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(keysPressed);
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            saveGame();
        } catch (IOException e) {
            System.out.println(e);
            saveGame();
        }
    }

    // UTILITY
    private void setlastWasColon(char c) {
        if (lastWasColon) {
            if (c == 'Q') {
                return;
            }
            if (c != '|') {
                lastWasColon = false;
            }
        } else {
            if (c == ':') {
                lastWasColon = true;
            }
        }
    }
}
