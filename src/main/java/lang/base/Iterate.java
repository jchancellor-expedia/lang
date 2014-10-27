package lang.base;

import lang.builder.Block;
import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Element;
import lang.builder.Expression;
import lang.builder.Form;
import lang.builder.Identifier;
import lang.builder.Form;
import lang.builder.Sequence;
import lang.builder.Tuple;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.OperatorValue;
import lang.executor.SyntaxValue;
import lang.executor.Value;
import lang.parser.SList;

public class Iterate implements Base {

    public String getSymbol() {
        return "iterate";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                BuilderEnvironment ext = env.extend();
                Identifier ssItem = Builder.buildDeclarator(operands.get(0).asSymbol(), ext, null);
                Identifier ssSequence = Builder.buildIdentifier(operands.get(1).asSymbol(), env);
                Block ssBody = Builder.buildBlock(operands.get(2).asList(), ext);
                Tuple result = new Tuple();
                result.add(ssItem);
                result.add(ssSequence);
                result.add(ssBody);
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                Executor.executeExpression((Expression) operands.get(1), env);
                @SuppressWarnings("unchecked")
                Sequence<Element> seq = (Sequence<Element>) ((SyntaxValue) env.pop()).getSyntax();
                for (Element item : seq) {
                    ExecutorEnvironment ext = env.extend();
                    ext.push(new SyntaxValue(item));
                    Executor.executeDeclarator((Identifier) operands.get(0), ext);
                    Executor.executeBlock((Block) operands.get(2), ext);
                }
            }
        };
    }
}
