package model.expresion;

import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.IntValue;
import model.value.Value;
import model.value.type.IntType;
import model.value.type.Type;

public class ArithExp implements Exp {
    Exp e1;
    Exp e2;
    int op; //1-plus, 2-minus, 3-star, 4-divide

    public ArithExp(int op, Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    public Value eval(IMyDictionary<String, Value> tbl, IMyHeap heap) throws ExpException {
        Value v1, v2;
        v1 = e1.eval(tbl, heap);
        if (v1.getType().equals(new IntType())) {
            v2 = e2.eval(tbl, heap);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                int n1, n2;
                n1 = i1.getValue();
                n2 = i2.getValue();
                switch (op) {
                    case 1:
                        return new IntValue(n1 + n2);
                    case 2:
                        return new IntValue(n1 - n2);
                    case 3:
                        return new IntValue(n1 * n2);
                    case 4:
                        if (n2 == 0)
                            throw new ExpException("division by zero");
                        return new IntValue(n1 / n2);
                    default:
                        throw new ExpException("invalid operator");
                }
            }
            else
                throw new ExpException("second operand is not an integer");
        }
        else
            throw new ExpException("first operand is not an integer");
    }

    @Override
    public String toString() {
        String opSymbol = switch (op) {
            case 1 -> "+";
            case 2 -> "-";
            case 3 -> "*";
            case 4 -> "/";
            default -> "?";
        };
        return e1.toString() + opSymbol + e2.toString();
    }

    @Override
    public Exp deepCopy() {
        return new ArithExp(op, e1.deepCopy(), e2.deepCopy());
    }

    @Override
    public Type typecheck(IMyDictionary<String, Type> tbl) throws ExpException {
        Type typ1, typ2;
        typ1 = e1.typecheck(tbl);
        typ2 = e2.typecheck(tbl);

        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new IntType();
            }
            else
                throw new ExpException("second operand is not an integer");
        }
        else
            throw new ExpException("first operand is not an integer");
    }
}