package lang.builder;

import java.util.HashMap;
import java.util.Map;

public class BuilderEnvironment {
    
    private BuilderEnvironment parent;
    private Map<String, Variable> bindings = new HashMap<String, Variable>();
    
    private BuilderEnvironment(BuilderEnvironment parent) {
        this.parent = parent;
    }
    
    public static BuilderEnvironment root() {
        return new BuilderEnvironment(null);
    }

    public BuilderEnvironment extend() {
        return new BuilderEnvironment(this);
    }
    
    public Variable get(String identifier) {
        Variable var = tryGet(identifier);
        if (var == null) {
            failNotBound(identifier);
        }
        return var;
    }
    
    public void add(String identifier, Variable var) {
        if (bindings.containsKey(identifier)) {
            failAlreadyBound(identifier);
        }
        bindings.put(identifier, var);
    }
    
    private Variable tryGet(String identifier) {
        if (bindings.containsKey(identifier)) {
            return bindings.get(identifier);
        } else if (parent != null) {
            return parent.tryGet(identifier);
        } else {
            return null;
        }
    }
    
    private void failAlreadyBound(String identifier) {
        throw new RuntimeException(String.format("Variable %s already bound.", identifier));
    }
    
    private void failNotBound(String identifier) {
        throw new RuntimeException(String.format("Variable %s not bound.", identifier));
    }
}
