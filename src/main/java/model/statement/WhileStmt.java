package model.statement;

import model.expresion.Exp;
import model.prgState.PrgState;
import model.prgState.exeStack.IMyStack;
import model.prgState.symTable.IMyDictionary;
import model.value.BoolValue;
import model.value.Value;
import model.value.type.BoolType;
import model.value.type.Type;

public class WhileStmt implements IStmt {
    private Exp condition;
    private IStmt body;

    public WhileStmt(Exp condition, IStmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + ") " + body.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        IMyStack<IStmt> stk = state.getExeStack();

        Value condValue = condition.eval(state.getSymTable(), state.getHeap());
        if (!condValue.getType().equals(new BoolType()))
            throw new StmtException("While condition is not a boolean");

        BoolValue boolVal = (BoolValue) condValue;
        if (boolVal.getValue()) {
            // Push the body first, then the while statement again (for the next iteration)
            stk.push(this);
            stk.push(body);
        }
        // If false, do nothing: the while ends
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(condition.deepCopy(), body.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        Type typexp = condition.typecheck(typeEnv);
        if (typexp instanceof BoolType) {
            body.typecheck(typeEnv);
            return typeEnv;
        }
        else
            throw new StmtException("The condition in WHILE expression is not a boolean type.");
    }
}
