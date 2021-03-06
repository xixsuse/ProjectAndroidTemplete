package com.google.android.gms.common.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zza;
import com.google.android.gms.common.api.zzd;

public class zzb {
    @NonNull
    public static zza zzaf(@NonNull Status status) {
        return status.hasResolution() ? new zzd(status) : new zza(status);
    }

    @NonNull
    public static zza zzk(@NonNull ConnectionResult connectionResult) {
        return zzaf(new Status(connectionResult.getErrorCode(), connectionResult.getErrorMessage(), connectionResult.getResolution()));
    }
}
