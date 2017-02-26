package org.apache.commons.math4.optim.nonlinear.scalar;

import org.apache.commons.math4.analysis.MultivariateFunction;
import org.apache.commons.math4.analysis.UnivariateFunction;
import org.apache.commons.math4.analysis.function.Logit;
import org.apache.commons.math4.analysis.function.Sigmoid;
import org.apache.commons.math4.exception.DimensionMismatchException;
import org.apache.commons.math4.exception.NumberIsTooSmallException;
import org.apache.commons.math4.util.FastMath;
import org.apache.commons.math4.util.MathUtils;

public class MultivariateFunctionMappingAdapter implements MultivariateFunction {
    private final MultivariateFunction bounded;
    private final Mapper[] mappers;

    private interface Mapper {
        double boundedToUnbounded(double d);

        double unboundedToBounded(double d);
    }

    private static class LowerBoundMapper implements Mapper {
        private final double lower;

        public LowerBoundMapper(double lower) {
            this.lower = lower;
        }

        public double unboundedToBounded(double y) {
            return this.lower + FastMath.exp(y);
        }

        public double boundedToUnbounded(double x) {
            return FastMath.log(x - this.lower);
        }
    }

    private static class LowerUpperBoundMapper implements Mapper {
        private final UnivariateFunction boundingFunction;
        private final UnivariateFunction unboundingFunction;

        public LowerUpperBoundMapper(double lower, double upper) {
            this.boundingFunction = new Sigmoid(lower, upper);
            this.unboundingFunction = new Logit(lower, upper);
        }

        public double unboundedToBounded(double y) {
            return this.boundingFunction.value(y);
        }

        public double boundedToUnbounded(double x) {
            return this.unboundingFunction.value(x);
        }
    }

    private static class NoBoundsMapper implements Mapper {
        private NoBoundsMapper() {
        }

        public double unboundedToBounded(double y) {
            return y;
        }

        public double boundedToUnbounded(double x) {
            return x;
        }
    }

    private static class UpperBoundMapper implements Mapper {
        private final double upper;

        public UpperBoundMapper(double upper) {
            this.upper = upper;
        }

        public double unboundedToBounded(double y) {
            return this.upper - FastMath.exp(-y);
        }

        public double boundedToUnbounded(double x) {
            return -FastMath.log(this.upper - x);
        }
    }

    public MultivariateFunctionMappingAdapter(MultivariateFunction bounded, double[] lower, double[] upper) {
        MathUtils.checkNotNull(lower);
        MathUtils.checkNotNull(upper);
        if (lower.length != upper.length) {
            throw new DimensionMismatchException(lower.length, upper.length);
        }
        int i;
        for (i = 0; i < lower.length; i++) {
            if (upper[i] < lower[i]) {
                throw new NumberIsTooSmallException(Double.valueOf(upper[i]), Double.valueOf(lower[i]), true);
            }
        }
        this.bounded = bounded;
        this.mappers = new Mapper[lower.length];
        for (i = 0; i < this.mappers.length; i++) {
            if (Double.isInfinite(lower[i])) {
                if (Double.isInfinite(upper[i])) {
                    this.mappers[i] = new NoBoundsMapper();
                } else {
                    this.mappers[i] = new UpperBoundMapper(upper[i]);
                }
            } else if (Double.isInfinite(upper[i])) {
                this.mappers[i] = new LowerBoundMapper(lower[i]);
            } else {
                this.mappers[i] = new LowerUpperBoundMapper(lower[i], upper[i]);
            }
        }
    }

    public double[] unboundedToBounded(double[] point) {
        double[] mapped = new double[this.mappers.length];
        for (int i = 0; i < this.mappers.length; i++) {
            mapped[i] = this.mappers[i].unboundedToBounded(point[i]);
        }
        return mapped;
    }

    public double[] boundedToUnbounded(double[] point) {
        double[] mapped = new double[this.mappers.length];
        for (int i = 0; i < this.mappers.length; i++) {
            mapped[i] = this.mappers[i].boundedToUnbounded(point[i]);
        }
        return mapped;
    }

    public double value(double[] point) {
        return this.bounded.value(unboundedToBounded(point));
    }
}
