package com.bolin.physics.connection.unary;

import com.bolin.math.vector.VectorLW;
import com.bolin.physics.normal.Physical;

/**
 * Created by jonb3_000 on 7/21/2016.
 */
public class GravitationalField extends AbstractUnaryConnection {

    VectorLW down;
    protected double g;

    public GravitationalField(VectorLW down, double g) {
        this.down = down;
        this.g = g;
    }

    @Override
    public VectorLW getForce(Physical p) {
        VectorLW force = down.clone();
        force.scaleBy(g * p.getMass());
        return force;
    }

    @Override
    public void checkForce(Physical p) {

    }
}
