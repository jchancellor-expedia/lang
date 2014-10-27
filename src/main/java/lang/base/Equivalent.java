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
import lang.executor.IntegerValue;
import lang.executor.OperatorValue;
import lang.executor.Value;
import lang.parser.SList;

public class Equivalent implements Base {

    public String getSymbol() {
        return "same";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Tuple result = new Tuple();
                result.add(Builder.buildExpression(operands.get(0), env));
                result.add(Builder.buildExpression(operands.get(1), env));
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                if (type != Operation.EXPRESSION) throw new RuntimeException();
                Expression expr1 = (Expression) operands.get(0);
                Expression expr2 = (Expression) operands.get(1);
                Executor.executeExpression(expr1, env);
                Value val1 = env.pop();
                Executor.executeExpression(expr2, env);
                Value val2 = env.pop();
                env.push(new IntegerValue(val1 == val2 ? 1 : 0));
            }
        };
    }
}
