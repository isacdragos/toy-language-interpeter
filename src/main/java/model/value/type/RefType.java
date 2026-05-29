package model.value.type;

import model.value.RefValue;
import model.value.Value;

public class RefType implements Type {
    private Type inner;

    public RefType(Type inner){
        this.inner = inner;
    }

    public Type getInner(){
        return inner;
    }

    @Override
    public boolean equals(Object another){
        if(another instanceof RefType)
            return inner.equals(((RefType) another).inner);
        return false;
    }

    @Override
    public String toString(){
        return "Ref(" + inner.toString() + ")";
    }

    @Override
    public Value defaultValue(){
        return new RefValue(0, inner); // address 0 = null
    }

    @Override
    public Type deepCopy() {
        return new RefType(inner);
    }
}
