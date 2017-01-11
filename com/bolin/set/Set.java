package com.bolin.set;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jonb3_000 on 7/14/2016.
 */
public class Set<I extends Item> extends Item implements Iterable<I> {

    private ArrayList<I> items;

    public Set() {
        items = new ArrayList<I>();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void add(I item) {
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    public ArrayList<I> getItems() {
        return new ArrayList<I>(items);
    }

    public Set<I> union(Set<I> other) {
        Set<I> union = new Set<>();
        for (I i : items) {
            union.add(i);
        }
        for (I i : other.items) {
            union.add(i);
        }
        return union;
    }

    public Set<I> intersect(Set<I> other) {
        Set<I> intersection = new Set<>();
        iloop:
        for (I i : items) {
            for (I j : other.items) {
                if (i.equals(j)) {
                    intersection.add(i);
                    continue iloop;
                }
            }
        }
        return intersection;
    }

    public void attributeAll(Attribute<I> a) {
        for (I i : items) {
            i.addAttribute(a);
        }
    }

    @Override
    public Iterator<I> iterator() {
        return new Iterator<I>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < items.size();
            }

            @Override
            public I next() {
                I item = items.get(index);
                index ++;
                return item;
            }
        };
    }
}
