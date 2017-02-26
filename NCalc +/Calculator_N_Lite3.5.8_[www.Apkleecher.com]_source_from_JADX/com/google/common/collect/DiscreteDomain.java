package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import org.apache.commons.math4.analysis.integration.BaseAbstractUnivariateIntegrator;

@GwtCompatible
@Beta
public abstract class DiscreteDomain<C extends Comparable> {

    private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
        private static final BigIntegerDomain INSTANCE;
        private static final BigInteger MAX_LONG;
        private static final BigInteger MIN_LONG;
        private static final long serialVersionUID = 0;

        private BigIntegerDomain() {
        }

        static {
            INSTANCE = new BigIntegerDomain();
            MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
            MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
        }

        public BigInteger next(BigInteger value) {
            return value.add(BigInteger.ONE);
        }

        public BigInteger previous(BigInteger value) {
            return value.subtract(BigInteger.ONE);
        }

        public long distance(BigInteger start, BigInteger end) {
            return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public String toString() {
            return "DiscreteDomain.bigIntegers()";
        }
    }

    private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
        private static final IntegerDomain INSTANCE;
        private static final long serialVersionUID = 0;

        private IntegerDomain() {
        }

        static {
            INSTANCE = new IntegerDomain();
        }

        public Integer next(Integer value) {
            int i = value.intValue();
            return i == BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT ? null : Integer.valueOf(i + 1);
        }

        public Integer previous(Integer value) {
            int i = value.intValue();
            return i == RtlSpacingHelper.UNDEFINED ? null : Integer.valueOf(i - 1);
        }

        public long distance(Integer start, Integer end) {
            return ((long) end.intValue()) - ((long) start.intValue());
        }

        public Integer minValue() {
            return Integer.valueOf(RtlSpacingHelper.UNDEFINED);
        }

        public Integer maxValue() {
            return Integer.valueOf(BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT);
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public String toString() {
            return "DiscreteDomain.integers()";
        }
    }

    private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
        private static final LongDomain INSTANCE;
        private static final long serialVersionUID = 0;

        private LongDomain() {
        }

        static {
            INSTANCE = new LongDomain();
        }

        public Long next(Long value) {
            long l = value.longValue();
            return l == Long.MAX_VALUE ? null : Long.valueOf(1 + l);
        }

        public Long previous(Long value) {
            long l = value.longValue();
            return l == Long.MIN_VALUE ? null : Long.valueOf(l - 1);
        }

        public long distance(Long start, Long end) {
            long result = end.longValue() - start.longValue();
            if (end.longValue() > start.longValue() && result < 0) {
                return Long.MAX_VALUE;
            }
            if (end.longValue() >= start.longValue() || result <= 0) {
                return result;
            }
            return Long.MIN_VALUE;
        }

        public Long minValue() {
            return Long.valueOf(Long.MIN_VALUE);
        }

        public Long maxValue() {
            return Long.valueOf(Long.MAX_VALUE);
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public String toString() {
            return "DiscreteDomain.longs()";
        }
    }

    public abstract long distance(C c, C c2);

    public abstract C next(C c);

    public abstract C previous(C c);

    public static DiscreteDomain<Integer> integers() {
        return IntegerDomain.INSTANCE;
    }

    public static DiscreteDomain<Long> longs() {
        return LongDomain.INSTANCE;
    }

    public static DiscreteDomain<BigInteger> bigIntegers() {
        return BigIntegerDomain.INSTANCE;
    }

    protected DiscreteDomain() {
    }

    public C minValue() {
        throw new NoSuchElementException();
    }

    public C maxValue() {
        throw new NoSuchElementException();
    }
}
