package lang.builder;

import lang.parser.SList;

public interface Form {
    public abstract Element build(SList operands, BuilderEnvironment env);
}
