package model.statement;

import model.expresion.Exp;
import model.prgState.PrgState;
import model.prgState.exeStack.IMyStack;
import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.Value;
import model.value.type.Type;

public class AssignStmt implements IStmt{
    String id;
    Exp exp;

    public AssignStmt(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException{
        IMyStack<IStmt> stk = state.getExeStack();
        IMyDictionary<String, Value> symTable = state.getSymTable();
        IMyHeap heap = state.getHeap();

        if (!symTable.isDefined(id)) {
            throw new StmtException("The variable " + id + " is not defined");
        }

        Value val = exp.eval(symTable, heap);
        Type typeID = (symTable.lookup(id)).getType();
        if (!val.getType().equals(typeID)) {
            throw new StmtException("The variable " + id + " is not of type " + typeID);
        }

        symTable.put(id, val);

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(id, exp.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        exp.typecheck(typeEnv);
        Type typevar = typeEnv.lookup(id);
        Type typexp = exp.typecheck(typeEnv);
        if (typevar.equals(typexp)) {
            return typeEnv;
        }
        else
            throw new StmtException("The variable " + id + " is not of type " + typevar);
    }
}
