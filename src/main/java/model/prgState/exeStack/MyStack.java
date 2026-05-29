package model.prgState.exeStack;

import model.prgState.StateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class MyStack<T> implements IMyStack<T> {
    private Stack<T> stack;

    public MyStack() {
        stack = new Stack<>();
    }

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public T pop() {
        if (stack.isEmpty()) {
            throw new StateException("The stack is empty!");
        }
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public List<T> getReversed() {
        List<T> reversed = new ArrayList<>(stack);
        Collections.reverse(reversed);
        return reversed;
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
