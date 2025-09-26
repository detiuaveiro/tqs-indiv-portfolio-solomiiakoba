package tqs;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;
public class _TqsStackTest {

    @DisplayName("New stack is empty")
    @org.junit.jupiter.api.Test
    public void newStackIsEmpty() {
        TqsStack<Integer> stack = new TqsStack<>();
        assertTrue(stack.isEmpty(), "Stack should be empty on construction");
    }
    @DisplayName("New stack has size 0")
    @org.junit.jupiter.api.Test
    public void newStackHasSize0() {
        TqsStack<Integer> stack = new TqsStack<>();
        assertEquals(0, stack.size(), "Stack should have size 0");
    }
    @DisplayName("Push to not empty stack")
    @org.junit.jupiter.api.Test
    public void pushToNotEmptyStack() {
        TqsStack<Integer> stack = new TqsStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        assertFalse(stack.isEmpty(), "Stack should not be empty on construction");
        assertEquals(4, stack.size(), "Stack should have size 4");
    }
    @DisplayName("Pushed then popped")
    @org.junit.jupiter.api.Test
    public void pushedThenPopped() {
        TqsStack<Integer> stack = new TqsStack<>();
        stack.push(1);
        int res = stack.pop();
        assertTrue(stack.isEmpty(), "Stack should be empty on construction");
        assertEquals(1, res, "Pop should return last pushed value");
    }

    @DisplayName("Pushed then peek")
    @org.junit.jupiter.api.Test
    public void pushedThenPeek() {
        TqsStack<Integer> stack = new TqsStack<>();
        stack.push(4);
        int beforePeek = stack.size();
        assertEquals(4, stack.peek(), "Stack should return last peeked value");
        assertEquals(beforePeek, stack.size(), "Stack should have the same size after peek");
    }

    @DisplayName("Pop all to empty")
    @org.junit.jupiter.api.Test
    public void popAllToEmpty() {
        TqsStack<Integer> stack = new TqsStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.pop();
        stack.pop();
        stack.pop();
        assertTrue(stack.isEmpty(), "Stack should be empty on construction");
        assertEquals(0, stack.size(), "Stack should have size 0 after popping all elements");
    }
    @DisplayName("Pop from empty stack")
    @org.junit.jupiter.api.Test
    public void popFromEmptyStack() {
        TqsStack<Integer> stack = new TqsStack<>();
        assertThrows(NoSuchElementException.class, stack::pop, "Pop from empty stack should throw exception");
    }
    @DisplayName("Peek into empty stack")
    @org.junit.jupiter.api.Test
    public void peekIntoEmptyStack() {
        TqsStack<Integer> stack = new TqsStack<>();
        assertThrows(NoSuchElementException.class, stack::peek, "Peek from empty stack should throw exception");
    }
    @DisplayName("Return nth element deleting n-1 top elements")
    @org.junit.jupiter.api.Test
    public void returnNthElementToTop() {
        TqsStack<Integer> stack = new TqsStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(1, stack.popTopN(3), "Stack should return nth element from the stack");
    }
}
