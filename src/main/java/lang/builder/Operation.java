package lang.builder;

public class Operation implements Expression, Receiver {
    
    public static final Object EXPRESSION = new Object();
    public static final Object STATEMENT = new Object();
    public static final Object RECEIVER = new Object();
    
    private Identifier operator;
    private Form form;
    private Element operands;
    
    public Operation(Identifier operator, Form form, Element operands) {
        this.operator = operator;
        this.form = form;
        this.operands = operands;
    }
    
    public Identifier getOperator() {
        return operator;
    }
    
    public Form getForm() {
        return form;
    }
    
    public Element getOperands() {
        return operands;
    }
}
