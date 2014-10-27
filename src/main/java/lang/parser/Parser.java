package lang.parser;

import java.util.Stack;

public class Parser {

    private static enum State {
        Word, Number, Start, Space, Indentation, Comment;
    }

    private static enum Char {

        Letter, Digit, OpenParen, CloseParen, Space, Line, Hash, Dash, Other;

        public static Char of(char ch) {
            switch (ch) {
            case ' ':
                return Space;
            case '(':
                return OpenParen;
            case ')':
                return CloseParen;
            case '\n':
            case '\r':
                return Line;
            case '#':
                return Hash;
            case '-':
                return Dash;
            default:
                if (Character.isLetter(ch)) return Letter;
                else if (Character.isDigit(ch)) return Digit;
                else return Other;
            }
        }
    }

    private State state;
    private StringBuilder text;
    private Stack<SList> stack = new Stack<SList>();
    private int currentLevel = 0;
    private int newLevel;
    private int dashLevel;
    private int tabSize = 2;
    
    public static SList parseProgram(String code) {
        return new Parser().parse(code);
    }

    private SList parse(String code) {
        state = State.Start;
        doOpenParen();
        doOpenParen();
        for (int i = 0; i < code.length(); i++) {
            process(code.charAt(i));
        }
        process('\n');
        doIndentation();
        stack.pop();
        if (stack.size() != 1) {
            throw new ParseException("Wrong number of final closing parentheses.");
        }
        return stack.pop();
    }

    private void process(char ch) {
        switch (state) {
        case Start:
            processStart(ch);
            break;
        case Word:
            processWord(ch);
            break;
        case Number:
            processNumber(ch);
            break;
        case Space:
            processSpace(ch);
            break;
        case Indentation:
            processIndentation(ch);
            break;
        case Comment:
            processComment(ch);
            break;
        default:
            throw new RuntimeException();
        }
    }

    private void processStart(char ch) {
        switch (Char.of(ch)) {
        case Letter:
            text = new StringBuilder();
            text.append(ch);
            state = State.Word;
            break;
        case Digit:
            text = new StringBuilder();
            text.append(ch);
            state = State.Number;
            break;
        case OpenParen:
            doOpenParen();
            break;
        case CloseParen:
            doCloseParen();
            break;
        case Space:
            state = State.Space;
            break;
        case Line:
            newLevel = 0;
            dashLevel = 0;
            state = State.Indentation;
            break;
        case Hash:
            state = State.Comment;
            break;
        default:
            throw new ParseException("Character type not expected.");
        }
    }

    private void processWord(char ch) {
        switch (Char.of(ch)) {
        case Letter:
            text.append(ch);
            break;
        case Digit:
        case Hash:
            throw new ParseException("Character not expected.");
        default:
            stack.peek().add(new SSymbol(text.toString()));
            state = State.Start;
            process(ch);
        }
    }

    private void processNumber(char ch) {
        switch (Char.of(ch)) {
        case Digit:
            text.append(ch);
            break;
        case Letter:
        case Hash:
            throw new ParseException("Character not expected.");
        default:
            stack.peek().add(new SInteger(Integer.parseInt(text.toString())));
            state = State.Start;
            process(ch);
        }
    }

    private void processSpace(char ch) {
        switch (Char.of(ch)) {
        case Space:
            break;
        default:
            state = State.Start;
            process(ch);
        }
    }

    private void processIndentation(char ch) {
        switch (Char.of(ch)) {
        case Line:
            break;
        case Space:
            newLevel++;
            break;
        case Dash:
            dashLevel = newLevel;
            newLevel++;
            break;
        case Hash:
            state = State.Comment;
            process(ch);
            break;
        default:
            doIndentation();
            state = State.Start;
            process(ch);
        }
    }

    private void processComment(char ch) {
        switch (Char.of(ch)) {
        case Line:
            state = State.Start;
            process(ch);
        }
    }

    private void doIndentation() {
        if (dashLevel == 0) dashLevel = newLevel;
        if (newLevel % tabSize != 0 || dashLevel % tabSize != 0) {
            throw new ParseException(String.format("Indent was not multiple of %d.", tabSize));
        }
        while (currentLevel >= dashLevel) {
            doCloseParen();
            currentLevel -= tabSize;
        }
        while (currentLevel < newLevel) {
            doOpenParen();
            currentLevel += tabSize;
        }
    }

    private void doOpenParen() {
        stack.push(new SList());
    }

    private void doCloseParen() {
        SList top = stack.pop();
        stack.peek().add(top);
    }
}

// S = (T
// T = aW | 0N | (T | ) 