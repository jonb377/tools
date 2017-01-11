package com.bolin.math.geometry;

import com.bolin.exceptions.GeometryException;
import com.bolin.math.BMath;
import com.bolin.math.matrix.RealMatrix;
import com.bolin.math.vector.VectorLW;

/**
 * Created by jonb3_000 on 7/21/2016.
 */
public class HyperPlane {

    protected VectorLW point;
    protected VectorLW normal;
    public final int dimension;
    public final HyperPlane container;
    protected RealMatrix rotation, invRotation;

    public static HyperPlane deduceFrom(VectorLW... points) {
        if (points.length == 0) {
            throw new GeometryException("Cannot create a plane with no source points.");
        }
        if (points.length != points[0].dimensions) {
            throw new GeometryException("Cannot construct a " + (points[0].dimensions - 1) + "D plane with " + points.length + " points.");
        }
        VectorLW[] diffs = new VectorLW[points.length - 1];
        for (int i = 0;i < diffs.length;i ++) {
            diffs[i] = points[i].clone();
            diffs[i].subtract(points[i + 1]);
        }
        VectorLW normal = VectorLW.normal(diffs);
        VectorLW point = points[0].clone();
        return new HyperPlane(normal, point);
    }

    public HyperPlane(VectorLW normal, VectorLW point, HyperPlane container) {
        this.normal = normal;
        normal.unitize();
        this.point = point;
        dimension = normal.dimensions - 1;
        this.container = container;
    }

    public HyperPlane(VectorLW normal, VectorLW point) {
        this.normal = normal;
        normal.unitize();
        this.point = point;
        dimension = normal.dimensions - 1;
        container = null;
    }

    public boolean isOn(VectorLW point) {
        checkDimensions(point);
        VectorLW disp = point.clone();
        disp.subtract(this.point);
        return Math.abs(disp.dot(normal)) < 1E-10;
    }

    public VectorLW dispFrom(VectorLW point) {
        checkDimensions(point);
        VectorLW disp = point.clone();
        disp.subtract(this.point);
        disp.scaleBy(-1);
        return disp.proj(normal);
    }

    protected void checkDimensions(VectorLW point) {
        if (point.dimensions != this.point.dimensions) {
            throw new GeometryException("Point provided is of different dimension than this plane:\t" + point + "\t" + this.point);
        }
    }

    /**
     *
     * @param other
     * @return Returns a plane of one less dimension representing the intersection of the two planes.
     */

    public HyperPlane intersect(HyperPlane other) {
        if (dimension != other.dimension) {
            throw new GeometryException("Cannot calculate the intersection of two hyperplanes of different dimension:\t" + dimension + "\t" + other.dimension);
        }
        if (isParallel(other)) {
            throw new GeometryException("Cannot find intersection of two parallel planes:\t" + this + ",\t" + other);
        }
        if (dimension < 2) {
            if (dimension < 1) {
                throw new GeometryException(dimension + "D intersection");
            }
            /*
                two lines intersect
                n . p == n . s
                no . p == no . s
                Ray cast?
             */
            Ray ray = new Ray(point, normal.normal());
            VectorLW point = planeSpaceUnchecked(ray.pointAtTime(ray.intersectTime(other)));
            return new HyperPlane(point.clone(), point, this);
        }
        /*
            where two planes intersect
            intersection will be along a plane of dimension - 1
            Choose dimension - 1 values at random for the intersection point.
            p3 ... pd = this.point3 ... this.pointd
            There will be a line on the other plane corresponding to these values.
            There will be a line on this plane corresponding to these values.
            We need to find where these two lines intersect.

            P denotes chosen value for point, p is variable

            no is normal other, so is source other
            other plane line:       no1 p1 + no2 p2 + no3 P3 + ... + nod Pd == no . so
            this  plane line:       n1 p1 + n2 p2 + n3 P3 + ... + nd Pd == n . s

            two equations, two variables.

            n.P is the partial dot product for the chosen values

            no1 p1 + no2 p2 + no . P == no . so
            no1 p1 + no2 p2 == no . so - no . P

            p2(p1) = (no . so - no . P - no1 p1) / no2

            n1 p1 + n2 p2 + n . P == n . s
            n1 p1 + n2 p2 == n . s - n . P

            n1 p1 + n2 (no . so - no . P - no1 p1) / no2 == n . s - n . P

            n1 p1 + n2 (no . so - no . P) / no2 - n2 no1 p1 / no2 == n . s - n . P

            n1 p1 - n2 no1 p1 / no2 == n . s - n . P - n2 (no . so - no . P) / no2

            p1 (n1 - n2 no1 / no2) == n . s - n . P - n2 (no . so - no . P) / no2
            p1 == (n . s - n . P - n2 (no . so - no . P) / no2) / (n1 - n2 no1 / no2)
            p2 == (no . so - no . P - no1 p1) / no2
         */
        int d1, d2 = -1;
        d1 = findNonZeroDimension(normal, d2);
        d2 = findNonZeroDimension(other.normal, d1);
        if (d1 == -1 || d2 == -1) {
            d1 = -1;
            d2 = findNonZeroDimension(other.normal, d1);
            d1 = findNonZeroDimension(normal, d2);
        }
        double[] p = new double[dimension + 1];
        for (int i = 0;i < p.length;i ++) {
            if (i != d1 && i != d2) {
                p[i] = point.getValues()[i];
            }
        }
        double ns = normal.dot(point);
        double noso = other.normal.dot(other.point);
        double nP = 0;
        double noP = 0;
        double[] n = normal.getValues();
        double[] no = other.normal.getValues();
        for (int i = 0;i < p.length;i ++) {
            if (i != d1 && i != d2) {
                nP += n[i] * p[i];
                noP += no[i] * p[i];
            }
        }
        p[d1] = (ns - nP - n[d2] * (noso - noP) / no[d2]) / (n[d1] - n[d2] * no[d1] / no[d2]);
        p[d2] = (noso - noP - no[d1] * p[d1]) / no[d2];
        VectorLW point = new VectorLW(p);
        if (!((HyperPlane) other).isOn(point)) {
            throw new GeometryException("Bad calculation in " + dimension + "D\t" + this + "\t\t" + other + "\n" + point);
        }
        VectorLW norm = other.normal.rejection(normal);
        return new HyperPlane(planeSpaceUnchecked(norm), planeSpaceUnchecked(point), this);
    }

    /**
     * Finds a non-zero dimension in the vector.
     * @param v The vector
     * @param notThese Dimensions to be excluded in the search.
     * @return The dimension with a non-zero component that is not in notThese, or -1 if no such dimension exists
     */
    protected int findNonZeroDimension(VectorLW v, int... notThese) {
        double[] values = v.getValues();
        outer:
        for (int i = 0;i < values.length;i ++) {
            for (int j = 0;j < notThese.length;j ++) {
                if (i == notThese[j]) {
                    continue outer;
                }
            }
            if (values[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    public VectorLW getNormal() {
        return normal.clone();
    }

    public VectorLW getPoint() {
        return point;
    }

    protected void setupRotationMatrix() {
        double[][] temp = new double[dimension + 1][dimension + 1];
        for (int i = 0;i < dimension + 1;i ++) {
            temp[i][i] = 1;
        }
        rotation = new RealMatrix(temp);
        VectorLW rotatedNormal = normal.clone();
        for (int d = 0;d < dimension;d ++) {
            if (rotatedNormal.getValues()[d] != 0) {
                double mag = BMath.pythagorize(rotatedNormal.getValues()[d], rotatedNormal.getValues()[dimension]);
                double sin = rotatedNormal.getValues()[d] / mag;
                double cos = Math.sqrt(1 - sin * sin);
                double[][] mat = new double[dimension + 1][dimension + 1];
                mat[d][d] = cos;
                mat[d][dimension] = -sin;
                mat[dimension][d] = sin;
                mat[dimension][dimension] = cos;
                for (int i = 0; i < dimension; i++) {
                    if (i != d) {
                        mat[i][i] = 1;
                    }
                }
                RealMatrix matrix = new RealMatrix(mat);
                rotation = rotation.multiply(matrix);
                rotatedNormal.multiply(matrix);
            }
        }
        invRotation = rotation.inverse();
    }

    public boolean isParallel(HyperPlane other) {
        if (dimension != other.dimension) {
            throw new GeometryException("Dissimilar dimensionality for parallel check:\t" + dimension + " vs " + other.dimension);
        }
        return Math.abs(normal.dot(other.normal)) == normal.magnitude() * other.normal.magnitude();
    }

    /**
     *
     * @param v
     * @return A vector with this plane's dimension representing v's location on this plane, or null if v is not on this plane.
     */
    public VectorLW planeSpace(VectorLW v) {
        if (isOn(v)) {
            return planeSpaceUnchecked(v);
        }
        return null;
    }

    public VectorLW planeSpaceUnchecked(VectorLW v) {
        if (rotation == null) {
            setupRotationMatrix();
        }
        VectorLW diff = v.clone();
        diff.subtract(point);
        diff.multiply(rotation);
        double[] newVect = new double[dimension];
        for (int i = 0;i < newVect.length;i ++) {
            newVect[i] = diff.getValues()[i];
        }
        return new VectorLW(newVect);
    }

    public VectorLW fromPlaneSpace(VectorLW v) {
        if (v.dimensions != dimension) {
            throw new GeometryException("Cannot convert a " + v.dimensions + "D vector from a " + dimension + "D planespace.");
        }
        if (invRotation == null) {
            setupRotationMatrix();
        }
        double[] newVect = new double[v.dimensions + 1];
        System.arraycopy(v.getValues(), 0, newVect, 0, v.dimensions);
        VectorLW result = new VectorLW(newVect);
        result.multiply(invRotation);
        return result;
    }

    public VectorLW fromPlaneSpaceToAbsolute(VectorLW v) {
        if (container != null) {
            return container.fromPlaneSpaceToAbsolute(fromPlaneSpace(v));
        } else {
            return fromPlaneSpace(v);
        }
    }

    public VectorLW somePoint() {
        /*
            normal . (point - rand) = 0
            normal . point - normal . rand = 0
            normal . rand = normal . point
            n1 r1 + n2 r2 + n3 r3 + ... + nd rd = n1 p1 + n2 p2 + n3 p3 + ... + nd pd
            n1 r1 = n1 p1 + n2 (p2 - r2) + n3 (p3 - r3) + ... + nd (pd - rd)
            Let n1 be some non-zero component of the normal, doesn't matter which.
            Choose r2 through rd at random
            calculate the r1 value that will put the point on the plane.
         */
        double[] newVect = new double[normal.dimensions];
        int start = BMath.randomLess(newVect.length);
        int d = 0;
        boolean go = true;
        for (int i = start;i != start || go;i = (i + 1) % newVect.length) {
            go = false;
            if (normal.getValues()[i] != 0) {
                d = i;
                break;
            }
        }
        for (int i = 0;i < newVect.length;i ++) {
            if (i != d) {
                newVect[i] = 1 / BMath.drandom();
            }
        }
        double sum = normal.getValues()[d] * point.getValues()[d];
        for (int i = 0;i < newVect.length;i ++) {
            if (i != d) {
                double diff = point.getValues()[i] - newVect[i];
                sum += normal.getValues()[i] * diff;
            }
        }
        newVect[d] = sum / normal.getValues()[d];
        return new VectorLW(newVect);
    }

    public String absoluteString() {
        if (container == null) {
            return toString();
        }
        return container.fromPlaneSpaceToAbsolute(normal) + "\t:\t" + container.fromPlaneSpaceToAbsolute(point);
    }

    @Override
    public String toString() {
        return normal + "\t:\t" + point;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof HyperPlane) {
            HyperPlane plane = (HyperPlane) other;
            if (container != plane.container) {
                return false;
            }
            return point.equals(plane.point) && normal.equals(plane.normal);
        }
        return false;
    }
}
