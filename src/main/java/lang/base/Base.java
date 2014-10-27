package lang.base;

import lang.builder.Form;
import lang.executor.Value;

public interface Base {
    public String getSymbol();
    public Form getForm();
    public Value getValue();
}
