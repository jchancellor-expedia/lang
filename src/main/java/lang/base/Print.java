package lang.base;

import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Element;
import lang.builder.Expression;
import lang.builder.Form;
import lang.builder.Operation;
import lang.builder.Form;
import lang.builder.Tuple;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.OperatorValue;
import lang.executor.Value;
import lang.parser.SList;

public class Print implements Base {

    public String getSymbol() {
        return "print";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Tuple result = new Tuple();
                result.add(Builder.buildExpression(operands.get(0), env));
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                if (type != Operation.STATEMENT) throw new RuntimeException();
                Expression expr = (Expression) operands.get(0);
                Executor.executeExpression(expr, env);
                env.print(env.pop(), true);
            }
        };
    }
}
