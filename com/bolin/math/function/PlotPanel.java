package com.bolin.math.function;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by jonb3_000 on 8/22/2016.
 */

public class PlotPanel extends JPanel implements MouseMotionListener, MouseListener {

    public static final int EDGE_BUFFER = 50;

    private Point mousePoint;
    private Function func;
    private double minx, maxx, miny, maxy;
    private double[] values;
    private boolean painting;

    public PlotPanel(Function f, double minx, double maxx) {
        func = f;
        this.minx = minx;
        this.maxx = maxx;
        addMouseListener(this);
        addMouseMotionListener(this);
        painting = false;
    }

    public void reevaluate() {
        initValues();
        repaint();
    }

    private void initValues() {
        int width = getWidth() - 2 * EDGE_BUFFER;
        values = new double[width];
        double xstep = (maxx - minx) / width;
        miny = Double.POSITIVE_INFINITY;
        maxy = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < values.length; i++) {
            double x = minx + xstep * i;
            values[i] = func.of(x);
            if (Double.isFinite(values[i])) {
                if (values[i] < miny) {
                    miny = values[i];
                } else if (values[i] > maxy) {
                    maxy = values[i];
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (!painting) {
            painting = true;
            super.paint(g);
            if (values == null) {
                initValues();
            }
            int height = getHeight() - 2 * EDGE_BUFFER;
            int width = getWidth() - 2 * EDGE_BUFFER;
            double ystep = height / (maxy - miny);
            g.setColor(Color.gray);
            int yaxisx, xaxisy;
            if (minx >= 0) {
                yaxisx = EDGE_BUFFER;
            } else if (maxx <= 0) {
                yaxisx = getWidth() - EDGE_BUFFER;
            } else {
                double dx = maxx - minx;
                double prop = Math.abs(maxx / dx);
                int zero = (int) (prop * width);
                yaxisx = width - zero + EDGE_BUFFER;
            }
            g.drawLine(yaxisx, EDGE_BUFFER, yaxisx, getHeight() - EDGE_BUFFER);
            if (miny >= 0) {
                xaxisy = getHeight() - EDGE_BUFFER;
            } else if (maxy <= 0) {
                xaxisy = EDGE_BUFFER;
                g.drawLine(EDGE_BUFFER, EDGE_BUFFER, getWidth() - EDGE_BUFFER, EDGE_BUFFER);
            } else {
                double dy = maxy - miny;
                double prop = Math.abs(miny / dy);
                int zero = (int) (prop * height);
                xaxisy = height - zero + EDGE_BUFFER;
            }
            g.drawLine(EDGE_BUFFER, xaxisy, getWidth() - EDGE_BUFFER, xaxisy);
            g.drawString("" + miny, yaxisx, getHeight() - EDGE_BUFFER + 10);
            g.drawString("" + maxy, yaxisx, EDGE_BUFFER - 10);
            g.drawString("" + minx, EDGE_BUFFER - 10, xaxisy);
            g.drawString("" + maxx, getWidth() - EDGE_BUFFER + 10, xaxisy);
            g.setColor(Color.black);
            for (int x = 0; x < values.length - 1; x++) {
                if (Double.isFinite(values[x]) || Double.isNaN(values[x])) {
                    if (!Double.isNaN(values[x + 1])) {
                        int y = (int) ((values[x] - miny) * ystep);
                        int nexty = (int) ((values[x + 1] - miny) * ystep);
                        if (!Double.isNaN(values[x])) {
                            g.drawLine(x + EDGE_BUFFER, height - y + EDGE_BUFFER, x + 1 + EDGE_BUFFER, height - nexty + EDGE_BUFFER);
                        } else {
                            g.drawOval(x + EDGE_BUFFER - 3, height - nexty + EDGE_BUFFER - 3, 6, 6);
                        }
                    }
                }
            }
            double xstep = (maxx - minx) / width;
            if (mousePoint != null) {
                int x = mousePoint.x - EDGE_BUFFER;
                if (x < values.length && x >= 0) {
                    if (!Double.isNaN(values[x])) {
                        int y = (int) ((values[x] - miny) * ystep);
                        double realx = xstep * x + minx;
                        g.drawString("(" + realx + ", " + values[x] + ")", mousePoint.x, mousePoint.y);
                        g.setColor(Color.gray);
                        int ploty = height - y + EDGE_BUFFER;
                        g.drawLine(mousePoint.x, mousePoint.y + 15, mousePoint.x, ploty);
                        g.drawOval(mousePoint.x - 5, ploty - 5, 10, 10);
                    } else {
                        mousePoint = null;
                    }
                } else {
                    mousePoint = null;
                }
            }
            painting = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePoint = new Point(e.getPoint().x, e.getPoint().y - 15);
        if (!painting) {
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        mousePoint = null;
    }

    class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}