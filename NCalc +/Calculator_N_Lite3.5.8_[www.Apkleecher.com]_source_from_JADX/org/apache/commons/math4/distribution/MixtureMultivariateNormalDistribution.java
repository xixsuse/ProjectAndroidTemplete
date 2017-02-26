package org.apache.commons.math4.distribution;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math4.exception.DimensionMismatchException;
import org.apache.commons.math4.exception.NotPositiveException;
import org.apache.commons.math4.random.RandomGenerator;
import org.apache.commons.math4.util.Pair;

public class MixtureMultivariateNormalDistribution extends MixtureMultivariateRealDistribution<MultivariateNormalDistribution> {
    public MixtureMultivariateNormalDistribution(double[] weights, double[][] means, double[][][] covariances) {
        super(createComponents(weights, means, covariances));
    }

    public MixtureMultivariateNormalDistribution(List<Pair<Double, MultivariateNormalDistribution>> components) {
        super(components);
    }

    public MixtureMultivariateNormalDistribution(RandomGenerator rng, List<Pair<Double, MultivariateNormalDistribution>> components) throws NotPositiveException, DimensionMismatchException {
        super(rng, components);
    }

    private static List<Pair<Double, MultivariateNormalDistribution>> createComponents(double[] weights, double[][] means, double[][][] covariances) {
        List<Pair<Double, MultivariateNormalDistribution>> mvns = new ArrayList(weights.length);
        for (int i = 0; i < weights.length; i++) {
            mvns.add(new Pair(Double.valueOf(weights[i]), new MultivariateNormalDistribution(means[i], covariances[i])));
        }
        return mvns;
    }
}
