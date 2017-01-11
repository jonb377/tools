package com.bolin.math.vector;

import com.bolin.exceptions.VectorException;

/**
 * Created by jonb3_000 on 7/15/2016.
 */
public class Vector2D extends SimpleVector {

    private double magnitude, theta;

    public Vector2D(double x, double y) {
        super(x, y);
    }

    public Vector2D(Vector2D other) {
        super(other);
        this.magnitude = other.magnitude;
        this.theta = other.theta;
    }

    protected void update() {
        magnitude = Math.sqrt(values[0] * values[0] + values[1] * values[1]);
        theta = Math.atan2(values[1], values[0]);
    }

    @Override
    public double magnitude() {
        return magnitude;
    }

    public double theta() {
        return theta;
    }

    public double x() {
        return values[0];
    }

    public double y() {
        return values[1];
    }

    public void addPolar(double magnitude, double angle) {
        double dx = magnitude * Math.cos(angle);
        double dy = magnitude * Math.sin(angle);
        values[0] += dx;
        values[1] += dy;
        update();
    }

    @Override
    public Vector2D clone() {
        return new Vector2D(this);
    }

    @Override
    public Vector2D proj(SimpleVector other) {
        if (!(other instanceof Vector2D)) {
            throw new VectorException("Cannot project a 2D vector onto a non-2D vector.");
        }
        Vector2D b = ((Vector2D) other).clone();
        b.unitize();
        b.scaleBy(dot(b));
        return b;
    }

    @Override
    public Vector2D rejection(SimpleVector other) {
        if (!(other instanceof Vector2D)) {
            throw new VectorException("Cannot reject a 2D vector onto a non-2D vector.");
        }
        Vector2D proj = proj(other);
        proj.subtract(this);
        proj.scaleBy(-1);
        return proj;
    }
}
