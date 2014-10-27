package lang.base;

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
import lang.executor.Value;
import lang.parser.SList;

public class Extend implements Base {

    public String getSymbol() {
        return "extend";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Identifier ssEnv = Builder.buildIdentifier(operands.get(0).asSymbol(), env);
                Identifier ssExt = Builder.buildDeclarator(operands.get(1).asSymbol(), env, null);
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
                EnvironmentValue envValue = (EnvironmentValue) env.pop();
                env.push(new EnvironmentValue(envValue.getEnvironment().extend()));
                Executor.executeDeclarator((Identifier) operands.get(1), env);
            }
        };
    }
}
