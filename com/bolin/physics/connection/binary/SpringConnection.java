package com.bolin.physics.connection.binary;

import com.bolin.math.vector.VectorLW;
import com.bolin.physics.normal.Physical;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public class SpringConnection extends AbstractBinaryConnection {

    private double k;           //  N / m
    private double idealLength; //  m

    public SpringConnection(double k, double idealLength, Physical p1, Physical p2){
        super(p1, p2);
        this.k = k;
        this.idealLength = idealLength;
    }

    @Override
    public VectorLW getForce(Physical p) {          //  returns vector in kg m / s ^ 2
        checkConnection(p);
        Physical other = getOther(p);
        VectorLW disp = other.getLocation();
        disp.subtract(p.getLocation());
        double dist = disp.magnitude();
        disp.unitize();
        disp.scaleBy(k * (dist - idealLength));
        return disp;
    }

    @Override
    public void checkForce(Physical p) {

    }

}
