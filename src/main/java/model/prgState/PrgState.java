package model.prgState;

import controller.CtrlException;
import model.prgState.barrierTable.IMyBarrierTable;
import model.prgState.barrierTable.MyBarrierTable;
import model.prgState.exeStack.IMyStack;
import model.prgState.exeStack.MyStack;
import model.prgState.fileTable.IMyFileTable;
import model.prgState.fileTable.MyFileTable;
import model.prgState.heap.IMyHeap;
import model.prgState.heap.MyHeap;
import model.prgState.out.IMyList;
import model.prgState.out.MyList;
import model.prgState.symTable.IMyDictionary;
import model.prgState.symTable.MyDictionary;
import model.statement.IStmt;
import model.value.Value;
import model.value.type.Type;

import java.util.concurrent.atomic.AtomicInteger;

public class PrgState {
    private IMyStack<IStmt> exeStack;
    private IMyDictionary<String, Value> symTable;
    private IMyDictionary<String, Type> typeEnv;
    private IMyList<Value> out;
    private IMyFileTable fileTable;
    private IMyHeap heap;
    private IStmt originalProgram;
    private Integer id;
    private IMyBarrierTable  barrierTable;

    public Boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public PrgState(IStmt prg) {
        this.exeStack = new MyStack<>();
        this.symTable = new MyDictionary<>();
        this.out = new MyList<>();
        this.fileTable = new MyFileTable();
        this.heap = new MyHeap();
        this.typeEnv = new MyDictionary<>();
        this.barrierTable = new MyBarrierTable();
        this.id = 1;
        this.originalProgram = prg.deepCopy();
        exeStack.push(prg);
    }
    private static AtomicInteger lastID = new AtomicInteger(2);
    public PrgState(
            IMyStack<IStmt> exeStack,
            IMyDictionary<String, Value> symTable,
            IMyList<Value> out,
            IMyFileTable fileTable,
            IMyHeap heap,
            IMyDictionary<String, Type> typeEnv,
            IMyBarrierTable barrierTable,
            Integer id
    ) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.typeEnv = typeEnv;
        this.barrierTable = barrierTable;
        this.id = lastID.getAndIncrement();
    }

    public IMyStack<IStmt> getExeStack() {
        return exeStack;
    }
    public IMyDictionary<String, Value> getSymTable() {
        return symTable;
    }
    public IMyList<Value> getOut() {
        return out;
    }
    public IMyFileTable getFileTable() {
        return fileTable;
    }
    public IMyHeap getHeap() {
        return heap;
    }
    public IMyDictionary<String, Type> getTypeEnv() {
        return typeEnv;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public IMyBarrierTable getBarrierTable() {
        return barrierTable;
    }

    @Override
    public String toString() {
        return  "ID: " + this.id + "\n" +
                "Stack: " + exeStack.toString() + "\n" +
                "SymTable: " + symTable.toString() + "\n" +
                "Out: " + out.toString() + "\n" +
                "FileTable: " + fileTable.toString() + "\n" +
                "Heap: " + heap.toString() + "\n" +
                "TypeEnv: " + typeEnv.toString() + "\n" +
                "BarrierTable " + barrierTable.toString();
    }

    public PrgState oneStep() throws CtrlException {
        if (exeStack.isEmpty()) {
            throw new StateException("Stack is empty");
        }
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }
}
