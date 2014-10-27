package lang.parser;

public class SInteger extends STree {

    private int value;

    public SInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void appendString(StringBuilder sb) {
        sb.append(value);
    }
}
