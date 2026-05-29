package model.statement;

import model.expresion.Exp;
import model.expresion.ExpException;
import model.prgState.PrgState;
import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.RefValue;
import model.value.Value;
import model.value.type.RefType;
import model.value.type.Type;

public class WriteHeapStmt implements IStmt {

    private final String varName;
    private final Exp expression;

    public WriteHeapStmt(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException, ExpException {
        IMyDictionary<String, Value> symTable = state.getSymTable();
        IMyHeap heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new StmtException("wH: Variable " + varName + " is not defined!");
        }

        Value varValue = symTable.lookup(varName);

        if (!(varValue instanceof RefValue)) {
            throw new StmtException("wH: Variable " + varName + " is not a RefValue!");
        }

        RefValue ref = (RefValue) varValue;
        int address = ref.getAddr();

        if (!heap.isDefined(address)) {
            throw new StmtException("wH: Address " + address + " is not in the heap!");
        }

        Value evalValue = expression.eval(symTable, heap);

        if (!evalValue.getType().equals(ref.getLocationType())) {
            throw new StmtException("wH: Type mismatch! Expression type "
                    + evalValue.getType() + " does not match location type "
                    + ref.getLocationType());
        }

        // 6. update heap
        heap.put(address, evalValue);

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WriteHeapStmt(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + expression.toString() + ")";
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        Type varType = typeEnv.lookup(varName);
        Type exprType = expression.typecheck(typeEnv);
        if (!(varType instanceof RefType)) {
            throw new StmtException("wH: Variable " + varName + " is not a RefType!");
        }

        Type locationType = ((RefType) varType).getInner();

        if (!exprType.equals(locationType)) {
            throw new StmtException("wH: Type mismatch! Expression type does not match location type!");
        }
        return typeEnv;
    }
}

