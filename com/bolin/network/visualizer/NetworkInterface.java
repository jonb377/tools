package com.bolin.network.visualizer;

import java.util.List;

/**
 * Created by jonb3_000 on 7/14/2016.
 */
public interface NetworkInterface<E> {

    /**
     * @return Returns the list of nodes in the network.
     */
    List<E> getNodes();

    /**
     * @param node The node in question
     * @return Returns the list of nodes that are connected to the specified node in the network.
     */
    List<E> neighbors(E node);

}
