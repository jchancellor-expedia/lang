package lang.executor;

import lang.builder.Element;

public class SyntaxValue implements Value {

    private Element syntax;

    public SyntaxValue(Element syntax) {
        this.syntax = syntax;
    }

    public Element getSyntax() {
        return syntax;
    }
}
