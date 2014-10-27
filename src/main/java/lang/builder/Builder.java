package lang.builder;

import lang.parser.SInteger;
import lang.parser.SList;
import lang.parser.SSymbol;
import lang.parser.STree;

public class Builder {
    
    public static Program buildProgram(SList list, BuilderEnvironment env) {
        return new Program(buildBlock(list, env.extend()), env);
    }
    
    public static Block buildBlock(SList list, BuilderEnvironment env) {
        Block block = new Block();
        for (STree tree : list) {
            Operation operation = buildOperation(tree.asList(), env);
            block.add(operation);
        }
        return block;
    }
    
    private static Operation buildOperation(SList list, BuilderEnvironment env) {
        list.assertNonEmpty();
        SSymbol sym = list.first().asSymbol();
        Identifier operator = buildIdentifier(sym, env);
        Form form = operator.getVariable().getForm();
        Element operands = form.build(list.rest(), env);
        return new Operation(operator, form, operands);
    }
    
    public static Expression buildExpression(STree expression, BuilderEnvironment env) {
        if (expression.isList()) {
            return buildOperation(expression.asList(), env);
        } else if (expression.isSymbol()) {
            return buildIdentifier(expression.asSymbol(), env);
        } else if (expression.isInteger()) {
            return buildLiteral(expression.asInteger(), env);
        } else {
            throw new RuntimeException();
        }
    }
    
    public static Receiver buildReceiver(STree expression, BuilderEnvironment env) {
        if (expression.isList()) {
            return buildOperation(expression.asList(), env);
        } else if (expression.isSymbol()) {
            return buildIdentifier(expression.asSymbol(), env);
        } else {
            throw new RuntimeException();
        }
    }
    
    public static Identifier buildIdentifier(SSymbol expression, BuilderEnvironment env) {
        Variable var = env.get(expression.getValue());
        return new Identifier(expression.getValue(), var);
    }
    
    public static Identifier buildDeclarator(SSymbol sym, BuilderEnvironment env, Form form) {
        Variable var = new Variable(form);
        env.add(sym.getValue(), var);
        return new Identifier(sym.getValue(), var);
    }
    
    private static Literal buildLiteral(SInteger expression, BuilderEnvironment env) {
        return new Literal(expression.getValue());
    }
    
    public static SymbolElement buildSymbolElement(STree tree, BuilderEnvironment env) {
        return new SymbolElement(tree);
    }
}
