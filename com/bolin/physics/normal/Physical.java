package com.bolin.physics.normal;

import com.bolin.math.vector.VectorLW;
import com.bolin.physics.AbstractPhysical;
import com.bolin.physics.connection.Connection;
import com.bolin.physics.connection.binary.AbstractBinaryConnection;

import java.util.ArrayList;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public abstract class Physical extends AbstractPhysical {

    private ArrayList<Connection> connections;
    protected VectorLW location;         //  location in m
    private VectorLW velocity,           //  velocity in m/s
            force;                      //  force in kg m / s ^2
    private double mass;                //  mass in kg
    private double charge;              //  charge in C

    public Physical(double mass, double charge) {
        this.mass = mass;
        this.charge = charge;
        connections = new ArrayList<>();
        location = new VectorLW(0, 0, 0);
        velocity = new VectorLW(0, 0, 0);
        force = new VectorLW(0, 0, 0);
    }

    public Physical(double mass, double charge, VectorLW loc) {
        this.mass = mass;
        this.charge = charge;
        this.location = loc;
        velocity = new VectorLW(0, 0, 0);
        force = new VectorLW(0, 0, 0);
        connections = new ArrayList<>();
    }

    public void addConnection(Connection c) {
        connections.add(c);
    }

    public double distFrom(Physical other) {
        VectorLW disp = new VectorLW(location);
        disp.subtract(other.location);
        return disp.magnitude();
    }

    public void updateForce() {
        force.scaleBy(0);
        for (Connection c : connections) {
            force.add(c.getForce(this));
        }
        for (Connection c : connections) {
            c.checkForce(this);
        }
    }

    public void applyForce(double timeScale) {
        force.scaleBy(timeScale / mass);
        velocity.add(force);
        VectorLW tsv = new VectorLW(velocity);
        tsv.scaleBy(timeScale);
        location.add(tsv);
    }

    public VectorLW getLocation() {
        return location.clone();
    }

    public VectorLW getVelocity() {
        return velocity.clone();
    }

    public VectorLW getForce() {
        return force.clone();
    }

    public double getMass() {
        return mass;
    }

    public double getCharge() {
        return charge;
    }

    public abstract double getRadiusToward(VectorLW location);

    public void freeze() {
        velocity.scaleBy(0);
    }

    public void setForce(VectorLW force){
        this.force = force;
    }

    public void setVelocity(VectorLW velocity) {
        this.velocity = velocity;
    }

    public void setLocation(VectorLW location) {
        this.location = location;
    }
}
