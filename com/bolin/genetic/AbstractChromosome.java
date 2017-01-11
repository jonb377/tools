package com.bolin.genetic;

/**
 * Created by jonb3_000 on 7/15/2016.
 */
public abstract class AbstractChromosome {

    private static int ID_COUNTER = 0;

    public final int ID;
    private double score;

    public AbstractChromosome() {
        ID = ID_COUNTER ++;
        score = 0;
    }

    /**
     * Sets this chromosome's fitness score.
     * @param d The new score.
     */
    public void setScore(double d) {
        score = d;
    }

    /**
     * @return Returns this chromosome's fitness score.
     */
    public double getScore() {
        return score;
    }

    /**
     * Mutates the values in this chromosome by a small amount.
     */
    public abstract void mutate();

    /**
     * Returns a new chromosome with the combined traits of this and other.
     * @param other The other chromosome
     * @return A new chromosome of the same type as this.
     */
    public abstract AbstractChromosome mateWith(AbstractChromosome other);

    /**
     * Tells if this chromosome is compatible with the other for mating.
     * @param other The other chromosome.
     * @return True if the two can mate, false otherwise.
     */
    public abstract boolean isCompatible(AbstractChromosome other);

}
