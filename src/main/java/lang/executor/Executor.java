package lang.executor;

import lang.builder.Block;
import lang.builder.Expression;
import lang.builder.Identifier;
import lang.builder.Operation;
import lang.builder.Tuple;
import lang.builder.Literal;
import lang.builder.Program;
import lang.builder.Receiver;
import lang.builder.Element;

public class Executor {
    
    public static void executeProgram(Program program, ExecutorEnvironment env) {
        executeBlock(program.getBody(), env.extend());
    }
    
    public static void executeBlock(Block block, ExecutorEnvironment env) {
        for (Operation operation : block.getStatements()) {
            executeOperation(operation, Operation.STATEMENT, env);
        }
    }
    
    private static void executeOperation(Operation operation, Object type, ExecutorEnvironment env) {
        Identifier operator = operation.getOperator();
        Element operands = operation.getOperands();
        Executor.executeIdentifierExpression(operator, env);
        OperatorValue op = (OperatorValue) env.pop();
        op.operate((Tuple) operands, type, env);
    }
    
    public static void executeExpression(Expression expression, ExecutorEnvironment env) {
        if (expression instanceof Identifier) {
            executeIdentifierExpression((Identifier) expression, env);
        } else if (expression instanceof Operation) {
            executeOperation((Operation) expression, Operation.EXPRESSION, env);
        } else if (expression instanceof Literal) {
            executeLiteral((Literal) expression, env);
        } else {
            throw new RuntimeException();
        }
    }
    
    public static void executeReceiver(Receiver receiver, ExecutorEnvironment env) {
        if (receiver instanceof Identifier) {
            executeIdentifierReceiver((Identifier) receiver, env);
        } else if (receiver instanceof Operation) {
            executeOperation((Operation) receiver, Operation.RECEIVER, env);
        } else {
            throw new RuntimeException();
        }
    }
    
    public static void executeIdentifierReceiver(Identifier receiver, ExecutorEnvironment env) {
        env.set(receiver.getText(), env.pop());
    }
    
    private static void executeLiteral(Literal expression, ExecutorEnvironment env) {
        env.push(new IntegerValue(expression.getValue()));
    }
    
    private static void executeIdentifierExpression(Identifier operator, ExecutorEnvironment env) {
        env.push(env.get(operator.getText()));
        
    }
    
    public static void executeDeclarator(Identifier declarator, ExecutorEnvironment env) {
        env.add(declarator.getText(), env.pop());
    }
}
