package controller;

import model.prgState.PrgState;
import model.prgState.heap.IMyHeap;
import model.value.RefValue;
import model.value.Value;
import repository.IRepository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private IRepository repository;
    private boolean displayFlag;
    private ExecutorService executor;

    public void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {
        prgList.forEach(prg -> repository.logPrgStateExec(prg));

        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();}))
                .collect(Collectors.toList());

        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> { try { return future.get();}
                    catch (Exception e) {
                    return null;
                }})
                .filter(p->p!=null)
                .collect(Collectors.toList());

        prgList.addAll(newPrgList);

        prgList.forEach(prg -> repository.logPrgStateExec(prg));

        repository.setPrgList(prgList);

    }

    public List<Integer> getReachableAddresses(Collection<Value> symTableValues,
                                                Map<Integer, Value> heap) {
        List<Integer> workList = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        // 1. Start with addresses from the symTable
        for (Value v : symTableValues) {
            if (v instanceof RefValue ref) {
                workList.add(ref.getAddr());
            }
        }

        // 2. BFS/DFS through the heap references
        while (!workList.isEmpty()) {
            int addr = workList.remove(0);

            if (addr != 0 && !visited.contains(addr) && heap.containsKey(addr)) {
                visited.add(addr);

                Value heapVal = heap.get(addr);

                if (heapVal instanceof RefValue refVal) {
                    workList.add(refVal.getAddr());
                }
            }
        }

        return new ArrayList<>(visited);
    }

    public Map<Integer, Value> safeGarbageCollector(List<Integer> reachableAddrs,
                                                     Map<Integer, Value> heap) {
        return heap.entrySet()
                .stream()
                .filter(e -> reachableAddrs.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Controller(IRepository repository, boolean displayFlag) {
        this.repository = repository;
        this.displayFlag = displayFlag;
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public ExecutorService getExecutor() {
        return executor;
    }
    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
    public void allStep() throws CtrlException, InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());
        while (!prgList.isEmpty()) {
            List<Integer> allReachableAddresses = prgList.stream()
                    .flatMap(prg -> getReachableAddresses(
                            prg.getSymTable().getContent().values(),
                            prg.getHeap().getContent()
                    ).stream())
                    .distinct()
                    .collect(Collectors.toList());

            // update the shared heap
            if (!prgList.isEmpty()) {
                IMyHeap sharedHeap = prgList.getFirst().getHeap();
                sharedHeap.setContent(safeGarbageCollector(allReachableAddresses, sharedHeap.getContent()));
            }
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repository.getPrgList());
        }
        executor.shutdownNow();

        repository.setPrgList(prgList);
    }
    public IRepository getRepository() {
        return repository;
    }
}
