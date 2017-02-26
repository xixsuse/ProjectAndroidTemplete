package org.apache.commons.math4.optim;

import java.util.Arrays;

public class SimpleBounds implements OptimizationData {
    private final double[] lower;
    private final double[] upper;

    public SimpleBounds(double[] lB, double[] uB) {
        this.lower = (double[]) lB.clone();
        this.upper = (double[]) uB.clone();
    }

    public double[] getLower() {
        return (double[]) this.lower.clone();
    }

    public double[] getUpper() {
        return (double[]) this.upper.clone();
    }

    public static SimpleBounds unbounded(int dim) {
        double[] lB = new double[dim];
        Arrays.fill(lB, Double.NEGATIVE_INFINITY);
        double[] uB = new double[dim];
        Arrays.fill(uB, Double.POSITIVE_INFINITY);
        return new SimpleBounds(lB, uB);
    }
}
