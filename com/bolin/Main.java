package com.bolin;

import com.bolin.math.geometry.BoundedPlane;
import com.bolin.math.geometry.HyperPlane;
import com.bolin.math.geometry.Line;
import com.bolin.math.geometry.Ray;
import com.bolin.math.vector.VectorLW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    static Ray clickRay = null;
    static VectorLW clickPoint = null, releasePoint = null;
    static BoundedPlane plane;
    static HyperPlane bound1, bound2, bound3;

    public static void main(String[] args) throws InterruptedException {

        if (true) {
            VectorLW point = new VectorLW(1, 2, 3);
            VectorLW linePoint = new VectorLW(0, -1, 0);
            VectorLW lineDir = new VectorLW(2, 0, 1);
            System.out.println(point.su)
            System.exit(0);
        }

        System.out.println("This is the tools library.");

//        HyperPlane plane = new HyperPlane(new VectorLW(0, 1, 1), new VectorLW(0, 0, 0));
//        HyperPlane other = new HyperPlane(new VectorLW(-1, 1, 0), new VectorLW(0, 1, 0));
//        VectorLW intersectPoint = plane.intersect(other).getSource();
//        System.out.println(intersectPoint);
//        System.out.println(plane.isOn(intersectPoint) + "\t" + plane.dispFrom(intersectPoint));
//        System.out.println(other.isOn(intersectPoint) + "\t" + plane.dispFrom(intersectPoint));

        plane = new BoundedPlane(new VectorLW(0, 0, 1), new VectorLW(0, 0, 0));
        bound1 = new HyperPlane(new VectorLW(1, 1, 0), new VectorLW(0, 1, 0));
        bound2 = new HyperPlane(new VectorLW(1, -1, 0), new VectorLW(0, 1, 0));
        bound3 = new HyperPlane(new VectorLW(0, 1, 0), new VectorLW(0, -1, 0));
//        HyperPlane bound4 = new HyperPlane(new VectorLW(1, -1, 0), new VectorLW(-2, 0, 0));
        plane.clip(bound1);         //  Will have no clips in its bounds
        plane.clip(bound2);           //  Will clip this and the other
        plane.clip(bound3);          //  Will clip this by both and each other by this
        System.out.println(plane);
        printBounds(plane, "\t");
//        plane.clip(bound4);
//          -8.750000000000027 -1.0400000000001697
        doGraphics();
        Scanner in = new Scanner(System.in);
        while (true) {
            VectorLW v = new VectorLW(in.nextDouble(), in.nextDouble(), 0);
            System.out.println(plane.isOn(v));
        }
    }


    public static void doGraphics() {
        ArrayList<VectorLW> vectors = new ArrayList<>();
        ArrayList<VectorLW> red = new ArrayList<>();
        ArrayList<VectorLW> green = new ArrayList<>();

        JFrame frame = new JFrame("Hey");
        final JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.black);
                g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
                g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
                g.setColor(Color.red);
                for (int i = 0; i < red.size(); i++) {
                    VectorLW v = red.get(i);
                    double x = v.getValues()[0] * 100;
                    double y = v.getValues()[1] * 100;
                    x += getWidth() / 2;
                    y += getHeight() / 2;
                    g.fillRect((int) x, getHeight() - ((int) y), 1, 1);
                }
                g.setColor(Color.green);
                for (int i = 0; i < green.size(); i++) {
                    VectorLW v = green.get(i);
                    double x = v.getValues()[0] * 100;
                    double y = v.getValues()[1] * 100;
                    x += getWidth() / 2;
                    y += getHeight() / 2;
                    g.fillRect((int) x, getHeight() - ((int) y), 1, 1);
                }
                g.setColor(Color.black);
                for (int i = 0; i < vectors.size(); i++) {
                    VectorLW v = vectors.get(i);
                    double x = v.getValues()[0] * 100;
                    double y = v.getValues()[1] * 100;
                    x += getWidth() / 2;
                    y += getHeight() / 2;
                    g.fillRect((int) x, getHeight() - ((int) y), 1, 1);
                }
                if (clickRay != null) {
                    VectorLW dir = clickRay.getDirection();
                    VectorLW point = clickRay.getSource();
                    g.drawString(clickRay.toString(), getWidth() / 2, 10);
                    int x = (int) (point.getValues()[0] * 100) + getWidth() / 2;
                    int y = (int) (point.getValues()[1] * 100) + getHeight() / 2;
                    g.drawLine(x, y, (int) (x + dir.getValues()[0]), (int) (y + dir.getValues()[1]));
                    int count = 0;
                    Ray planeSpaceRay = new Ray(plane.planeSpaceUnchecked(clickRay.getSource()), plane.planeSpaceUnchecked(clickRay.getDirection()));
                    for (BoundedPlane b : plane.getBounds()) {
                        if (planeSpaceRay.willIntersect(b)) {
                            VectorLW intersectPoint = planeSpaceRay.pointAtTime(planeSpaceRay.intersectTime(b));
                            if (b.isOn(intersectPoint)) {
                                count++;
                            }
                        }
                    }
                    g.drawString("" + count, getWidth() / 2 + 100, 100);
                }
                if (releasePoint != null) {
                    g.fillOval((int) (clickPoint.getValues()[0] * 100) + getWidth() / 2 - 10, (int) (clickPoint.getValues()[1] * 100) - 10 + getHeight() / 2, 20, 20);
                }
            }
        };
        frame.add(panel);
        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                double x = (e.getPoint().x - panel.getWidth() / 2) / 100.0;
                double y = (e.getPoint().y - panel.getHeight() / 2) / 100.0;
                clickPoint = new VectorLW(x, y, 0);
                releasePoint = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                double x = (e.getPoint().x - panel.getWidth() / 2) / 100.0;
                double y = (e.getPoint().y - panel.getHeight() / 2) / 100.0;
                VectorLW v = new VectorLW(x, y, 0);
                VectorLW dir = v.clone();
                dir.subtract(clickPoint);
                System.err.println(dir);
                dir.unitize();
                dir.scaleBy(1000);
                releasePoint = v.clone();
                clickRay = new Ray(clickPoint.clone(), dir);
                System.err.println(clickRay);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    frame.repaint();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

//        System.out.println(plane);
        for (double i = -10; i < 10; i += .005) {
            for (double j = -10; j < 10; j += .005) {
                VectorLW v = new VectorLW(i, j, 0);
                if (plane.isOn(v)) {
//                    red.add(v);
                }
                if (bound1 != null && bound1.isOn(v)) {
                    vectors.add(v);
                } else if (bound2 != null && bound2.isOn(v)) {
                    vectors.add(v);
                } else if (bound3 != null && bound3.isOn(v)) {
                    vectors.add(v);
                }
            }
        }
        System.out.println("Done!");
    }

    public static void printSubPlane(HyperPlane container, HyperPlane plane) {
        System.out.println(container.fromPlaneSpace(plane.getNormal()) + "\t:\t" + container.fromPlaneSpace(plane.getPoint()));
    }

    public static void printBounds(BoundedPlane bp, String spacing) {
        for (BoundedPlane b : bp.getBounds()) {
            System.out.println(spacing + (b.getNormal()) + "\t:\t" + (b.getPoint()));
            printBounds(b, spacing + "\t");
        }
    }

}
