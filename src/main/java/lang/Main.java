package lang;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lang.base.BaseEnvironment;
import lang.builder.Builder;
import lang.builder.BuilderEnvironment;
import lang.builder.Program;
import lang.executor.Executor;
import lang.executor.ExecutorEnvironment;
import lang.loader.Loader;
import lang.parser.Parser;
import lang.parser.SList;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static boolean fineEnabled = logger.isLoggable(Level.FINE);

    public static void main(String[] args) throws IOException {
        BaseEnvironment env = new BaseEnvironment();
        String code = load();
        SList tree = parse(code);
        Program program = build(tree, env.getBuilderEnvironment());
        execute(program, env.getExecutorEnvironment());
    }

    private static String load() throws IOException {
        logger.info("Loading...............");
        String code = Loader.load("build-test.lang", "build-test-2.lang");
        if (fineEnabled) logger.fine(code.toString());
        return code;
    }
    
    private static SList parse(String code) {
        logger.info("Parsing...............");
        SList tree = Parser.parseProgram(code);
        if (fineEnabled) logger.fine(tree.toString());
        return tree;
    }

    private static Program build(SList tree, BuilderEnvironment env) {
        logger.info("Building..............");
        return Builder.buildProgram(tree, env);
    }

    private static void execute(Program tree, ExecutorEnvironment env) {
        logger.info("Executing.............");
        Executor.executeProgram(tree, env);
    }
}
