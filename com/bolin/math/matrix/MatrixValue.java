package com.bolin.math.matrix;

/**
 * Created by jonb3_000 on 7/15/2016.
 */
public interface MatrixValue extends Cloneable {

    void add(MatrixValue other);

    void subtract(MatrixValue other);

    void multiply(MatrixValue other);

    void divide(MatrixValue other);

    boolean equals(MatrixValue other);

    MatrixValue zero();

    MatrixValue clone();

}
