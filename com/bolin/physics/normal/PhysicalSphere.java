package com.bolin.physics.normal;

import com.bolin.graphics.Drawable;
import com.bolin.graphics.SphereInterface;
import com.bolin.math.vector.VectorLW;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Created by jonb3_000 on 7/20/2016.
 */
public class PhysicalSphere extends Physical implements Drawable, SphereInterface {

    private Texture texture;
    private String textureLoc;
    private float[] color;
    private double radius;

    public PhysicalSphere(double mass, double charge, double density) {
        super(mass, charge);
        radius = Math.pow(3 * mass / (4 * Math.PI * density), 1d/3d);
    }

    public PhysicalSphere(double mass, double charge, VectorLW loc, double density) {
        super(mass, charge, loc);
        radius = Math.pow(3 * mass / (4 * Math.PI * density), 1d/3d);
    }

    @Override
    public boolean isReady() {
        return color != null && location != null;
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
    public String getTextureLocation() {
        return textureLoc;
    }

    @Override
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void setTextureLoc(String s) {
        textureLoc = s;
    }

    @Override
    public void setColor(float[] color) {
        this.color = color;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public double getRadiusToward(VectorLW loc) {
        return radius;
    }

}
