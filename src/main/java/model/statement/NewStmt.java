package model.statement;

import model.expresion.Exp;
import model.prgState.PrgState;
import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.RefValue;
import model.value.Value;
import model.value.type.RefType;
import model.value.type.Type;

public class NewStmt implements IStmt {
    private final String varName;
    private final Exp expression;

    public NewStmt(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        IMyDictionary<String, Value> symTable = state.getSymTable();
        IMyHeap heap = state.getHeap();

        // 1. var_name must exist in SymTable
        if (!symTable.isDefined(varName))
            throw new StmtException("Variable '" + varName + "' is not defined.");

        Value varValue = symTable.lookup(varName);

        // 2. var_name must have RefType
        if (!(varValue.getType() instanceof RefType refType))
            throw new StmtException("Variable '" + varName + "' must be of RefType.");

        // 3. Evaluate expression
        Value evaluated = expression.eval(symTable, heap);

        // 4. Check type compatibility
        if (!evaluated.getType().equals(refType.getInner()))
            throw new StmtException("Type mismatch: variable '" + varName +
                    "' refers to " + refType.getInner() +
                    " but expression has type " + evaluated.getType());

        // 5. Allocate new heap address
        int address = heap.allocate(evaluated);

        // 6. Update Symbol Table with new Reference
        symTable.put(varName, new RefValue(address, refType.getInner()));

        return null;
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + expression + ")";
    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(varName, expression.deepCopy());
    }

    @Override
    public IMyDictionary<String, Type> typecheck(IMyDictionary<String, Type> typeEnv) throws StmtException {
        Type typevar =  typeEnv.lookup(varName);
        Type typexp = expression.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new StmtException("New stmt: right hand side and left hand side have diffrent types");
    }
}
