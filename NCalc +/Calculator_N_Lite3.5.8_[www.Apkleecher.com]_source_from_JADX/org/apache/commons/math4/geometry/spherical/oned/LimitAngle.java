package org.apache.commons.math4.geometry.spherical.oned;

import org.apache.commons.math4.geometry.Point;
import org.apache.commons.math4.geometry.partitioning.Hyperplane;

public class LimitAngle implements Hyperplane<Sphere1D> {
    private final boolean direct;
    private final S1Point location;
    private final double tolerance;

    public LimitAngle(S1Point location, boolean direct, double tolerance) {
        this.location = location;
        this.direct = direct;
        this.tolerance = tolerance;
    }

    public LimitAngle copySelf() {
        return this;
    }

    public double getOffset(Point<Sphere1D> point) {
        double delta = ((S1Point) point).getAlpha() - this.location.getAlpha();
        return this.direct ? delta : -delta;
    }

    public boolean isDirect() {
        return this.direct;
    }

    public LimitAngle getReverse() {
        return new LimitAngle(this.location, !this.direct, this.tolerance);
    }

    public SubLimitAngle wholeHyperplane() {
        return new SubLimitAngle(this, null);
    }

    public ArcsSet wholeSpace() {
        return new ArcsSet(this.tolerance);
    }

    public boolean sameOrientationAs(Hyperplane<Sphere1D> other) {
        return (this.direct ^ ((LimitAngle) other).direct) == 0;
    }

    public S1Point getLocation() {
        return this.location;
    }

    public Point<Sphere1D> project(Point<Sphere1D> point) {
        return this.location;
    }

    public double getTolerance() {
        return this.tolerance;
    }
}
