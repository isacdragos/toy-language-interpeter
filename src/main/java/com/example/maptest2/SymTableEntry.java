package com.example.maptest2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.value.Value;

public class SymTableEntry {
    private StringProperty varName;
    private StringProperty value;

    public SymTableEntry(String varName, Value value) {
        this.varName = new SimpleStringProperty(varName);
        this.value = new SimpleStringProperty(value.toString());
    }
    public StringProperty varNameProperty() {
        return varName;
    }

    public StringProperty valueProperty() {
        return value;
    }
}
