package com.bolin.graphics;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public class Sphere extends AbstractDrawable implements SphereInterface {

    private double radius;

    public Sphere(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

}
