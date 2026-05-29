package model.statement;

import model.expresion.Exp;
import model.expresion.VarExp;
import model.prgState.PrgState;
import model.prgState.barrierTable.IMyBarrierTable;
import model.prgState.symTable.IMyDictionary;
import model.value.IntValue;
import model.value.Value;
import model.value.type.IntType;
import model.value.type.Type;

public class NewBarrierStmt implements IStmt{
    private String var;
    private Exp exp;
    public NewBarrierStmt(String var, Exp exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState prgState) throws StmtException {
        Value value = exp.eval(prgState.getSymTable(), prgState.getHeap());

        if (!value.getType().equals(new IntType())) {
            throw new StmtException("Type Error on execute");
        }
        int capacity = ((IntValue) value).getValue();

        IMyBarrierTable table = prgState.getBarrierTable();
        int newLocation = table.allocate(capacity);
        IMyDictionary<String, Value> symTable = prgState.getSymTable();

        if (!symTable.isDefined(var)) {
            throw new StmtException("Variable " + var + " is not defined");
        }
        if (!symTable.lookup(var).getType().equals(new IntType())) {
            throw new StmtException("Variable " + var + " is not of type Int");
        }
        symTable.put(var, new IntValue(newLocation));
        return null;
    }

    @Override
    public String toString() {
        return "newBarrier(" + var + ", " + exp + ")";
    }

    @Override
    public IStmt deepCopy() {
        return new NewBarrierStmt(var, exp.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        if (!typeEnv.lookup(var).equals(new IntType())) {
            throw new StmtException("Variable " + var + " is not int");
        }

        Type exptype = exp.typecheck(typeEnv);
        if (!exptype.equals(new IntType())) {
            throw new StmtException("Variable " + var + " is not int");
        }

        return typeEnv;
    }
}
