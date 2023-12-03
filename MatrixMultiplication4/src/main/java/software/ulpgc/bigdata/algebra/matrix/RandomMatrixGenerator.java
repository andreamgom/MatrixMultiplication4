package software.ulpgc.bigdata.algebra.matrix;

import java.util.Random;

public class RandomMatrixGenerator {
    private static final Random random = new Random();

    public static Matrix<Integer> generateRandomMatrix(int rows, int columns) {
        Matrix<Integer> matrix = new BlockMatrix(rows, columns, 2);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix.set(i, j, random.nextInt(10));
            }
        }
        return matrix;
    }
}

