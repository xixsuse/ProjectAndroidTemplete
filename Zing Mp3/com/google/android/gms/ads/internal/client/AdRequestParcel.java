package com.google.android.gms.ads.internal.client;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.zzz;
import com.google.android.gms.internal.zzji;
import java.util.List;

@zzji
public final class AdRequestParcel extends AbstractSafeParcelable {
    public static final Creator<AdRequestParcel> CREATOR;
    public final Bundle extras;
    public final int versionCode;
    public final long zzayl;
    public final int zzaym;
    public final List<String> zzayn;
    public final boolean zzayo;
    public final int zzayp;
    public final boolean zzayq;
    public final String zzayr;
    public final SearchAdRequestParcel zzays;
    public final Location zzayt;
    public final String zzayu;
    public final Bundle zzayv;
    public final Bundle zzayw;
    public final List<String> zzayx;
    public final String zzayy;
    public final String zzayz;
    public final boolean zzaza;

    static {
        CREATOR = new zzg();
    }

    public AdRequestParcel(int i, long j, Bundle bundle, int i2, List<String> list, boolean z, int i3, boolean z2, String str, SearchAdRequestParcel searchAdRequestParcel, Location location, String str2, Bundle bundle2, Bundle bundle3, List<String> list2, String str3, String str4, boolean z3) {
        this.versionCode = i;
        this.zzayl = j;
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.extras = bundle;
        this.zzaym = i2;
        this.zzayn = list;
        this.zzayo = z;
        this.zzayp = i3;
        this.zzayq = z2;
        this.zzayr = str;
        this.zzays = searchAdRequestParcel;
        this.zzayt = location;
        this.zzayu = str2;
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        this.zzayv = bundle2;
        this.zzayw = bundle3;
        this.zzayx = list2;
        this.zzayy = str3;
        this.zzayz = str4;
        this.zzaza = z3;
    }

    public static void zzj(AdRequestParcel adRequestParcel) {
        adRequestParcel.zzayv.putBundle("com.google.ads.mediation.admob.AdMobAdapter", adRequestParcel.extras);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AdRequestParcel)) {
            return false;
        }
        AdRequestParcel adRequestParcel = (AdRequestParcel) obj;
        return this.versionCode == adRequestParcel.versionCode && this.zzayl == adRequestParcel.zzayl && zzz.equal(this.extras, adRequestParcel.extras) && this.zzaym == adRequestParcel.zzaym && zzz.equal(this.zzayn, adRequestParcel.zzayn) && this.zzayo == adRequestParcel.zzayo && this.zzayp == adRequestParcel.zzayp && this.zzayq == adRequestParcel.zzayq && zzz.equal(this.zzayr, adRequestParcel.zzayr) && zzz.equal(this.zzays, adRequestParcel.zzays) && zzz.equal(this.zzayt, adRequestParcel.zzayt) && zzz.equal(this.zzayu, adRequestParcel.zzayu) && zzz.equal(this.zzayv, adRequestParcel.zzayv) && zzz.equal(this.zzayw, adRequestParcel.zzayw) && zzz.equal(this.zzayx, adRequestParcel.zzayx) && zzz.equal(this.zzayy, adRequestParcel.zzayy) && zzz.equal(this.zzayz, adRequestParcel.zzayz) && this.zzaza == adRequestParcel.zzaza;
    }

    public int hashCode() {
        return zzz.hashCode(Integer.valueOf(this.versionCode), Long.valueOf(this.zzayl), this.extras, Integer.valueOf(this.zzaym), this.zzayn, Boolean.valueOf(this.zzayo), Integer.valueOf(this.zzayp), Boolean.valueOf(this.zzayq), this.zzayr, this.zzays, this.zzayt, this.zzayu, this.zzayv, this.zzayw, this.zzayx, this.zzayy, this.zzayz, Boolean.valueOf(this.zzaza));
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzg.zza(this, parcel, i);
    }
}
