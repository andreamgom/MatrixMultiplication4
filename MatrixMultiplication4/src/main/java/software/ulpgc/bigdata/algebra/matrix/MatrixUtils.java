package software.ulpgc.bigdata.algebra.matrix;

public class MatrixUtils {
    public static void printMatrix(Matrix<Integer> matrix) {
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                System.out.print(matrix.get(i, j) + " ");
            }
            System.out.println();
        }
    }
}
