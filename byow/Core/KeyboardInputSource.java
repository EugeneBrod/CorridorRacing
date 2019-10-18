package byow.Core;

/**
 * Created by hug.
 */
import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInputSource implements InputSource {

    public KeyboardInputSource() {
    }

    public char getNextKey() {
        long timer = System.currentTimeMillis();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                return c;
            }
        }
    }

    public boolean possibleNextInput() {
        return true;
    }
}
