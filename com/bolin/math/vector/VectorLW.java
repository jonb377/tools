package com.bolin.math.vector;

import com.bolin.exceptions.VectorException;
import com.bolin.math.matrix.RealMatrix;

/**
 * Lightweight vector. Does not calculate angles or magnitude.
 * Created by jonb3_000 on 7/15/2016.
 */
public class VectorLW extends SimpleVector {

    public static VectorLW normal(VectorLW[] vectors) {
        VectorLW base = vectors[0];
        if (vectors.length != base.dimensions - 1) {
            throw new VectorException("Cannot find the common normal of " + (vectors.length) + " vectors in " + base.dimensions + "D space.");
        }
        for (SimpleVector v : vectors) {
            if (v.dimensions != vectors[0].dimensions) {
                throw new VectorException("Cannot find the common normal of vector with dimension " + v.dimensions + " in " + base.dimensions + "D space");
            }
        }
        if (base.dimensions == 2) {
            VectorLW v3d = new VectorLW(base.values[0], base.values[1], 0);
            VectorLW k = new VectorLW(0, 0, 1);
            VectorLW norm = v3d.normal(k);
            return new VectorLW(norm.values[0], norm.values[1]);
        } else {
            double[][] values = new double[vectors.length + 1][];
            values[0] = new double[base.dimensions];
            for (int i = 0; i < vectors.length; i++) {
                values[i + 1] = vectors[i].values;
            }
            RealMatrix mat = new RealMatrix(values);
            VectorLW result = new VectorLW(values[0]);
            for (int i = 0; i < result.dimensions; i++) {
                double sign = 1;
                if (i % 2 == 1) {
                    sign = -1;
                }
                result.values[i] = mat.cut(0, i).determinant() * sign;
            }
            return result;
        }
    }

    private double magnitude = Double.NaN;

    public VectorLW(double... values) {
        super(values);
    }

    public VectorLW(SimpleVector other) {
        super(other.values, ARR_CPY);
    }

    protected VectorLW(double[] values, int opCode) {
        super(values, opCode);
    }

    public double[] getValues() {
        return values;
    }

    public VectorLW normal(SimpleVector... vectors) {
        if (vectors.length != dimensions - 2) {
            throw new VectorException("Cannot find the common normal of " + (vectors.length + 1) + " vectors in " + dimensions + "D space.");
        }
        for (SimpleVector v : vectors) {
            if (v.dimensions != dimensions) {
                throw new VectorException("Cannot find the common normal of vector with dimension " + v.dimensions + " in " + dimensions + "D space");
            }
        }
        if (dimensions == 2) {
            VectorLW v3d = new VectorLW(values[0], values[1], 0);
            VectorLW k = new VectorLW(0, 0, 1);
            VectorLW norm = v3d.normal(k);
            return new VectorLW(norm.values[0], norm.values[1]);
        } else {
            double[][] values = new double[vectors.length + 2][];
            values[0] = new double[dimensions];
            values[1] = this.values;
            for (int i = 0; i < vectors.length; i++) {
                values[i + 2] = vectors[i].values;
            }
            RealMatrix mat = new RealMatrix(values);
            VectorLW result = new VectorLW(values[0]);
            for (int i = 0; i < result.dimensions; i++) {
                double sign = 1;
                if (i % 2 == 1) {
                    sign = -1;
                }
                result.values[i] = mat.cut(0, i).determinant() * sign;
            }
            return result;
        }
    }

    @Override
    public VectorLW clone() {
        return new VectorLW(this);
    }

    @Override
    protected void update() {
        magnitude = Double.NaN;
    }

    @Override
    public VectorLW proj(SimpleVector other) {
        if (dimensions != other.dimensions) {
            throw new VectorException("Cannot project a " + dimensions + "D vector onto a " + other.dimensions + "D vector.");
        }
        VectorLW b = ((VectorLW) other).clone();
        b.unitize();
        b.scaleBy(dot(b));
        return b;
    }

    @Override
    public VectorLW rejection(SimpleVector other) {
        if (dimensions != other.dimensions) {
            throw new VectorException("Cannot reject a " + dimensions + "D vector from a " + other.dimensions + "D vector.");
        }
        VectorLW proj = proj(other);
        proj.subtract(this);
        proj.scaleBy(-1);
        return proj;
    }

    @Override
    public double magnitude() {
        if (Double.isNaN(magnitude)) {
            magnitude = super.magnitude();
        }
        return magnitude;
    }
}
