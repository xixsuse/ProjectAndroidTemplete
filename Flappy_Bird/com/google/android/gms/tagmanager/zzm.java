package com.google.android.gms.tagmanager;

import android.os.Build.VERSION;

class zzm<K, V> {
    final zza<K, V> zzaKw;

    public interface zza<K, V> {
        int sizeOf(K k, V v);
    }

    /* renamed from: com.google.android.gms.tagmanager.zzm.1 */
    class C05301 implements zza<K, V> {
        final /* synthetic */ zzm zzaKx;

        C05301(zzm com_google_android_gms_tagmanager_zzm) {
            this.zzaKx = com_google_android_gms_tagmanager_zzm;
        }

        public int sizeOf(K k, V v) {
            return 1;
        }
    }

    public zzm() {
        this.zzaKw = new C05301(this);
    }

    public zzl<K, V> zza(int i, zza<K, V> com_google_android_gms_tagmanager_zzm_zza_K__V) {
        if (i > 0) {
            return zzyj() < 12 ? new zzcw(i, com_google_android_gms_tagmanager_zzm_zza_K__V) : new zzba(i, com_google_android_gms_tagmanager_zzm_zza_K__V);
        } else {
            throw new IllegalArgumentException("maxSize <= 0");
        }
    }

    int zzyj() {
        return VERSION.SDK_INT;
    }
}
