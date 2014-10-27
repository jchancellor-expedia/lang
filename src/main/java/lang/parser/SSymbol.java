package lang.parser;

public class SSymbol extends STree {

    private String value;

    public SSymbol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void appendString(StringBuilder sb) {
        sb.append(value);
    }
}
