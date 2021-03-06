package com.google.android.gms.fitness.request;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.example.games.basegameutils.GameHelper;
import java.util.List;

public class zzu implements Creator<SessionInsertRequest> {
    static void zza(SessionInsertRequest sessionInsertRequest, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zza(parcel, 1, sessionInsertRequest.getSession(), i, false);
        zzb.zzc(parcel, GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE, sessionInsertRequest.getVersionCode());
        zzb.zzc(parcel, 2, sessionInsertRequest.getDataSets(), false);
        zzb.zzc(parcel, 3, sessionInsertRequest.getAggregateDataPoints(), false);
        zzb.zza(parcel, 4, sessionInsertRequest.zzqU(), false);
        zzb.zza(parcel, 5, sessionInsertRequest.getPackageName(), false);
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzcT(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzeO(x0);
    }

    public SessionInsertRequest zzcT(Parcel parcel) {
        String str = null;
        int zzab = zza.zzab(parcel);
        int i = 0;
        IBinder iBinder = null;
        List list = null;
        List list2 = null;
        Session session = null;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    session = (Session) zza.zza(parcel, zzaa, Session.CREATOR);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    list2 = zza.zzc(parcel, zzaa, DataSet.CREATOR);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    list = zza.zzc(parcel, zzaa, DataPoint.CREATOR);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    iBinder = zza.zzp(parcel, zzaa);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
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
            return new SessionInsertRequest(i, session, list2, list, iBinder, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public SessionInsertRequest[] zzeO(int i) {
        return new SessionInsertRequest[i];
    }
}
