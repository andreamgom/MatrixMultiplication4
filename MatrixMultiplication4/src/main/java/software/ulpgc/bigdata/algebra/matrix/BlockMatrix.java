package software.ulpgc.bigdata.algebra.matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class BlockMatrix implements Matrix<Integer> {
    private final int rows;
    private final int columns;
    private final int blockSize;
    private final int[][] data;

    public BlockMatrix(int rows, int columns, int blockSize) {
        this.rows = rows;
        this.columns = columns;
        this.blockSize = blockSize;
        this.data = new int[rows][columns];
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public Integer get(int row, int col) {
        return data[row][col];
    }

    @Override
    public void set(int row, int col, Integer value) {
        data[row][col] = value;
    }

    @Override
    public Matrix<Integer> multiply(Matrix<Integer> other) {
        if (other.getRows() != columns) {
            throw new IllegalArgumentException("Incompatible matrix dimensions for multiplication");
        }

        BlockMatrix result = new BlockMatrix(rows, other.getColumns(), blockSize);

        int availableThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(availableThreads);
        Semaphore semaphore = new Semaphore(availableThreads);

        try {
            Future<?>[] futures = new Future<?>[rows / blockSize * other.getColumns() / blockSize];

            IntStream.range(0, rows / blockSize)
                    .parallel()
                    .forEach(iBlock -> {
                        IntStream.range(0, other.getColumns() / blockSize)
                                .parallel()
                                .forEach(jBlock -> {
                                    try {
                                        semaphore.acquire();
                                        int finalIBlock = iBlock;
                                        int finalJBlock = jBlock;
                                        futures[finalIBlock * (other.getColumns() / blockSize) + finalJBlock] = executorService.submit(() ->
                                                multiplyBlock(
                                                        this,
                                                        other,
                                                        result,
                                                        finalIBlock * blockSize,
                                                        finalJBlock * blockSize,
                                                        Math.min((finalIBlock + 1) * blockSize, rows),
                                                        Math.min((finalJBlock + 1) * blockSize, other.getColumns())
                                                )
                                        );
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } finally {
                                        semaphore.release();
                                    }
                                });
                    });

            for (Future<?> future : futures) {
                future.get();  // Esperar a que todas las tareas se completen
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        return result;
    }

    private void multiplyBlock(
            Matrix<Integer> a,
            Matrix<Integer> b,
            Matrix<Integer> c,
            int startRowA,
            int startColB,
            int endRowA,
            int endColB) {

        for (int i = startRowA; i < endRowA; i++) {
            for (int j = startColB; j < endColB; j++) {
                int sum = 0;
                for (int k = 0; k < columns; k++) {
                    sum += a.get(i, k) * b.get(k, j);
                }
                c.set(i, j, sum);
            }
        }
    }
}


