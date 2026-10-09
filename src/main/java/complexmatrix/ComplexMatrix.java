package complexmatrix;

import java.util.Objects;

public final class ComplexMatrix {
    private static final double PIVOT_EPSILON = 1e-12;

    private final ComplexNumber[][] matrix;
    private final int rows;
    private final int cols;

    public ComplexMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Размеры матрицы должны быть положительными");
        }

        this.rows = rows;
        this.cols = cols;
        this.matrix = new ComplexNumber[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                matrix[row][col] = ComplexNumber.ZERO;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void setValue(int row, int col, ComplexNumber value) {
        checkIndex(row, col);
        matrix[row][col] = Objects.requireNonNull(value, "value must not be null");
    }

    public ComplexNumber getValue(int row, int col) {
        checkIndex(row, col);
        return matrix[row][col];
    }

    public ComplexMatrix add(ComplexMatrix other) {
        requireSameDimensions(other, "сложения");
        ComplexMatrix result = new ComplexMatrix(rows, cols);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                result.matrix[row][col] = matrix[row][col].add(other.matrix[row][col]);
            }
        }
        return result;
    }

    public ComplexMatrix subtract(ComplexMatrix other) {
        requireSameDimensions(other, "вычитания");
        ComplexMatrix result = new ComplexMatrix(rows, cols);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                result.matrix[row][col] = matrix[row][col].subtract(other.matrix[row][col]);
            }
        }
        return result;
    }

    public ComplexMatrix multiply(ComplexMatrix other) {
        Objects.requireNonNull(other, "other must not be null");
        if (cols != other.rows) {
            throw new IllegalArgumentException(
                    "Для умножения число столбцов первой матрицы должно равняться числу строк второй"
            );
        }

        ComplexMatrix result = new ComplexMatrix(rows, other.cols);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < other.cols; col++) {
                ComplexNumber sum = ComplexNumber.ZERO;
                for (int k = 0; k < cols; k++) {
                    sum = sum.add(matrix[row][k].multiply(other.matrix[k][col]));
                }
                result.matrix[row][col] = sum;
            }
        }
        return result;
    }

    public ComplexMatrix transpose() {
        ComplexMatrix result = new ComplexMatrix(cols, rows);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                result.matrix[col][row] = matrix[row][col];
            }
        }
        return result;
    }

    public ComplexNumber determinant() {
        requireSquare();

        ComplexNumber[][] work = copyData();
        ComplexNumber determinant = ComplexNumber.ONE;
        int sign = 1;

        for (int pivotIndex = 0; pivotIndex < rows; pivotIndex++) {
            int pivotRow = findPivotRow(work, pivotIndex, false);
            if (work[pivotRow][pivotIndex].isZero()) {
                return ComplexNumber.ZERO;
            }

            if (pivotRow != pivotIndex) {
                swapRows(work, pivotRow, pivotIndex);
                sign = -sign;
            }

            ComplexNumber pivot = work[pivotIndex][pivotIndex];
            determinant = determinant.multiply(pivot);

            for (int row = pivotIndex + 1; row < rows; row++) {
                ComplexNumber factor = work[row][pivotIndex].divide(pivot);
                for (int col = pivotIndex + 1; col < cols; col++) {
                    work[row][col] = work[row][col].subtract(factor.multiply(work[pivotIndex][col]));
                }
                work[row][pivotIndex] = ComplexNumber.ZERO;
            }
        }

        return sign < 0
                ? new ComplexNumber(-determinant.getReal(), -determinant.getImaginary())
                : determinant;
    }


    public ComplexMatrix inverse() {
        requireSquare();

        ComplexNumber[][] left = copyData();
        ComplexNumber[][] right = new ComplexMatrix(rows, cols).matrix;

        for (int i = 0; i < rows; i++) {
            right[i][i] = ComplexNumber.ONE;
        }

        for (int pivotIndex = 0; pivotIndex < rows; pivotIndex++) {
            int pivotRow = findPivotRow(left, pivotIndex, true);
            ComplexNumber pivot = left[pivotRow][pivotIndex];

            if (isNumericallyZero(pivot, left[pivotRow])) {
                throw new ArithmeticException("Матрица вырождена или численно близка к вырожденной");
            }

            if (pivotRow != pivotIndex) {
                swapRows(left, pivotRow, pivotIndex);
                swapRows(right, pivotRow, pivotIndex);
            }

            pivot = left[pivotIndex][pivotIndex];
            for (int col = 0; col < cols; col++) {
                left[pivotIndex][col] = left[pivotIndex][col].divide(pivot);
                right[pivotIndex][col] = right[pivotIndex][col].divide(pivot);
            }

            for (int row = 0; row < rows; row++) {
                if (row == pivotIndex) {
                    continue;
                }

                ComplexNumber factor = left[row][pivotIndex];
                for (int col = 0; col < cols; col++) {
                    left[row][col] = left[row][col]
                            .subtract(factor.multiply(left[pivotIndex][col]));
                    right[row][col] = right[row][col]
                            .subtract(factor.multiply(right[pivotIndex][col]));
                }
                left[row][pivotIndex] = ComplexNumber.ZERO;
            }
        }

        ComplexMatrix result = new ComplexMatrix(rows, cols);
        for (int row = 0; row < rows; row++) {
            System.arraycopy(right[row], 0, result.matrix[row], 0, cols);
        }
        return result;
    }

    public ComplexMatrix divide(ComplexMatrix other) {
        Objects.requireNonNull(other, "other must not be null");
        return multiply(other.inverse());
    }

    private void requireSameDimensions(ComplexMatrix other, String operation) {
        Objects.requireNonNull(other, "other must not be null");
        if (rows != other.rows || cols != other.cols) {
            throw new IllegalArgumentException(
                    "Для " + operation + " матрицы должны иметь одинаковые размеры"
            );
        }
    }

    private void requireSquare() {
        if (rows != cols) {
            throw new IllegalArgumentException("Операция определена только для квадратной матрицы");
        }
    }

    private void checkIndex(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Индекс элемента матрицы вне допустимых границ");
        }
    }

    private ComplexNumber[][] copyData() {
        ComplexNumber[][] copy = new ComplexNumber[rows][cols];
        for (int row = 0; row < rows; row++) {
            System.arraycopy(matrix[row], 0, copy[row], 0, cols);
        }
        return copy;
    }

    private static int findPivotRow(
            ComplexNumber[][] data,
            int pivotIndex,
            boolean scaled
    ) {
        int bestRow = pivotIndex;
        double bestScore = -1.0;

        for (int row = pivotIndex; row < data.length; row++) {
            double magnitude = data[row][pivotIndex].abs();
            double scale = scaled ? rowScale(data[row]) : 1.0;
            double score = scale == 0.0 ? magnitude : magnitude / scale;

            if (score > bestScore) {
                bestScore = score;
                bestRow = row;
            }
        }
        return bestRow;
    }

    private static double rowScale(ComplexNumber[] row) {
        double scale = 0.0;
        for (ComplexNumber value : row) {
            scale = Math.max(scale, value.abs());
        }
        return scale;
    }

    private static boolean isNumericallyZero(ComplexNumber pivot, ComplexNumber[] row) {
        double scale = rowScale(row);
        return pivot.abs() <= PIVOT_EPSILON * scale;
    }

    private static void swapRows(ComplexNumber[][] data, int first, int second) {
        ComplexNumber[] temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (col > 0) {
                    output.append('\t');
                }
                output.append(matrix[row][col]);
            }
            if (row < rows - 1) {
                output.append(System.lineSeparator());
            }
        }
        return output.toString();
    }
}
