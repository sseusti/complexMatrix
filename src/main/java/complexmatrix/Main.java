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
            int command = readInt("Команда: ");

            if (command == 0) {
                System.out.println("Работа завершена.");
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
                    default -> System.out.println("Неизвестная команда. Выберите пункт от 0 до 7.");
                }
            } catch (IllegalArgumentException | ArithmeticException exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            }
        }
    }

    private static ComplexMatrix readMatrix(String name) {
        System.out.println("Матрица " + name + ": введите число строк и столбцов.");

        int rows;
        int cols;
        while (true) {
            rows = readInt("Строки: ");
            cols = readInt("Столбцы: ");
            if (rows > 0 && cols > 0) {
                break;
            }
            System.out.println("Размеры должны быть положительными числами. Попробуйте ещё раз.");
        }

        ComplexMatrix matrix = new ComplexMatrix(rows, cols);
        System.out.println("Введите элементы построчно: действительная и мнимая части через пробел.");

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double real = readDouble("Элемент [" + row + "][" + col + "], действительная часть: ");
                double imaginary = readDouble("Элемент [" + row + "][" + col + "], мнимая часть: ");
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
                System.out.println("Нужно ввести целое число.");
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
                    System.out.println("Введите конечное число, не NaN и не Infinity.");
                    continue;
                }
                return value;
            } catch (NumberFormatException exception) {
                System.out.println("Нужно ввести число. Для дробной части используйте точку.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1 — A + B");
        System.out.println("2 — A - B");
        System.out.println("3 — A * B");
        System.out.println("4 — A / B (A * inverse(B))");
        System.out.println("5 — транспонировать A");
        System.out.println("6 — определитель A");
        System.out.println("7 — ввести матрицы заново");
        System.out.println("0 — выход");
    }
}
