package com.bolin.math.geometry;

import com.bolin.exceptions.GeometryException;
import com.bolin.math.vector.VectorLW;

/**
 * Created by jonb3_000 on 7/21/2016.
 */
public class Line {

    private VectorLW point, direction;

    public Line(VectorLW point, VectorLW direction) {
        this.point = point;
        this.direction = direction;
    }

    public boolean isOn(VectorLW point) {
        checkDimensions(point);
        VectorLW clone = point.clone();
        clone.subtract(this.point);
        return clone.isParallel(direction);
    }

    public VectorLW dispFrom(VectorLW point) {
        checkDimensions(point);
        VectorLW disp = point.clone();
        disp.subtract(this.point);
        disp.scaleBy(-1);
        VectorLW rej = disp.rejection(direction);
        return rej;
    }

    private void checkDimensions(VectorLW point) {
        if (point.dimensions != this.point.dimensions) {
            throw new GeometryException("Point provided is of different dimension than this plane:\t" + point + "\t" + this.point);
        }
    }

    @Override
    public String toString() {
        return point + " @ " + direction;
    }

}
