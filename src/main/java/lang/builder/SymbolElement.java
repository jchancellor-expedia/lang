package lang.builder;

import lang.parser.STree;

public class SymbolElement implements Element {
    
    private STree tree;
    
    public SymbolElement(STree tree) {
        this.tree = tree;
    }
    
    public STree getTree() {
        return tree;
    }
}
