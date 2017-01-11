package com.bolin.physics.staticphys;

import com.bolin.physics.AbstractPhysical;

/**
 * Created by jonb3_000 on 7/21/2016.
 */
public abstract class StaticPhysical extends AbstractPhysical {

    public abstract void updateForce(double timeScale);

}
