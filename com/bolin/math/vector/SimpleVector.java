package com.bolin.math.vector;

import com.bolin.exceptions.VectorException;
import com.bolin.math.matrix.RealMatrix;

/**
 * Created by jonb3_000 on 7/15/2016.
 */
public abstract class SimpleVector implements Cloneable{

    protected static final int ARR_CPY = 1;

    protected final double[] values;
    public final int dimensions;

    public SimpleVector(double... values) {
        dimensions = values.length;
        this.values = new double[dimensions];
        System.arraycopy(values, 0, this.values, 0, dimensions);
        update();
    }

    protected SimpleVector(double[] values, int opCode) {
        dimensions = values.length;
        switch(opCode) {
            case ARR_CPY:
                this.values = new double[dimensions];
                System.arraycopy(values, 0, this.values, 0, dimensions);
                update();
                break;
            default:
                this.values = values;
                update();
        }
    }

    protected SimpleVector(SimpleVector other) {
        dimensions = other.values.length;
        values = new double[dimensions];
        System.arraycopy(other.values, 0, values, 0, dimensions);
    }

    /**
     * Evaluates the dot product between the two vectors, or throws VectorException if the two vectors are of different dimension.
     * @param other
     * @return
     */
    public double dot(SimpleVector other) {
        if (other.dimensions != this.dimensions) {
            throw new VectorException("Cannot dot vectors with different dimensions:\t" + dimensions + "\t" + other.dimensions);
        }
        double result = 0;
        for (int i = 0;i < dimensions;i ++) {
            result += values[i] * other.values[i];
        }
        return result;
    }

    /**
     * @return Returns the magnitude of this vector.
     */
    public double magnitude() {
        double sqrSum = 0;
        for (double d : values) {
            sqrSum += d * d;
        }
        return Math.sqrt(sqrSum);
    }

    /**
     * Turns this vector into a unit vector.
     */
    public void unitize() {
        double magnitude = magnitude();
        if (magnitude == 0) {
            return;
        }
        for (int i = 0;i < dimensions;i ++) {
            values[i] /= magnitude;
        }
        update();
    }

    public boolean isParallel(SimpleVector other) {
        if (other.dimensions != dimensions) {
            throw new VectorException("Cannot compare two vectors of different dimension:\t" + dimensions + "\t" + other.dimensions);
        }
        return Math.abs(dot(other)) == other.magnitude() * magnitude();
    }

    /**
     * Adds the other vector to this one.
     * @param other The vector to be added.
     */
    public void add(SimpleVector other) {
        if (dimensions != other.dimensions) {
            throw new VectorException("Cannot add two vectors of different dimension.");
        }
        for (int i = 0;i < dimensions;i ++) {
            values[i] += other.values[i];
        }
        update();
    }

    /**
     * Subtracts the other vector from this one.
     * @param other The other vector.
     */
    public void subtract(SimpleVector other) {
        if (dimensions != other.dimensions) {
            throw new VectorException("Cannot add two vectors of different dimension.");
        }
        for (int i = 0;i < dimensions;i ++) {
            values[i] -= other.values[i];
        }
        update();
    }

    /**
     * Scales this vector by amt.
     * @param amt The amount to scale by.
     */
    public void scaleBy(double amt) {
        for (int i = 0;i < dimensions;i ++) {
            values[i] *= amt;
        }
        update();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SimpleVector) {
            SimpleVector vect = (SimpleVector) other;
            if (vect.dimensions == dimensions) {
                for (int i = 0;i < dimensions;i ++) {
                    if (Math.abs(values[i] - vect.values[i]) > 1E-10) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void multiply(RealMatrix mat) {
        if (mat.rows != dimensions || mat.cols != dimensions) {
            throw new VectorException("Cannot multiply a " + dimensions + "D vector by a " + mat.rows + "x" + mat.cols + " matrix.");
        }
        double[] old = new double[dimensions];
        System.arraycopy(values, 0, old, 0, dimensions);
        for (int row = 0;row < dimensions;row ++) {
            double sum = 0;
            for (int i = 0;i < dimensions;i ++) {
                sum += old[i] * mat.valueAt(row, i);
            }
            values[row] = sum;
        }
    }

    /**
     * Clones this vector.
     * @return A new vector with identical components to this.
     */
    public abstract SimpleVector clone();

    /**
     * Calculates the projection of this vector onto other.
     * @param other The other vector.
     * @return Returns the projected vector. This will be parallel to other.
     */
    public abstract SimpleVector proj(SimpleVector other);

    /**
     * Returns the rejection of this vector from other.
     * This is equal to this - proj(other)
     * @param other The other vector.
     * @return The rejected vector. That will be parallel to this.
     */
    public abstract SimpleVector rejection(SimpleVector other);

    /**
     * Called when the values in this vector change.
     */
    protected abstract void update();

    @Override
    public String toString() {
        String s = "<";
        for (int i = 0;i < dimensions - 1;i ++) {
            s += values[i] + ", ";
        }
        s += values[dimensions - 1] + ">";
        return s;
    }

}
