package model.prgState.exeStack;

import java.util.List;

public interface IMyStack<T> {
    void push(T v);
    T pop();
    boolean isEmpty();
    List<T> getReversed();
}
