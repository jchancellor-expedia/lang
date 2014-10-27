package lang.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sequence<T> implements Element, Iterable<T> {
    
    private List<T> children = new ArrayList<>();
    
    public void add(T child) {
        children.add(child);
    }

    public T get(int index) {
        return children.get(index);
    }
    
    public int size() {
        return children.size();
    }

    @Override
    public Iterator<T> iterator() {
        return children.iterator();
    }
}
