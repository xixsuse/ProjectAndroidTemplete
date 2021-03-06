package com.google.android.gms.internal;

import android.content.Context;

public class zzacx {
    private static zzacx zzaHl;
    private zzacw zzaHk;

    static {
        zzaHl = new zzacx();
    }

    public zzacx() {
        this.zzaHk = null;
    }

    public static zzacw zzaQ(Context context) {
        return zzaHl.zzaP(context);
    }

    public synchronized zzacw zzaP(Context context) {
        if (this.zzaHk == null) {
            if (context.getApplicationContext() != null) {
                context = context.getApplicationContext();
            }
            this.zzaHk = new zzacw(context);
        }
        return this.zzaHk;
    }
}
