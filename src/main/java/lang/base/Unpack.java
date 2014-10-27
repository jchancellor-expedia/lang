package lang.base;

import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Element;
import lang.builder.Expression;
import lang.builder.Form;
import lang.builder.Identifier;
import lang.builder.Form;
import lang.builder.Tuple;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.OperatorValue;
import lang.executor.SyntaxValue;
import lang.executor.Value;
import lang.parser.SList;
import lang.parser.STree;

public class Unpack implements Base {

    public String getSymbol() {
        return "unpack";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Tuple result = new Tuple();
                result.add(Builder.buildIdentifier(operands.get(0).asSymbol(), env));
                Tuple items = new Tuple();
                for (STree item : operands.get(1).asList()) {
                    items.add(Builder.buildDeclarator(item.asSymbol(), env, null));
                }
                result.add(items);
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                Expression tuple = (Expression) operands.get(0);
                Tuple items = (Tuple) operands.get(1);
                Executor.executeExpression(tuple, env);
                SyntaxValue v = (SyntaxValue) env.pop();
                Tuple itemValues = (Tuple) v.getSyntax();
                for (int i = 0; i < items.size(); i++) {
                    env.push(new SyntaxValue(itemValues.get(i)));
                    Executor.executeDeclarator((Identifier) items.get(i), env);
                }
            }
        };
    }
}
