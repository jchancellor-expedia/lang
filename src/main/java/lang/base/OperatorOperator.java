package lang.base;

import java.util.HashMap;
import java.util.Map;

import lang.builder.Block;
import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Element;
import lang.builder.Form;
import lang.builder.Identifier;
import lang.builder.Operation;
import lang.builder.Sequence;
import lang.builder.Tuple;
import lang.executor.EnvironmentValue;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.executor.OperatorValue;
import lang.executor.SyntaxValue;
import lang.executor.Value;
import lang.parser.SList;
import lang.parser.SSymbol;
import lang.parser.STree;

public class OperatorOperator implements Base {

    public String getSymbol() {
        return "operator";
    }

    public Form getForm() {
        return new Form() {
            public Element build(SList operands, BuilderEnvironment env) {
                SSymbol opName = operands.get(0).asSymbol();
                SSymbol opArgs = operands.get(1).asSymbol();
                SSymbol opEnv = operands.get(2).asSymbol();
                SList opFormDescription = operands.get(3).asList();
                SList opBody = operands.get(4).asList();
                final SList opArgsDescription = opFormDescription.get(0).asList();
                final SList opEnvDescription = opFormDescription.get(1).asList();
                Form opForm = new Form() {
                    public Element build(SList operands, BuilderEnvironment env) {
                        Map<String, BuilderEnvironment> envs = new HashMap<String, BuilderEnvironment>();
                        buildEnvs(opEnvDescription, env, envs);
                        return buildOperands(opArgsDescription, operands, envs);
                    }
                };
                BuilderEnvironment extended = env.extend();
                Tuple result = new Tuple();
                result.add(Builder.buildDeclarator(opName, env, opForm));
                result.add(Builder.buildDeclarator(opArgs, extended, null));
                result.add(Builder.buildDeclarator(opEnv, extended, null));
                Tuple descrip = new Tuple();
                // Why add the following two?
                descrip.add(Builder.buildSymbolElement(opArgsDescription, null));
                descrip.add(Builder.buildSymbolElement(opEnvDescription, null));
                result.add(descrip);
                result.add(Builder.buildBlock(opBody, extended));
                return result;
            }
        };
    }

    public Value getValue() {
        return new OperatorValue() {
            public void operate(Tuple operands, Object type, final ExecutorEnvironment definingEnv) {
                if (type != Operation.STATEMENT) throw new RuntimeException();
                final Identifier opName = (Identifier) operands.get(0);
                final Identifier opArgs = (Identifier) operands.get(1);
                final Identifier opEnv = (Identifier) operands.get(2);
                // Ignoring 3? Form checking (cf type checking) only?
                final Block opBody = (Block) operands.get(4);
                Value opValue = new OperatorValue() {
                    public void operate(Tuple operands, Object type, ExecutorEnvironment callingEnv) {
                        ExecutorEnvironment definingEnvExt = definingEnv.extend();
                        if (type != Operation.STATEMENT) throw new RuntimeException();
                        definingEnvExt.push(new SyntaxValue(operands));
                        Executor.executeDeclarator(opArgs, definingEnvExt);
                        definingEnvExt.push(new EnvironmentValue(callingEnv));
                        Executor.executeDeclarator(opEnv, definingEnvExt);
                        Executor.executeBlock(opBody, definingEnvExt);
                    }
                };
                definingEnv.push(opValue);
                Executor.executeDeclarator(opName, definingEnv);
            }
        };
    }
    
    private void buildEnvs(SList envDescription, BuilderEnvironment env, Map<String, BuilderEnvironment> envs) {
        envs.put(envDescription.get(0).asSymbol().getValue(), env);
        for (STree subDescription : envDescription.rest()) {
            buildEnvs(subDescription.asList(), env.extend(), envs);
        }
    }
    
    private Element buildOperands(SList argsDescription, STree operands, Map<String, BuilderEnvironment> envs) {
        String formTag = argsDescription.get(0).asSymbol().getValue();
        SList rest = argsDescription.rest(); 
        if (formTag.equals("tuple")) {
            return buildTuple(rest, operands, envs);
        } else if (formTag.equals("sequence")) {
            return buildSequence(rest, operands, envs);
        } else if (formTag.equals("expression")) {
            return buildExpression(rest, operands, envs);
        } else if (formTag.equals("receiver")) {
            return buildReceiver(rest, operands, envs);
        } else if (formTag.equals("declarator")) {
            return buildDeclarator(rest, operands, envs);
        } else if (formTag.equals("block")) {
            return buildBlock(rest, operands, envs);
        } else {
            throw new RuntimeException("formTag not expected: " + formTag);
        }
    }

    private Element buildBlock(SList rest, STree operands, Map<String, BuilderEnvironment> envs) {
        BuilderEnvironment env = envs.get(rest.get(0).asSymbol().getValue());
        if (env == null) throw new RuntimeException("env not found");
        return Builder.buildBlock(operands.asList(), env);
    }
    
    private Element buildDeclarator(SList rest, STree operands, Map<String, BuilderEnvironment> envs) {
        BuilderEnvironment env = envs.get(rest.get(0).asSymbol().getValue());
        if (env == null) throw new RuntimeException("env not found");
        return Builder.buildDeclarator(operands.asSymbol(), env, null);
    }
    
    private Element buildReceiver(SList rest, STree operands, Map<String, BuilderEnvironment> envs) {
        BuilderEnvironment env = envs.get(rest.get(0).asSymbol().getValue());
        if (env == null) throw new RuntimeException("env not found");
        return Builder.buildReceiver(operands, env);
    }
    
    private Element buildExpression(SList rest, STree operands, Map<String, BuilderEnvironment> envs) {
        BuilderEnvironment env = envs.get(rest.get(0).asSymbol().getValue());
        if (env == null) throw new RuntimeException("env not found");
        return Builder.buildExpression(operands, env);
    }
    
    private Element buildSequence(SList rest, STree operands, Map<String, BuilderEnvironment> envs) {
        Sequence<Element> result = new Sequence<>();
        SList eachItemDescription = rest.get(0).asList();
        for (STree item : operands.asList()) {
            result.add(buildOperands(eachItemDescription, item, envs));
        }
        return result;
    }
    
    private Element buildTuple(SList rest, STree operands, Map<String, BuilderEnvironment> envs) {
        Tuple result = new Tuple();
        for (int i = 0; i < rest.size(); i++) {
            result.add(buildOperands(rest.get(i).asList(), operands.asList().get(i), envs));
        }
        return result;
    }
}
