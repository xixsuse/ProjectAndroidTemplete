package org.apache.commons.math4.analysis.integration.gauss;

import java.math.BigDecimal;
import java.math.MathContext;
import org.apache.commons.math4.exception.DimensionMismatchException;
import org.apache.commons.math4.util.Pair;

public class LegendreHighPrecisionRuleFactory extends BaseRuleFactory<BigDecimal> {
    private final MathContext mContext;
    private final BigDecimal minusOne;
    private final BigDecimal oneHalf;
    private final BigDecimal two;

    public LegendreHighPrecisionRuleFactory() {
        this(MathContext.DECIMAL128);
    }

    public LegendreHighPrecisionRuleFactory(MathContext mContext) {
        this.mContext = mContext;
        this.two = new BigDecimal("2", mContext);
        this.minusOne = new BigDecimal("-1", mContext);
        this.oneHalf = new BigDecimal("0.5", mContext);
    }

    protected Pair<BigDecimal[], BigDecimal[]> computeRule(int numberOfPoints) throws DimensionMismatchException {
        if (numberOfPoints == 1) {
            BigDecimal[] bigDecimalArr = new BigDecimal[]{BigDecimal.ZERO};
            BigDecimal[] bigDecimalArr2 = new BigDecimal[1];
            bigDecimalArr2[0] = this.two;
            return new Pair(bigDecimalArr, bigDecimalArr2);
        }
        BigDecimal[] previousPoints = (BigDecimal[]) getRuleInternal(numberOfPoints - 1).getFirst();
        Object points = new BigDecimal[numberOfPoints];
        Object weights = new BigDecimal[numberOfPoints];
        int iMax = numberOfPoints / 2;
        int i = 0;
        while (i < iMax) {
            int j;
            BigDecimal a = i == 0 ? this.minusOne : previousPoints[i - 1];
            BigDecimal b = iMax == 1 ? BigDecimal.ONE : previousPoints[i];
            BigDecimal pma = BigDecimal.ONE;
            BigDecimal pa = a;
            BigDecimal pmb = BigDecimal.ONE;
            BigDecimal pb = b;
            for (j = 1; j < numberOfPoints; j++) {
                BigDecimal b_two_j_p_1 = new BigDecimal((j * 2) + 1, this.mContext);
                BigDecimal b_j = new BigDecimal(j, this.mContext);
                BigDecimal b_j_p_1 = new BigDecimal(j + 1, this.mContext);
                BigDecimal ppa = pa.multiply(a.multiply(b_two_j_p_1, this.mContext), this.mContext).subtract(pma.multiply(b_j, this.mContext), this.mContext).divide(b_j_p_1, this.mContext);
                pma = pa;
                pa = ppa;
                pmb = pb;
                pb = pb.multiply(b.multiply(b_two_j_p_1, this.mContext), this.mContext).subtract(pmb.multiply(b_j, this.mContext), this.mContext).divide(b_j_p_1, this.mContext);
            }
            BigDecimal c = a.add(b, this.mContext).multiply(this.oneHalf, this.mContext);
            BigDecimal pmc = BigDecimal.ONE;
            BigDecimal pc = c;
            boolean done = false;
            while (!done) {
                done = b.subtract(a, this.mContext).compareTo(c.ulp().multiply(BigDecimal.TEN, this.mContext)) <= 0;
                pmc = BigDecimal.ONE;
                pc = c;
                for (j = 1; j < numberOfPoints; j++) {
                    pmc = pc;
                    pc = pc.multiply(c.multiply(new BigDecimal((j * 2) + 1, this.mContext), this.mContext), this.mContext).subtract(pmc.multiply(new BigDecimal(j, this.mContext), this.mContext), this.mContext).divide(new BigDecimal(j + 1, this.mContext), this.mContext);
                }
                if (!done) {
                    if (pa.signum() * pc.signum() <= 0) {
                        b = c;
                        pmb = pmc;
                        pb = pc;
                    } else {
                        a = c;
                        pma = pmc;
                        pa = pc;
                    }
                    c = a.add(b, this.mContext).multiply(this.oneHalf, this.mContext);
                }
            }
            BigDecimal tmp2 = BigDecimal.ONE.subtract(c.pow(2, this.mContext), this.mContext).multiply(this.two, this.mContext).divide(pmc.subtract(c.multiply(pc, this.mContext), this.mContext).multiply(new BigDecimal(numberOfPoints, this.mContext)).pow(2, this.mContext), this.mContext);
            points[i] = c;
            weights[i] = tmp2;
            int idx = (numberOfPoints - i) - 1;
            points[idx] = c.negate(this.mContext);
            weights[idx] = tmp2;
            i++;
        }
        if (numberOfPoints % 2 != 0) {
            pmc = BigDecimal.ONE;
            for (j = 1; j < numberOfPoints; j += 2) {
                pmc = pmc.multiply(new BigDecimal(j, this.mContext), this.mContext).divide(new BigDecimal(j + 1, this.mContext), this.mContext).negate(this.mContext);
            }
            tmp2 = this.two.divide(pmc.multiply(new BigDecimal(numberOfPoints, this.mContext), this.mContext).pow(2, this.mContext), this.mContext);
            points[iMax] = BigDecimal.ZERO;
            weights[iMax] = tmp2;
        }
        return new Pair(points, weights);
    }
}
