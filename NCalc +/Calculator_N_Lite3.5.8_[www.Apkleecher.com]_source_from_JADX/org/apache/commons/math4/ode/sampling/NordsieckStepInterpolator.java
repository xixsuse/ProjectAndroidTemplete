package org.apache.commons.math4.ode.sampling;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import org.apache.commons.math4.exception.MaxCountExceededException;
import org.apache.commons.math4.linear.Array2DRowRealMatrix;
import org.apache.commons.math4.ode.EquationsMapper;
import org.apache.commons.math4.util.FastMath;

public class NordsieckStepInterpolator extends AbstractStepInterpolator {
    private static final long serialVersionUID = -7179861704951334960L;
    private Array2DRowRealMatrix nordsieck;
    private double referenceTime;
    private double[] scaled;
    private double scalingH;
    protected double[] stateVariation;

    public NordsieckStepInterpolator(NordsieckStepInterpolator interpolator) {
        super(interpolator);
        this.scalingH = interpolator.scalingH;
        this.referenceTime = interpolator.referenceTime;
        if (interpolator.scaled != null) {
            this.scaled = (double[]) interpolator.scaled.clone();
        }
        if (interpolator.nordsieck != null) {
            this.nordsieck = new Array2DRowRealMatrix(interpolator.nordsieck.getDataRef(), true);
        }
        if (interpolator.stateVariation != null) {
            this.stateVariation = (double[]) interpolator.stateVariation.clone();
        }
    }

    protected StepInterpolator doCopy() {
        return new NordsieckStepInterpolator(this);
    }

    public void reinitialize(double[] y, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {
        super.reinitialize(y, forward, primaryMapper, secondaryMappers);
        this.stateVariation = new double[y.length];
    }

    public void reinitialize(double time, double stepSize, double[] scaledDerivative, Array2DRowRealMatrix nordsieckVector) {
        this.referenceTime = time;
        this.scalingH = stepSize;
        this.scaled = scaledDerivative;
        this.nordsieck = nordsieckVector;
        setInterpolatedTime(getInterpolatedTime());
    }

    public void rescale(double stepSize) {
        double ratio = stepSize / this.scalingH;
        for (int i = 0; i < this.scaled.length; i++) {
            double[] dArr = this.scaled;
            dArr[i] = dArr[i] * ratio;
        }
        double[][] nData = this.nordsieck.getDataRef();
        double power = ratio;
        for (double[] nDataI : nData) {
            power *= ratio;
            for (int j = 0; j < nDataI.length; j++) {
                nDataI[j] = nDataI[j] * power;
            }
        }
        this.scalingH = stepSize;
    }

    public double[] getInterpolatedStateVariation() throws MaxCountExceededException {
        getInterpolatedState();
        return this.stateVariation;
    }

    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        int j;
        double x = this.interpolatedTime - this.referenceTime;
        double normalizedAbscissa = x / this.scalingH;
        Arrays.fill(this.stateVariation, 0.0d);
        Arrays.fill(this.interpolatedDerivatives, 0.0d);
        double[][] nData = this.nordsieck.getDataRef();
        for (int i = nData.length - 1; i >= 0; i--) {
            int order = i + 2;
            double[] nDataI = nData[i];
            double power = FastMath.pow(normalizedAbscissa, order);
            for (j = 0; j < nDataI.length; j++) {
                double d = nDataI[j] * power;
                double[] dArr = this.stateVariation;
                dArr[j] = dArr[j] + d;
                dArr = this.interpolatedDerivatives;
                dArr[j] = dArr[j] + (((double) order) * d);
            }
        }
        for (j = 0; j < this.currentState.length; j++) {
            dArr = this.stateVariation;
            dArr[j] = dArr[j] + (this.scaled[j] * normalizedAbscissa);
            this.interpolatedState[j] = this.currentState[j] + this.stateVariation[j];
            this.interpolatedDerivatives[j] = (this.interpolatedDerivatives[j] + (this.scaled[j] * normalizedAbscissa)) / x;
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        writeBaseExternal(out);
        out.writeDouble(this.scalingH);
        out.writeDouble(this.referenceTime);
        int n = this.currentState == null ? -1 : this.currentState.length;
        if (this.scaled == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            for (int j = 0; j < n; j++) {
                out.writeDouble(this.scaled[j]);
            }
        }
        if (this.nordsieck == null) {
            out.writeBoolean(false);
            return;
        }
        out.writeBoolean(true);
        out.writeObject(this.nordsieck);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        double t = readBaseExternal(in);
        this.scalingH = in.readDouble();
        this.referenceTime = in.readDouble();
        int n = this.currentState == null ? -1 : this.currentState.length;
        boolean hasScaled = in.readBoolean();
        if (hasScaled) {
            this.scaled = new double[n];
            for (int j = 0; j < n; j++) {
                this.scaled[j] = in.readDouble();
            }
        } else {
            this.scaled = null;
        }
        boolean hasNordsieck = in.readBoolean();
        if (hasNordsieck) {
            this.nordsieck = (Array2DRowRealMatrix) in.readObject();
        } else {
            this.nordsieck = null;
        }
        if (hasScaled && hasNordsieck) {
            this.stateVariation = new double[n];
            setInterpolatedTime(t);
            return;
        }
        this.stateVariation = null;
    }
}
