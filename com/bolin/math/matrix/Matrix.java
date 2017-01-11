package com.bolin.math.matrix;

import com.bolin.exceptions.MatrixException;

/**
 * Created by jonb3_000 on 7/15/2016.
 */
public class Matrix<V extends MatrixValue> implements Cloneable {

    private V[][] values;
    public final int rows, cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        values = (V[][]) new MatrixValue[rows][cols];
    }

    public Matrix(V[][] values) {
        this.rows = values.length;
        this.cols = values[0].length;
        this.values = values;
    }

    private Matrix(V[][] values, boolean arrCpy) {
        this.rows = values.length;
        this.cols = values[0].length;
        if (arrCpy) {
            this.values = (V[][]) new MatrixValue[rows][cols];
            for (int row = 0;row < rows;row ++) {
                System.arraycopy(values[row], 0, this.values[row], 0, cols);
            }
        } else {
            this.values = values;
        }
    }

    public void scaleBy(V value) {
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                values[row][col].multiply(value);
            }
        }
    }

    public void divideBy(V value) {
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                values[row][col].divide(value);
            }
        }
    }

    public Matrix<V> inverse() {
        Matrix<V> adjugate = adjugate();
        adjugate.divideBy(deteminant());
        return adjugate;
    }

    public Matrix<V> adjugate() {
        Matrix<V> cofactor = cofactor();
        Matrix<V> adjugate = cofactor.transpose();
        return adjugate;
    }

    public Matrix<V> cofactor() {
        V[][] newMat = getNewMat(rows, cols);
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                if (row % 2 == col % 2) {
                    newMat[row][col] = cut(row, col).deteminant();
                } else {
                    newMat[row][col] = (V) values[row][col].zero();
                    newMat[row][col].subtract(cut(row, col).deteminant());
                }
            }
        }
        return new Matrix<>(newMat, false);
    }

    public V deteminant() {
        if (rows != cols) {
            throw new MatrixException("Cannot take determinant of non-square matrix:\t" + rows + "x" + cols);
        }
        if (rows == 2) {
            V a = (V) values[0][0].clone();
            V b = (V) values[0][1].clone();
            V c = (V) values[1][0].clone();
            V d = (V) values[1][1].clone();
            a.multiply(d);
            b.multiply(c);
            a.subtract(b);
            return a;
        }
        V result = (V) values[0][0].zero();
        for (int col = 0;col < cols;col ++) {
            V minor = cut(0, col).deteminant();
            if (col % 2 == 0) {
                result.add(minor);
            } else {
                result.subtract(minor);
            }
        }
        return result;
    }

    public Matrix<V> cut(int cutrow, int cutcol) {
        V[][] newMat = getNewMat(rows - 1, cols - 1);
        int r = 0;
        for (int row = 0;row < rows;row ++) {
            if (row != cutrow) {
                int c = 0;
                for (int col = 0; col < cols; col++) {
                    if (col != cutcol) {
                        newMat[r][c] = (V) values[row][col].clone();
                    }
                }
                r ++;
            }
        }
        return new Matrix(newMat, false);
    }

    public void put(int row, int col, V val) {
        values[row][col] = val;
    }

    public Matrix<V> transpose() {
        V[][] newMat = getNewMat(cols, rows);
        for (int i = 0;i < rows;i ++) {
            for (int j = 0;j < cols;j ++) {
                newMat[j][i] = values[i][j];
            }
        }
        return new Matrix<>(newMat, false);
    }

    public void add(Matrix<V> other) {
        if (other.rows != rows || other.cols != cols) {
            throw new MatrixException("Cannot add a " + other.rows + "x" + other.cols + " matrix to a " + rows + "x" + cols + " matrix");
        }
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                values[row][col].add(other.values[row][col]);
            }
        }
    }

    public void subtract(Matrix<V> other) {
        if (other.rows != rows || other.cols != cols) {
            throw new MatrixException("Cannot subtract a " + other.rows + "x" + other.cols + " matrix from a " + rows + "x" + cols + " matrix");
        }
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                values[row][col].subtract(other.values[row][col]);
            }
        }
    }

    public Matrix<V> multiply(Matrix<V> other) {
        if (other.rows != cols) {
            throw new MatrixException("Cannot multiply a " + rows + "x" + cols + " matrix by a " + other.rows + "x" + other.cols + " matrix");
        }
        V[][] newMat = getNewMat(rows, other.cols);
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < other.cols;col ++) {
                V sum = (V) values[row][0].zero();
                for (int i = 0;i < cols;i ++) {
                    V temp = (V) values[row][i].clone();
                    temp.multiply(other.values[i][col]);
                    sum.add(temp);
                }
                newMat[row][col] = sum;
            }
        }
        return new Matrix<>(newMat, false);
    }

    @Override
    public Matrix<V> clone() {
        return new Matrix<>(values, true);
    }

    private V[][] getNewMat(int rows, int cols) {
        return (V[][]) new MatrixValue[rows][cols];
    }

}
