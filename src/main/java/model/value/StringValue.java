package model.value;

import model.value.type.StringType;
import model.value.type.Type;

public class StringValue implements Value {
    private String value;
    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "'" + value + "'";
    }

    @Override
    public Value deepCopy() {
        return new StringValue(value);
    }
}
