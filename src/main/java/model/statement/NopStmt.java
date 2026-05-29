package model.statement;

import model.prgState.PrgState;
import model.prgState.symTable.IMyDictionary;
import model.value.type.Type;

public class NopStmt implements IStmt {
    @Override
    public String toString() {
        return "NOP";
    }

    @Override
    public PrgState execute(PrgState prgState) throws StmtException {
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NopStmt();
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        return typeEnv;
    }
}
