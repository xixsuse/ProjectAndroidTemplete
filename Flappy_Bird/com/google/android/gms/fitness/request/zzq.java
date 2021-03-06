package com.google.android.gms.fitness.request;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.example.games.basegameutils.GameHelper;
import java.util.List;

public class zzq implements Creator<ReadRawRequest> {
    static void zza(ReadRawRequest readRawRequest, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, readRawRequest.zzqU(), false);
        zzb.zzc(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, readRawRequest.getVersionCode());
        zzb.zza(parcel, 2, readRawRequest.getPackageName(), false);
        zzb.zzc(parcel, 3, readRawRequest.zzrf(), false);
        zzb.zza(parcel, 4, readRawRequest.zzra());
        zzb.zza(parcel, 5, readRawRequest.zzqZ());
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzcP(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzeJ(x0);
    }

    public ReadRawRequest zzcP(Parcel parcel) {
        List list = null;
        boolean z = false;
        int zzab = zza.zzab(parcel);
        boolean z2 = false;
        String str = null;
        IBinder iBinder = null;
        int i = 0;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    iBinder = zza.zzp(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    str = zza.zzo(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    list = zza.zzc(parcel, zzaa, DataSourceQueryParams.CREATOR);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    z2 = zza.zzc(parcel, zzaa);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    z = zza.zzc(parcel, zzaa);
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
            return new ReadRawRequest(i, iBinder, str, list, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public ReadRawRequest[] zzeJ(int i) {
        return new ReadRawRequest[i];
    }
}
