package model.statement;

import model.prgState.PrgState;
import model.prgState.exeStack.IMyStack;
import model.prgState.symTable.IMyDictionary;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.Value;
import model.value.type.BoolType;
import model.value.type.IntType;
import model.value.type.StringType;
import model.value.type.Type;

public class VarDeclStmt implements IStmt {
    String name;
    Type type;

    public VarDeclStmt(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        IMyDictionary<String, Value> symTable = state.getSymTable();
        IMyStack<IStmt> stack = state.getExeStack();
        Value initialValue;

        if (symTable.isDefined(name)) {
            throw new StmtException("Variable already declared: " + name);
        }

        if (type.equals(new IntType())) {
            initialValue = new IntValue(0);
        }
        else if (type.equals(new BoolType())) {
            initialValue = new BoolValue(false);
        }
        else if (type.equals(new StringType())) {
            initialValue = new StringValue("");
        }
        else {
            initialValue = new StringValue("");
        }

        symTable.put(name, type.defaultValue());
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(name, type.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        typeEnv.put(name, type);
        return typeEnv;
    }
}
