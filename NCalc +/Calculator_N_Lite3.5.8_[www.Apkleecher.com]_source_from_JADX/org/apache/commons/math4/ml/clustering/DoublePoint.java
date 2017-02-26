package org.apache.commons.math4.ml.clustering;

import java.io.Serializable;
import java.util.Arrays;

public class DoublePoint implements Clusterable, Serializable {
    private static final long serialVersionUID = 3946024775784901369L;
    private final double[] point;

    public DoublePoint(double[] point) {
        this.point = point;
    }

    public DoublePoint(int[] point) {
        this.point = new double[point.length];
        for (int i = 0; i < point.length; i++) {
            this.point[i] = (double) point[i];
        }
    }

    public double[] getPoint() {
        return this.point;
    }

    public boolean equals(Object other) {
        if (other instanceof DoublePoint) {
            return Arrays.equals(this.point, ((DoublePoint) other).point);
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.point);
    }

    public String toString() {
        return Arrays.toString(this.point);
    }
}
