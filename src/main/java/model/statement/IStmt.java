package model.statement;

import model.prgState.PrgState;
import model.prgState.symTable.IMyDictionary;
import model.value.type.Type;

import java.util.List;

public interface IStmt {
    PrgState execute(PrgState state) throws StmtException;
    default List<String> toInfixList() {
        return List.of(this.toString());
    }
    public IStmt deepCopy();
    IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> tbl) throws StmtException;
}
