package org.apache.commons.math4.analysis.differentiation;

import org.apache.commons.math4.analysis.MultivariateVectorFunction;

public class GradientFunction implements MultivariateVectorFunction {
    private final MultivariateDifferentiableFunction f;

    public GradientFunction(MultivariateDifferentiableFunction f) {
        this.f = f;
    }

    public double[] value(double[] point) {
        int i;
        DerivativeStructure[] dsX = new DerivativeStructure[point.length];
        for (i = 0; i < point.length; i++) {
            dsX[i] = new DerivativeStructure(point.length, 1, i, point[i]);
        }
        DerivativeStructure dsY = this.f.value(dsX);
        double[] y = new double[point.length];
        int[] orders = new int[point.length];
        for (i = 0; i < point.length; i++) {
            orders[i] = 1;
            y[i] = dsY.getPartialDerivative(orders);
            orders[i] = 0;
        }
        return y;
    }
}
