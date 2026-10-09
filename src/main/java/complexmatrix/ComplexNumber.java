package complexmatrix;

import java.util.Objects;

public final class ComplexNumber {
    public static final ComplexNumber ZERO = new ComplexNumber(0.0, 0.0);
    public static final ComplexNumber ONE = new ComplexNumber(1.0, 0.0);

    private final double real;
    private final double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public ComplexNumber add(ComplexNumber other) {
        Objects.requireNonNull(other, "other must not be null");
        return new ComplexNumber(real + other.real, imaginary + other.imaginary);
    }

    public ComplexNumber subtract(ComplexNumber other) {
        Objects.requireNonNull(other, "other must not be null");
        return new ComplexNumber(real - other.real, imaginary - other.imaginary);
    }

    public ComplexNumber multiply(ComplexNumber other) {
        Objects.requireNonNull(other, "other must not be null");
        return new ComplexNumber(
                real * other.real - imaginary * other.imaginary,
                real * other.imaginary + imaginary * other.real
        );
    }

    public ComplexNumber divide(ComplexNumber other) {
        Objects.requireNonNull(other, "other must not be null");

        double c = other.real;
        double d = other.imaginary;
        if (c == 0.0 && d == 0.0) {
            throw new ArithmeticException("Нельзя делить на нулевое комплексное число");
        }

        double resultReal;
        double resultImaginary;

        if (Math.abs(c) >= Math.abs(d)) {
            double ratio = d / c;
            double denominator = c + d * ratio;
            resultReal = (real + imaginary * ratio) / denominator;
            resultImaginary = (imaginary - real * ratio) / denominator;
        } else {
            double ratio = c / d;
            double denominator = d + c * ratio;
            resultReal = (real * ratio + imaginary) / denominator;
            resultImaginary = (imaginary * ratio - real) / denominator;
        }

        return new ComplexNumber(resultReal, resultImaginary);
    }

    public double abs() {
        return Math.hypot(real, imaginary);
    }

    public boolean isZero() {
        return real == 0.0 && imaginary == 0.0;
    }

    @Override
    public String toString() {
        if (imaginary == 0.0) {
            return format(real);
        }
        if (real == 0.0) {
            return format(imaginary) + "i";
        }
        String sign = imaginary > 0.0 ? "+" : "";
        return format(real) + sign + format(imaginary) + "i";
    }

    private static String format(double value) {
        return Double.toString(value == 0.0 ? 0.0 : value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ComplexNumber other)) {
            return false;
        }
        return Double.compare(real, other.real) == 0
                && Double.compare(imaginary, other.imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }
}
