package lang.base;

import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Element;
import lang.builder.Expression;
import lang.builder.Form;
import lang.builder.Identifier;
import lang.builder.Receiver;
import lang.builder.Form;
import lang.builder.Tuple;
import lang.executor.EnvironmentValue;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.OperatorValue;
import lang.executor.SyntaxValue;
import lang.executor.Value;
import lang.parser.SList;

public class Evaluate implements Base {

    public String getSymbol() {
        return "evaluate";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Identifier ssExpr = Builder.buildIdentifier(operands.get(0).asSymbol(), env);
                Identifier ssEnv = Builder.buildIdentifier(operands.get(1).asSymbol(), env);
                Receiver sReceiver = Builder.buildReceiver(operands.get(2), env);
                Tuple result = new Tuple();
                result.add(ssExpr);
                result.add(ssEnv);
                result.add(sReceiver);
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                Executor.executeExpression((Expression) operands.get(0), env);
                SyntaxValue expressionValue = (SyntaxValue) env.pop();
                Executor.executeExpression((Expression) operands.get(1), env);
                EnvironmentValue envValue = (EnvironmentValue) env.pop();
                Executor.executeExpression((Expression) expressionValue.getSyntax(), envValue.getEnvironment());
                env.push(envValue.getEnvironment().pop());
                Executor.executeReceiver((Receiver) operands.get(2), env);
            }
        };
    }
}
