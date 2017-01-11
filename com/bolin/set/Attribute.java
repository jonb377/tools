package com.bolin.set;

/**
 * Created by jonb3_000 on 7/14/2016.
 */
public abstract class Attribute<I extends Item> extends Item {

    public abstract boolean equals(Attribute<I> other);

}
