package complexmatrix;

import java.util.Locale;
import java.util.Scanner;

public final class Main {
    private static final Scanner SCANNER = new Scanner(System.in).useLocale(Locale.US);

    private Main() {
    }

    public static void main(String[] args) {
        ComplexMatrix a = readMatrix("A");
        ComplexMatrix b = readMatrix("B");

        while (true) {
            printMenu();
            int command = readInt("Command: ");

            if (command == 0) {
                System.out.println("Goodbye.");
                return;
            }

            try {
                switch (command) {
                    case 1 -> System.out.println(a.add(b));
                    case 2 -> System.out.println(a.subtract(b));
                    case 3 -> System.out.println(a.multiply(b));
                    case 4 -> System.out.println(a.divide(b));
                    case 5 -> System.out.println(a.transpose());
                    case 6 -> System.out.println(a.determinant());
                    case 7 -> {
                        a = readMatrix("A");
                        b = readMatrix("B");
                    }
                    default -> System.out.println("Unknown command. Choose an option from 0 to 7.");
                }
            } catch (IllegalArgumentException | ArithmeticException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    private static ComplexMatrix readMatrix(String name) {
        System.out.println("Matrix " + name + ": enter the number of rows and columns.");

        int rows;
        int cols;
        while (true) {
            rows = readInt("Rows: ");
            cols = readInt("Columns: ");
            if (rows > 0 && cols > 0) {
                break;
            }
            System.out.println("Dimensions must be positive numbers. Please try again.");
        }

        ComplexMatrix matrix = new ComplexMatrix(rows, cols);
        System.out.println("Enter the elements row by row, providing the real and imaginary parts for each.");

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double real = readDouble("Element [" + row + "][" + col + "], real part: ");
                double imaginary = readDouble("Element [" + row + "][" + col + "], imaginary part: ");
                matrix.setValue(row, col, new ComplexNumber(real, imaginary));
            }
        }
        return matrix;
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter an integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (!Double.isFinite(value)) {
                    System.out.println("Please enter a finite number (not NaN or Infinity).");
                    continue;
                }
                return value;
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a number. Use a period as the decimal separator.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1 - A + B");
        System.out.println("2 - A - B");
        System.out.println("3 - A * B");
        System.out.println("4 - A / B (A * inverse(B))");
        System.out.println("5 - Transpose A");
        System.out.println("6 - Determinant of A");
        System.out.println("7 - Enter matrices again");
        System.out.println("0 - Exit");
    }
}
