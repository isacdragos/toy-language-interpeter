package model.value;

import model.value.type.Type;

public interface Value {
    Type getType(); // 0-integer 1-boolean
    Object getValue();
    Value deepCopy();
}
