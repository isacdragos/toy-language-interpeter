package com.example.maptest2;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.value.Value;

public class HeapEntry {
    private final IntegerProperty address;
    private final StringProperty value;

    public HeapEntry(int address, Value value) {
        this.address = new SimpleIntegerProperty(address);
        this.value = new SimpleStringProperty(value.toString());
    }

    public IntegerProperty addressProperty() {
        return address;
    }

    public StringProperty valueProperty() {
        return value;
    }
}
