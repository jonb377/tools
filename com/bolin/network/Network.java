package com.bolin.network;

import com.bolin.network.visualizer.NetworkInterface;
import com.bolin.network.visualizer.NetworkVisualizer;
import com.bolin.network.visualizer.NodeInterface;

import java.util.*;

/**
 * Created by jonb3_000 on 7/20/2016.
 */
public class Network<E extends NodeInterface> implements NetworkInterface<E> {

    private ArrayList<E> nodes;
    private HashMap<E, ArrayList<E>> edges;

    public Network() {
        nodes = new ArrayList<E>();
        edges = new HashMap<E, ArrayList<E>>();
    }

    public void addNode(E node) {
        nodes.add(node);
        edges.put(node, new ArrayList<E>());
    }

    public void addEdge(E start, E end) {
        edges.get(start).add(end);
    }

    public NetworkVisualizer<E> getVisualizer(boolean visible) {
        return new NetworkVisualizer<E>(this, visible);
    }

    @Override
    public List<E> getNodes() {
        return nodes;
    }

    @Override
    public ArrayList<E> neighbors(E node) {
        return (ArrayList<E>) edges.get(node).clone();
    }
}
