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
import lang.executor.CompositeValue;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.OperatorValue;
import lang.executor.Value;
import lang.parser.SList;
import lang.parser.SSymbol;

public class Composite implements Base {

    public String getSymbol() {
        return "composite";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                SSymbol sConstructor = operands.get(0).asSymbol();
                final SList sFields = operands.get(1).asList();
                Form constructorForm = new Form() {
                    public Element build(SList operands, BuilderEnvironment env) {
                        Tuple result = new Tuple();
                        for (int i = 0; i < sFields.size(); i++) {
                            result.add(Builder.buildExpression(operands.get(i), env));
                        }
                        if (operands.size() == sFields.size() + 1) {
                            result.add(Builder.buildReceiver(operands.last(), env));
                        }
                        return result;
                    }
                };
                Form fieldForm = new Form() {
                    public Element build(SList operands, BuilderEnvironment env) {
                        Tuple result = new Tuple();
                        result.add(Builder.buildExpression(operands.get(0), env));
                        if (operands.size() == 2) {
                            result.add(Builder.buildReceiver(operands.get(1), env));
                        }
                        return result;
                    }
                };
                Tuple result = new Tuple();
                Identifier constructor = Builder.buildDeclarator(sConstructor, env, constructorForm);
                Tuple fields = new Tuple();
                for (int i = 0; i < sFields.size(); i++) {
                    fields.add(Builder.buildDeclarator(sFields.get(i).asSymbol(), env, fieldForm));
                }
                result.add(constructor);
                result.add(fields);
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                if (type != Operation.STATEMENT) throw new RuntimeException();
                final Identifier constructor = (Identifier) operands.get(0);
                final Tuple fields = (Tuple) operands.get(1);
                OperatorValue constructorValue = new OperatorValue() {
                    public void operate(Tuple constructorArgs, Object type, ExecutorEnvironment callingEnv) {
                        if (!Arrays.asList(Operation.EXPRESSION, Operation.STATEMENT).contains(type)) throw new RuntimeException();
                        CompositeValue compositeValue = new CompositeValue(constructor.getText());
                        for (int i = 0; i < fields.size(); i++) {
                            Executor.executeExpression((Expression) constructorArgs.get(i), callingEnv);
                            compositeValue.put(((Identifier) fields.get(i)).getText(), callingEnv.pop());
                        }
                        callingEnv.push(compositeValue);
                        if (constructorArgs.size() == fields.size() + 1) {
                            Executor.executeReceiver((Receiver) constructorArgs.get(constructorArgs.size() - 1), callingEnv);
                        }
                    }
                };
                env.push(constructorValue);
                Executor.executeDeclarator(constructor, env);
                for (int i = 0; i < fields.size(); i++) {
                    final int fi = i;
                    OperatorValue fieldValue = new OperatorValue() {
                        public void operate(Tuple fieldArgs, Object type, ExecutorEnvironment callingEnv) {
                            if (type == Operation.EXPRESSION || type == Operation.STATEMENT) {
                                Executor.executeExpression((Expression) fieldArgs.get(0), callingEnv);
                                CompositeValue compositeValue = (CompositeValue) callingEnv.pop();
                                callingEnv.push(compositeValue.get(((Identifier) fields.get(fi)).getText()));
                                if (type == Operation.STATEMENT) {
                                    Executor.executeReceiver((Receiver) fieldArgs.get(1), callingEnv);
                                }
                            } else if (type == Operation.RECEIVER) {
                                Value v = callingEnv.pop();
                                Executor.executeExpression((Expression) fieldArgs.get(0), callingEnv);
                                CompositeValue compositeValue = (CompositeValue) callingEnv.pop();
                                compositeValue.put(((Identifier) fields.get(fi)).getText(), v);
                            } else {
                                throw new RuntimeException();
                            }
                        }
                    };
                    env.push(fieldValue);
                    Executor.executeDeclarator((Identifier) fields.get(i), env);
                }
            }
        };
    }
}
