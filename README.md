# Complex Matrix Calculator

A Java 17 console application for performing operations on matrices with complex-number entries.

## Features

- Add and subtract matrices of the same dimensions.
- Multiply compatible matrices.
- Divide matrix `A` by matrix `B`, calculated as `A × inverse(B)`.
- Transpose a matrix and calculate its determinant.
- Calculate a matrix inverse through the `ComplexMatrix` API.
- Validate matrix dimensions and handle invalid console input.
- Run unit tests with JUnit 5.

## Requirements

- JDK 17 or later
- Maven 3.8 or later

## Build and run

Run the test suite:

```bash
mvn test
```

Build the project:

```bash
mvn package
```

Launch the console application after building:

```bash
java -cp target/classes complexmatrix.Main
```

## Using the console application

On startup, enter the number of rows and columns for matrices `A` and `B`. Then enter each matrix element as two values: its real part followed by its imaginary part. Use a decimal point for fractional values, for example `2.5`.

The menu provides addition, subtraction, multiplication, division (`A × inverse(B)`), transposition of `A`, determinant calculation for `A`, and an option to enter both matrices again. Enter `0` to exit. Division requires `B` to be square and invertible; determinant calculation requires `A` to be square.

## Project structure

- `src/main/java/complexmatrix/ComplexNumber.java` — immutable complex-number value type and arithmetic operations.
- `src/main/java/complexmatrix/ComplexMatrix.java` — matrix storage and operations, including inverse calculation.
- `src/main/java/complexmatrix/Main.java` — interactive console interface.
- `src/tests/complexmatrix/` — JUnit 5 tests.

Matrix calculations use `double` values, so floating-point rounding errors are possible. The inverse operation uses a scale-aware numerical threshold to detect singular or near-singular matrices.
