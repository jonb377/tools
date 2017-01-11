package com.bolin.physics.staticphys;

import com.bolin.graphics.PolygonInterface;
import com.bolin.math.geometry.HyperPlane;
import com.bolin.math.vector.VectorLW;
import com.bolin.physics.normal.Physical;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Created by jonb3_000 on 7/21/2016.
 */
public class PhysicalPlane extends StaticPhysical implements PolygonInterface {

    private HyperPlane plane;
    private float[] color;
    private Texture texture;
    private String textureLoc;
    private VectorLW max, min;
    private float[][] vertices;

    public PhysicalPlane(VectorLW[] endPoints) {
        maxMin(endPoints);
        vertices = new float[endPoints.length][];
        for (int e = 0;e < endPoints.length;e ++) {
            VectorLW v = endPoints[e];
            double[] values = v.getValues();
            float[] curr = new float[values.length];
            for (int i = 0;i < values.length;i ++) {
                curr[i] = (float) values[i];
            }
            vertices[e] = curr;
        }
        setupPlane(endPoints);
        this.color = new float[]{1, .5f, 1};
    }

    private void maxMin(VectorLW[] endPoints) {
        double maxX = Double.NEGATIVE_INFINITY;
        double minX = Double.POSITIVE_INFINITY;
        double maxY = maxX;
        double minY = minX;
        double maxZ = maxX;
        double minZ = minX;
        for (VectorLW v : endPoints) {
            double[] values = v.getValues();
            if (values[0] > maxX) {
                maxX = values[0];
            } else if (values[0] < minX) {
                minX = values[0];
            }
            if (values[1] > maxY) {
                maxY = values[1];
            } else if (values[1] < minY) {
                minY = values[1];
            }
            if (values[2] > maxZ) {
                maxZ = values[2];
            } else if (values[2] < minZ) {
                minZ = values[2];
            }
        }
        max = new VectorLW(maxX, maxY, maxZ);
        min = new VectorLW(minX, minY, minZ);

    }

    private void setupPlane(VectorLW[] endpoints) {
        VectorLW point = endpoints[0].clone();
        endpoints[0].subtract(endpoints[2]);
        endpoints[1].subtract(endpoints[3]);
        VectorLW normal = endpoints[0].normal(endpoints[1]);
        normal.unitize();
        plane = new HyperPlane(normal, point);
    }

    @Override
    public void updateForce(double timeScale) {
        for (Physical p : space.getPhysicals()) {
            VectorLW dispTo = plane.dispFrom(p.getLocation());
            VectorLW nearestPoint = dispTo.clone();
            nearestPoint.add(p.getLocation());
            if (isOn(nearestPoint)) {
                VectorLW v = p.getVelocity();
                double radius = p.getRadiusToward(nearestPoint);
                double vtoward = v.proj(dispTo).magnitude();
                double dist = dispTo.magnitude();
                if (dist <= radius || vtoward * timeScale > dist + radius) {
                    if (v.dot(dispTo) > 0) {
                        VectorLW normal = plane.getNormal();
                        if (dist > radius) {
                            dispTo.unitize();
                            VectorLW move = dispTo.clone();
                            move.scaleBy(-radius);
                            nearestPoint.add(move);
                            p.setLocation(nearestPoint);
                        }
                        VectorLW keep = v.rejection(normal);
                        VectorLW reverse = v.proj(normal);
                        reverse.scaleBy(-.75);
                        keep.add(reverse);
                        p.setVelocity(keep);
                        VectorLW fkeep = p.getForce().rejection(normal);
                        p.setForce(fkeep);
                    }
                }
            }
        }
    }

    public boolean isOn(VectorLW v) {
        if (plane.isOn(v)) {
            boolean on = true;
            double[] values = v.getValues();
            for (int j = 0;j < values.length;j ++) {
                if (values[j] > max.getValues()[j] + 1 || values[j] < min.getValues()[j] - 1) {
                    on = false;
                    break;
                }
            }
            return on;
        }
        return false;
    }

    @Override
    public float[][] getVertices() {
        return vertices;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public float[] getColor() {
        return color;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public boolean hasTexture() {
        return texture != null;
    }

    @Override
    public VectorLW getLocation() {
        return null;
    }

    @Override
    public String getTextureLocation() {
        return textureLoc;
    }

    @Override
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void setTextureLoc(String s) {
        this.textureLoc = s;
    }

    @Override
    public void setColor(float[] color) {
        this.color = color;
    }
}
