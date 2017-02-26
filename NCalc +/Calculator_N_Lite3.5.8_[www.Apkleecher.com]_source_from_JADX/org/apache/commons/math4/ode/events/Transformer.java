package org.apache.commons.math4.ode.events;

import org.apache.commons.math4.util.FastMath;
import org.apache.commons.math4.util.Precision;

enum Transformer {
    UNINITIALIZED {
        protected double transformed(double g) {
            return 0.0d;
        }
    },
    PLUS {
        protected double transformed(double g) {
            return g;
        }
    },
    MINUS {
        protected double transformed(double g) {
            return -g;
        }
    },
    MIN {
        protected double transformed(double g) {
            return FastMath.min(-Precision.SAFE_MIN, FastMath.min(-g, g));
        }
    },
    MAX {
        protected double transformed(double g) {
            return FastMath.max(Precision.SAFE_MIN, FastMath.max(-g, g));
        }
    };

    protected abstract double transformed(double d);
}
