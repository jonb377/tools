package com.bolin.genetic;

/**
 * Created by jonb3_000 on 7/15/2016.
 */
public interface SimulatorInterface<C extends AbstractChromosome> {

    /**
     * Runs the simulation with the chromosome's specifications.
     * @param c The chromosome to be tested.
     * @return Returns the fitness score of the chromosome.
     */
    double runOn(C c);

    /**
     * Resets the simulator so it is ready to test a new chromosome.
     */
    void reset();

}
