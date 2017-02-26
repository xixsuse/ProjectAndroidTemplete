package org.apache.commons.math4.analysis.polynomials;

import com.example.duy.calculator.math_eval.Constants;
import edu.jas.ps.UnivPowerSeriesRing;
import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math4.analysis.ParametricUnivariateFunction;
import org.apache.commons.math4.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math4.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math4.exception.NoDataException;
import org.apache.commons.math4.exception.NullArgumentException;
import org.apache.commons.math4.exception.util.LocalizedFormats;
import org.apache.commons.math4.util.FastMath;
import org.apache.commons.math4.util.MathUtils;

public class PolynomialFunction implements UnivariateDifferentiableFunction, Serializable {
    private static final long serialVersionUID = -7726511984200295583L;
    private final double[] coefficients;

    public static class Parametric implements ParametricUnivariateFunction {
        public double[] gradient(double x, double... parameters) {
            double[] gradient = new double[parameters.length];
            double xn = 1.0d;
            for (int i = 0; i < parameters.length; i++) {
                gradient[i] = xn;
                xn *= x;
            }
            return gradient;
        }

        public double value(double x, double... parameters) throws NoDataException {
            return PolynomialFunction.evaluate(parameters, x);
        }
    }

    public PolynomialFunction(double[] c) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(c);
        int n = c.length;
        if (n == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        while (n > 1 && c[n - 1] == 0.0d) {
            n--;
        }
        this.coefficients = new double[n];
        System.arraycopy(c, 0, this.coefficients, 0, n);
    }

    public double value(double x) {
        return evaluate(this.coefficients, x);
    }

    public int degree() {
        return this.coefficients.length - 1;
    }

    public double[] getCoefficients() {
        return (double[]) this.coefficients.clone();
    }

    protected static double evaluate(double[] coefficients, double argument) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(coefficients);
        int n = coefficients.length;
        if (n == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        double result = coefficients[n - 1];
        for (int j = n - 2; j >= 0; j--) {
            result = (argument * result) + coefficients[j];
        }
        return result;
    }

    public DerivativeStructure value(DerivativeStructure t) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(this.coefficients);
        int n = this.coefficients.length;
        if (n == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        }
        DerivativeStructure result = new DerivativeStructure(t.getFreeParameters(), t.getOrder(), this.coefficients[n - 1]);
        for (int j = n - 2; j >= 0; j--) {
            result = result.multiply(t).add(this.coefficients[j]);
        }
        return result;
    }

    public PolynomialFunction add(PolynomialFunction p) {
        int lowLength = FastMath.min(this.coefficients.length, p.coefficients.length);
        int highLength = FastMath.max(this.coefficients.length, p.coefficients.length);
        double[] newCoefficients = new double[highLength];
        for (int i = 0; i < lowLength; i++) {
            newCoefficients[i] = this.coefficients[i] + p.coefficients[i];
        }
        System.arraycopy(this.coefficients.length < p.coefficients.length ? p.coefficients : this.coefficients, lowLength, newCoefficients, lowLength, highLength - lowLength);
        return new PolynomialFunction(newCoefficients);
    }

    public PolynomialFunction subtract(PolynomialFunction p) {
        int i;
        int lowLength = FastMath.min(this.coefficients.length, p.coefficients.length);
        int highLength = FastMath.max(this.coefficients.length, p.coefficients.length);
        double[] newCoefficients = new double[highLength];
        for (i = 0; i < lowLength; i++) {
            newCoefficients[i] = this.coefficients[i] - p.coefficients[i];
        }
        if (this.coefficients.length < p.coefficients.length) {
            for (i = lowLength; i < highLength; i++) {
                newCoefficients[i] = -p.coefficients[i];
            }
        } else {
            System.arraycopy(this.coefficients, lowLength, newCoefficients, lowLength, highLength - lowLength);
        }
        return new PolynomialFunction(newCoefficients);
    }

    public PolynomialFunction negate() {
        double[] newCoefficients = new double[this.coefficients.length];
        for (int i = 0; i < this.coefficients.length; i++) {
            newCoefficients[i] = -this.coefficients[i];
        }
        return new PolynomialFunction(newCoefficients);
    }

    public PolynomialFunction multiply(PolynomialFunction p) {
        double[] newCoefficients = new double[((this.coefficients.length + p.coefficients.length) - 1)];
        for (int i = 0; i < newCoefficients.length; i++) {
            newCoefficients[i] = 0.0d;
            for (int j = FastMath.max(0, (i + 1) - p.coefficients.length); j < FastMath.min(this.coefficients.length, i + 1); j++) {
                newCoefficients[i] = newCoefficients[i] + (this.coefficients[j] * p.coefficients[i - j]);
            }
        }
        return new PolynomialFunction(newCoefficients);
    }

    protected static double[] differentiate(double[] coefficients) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(coefficients);
        int n = coefficients.length;
        if (n == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_POLYNOMIALS_COEFFICIENTS_ARRAY);
        } else if (n == 1) {
            return new double[]{0.0d};
        } else {
            double[] result = new double[(n - 1)];
            for (int i = n - 1; i > 0; i--) {
                result[i - 1] = ((double) i) * coefficients[i];
            }
            return result;
        }
    }

    public PolynomialFunction polynomialDerivative() {
        return new PolynomialFunction(differentiate(this.coefficients));
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        if (this.coefficients[0] != 0.0d) {
            s.append(toString(this.coefficients[0]));
        } else if (this.coefficients.length == 1) {
            return Constants.ZERO;
        }
        for (int i = 1; i < this.coefficients.length; i++) {
            if (this.coefficients[i] != 0.0d) {
                if (s.length() > 0) {
                    if (this.coefficients[i] < 0.0d) {
                        s.append(" - ");
                    } else {
                        s.append(" + ");
                    }
                } else if (this.coefficients[i] < 0.0d) {
                    s.append("-");
                }
                double absAi = FastMath.abs(this.coefficients[i]);
                if (absAi - 1.0d != 0.0d) {
                    s.append(toString(absAi));
                    s.append(Letters.SPACE);
                }
                s.append(UnivPowerSeriesRing.DEFAULT_NAME);
                if (i > 1) {
                    s.append(Constants.POWER_UNICODE);
                    s.append(Integer.toString(i));
                }
            }
        }
        return s.toString();
    }

    private static String toString(double coeff) {
        String c = Double.toString(coeff);
        if (c.endsWith(".0")) {
            return c.substring(0, c.length() - 2);
        }
        return c;
    }

    public int hashCode() {
        return Arrays.hashCode(this.coefficients) + 31;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PolynomialFunction)) {
            return false;
        }
        if (Arrays.equals(this.coefficients, ((PolynomialFunction) obj).coefficients)) {
            return true;
        }
        return false;
    }
}
