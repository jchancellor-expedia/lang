package lang.base;

import lang.builder.Block;
import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Element;
import lang.builder.Expression;
import lang.builder.Form;
import lang.builder.Identifier;
import lang.builder.Form;
import lang.builder.Tuple;
import lang.executor.EnvironmentValue;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.OperatorValue;
import lang.executor.SyntaxValue;
import lang.executor.Value;
import lang.parser.SList;

public class Execute implements Base {

    public String getSymbol() {
        return "execute";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Identifier ssEnv = Builder.buildIdentifier(operands.get(0).asSymbol(), env);
                Identifier ssExt = Builder.buildIdentifier(operands.get(1).asSymbol(), env);
                Tuple result = new Tuple();
                result.add(ssEnv);
                result.add(ssExt);
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                Executor.executeExpression((Expression) operands.get(0), env);
                SyntaxValue blockValue = (SyntaxValue) env.pop();
                Executor.executeExpression((Expression) operands.get(1), env);
                EnvironmentValue envValue = (EnvironmentValue) env.pop();
                Executor.executeBlock((Block) blockValue.getSyntax(), envValue.getEnvironment());
            }
        };
    }
}
