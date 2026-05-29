package model.prgState.symTable;

import java.util.Map;

public interface IMyDictionary<K, V> {
    void put (K key, V value);
    V lookup(K key);
    boolean isDefined(K key);
    Map<K, V> getContent();
    IMyDictionary<K,V> copy();
}
