package com.bolin.physics.normal;

import com.bolin.math.vector.VectorLW;

/**
 * Created by jonb3_000 on 7/22/2016.
 */
public class FixedSphere extends PhysicalSphere {

    public FixedSphere(double mass, double charge, VectorLW loc, double density) {
        super(mass, charge, loc, density);
    }

    @Override
    public void updateForce() {

    }

    @Override
    public void applyForce(double timescale) {

    }
}
