package model.statement;

import model.prgState.PrgState;
import model.prgState.exeStack.IMyStack;
import model.prgState.symTable.IMyDictionary;
import model.value.type.Type;

import java.util.ArrayList;
import java.util.List;


public class CompStmt implements IStmt {
    IStmt first;
    IStmt second;

    public CompStmt(IStmt first, IStmt second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ";" + second.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        IMyStack<IStmt> stk = state.getExeStack();
        stk.push(second);
        stk.push(first);
        return null;
    }

    @Override
    public List<String> toInfixList() {
        List<String> result = new ArrayList<>();
        result.addAll(first.toInfixList());
        result.addAll(second.toInfixList());
        return result;
    }

    public IStmt deepCopy() {
        return new CompStmt(first.deepCopy(), second.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck (IMyDictionary<String, Type> typeEnv) throws StmtException {
        IMyDictionary<String, Type> typEnv1 = first.typecheck(typeEnv);
        IMyDictionary<String, Type> typEnv2 = second.typecheck(typEnv1);
        return typEnv2;
    }
}