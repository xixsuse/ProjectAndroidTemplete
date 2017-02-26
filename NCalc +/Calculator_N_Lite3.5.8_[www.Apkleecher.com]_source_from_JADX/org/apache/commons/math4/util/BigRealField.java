package org.apache.commons.math4.util;

import java.io.Serializable;
import org.apache.commons.math4.Field;
import org.apache.commons.math4.FieldElement;

public class BigRealField implements Field<BigReal>, Serializable {
    private static final long serialVersionUID = 4756431066541037559L;

    private static class LazyHolder {
        private static final BigRealField INSTANCE;

        private LazyHolder() {
        }

        static {
            INSTANCE = new BigRealField();
        }
    }

    private BigRealField() {
    }

    public static BigRealField getInstance() {
        return LazyHolder.INSTANCE;
    }

    public BigReal getOne() {
        return BigReal.ONE;
    }

    public BigReal getZero() {
        return BigReal.ZERO;
    }

    public Class<? extends FieldElement<BigReal>> getRuntimeClass() {
        return BigReal.class;
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
