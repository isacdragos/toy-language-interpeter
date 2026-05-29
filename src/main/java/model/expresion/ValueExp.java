package model.expresion;

import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.Value;
import model.value.type.Type;

public class ValueExp implements Exp{
    private Value value;

    public ValueExp(Value value) {
        this.value = value;
    }

    @Override
    public Value eval(IMyDictionary<String, Value> tbl, IMyHeap heap) throws ExpException {
        return value;
    }
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Exp deepCopy() {
        return new ValueExp(value.deepCopy());
    }

    @Override
    public Type typecheck(IMyDictionary<String, Type> tbl) throws ExpException {
        return value.getType();
    }
}
