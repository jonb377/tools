package com.bolin.set;

import java.util.ArrayList;

/**
 * Created by jonb3_000 on 7/14/2016.
 */
public class Filter<I extends Item> {

    private ArrayList<Attribute<? super I>> targetAttributes;

    public Filter() {
        targetAttributes = new ArrayList<>();
    }

    public void addTarget(Attribute<I> a) {
        targetAttributes.add(a);
    }

    public Set<I> apply(Set<I> target) {
        Set<I> newset = new Set<I>();
        ArrayList<I> items = target.getItems();
        outer:
        for (int i = 0;i < items.size();i ++) {
            for (int j = 0;j < targetAttributes.size();j ++) {
                if (!items.get(i).has(targetAttributes.get(j))) {
                    continue outer;
                }
            }
            newset.add(items.get(i));
        }
        return newset;
    }

}
