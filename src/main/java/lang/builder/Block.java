package lang.builder;

import java.util.ArrayList;
import java.util.List;

public class Block implements Element {

    private List<Operation> operations = new ArrayList<Operation>();
    
    public void add(Operation operation) {
        operations.add(operation);
    }

    public List<Operation> getStatements() {
        return operations;
    }
}
