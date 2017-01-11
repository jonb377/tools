package com.bolin.math.matrix;

import com.bolin.exceptions.MatrixException;

/**
 * Created by jonb3_000 on 7/21/2016.
 */
public class RealMatrix implements Cloneable {

    public final int rows, cols;

    private double[][] values;

    public RealMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        values = new double[rows][cols];
    }

    public RealMatrix(double[][] v) {
        this.values = v;
        rows = values.length;
        cols = values[0].length;
    }

    public double determinant() {
        if (rows != cols) {
            throw new MatrixException("Cannot take determinant of non-square matrix: " + rows + "x" + cols);
        }
        if (rows <= 2) {
            if (rows == 2) {
                return values[0][0] * values[1][1] - values[1][0] * values[0][1];
            } else if (rows == 1) {
                return values[0][0];
            }
        }
        double det = 0;
        for (int col = 0;col < cols;col ++) {
            double sign = 1;
            if (col % 2 == 1) {
                sign = -1;
            }
            double minor = cut(0, col).determinant() * sign;
            det += values[0][col] * minor;
        }
        return det;
    }

    public RealMatrix cut(int cutrow, int cutcol) {
        double[][] newMat = getNewMat(rows - 1, cols - 1);
        int r = 0;
        for (int row = 0;row < rows;row ++) {
            if (row != cutrow) {
                int c = 0;
                for (int col = 0;col < cols;col ++) {
                    if (col != cutcol) {
                        newMat[r][c] = values[row][col];
                        c ++;
                    }
                }
                r ++;
            }
        }
        return new RealMatrix(newMat);
    }

    public void scaleBy(double d) {
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                values[row][col] *= d;
            }
        }
    }

    public RealMatrix multiply(RealMatrix other) {
        if (cols != other.rows) {
            throw new MatrixException("Cannot multiply " + rows + "x" + cols + " matrix by " + other.rows + "x" + other.cols + " matrix.");
        }
        double[][] newMat = getNewMat(rows, other.cols);
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < other.cols;col ++) {
                double sum = 0;
                for (int i = 0;i < cols;i ++) {
                    sum += values[row][i] * other.values[i][col];
                }
                newMat[row][col] = sum;
            }
        }
        return new RealMatrix(newMat);
    }

    public void add(RealMatrix other) {
        if (other.rows != rows || other.cols != cols) {
            throw new MatrixException("Cannot add " + other.rows + "x" + other.cols + " matrix to " + rows + "x" + cols + " matrix.");
        }
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                values[row][col] += other.values[row][col];
            }
        }
    }

    public void subtract(RealMatrix other) {
        if (other.rows != rows || other.cols != cols) {
            throw new MatrixException("Cannot subtract " + other.rows + "x" + other.cols + " matrix from " + rows + "x" + cols + " matrix.");
        }
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                values[row][col] -= other.values[row][col];
            }
        }
    }

    public RealMatrix transpose() {
        double[][] newMat = getNewMat(cols, rows);
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                newMat[col][row] = values[row][col];
            }
        }
        return new RealMatrix(newMat);
    }

    public RealMatrix cofactor() {
        if (rows == 1 && cols == 1) {
            return this.clone();
        }
        double[][] newMat = getNewMat(rows, cols);
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                RealMatrix cut = cut(row, col);
                double sign = 1;
                if (row % 2 != col % 2) {
                    sign = -1;
                }
                newMat[row][col] = cut.determinant() * sign;
            }
        }
        return new RealMatrix(newMat);
    }

    public RealMatrix adjugate(){
        RealMatrix cofactor = cofactor();
        return cofactor.transpose();
    }

    public RealMatrix inverse() {
        RealMatrix adjugate = adjugate();
        adjugate.scaleBy(1 / determinant());
        return adjugate;
    }

    public String toString() {
        String s = "{{";
        for (int row = 0;row < rows;row ++) {
            for (int col = 0;col < cols;col ++) {
                String curr = values[row][col] + "";
                if (col != cols - 1) {
                    curr += ",";
                    while (curr.length() < 8) {
                        curr += " ";
                    }
                }
                s += curr;
                if (col != cols - 1) {
                    s += "\t";
                }
            }
            if (row != rows - 1) {
                s += "},\n{";
            }
        }
        s += "}}";
        return s;
    }

    public double[][] getNewMat(int rows, int cols) {
        return new double[rows][cols];
    }

    public double valueAt(int row, int col) {
        return values[row][col];
    }

    @Override
    public RealMatrix clone() {
        double[][] newMat = getNewMat(rows, cols);
        for (int row = 0;row < rows;row ++) {
            System.arraycopy(values[row], 0, newMat[row], 0, cols);
        }
        return new RealMatrix(newMat);
    }
}
