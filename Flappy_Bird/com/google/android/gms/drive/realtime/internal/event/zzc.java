package com.google.android.gms.drive.realtime.internal.event;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.example.games.basegameutils.GameHelper;
import java.util.List;

public class zzc implements Creator<ParcelableEvent> {
    static void zza(ParcelableEvent parcelableEvent, Parcel parcel, int i) {
        int zzac = zzb.zzac(parcel);
        zzb.zzc(parcel, 1, parcelableEvent.zzCY);
        zzb.zza(parcel, 2, parcelableEvent.zzFE, false);
        zzb.zza(parcel, 3, parcelableEvent.zzEO, false);
        zzb.zzb(parcel, 4, parcelableEvent.zzaiC, false);
        zzb.zza(parcel, 5, parcelableEvent.zzaiD);
        zzb.zza(parcel, 6, parcelableEvent.zzaiw, false);
        zzb.zza(parcel, 7, parcelableEvent.zzaiG, false);
        zzb.zza(parcel, 8, parcelableEvent.zzaiH, i, false);
        zzb.zza(parcel, 9, parcelableEvent.zzaiI, i, false);
        zzb.zza(parcel, 10, parcelableEvent.zzaiJ, i, false);
        zzb.zza(parcel, 11, parcelableEvent.zzaiK, i, false);
        zzb.zza(parcel, 12, parcelableEvent.zzaiL, i, false);
        zzb.zza(parcel, 13, parcelableEvent.zzaiM, i, false);
        zzb.zza(parcel, 14, parcelableEvent.zzaiN, i, false);
        zzb.zza(parcel, 15, parcelableEvent.zzaiO, i, false);
        zzb.zza(parcel, 17, parcelableEvent.zzaiF);
        zzb.zza(parcel, 16, parcelableEvent.zzaiE);
        zzb.zza(parcel, 18, parcelableEvent.zzaiP, i, false);
        zzb.zzH(parcel, zzac);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzbZ(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return zzdP(x0);
    }

    public ParcelableEvent zzbZ(Parcel parcel) {
        int zzab = zza.zzab(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        List list = null;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        String str3 = null;
        String str4 = null;
        TextInsertedDetails textInsertedDetails = null;
        TextDeletedDetails textDeletedDetails = null;
        ValuesAddedDetails valuesAddedDetails = null;
        ValuesRemovedDetails valuesRemovedDetails = null;
        ValuesSetDetails valuesSetDetails = null;
        ValueChangedDetails valueChangedDetails = null;
        ReferenceShiftedDetails referenceShiftedDetails = null;
        ObjectChangedDetails objectChangedDetails = null;
        FieldChangedDetails fieldChangedDetails = null;
        while (parcel.dataPosition() < zzab) {
            int zzaa = zza.zzaa(parcel);
            switch (zza.zzbA(zzaa)) {
                case CompletionEvent.STATUS_FAILURE /*1*/:
                    i = zza.zzg(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CONFLICT /*2*/:
                    str = zza.zzo(parcel, zzaa);
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    str2 = zza.zzo(parcel, zzaa);
                    break;
                case GameHelper.CLIENT_APPSTATE /*4*/:
                    list = zza.zzC(parcel, zzaa);
                    break;
                case Place.TYPE_ART_GALLERY /*5*/:
                    z = zza.zzc(parcel, zzaa);
                    break;
                case Place.TYPE_ATM /*6*/:
                    str3 = zza.zzo(parcel, zzaa);
                    break;
                case Place.TYPE_BAKERY /*7*/:
                    str4 = zza.zzo(parcel, zzaa);
                    break;
                case GameHelper.CLIENT_SNAPSHOT /*8*/:
                    textInsertedDetails = (TextInsertedDetails) zza.zza(parcel, zzaa, TextInsertedDetails.CREATOR);
                    break;
                case Place.TYPE_BAR /*9*/:
                    textDeletedDetails = (TextDeletedDetails) zza.zza(parcel, zzaa, TextDeletedDetails.CREATOR);
                    break;
                case Place.TYPE_BEAUTY_SALON /*10*/:
                    valuesAddedDetails = (ValuesAddedDetails) zza.zza(parcel, zzaa, ValuesAddedDetails.CREATOR);
                    break;
                case Place.TYPE_BICYCLE_STORE /*11*/:
                    valuesRemovedDetails = (ValuesRemovedDetails) zza.zza(parcel, zzaa, ValuesRemovedDetails.CREATOR);
                    break;
                case Place.TYPE_BOOK_STORE /*12*/:
                    valuesSetDetails = (ValuesSetDetails) zza.zza(parcel, zzaa, ValuesSetDetails.CREATOR);
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR /*13*/:
                    valueChangedDetails = (ValueChangedDetails) zza.zza(parcel, zzaa, ValueChangedDetails.CREATOR);
                    break;
                case Place.TYPE_BUS_STATION /*14*/:
                    referenceShiftedDetails = (ReferenceShiftedDetails) zza.zza(parcel, zzaa, ReferenceShiftedDetails.CREATOR);
                    break;
                case GameHelper.CLIENT_ALL /*15*/:
                    objectChangedDetails = (ObjectChangedDetails) zza.zza(parcel, zzaa, ObjectChangedDetails.CREATOR);
                    break;
                case Place.TYPE_CAMPGROUND /*16*/:
                    z2 = zza.zzc(parcel, zzaa);
                    break;
                case Place.TYPE_CAR_DEALER /*17*/:
                    z3 = zza.zzc(parcel, zzaa);
                    break;
                case Place.TYPE_CAR_RENTAL /*18*/:
                    fieldChangedDetails = (FieldChangedDetails) zza.zza(parcel, zzaa, FieldChangedDetails.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzaa);
                    break;
            }
        }
        if (parcel.dataPosition() == zzab) {
            return new ParcelableEvent(i, str, str2, list, z, z2, z3, str3, str4, textInsertedDetails, textDeletedDetails, valuesAddedDetails, valuesRemovedDetails, valuesSetDetails, valueChangedDetails, referenceShiftedDetails, objectChangedDetails, fieldChangedDetails);
        }
        throw new zza.zza("Overread allowed size end=" + zzab, parcel);
    }

    public ParcelableEvent[] zzdP(int i) {
        return new ParcelableEvent[i];
    }
}
