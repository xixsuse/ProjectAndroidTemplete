package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.places.Place;
import com.google.example.games.basegameutils.GameHelper;

public class zze implements Creator<PlayLoggerContext> {
    static void zza(PlayLoggerContext playLoggerContext, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, playLoggerContext.versionCode);
        zzb.zza(parcel, 2, playLoggerContext.packageName, false);
        zzb.zzc(parcel, 3, playLoggerContext.zzaGP);
        zzb.zzc(parcel, 4, playLoggerContext.zzaGQ);
        zzb.zza(parcel, 5, playLoggerContext.zzaGR, false);
        zzb.zza(parcel, 6, playLoggerContext.zzaGS, false);
        zzb.zza(parcel, 7, playLoggerContext.zzaGT);
        zzb.zza(parcel, 8, playLoggerContext.zzaGU, false);
        zzb.zza(parcel, 9, playLoggerContext.zzaGV);
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfH(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zziw(x0);
    }

    public PlayLoggerContext zzfH(Parcel parcel) {
        String str = null;
        boolean z = false;
        int zzab = zza.zzab(parcel);
        boolean z2 = true;
        String str2 = null;
        String str3 = null;
        int i = 0;
        int i2 = 0;
        String str4 = null;
        int i3 = 0;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    i3 = zza.zzg(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    str4 = zza.zzo(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    i2 = zza.zzg(parcel, zzaa);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    i = zza.zzg(parcel, zzaa);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    str3 = zza.zzo(parcel, zzaa);
                    break;
                case Place.TYPE_ATM /*6*/:
                    str2 = zza.zzo(parcel, zzaa);
                    break;
                case Place.TYPE_BAKERY /*7*/:
                    z2 = zza.zzc(parcel, zzaa);
                    break;
                case GameHelper.CLIENT_SNAPSHOT /*8*/:
                    str = zza.zzo(parcel, zzaa);
                    break;
                case Place.TYPE_BAR /*9*/:
                    z = zza.zzc(parcel, zzaa);
                    break;
                default:
                    zza.zzb(parcel, zzaa);
                    break;
            }
        }
        if (parcel.dataPosition() == zzab) {
            return new PlayLoggerContext(i3, str4, i2, i, str3, str2, z2, str, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public PlayLoggerContext[] zziw(int i) {
        return new PlayLoggerContext[i];
    }
}
