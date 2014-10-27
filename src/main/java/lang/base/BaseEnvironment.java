package lang.base;

import java.util.ArrayList;
import java.util.List;

import lang.builder.BuilderEnvironment;
import lang.builder.Variable;
import lang.executor.ExecutorEnvironment;

public class BaseEnvironment {
    
    List<Base> primitives;
    private BuilderEnvironment buildEnv;
    private ExecutorEnvironment execEnv;
    
    public BaseEnvironment() {
        addPrimitives();
        makeBuilderEnvironment();
        makeExecutorEnvironment();
    }

    public BuilderEnvironment getBuilderEnvironment() {
        return buildEnv;
    }

    public ExecutorEnvironment getExecutorEnvironment() {
        return execEnv;
    }

    private void addPrimitives() {
        primitives = new ArrayList<>();
        primitives.add(new Var(true));
        primitives.add(new Var(false));
        primitives.add(new Equivalent());
        primitives.add(new Print());
        primitives.add(new Binary.Sum());
        primitives.add(new Binary.Difference());
        primitives.add(new Binary.Product());
        primitives.add(new Binary.Quotient());
        primitives.add(new Binary.Remainder());
        primitives.add(new Binary.Less());
        primitives.add(new While());
        primitives.add(new If());
        primitives.add(new Procedure(false));
        primitives.add(new Procedure(true));
        primitives.add(new Composite());
        primitives.add(new OperatorOperator());
        primitives.add(new Evaluate());
        primitives.add(new Declare(true));
        primitives.add(new Declare(false));
        primitives.add(new Unpack());
        primitives.add(new Extend());
        primitives.add(new Execute());
        primitives.add(new Iterate());
    }

    private void makeBuilderEnvironment() {
        buildEnv = BuilderEnvironment.root();
        for (Base primitive : primitives) {
            buildEnv.add(primitive.getSymbol(), new Variable(primitive.getForm()));
        }
    }
    
    private void makeExecutorEnvironment() {
        execEnv = ExecutorEnvironment.root();
        for (Base primitive : primitives) {
            execEnv.add(primitive.getSymbol(), primitive.getValue());
        }
    }
}
