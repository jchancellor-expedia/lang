package lang.executor;

public class IntegerValue implements Value {

    private int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
