package org.apache.commons.math4.analysis;

public interface ParametricUnivariateFunction {
    double[] gradient(double d, double... dArr);

    double value(double d, double... dArr);
}
