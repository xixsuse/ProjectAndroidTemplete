package com.google.android.gms.fitness.data;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.example.games.basegameutils.GameHelper;
import java.util.List;

public class zzm implements Creator<RawBucket> {
    static void zza(RawBucket rawBucket, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, rawBucket.zzKT);
        zzb.zzc(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, rawBucket.zzCY);
        zzb.zza(parcel, 2, rawBucket.zzajH);
        zzb.zza(parcel, 3, rawBucket.zzajJ, i, false);
        zzb.zzc(parcel, 4, rawBucket.zzakG);
        zzb.zzc(parcel, 5, rawBucket.zzajS, false);
        zzb.zzc(parcel, 6, rawBucket.zzajT);
        zzb.zza(parcel, 7, rawBucket.zzajU);
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzcs(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzel(x0);
    }

    public RawBucket zzcs(Parcel parcel) {
        long j = 0;
        List list = null;
        boolean z = false;
        int zzab = zza.zzab(parcel);
        int i = 0;
        int i2 = 0;
        Session session = null;
        long j2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    j2 = zza.zzi(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    j = zza.zzi(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    session = (Session) zza.zza(parcel, zzaa, Session.CREATOR);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    i2 = zza.zzg(parcel, zzaa);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    list = zza.zzc(parcel, zzaa, RawDataSet.CREATOR);
                    break;
                case Place.TYPE_ATM /*6*/:
                    i = zza.zzg(parcel, zzaa);
                    break;
                case Place.TYPE_BAKERY /*7*/:
                    z = zza.zzc(parcel, zzaa);
                    break;
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i3 = zza.zzg(parcel, zzaa);
                    break;
                default:
                    zza.zzb(parcel, zzaa);
                    break;
            }
        }
        if (parcel.dataPosition() == zzab) {
            return new RawBucket(i3, j2, j, session, i2, list, i, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public RawBucket[] zzel(int i) {
        return new RawBucket[i];
    }
}
