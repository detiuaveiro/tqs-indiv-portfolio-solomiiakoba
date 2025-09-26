package ua;

import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TqsCalculatorTest {

    // Don't use System.out.println, use a logger instead
    static final Logger log = org.slf4j.LoggerFactory.getLogger(lookup().lookupClass());

    @org.junit.jupiter.api.Test
    void add() {
        TqsCalculator t = new TqsCalculator();
        log.debug("Testing sum method in {}", t.getClass().getName());

        assertEquals(5, t.add(2, 3));
        assertEquals(-1, t.add(2, -3));
    }

    @org.junit.jupiter.api.Test
    void subtract() {
        TqsCalculator t = new TqsCalculator();
        log.debug("Testing subtract method in {}", t.getClass().getName());

        assertEquals(-1, t.subtract(2, 3));
        assertEquals(5, t.subtract(2, -3));
    }

    @DisplayName("Multiplies two numbers and returns the product")
    @org.junit.jupiter.api.Test
    void multiply() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(6, t.multiply(2, 3));
        assertEquals(-6, t.multiply(2, -3));
        assertEquals(0, t.multiply(2, 0));
    }

    @DisplayName("Divides two numbers and returns the quotient, excludes division by zero")
    @org.junit.jupiter.api.Test
    void divide() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(2, t.divide(6, 3));
        assertEquals(-2, t.divide(6, -3));

        assertThrows(IllegalArgumentException.class, () -> t.divide(2, 0));
    }

    @DisplayName("Divides two numbers with infinite quotients")
    @org.junit.jupiter.api.Test
    void divideWithInfiniteQuotients() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(1.1818, t.divide(13, 11), 0.0001);
    }
    @DisplayName("Exp of number")
    @org.junit.jupiter.api.Test
    void expOfNumber() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(2.7183, t.exp(1), 0.0001);
    }

    @DisplayName("Pow of number")
    @org.junit.jupiter.api.Test
    void powOfNumber() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(4, t.pow(2, 2));
    }

    @DisplayName("Sqrt of number")
    @org.junit.jupiter.api.Test
    void sqrtOfNumber() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(3, t.sqrt(9));
        assertThrows(IllegalArgumentException.class, () -> t.sqrt(-1));

    }

    @DisplayName("Remainder of division of numbers")
    @org.junit.jupiter.api.Test
    void remainderOfDivision() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(1, t.mod(10, 3));
        assertThrows(IllegalArgumentException.class, () -> t.mod(2, 0));
    }

    @DisplayName("Absolute value of number")
    @org.junit.jupiter.api.Test
    void absoluteValueOfNumber() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(10, t.abs(10));
        assertEquals(1, t.abs(-1));
    }

    @DisplayName("Fatorial of number")
    @org.junit.jupiter.api.Test
    void fatorialOfNumber() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(6, t.fatorial(3));
        assertEquals(1, t.fatorial(0));
        assertThrows(ArithmeticException.class, () -> t.fatorial(-1));

    }
    @DisplayName("Log of number")
    @org.junit.jupiter.api.Test
    void logOfNumber() {
        TqsCalculator t = new TqsCalculator();
        assertEquals(0, t.log(1));
    }
}