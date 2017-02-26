package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.places.Place;
import com.google.example.games.basegameutils.GameHelper;

public class zzw implements Creator<ResolveAccountResponse> {
    static void zza(ResolveAccountResponse resolveAccountResponse, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, resolveAccountResponse.zzCY);
        zzb.zza(parcel, 2, resolveAccountResponse.zzZO, false);
        zzb.zza(parcel, 3, resolveAccountResponse.zzoa(), i, false);
        zzb.zza(parcel, 4, resolveAccountResponse.zzob());
        zzb.zza(parcel, 5, resolveAccountResponse.zzoc());
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzY(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzby(x0);
    }

    public ResolveAccountResponse zzY(Parcel parcel) {
        ConnectionResult connectionResult = null;
        boolean z = false;
        int zzab = zza.zzab(parcel);
        boolean z2 = false;
        IBinder iBinder = null;
        int i = 0;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    i = zza.zzg(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    iBinder = zza.zzp(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    connectionResult = (ConnectionResult) zza.zza(parcel, zzaa, ConnectionResult.CREATOR);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    z2 = zza.zzc(parcel, zzaa);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    z = zza.zzc(parcel, zzaa);
                    break;
                default:
                    zza.zzb(parcel, zzaa);
                    break;
            }
        }
        if (parcel.dataPosition() == zzab) {
            return new ResolveAccountResponse(i, iBinder, connectionResult, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public ResolveAccountResponse[] zzby(int i) {
        return new ResolveAccountResponse[i];
    }
}
