package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzdt.zzd;
import java.util.Map;
import java.util.concurrent.Future;

@zzgd
public final class zzgh {
    private String zzDK;
    private String zzDL;
    private zzhs<zzgj> zzDM;
    zzd zzDN;
    public final zzdg zzDO;
    public final zzdg zzDP;
    zzid zzoA;
    private final Object zzqt;

    /* renamed from: com.google.android.gms.internal.zzgh.1 */
    class C04731 implements zzdg {
        final /* synthetic */ zzgh zzDQ;

        C04731(zzgh com_google_android_gms_internal_zzgh) {
            this.zzDQ = com_google_android_gms_internal_zzgh;
        }

        public void zza(zzid com_google_android_gms_internal_zzid, Map<String, String> map) {
            synchronized (this.zzDQ.zzqt) {
                if (this.zzDQ.zzDM.isDone()) {
                } else if (this.zzDQ.zzDK.equals(map.get("request_id"))) {
                    zzgj com_google_android_gms_internal_zzgj = new zzgj(1, map);
                    zzb.zzaC("Invalid " + com_google_android_gms_internal_zzgj.getType() + " request error: " + com_google_android_gms_internal_zzgj.zzfG());
                    this.zzDQ.zzDM.zzf(com_google_android_gms_internal_zzgj);
                }
            }
        }
    }

    /* renamed from: com.google.android.gms.internal.zzgh.2 */
    class C04742 implements zzdg {
        final /* synthetic */ zzgh zzDQ;

        C04742(zzgh com_google_android_gms_internal_zzgh) {
            this.zzDQ = com_google_android_gms_internal_zzgh;
        }

        public void zza(zzid com_google_android_gms_internal_zzid, Map<String, String> map) {
            synchronized (this.zzDQ.zzqt) {
                if (this.zzDQ.zzDM.isDone()) {
                    return;
                }
                zzgj com_google_android_gms_internal_zzgj = new zzgj(-2, map);
                if (this.zzDQ.zzDK.equals(com_google_android_gms_internal_zzgj.getRequestId())) {
                    String url = com_google_android_gms_internal_zzgj.getUrl();
                    if (url == null) {
                        zzb.zzaC("URL missing in loadAdUrl GMSG.");
                        return;
                    }
                    if (url.contains("%40mediation_adapters%40")) {
                        String replaceAll = url.replaceAll("%40mediation_adapters%40", zzhg.zza(com_google_android_gms_internal_zzid.getContext(), (String) map.get("check_adapters"), this.zzDQ.zzDL));
                        com_google_android_gms_internal_zzgj.setUrl(replaceAll);
                        zzb.zzaB("Ad request URL modified to " + replaceAll);
                    }
                    this.zzDQ.zzDM.zzf(com_google_android_gms_internal_zzgj);
                    return;
                }
                zzb.zzaC(com_google_android_gms_internal_zzgj.getRequestId() + " ==== " + this.zzDQ.zzDK);
            }
        }
    }

    public zzgh(String str, String str2) {
        this.zzqt = new Object();
        this.zzDM = new zzhs();
        this.zzDO = new C04731(this);
        this.zzDP = new C04742(this);
        this.zzDL = str2;
        this.zzDK = str;
    }

    public void zzb(zzd com_google_android_gms_internal_zzdt_zzd) {
        this.zzDN = com_google_android_gms_internal_zzdt_zzd;
    }

    public void zze(zzid com_google_android_gms_internal_zzid) {
        this.zzoA = com_google_android_gms_internal_zzid;
    }

    public zzd zzfD() {
        return this.zzDN;
    }

    public Future<zzgj> zzfE() {
        return this.zzDM;
    }

    public void zzfF() {
        if (this.zzoA != null) {
            this.zzoA.destroy();
            this.zzoA = null;
        }
    }
}
