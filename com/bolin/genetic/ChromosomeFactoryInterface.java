package com.bolin.genetic;

/**
 * Created by jonb3_000 on 7/15/2016.
 */
public interface ChromosomeFactoryInterface<C extends AbstractChromosome> {

    /**
     * @return Returns a new AbstractChromosome that is randomly initialized.
     */
    C getRandom();

}
