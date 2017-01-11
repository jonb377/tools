package com.bolin.factory;

/**
 * Created by jonb3_000 on 7/18/2016.
 */
public interface FactoryInterface<E> {

    E getNew(Object... args);

}
