package model.prgState.barrierTable;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface IMyBarrierTable {
    int allocate(int n);
    void update (int index, Pair<Integer, List<Integer>> pair);

    Boolean isDefined(int  index);
    Map<Integer, Pair<Integer, List<Integer>>> getAllBarriers();
    Pair<Integer, List<Integer>> lookup(int index);
}
