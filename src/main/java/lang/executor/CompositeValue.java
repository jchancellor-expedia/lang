package lang.executor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CompositeValue implements Value {

    private String name;
    private Map<String, Value> fields = new LinkedHashMap<>();

    public CompositeValue(String name) {
        this.name = name;
    }

    public Value get(String name) {
        return fields.get(name);
    }

    public void put(String name, Value field) {
        fields.put(name, field);
    }
    
    public String getName() {
        return name;
    }

    public Set<String> getFieldNames() {
        return fields.keySet();
    }
}
