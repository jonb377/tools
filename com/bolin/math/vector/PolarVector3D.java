package com.bolin.math.vector;

/**
 * Created by jonb3_000 on 7/20/2016.
 */
public class PolarVector3D extends Vector3D {

    private double theta, phi;

    public PolarVector3D(double x, double y, double z) {
        super(x, y, z);
        update();
    }

    public PolarVector3D(double magnitude, double theta, double phi, boolean polar) {
        super(magnitude * Math.cos(theta) * Math.sin(phi), magnitude * Math.sin(theta) * Math.sin(phi), magnitude * Math.cos(phi));
        this.magnitude = magnitude;
        this.theta = theta;
        this.phi = phi;
    }

    public PolarVector3D(Vector3D source) {
        super(source);
        if (source instanceof PolarVector3D) {
            PolarVector3D s = (PolarVector3D) source;
            this.theta = s.theta;
            this.phi = s.phi;
        }
    }

    public double theta() {
        return theta;
    }

    public double phi() {
        return phi;
    }

    public void rotate(double dtheta, double dphi) {
        this.theta += dtheta;
        this.phi += dphi;
        double magSinPhi = magnitude * Math.sin(this.phi);
        values[0] = Math.cos(this.theta) * magSinPhi;
        values[1] = Math.sin(this.theta) * magSinPhi;
        values[2] = magnitude * Math.cos(this.phi);
    }

    public void update() {
        super.update();
        theta = Math.atan2(values[1], values[0]);
        phi = Math.acos(values[2] / magnitude);
    }

    @Override
    public PolarVector3D clone() {
        return new PolarVector3D(this);
    }

    @Override
    public String toString() {
        return "<" + magnitude + " @ (" + theta + ", " + phi + ")>";
    }

}
