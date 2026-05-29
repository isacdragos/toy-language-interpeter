package model.value.type;

import model.value.Value;

public interface Type {
    boolean equals(Object another);   // used to check type compatibility
    String toString();
    Value defaultValue();
    Type deepCopy();
}
