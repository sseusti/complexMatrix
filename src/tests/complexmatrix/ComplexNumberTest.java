package complexmatrix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNumberTest {
    @Test
    void addsComplexNumbers() {
        assertEquals(new ComplexNumber(4, 6),
                new ComplexNumber(1, 2).add(new ComplexNumber(3, 4)));
    }

    @Test
    void multipliesComplexNumbers() {
        assertEquals(new ComplexNumber(-5, 10),
                new ComplexNumber(1, 2).multiply(new ComplexNumber(3, 4)));
    }

    @Test
    void dividesComplexNumbers() {
        ComplexNumber result = new ComplexNumber(1, 2).divide(new ComplexNumber(3, 4));
        assertEquals(0.44, result.getReal(), 1e-12);
        assertEquals(0.08, result.getImaginary(), 1e-12);
    }

    @Test
    void divisionByZeroThrows() {
        assertThrows(ArithmeticException.class,
                () -> ComplexNumber.ONE.divide(ComplexNumber.ZERO));
    }
}
