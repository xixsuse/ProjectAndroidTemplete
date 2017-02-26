package org.apache.commons.math4.linear;

import org.apache.commons.math4.exception.MathIllegalArgumentException;
import org.apache.commons.math4.exception.util.LocalizedFormats;

public class NonSymmetricMatrixException extends MathIllegalArgumentException {
    private static final long serialVersionUID = -7518495577824189882L;
    private final int column;
    private final int row;
    private final double threshold;

    public NonSymmetricMatrixException(int row, int column, double threshold) {
        super(LocalizedFormats.NON_SYMMETRIC_MATRIX, Integer.valueOf(row), Integer.valueOf(column), Double.valueOf(threshold));
        this.row = row;
        this.column = column;
        this.threshold = threshold;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public double getThreshold() {
        return this.threshold;
    }
}
