package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.LocationRequest;
import com.google.example.games.basegameutils.GameHelper;

public class zzl implements Creator<PlaceRequest> {
    static void zza(PlaceRequest placeRequest, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, placeRequest.zzCY);
        zzb.zza(parcel, 2, placeRequest.zzuG(), i, false);
        zzb.zza(parcel, 3, placeRequest.getInterval());
        zzb.zzc(parcel, 4, placeRequest.getPriority());
        zzb.zza(parcel, 5, placeRequest.getExpirationTime());
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzev(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzgT(x0);
    }

    public PlaceRequest zzev(Parcel parcel) {
        int zzab = zza.zzab(parcel);
        int i = 0;
        PlaceFilter placeFilter = null;
        long j = PlaceRequest.zzazM;
        int i2 = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
        long j2 = Long.MAX_VALUE;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    placeFilter = (PlaceFilter) zza.zza(parcel, zzaa, PlaceFilter.CREATOR);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    j = zza.zzi(parcel, zzaa);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    i2 = zza.zzg(parcel, zzaa);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    j2 = zza.zzi(parcel, zzaa);
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
            return new PlaceRequest(i, placeFilter, j, i2, j2);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public PlaceRequest[] zzgT(int i) {
        return new PlaceRequest[i];
    }
}
