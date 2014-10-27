package lang.builder;

/**
 * Think we need a top level element to hold some data associated with the body.
 * (Though perhaps this top level element should be an OperatorValue instead and we think of
 * executing the program as invoking the operator.)
 */
public class Program implements Element {
    
    private Block body;
    private BuilderEnvironment env;

    public Program(Block body, BuilderEnvironment env) {
        this.body = body;
        this.env = env;
    }

    public Block getBody() {
        return body;
    }
}
