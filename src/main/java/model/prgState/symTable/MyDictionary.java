package model.prgState.symTable;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements IMyDictionary<K, V> {
    private final Map<K, V> dict;
    public MyDictionary() {
        dict = new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        dict.put(key, value);
    }

    @Override
    public V lookup(K key) {
        return dict.get(key);
    }

    @Override
    public boolean isDefined(K key) {
        return dict.containsKey(key);
    }

    @Override
    public Map<K, V> getContent() {
        return dict;
    }

    @Override
    public String toString() {
        return dict.toString();
    }

    @Override
    public IMyDictionary<K,V> copy() {
        IMyDictionary<K,V> copy = new MyDictionary<>();
        for (Map.Entry<K,V> entry : dict.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }
}
