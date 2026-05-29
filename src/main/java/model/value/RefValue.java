package model.value;

import model.value.type.RefType;
import model.value.type.Type;

public class RefValue implements Value {
    private int address;
    private Type locationType;

    public RefValue(int address, Type locationType){
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddr(){
        return address;
    }

    public Type getLocationType(){
        return locationType;
    }

    @Override
    public Type getType(){
        return new RefType(locationType);
    }

    @Override
    public Object getValue() {
        return address;
    }

    @Override
    public String toString(){
        return "(" + address + ", " + locationType + ")";
    }

    @Override
    public Value deepCopy(){
        return new RefValue(address, locationType.deepCopy());
    }
}
