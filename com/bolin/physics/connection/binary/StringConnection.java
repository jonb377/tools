package com.bolin.physics.connection.binary;

import com.bolin.math.vector.VectorLW;
import com.bolin.physics.normal.Physical;

/**
 * Created by jonb3_000 on 7/22/2016.
 */
public class StringConnection extends AbstractBinaryConnection {

    private static final VectorLW zero = new VectorLW(0, 0, 0);
    private static final double RIGIDITY = 100;

    private double length;
    private VectorLW disp1, disp2;
    private double mag;
    private boolean applied;

    public StringConnection(double length, Physical p1, Physical p2) {
        super(p1, p2);
        this.length = length;
    }

    @Override
    public VectorLW getForce(Physical p) {
        checkConnection(p);
        applied = false;
        Physical other = getOther(p);
        VectorLW disp = other.getLocation();
        disp.subtract(p.getLocation());
        double magnitude = disp.magnitude();
        setDisp(p, disp);
        mag = magnitude;
        return zero;
    }

    private void setDisp(Physical p, VectorLW disp) {
        if (p == p1) {
            disp1 = disp;
        } else {
            disp2 = disp;
        }
    }

    private VectorLW getDisp(Physical p) {
        if (p == p1) {
            return disp1;
        }
        return disp2;
    }

    @Override
    public void checkForce(Physical p) {
        checkConnection(p);
        VectorLW disp = getDisp(p);
        double magnitude = mag;
        Physical other = getOther(p);
        if (magnitude >= length && !applied) {
            applied = true;
            double delta = magnitude - length;
            disp.unitize();
            disp.scaleBy(10 * delta);
            VectorLW force = p.getForce();
            force.add(disp);
            p.setForce(force.clone());
            force = other.getForce();
            disp.scaleBy(-1);
            force.add(disp);
            other.setForce(force);
        }
    }
}
