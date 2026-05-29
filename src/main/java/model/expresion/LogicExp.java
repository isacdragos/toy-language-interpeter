package model.expresion;

import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.BoolValue;
import model.value.Value;
import model.value.type.BoolType;
import model.value.type.Type;

public class LogicExp implements Exp {
    Exp e1, e2;
    int op;
    public LogicExp(Exp e1, Exp e2, int op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    public Value eval(IMyDictionary<String, Value> tbl, IMyHeap heap) throws ExpException {
        BoolValue a =  (BoolValue) e1.eval(tbl, heap);
        BoolValue b =  (BoolValue) e2.eval(tbl, heap);
        boolean b1 = a.getValue();
        boolean b2 = b.getValue();

        return switch (this.op) {
            case 0 -> new BoolValue(b1 && b2);
            case 1 -> new BoolValue(b1 || b2);
            case 2 -> new BoolValue(b1 ^ b2);
            default -> throw new ExpException("Invalid operation");
        };
    }

    @Override
    public Exp deepCopy() {
        return new LogicExp(e1.deepCopy(), e2.deepCopy(), op);
    }
    @Override
    public Type typecheck(IMyDictionary<String, Type> tbl) throws ExpException {
        Type typ1, typ2;
        typ1 = e1.typecheck(tbl);
        typ2 = e2.typecheck(tbl);

        if (typ1.equals(new BoolType())) {
            if (typ2.equals(new BoolType())) {
                return new BoolType();
            }
            else
                throw new ExpException("second operand is not a bool");
        }
        else
            throw new ExpException("first operand is not a bool");
    }
}
