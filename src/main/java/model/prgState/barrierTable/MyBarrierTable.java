package model.prgState.barrierTable;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyBarrierTable implements IMyBarrierTable {
    private Map<Integer, Pair<Integer, List<Integer>>> allBarriers;
    private int freeLocation;
    private Lock lock;

    public MyBarrierTable() {
        this.freeLocation = 0;
        this.lock = new ReentrantLock();
    }

    @Override
    public int allocate(int x) {
        lock.lock();
        try {
            int location = freeLocation;
            allBarriers.put(location, new Pair<>(x, new ArrayList<>()));
            freeLocation++;
            return location;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Pair<Integer, List<Integer>> lookup(int index) {
        lock.lock();
        try {
            return allBarriers.get(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(int index, Pair<Integer, List<Integer>> pair) {
        lock.lock();
        try {
            allBarriers.put(index, pair);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getAllBarriers() {
        lock.lock();
            try {
                return allBarriers;
            } finally {
                lock.unlock();
            }
    }

    @Override
    public Boolean isDefined(int index) {
        lock.lock();
        try {
            return allBarriers.containsKey(index);
        } finally {
            lock.unlock();
        }
    }
}
