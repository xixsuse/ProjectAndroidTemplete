package org.apache.commons.math4.geometry.euclidean.twod;

import org.apache.commons.math4.util.FastMath;

public class Segment {
    private final Vector2D end;
    private final Line line;
    private final Vector2D start;

    public Segment(Vector2D start, Vector2D end, Line line) {
        this.start = start;
        this.end = end;
        this.line = line;
    }

    public Vector2D getStart() {
        return this.start;
    }

    public Vector2D getEnd() {
        return this.end;
    }

    public Line getLine() {
        return this.line;
    }

    public double distance(Vector2D p) {
        double deltaX = this.end.getX() - this.start.getX();
        double deltaY = this.end.getY() - this.start.getY();
        double r = (((p.getX() - this.start.getX()) * deltaX) + ((p.getY() - this.start.getY()) * deltaY)) / ((deltaX * deltaX) + (deltaY * deltaY));
        if (r < 0.0d || r > 1.0d) {
            return FastMath.min(getStart().distance(p), getEnd().distance(p));
        }
        return new Vector2D(this.start.getX() + (r * deltaX), this.start.getY() + (r * deltaY)).distance(p);
    }
}
