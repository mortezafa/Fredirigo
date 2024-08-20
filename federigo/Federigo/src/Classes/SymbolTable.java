package Classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Symbol> table = new HashMap<>();

    public void add(String name, String type, Object value, Boolean isMutable) {
        table.put(name, new Symbol(name, type, value, isMutable));
    }

    public Symbol lookup(String name) {
       return table.get(name);
    }

    public void update(String name, Object newValue) {
        Symbol symbol = table.get(name);
        if (symbol != null && symbol.isMutable) {
            symbol.setValue(newValue);
        } else {
            throw new RuntimeException("Cannot assign to immutable variable: " + name);
        }
    }
    @AllArgsConstructor
    @Getter
    @Setter
    static class Symbol {
        private String name;
        private String type;
        private Object value;
        private boolean isMutable;
    }
}
