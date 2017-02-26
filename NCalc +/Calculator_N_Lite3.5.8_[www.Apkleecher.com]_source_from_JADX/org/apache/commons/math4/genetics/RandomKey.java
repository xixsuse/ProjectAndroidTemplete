package org.apache.commons.math4.genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.math4.exception.DimensionMismatchException;
import org.apache.commons.math4.exception.MathIllegalArgumentException;
import org.apache.commons.math4.exception.util.LocalizedFormats;

public abstract class RandomKey<T> extends AbstractListChromosome<Double> implements PermutationChromosome<T> {
    private final List<Integer> baseSeqPermutation;
    private final List<Double> sortedRepresentation;

    public RandomKey(List<Double> representation) throws InvalidRepresentationException {
        super((List) representation);
        List<Double> sortedRepr = new ArrayList(getRepresentation());
        Collections.sort(sortedRepr);
        this.sortedRepresentation = Collections.unmodifiableList(sortedRepr);
        this.baseSeqPermutation = Collections.unmodifiableList(decodeGeneric(baseSequence(getLength()), getRepresentation(), this.sortedRepresentation));
    }

    public RandomKey(Double[] representation) throws InvalidRepresentationException {
        this(Arrays.asList(representation));
    }

    public List<T> decode(List<T> sequence) {
        return decodeGeneric(sequence, getRepresentation(), this.sortedRepresentation);
    }

    private static <S> List<S> decodeGeneric(List<S> sequence, List<Double> representation, List<Double> sortedRepr) throws DimensionMismatchException {
        int l = sequence.size();
        if (representation.size() != l) {
            throw new DimensionMismatchException(representation.size(), l);
        } else if (sortedRepr.size() != l) {
            throw new DimensionMismatchException(sortedRepr.size(), l);
        } else {
            List<Double> reprCopy = new ArrayList(representation);
            List<S> res = new ArrayList(l);
            for (int i = 0; i < l; i++) {
                int index = reprCopy.indexOf(sortedRepr.get(i));
                res.add(sequence.get(index));
                reprCopy.set(index, null);
            }
            return res;
        }
    }

    protected boolean isSame(Chromosome another) {
        if (!(another instanceof RandomKey)) {
            return false;
        }
        RandomKey<?> anotherRk = (RandomKey) another;
        if (getLength() != anotherRk.getLength()) {
            return false;
        }
        List<Integer> thisPerm = this.baseSeqPermutation;
        List<Integer> anotherPerm = anotherRk.baseSeqPermutation;
        for (int i = 0; i < getLength(); i++) {
            if (thisPerm.get(i) != anotherPerm.get(i)) {
                return false;
            }
        }
        return true;
    }

    protected void checkValidity(List<Double> chromosomeRepresentation) throws InvalidRepresentationException {
        for (Double doubleValue : chromosomeRepresentation) {
            double val = doubleValue.doubleValue();
            if (val >= 0.0d) {
                if (val > 1.0d) {
                }
            }
            throw new InvalidRepresentationException(LocalizedFormats.OUT_OF_RANGE_SIMPLE, Double.valueOf(val), Integer.valueOf(0), Integer.valueOf(1));
        }
    }

    public static final List<Double> randomPermutation(int l) {
        List<Double> repr = new ArrayList(l);
        for (int i = 0; i < l; i++) {
            repr.add(Double.valueOf(GeneticAlgorithm.getRandomGenerator().nextDouble()));
        }
        return repr;
    }

    public static final List<Double> identityPermutation(int l) {
        List<Double> repr = new ArrayList(l);
        for (int i = 0; i < l; i++) {
            repr.add(Double.valueOf(((double) i) / ((double) l)));
        }
        return repr;
    }

    public static <S> List<Double> comparatorPermutation(List<S> data, Comparator<S> comparator) {
        List<S> sortedData = new ArrayList(data);
        Collections.sort(sortedData, comparator);
        return inducedPermutation(data, sortedData);
    }

    public static <S> List<Double> inducedPermutation(List<S> originalData, List<S> permutedData) throws DimensionMismatchException, MathIllegalArgumentException {
        if (originalData.size() != permutedData.size()) {
            throw new DimensionMismatchException(permutedData.size(), originalData.size());
        }
        int l = originalData.size();
        List<S> origDataCopy = new ArrayList(originalData);
        Double[] res = new Double[l];
        for (int i = 0; i < l; i++) {
            int index = origDataCopy.indexOf(permutedData.get(i));
            if (index == -1) {
                throw new MathIllegalArgumentException(LocalizedFormats.DIFFERENT_ORIG_AND_PERMUTED_DATA, new Object[0]);
            }
            res[index] = Double.valueOf(((double) i) / ((double) l));
            origDataCopy.set(index, null);
        }
        return Arrays.asList(res);
    }

    public String toString() {
        return String.format("(f=%s pi=(%s))", new Object[]{Double.valueOf(getFitness()), this.baseSeqPermutation});
    }

    private static List<Integer> baseSequence(int l) {
        List<Integer> baseSequence = new ArrayList(l);
        for (int i = 0; i < l; i++) {
            baseSequence.add(Integer.valueOf(i));
        }
        return baseSequence;
    }
}
