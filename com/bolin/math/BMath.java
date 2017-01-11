package com.bolin.math;

/**
 * Created by jonb3_000 on 7/14/2016.
 */
public class BMath {

    private BMath(){}

    /**
     * @return Returns a random number in the range (-1, 1)
     */
    public static double drandom() {
        return 2 * Math.random() - 1;
    }

    /**
     * Returns a random integer between [0, max)
     * @param max The maximum value, this cannot be returned.
     * @return A random integer less than max and greater than or equal to zero.
     */
    public static int randomLess(int max) {
        return (int) (Math.random() * max);
    }

    /**
     * Calculates the partial sum of the geometric series up to 'steps' with the first term firstTerm and
     * common factor commonFactor.
     * @param firstTerm The first term in the sequence. Aka: What is the sequence at i = 0?
     * @param commonFactor The common ratio between each term.
     * @param steps The number of steps to carry out the sum. This will evaluate the sum from 0 to steps - 1, inclusive.
     * @return The partial sum of the geometric series.
     */
    public static double geometricSeries(double firstTerm, double commonFactor, int steps) {
        return firstTerm * (1 - Math.pow(commonFactor, steps)) / (1 - commonFactor);
    }

    /**
     * Calculates the partial sum of the arithmetic series up to 'steps' with the first term firstTerm and
     * common difference commonDifference.
     * @param firstTerm The first term in the sequence. What is the sequence at i = 0?
     * @param commonDifference The common difference between each term.
     * @param steps The number of steps to carry out the sum. This will evaluate the sum from 0 to steps - 1, inclusive.
     * @return The partial sum of the arithmetic series.
     */
    public static double arithmeticSeries(double firstTerm, double commonDifference, int steps) {
        return steps * (2 * firstTerm + commonDifference * steps - commonDifference) / 2;
    }

    public static double pythagorize(double... values) {
        double sum = 0;
        for (int i = 0;i < values.length;i ++) {
            sum += values[i] * values[i];
        }
        return Math.sqrt(sum);
    }
}
