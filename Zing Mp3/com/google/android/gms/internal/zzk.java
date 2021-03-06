package com.google.android.gms.internal;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.android.exoplayer.C0989C;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

public abstract class zzk<T> implements Comparable<zzk<T>> {
    private final zza zzac;
    private final int zzad;
    private final String zzae;
    private final int zzaf;
    private final com.google.android.gms.internal.zzm.zza zzag;
    private Integer zzah;
    private zzl zzai;
    private boolean zzaj;
    private boolean zzak;
    private boolean zzal;
    private long zzam;
    private zzo zzan;
    private com.google.android.gms.internal.zzb.zza zzao;

    /* renamed from: com.google.android.gms.internal.zzk.1 */
    class C14191 implements Runnable {
        final /* synthetic */ String zzap;
        final /* synthetic */ long zzaq;
        final /* synthetic */ zzk zzar;

        C14191(zzk com_google_android_gms_internal_zzk, String str, long j) {
            this.zzar = com_google_android_gms_internal_zzk;
            this.zzap = str;
            this.zzaq = j;
        }

        public void run() {
            this.zzar.zzac.zza(this.zzap, this.zzaq);
            this.zzar.zzac.zzd(toString());
        }
    }

    public enum zza {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    public zzk(int i, String str, com.google.android.gms.internal.zzm.zza com_google_android_gms_internal_zzm_zza) {
        this.zzac = zza.zzbj ? new zza() : null;
        this.zzaj = true;
        this.zzak = false;
        this.zzal = false;
        this.zzam = 0;
        this.zzao = null;
        this.zzad = i;
        this.zzae = str;
        this.zzag = com_google_android_gms_internal_zzm_zza;
        zza(new zzd());
        this.zzaf = zzb(str);
    }

    private byte[] zza(Map<String, String> map, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (Entry entry : map.entrySet()) {
                stringBuilder.append(URLEncoder.encode((String) entry.getKey(), str));
                stringBuilder.append('=');
                stringBuilder.append(URLEncoder.encode((String) entry.getValue(), str));
                stringBuilder.append('&');
            }
            return stringBuilder.toString().getBytes(str);
        } catch (Throwable e) {
            Throwable th = e;
            String str2 = "Encoding not supported: ";
            String valueOf = String.valueOf(str);
            throw new RuntimeException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), th);
        }
    }

    private static int zzb(String str) {
        if (!TextUtils.isEmpty(str)) {
            Uri parse = Uri.parse(str);
            if (parse != null) {
                String host = parse.getHost();
                if (host != null) {
                    return host.hashCode();
                }
            }
        }
        return 0;
    }

    public /* synthetic */ int compareTo(Object obj) {
        return zzc((zzk) obj);
    }

    public Map<String, String> getHeaders() throws zza {
        return Collections.emptyMap();
    }

    public int getMethod() {
        return this.zzad;
    }

    public String getUrl() {
        return this.zzae;
    }

    public boolean isCanceled() {
        return false;
    }

    public String toString() {
        String str = "0x";
        String valueOf = String.valueOf(Integer.toHexString(zzf()));
        valueOf = valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
        str = "[ ] ";
        String valueOf2 = String.valueOf(getUrl());
        String valueOf3 = String.valueOf(zzq());
        String valueOf4 = String.valueOf(this.zzah);
        return new StringBuilder(((((String.valueOf(str).length() + 3) + String.valueOf(valueOf2).length()) + String.valueOf(valueOf).length()) + String.valueOf(valueOf3).length()) + String.valueOf(valueOf4).length()).append(str).append(valueOf2).append(" ").append(valueOf).append(" ").append(valueOf3).append(" ").append(valueOf4).toString();
    }

    public final zzk<?> zza(int i) {
        this.zzah = Integer.valueOf(i);
        return this;
    }

    public zzk<?> zza(com.google.android.gms.internal.zzb.zza com_google_android_gms_internal_zzb_zza) {
        this.zzao = com_google_android_gms_internal_zzb_zza;
        return this;
    }

    public zzk<?> zza(zzl com_google_android_gms_internal_zzl) {
        this.zzai = com_google_android_gms_internal_zzl;
        return this;
    }

    public zzk<?> zza(zzo com_google_android_gms_internal_zzo) {
        this.zzan = com_google_android_gms_internal_zzo;
        return this;
    }

    protected abstract zzm<T> zza(zzi com_google_android_gms_internal_zzi);

    protected abstract void zza(T t);

    protected zzr zzb(zzr com_google_android_gms_internal_zzr) {
        return com_google_android_gms_internal_zzr;
    }

    public int zzc(zzk<T> com_google_android_gms_internal_zzk_T) {
        zza zzq = zzq();
        zza zzq2 = com_google_android_gms_internal_zzk_T.zzq();
        return zzq == zzq2 ? this.zzah.intValue() - com_google_android_gms_internal_zzk_T.zzah.intValue() : zzq2.ordinal() - zzq.ordinal();
    }

    public void zzc(zzr com_google_android_gms_internal_zzr) {
        if (this.zzag != null) {
            this.zzag.zze(com_google_android_gms_internal_zzr);
        }
    }

    public void zzc(String str) {
        if (zza.zzbj) {
            this.zzac.zza(str, Thread.currentThread().getId());
        } else if (this.zzam == 0) {
            this.zzam = SystemClock.elapsedRealtime();
        }
    }

    void zzd(String str) {
        if (this.zzai != null) {
            this.zzai.zzf(this);
        }
        if (zza.zzbj) {
            long id = Thread.currentThread().getId();
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(Looper.getMainLooper()).post(new C14191(this, str, id));
                return;
            }
            this.zzac.zza(str, id);
            this.zzac.zzd(toString());
            return;
        }
        if (SystemClock.elapsedRealtime() - this.zzam >= 3000) {
            zzs.zzb("%d ms: %s", Long.valueOf(SystemClock.elapsedRealtime() - this.zzam), toString());
        }
    }

    public int zzf() {
        return this.zzaf;
    }

    public String zzg() {
        return getUrl();
    }

    public com.google.android.gms.internal.zzb.zza zzh() {
        return this.zzao;
    }

    @Deprecated
    protected Map<String, String> zzi() throws zza {
        return null;
    }

    @Deprecated
    protected String zzj() {
        return zzm();
    }

    @Deprecated
    public String zzk() {
        return zzn();
    }

    @Deprecated
    public byte[] zzl() throws zza {
        Map zzi = zzi();
        return (zzi == null || zzi.size() <= 0) ? null : zza(zzi, zzj());
    }

    protected String zzm() {
        return C0989C.UTF8_NAME;
    }

    public String zzn() {
        String str = "application/x-www-form-urlencoded; charset=";
        String valueOf = String.valueOf(zzm());
        return valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
    }

    public byte[] zzo() throws zza {
        return null;
    }

    public final boolean zzp() {
        return this.zzaj;
    }

    public zza zzq() {
        return zza.NORMAL;
    }

    public final int zzr() {
        return this.zzan.zzc();
    }

    public zzo zzs() {
        return this.zzan;
    }

    public void zzt() {
        this.zzal = true;
    }

    public boolean zzu() {
        return this.zzal;
    }
}
