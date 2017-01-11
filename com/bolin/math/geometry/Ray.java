package com.bolin.math.geometry;

import com.bolin.math.vector.VectorLW;

/**
 * Created by jonb3_000 on 7/23/2016.
 */
public class Ray {

    private VectorLW source, direction;

    public Ray(VectorLW source, VectorLW direction) {
        this.source = source;
        this.direction = direction;
    }

    public double intersectTime(HyperPlane p) {
        /*
            Planes are (v.subtract(plane.source)).dot(plane.normal) == 0
                       ((source.add(direction.scaleBy(t)).subtract(plane.source)).dot(plane.normal) == 0
                       (source + t direction - plane.source) . plane.normal == 0
                       source . plane.normal + t direction . plane.normal - plane.source . plane.normal == 0
                       t direction . plane.normal == plane.source . plane.normal - source . plane.normal
                       t = (plane.source . plane.normal - source . plane.normal) / (direction . plane.normal)
                       t = (plane.source - source) . plane.normal / (direction . plane.normal)
         */
        VectorLW sourceShift = p.getPoint().clone();
        sourceShift.subtract(source);
        double sourceDot = sourceShift.dot(p.getNormal());
        double dirDot = direction.dot(p.getNormal());
        return sourceDot / dirDot;
    }

    public VectorLW pointAtTime(double t) {
        VectorLW v = source.clone();
        VectorLW dir = direction.clone();
        dir.scaleBy(t);
        v.add(dir);
        return v;
    }

    public boolean willIntersect(HyperPlane p) {
        double intersectTime = this.intersectTime(p);
        if (Double.isFinite(intersectTime)) {
            return intersectTime >= 0;
        }
        return false;
    }

    public String toString() {
        return source + " @ " + direction;
    }

    public VectorLW getDirection() {
        return direction.clone();
    }

    public VectorLW getSource() {
        return source;
    }
}
