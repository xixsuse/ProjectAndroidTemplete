package com.badlogic.gdx.physics.box2d;

public class ContactImpulse {
    long addr;
    final float[] normalImpulses;
    final float[] tangentImpulses;
    float[] tmp;
    final World world;

    private native int jniGetCount(long j);

    private native void jniGetNormalImpulses(long j, float[] fArr);

    private native void jniGetTangentImpulses(long j, float[] fArr);

    protected ContactImpulse(World world, long addr) {
        this.tmp = new float[2];
        this.normalImpulses = new float[2];
        this.tangentImpulses = new float[2];
        this.world = world;
        this.addr = addr;
    }

    public float[] getNormalImpulses() {
        jniGetNormalImpulses(this.addr, this.normalImpulses);
        return this.normalImpulses;
    }

    public float[] getTangentImpulses() {
        jniGetTangentImpulses(this.addr, this.tangentImpulses);
        return this.tangentImpulses;
    }

    public int getCount() {
        return jniGetCount(this.addr);
    }
}
