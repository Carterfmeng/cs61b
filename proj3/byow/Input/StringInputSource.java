package byow.Input;

public class StringInputSource implements InputSource {
    private String input;
    private int index;

    public StringInputSource(String s) {
        input = s;
        index = 0;
    }

    @Override
    public char getNextKey() {
        char nextKey = input.charAt(index);
        index += 1;
        return nextKey;
    }

    @Override
    public boolean possibleNextInput() {
        return index < input.length();
    }

}
