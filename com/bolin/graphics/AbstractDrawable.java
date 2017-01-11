package com.bolin.graphics;

import com.bolin.math.vector.Vector3D;
import com.bolin.math.vector.VectorLW;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public abstract class AbstractDrawable implements Drawable {

    protected float[] color;
    protected VectorLW location;
    protected String textureLoc;
    private Texture texture;

    public AbstractDrawable() {
        color = null;
        location = null;
        textureLoc = null;
        texture = null;
    }

    public boolean isReady() {
        return color != null && location != null;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public void setLocation(VectorLW location) {
        this.location = location;
    }

    public void setTextureLoc(String textureLoc) {
        this.textureLoc = textureLoc;
    }

    public void setTexture(Texture t) {
        this.texture = t;
    }

    public String getTextureLocation() {
        return textureLoc;
    }

    public boolean hasTexture() {
        return textureLoc != null;
    }

    public VectorLW getLocation() {
        return location;
    }

    public Texture getTexture() {
        return texture;
    }

    public float[] getColor() {
        return color;
    }

}
