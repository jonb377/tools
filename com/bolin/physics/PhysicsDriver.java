package com.bolin.physics;

import com.bolin.physics.normal.Physical;
import com.bolin.physics.staticphys.StaticPhysical;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public class PhysicsDriver {

    public static final double MAX_V = 1;

    private PhysicalSpace space;
    private double timeScale = 1;       //  seconds per run()
    private long delay;
    private Thread runThread;

    public PhysicsDriver() {
        space = new PhysicalSpace();
        runThread = new Thread() {

            boolean running = true;

            @Override
            public void interrupt() {
                running = false;
            }

            @Override
            public void run() {
                while (running) {
                    PhysicsDriver.this.run();
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void start(){
        runThread.start();
    }

    public void finish() {
        runThread.interrupt();
    }

    public void realTime() {
        timeScale = 1d / (double) delay;
        System.out.println("RealTime timeScale:\t" + timeScale);
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void addPhysical(Physical p) {
        space.add(p);
        p.setSpace(space);
    }

    public void addStaticPhysical(StaticPhysical p) {
        space.add(p);
        p.setSpace(space);
    }

    public void run() {
        space.getPhysicals().forEach(Physical::updateForce);
        space.getStaticPhysicals().forEach(staticPhysical -> staticPhysical.updateForce(timeScale));
        for (Physical p : space.getPhysicals()) {
            p.applyForce(timeScale);
        }
    }

}
