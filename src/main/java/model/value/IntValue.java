package model.value;

import model.value.type.IntType;
import model.value.type.Type;

public class IntValue implements Value {
    private int val;
    public IntValue(int val) {
        this.val = val;
    }

    @Override
    public Type getType() {
        return new IntType();
    }

    @Override
    public Integer getValue() {
        return val;
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }

    @Override
    public Value deepCopy() {
        return new IntValue(val);
    }
}
