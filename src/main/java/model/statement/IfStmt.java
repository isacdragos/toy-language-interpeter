package model.statement;

import model.expresion.Exp;
import model.expresion.ExpException;
import model.prgState.PrgState;
import model.prgState.exeStack.IMyStack;
import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.BoolValue;
import model.value.Value;
import model.value.type.BoolType;
import model.value.type.Type;

public class IfStmt implements IStmt {
    Exp exp;
    IStmt thenS;
    IStmt elseS;

    public IfStmt(Exp exp, IStmt thenStatement, IStmt elseStatement) {
        this.exp = exp;
        this.thenS = thenStatement;
        this.elseS = elseStatement;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        Value condValue;
        IMyDictionary<String, Value> symTable = state.getSymTable();
        IMyStack<IStmt> stack = state.getExeStack();
        IMyHeap heap = state.getHeap();
        BoolType bool = new BoolType();

        try {
            condValue = exp.eval(symTable, heap);
        } catch (ExpException e) {
            throw new StmtException("Error evaluating IF expression: " + e.getMessage());
        }

        if (!condValue.getType().equals(bool)) {
            throw new StmtException("The condition in IF expression is not a boolean type.");
        }

        BoolValue boolValue = (BoolValue) condValue;

        if (boolValue.getValue()) {
            stack.push(thenS);
        }
        else {
            stack.push(elseS);
        }
        return null;
    }

    @Override
    public String toString() {
        return "(IF(" + exp.toString() + ")THEN(" + thenS.toString() + ")ELSE(" + elseS.toString() + "))";
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        Type typexp = exp.typecheck(typeEnv);
        if (typexp instanceof BoolType) {
            thenS.typecheck(typeEnv.copy());
            elseS.typecheck(typeEnv.copy());
            return typeEnv;
        }
        else
            throw new StmtException("The condition in IF expression is not a boolean type.");
    }
}
