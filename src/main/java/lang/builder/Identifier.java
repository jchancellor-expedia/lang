package lang.builder;

public class Identifier implements Expression, Receiver {

    private String text;
    private Variable variable;

    public Identifier(String text, Variable variable) {
        this.text = text;
        this.variable = variable;
    }

    public String getText() {
        return text;
    }

    public Variable getVariable() {
        return variable;
    }
}
