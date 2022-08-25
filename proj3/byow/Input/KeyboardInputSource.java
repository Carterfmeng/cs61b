package byow.Input;

import com.sun.tools.doclets.formats.html.PackageUseWriter;
import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInputSource implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = false;

    public KeyboardInputSource() {}

    @Override
    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (PRINT_TYPED_KEYS) {
                    System.out.print(c);
                }
                return c;
            }
        }
    }

    @Override
    public boolean possibleNextInput() {
        return true;
    }
}
