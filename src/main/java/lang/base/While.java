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

public class While implements Base {
    
    public String getSymbol() {
        return "while";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Tuple result = new Tuple();
                result.add(Builder.buildExpression(operands.get(0), env));
                result.add(Builder.buildBlock(operands.get(1).asList(), env.extend()));
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                if (type != Operation.STATEMENT) throw new RuntimeException();
                while (true) {
                    Executor.executeExpression((Expression) operands.get(0), env);
                    if (((IntegerValue) env.pop()).getValue() == 0) break;
                    Executor.executeBlock((Block) operands.get(1), env.extend());
                }
            }
        };
    }
}
