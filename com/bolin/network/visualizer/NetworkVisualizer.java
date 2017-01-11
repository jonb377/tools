package com.bolin.network.visualizer;

import com.bolin.math.BMath;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jonb3_000 on 7/12/2016.
 */
public class NetworkVisualizer<E extends NodeInterface> extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    public static final int NODE_RADIUS = 10;

    private final JFrame frame;
    private NetworkInterface<E> network;
    private HashMap<E, Point> nodes;
    private E target;
    private int count;
    private boolean sleep = false;
    private boolean saving;
    private ArrayList<ForceNode> fnodes;
    private Point mousePressPoint;
    private Thread repaintThread;

    public NetworkVisualizer(NetworkInterface<E> network, boolean showWindow) {
        saving = false;
        mousePressPoint = null;
        fnodes = new ArrayList<>();
        nodes = new HashMap();
        this.network = network;
        count = 0;
        initializeNodes();
        if (showWindow) {
            frame = new JFrame("Network");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setSize(500, 500);
            frame.add(this);
            frame.setVisible(true);
            addMouseListener(this);
            addMouseMotionListener(this);
            frame.addKeyListener(this);
            repaintThread = new Thread() {

                boolean running = true;

                @Override
                public void interrupt() {
                    running = false;
                }

                @Override
                public void run() {
                    while (running) {
                        repaint();
                        try {
                            Thread.sleep(16);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            };
            repaintThread.start();
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    repaintThread.interrupt();
                }
            });
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    initializeNodes();
                }
            });
        } else {
            frame = null;
        }
    }

    /**
     * Closes the window that is showing the network.
     */
    public void close() {
        if (frame != null) {
            if (repaintThread != null) {
                repaintThread.interrupt();
            }
            frame.dispose();
        }
    }

    /**
     * Sets the network being displayed.
     *
     * @param n The new network to be shown.
     */
    public void setNetwork(NetworkInterface<E> n) {
        network = n;
        nodes.clear();
        fnodes.clear();
        target = null;
        initializeNodes();
    }

    /**
     * Translates every node in the network to be shows at its current location minus the specified amounts.
     *
     * @param x The amount to move in the negative x-direction
     * @param y The amount to move in the negative y-direction
     */
    public void translate(int x, int y) {
        for (ForceNode f : fnodes) {
            f.point.add(-x, -y);
            f.x -= x;
            f.y -= y;
        }
    }

    public void center() {
        int x = 0;
        int y = 0;
        for (ForceNode f : fnodes) {
            x += f.point.x;
            y += f.point.y;
        }
        x /= fnodes.size();
        y /= fnodes.size();
        x -= getWidth() / 2;
        y -= getHeight() / 2;
        translate(x, y);
    }

    /**
     * Saves the current network display as a .png file.
     *
     * @param filename The name of the .png file.
     * @return Returns true if the save was successful, false otherwise.
     */
    public boolean saveTo(String filename) {
        if (network != null) {
            target = null;
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (E a : nodes.keySet()) {
                Point p = nodes.get(a);
                if (p.x > maxX) {
                    maxX = p.x;
                } else if (p.x < minX) {
                    minX = p.x;
                }
                if (p.y > maxY) {
                    maxY = p.y;
                } else if (p.y < minY) {
                    minY = p.y;
                }
            }
            int width = maxX - minX + 2 * NODE_RADIUS;
            int height = maxY - minY + 2 * NODE_RADIUS;
            translate(minX - NODE_RADIUS, minY - NODE_RADIUS);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            saving = true;
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            paint(g);
            saving = false;
            try {
                ImageIO.write(image, "PNG", new File(filename + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * If the force nodes are not initialized, initializes them. If the network topology has changed since the last call
     * of this function, it will re-initialize those nodes who were affected.
     */
    private void setupForceNodes() {
        double connectionCount = 0;
        for (E a : network.getNodes()) {
            connectionCount += network.neighbors(a).size();
        }
        connectionCount /= 2;
        int nodeCount = network.getNodes().size();
        double potentialConnections = nodeCount * (nodeCount - 1) / 2;
        ForceNode.k = (connectionCount / (1000 * potentialConnections));
        ForceNode.IDEAL_DIST = getWidth() * getHeight() / (nodeCount * nodeCount);
        ForceNode.G = 1 / Math.sqrt(ForceNode.k);
        if (network != null) {
            List<E> enodes = network.getNodes();
            for (int i = 0; i < enodes.size(); i++) {
                E a = enodes.get(i);
                List<E> neighbors = network.neighbors(a);
                if (fnodes.get(i).attract.size() != neighbors.size()) {
                    fnodes.get(i).clear();
                    for (int j = 0; j < enodes.size(); j++) {
                        if (i != j) {
                            if (neighbors.contains(enodes.get(j))) {
                                fnodes.get(i).addAttractee(fnodes.get(j));
                            } else {
                                fnodes.get(i).addRepelee(fnodes.get(j));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Initializes the nodes map.
     */
    private void initializeNodes() {
        if (network != null) {
            List<E> enodes = network.getNodes();
            if (fnodes.isEmpty()) {
                for (int i = 0; i < enodes.size(); i++) {
                    fnodes.add(new ForceNode());
                    nodes.put(enodes.get(i), fnodes.get(i).getPoint());
                }
            }
            setupForceNodes();
//            stabilize();
        }
    }

    /**
     * Runs the physics simulation until the network reaches a stable state.
     */
    public void stabilize() {
        setupForceNodes();
        shock();
        for (int i = 0; i < 100; i++) {
            if (step()) {
                break;
            }
        }
        int i = 0;
        while (step()) {
            i++;
            if (i > 10000) {
                break;
            } else if (i == 5000) {
                freeze();
            }
            if (sleep) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Energizes the nodes in the network, making them move faster.
     */
    public void shock() {
        for (ForceNode n : fnodes) {
            n.energize();
        }
    }

    /**
     * Freezes the nodes in the network, making their velocity 0.
     */
    public void freeze() {
        for (ForceNode n : fnodes) {
            n.vx = n.vy = 0;
        }
    }

    /**
     * Runs a single turn in the physics simulation.
     * Calculates forces, moves the points, and makes the average velocity be zero.
     * @return Returns true if any nodes in the network moved, false otherwise.
     */
    private boolean step() {
        boolean stepped = false;
        for (ForceNode node : fnodes) {
            node.applyForces();
        }
        for (ForceNode node : fnodes) {
            stepped = node.step() || stepped;
        }
        double avgvx = 0;
        double avgvy = 0;
        for (ForceNode node : fnodes) {
            avgvx += node.vx;
            avgvy += node.vy;
        }
        avgvx /= fnodes.size();
        avgvy /= fnodes.size();
        for (ForceNode node : fnodes) {
            node.vx -= avgvx;
            node.vy -= avgvy;
        }
        return stepped;
    }

    @Override
    public void paint(Graphics g) {
        if (!saving) {
            super.paint(g);
        }
        if (network != null) {
            List<E> agents = network.getNodes();
            if (target == null) {
                for (E a : agents) {
                    Point p = nodes.get(a);
                    for (E n : new ArrayList<E>(network.neighbors(a))) {
                        Point pn = nodes.get(n);
                        g.setColor(Color.GRAY);
                        g.drawLine(p.x, p.y, pn.x, pn.y);
                    }
                }
                for (E a : agents) {
                    Point p = nodes.get(a);
                    Color color = a.getColor();
                    g.setColor(color);
                    g.fillOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                }
            } else {
                Point p = nodes.get(target);
                for (E n : network.neighbors(target)) {
                    Point pn = nodes.get(n);
                    g.setColor(new Color(200, 200, 200));
                    for (E nn : network.neighbors(n)) {
                        Point pnn = nodes.get(nn);
                        g.drawLine(pnn.x, pnn.y, pn.x, pn.y);
                    }
                    for (E nn : network.neighbors(n)) {
                        Point pnn = nodes.get(nn);
                        Color color = nn.getColor();
                        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
                        g.fillOval(pnn.x - NODE_RADIUS, pnn.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                    }
                }
                for (E n : network.neighbors(target)) {
                    Point pn = nodes.get(n);
                    g.setColor(Color.gray);
                    g.drawLine(p.x, p.y, pn.x, pn.y);
                    Color color = n.getColor();
                    g.setColor(color);
                    g.fillOval(pn.x - NODE_RADIUS, pn.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
                }
                Color color = target.getColor();
                g.setColor(color);
                g.fillOval(p.x - NODE_RADIUS, p.y - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!saving) {
            int x = e.getPoint().x;
            int y = e.getPoint().y;
            mousePressPoint = new Point(x, y);
            for (E a : nodes.keySet()) {
                if (nodes.get(a).distFrom(mousePressPoint) < NODE_RADIUS) {
                    if (target != a) {
                        target = a;
                    } else {
                        target = null;
                    }
                    return;
                }
            }
            target = null;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        mousePressPoint = null;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 's') {
            saveTo("NetworkCapture " + count);
            count++;
        } else if (e.getKeyChar() == ' ') {
            sleep = !sleep;
        } else if (e.getKeyChar() == 'f') {
            freeze();
        } else if (e.getKeyChar() == 'd') {
            shock();
        } else if (e.getKeyChar() == 'c') {
            center();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mousePressPoint != null && !saving) {
            Point p = new Point(e.getPoint().x, e.getPoint().y);
            int dx = mousePressPoint.x - p.x;
            int dy = mousePressPoint.y - p.y;
            translate(dx, dy);
            mousePressPoint = p;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * A discrete point on the screen. Used for graphics.
     */
    static class Point {

        private int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Adds the specified amounts to the point.
         * @param x The change in x
         * @param y The change in y
         */
        public void add(int x, int y) {
            this.x += x;
            this.y += y;
        }

        /**
         * Calculates the angle to the other point.
         * @param other The other point
         * @return The angle from this point to the other.
         */
        public double angleTo(Point other) {
            double dx = other.x - x;
            double dy = other.y - y;
            return Math.atan2(dy, dx);
        }

        /**
         * Returns the geometric distance between the two points.
         * @param other The other point.
         * @return The distance between this and other.
         */
        public double distFrom(Point other) {
            double dx = other.x - x;
            double dy = other.y - y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        /**
         * Tells if this point is in between the source and dest points, provided that this point is of radius 'pointRadius'
         * @param source The source endpoint.
         * @param dest The destination endpoint.
         * @param pointRadius The radius of this point.
         * @return True if this point would block a ray emitted from source toward dest, false otherwise.
         */
        public boolean isBetween(Point source, Point dest, double pointRadius) {
            double sToD = source.angleTo(dest);
            double sToThis = source.angleTo(this);
            double dist = source.distFrom(this);
            double pointAngle = Math.atan2(pointRadius, dist);
            return Math.abs(sToD - sToThis) < Math.abs(pointAngle);
        }

        public String toString() {
            return "Point[{" + x + ", " + y + "}]";
        }

    }

    /**
     * A class containing a point, its precise position, its velocity, and lists of other forcenodes that this one will
     * repel and attract.
     */
    static class ForceNode {

        public static double IDEAL_DIST = 100;
        public static double k = .0001;
        public static double G = 100;
        public static double fric = 0.99;

        private Point point;
        private double vx, vy;
        private double x, y;
        private ArrayList<ForceNode> attract, repel;

        public ForceNode() {
            int x = (int) (Math.random() * 1000);
            int y = (int) (Math.random() * 800);
            point = new Point(x, y);
            this.x = x;
            this.y = y;
            vx = vy = 0;
            attract = new ArrayList<>();
            repel = new ArrayList<>();
        }

        /**
         * Adds a forcenode to the list of forcenodes this will attract.
         * @param f The forcenode that this should attract.
         */
        public void addAttractee(ForceNode f) {
            attract.add(f);
        }

        /**
         * Adds a forcenode to the list of forcenodes this will repel.
         * @param f The forcenode that this should repel.
         */
        public void addRepelee(ForceNode f) {
            repel.add(f);
        }

        /**
         * Moves this forcenode based on its current velocity, and decreases the velocity by a certain amount.
         * @return True if this node's point moved, false otherwise.
         */
        public boolean step() {
            int oldx = point.x;
            int oldy = point.y;
            x += vx;
            y += vy;
            point.x = (int) Math.round(x);
            point.y = (int) Math.round(y);
            vx *= fric;
            vy *= fric;
//            fix();
            return point.x != oldx || point.y != oldy;
        }

        /**
         * Multiplies the velocity of this node by a randomInt amount in each axis.
         */
        public void energize() {
            vx *= BMath.drandom() * 50;
            vy *= BMath.drandom() * 50;
        }

        /**
         * Applies the force to the forcenode's current velocity.
         * @param fx The amount to change vx
         * @param fy The amount to change vy
         */
        public void addForce(double fx, double fy) {
            vx += fx;
            vy += fy;
        }

        /**
         * @return Returns the point stored in this forcenode.
         */
        public Point getPoint() {
            return point;
        }

        /**
         * Applies forces to each other forcenode in this one's 'repel' and 'attract' lists.
         */
        public void applyForces() {
            for (ForceNode f : attract) {
                double dist = point.distFrom(f.point);
                double angle = point.angleTo(f.point);
                double d = dist - IDEAL_DIST;
                double fmagnitude;
//                if (d < 0) {
//                    fmagnitude = k * d * d;
//                } else {
//                    fmagnitude = - k * d;
//                }
                fmagnitude = -k * d;
                double fx = fmagnitude * Math.cos(angle);
                double fy = fmagnitude * Math.sin(angle);
                f.addForce(fx, fy);
            }
            for (ForceNode f : repel) {
                double dist = point.distFrom(f.point);
                double angle = point.angleTo(f.point);
                if (dist == 0) {
                    continue;
                }
                double fmagnitude = G / (dist * dist);
                double fx = fmagnitude * Math.cos(angle);
                double fy = fmagnitude * Math.sin(angle);
                f.addForce(fx, fy);
            }
        }

        /**
         * Clears this forcenode's repel and attract lists, sets its velocity to zero.
         */
        public void clear() {
            repel.clear();
            attract.clear();
            vy = vx = 0;
        }
    }
}
