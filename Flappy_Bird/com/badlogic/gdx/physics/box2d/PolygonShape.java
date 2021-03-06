package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape.Type;

public class PolygonShape extends Shape {
    private static float[] verts;

    private native void jniGetVertex(long j, int i, float[] fArr);

    private native int jniGetVertexCount(long j);

    private native void jniSet(long j, float[] fArr, int i, int i2);

    private native void jniSetAsBox(long j, float f, float f2);

    private native void jniSetAsBox(long j, float f, float f2, float f3, float f4, float f5);

    private native long newPolygonShape();

    public PolygonShape() {
        this.addr = newPolygonShape();
    }

    protected PolygonShape(long addr) {
        this.addr = addr;
    }

    public Type getType() {
        return Type.Polygon;
    }

    public void set(Vector2[] vertices) {
        float[] verts = new float[(vertices.length * 2)];
        int i = 0;
        int j = 0;
        while (i < vertices.length * 2) {
            verts[i] = vertices[j].f100x;
            verts[i + 1] = vertices[j].f101y;
            i += 2;
            j++;
        }
        jniSet(this.addr, verts, 0, verts.length);
    }

    public void set(float[] vertices) {
        jniSet(this.addr, vertices, 0, vertices.length);
    }

    public void set(float[] vertices, int offset, int len) {
        jniSet(this.addr, vertices, offset, len);
    }

    public void setAsBox(float hx, float hy) {
        jniSetAsBox(this.addr, hx, hy);
    }

    public void setAsBox(float hx, float hy, Vector2 center, float angle) {
        jniSetAsBox(this.addr, hx, hy, center.f100x, center.f101y, angle);
    }

    public int getVertexCount() {
        return jniGetVertexCount(this.addr);
    }

    static {
        verts = new float[2];
    }

    public void getVertex(int index, Vector2 vertex) {
        jniGetVertex(this.addr, index, verts);
        vertex.f100x = verts[0];
        vertex.f101y = verts[1];
    }
}
