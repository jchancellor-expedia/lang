package lang.base;

import lang.builder.Block;
import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Element;
import lang.builder.Expression;
import lang.builder.Form;
import lang.builder.Identifier;
import lang.builder.Operation;
import lang.builder.Form;
import lang.builder.Tuple;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.OperatorValue;
import lang.executor.Value;
import lang.parser.SList;
import lang.parser.STree;

public class Procedure implements Base {
    
    private boolean returnsValue;
    
    public Procedure(boolean returnsValue) {
        this.returnsValue = returnsValue;
    }
    
    public String getSymbol() {
        return returnsValue ? "function" : "procedure";
    }
    
    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                BuilderEnvironment extended = env.extend();
                final SList sProcParams = operands.get(1).asList();
                Tuple procParams = new Tuple();
                for (STree t : sProcParams) {
                    procParams.add(Builder.buildDeclarator(t.asSymbol(), extended, null));
                }
                Block procBody = Builder.buildBlock(operands.get(returnsValue ? 3 : 2).asList(), extended);
                Form procForm = new Form() {
                    public Element build(SList operands, BuilderEnvironment env) {
                        Tuple result = new Tuple();
                        for (int i = 0; i < sProcParams.size(); i++) {
                            result.add(Builder.buildExpression(operands.get(i), env));
                        }
                        return result;
                    }
                };
                Identifier procName = Builder.buildDeclarator(operands.get(0).asSymbol(), env, procForm);
                Tuple result = new Tuple();
                result.add(procName);
                result.add(procParams);
                if (returnsValue) {
                    Identifier procResult = Builder.buildIdentifier(operands.get(2).asSymbol(), extended);
                    result.add(procResult);
                }
                result.add(procBody);
                return result;
            }
        };
    }
    
    public Value getValue() {
        return new lang.executor.OperatorValue() {
            public void operate(Tuple operands, Object type, final ExecutorEnvironment env) {
                if (type != Operation.STATEMENT) throw new RuntimeException();
                Identifier procName = (Identifier) operands.get(0);
                final Tuple procParams = (Tuple) operands.get(1);
                final Identifier procResult = returnsValue ? (Identifier) operands.get(2) : null;
                final Block procBody = (Block) operands.get(returnsValue ? 3 : 2);
                OperatorValue procValue = new OperatorValue() {
                    public void operate(Tuple procArgs, Object type, ExecutorEnvironment callingEnv) {
                        if (type != (returnsValue ? Operation.EXPRESSION : Operation.STATEMENT)) throw new RuntimeException();
                        ExecutorEnvironment definingEnvExt = env.extend();
                        for (int i = 0; i < procArgs.size(); i++) {
                            Executor.executeExpression((Expression) procArgs.get(i), callingEnv);
                            definingEnvExt.push(callingEnv.pop());
                            Executor.executeDeclarator((Identifier) procParams.get(i), definingEnvExt);
                        }
                        Executor.executeBlock(procBody, definingEnvExt);
                        if (returnsValue) {
                            Executor.executeExpression(procResult, definingEnvExt);
                            callingEnv.push(definingEnvExt.pop());
                        }
                    }
                };
                env.push(procValue);
                Executor.executeDeclarator(procName, env);
            }
        };
    }
}
