package com.bolin.physics.connection.binary;

import com.bolin.exceptions.ConnectionException;
import com.bolin.math.vector.VectorLW;
import com.bolin.physics.normal.Physical;
import com.bolin.physics.connection.Connection;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public abstract class AbstractBinaryConnection implements Connection {

    protected Physical p1, p2;

    public AbstractBinaryConnection(Physical p1, Physical p2) {
        this.p1 = p1;
        this.p2 = p2;
        p1.addConnection(this);
        p2.addConnection(this);
    }

    public boolean isConnected(Physical p) {
        return p == p1 || p == p2;
    }

    public void checkConnection(Physical p) {
        if (!isConnected(p)) {
            throw new ConnectionException("Finding connection force of physical not in connection:\t" + p + "\t" + p1 + "\t" + p2);
        }
    }

    public Physical getOther(Physical p) {
        if (p == p1) {
            return p2;
        }
        return p1;
    }

    public abstract VectorLW getForce(Physical p);

}
