package model.prgState.heap;

import model.value.Value;

import java.util.HashMap;
import java.util.Map;

public class MyHeap implements IMyHeap {

    private Map<Integer, Value> heap;
    private int nextFreeAddress;

    public MyHeap() {
        heap = new HashMap<>();
        nextFreeAddress = 1;
    }

    @Override
    public int allocate(Value value) {
        heap.put(nextFreeAddress, value);
        nextFreeAddress++;
        return nextFreeAddress - 1;
    }

    @Override
    public Value get(int address) {
        return heap.get(address);
    }

    @Override
    public void put(int address, Value value) {
        heap.put(address, value);
    }

    @Override
    public boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override
    public Map<Integer, Value> getContent() {
        return heap;
    }

    @Override
    public void setContent(Map<Integer, Value> newContent) {
        heap = newContent;
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}
