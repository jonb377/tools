package com.bolin.math.geometry;

import com.bolin.exceptions.GeometryException;
import com.bolin.math.BMath;
import com.bolin.math.vector.VectorLW;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Bounded plane. Used for edges in constructing shapes.
 * Created by jonb3_000 on 7/23/2016.
 * @ItSucks
 */
public class BoundedPlane extends HyperPlane {

    private ArrayList<BoundedPlane> bounds;

    public BoundedPlane(VectorLW normal, VectorLW point, HyperPlane container) {
        super(normal, point, container);
        bounds = new ArrayList<>();
    }

    public BoundedPlane(VectorLW normal, VectorLW point) {
        super(normal, point);
        bounds = new ArrayList<>();
    }

    public BoundedPlane(HyperPlane other) {
        super(other.normal, other.point, other.container);
        bounds = new ArrayList<>();
    }

    public boolean clip(HyperPlane bound) {
        BoundedPlane p = intersect(bound);
        for (BoundedPlane b : bounds) {
            if (b.equals(p)) {
                return false;
            }
        }
        boolean success = true;
        if (dimension > 1) {
            for (BoundedPlane b : bounds) {
                if (!b.isParallel(p)) {
                    success = success && b.clip(p);
                    p.clip(b);
                }
            }
        }
        if (!success) {
            String s = "";
            for (int i = 0;i < 3 - dimension;i ++) {
                s += "\t";
            }
            System.err.println(s + p);
        }
        bounds.add(p);
        return success;
    }

    @Override
    public BoundedPlane intersect(HyperPlane other) {
        return new BoundedPlane(super.intersect(other));
    }

    @Override
    public boolean isOn(VectorLW v) {
        if (super.isOn(v)) {
            if (bounds.isEmpty()) {
                return true;
            }
            double[] d = new double[dimension];
            d[0] = 1;
            VectorLW vect = new VectorLW(d);
            Ray ray = new Ray(planeSpaceUnchecked(v), vect);
            int count = 0;
            for (BoundedPlane p : bounds) {
                if (ray.willIntersect(p)) {
                    VectorLW intersectPoint = ray.pointAtTime(ray.intersectTime(p));
                    if (p.isOn(intersectPoint)) {
                        count++;
                    }
                }
            }
//            System.out.println("Count:\t" + count);
            return count % 2 == 1;
        }
        return false;
    }

    public ArrayList<BoundedPlane> getBounds() {
        return bounds;
    }

//    @Override
//    public String toString() {
//        String s = normal + "\t:\t" + point + "\n";
//        for (BoundedPlane b : bounds) {
//            s += "\t" + b;
//        }
//        return s;
//    }

}
