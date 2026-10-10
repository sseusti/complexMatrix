package complexmatrix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexMatrixTest {
    @Test
    void addsMatrices() {
        ComplexMatrix a = matrix(new double[][]{{1, 2}, {3, 4}});
        ComplexMatrix b = matrix(new double[][]{{5, 6}, {7, 8}});
        assertEquals(new ComplexNumber(6, 0), a.add(b).getValue(0, 0));
        assertEquals(new ComplexNumber(12, 0), a.add(b).getValue(1, 1));
    }

    @Test
    void multipliesCompatibleMatrices() {
        ComplexMatrix a = matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        ComplexMatrix b = matrix(new double[][]{{7, 8}, {9, 10}, {11, 12}});
        ComplexMatrix result = a.multiply(b);
        assertEquals(2, result.getRows());
        assertEquals(2, result.getCols());
        assertEquals(new ComplexNumber(58, 0), result.getValue(0, 0));
        assertEquals(new ComplexNumber(154, 0), result.getValue(1, 1));
    }

    @Test
    void computesTwoByTwoDeterminant() {
        ComplexMatrix matrix = matrix(new double[][]{{1, 2}, {3, 4}});
        assertEquals(-2.0, matrix.determinant().getReal(), 1e-12);
    }

    @Test
    void inverseMultipliedByOriginalIsIdentity() {
        ComplexMatrix matrix = matrix(new double[][]{{4, 7}, {2, 6}});
        ComplexMatrix product = matrix.multiply(matrix.inverse());
        assertEquals(1.0, product.getValue(0, 0).getReal(), 1e-10);
        assertEquals(0.0, product.getValue(0, 1).getReal(), 1e-10);
        assertEquals(0.0, product.getValue(1, 0).getReal(), 1e-10);
        assertEquals(1.0, product.getValue(1, 1).getReal(), 1e-10);
    }

    @Test
    void rejectsNonPositiveDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new ComplexMatrix(0, 2));
    }

    @Test
    void rejectsAdditionOfDifferentSizes() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComplexMatrix(1, 2).add(new ComplexMatrix(2, 1)));
    }

    private static ComplexMatrix matrix(double[][] values) {
        ComplexMatrix result = new ComplexMatrix(values.length, values[0].length);
        for (int row = 0; row < values.length; row++) {
            for (int col = 0; col < values[row].length; col++) {
                result.setValue(row, col, new ComplexNumber(values[row][col], 0));
            }
        }
        return result;
    }
}
