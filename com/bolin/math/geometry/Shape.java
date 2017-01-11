package com.bolin.math.geometry;

import com.bolin.exceptions.GeometryException;
import com.bolin.math.vector.VectorLW;

/**
 * Created by jonb3_000 on 7/22/2016.
 */
public class Shape {

    private HyperPlane[] edges;
    private final int dimension;

    public Shape(HyperPlane... edges) {
        if (edges.length == 0) {
            throw new GeometryException("Cannot construct a shape with no edges!");
        }
        this.dimension = edges[0].getNormal().dimensions - 1;
        if (edges.length < dimension) {
            throw new GeometryException("Cannot construct a " + edges.length + "-sided figure in " + dimension + "D space.");
        }
        this.edges = edges;
    }

    public boolean isWithin(VectorLW v) {
        Ray ray = new Ray(v, v);
        int intersectCount = 0;
        for (HyperPlane p : edges) {
            if (ray.willIntersect(p)) {
                System.err.println(p + "\t" + ray.intersectTime(p));
                intersectCount ++;
            }
        }
        System.err.println(intersectCount);
        return intersectCount % 2 == 1;
    }
}
