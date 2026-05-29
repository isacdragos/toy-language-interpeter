package model.statement;

import model.expresion.Exp;
import model.prgState.PrgState;
import model.prgState.symTable.IMyDictionary;
import model.value.StringValue;
import model.value.Value;
import model.value.type.StringType;
import model.value.type.Type;

import java.io.BufferedReader;

public class CloseRFile implements IStmt{
    private Exp exp;
    public CloseRFile(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ')';
    }

    @Override
    public PrgState execute(PrgState prgState) throws StmtException {
        Value v = exp.eval(prgState.getSymTable(), prgState.getHeap());
        if (!(v instanceof StringValue)) {
            throw new StmtException("closeRFile: expression is not a string");
        }

        BufferedReader br = prgState.getFileTable().lookup((StringValue) v);
        if (br == null) {
            throw new StmtException("closeRFile: file not found");
        }

        try {
            br.close();
        }
        catch (Exception e) {
            throw new StmtException("closeRFile: Error closing: " + e.getMessage());
        }

        prgState.getFileTable().remove((StringValue) v);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CloseRFile(this.exp.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        Type expType = exp.typecheck(typeEnv);

        if (!expType.equals(new StringType())) {
            throw new StmtException("CloseRFile: Expression must be of type String.");
        }

        return typeEnv;
    }
}
