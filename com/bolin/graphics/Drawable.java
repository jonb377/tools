package com.bolin.graphics;

import com.bolin.math.vector.VectorLW;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Created by jonb3_000 on 7/20/2016.
 */
public interface Drawable {

    boolean isReady();

    float[] getColor();

    Texture getTexture();

    boolean hasTexture();

    VectorLW getLocation();

    String getTextureLocation();

    void setTexture(Texture texture);

    void setTextureLoc(String s);

    void setColor(float[] color);

}
