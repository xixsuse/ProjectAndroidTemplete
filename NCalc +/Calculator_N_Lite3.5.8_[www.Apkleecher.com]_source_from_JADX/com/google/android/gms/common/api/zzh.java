package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.safeparcel.zzb.zza;
import com.google.android.gms.common.internal.safeparcel.zzc;
import org.apache.commons.math4.random.EmpiricalDistribution;
import org.apache.commons.math4.random.ValueServer;
import org.matheclipse.core.interfaces.IExpr;

public class zzh implements Creator<Status> {
    static void zza(Status status, Parcel parcel, int i) {
        int zzaV = zzc.zzaV(parcel);
        zzc.zzc(parcel, 1, status.getStatusCode());
        zzc.zza(parcel, 2, status.getStatusMessage(), false);
        zzc.zza(parcel, 3, status.zzuT(), i, false);
        zzc.zzc(parcel, EmpiricalDistribution.DEFAULT_BIN_COUNT, status.mVersionCode);
        zzc.zzJ(parcel, zzaV);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzaI(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzct(i);
    }

    public Status zzaI(Parcel parcel) {
        PendingIntent pendingIntent = null;
        int i = 0;
        int zzaU = zzb.zzaU(parcel);
        String str = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzaU) {
            int zzaT = zzb.zzaT(parcel);
            switch (zzb.zzcW(zzaT)) {
                case ValueServer.REPLAY_MODE /*1*/:
                    i = zzb.zzg(parcel, zzaT);
                    break;
                case IExpr.DOUBLEID /*2*/:
                    str = zzb.zzq(parcel, zzaT);
                    break;
                case ValueServer.EXPONENTIAL_MODE /*3*/:
                    pendingIntent = (PendingIntent) zzb.zza(parcel, zzaT, PendingIntent.CREATOR);
                    break;
                case EmpiricalDistribution.DEFAULT_BIN_COUNT /*1000*/:
                    i2 = zzb.zzg(parcel, zzaT);
                    break;
                default:
                    zzb.zzb(parcel, zzaT);
                    break;
            }
        }
        if (parcel.dataPosition() == zzaU) {
            return new Status(i2, i, str, pendingIntent);
        }
        throw new zza("Overread allowed size end=" + zzaU, parcel);
    }

    public Status[] zzct(int i) {
        return new Status[i];
    }
}
