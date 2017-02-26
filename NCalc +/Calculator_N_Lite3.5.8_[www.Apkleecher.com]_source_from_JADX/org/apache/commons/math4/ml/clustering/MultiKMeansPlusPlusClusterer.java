package org.apache.commons.math4.ml.clustering;

import java.util.Collection;
import java.util.List;
import org.apache.commons.math4.exception.ConvergenceException;
import org.apache.commons.math4.exception.MathIllegalArgumentException;
import org.apache.commons.math4.ml.clustering.evaluation.ClusterEvaluator;
import org.apache.commons.math4.ml.clustering.evaluation.SumOfClusterVariances;

public class MultiKMeansPlusPlusClusterer<T extends Clusterable> extends Clusterer<T> {
    private final KMeansPlusPlusClusterer<T> clusterer;
    private final ClusterEvaluator<T> evaluator;
    private final int numTrials;

    public MultiKMeansPlusPlusClusterer(KMeansPlusPlusClusterer<T> clusterer, int numTrials) {
        this(clusterer, numTrials, new SumOfClusterVariances(clusterer.getDistanceMeasure()));
    }

    public MultiKMeansPlusPlusClusterer(KMeansPlusPlusClusterer<T> clusterer, int numTrials, ClusterEvaluator<T> evaluator) {
        super(clusterer.getDistanceMeasure());
        this.clusterer = clusterer;
        this.numTrials = numTrials;
        this.evaluator = evaluator;
    }

    public KMeansPlusPlusClusterer<T> getClusterer() {
        return this.clusterer;
    }

    public int getNumTrials() {
        return this.numTrials;
    }

    public ClusterEvaluator<T> getClusterEvaluator() {
        return this.evaluator;
    }

    public List<CentroidCluster<T>> cluster(Collection<T> points) throws MathIllegalArgumentException, ConvergenceException {
        List<CentroidCluster<T>> best = null;
        double bestVarianceSum = Double.POSITIVE_INFINITY;
        for (int i = 0; i < this.numTrials; i++) {
            List<CentroidCluster<T>> clusters = this.clusterer.cluster(points);
            double varianceSum = this.evaluator.score(clusters);
            if (this.evaluator.isBetterScore(varianceSum, bestVarianceSum)) {
                best = clusters;
                bestVarianceSum = varianceSum;
            }
        }
        return best;
    }
}
