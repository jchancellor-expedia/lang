package lang.executor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ExecutorEnvironment {
    
    private ExecutorEnvironment parent;
    private Map<String, Value> bindings = new HashMap<>();
    private Deque<Value> temp = new ArrayDeque<>();
    
    private ExecutorEnvironment(ExecutorEnvironment parent) {
        this.parent = parent;
    }
    
    public static ExecutorEnvironment root() {
        return new ExecutorEnvironment(null);
    }
    
    public ExecutorEnvironment extend() {
        return new ExecutorEnvironment(this);
    }
    
    public Value get(String identifier) {
        Value value = tryGet(identifier);
        if (value == null) {
            failNotBound(identifier);
        }
        return value;
    }
    
    public void set(String identifier, Value value) {
        if (bindings.containsKey(identifier)) {
            bindings.put(identifier, value);
        } else if (parent != null) {
            parent.set(identifier, value);
        } else {
            failNotBound(identifier);
        }
    }
    
    public void add(String identifier, Value value) {
        if (bindings.containsKey(identifier)) {
            failAlreadyBound(identifier);
        }
        bindings.put(identifier, value);
    }
    
    private Value tryGet(String identifier) {
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
    
    public void push(Value value) {
        temp.push(value);
    }
    
    public Value pop() {
        return temp.pop();
    }
    
    public void print(Value value, boolean newline) {
        if (value instanceof IntegerValue) {
            System.out.print(((IntegerValue) value).getValue());
        } else if (value instanceof CompositeValue) {
            CompositeValue cv = (CompositeValue) value;
            System.out.print("(" + cv.getName());
            for (String fieldName : cv.getFieldNames()) {
                System.out.print(" ");
                // System.out.print(fieldName + ":");
                print(cv.get(fieldName), false);
            }
            System.out.print(")");
        } else {
            System.out.println("Can't print non-integer-value " + value);
        }
        if (newline) System.out.println();
    }
}
