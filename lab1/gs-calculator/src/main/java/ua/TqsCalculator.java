package ua;

/**
 * Basic calculator class with methods for addition, subtraction, multiplication, and division.
 */
public class TqsCalculator {

    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return a / b;
    }
    public double exp(double a) { return Math.exp(a); }
    public double pow(double a, double b) { return Math.pow(a, b); }
    public double sqrt(double a) {
        if (a < 0) throw new IllegalArgumentException("Cannot take sqrt of negative number.");
        return Math.sqrt(a);
    }
    public double mod (double a, double b) {
        if (b == 0) throw new IllegalArgumentException("Modulo by zero is not allowed.");
        return a % b;
    }
    public double abs (double a) { return Math.abs(a); }
    public double log (double a) {
        if (a <= 0) throw new IllegalArgumentException("Logarithm undefined for zero or negative numbers.");
        return Math.log(a);
    }
    public double fatorial (double a) {
        if (a < 0 ) {
            throw new ArithmeticException("Factorial can not be defined for negative numbers.");
        }
        if (a == 1 || a == 0) {
            return 1;
        }
        return a*fatorial(a-1);
    }
}
