package model.expresion;

import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.RefValue;
import model.value.Value;
import model.value.type.RefType;
import model.value.type.Type;

public class ReadHeapExp implements Exp {

    private final Exp exp;

    public ReadHeapExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Value eval(IMyDictionary<String, Value> tbl, IMyHeap heap) throws ExpException {
        Value v = exp.eval(tbl, heap);

        if (!(v instanceof RefValue)) {
            throw new ExpException("rH: Expression is not a RefValue!");
        }

        RefValue ref = (RefValue) v;
        int addr = ref.getAddr();

        if (!heap.isDefined(addr)) {
            throw new ExpException("rH: Invalid heap address " + addr + "!");
        }

        return heap.get(addr);
    }
    @Override
    public Type typecheck(IMyDictionary<String, Type> tbl) throws ExpException {
        Type typ = exp.typecheck(tbl);
        if (typ instanceof RefType ref) {
            return ref.getInner();
        }
        else
            return typ;
    }

    @Override
    public Exp deepCopy() {
        return new ReadHeapExp(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "rH(" + exp.toString() + ")";
    }
}
