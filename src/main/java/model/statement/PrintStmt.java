package model.statement;

import model.expresion.Exp;
import model.prgState.PrgState;
import model.prgState.out.IMyList;
import model.prgState.symTable.IMyDictionary;
import model.value.Value;
import model.value.type.Type;

public class PrintStmt implements IStmt{
    Exp exp;

    public PrintStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString(){
        return "Print(" +exp.toString()+")";
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException{
        Value value = exp.eval(state.getSymTable(), state.getHeap());

        IMyList out = state.getOut();
        out.add(value);

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        exp.typecheck(typeEnv);
        return typeEnv;
    }
}