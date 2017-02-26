package org.apache.commons.math4.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.math4.exception.DimensionMismatchException;
import org.apache.commons.math4.exception.MathInternalError;
import org.apache.commons.math4.exception.OutOfRangeException;
import org.apache.commons.math4.random.ValueServer;

public class Combinations implements Iterable<int[]> {
    private static /* synthetic */ int[] $SWITCH_TABLE$org$apache$commons$math4$util$Combinations$IterationOrder;
    private final IterationOrder iterationOrder;
    private final int k;
    private final int n;

    private enum IterationOrder {
        LEXICOGRAPHIC
    }

    private static class LexicographicComparator implements Comparator<int[]>, Serializable {
        private static final long serialVersionUID = 20130906;
        private final int k;
        private final int n;

        public LexicographicComparator(int n, int k) {
            this.n = n;
            this.k = k;
        }

        public int compare(int[] c1, int[] c2) {
            if (c1.length != this.k) {
                throw new DimensionMismatchException(c1.length, this.k);
            } else if (c2.length != this.k) {
                throw new DimensionMismatchException(c2.length, this.k);
            } else {
                int[] c1s = MathArrays.copyOf(c1);
                Arrays.sort(c1s);
                int[] c2s = MathArrays.copyOf(c2);
                Arrays.sort(c2s);
                long v1 = lexNorm(c1s);
                long v2 = lexNorm(c2s);
                if (v1 < v2) {
                    return -1;
                }
                if (v1 > v2) {
                    return 1;
                }
                return 0;
            }
        }

        private long lexNorm(int[] c) {
            long ret = 0;
            for (int i = 0; i < c.length; i++) {
                int digit = c[i];
                if (digit < 0 || digit >= this.n) {
                    throw new OutOfRangeException(Integer.valueOf(digit), Integer.valueOf(0), Integer.valueOf(this.n - 1));
                }
                ret += (long) (c[i] * ArithmeticUtils.pow(this.n, i));
            }
            return ret;
        }
    }

    private static class LexicographicIterator implements Iterator<int[]> {
        private final int[] c;
        private int j;
        private final int k;
        private boolean more;

        public LexicographicIterator(int n, int k) {
            this.more = true;
            this.k = k;
            this.c = new int[(k + 3)];
            if (k == 0 || k >= n) {
                this.more = false;
                return;
            }
            for (int i = 1; i <= k; i++) {
                this.c[i] = i - 1;
            }
            this.c[k + 1] = n;
            this.c[k + 2] = 0;
            this.j = k;
        }

        public boolean hasNext() {
            return this.more;
        }

        public int[] next() {
            if (this.more) {
                int[] ret = new int[this.k];
                System.arraycopy(this.c, 1, ret, 0, this.k);
                int x = 0;
                if (this.j > 0) {
                    this.c[this.j] = this.j;
                    this.j--;
                } else if (this.c[1] + 1 < this.c[2]) {
                    int[] iArr = this.c;
                    iArr[1] = iArr[1] + 1;
                } else {
                    this.j = 2;
                    boolean stepDone = false;
                    while (!stepDone) {
                        this.c[this.j - 1] = this.j - 2;
                        x = this.c[this.j] + 1;
                        if (x == this.c[this.j + 1]) {
                            this.j++;
                        } else {
                            stepDone = true;
                        }
                    }
                    if (this.j > this.k) {
                        this.more = false;
                    } else {
                        this.c[this.j] = x;
                        this.j--;
                    }
                }
                return ret;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class SingletonIterator implements Iterator<int[]> {
        private boolean more;
        private final int[] singleton;

        public SingletonIterator(int[] singleton) {
            this.more = true;
            this.singleton = singleton;
        }

        public boolean hasNext() {
            return this.more;
        }

        public int[] next() {
            if (this.more) {
                this.more = false;
                return this.singleton;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$apache$commons$math4$util$Combinations$IterationOrder() {
        int[] iArr = $SWITCH_TABLE$org$apache$commons$math4$util$Combinations$IterationOrder;
        if (iArr == null) {
            iArr = new int[IterationOrder.values().length];
            try {
                iArr[IterationOrder.LEXICOGRAPHIC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            $SWITCH_TABLE$org$apache$commons$math4$util$Combinations$IterationOrder = iArr;
        }
        return iArr;
    }

    public Combinations(int n, int k) {
        this(n, k, IterationOrder.LEXICOGRAPHIC);
    }

    private Combinations(int n, int k, IterationOrder iterationOrder) {
        CombinatoricsUtils.checkBinomial(n, k);
        this.n = n;
        this.k = k;
        this.iterationOrder = iterationOrder;
    }

    public int getN() {
        return this.n;
    }

    public int getK() {
        return this.k;
    }

    public Iterator<int[]> iterator() {
        if (this.k == 0 || this.k == this.n) {
            return new SingletonIterator(MathArrays.natural(this.k));
        }
        switch ($SWITCH_TABLE$org$apache$commons$math4$util$Combinations$IterationOrder()[this.iterationOrder.ordinal()]) {
            case ValueServer.REPLAY_MODE /*1*/:
                return new LexicographicIterator(this.n, this.k);
            default:
                throw new MathInternalError();
        }
    }

    public Comparator<int[]> comparator() {
        return new LexicographicComparator(this.n, this.k);
    }
}
