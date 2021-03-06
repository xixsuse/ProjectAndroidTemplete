package org.apache.commons.math4.optim;

import org.apache.commons.math4.analysis.integration.BaseAbstractUnivariateIntegrator;
import org.apache.commons.math4.exception.NotStrictlyPositiveException;

public class MaxEval implements OptimizationData {
    private final int maxEval;

    public MaxEval(int max) {
        if (max <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(max));
        }
        this.maxEval = max;
    }

    public int getMaxEval() {
        return this.maxEval;
    }

    public static MaxEval unlimited() {
        return new MaxEval(BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT);
    }
}
