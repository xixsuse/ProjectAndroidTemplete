package com.google.android.gms.internal;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.facebook.internal.AnalyticsEvents;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.request.AutoClickProtectionConfigurationParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.safebrowsing.SafeBrowsingConfigParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzu;
import com.mp3download.zingmp3.BuildConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

@zzji
public final class zzjo {
    private int mOrientation;
    private String zzbna;
    private boolean zzbwe;
    private final AdRequestInfoParcel zzbws;
    private List<String> zzchv;
    private String zzcog;
    private String zzcoh;
    private List<String> zzcoi;
    private String zzcoj;
    private String zzcok;
    private String zzcol;
    private List<String> zzcom;
    private long zzcon;
    private boolean zzcoo;
    private final long zzcop;
    private long zzcoq;
    private boolean zzcor;
    private boolean zzcos;
    private boolean zzcot;
    private boolean zzcou;
    private boolean zzcov;
    private String zzcow;
    private boolean zzcox;
    private RewardItemParcel zzcoy;
    private List<String> zzcoz;
    private List<String> zzcpa;
    private boolean zzcpb;
    private AutoClickProtectionConfigurationParcel zzcpc;
    private boolean zzcpd;
    private String zzcpe;
    private List<String> zzcpf;
    private boolean zzcpg;
    private String zzcph;
    private SafeBrowsingConfigParcel zzcpi;

    public zzjo(AdRequestInfoParcel adRequestInfoParcel, String str) {
        this.zzcon = -1;
        this.zzcoo = false;
        this.zzcop = -1;
        this.zzcoq = -1;
        this.mOrientation = -1;
        this.zzcor = false;
        this.zzcos = false;
        this.zzcot = false;
        this.zzcou = true;
        this.zzcov = true;
        this.zzcow = BuildConfig.FLAVOR;
        this.zzcox = false;
        this.zzbwe = false;
        this.zzcpb = false;
        this.zzcpd = false;
        this.zzcoh = str;
        this.zzbws = adRequestInfoParcel;
    }

    private void zzaa(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Use-HTTPS");
        if (list != null && !list.isEmpty()) {
            this.zzcot = Boolean.valueOf((String) list.get(0)).booleanValue();
        }
    }

    private void zzab(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Content-Url-Opted-Out");
        if (list != null && !list.isEmpty()) {
            this.zzcou = Boolean.valueOf((String) list.get(0)).booleanValue();
        }
    }

    private void zzac(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Content-Vertical-Opted-Out");
        if (list != null && !list.isEmpty()) {
            this.zzcov = Boolean.valueOf((String) list.get(0)).booleanValue();
        }
    }

    private void zzad(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Gws-Query-Id");
        if (list != null && !list.isEmpty()) {
            this.zzcow = (String) list.get(0);
        }
    }

    private void zzae(Map<String, List<String>> map) {
        String zzd = zzd(map, "X-Afma-Fluid");
        if (zzd != null && zzd.equals("height")) {
            this.zzcox = true;
        }
    }

    private void zzaf(Map<String, List<String>> map) {
        this.zzbwe = "native_express".equals(zzd(map, "X-Afma-Ad-Format"));
    }

    private void zzag(Map<String, List<String>> map) {
        this.zzcoy = RewardItemParcel.zzct(zzd(map, "X-Afma-Rewards"));
    }

    private void zzah(Map<String, List<String>> map) {
        if (this.zzcoz == null) {
            this.zzcoz = zzf(map, "X-Afma-Reward-Video-Start-Urls");
        }
    }

    private void zzai(Map<String, List<String>> map) {
        if (this.zzcpa == null) {
            this.zzcpa = zzf(map, "X-Afma-Reward-Video-Complete-Urls");
        }
    }

    private void zzaj(Map<String, List<String>> map) {
        this.zzcpb |= zzg(map, "X-Afma-Use-Displayed-Impression");
    }

    private void zzak(Map<String, List<String>> map) {
        this.zzcpd |= zzg(map, "X-Afma-Auto-Collect-Location");
    }

    private void zzal(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Remote-Ping-Urls");
        if (zzf != null) {
            this.zzcpf = zzf;
        }
    }

    private void zzam(Map<String, List<String>> map) {
        Object zzd = zzd(map, "X-Afma-Auto-Protection-Configuration");
        if (zzd == null || TextUtils.isEmpty(zzd)) {
            Builder buildUpon = Uri.parse("https://pagead2.googlesyndication.com/pagead/gen_204").buildUpon();
            buildUpon.appendQueryParameter(TtmlNode.ATTR_ID, "gmob-apps-blocked-navigation");
            if (!TextUtils.isEmpty(this.zzcok)) {
                buildUpon.appendQueryParameter("debugDialog", this.zzcok);
            }
            boolean booleanValue = ((Boolean) zzdr.zzbdf.get()).booleanValue();
            String[] strArr = new String[1];
            String valueOf = String.valueOf(buildUpon.toString());
            String valueOf2 = String.valueOf("navigationURL");
            strArr[0] = new StringBuilder((String.valueOf(valueOf).length() + 18) + String.valueOf(valueOf2).length()).append(valueOf).append("&").append(valueOf2).append("={NAVIGATION_URL}").toString();
            this.zzcpc = new AutoClickProtectionConfigurationParcel(booleanValue, Arrays.asList(strArr));
            return;
        }
        try {
            this.zzcpc = AutoClickProtectionConfigurationParcel.zzh(new JSONObject(zzd));
        } catch (Throwable e) {
            zzb.zzc("Error parsing configuration JSON", e);
            this.zzcpc = new AutoClickProtectionConfigurationParcel();
        }
    }

    private void zzan(Map<String, List<String>> map) {
        this.zzcpe = zzd(map, "Set-Cookie");
    }

    private void zzao(Map<String, List<String>> map) {
        Object zzd = zzd(map, "X-Afma-Safe-Browsing");
        if (!TextUtils.isEmpty(zzd)) {
            try {
                this.zzcpi = SafeBrowsingConfigParcel.zzj(new JSONObject(zzd));
            } catch (Throwable e) {
                zzb.zzc("Error parsing safe browsing header", e);
            }
        }
    }

    static String zzd(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        return (list == null || list.isEmpty()) ? null : (String) list.get(0);
    }

    static long zze(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            try {
                return (long) (Float.parseFloat(str2) * 1000.0f);
            } catch (NumberFormatException e) {
                zzb.zzdi(new StringBuilder((String.valueOf(str).length() + 36) + String.valueOf(str2).length()).append("Could not parse float from ").append(str).append(" header: ").append(str2).toString());
            }
        }
        return -1;
    }

    static List<String> zzf(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            if (str2 != null) {
                return Arrays.asList(str2.trim().split("\\s+"));
            }
        }
        return null;
    }

    private boolean zzg(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        return (list == null || list.isEmpty() || !Boolean.valueOf((String) list.get(0)).booleanValue()) ? false : true;
    }

    private void zzl(Map<String, List<String>> map) {
        this.zzcog = zzd(map, "X-Afma-Ad-Size");
    }

    private void zzm(Map<String, List<String>> map) {
        this.zzcph = zzd(map, "X-Afma-Ad-Slot-Size");
    }

    private void zzn(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Click-Tracking-Urls");
        if (zzf != null) {
            this.zzcoi = zzf;
        }
    }

    private void zzo(Map<String, List<String>> map) {
        this.zzcoj = zzd(map, "X-Afma-Debug-Signals");
    }

    private void zzp(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Debug-Dialog");
        if (list != null && !list.isEmpty()) {
            this.zzcok = (String) list.get(0);
        }
    }

    private void zzq(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Tracking-Urls");
        if (zzf != null) {
            this.zzcom = zzf;
        }
    }

    private void zzr(Map<String, List<String>> map) {
        long zze = zze(map, "X-Afma-Interstitial-Timeout");
        if (zze != -1) {
            this.zzcon = zze;
        }
    }

    private void zzs(Map<String, List<String>> map) {
        this.zzcol = zzd(map, "X-Afma-ActiveView");
    }

    private void zzt(Map<String, List<String>> map) {
        this.zzcos = AnalyticsEvents.PARAMETER_SHARE_DIALOG_SHOW_NATIVE.equals(zzd(map, "X-Afma-Ad-Format"));
    }

    private void zzu(Map<String, List<String>> map) {
        this.zzcor |= zzg(map, "X-Afma-Custom-Rendering-Allowed");
    }

    private void zzv(Map<String, List<String>> map) {
        this.zzcoo |= zzg(map, "X-Afma-Mediation");
    }

    private void zzw(Map<String, List<String>> map) {
        this.zzcpg |= zzg(map, "X-Afma-Render-In-Browser");
    }

    private void zzx(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Manual-Tracking-Urls");
        if (zzf != null) {
            this.zzchv = zzf;
        }
    }

    private void zzy(Map<String, List<String>> map) {
        long zze = zze(map, "X-Afma-Refresh-Rate");
        if (zze != -1) {
            this.zzcoq = zze;
        }
    }

    private void zzz(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Orientation");
        if (list != null && !list.isEmpty()) {
            String str = (String) list.get(0);
            if ("portrait".equalsIgnoreCase(str)) {
                this.mOrientation = zzu.zzgo().zzvx();
            } else if ("landscape".equalsIgnoreCase(str)) {
                this.mOrientation = zzu.zzgo().zzvw();
            }
        }
    }

    public void zzb(String str, Map<String, List<String>> map, String str2) {
        this.zzbna = str2;
        zzk(map);
    }

    public AdResponseParcel zzj(long j) {
        return new AdResponseParcel(this.zzbws, this.zzcoh, this.zzbna, this.zzcoi, this.zzcom, this.zzcon, this.zzcoo, -1, this.zzchv, this.zzcoq, this.mOrientation, this.zzcog, j, this.zzcok, this.zzcol, this.zzcor, this.zzcos, this.zzcot, this.zzcou, false, this.zzcow, this.zzcox, this.zzbwe, this.zzcoy, this.zzcoz, this.zzcpa, this.zzcpb, this.zzcpc, this.zzcpd, this.zzcpe, this.zzcpf, this.zzcpg, this.zzcph, this.zzcpi, this.zzcoj, this.zzcov);
    }

    public void zzk(Map<String, List<String>> map) {
        zzl(map);
        zzm(map);
        zzn(map);
        zzo(map);
        zzp(map);
        zzq(map);
        zzr(map);
        zzv(map);
        zzx(map);
        zzy(map);
        zzz(map);
        zzs(map);
        zzaa(map);
        zzu(map);
        zzt(map);
        zzab(map);
        zzac(map);
        zzad(map);
        zzae(map);
        zzaf(map);
        zzag(map);
        zzah(map);
        zzai(map);
        zzaj(map);
        zzak(map);
        zzan(map);
        zzam(map);
        zzal(map);
        zzao(map);
        zzw(map);
    }
}
