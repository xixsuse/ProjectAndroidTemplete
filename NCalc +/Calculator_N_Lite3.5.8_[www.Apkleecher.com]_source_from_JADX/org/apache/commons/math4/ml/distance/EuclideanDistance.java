package org.apache.commons.math4.ml.distance;

import org.apache.commons.math4.util.MathArrays;

public class EuclideanDistance implements DistanceMeasure {
    private static final long serialVersionUID = 1717556319784040040L;

    public double compute(double[] a, double[] b) {
        return MathArrays.distance(a, b);
    }
}
