package com.google.android.gms.drive;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.places.Place;
import com.google.example.games.basegameutils.GameHelper;

public class zzh implements Creator<Permission> {
    static void zza(Permission permission, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, permission.zzCY);
        zzb.zza(parcel, 2, permission.zzpo(), false);
        zzb.zzc(parcel, 3, permission.zzpp());
        zzb.zza(parcel, 4, permission.zzpq(), false);
        zzb.zza(parcel, 5, permission.zzpr(), false);
        zzb.zzc(parcel, 6, permission.getRole());
        zzb.zza(parcel, 7, permission.zzps());
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzas(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzcb(x0);
    }

    public Permission zzas(Parcel parcel) {
        String str = null;
        boolean z = false;
        int zzab = zza.zzab(parcel);
        int i = 0;
        String str2 = null;
        int i2 = 0;
        String str3 = null;
        int i3 = 0;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    i3 = zza.zzg(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    str3 = zza.zzo(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    i2 = zza.zzg(parcel, zzaa);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    str2 = zza.zzo(parcel, zzaa);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    str = zza.zzo(parcel, zzaa);
                    break;
                case Place.TYPE_ATM /*6*/:
                    i = zza.zzg(parcel, zzaa);
                    break;
                case Place.TYPE_BAKERY /*7*/:
                    z = zza.zzc(parcel, zzaa);
                    break;
                default:
                    zza.zzb(parcel, zzaa);
                    break;
            }
        }
        if (parcel.dataPosition() == zzab) {
            return new Permission(i3, str3, i2, str2, str, i, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public Permission[] zzcb(int i) {
        return new Permission[i];
    }
}