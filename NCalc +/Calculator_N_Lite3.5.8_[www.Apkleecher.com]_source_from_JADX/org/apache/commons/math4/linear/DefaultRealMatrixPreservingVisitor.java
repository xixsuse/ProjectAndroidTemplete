package org.apache.commons.math4.linear;

public class DefaultRealMatrixPreservingVisitor implements RealMatrixPreservingVisitor {
    public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
    }

    public void visit(int row, int column, double value) {
    }

    public double end() {
        return 0.0d;
    }
}
