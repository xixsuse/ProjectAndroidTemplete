package org.apache.commons.math4.analysis.function;

import org.apache.commons.math4.analysis.BivariateFunction;

public class Multiply implements BivariateFunction {
    public double value(double x, double y) {
        return x * y;
    }
}
