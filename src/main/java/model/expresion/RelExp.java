package model.expresion;

import model.prgState.heap.IMyHeap;
import model.prgState.symTable.IMyDictionary;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;
import model.value.type.BoolType;
import model.value.type.IntType;
import model.value.type.Type;

public class RelExp implements Exp {
    private Exp e1, e2;
    private String op;

    public RelExp(String op, Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public Value eval(IMyDictionary<String, Value> dict, IMyHeap heap) throws ExpException {
        Value v1 = e1.eval(dict, heap);
        if (!v1.getType().equals(new IntType())) {
            throw new ExpException("first operand: " + v1.getType().toString() + " is not an integer.");
        }

        Value v2 = e2.eval(dict, heap);
        if (!v2.getType().equals(new IntType())) {
            throw new ExpException("second operand: " + v2.getType().toString() + " is not an integer.");
        }

        IntValue i1 = (IntValue) v1;
        IntValue i2 = (IntValue) v2;
        int n1 = i1.getValue();
        int n2 = i2.getValue();

        return switch (op) {
            case "<" -> new BoolValue(n1 < n2);
            case "<=" -> new BoolValue(n1 <= n2);
            case "==" -> new BoolValue(n1 == n2);
            case ">" -> new BoolValue(n1 > n2);
            case ">=" -> new BoolValue(n1 >= n2);
            case "!=" -> new BoolValue(n1 != n2);
            default -> throw new ExpException("Invalid operation: " + op);
        };
    }

    @Override
    public String toString(){
        return e1.toString() + op + e2.toString();
    }

    @Override
    public Exp deepCopy() {
        return new RelExp(op, e1.deepCopy(), e2.deepCopy());
    }

    @Override
    public Type typecheck(IMyDictionary<String, Type> tbl) throws ExpException {
        Type typ1, typ2;
        typ1 = e1.typecheck(tbl);
        typ2 = e2.typecheck(tbl);

        if (typ1.equals(new IntType())) {
            if (typ2.equals(new IntType())) {
                return new BoolType();
            }
            else
                throw new ExpException("second operand is not an integer");
        }
        else
            throw new ExpException("first operand is not an integer");
    }
}
