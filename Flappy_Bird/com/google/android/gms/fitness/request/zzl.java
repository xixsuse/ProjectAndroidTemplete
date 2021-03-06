package com.google.android.gms.fitness.request;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.GeofenceStatusCodes;

public class zzl implements Creator<DisableFitRequest> {
    static void zza(DisableFitRequest disableFitRequest, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, disableFitRequest.zzqU(), false);
        zzb.zzc(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, disableFitRequest.getVersionCode());
        zzb.zza(parcel, 2, disableFitRequest.getPackageName(), false);
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzcL(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzeF(x0);
    }

    public DisableFitRequest zzcL(Parcel parcel) {
        String str = null;
        int zzab = zza.zzab(parcel);
        int i = 0;
        IBinder iBinder = null;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    iBinder = zza.zzp(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    str = zza.zzo(parcel, zzaa);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i = zza.zzg(parcel, zzaa);
                    break;
                default:
                    zza.zzb(parcel, zzaa);
                    break;
            }
        }
        if (parcel.dataPosition() == zzab) {
            return new DisableFitRequest(i, iBinder, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public DisableFitRequest[] zzeF(int i) {
        return new DisableFitRequest[i];
    }
}
