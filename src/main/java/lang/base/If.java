package lang.base;

import lang.builder.Block;
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

public class If implements Base {

    public String getSymbol() {
        return "if";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Tuple result = new Tuple();
                result.add(Builder.buildExpression(operands.get(0), env));
                result.add(Builder.buildBlock(operands.get(1).asList(), env.extend()));
                if (operands.size() == 3) {
                    result.add(Builder.buildBlock(operands.get(2).asList(), env.extend()));
                }
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                if (type != Operation.STATEMENT) throw new RuntimeException();
                Expression condition = (Expression) operands.get(0);
                Executor.executeExpression(condition, env);
                ExecutorEnvironment childEnv = env.extend();
                if (((IntegerValue) env.pop()).getValue() != 0) {
                    Block consequent = (Block) operands.get(1);
                    Executor.executeBlock(consequent, childEnv);
                } else if (operands.size() == 3) {
                    Block alternative = (Block) operands.get(2);
                    Executor.executeBlock(alternative, childEnv);
                }
            }
        };
    }
}
