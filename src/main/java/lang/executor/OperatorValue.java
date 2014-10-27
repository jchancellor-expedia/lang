package lang.executor;

import lang.builder.Tuple;

public interface OperatorValue extends Value {
    public void operate(Tuple operands, Object type, ExecutorEnvironment env);
}
