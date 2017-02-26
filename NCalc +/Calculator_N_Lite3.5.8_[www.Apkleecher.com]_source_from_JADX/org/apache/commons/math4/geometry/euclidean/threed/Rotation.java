package org.apache.commons.math4.geometry.euclidean.threed;

import java.io.Serializable;
import java.lang.reflect.Array;
import org.apache.commons.math4.exception.MathArithmeticException;
import org.apache.commons.math4.exception.MathIllegalArgumentException;
import org.apache.commons.math4.exception.util.LocalizedFormats;
import org.apache.commons.math4.util.FastMath;
import org.apache.commons.math4.util.MathArrays;

public class Rotation implements Serializable {
    public static final Rotation IDENTITY;
    private static final long serialVersionUID = -2153622329907944313L;
    private final double q0;
    private final double q1;
    private final double q2;
    private final double q3;

    static {
        IDENTITY = new Rotation(1.0d, 0.0d, 0.0d, 0.0d, false);
    }

    public Rotation(double q0, double q1, double q2, double q3, boolean needsNormalization) {
        if (needsNormalization) {
            double inv = 1.0d / FastMath.sqrt((((q0 * q0) + (q1 * q1)) + (q2 * q2)) + (q3 * q3));
            q0 *= inv;
            q1 *= inv;
            q2 *= inv;
            q3 *= inv;
        }
        this.q0 = q0;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }

    public Rotation(Vector3D axis, double angle) throws MathIllegalArgumentException {
        double norm = axis.getNorm();
        if (norm == 0.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS, new Object[0]);
        }
        double halfAngle = -0.5d * angle;
        double coeff = FastMath.sin(halfAngle) / norm;
        this.q0 = FastMath.cos(halfAngle);
        this.q1 = axis.getX() * coeff;
        this.q2 = axis.getY() * coeff;
        this.q3 = axis.getZ() * coeff;
    }

    public Rotation(double[][] m, double threshold) throws NotARotationMatrixException {
        if (m.length == 3 && m[0].length == 3 && m[1].length == 3 && m[2].length == 3) {
            double[][] ort = orthogonalizeMatrix(m, threshold);
            if (((ort[0][0] * ((ort[1][1] * ort[2][2]) - (ort[2][1] * ort[1][2]))) - (ort[1][0] * ((ort[0][1] * ort[2][2]) - (ort[2][1] * ort[0][2])))) + (ort[2][0] * ((ort[0][1] * ort[1][2]) - (ort[1][1] * ort[0][2]))) < 0.0d) {
                throw new NotARotationMatrixException(LocalizedFormats.CLOSEST_ORTHOGONAL_MATRIX_HAS_NEGATIVE_DETERMINANT, Double.valueOf(det));
            }
            double[] quat = mat2quat(ort);
            this.q0 = quat[0];
            this.q1 = quat[1];
            this.q2 = quat[2];
            this.q3 = quat[3];
            return;
        }
        throw new NotARotationMatrixException(LocalizedFormats.ROTATION_MATRIX_DIMENSIONS, Integer.valueOf(m.length), Integer.valueOf(m[0].length));
    }

    public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) throws MathArithmeticException {
        u2 = u1.crossProduct(u2).normalize().crossProduct(u1).normalize();
        u1 = u1.normalize();
        v2 = v1.crossProduct(v2).normalize().crossProduct(v1).normalize();
        v1 = v1.normalize();
        m = new double[3][];
        m[0] = new double[]{MathArrays.linearCombination(u1.getX(), v1.getX(), u2.getX(), v2.getX(), u3.getX(), v3.getX()), MathArrays.linearCombination(u1.getY(), v1.getX(), u2.getY(), v2.getX(), u3.getY(), v3.getX()), MathArrays.linearCombination(u1.getZ(), v1.getX(), u2.getZ(), v2.getX(), u3.getZ(), v3.getX())};
        m[1] = new double[]{MathArrays.linearCombination(u1.getX(), v1.getY(), u2.getX(), v2.getY(), u3.getX(), v3.getY()), MathArrays.linearCombination(u1.getY(), v1.getY(), u2.getY(), v2.getY(), u3.getY(), v3.getY()), MathArrays.linearCombination(u1.getZ(), v1.getY(), u2.getZ(), v2.getY(), u3.getZ(), v3.getY())};
        m[2] = new double[]{MathArrays.linearCombination(u1.getX(), v1.getZ(), u2.getX(), v2.getZ(), u3.getX(), v3.getZ()), MathArrays.linearCombination(u1.getY(), v1.getZ(), u2.getY(), v2.getZ(), u3.getY(), v3.getZ()), MathArrays.linearCombination(u1.getZ(), v1.getZ(), u2.getZ(), v2.getZ(), u3.getZ(), v3.getZ())};
        double[] quat = mat2quat(m);
        this.q0 = quat[0];
        this.q1 = quat[1];
        this.q2 = quat[2];
        this.q3 = quat[3];
    }

    public Rotation(Vector3D u, Vector3D v) throws MathArithmeticException {
        double normProduct = u.getNorm() * v.getNorm();
        if (normProduct == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR, new Object[0]);
        }
        double dot = u.dotProduct(v);
        if (dot < -0.999999999999998d * normProduct) {
            Vector3D w = u.orthogonal();
            this.q0 = 0.0d;
            this.q1 = -w.getX();
            this.q2 = -w.getY();
            this.q3 = -w.getZ();
            return;
        }
        this.q0 = FastMath.sqrt(0.5d * (1.0d + (dot / normProduct)));
        double coeff = 1.0d / ((2.0d * this.q0) * normProduct);
        Vector3D q = v.crossProduct(u);
        this.q1 = q.getX() * coeff;
        this.q2 = q.getY() * coeff;
        this.q3 = q.getZ() * coeff;
    }

    public Rotation(RotationOrder order, double alpha1, double alpha2, double alpha3) {
        Rotation composed = new Rotation(order.getA1(), alpha1).applyTo(new Rotation(order.getA2(), alpha2).applyTo(new Rotation(order.getA3(), alpha3)));
        this.q0 = composed.q0;
        this.q1 = composed.q1;
        this.q2 = composed.q2;
        this.q3 = composed.q3;
    }

    private static double[] mat2quat(double[][] ort) {
        double[] quat = new double[4];
        double s = (ort[0][0] + ort[1][1]) + ort[2][2];
        double inv;
        if (s > -0.19d) {
            quat[0] = 0.5d * FastMath.sqrt(1.0d + s);
            inv = 0.25d / quat[0];
            quat[1] = (ort[1][2] - ort[2][1]) * inv;
            quat[2] = (ort[2][0] - ort[0][2]) * inv;
            quat[3] = (ort[0][1] - ort[1][0]) * inv;
        } else {
            s = (ort[0][0] - ort[1][1]) - ort[2][2];
            if (s > -0.19d) {
                quat[1] = 0.5d * FastMath.sqrt(1.0d + s);
                inv = 0.25d / quat[1];
                quat[0] = (ort[1][2] - ort[2][1]) * inv;
                quat[2] = (ort[0][1] + ort[1][0]) * inv;
                quat[3] = (ort[0][2] + ort[2][0]) * inv;
            } else {
                s = (ort[1][1] - ort[0][0]) - ort[2][2];
                if (s > -0.19d) {
                    quat[2] = 0.5d * FastMath.sqrt(1.0d + s);
                    inv = 0.25d / quat[2];
                    quat[0] = (ort[2][0] - ort[0][2]) * inv;
                    quat[1] = (ort[0][1] + ort[1][0]) * inv;
                    quat[3] = (ort[2][1] + ort[1][2]) * inv;
                } else {
                    quat[3] = 0.5d * FastMath.sqrt(1.0d + ((ort[2][2] - ort[0][0]) - ort[1][1]));
                    inv = 0.25d / quat[3];
                    quat[0] = (ort[0][1] - ort[1][0]) * inv;
                    quat[1] = (ort[0][2] + ort[2][0]) * inv;
                    quat[2] = (ort[2][1] + ort[1][2]) * inv;
                }
            }
        }
        return quat;
    }

    public Rotation revert() {
        return new Rotation(-this.q0, this.q1, this.q2, this.q3, false);
    }

    public double getQ0() {
        return this.q0;
    }

    public double getQ1() {
        return this.q1;
    }

    public double getQ2() {
        return this.q2;
    }

    public double getQ3() {
        return this.q3;
    }

    public Vector3D getAxis() {
        double squaredSine = ((this.q1 * this.q1) + (this.q2 * this.q2)) + (this.q3 * this.q3);
        if (squaredSine == 0.0d) {
            return new Vector3D(1.0d, 0.0d, 0.0d);
        }
        if (this.q0 < 0.0d) {
            double inverse = 1.0d / FastMath.sqrt(squaredSine);
            return new Vector3D(this.q1 * inverse, this.q2 * inverse, this.q3 * inverse);
        }
        inverse = -1.0d / FastMath.sqrt(squaredSine);
        return new Vector3D(this.q1 * inverse, this.q2 * inverse, this.q3 * inverse);
    }

    public double getAngle() {
        if (this.q0 < -0.1d || this.q0 > 0.1d) {
            return FastMath.asin(FastMath.sqrt(((this.q1 * this.q1) + (this.q2 * this.q2)) + (this.q3 * this.q3))) * 2.0d;
        }
        if (this.q0 < 0.0d) {
            return FastMath.acos(-this.q0) * 2.0d;
        }
        return FastMath.acos(this.q0) * 2.0d;
    }

    public double[] getAngles(RotationOrder order) throws CardanEulerSingularityException {
        Vector3D v1;
        Vector3D v2;
        if (order == RotationOrder.XYZ) {
            v1 = applyTo(Vector3D.PLUS_K);
            v2 = applyInverseTo(Vector3D.PLUS_I);
            if (v2.getZ() < -0.9999999999d || v2.getZ() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(-v1.getY(), v1.getZ()), FastMath.asin(v2.getZ()), FastMath.atan2(-v2.getY(), v2.getX())};
        } else if (order == RotationOrder.XZY) {
            v1 = applyTo(Vector3D.PLUS_J);
            v2 = applyInverseTo(Vector3D.PLUS_I);
            if (v2.getY() < -0.9999999999d || v2.getY() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(v1.getZ(), v1.getY()), -FastMath.asin(v2.getY()), FastMath.atan2(v2.getZ(), v2.getX())};
        } else if (order == RotationOrder.YXZ) {
            v1 = applyTo(Vector3D.PLUS_K);
            v2 = applyInverseTo(Vector3D.PLUS_J);
            if (v2.getZ() < -0.9999999999d || v2.getZ() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(v1.getX(), v1.getZ()), -FastMath.asin(v2.getZ()), FastMath.atan2(v2.getX(), v2.getY())};
        } else if (order == RotationOrder.YZX) {
            v1 = applyTo(Vector3D.PLUS_I);
            v2 = applyInverseTo(Vector3D.PLUS_J);
            if (v2.getX() < -0.9999999999d || v2.getX() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(-v1.getZ(), v1.getX()), FastMath.asin(v2.getX()), FastMath.atan2(-v2.getZ(), v2.getY())};
        } else if (order == RotationOrder.ZXY) {
            v1 = applyTo(Vector3D.PLUS_J);
            v2 = applyInverseTo(Vector3D.PLUS_K);
            if (v2.getY() < -0.9999999999d || v2.getY() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(-v1.getX(), v1.getY()), FastMath.asin(v2.getY()), FastMath.atan2(-v2.getX(), v2.getZ())};
        } else if (order == RotationOrder.ZYX) {
            v1 = applyTo(Vector3D.PLUS_I);
            v2 = applyInverseTo(Vector3D.PLUS_K);
            if (v2.getX() < -0.9999999999d || v2.getX() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(v1.getY(), v1.getX()), -FastMath.asin(v2.getX()), FastMath.atan2(v2.getY(), v2.getZ())};
        } else if (order == RotationOrder.XYX) {
            v1 = applyTo(Vector3D.PLUS_I);
            v2 = applyInverseTo(Vector3D.PLUS_I);
            if (v2.getX() < -0.9999999999d || v2.getX() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v1.getY(), -v1.getZ()), FastMath.acos(v2.getX()), FastMath.atan2(v2.getY(), v2.getZ())};
        } else if (order == RotationOrder.XZX) {
            v1 = applyTo(Vector3D.PLUS_I);
            v2 = applyInverseTo(Vector3D.PLUS_I);
            if (v2.getX() < -0.9999999999d || v2.getX() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v1.getZ(), v1.getY()), FastMath.acos(v2.getX()), FastMath.atan2(v2.getZ(), -v2.getY())};
        } else if (order == RotationOrder.YXY) {
            v1 = applyTo(Vector3D.PLUS_J);
            v2 = applyInverseTo(Vector3D.PLUS_J);
            if (v2.getY() < -0.9999999999d || v2.getY() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v1.getX(), v1.getZ()), FastMath.acos(v2.getY()), FastMath.atan2(v2.getX(), -v2.getZ())};
        } else if (order == RotationOrder.YZY) {
            v1 = applyTo(Vector3D.PLUS_J);
            v2 = applyInverseTo(Vector3D.PLUS_J);
            if (v2.getY() < -0.9999999999d || v2.getY() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v1.getZ(), -v1.getX()), FastMath.acos(v2.getY()), FastMath.atan2(v2.getZ(), v2.getX())};
        } else if (order == RotationOrder.ZXZ) {
            v1 = applyTo(Vector3D.PLUS_K);
            v2 = applyInverseTo(Vector3D.PLUS_K);
            if (v2.getZ() < -0.9999999999d || v2.getZ() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v1.getX(), -v1.getY()), FastMath.acos(v2.getZ()), FastMath.atan2(v2.getX(), v2.getY())};
        } else {
            v1 = applyTo(Vector3D.PLUS_K);
            v2 = applyInverseTo(Vector3D.PLUS_K);
            if (v2.getZ() < -0.9999999999d || v2.getZ() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v1.getY(), v1.getX()), FastMath.acos(v2.getZ()), FastMath.atan2(v2.getY(), -v2.getX())};
        }
    }

    public double[][] getMatrix() {
        double q0q0 = this.q0 * this.q0;
        double q0q1 = this.q0 * this.q1;
        double q0q2 = this.q0 * this.q2;
        double q0q3 = this.q0 * this.q3;
        double q1q1 = this.q1 * this.q1;
        double q1q2 = this.q1 * this.q2;
        double q1q3 = this.q1 * this.q3;
        double q2q2 = this.q2 * this.q2;
        double q2q3 = this.q2 * this.q3;
        double q3q3 = this.q3 * this.q3;
        double[][] m = new double[3][];
        r24 = new double[3];
        r24 = new double[3];
        r24 = new double[]{r0, (2.0d * (q0q0 + q1q1)) - 1.0d, 2.0d * (q1q2 - q0q3)};
        m[2][0] = 2.0d * (q1q3 + q0q2);
        m[0][1] = 2.0d * (q1q2 + q0q3);
        m[1][1] = (2.0d * (q0q0 + q2q2)) - 1.0d;
        m[2][1] = 2.0d * (q2q3 - q0q1);
        m[0][2] = 2.0d * (q1q3 - q0q2);
        m[1][2] = 2.0d * (q2q3 + q0q1);
        m[2][2] = (2.0d * (q0q0 + q3q3)) - 1.0d;
        return m;
    }

    public Vector3D applyTo(Vector3D u) {
        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();
        double s = ((this.q1 * x) + (this.q2 * y)) + (this.q3 * z);
        return new Vector3D((2.0d * ((this.q0 * ((this.q0 * x) - ((this.q2 * z) - (this.q3 * y)))) + (this.q1 * s))) - x, (2.0d * ((this.q0 * ((this.q0 * y) - ((this.q3 * x) - (this.q1 * z)))) + (this.q2 * s))) - y, (2.0d * ((this.q0 * ((this.q0 * z) - ((this.q1 * y) - (this.q2 * x)))) + (this.q3 * s))) - z);
    }

    public void applyTo(double[] in, double[] out) {
        double x = in[0];
        double y = in[1];
        double z = in[2];
        double s = ((this.q1 * x) + (this.q2 * y)) + (this.q3 * z);
        out[0] = (2.0d * ((this.q0 * ((this.q0 * x) - ((this.q2 * z) - (this.q3 * y)))) + (this.q1 * s))) - x;
        out[1] = (2.0d * ((this.q0 * ((this.q0 * y) - ((this.q3 * x) - (this.q1 * z)))) + (this.q2 * s))) - y;
        out[2] = (2.0d * ((this.q0 * ((this.q0 * z) - ((this.q1 * y) - (this.q2 * x)))) + (this.q3 * s))) - z;
    }

    public Vector3D applyInverseTo(Vector3D u) {
        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();
        double s = ((this.q1 * x) + (this.q2 * y)) + (this.q3 * z);
        double m0 = -this.q0;
        return new Vector3D((2.0d * ((((x * m0) - ((this.q2 * z) - (this.q3 * y))) * m0) + (this.q1 * s))) - x, (2.0d * ((((y * m0) - ((this.q3 * x) - (this.q1 * z))) * m0) + (this.q2 * s))) - y, (2.0d * ((((z * m0) - ((this.q1 * y) - (this.q2 * x))) * m0) + (this.q3 * s))) - z);
    }

    public void applyInverseTo(double[] in, double[] out) {
        double x = in[0];
        double y = in[1];
        double z = in[2];
        double s = ((this.q1 * x) + (this.q2 * y)) + (this.q3 * z);
        double m0 = -this.q0;
        out[0] = (2.0d * ((((x * m0) - ((this.q2 * z) - (this.q3 * y))) * m0) + (this.q1 * s))) - x;
        out[1] = (2.0d * ((((y * m0) - ((this.q3 * x) - (this.q1 * z))) * m0) + (this.q2 * s))) - y;
        out[2] = (2.0d * ((((z * m0) - ((this.q1 * y) - (this.q2 * x))) * m0) + (this.q3 * s))) - z;
    }

    public Rotation applyTo(Rotation r) {
        return new Rotation((r.q0 * this.q0) - (((r.q1 * this.q1) + (r.q2 * this.q2)) + (r.q3 * this.q3)), ((r.q1 * this.q0) + (r.q0 * this.q1)) + ((r.q2 * this.q3) - (r.q3 * this.q2)), ((r.q2 * this.q0) + (r.q0 * this.q2)) + ((r.q3 * this.q1) - (r.q1 * this.q3)), ((r.q3 * this.q0) + (r.q0 * this.q3)) + ((r.q1 * this.q2) - (r.q2 * this.q1)), false);
    }

    public Rotation applyInverseTo(Rotation r) {
        return new Rotation(((-r.q0) * this.q0) - (((r.q1 * this.q1) + (r.q2 * this.q2)) + (r.q3 * this.q3)), (((-r.q1) * this.q0) + (r.q0 * this.q1)) + ((r.q2 * this.q3) - (r.q3 * this.q2)), (((-r.q2) * this.q0) + (r.q0 * this.q2)) + ((r.q3 * this.q1) - (r.q1 * this.q3)), (((-r.q3) * this.q0) + (r.q0 * this.q3)) + ((r.q1 * this.q2) - (r.q2 * this.q1)), false);
    }

    private double[][] orthogonalizeMatrix(double[][] m, double threshold) throws NotARotationMatrixException {
        double[] m0 = m[0];
        double[] m1 = m[1];
        double[] m2 = m[2];
        double x00 = m0[0];
        double x01 = m0[1];
        double x02 = m0[2];
        double x10 = m1[0];
        double x11 = m1[1];
        double x12 = m1[2];
        double x20 = m2[0];
        double x21 = m2[1];
        double x22 = m2[2];
        double fn = 0.0d;
        int[] iArr = new int[]{3, 3};
        double[][] o = (double[][]) Array.newInstance(Double.TYPE, iArr);
        double[] o0 = o[0];
        double[] o1 = o[1];
        double[] o2 = o[2];
        int i = 0;
        while (true) {
            i++;
            if (i >= 11) {
                break;
            }
            double mx00 = ((m0[0] * x00) + (m1[0] * x10)) + (m2[0] * x20);
            double mx10 = ((m0[1] * x00) + (m1[1] * x10)) + (m2[1] * x20);
            double mx20 = ((m0[2] * x00) + (m1[2] * x10)) + (m2[2] * x20);
            double mx01 = ((m0[0] * x01) + (m1[0] * x11)) + (m2[0] * x21);
            double mx11 = ((m0[1] * x01) + (m1[1] * x11)) + (m2[1] * x21);
            double mx21 = ((m0[2] * x01) + (m1[2] * x11)) + (m2[2] * x21);
            double mx02 = ((m0[0] * x02) + (m1[0] * x12)) + (m2[0] * x22);
            double mx12 = ((m0[1] * x02) + (m1[1] * x12)) + (m2[1] * x22);
            double mx22 = ((m0[2] * x02) + (m1[2] * x12)) + (m2[2] * x22);
            o0[0] = x00 - (0.5d * ((((x00 * mx00) + (x01 * mx10)) + (x02 * mx20)) - m0[0]));
            o0[1] = x01 - (0.5d * ((((x00 * mx01) + (x01 * mx11)) + (x02 * mx21)) - m0[1]));
            o0[2] = x02 - (0.5d * ((((x00 * mx02) + (x01 * mx12)) + (x02 * mx22)) - m0[2]));
            o1[0] = x10 - (0.5d * ((((x10 * mx00) + (x11 * mx10)) + (x12 * mx20)) - m1[0]));
            o1[1] = x11 - (0.5d * ((((x10 * mx01) + (x11 * mx11)) + (x12 * mx21)) - m1[1]));
            o1[2] = x12 - (0.5d * ((((x10 * mx02) + (x11 * mx12)) + (x12 * mx22)) - m1[2]));
            o2[0] = x20 - (0.5d * ((((x20 * mx00) + (x21 * mx10)) + (x22 * mx20)) - m2[0]));
            o2[1] = x21 - (0.5d * ((((x20 * mx01) + (x21 * mx11)) + (x22 * mx21)) - m2[1]));
            o2[2] = x22 - (0.5d * ((((x20 * mx02) + (x21 * mx12)) + (x22 * mx22)) - m2[2]));
            double corr00 = o0[0] - m0[0];
            double corr01 = o0[1] - m0[1];
            double corr02 = o0[2] - m0[2];
            double corr10 = o1[0] - m1[0];
            double corr11 = o1[1] - m1[1];
            double corr12 = o1[2] - m1[2];
            double corr20 = o2[0] - m2[0];
            double corr21 = o2[1] - m2[1];
            double corr22 = o2[2] - m2[2];
            double fn1 = ((((((((corr00 * corr00) + (corr01 * corr01)) + (corr02 * corr02)) + (corr10 * corr10)) + (corr11 * corr11)) + (corr12 * corr12)) + (corr20 * corr20)) + (corr21 * corr21)) + (corr22 * corr22);
            if (FastMath.abs(fn1 - fn) <= threshold) {
                return o;
            }
            x00 = o0[0];
            x01 = o0[1];
            x02 = o0[2];
            x10 = o1[0];
            x11 = o1[1];
            x12 = o1[2];
            x20 = o2[0];
            x21 = o2[1];
            x22 = o2[2];
            fn = fn1;
        }
        throw new NotARotationMatrixException(LocalizedFormats.UNABLE_TO_ORTHOGONOLIZE_MATRIX, Integer.valueOf(i - 1));
    }

    public static double distance(Rotation r1, Rotation r2) {
        return r1.applyInverseTo(r2).getAngle();
    }
}
