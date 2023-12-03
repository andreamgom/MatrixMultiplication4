package software.ulpgc.bigdata.algebra.matrix;

public class MatrixMultiplicationBenchmark{
    public static void main(String[] args) {

        Matrix<Integer> matrixA = RandomMatrixGenerator.generateRandomMatrix(4, 4);
        Matrix<Integer> matrixB = RandomMatrixGenerator.generateRandomMatrix(4, 4);

        displayMatrix("Matrix A:", matrixA);
        displayMatrix("Matrix B:", matrixB);

        Matrix<Integer> result = multiplyMatrices(matrixA, matrixB);

        displayMatrix("Result Matrix:", result);

        benchmarkMultiplication(50);
        benchmarkMultiplication(100);
        benchmarkMultiplication(200);
        benchmarkMultiplication(300);
    }

    private static void displayMatrix(String message, Matrix<Integer> matrix) {
        System.out.println(message);
        MatrixUtils.printMatrix(matrix);
        System.out.println();
    }

    private static Matrix<Integer> multiplyMatrices(Matrix<Integer> matrixA, Matrix<Integer> matrixB) {
        return matrixA.multiply(matrixB);
    }

    private static void benchmarkMultiplication(int size) {
        Matrix<Integer> matrixA = RandomMatrixGenerator.generateRandomMatrix(size, size);
        Matrix<Integer> matrixB = RandomMatrixGenerator.generateRandomMatrix(size, size);

        long startTime = System.currentTimeMillis();


        matrixA.multiply(matrixB);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("Matrix multiplication for size " + size + " took " + elapsedTime + " milliseconds");
    }
}
