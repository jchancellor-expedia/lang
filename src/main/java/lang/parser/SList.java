package lang.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SList extends STree implements Iterable<STree> {

    private List<STree> elements = new ArrayList<STree>();

    public SList assertSize(int size) {
        if (size() != size) throw new RuntimeException("Expected list of different size.");
        return this;
    }

    public void assertNonEmpty() {
        if (size() == 0) throw new RuntimeException("Expected non-empty list.");
    }

    public void add(STree element) {
        elements.add(element);
    }

    public int size() {
        return elements.size();
    }

    public STree get(int index) {
        return elements.get(index);
    }

    public STree first() {
        return elements.get(0);
    }

    public STree last() {
        return elements.get(elements.size() - 1);
    }

    public SList subList(int start, int end) {
        SList tree = new SList();
        tree.elements = elements.subList(start, end);
        return tree;
    }

    public SList rest() {
        return subList(1, elements.size());
    }

    public SList allButLast() {
        return subList(0, elements.size() - 1);
    }

    public Iterator<STree> iterator() {
        return elements.iterator();
    }

    public void appendString(StringBuilder sb) {
        int quiteLong = 500;
        sb.append("(");
        boolean first = true;
        for (STree element : elements) {
            if (first) first = false;
            else sb.append(" ");
            if (sb.length() < quiteLong) {
                element.appendString(sb);
            } else {
                sb.append("...");
                break;
            }
        }
        sb.append(")");
    }
}
