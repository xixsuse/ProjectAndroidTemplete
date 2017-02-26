package org.apache.commons.math4.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math4.exception.MathIllegalArgumentException;
import org.apache.commons.math4.exception.MathIllegalStateException;
import org.apache.commons.math4.exception.NullArgumentException;
import org.apache.commons.math4.exception.util.LocalizedFormats;
import org.apache.commons.math4.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math4.stat.descriptive.StorelessUnivariateStatistic;
import org.apache.commons.math4.stat.descriptive.summary.SumOfLogs;
import org.apache.commons.math4.util.FastMath;
import org.apache.commons.math4.util.MathUtils;

public class GeometricMean extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = 20150412;
    private StorelessUnivariateStatistic sumOfLogs;

    public GeometricMean() {
        this.sumOfLogs = new SumOfLogs();
    }

    public GeometricMean(GeometricMean original) throws NullArgumentException {
        copy(original, this);
    }

    public GeometricMean(SumOfLogs sumOfLogs) {
        this.sumOfLogs = sumOfLogs;
    }

    public GeometricMean copy() {
        GeometricMean result = new GeometricMean();
        copy(this, result);
        return result;
    }

    public void increment(double d) {
        this.sumOfLogs.increment(d);
    }

    public double getResult() {
        if (this.sumOfLogs.getN() > 0) {
            return FastMath.exp(this.sumOfLogs.getResult() / ((double) this.sumOfLogs.getN()));
        }
        return Double.NaN;
    }

    public void clear() {
        this.sumOfLogs.clear();
    }

    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return FastMath.exp(this.sumOfLogs.evaluate(values, begin, length) / ((double) length));
    }

    public long getN() {
        return this.sumOfLogs.getN();
    }

    public void setSumLogImpl(StorelessUnivariateStatistic sumLogImpl) throws MathIllegalStateException {
        checkEmpty();
        this.sumOfLogs = sumLogImpl;
    }

    public StorelessUnivariateStatistic getSumLogImpl() {
        return this.sumOfLogs;
    }

    public static void copy(GeometricMean source, GeometricMean dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.sumOfLogs = source.sumOfLogs.copy();
    }

    private void checkEmpty() throws MathIllegalStateException {
        if (getN() > 0) {
            throw new MathIllegalStateException(LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, Long.valueOf(getN()));
        }
    }
}
