package lang.base;

import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Element;
import lang.builder.Expression;
import lang.builder.Form;
import lang.builder.Operation;
import lang.builder.Receiver;
import lang.builder.Form;
import lang.builder.Tuple;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.IntegerValue;
import lang.executor.OperatorValue;
import lang.executor.Value;
import lang.parser.SList;

public abstract class Binary implements Base {
    
    public static class Sum extends Binary {
        public String getSymbol() {
            return "sum";
        }
        protected int bin(int a, int b) {
            return a + b;
        }
    }
    
    public static class Difference extends Binary {
        public String getSymbol() {
            return "diff";
        }
        protected int bin(int a, int b) {
            return a - b;
        }
    }
    
    public static class Product extends Binary {
        public String getSymbol() {
            return "prod";
        }
        protected int bin(int a, int b) {
            return a * b;
        }
    }
    
    public static class Quotient extends Binary {
        public String getSymbol() {
            return "quot";
        }
        protected int bin(int a, int b) {
            return a / b;
        }
    }
    
    public static class Remainder extends Binary {
        public String getSymbol() {
            return "rem";
        }
        protected int bin(int a, int b) {
            return a % b;
        }
    }
    
    public static class Less extends Binary {
        public String getSymbol() {
            return "less";
        }
        protected int bin(int a, int b) {
            return a < b ? 1 : 0;
        }
    }
    
    protected abstract int bin(int a, int b);
    
    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                Tuple result = new Tuple();
                result.add(Builder.buildExpression(operands.get(0), env));
                result.add(Builder.buildExpression(operands.get(1), env));
                if (operands.size() == 3) {
                    result.add(Builder.buildReceiver(operands.get(2), env));
                }
                return result;
            }
        };
    }
    
    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, ExecutorEnvironment env) {
                if (type != Operation.STATEMENT && type != Operation.EXPRESSION) throw new RuntimeException();
                Expression expr1 = (Expression) operands.get(0);
                Expression expr2 = (Expression) operands.get(1);
                Executor.executeExpression(expr1, env);
                int val1 = ((IntegerValue) env.pop()).getValue();
                Executor.executeExpression(expr2, env);
                int val2 = ((IntegerValue) env.pop()).getValue();
                env.push(new IntegerValue(bin(val1, val2)));
                if (type == Operation.STATEMENT) {
                    Executor.executeReceiver((Receiver) operands.get(2), env);
                }
            }
        };
    }
}
