package model.statement;

import model.prgState.PrgState;
import model.prgState.exeStack.IMyStack;
import model.prgState.exeStack.MyStack;
import model.prgState.symTable.IMyDictionary;
import model.value.Value;
import model.value.type.Type;

public class ForkStmt implements IStmt {
    IStmt statement;
    public ForkStmt(IStmt statement) {
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState prgState) throws StmtException {
        IMyStack<IStmt> newStack = new MyStack<>();
        newStack.push(statement);

        IMyDictionary<String, Value> newSymTable = prgState.getSymTable().copy();

        return new PrgState(newStack, newSymTable, prgState.getOut(), prgState.getFileTable(), prgState.getHeap(), prgState.getTypeEnv(), prgState.getBarrierTable(), prgState.getId() + 1);
    }

    @Override
    public IMyDictionary<String, Type> typecheck (IMyDictionary<String, Type> typeEnv) throws StmtException {
        typeEnv = statement.typecheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(statement.deepCopy());
    }
}
