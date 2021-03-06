package com.google.android.gms.internal;

import com.google.android.exoplayer.ExoPlayer.Factory;

public class zzd implements zzo {
    private int zzn;
    private int zzo;
    private final int zzp;
    private final float zzq;

    public zzd() {
        this(Factory.DEFAULT_MIN_BUFFER_MS, 1, 1.0f);
    }

    public zzd(int i, int i2, float f) {
        this.zzn = i;
        this.zzp = i2;
        this.zzq = f;
    }

    public void zza(zzr com_google_android_gms_internal_zzr) throws zzr {
        this.zzo++;
        this.zzn = (int) (((float) this.zzn) + (((float) this.zzn) * this.zzq));
        if (!zze()) {
            throw com_google_android_gms_internal_zzr;
        }
    }

    public int zzc() {
        return this.zzn;
    }

    public int zzd() {
        return this.zzo;
    }

    protected boolean zze() {
        return this.zzo <= this.zzp;
    }
}
