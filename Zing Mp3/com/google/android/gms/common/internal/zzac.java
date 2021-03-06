package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.mp3download.zingmp3.C1569R;

public class zzac implements Creator<ResolveAccountResponse> {
    static void zza(ResolveAccountResponse resolveAccountResponse, Parcel parcel, int i) {
        int zzcs = zzb.zzcs(parcel);
        zzb.zzc(parcel, 1, resolveAccountResponse.mVersionCode);
        zzb.zza(parcel, 2, resolveAccountResponse.Df, false);
        zzb.zza(parcel, 3, resolveAccountResponse.zzawn(), i, false);
        zzb.zza(parcel, 4, resolveAccountResponse.zzawo());
        zzb.zza(parcel, 5, resolveAccountResponse.zzawp());
        zzb.zzaj(parcel, zzcs);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzcn(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzgr(i);
    }

    public ResolveAccountResponse zzcn(Parcel parcel) {
        ConnectionResult connectionResult = null;
        boolean z = false;
        int zzcr = zza.zzcr(parcel);
        boolean z2 = false;
        IBinder iBinder = null;
        int i = 0;
        while (parcel.dataPosition() < zzcr) {
            int zzcq = zza.zzcq(parcel);
            switch (zza.zzgu(zzcq)) {
                case C1569R.styleable.com_facebook_profile_picture_view_com_facebook_is_cropped /*1*/:
                    i = zza.zzg(parcel, zzcq);
                    break;
                case C1569R.styleable.com_facebook_login_view_com_facebook_logout_text /*2*/:
                    iBinder = zza.zzr(parcel, zzcq);
                    break;
                case C1569R.styleable.com_facebook_login_view_com_facebook_tooltip_mode /*3*/:
                    connectionResult = (ConnectionResult) zza.zza(parcel, zzcq, ConnectionResult.CREATOR);
                    break;
                case C1569R.styleable.com_facebook_like_view_com_facebook_auxiliary_view_position /*4*/:
                    z2 = zza.zzc(parcel, zzcq);
                    break;
                case C1569R.styleable.com_facebook_like_view_com_facebook_horizontal_alignment /*5*/:
                    z = zza.zzc(parcel, zzcq);
                    break;
                default:
                    zza.zzb(parcel, zzcq);
                    break;
            }
        }
        if (parcel.dataPosition() == zzcr) {
            return new ResolveAccountResponse(i, iBinder, connectionResult, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzcr, parcel);
    }

    public ResolveAccountResponse[] zzgr(int i) {
        return new ResolveAccountResponse[i];
    }
}
