package org.apache.commons.math4.stat.descriptive.rank;

import java.io.Serializable;
import org.apache.commons.math4.exception.MathIllegalArgumentException;
import org.apache.commons.math4.exception.NullArgumentException;
import org.apache.commons.math4.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math4.util.MathArrays;
import org.apache.commons.math4.util.MathUtils;

public class Max extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = 20150412;
    private long n;
    private double value;

    public Max() {
        this.n = 0;
        this.value = Double.NaN;
    }

    public Max(Max original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        if (d > this.value || Double.isNaN(this.value)) {
            this.value = d;
        }
        this.n++;
    }

    public void clear() {
        this.value = Double.NaN;
        this.n = 0;
    }

    public double getResult() {
        return this.value;
    }

    public long getN() {
        return this.n;
    }

    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        double max = Double.NaN;
        if (MathArrays.verifyValues(values, begin, length)) {
            max = values[begin];
            int i = begin;
            while (i < begin + length) {
                if (!Double.isNaN(values[i]) && max <= values[i]) {
                    max = values[i];
                }
                i++;
            }
        }
        return max;
    }

    public Max copy() {
        Max result = new Max();
        copy(this, result);
        return result;
    }

    public static void copy(Max source, Max dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.n = source.n;
        dest.value = source.value;
    }
}
