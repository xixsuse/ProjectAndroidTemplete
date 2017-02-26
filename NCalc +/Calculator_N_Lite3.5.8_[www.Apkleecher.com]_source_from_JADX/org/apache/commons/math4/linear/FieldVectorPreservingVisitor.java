package org.apache.commons.math4.linear;

import org.apache.commons.math4.FieldElement;

public interface FieldVectorPreservingVisitor<T extends FieldElement<?>> {
    T end();

    void start(int i, int i2, int i3);

    void visit(int i, T t);
}
