package tqs;

import java.util.LinkedList;
import java.util.NoSuchElementException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class TqsStack<T>{
    private LinkedList<T> stack;
    public TqsStack() {
        stack = new LinkedList<>();
    }
    public T pop() {
        if (stack.isEmpty())
            throw new NoSuchElementException("Stack is empty");
        return stack.removeFirst();
    }
    public int size() {
        return stack.size();
    }
    public T peek() {
        if  (stack.isEmpty())
            throw new NoSuchElementException("Stack is empty");
        return stack.getFirst();
    }
    public void push(T item) {
        stack.addFirst(item);
    }
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    public T popTopN(int n) {
        T top = null;
        for (int i = 0; i < n; i++) {
            top = stack.removeFirst();
        }
        return top;
    }
}