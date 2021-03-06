package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zza.zzb;

public final class zzkn implements zzkm {

    private static class zza extends zzkk {
        private final zzb<Status> zzOs;

        public zza(zzb<Status> com_google_android_gms_common_api_zza_zzb_com_google_android_gms_common_api_Status) {
            this.zzOs = com_google_android_gms_common_api_zza_zzb_com_google_android_gms_common_api_Status;
        }

        public void zzbB(int i) throws RemoteException {
            this.zzOs.zzm(new Status(i));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzkn.1 */
    class C10181 extends zza {
        final /* synthetic */ zzkn zzabk;

        C10181(zzkn com_google_android_gms_internal_zzkn, GoogleApiClient googleApiClient) {
            this.zzabk = com_google_android_gms_internal_zzkn;
            super(googleApiClient);
        }

        protected void zza(zzkp com_google_android_gms_internal_zzkp) throws RemoteException {
            ((zzkr) com_google_android_gms_internal_zzkp.zznM()).zza(new zza(this));
        }
    }

    public PendingResult<Status> zzc(GoogleApiClient googleApiClient) {
        return googleApiClient.zzb(new C10181(this, googleApiClient));
    }
}
