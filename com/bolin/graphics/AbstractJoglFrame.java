package com.bolin.graphics;

import com.bolin.math.vector.PolarVector3D;
import com.bolin.math.vector.Vector3D;
import com.bolin.math.vector.VectorLW;
import com.bolin.ui.KeyManager;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Provides basic framework for 3D graphics with interactive viewpoint.
 */
public abstract class AbstractJoglFrame implements KeyListener {

    protected double moveSpeed = 1;
    protected double angleSpeed = Math.PI / 50;

    private JoglCanvas canvas;
    private KeyManager keyManager;
    private JFrame frame;
    private boolean fullScreen;
    private Thread keyListenThread;

    public AbstractJoglFrame() {
        fullScreen = false;
        frame = new JFrame();
        canvas = new JoglCanvas();
        frame.add(canvas);
        frame.addKeyListener(this);
        canvas.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        keyManager = new KeyManager();
        keyListenThread = new Thread() {

            private boolean running = true;

            @Override
            public void interrupt() {
                running = false;
            }

            @Override
            public void run() {
                while (running) {
                    checkKeyEvents();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        keyListenThread.start();
    }

    public AbstractJoglFrame(String name) {
        this();
        frame.setTitle(name);
    }

    public JoglCanvas getCanvas() {
        return canvas;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void toggleFullscreen() {
//        fullScreen = !fullScreen;
//        JFrame oldFrame = frame;
//        frame = new JFrame(oldFrame.getTitle());
//        oldFrame.dispose();
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setUndecorated(fullScreen);
//        frame.add(canvas);
//        frame.addKeyListener(this);
//        frame.setVisible(true);
        throw new RuntimeException("Fullscreen not supported yet.");
    }

    protected void checkKeyEvents() {
        PolarVector3D v = canvas.getViewDirection();
        Vector3D move = new Vector3D(0, 0, 0);
        Vector3D right = v.cross(new Vector3D(0, 0, 1));
        Vector3D left = right.clone();
        left.scaleBy(-1);
        Vector3D down = v.cross(left);
        Vector3D up = down.clone();
        up.scaleBy(-1);
        double dtheta = 0, dphi = 0;
        if (keyManager.isPressed('a')) {
            move.add(left);
        }
        if (keyManager.isPressed('d')) {
            move.add(right);
        }
        if (keyManager.isPressed('w')) {
            move.subtract(v);
        }
        if (keyManager.isPressed('s')) {
            move.add(v);
        }
        if (keyManager.isPressed('e')) {
            move.add(up);
        }
        if (keyManager.isPressed('q')) {
            move.add(down);
        }
        if (keyManager.isPressed('i')) {
            dphi -= angleSpeed;
        }
        if (keyManager.isPressed('k')) {
            dphi += angleSpeed;
        }
        if (keyManager.isPressed('j')) {
            dtheta -= angleSpeed;
        }
        if (keyManager.isPressed('l')) {
            dtheta += angleSpeed;
        }
        move.unitize();
        move.scaleBy(moveSpeed);
        if (move.magnitude() > 0) {
            canvas.changeViewpoint(new VectorLW(move.x(), move.z(), move.y()));
        }
        double dangle = Math.sqrt(dphi * dphi + dtheta * dtheta);
        if (dangle > 0) {
            dtheta *= angleSpeed / dangle;
            dphi *= angleSpeed / dangle;
            canvas.changeViewDirection(dtheta, dphi);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keyManager.press(e.getKeyChar());
        if (e.getKeyChar() == 'f') {
            toggleFullscreen();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyManager.release(e.getKeyChar());
    }
}
