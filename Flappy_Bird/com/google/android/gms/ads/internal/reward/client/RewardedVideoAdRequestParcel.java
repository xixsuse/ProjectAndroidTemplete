package com.google.android.gms.ads.internal.reward.client;

import android.os.Parcel;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzgd;

@zzgd
public final class RewardedVideoAdRequestParcel implements SafeParcelable {
    public static final zzh CREATOR;
    public final int versionCode;
    public final AdRequestParcel zzCm;
    public final String zzpG;

    static {
        CREATOR = new zzh();
    }

    public RewardedVideoAdRequestParcel(int versionCode, AdRequestParcel adRequest, String adUnitId) {
        this.versionCode = versionCode;
        this.zzCm = adRequest;
        this.zzpG = adUnitId;
    }

    public RewardedVideoAdRequestParcel(AdRequestParcel adRequest, String adUnitId) {
        this(1, adRequest, adUnitId);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzh.zza(this, out, flags);
    }
}
