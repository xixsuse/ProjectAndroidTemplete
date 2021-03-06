package org.apache.commons.math4.distribution;

import org.apache.commons.math4.exception.NotStrictlyPositiveException;
import org.apache.commons.math4.exception.OutOfRangeException;
import org.apache.commons.math4.exception.util.LocalizedFormats;
import org.apache.commons.math4.random.RandomGenerator;
import org.apache.commons.math4.random.Well19937c;
import org.apache.commons.math4.special.Gamma;
import org.apache.commons.math4.util.FastMath;

public class WeibullDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 8589540077390120676L;
    private double numericalMean;
    private boolean numericalMeanIsCalculated;
    private double numericalVariance;
    private boolean numericalVarianceIsCalculated;
    private final double scale;
    private final double shape;
    private final double solverAbsoluteAccuracy;

    public WeibullDistribution(double alpha, double beta) throws NotStrictlyPositiveException {
        this(alpha, beta, (double) DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    public WeibullDistribution(double alpha, double beta, double inverseCumAccuracy) {
        this(new Well19937c(), alpha, beta, inverseCumAccuracy);
    }

    public WeibullDistribution(RandomGenerator rng, double alpha, double beta) throws NotStrictlyPositiveException {
        this(rng, alpha, beta, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    public WeibullDistribution(RandomGenerator rng, double alpha, double beta, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        super(rng);
        this.numericalMean = Double.NaN;
        this.numericalMeanIsCalculated = false;
        this.numericalVariance = Double.NaN;
        this.numericalVarianceIsCalculated = false;
        if (alpha <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SHAPE, Double.valueOf(alpha));
        } else if (beta <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SCALE, Double.valueOf(beta));
        } else {
            this.scale = beta;
            this.shape = alpha;
            this.solverAbsoluteAccuracy = inverseCumAccuracy;
        }
    }

    public double getShape() {
        return this.shape;
    }

    public double getScale() {
        return this.scale;
    }

    public double density(double x) {
        if (x < 0.0d) {
            return 0.0d;
        }
        double xscale = x / this.scale;
        double xscalepow = FastMath.pow(xscale, this.shape - 1.0d);
        return ((this.shape / this.scale) * xscalepow) * FastMath.exp(-(xscalepow * xscale));
    }

    public double logDensity(double x) {
        if (x < 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        double xscale = x / this.scale;
        double logxscalepow = FastMath.log(xscale) * (this.shape - 1.0d);
        return (FastMath.log(this.shape / this.scale) + logxscalepow) - (FastMath.exp(logxscalepow) * xscale);
    }

    public double cumulativeProbability(double x) {
        if (x <= 0.0d) {
            return 0.0d;
        }
        return 1.0d - FastMath.exp(-FastMath.pow(x / this.scale, this.shape));
    }

    public double inverseCumulativeProbability(double p) {
        if (p < 0.0d || p > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Double.valueOf(0.0d), Double.valueOf(1.0d));
        } else if (p == 0.0d) {
            return 0.0d;
        } else {
            if (p == 1.0d) {
                return Double.POSITIVE_INFINITY;
            }
            return this.scale * FastMath.pow(-FastMath.log1p(-p), 1.0d / this.shape);
        }
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        if (!this.numericalMeanIsCalculated) {
            this.numericalMean = calculateNumericalMean();
            this.numericalMeanIsCalculated = true;
        }
        return this.numericalMean;
    }

    protected double calculateNumericalMean() {
        double sh = getShape();
        return FastMath.exp(Gamma.logGamma((1.0d / sh) + 1.0d)) * getScale();
    }

    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    protected double calculateNumericalVariance() {
        double sh = getShape();
        double sc = getScale();
        double mn = getNumericalMean();
        return ((sc * sc) * FastMath.exp(Gamma.logGamma(1.0d + (2.0d / sh)))) - (mn * mn);
    }

    public double getSupportLowerBound() {
        return 0.0d;
    }

    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public boolean isSupportConnected() {
        return true;
    }
}
