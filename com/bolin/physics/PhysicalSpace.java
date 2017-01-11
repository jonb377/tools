package com.bolin.physics;

import com.bolin.physics.normal.Physical;
import com.bolin.physics.staticphys.StaticPhysical;

import java.util.ArrayList;

/**
 * Created by jonb3_000 on 7/21/2016.
 */
public class PhysicalSpace {

    private ArrayList<Physical> physicals;
    private ArrayList<StaticPhysical> staticPhysicals;

    public PhysicalSpace() {
        physicals = new ArrayList<>();
        staticPhysicals = new ArrayList<>();
    }

    public void add(Physical p) {
        physicals.add(p);
    }

    public void add(StaticPhysical p) {
        staticPhysicals.add(p);
    }

    public void remove(Physical p) {
        physicals.remove(p);
    }

    public void remove(StaticPhysical p) {
        staticPhysicals.remove(p);
    }

    public ArrayList<Physical> getPhysicals() {
        return physicals;
    }

    public ArrayList<StaticPhysical> getStaticPhysicals() {
        return staticPhysicals;
    }

}
