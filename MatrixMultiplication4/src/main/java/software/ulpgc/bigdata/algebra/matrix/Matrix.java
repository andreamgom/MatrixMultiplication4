package software.ulpgc.bigdata.algebra.matrix;

public interface Matrix<T> {
    int getRows();

    int getColumns();

    T get(int row, int col);

    void set(int row, int col, T value);

    Matrix<T> multiply(Matrix<T> other);
}


