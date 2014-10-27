package lang.builder;

public class Literal implements Expression {

    private int value;

    public Literal(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
