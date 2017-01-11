package com.bolin.physics.connection.binary;

import com.bolin.math.vector.VectorLW;
import com.bolin.physics.normal.Physical;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public class CoulombicConnection extends AbstractBinaryConnection {

    public static final double K = 8.987551787E9;   //  N m^2/C^2

    public CoulombicConnection(Physical p1, Physical p2) {
        super(p1, p2);
    }

    @Override
    public VectorLW getForce(Physical p) {
        checkConnection(p);
        Physical other = getOther(p);
        VectorLW disp = other.getLocation();
        disp.subtract(p.getLocation());
        double dist = disp.magnitude();
        disp.scaleBy(-K * p.getCharge() * other.getCharge() / (dist * dist * dist));
        return disp;
    }

    @Override
    public void checkForce(Physical p) {

    }
}
