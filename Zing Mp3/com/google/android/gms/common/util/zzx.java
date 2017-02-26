package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.zzf;
import com.google.android.gms.internal.zzsz;

public final class zzx {
    @TargetApi(19)
    public static boolean zzc(Context context, int i, String str) {
        return zzsz.zzco(context).zzg(i, str);
    }

    public static boolean zzf(Context context, int i) {
        boolean z = false;
        if (!zzc(context, i, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE)) {
            return z;
        }
        try {
            return zzf.zzbv(context).zzb(context.getPackageManager(), context.getPackageManager().getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, 64));
        } catch (NameNotFoundException e) {
            if (!Log.isLoggable("UidVerifier", 3)) {
                return z;
            }
            Log.d("UidVerifier", "Package manager can't find google play services package, defaulting to false");
            return z;
        }
    }
}
