package tqs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TqsStackTest {

    @Test
    @DisplayName("A stack is empty on construction")
    void testEmptyOnConstruction() {
        TqsStack<Integer> stack = new TqsStack<>();
        assertTrue(stack.isEmpty(), "Stack should be empty at creation");
    }

    @Test
    @DisplayName("A stack has size 0 on construction")
    void testSizeZeroOnConstruction() {
        TqsStack<Integer> stack = new TqsStack<>();
        assertEquals(0, stack.size(), "Size should be 0 at creation");
    }

    @Test
    @DisplayName("After n pushes, stack is not empty and size is n")
    void testPushIncreasesSize() {
        TqsStack<Integer> stack = new TqsStack<>();
        for (int i = 0; i < 5; i++) stack.push(i);
        assertFalse(stack.isEmpty());
        assertEquals(5, stack.size());
    }

    @Test
    @DisplayName("Push x then pop returns x")
    void testPushThenPop() {
        TqsStack<String> stack = new TqsStack<>();
        stack.push("hello");
        assertEquals("hello", stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("Push x then peek returns x but size stays the same")
    void testPushThenPeek() {
        TqsStack<String> stack = new TqsStack<>();
        stack.push("world");
        int before = stack.size();
        assertEquals("world", stack.peek());
        assertEquals(before, stack.size(), "Size must remain unchanged after peek");
    }

    @Test
    @DisplayName("After n pops, stack is empty and size is 0")
    void testPopUntilEmpty() {
        TqsStack<Integer> stack = new TqsStack<>();
        for (int i = 0; i < 3; i++) stack.push(i);
        for (int i = 0; i < 3; i++) stack.pop();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    @DisplayName("Popping from empty stack throws NoSuchElementException")
    void testPopEmptyThrows() {
        TqsStack<Integer> stack = new TqsStack<>();
        assertThrows(NoSuchElementException.class, stack::pop);
    }

    @Test
    @DisplayName("Peeking into empty stack throws NoSuchElementException")
    void testPeekEmptyThrows() {
        TqsStack<Integer> stack = new TqsStack<>();
        assertThrows(NoSuchElementException.class, stack::peek);
    }
}
