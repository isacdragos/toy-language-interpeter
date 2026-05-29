package model.prgState.fileTable;

import model.value.StringValue;

import java.io.BufferedReader;
import java.util.Map;


public interface IMyFileTable {
    boolean isDefined(StringValue key);
    void put(StringValue key, BufferedReader value);
    BufferedReader lookup(StringValue key);
    void remove(StringValue key);
    String toString();
    Map<StringValue, BufferedReader> getContent();
}
