package com.bolin.set;

import java.util.ArrayList;

/**
 * Created by jonb3_000 on 7/14/2016.
 */
public class Item {

    private Set<Attribute> attributes;

    public Item() {
        attributes = new Set<>();
    }

    public void addAttribute(Attribute a) {
        attributes.add(a);
    }

    public boolean has(Attribute a) {
        for (Attribute attribute : attributes) {
            if (a.equals(attribute)) {
                return true;
            }
        }
        return false;
    }

}
