package model.prgState.fileTable;

import model.value.StringValue;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class MyFileTable implements IMyFileTable {
    private Map<StringValue, BufferedReader> map;

    public MyFileTable() {
        map = new HashMap<StringValue, BufferedReader>();
    }

    @Override
    public boolean isDefined(StringValue key) {
        return map.containsKey(key);
    }

    @Override
    public void put(StringValue key, BufferedReader value) {
        map.put(key, value);
    }

    @Override
    public BufferedReader lookup(StringValue key) {
        return map.get(key);
    }
    @Override
    public void remove(StringValue key) {
        map.remove(key);
    }

    @Override
    public String toString() {
        return map.toString();
    }
    @Override
    public Map<StringValue, BufferedReader> getContent() {
        return map;
    }
}
