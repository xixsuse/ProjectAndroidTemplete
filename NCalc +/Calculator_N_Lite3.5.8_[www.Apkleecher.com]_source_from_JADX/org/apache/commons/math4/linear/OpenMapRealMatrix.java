package org.apache.commons.math4.linear;

import java.io.Serializable;
import org.apache.commons.math4.analysis.integration.BaseAbstractUnivariateIntegrator;
import org.apache.commons.math4.exception.DimensionMismatchException;
import org.apache.commons.math4.exception.NotStrictlyPositiveException;
import org.apache.commons.math4.exception.NumberIsTooLargeException;
import org.apache.commons.math4.exception.OutOfRangeException;
import org.apache.commons.math4.util.OpenIntToDoubleHashMap;
import org.apache.commons.math4.util.OpenIntToDoubleHashMap.Iterator;

public class OpenMapRealMatrix extends AbstractRealMatrix implements SparseRealMatrix, Serializable {
    private static final long serialVersionUID = -5962461716457143437L;
    private final int columns;
    private final OpenIntToDoubleHashMap entries;
    private final int rows;

    public OpenMapRealMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException, NumberIsTooLargeException {
        super(rowDimension, columnDimension);
        long lRow = (long) rowDimension;
        long lCol = (long) columnDimension;
        if (lRow * lCol >= 2147483647L) {
            throw new NumberIsTooLargeException(Long.valueOf(lRow * lCol), Integer.valueOf(BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT), false);
        }
        this.rows = rowDimension;
        this.columns = columnDimension;
        this.entries = new OpenIntToDoubleHashMap(0.0d);
    }

    public OpenMapRealMatrix(OpenMapRealMatrix matrix) {
        this.rows = matrix.rows;
        this.columns = matrix.columns;
        this.entries = new OpenIntToDoubleHashMap(matrix.entries);
    }

    public OpenMapRealMatrix copy() {
        return new OpenMapRealMatrix(this);
    }

    public OpenMapRealMatrix createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException, NumberIsTooLargeException {
        return new OpenMapRealMatrix(rowDimension, columnDimension);
    }

    public int getColumnDimension() {
        return this.columns;
    }

    public OpenMapRealMatrix add(OpenMapRealMatrix m) throws MatrixDimensionMismatchException {
        MatrixUtils.checkAdditionCompatible(this, m);
        OpenMapRealMatrix out = new OpenMapRealMatrix(this);
        Iterator iterator = m.entries.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            int row = iterator.key() / this.columns;
            int col = iterator.key() - (this.columns * row);
            out.setEntry(row, col, getEntry(row, col) + iterator.value());
        }
        return out;
    }

    public OpenMapRealMatrix subtract(RealMatrix m) throws MatrixDimensionMismatchException {
        try {
            return subtract((OpenMapRealMatrix) m);
        } catch (ClassCastException e) {
            return (OpenMapRealMatrix) super.subtract(m);
        }
    }

    public OpenMapRealMatrix subtract(OpenMapRealMatrix m) throws MatrixDimensionMismatchException {
        MatrixUtils.checkAdditionCompatible(this, m);
        OpenMapRealMatrix out = new OpenMapRealMatrix(this);
        Iterator iterator = m.entries.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            int row = iterator.key() / this.columns;
            int col = iterator.key() - (this.columns * row);
            out.setEntry(row, col, getEntry(row, col) - iterator.value());
        }
        return out;
    }

    public RealMatrix multiply(RealMatrix m) throws DimensionMismatchException, NumberIsTooLargeException {
        RealMatrix multiply;
        try {
            multiply = multiply((OpenMapRealMatrix) m);
        } catch (ClassCastException e) {
            MatrixUtils.checkMultiplicationCompatible(this, m);
            int outCols = m.getColumnDimension();
            multiply = new BlockRealMatrix(this.rows, outCols);
            Iterator iterator = this.entries.iterator();
            while (iterator.hasNext()) {
                iterator.advance();
                double value = iterator.value();
                int key = iterator.key();
                int i = key / this.columns;
                int k = key % this.columns;
                for (int j = 0; j < outCols; j++) {
                    multiply.addToEntry(i, j, m.getEntry(k, j) * value);
                }
            }
        }
        return multiply;
    }

    public OpenMapRealMatrix multiply(OpenMapRealMatrix m) throws DimensionMismatchException, NumberIsTooLargeException {
        MatrixUtils.checkMultiplicationCompatible(this, m);
        int outCols = m.getColumnDimension();
        OpenMapRealMatrix out = new OpenMapRealMatrix(this.rows, outCols);
        Iterator iterator = this.entries.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            double value = iterator.value();
            int key = iterator.key();
            int i = key / this.columns;
            int k = key % this.columns;
            for (int j = 0; j < outCols; j++) {
                int rightKey = m.computeKey(k, j);
                if (m.entries.containsKey(rightKey)) {
                    int outKey = out.computeKey(i, j);
                    double outValue = out.entries.get(outKey) + (m.entries.get(rightKey) * value);
                    if (outValue == 0.0d) {
                        out.entries.remove(outKey);
                    } else {
                        out.entries.put(outKey, outValue);
                    }
                }
            }
        }
        return out;
    }

    public double getEntry(int row, int column) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        MatrixUtils.checkColumnIndex(this, column);
        return this.entries.get(computeKey(row, column));
    }

    public int getRowDimension() {
        return this.rows;
    }

    public void setEntry(int row, int column, double value) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        MatrixUtils.checkColumnIndex(this, column);
        if (value == 0.0d) {
            this.entries.remove(computeKey(row, column));
        } else {
            this.entries.put(computeKey(row, column), value);
        }
    }

    public void addToEntry(int row, int column, double increment) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        MatrixUtils.checkColumnIndex(this, column);
        int key = computeKey(row, column);
        double value = this.entries.get(key) + increment;
        if (value == 0.0d) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, value);
        }
    }

    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        MatrixUtils.checkColumnIndex(this, column);
        int key = computeKey(row, column);
        double value = this.entries.get(key) * factor;
        if (value == 0.0d) {
            this.entries.remove(key);
        } else {
            this.entries.put(key, value);
        }
    }

    private int computeKey(int row, int column) {
        return (this.columns * row) + column;
    }
}
