package model.prgState.heap;

import model.value.Value;

import java.util.Map;

public interface IMyHeap {
    int allocate(Value value);
    Value get(int address);
    void put(int address, Value value);
    boolean isDefined(int address);

    Map<Integer, Value> getContent();
    void setContent(Map<Integer, Value> newContent);

    String toString();
}
