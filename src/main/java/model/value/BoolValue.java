package model.value;

import model.value.type.BoolType;
import model.value.type.Type;

public class BoolValue implements Value {
    private boolean val;

    public BoolValue(boolean v) {
        val = v;
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public Boolean getValue() {
        return val;
    }

    @Override
    public String toString() {
        return Boolean.toString(val);
    }

    @Override
    public Value deepCopy() {
        return new BoolValue(val);
    }
}
