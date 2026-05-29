package model.expresion;

import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.Value;
import model.value.type.Type;


public class VarExp implements Exp{
    String id;

    public VarExp(String id) {
        this.id = id;
    }

    @Override
    public Value eval(IMyDictionary<String, Value> tbl, IMyHeap heap) throws ExpException {
        return tbl.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public Exp deepCopy() {
        return new VarExp(id);
    }

    @Override
    public Type typecheck(IMyDictionary<String, Type> tbl) throws ExpException {
        return tbl.lookup(id);
    }
}
