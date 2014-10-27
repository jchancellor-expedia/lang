package lang.base;

import java.util.Arrays;

import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Element;
import lang.builder.Expression;
import lang.builder.Form;
import lang.builder.Identifier;
import lang.builder.Operation;
import lang.builder.Receiver;
import lang.builder.Form;
import lang.builder.Tuple;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.OperatorValue;
import lang.executor.Value;
import lang.parser.SList;

public class Var implements Base {
    
    private boolean declare;
    
    public Var(boolean declare) {
        this.declare = declare;
    }

    public String getSymbol() {
        return declare ? "var" : "set";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Tuple result = new Tuple();
                if (declare) {
                    result.add(Builder.buildDeclarator(operands.get(0).asSymbol(), env, null));
                } else {
                    result.add(Builder.buildReceiver(operands.get(0), env));
                }
                if (operands.size() == 2) {
                    result.add(Builder.buildExpression(operands.get(1), env));
                }
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                if (!Arrays.asList(Operation.STATEMENT, Operation.RECEIVER).contains(type)) throw new RuntimeException();
                if (type == Operation.STATEMENT) {
                    Expression expr = (Expression) operands.get(1);
                    Executor.executeExpression(expr, env);
                }
                Receiver recv = (Receiver) operands.get(0);
                if (declare) {
                    Executor.executeDeclarator((Identifier) recv, env);
                } else {
                    Executor.executeReceiver(recv, env);
                }
            }
        };
    }
}
