package com.bolin.math.vector;

import com.bolin.exceptions.VectorException;

/**
 * Created by jonb3_000 on 7/15/2016.
 */
public class Vector3D extends SimpleVector{

    protected double magnitude;

    public Vector3D(double x, double y, double z) {
        super(x, y, z);
    }

    public Vector3D(Vector3D other) {
        super(other);
        this.magnitude = other.magnitude;
    }

    public double x() {
        return values[0];
    }

    public double y() {
        return values[1];
    }

    public double z() {
        return values[2];
    }

    @Override
    public double magnitude() {
        return magnitude;
    }

    public void addPolar(double magnitude, double theta, double phi) {
        double dx = magnitude * Math.cos(theta) * Math.sin(phi);
        double dy = magnitude * Math.sin(theta) * Math.sin(phi);
        double dz = magnitude * Math.cos(phi);
        values[0] += dx;
        values[1] += dy;
        values[2] += dz;
        update();
    }

    public Vector3D cross(Vector3D other) {
        double x = values[1] * other.values[2] - values[2] * other.values[1];
        double y = values[2] * other.values[0] - values[0] * other.values[2];
        double z = values[0] * other.values[1] - values[1] * other.values[0];
        return new Vector3D(x, y, z);
    }

    @Override
    public Vector3D clone() {
        return new Vector3D(this);
    }

    @Override
    public Vector3D proj(SimpleVector other) {
        if (!(other instanceof Vector3D)) {
            throw new VectorException("Cannot project a 3D vector onto a non-3D vector.");
        }
        Vector3D b = ((Vector3D) other).clone();
        b.unitize();
        b.scaleBy(dot(b));
        return b;
    }

    @Override
    public Vector3D rejection(SimpleVector other) {
        if (!(other instanceof Vector3D)) {
            throw new VectorException("Cannot reject a 3D vector onto a non-3D vector.");
        }
        Vector3D proj = proj(other);
        proj.subtract(this);
        proj.scaleBy(-1);
        return proj;
    }

    @Override
    protected void update() {
        magnitude = Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]);
    }
}
