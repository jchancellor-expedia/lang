package lang.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tuple implements Element, Iterable<Element> {
    
    // Untyped children for now.
    private List<Element> children = new ArrayList<Element>();
    
    public void add(Element child) {
        children.add(child);
    }

    public Element get(int index) {
        return children.get(index);
    }
    
    public Element last() {
        return children.get(children.size() - 1);
    }
    
    public int size() {
        return children.size();
    }

    @Override
    public Iterator<Element> iterator() {
        return children.iterator();
    }
}
