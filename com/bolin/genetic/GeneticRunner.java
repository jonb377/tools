package com.bolin.genetic;

import com.bolin.math.BMath;

import java.util.ArrayList;

/**
 * Created by jonb3_000 on 7/15/2016.
 */
public class GeneticRunner<C extends AbstractChromosome> {

    private C[] chromosomes;
    private SimulatorInterface<C> simulator;
    private final int population;
    private final ChromosomeFactoryInterface<C> factory;

    public GeneticRunner(SimulatorInterface simulator, int population, ChromosomeFactoryInterface<C> factory) {
        this.simulator = simulator;
        this.population = population;
        chromosomes = (C[]) new AbstractChromosome[population];
        for (int i = 0;i < population;i ++) {
            chromosomes[i] = factory.getRandom();
        }
        this.factory = factory;
    }

    public AbstractChromosome runGenerations(int genCount) {
        for (int i = 0;i < genCount;i ++) {
            run();
            sort();
            reproduce();
        }
        return chromosomes[0];
    }


    private void run() {
        for (C c : chromosomes) {
            simulator.reset();
            c.setScore(simulator.runOn(c));
        }
    }

    private void sort() {
        for (int i = 0;i < chromosomes.length - 1;i ++) {
            int best = i;
            for (int j = i + 1;j < chromosomes.length;j ++) {
                if (chromosomes[j].getScore() > chromosomes[best].getScore()) {
                    best = j;
                }
            }
            C temp = chromosomes[i];
            chromosomes[i] = chromosomes[best];
            chromosomes[best] = temp;
        }
    }

    private void reproduce() {
        int mid = population / 2;
        int keep = population / 10;
        int toss = 9 * keep;
        for (int i = mid;i < toss;i ++) {
            ArrayList<C> compatible = new ArrayList<C>();
            for (int j = 0;j < mid;j ++) {
                if (chromosomes[i].isCompatible(chromosomes[j])) {
                    compatible.add(chromosomes[j]);
                }
            }
            if (compatible.isEmpty()) {
                chromosomes[i].mutate();
            } else {
                int mate = BMath.randomLess(compatible.size());
                chromosomes[i] = (C) chromosomes[i].mateWith(compatible.get(mate));
            }
        }
        for (int i = toss;i < population;i ++) {
            chromosomes[i] = factory.getRandom();
        }
    }
}
