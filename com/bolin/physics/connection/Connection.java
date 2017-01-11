package com.bolin.physics.connection;

import com.bolin.math.vector.VectorLW;
import com.bolin.physics.normal.Physical;

/**
 * Created by jonb3_000 on 7/21/2016.
 */
public interface Connection {

    VectorLW getForce(Physical p);

    void checkForce(Physical p);

}
