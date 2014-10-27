package lang.parser;

public abstract class STree {

    public boolean isList() {
        return this instanceof SList;
    }

    public boolean isSymbol() {
        return this instanceof SSymbol;
    }

    public boolean isInteger() {
        return this instanceof SInteger;
    }

    public SList asList() {
        if (!(this instanceof SList)) throw new RuntimeException("Expected list.");
        return (SList) this;
    }

    public SSymbol asSymbol() {
        if (!(this instanceof SSymbol)) throw new RuntimeException("Expected identifier.");
        return (SSymbol) this;
    }

    public SInteger asInteger() {
        if (!(this instanceof SInteger)) throw new RuntimeException("Expected integer.");
        return (SInteger) this;
    }

    public void failType() {
        throw new RuntimeException("Type not expected.");
    }

    public abstract void appendString(StringBuilder sb);

    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendString(sb);
        return sb.toString();
    }
}
