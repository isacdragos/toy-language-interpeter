package model.expresion;


import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.Value;
import model.value.type.Type;

public interface Exp {
    Value eval(IMyDictionary<String, Value> tbl, IMyHeap heap) throws ExpException;
    Type typecheck(IMyDictionary<String, Type> tbl) throws ExpException;
    Exp deepCopy();
}
