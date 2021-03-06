package com.google.android.gms.ads.doubleclick;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.internal.client.zzad;
import com.google.android.gms.ads.internal.client.zzad.zza;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.mediation.customevent.CustomEvent;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzx;
import java.util.Date;
import java.util.List;
import java.util.Set;

public final class PublisherAdRequest {
    public static final String DEVICE_ID_EMULATOR;
    public static final int ERROR_CODE_INTERNAL_ERROR = 0;
    public static final int ERROR_CODE_INVALID_REQUEST = 1;
    public static final int ERROR_CODE_NETWORK_ERROR = 2;
    public static final int ERROR_CODE_NO_FILL = 3;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_UNKNOWN = 0;
    private final zzad zzakf;

    public static final class Builder {
        private final zza zzakg;

        public Builder() {
            this.zzakg = new zza();
        }

        public Builder addCategoryExclusion(String str) {
            this.zzakg.zzas(str);
            return this;
        }

        public Builder addCustomEventExtrasBundle(Class<? extends CustomEvent> cls, Bundle bundle) {
            this.zzakg.zzb(cls, bundle);
            return this;
        }

        public Builder addCustomTargeting(String str, String str2) {
            this.zzakg.zze(str, str2);
            return this;
        }

        public Builder addCustomTargeting(String str, List<String> list) {
            if (list != null) {
                this.zzakg.zze(str, zzx.zzia(",").zzb(list));
            }
            return this;
        }

        public Builder addKeyword(String str) {
            this.zzakg.zzam(str);
            return this;
        }

        public Builder addNetworkExtras(NetworkExtras networkExtras) {
            this.zzakg.zza(networkExtras);
            return this;
        }

        public Builder addNetworkExtrasBundle(Class<? extends MediationAdapter> cls, Bundle bundle) {
            this.zzakg.zza(cls, bundle);
            return this;
        }

        public Builder addTestDevice(String str) {
            this.zzakg.zzan(str);
            return this;
        }

        public PublisherAdRequest build() {
            return new PublisherAdRequest();
        }

        public Builder setBirthday(Date date) {
            this.zzakg.zza(date);
            return this;
        }

        public Builder setContentUrl(String str) {
            zzaa.zzb((Object) str, (Object) "Content URL must be non-null.");
            zzaa.zzh(str, "Content URL must be non-empty.");
            boolean z = str.length() <= AdRequest.MAX_CONTENT_URL_LENGTH;
            Object[] objArr = new Object[PublisherAdRequest.GENDER_FEMALE];
            objArr[PublisherAdRequest.ERROR_CODE_INTERNAL_ERROR] = Integer.valueOf(AdRequest.MAX_CONTENT_URL_LENGTH);
            objArr[PublisherAdRequest.GENDER_MALE] = Integer.valueOf(str.length());
            zzaa.zzb(z, "Content URL must not exceed %d in length.  Provided length was %d.", objArr);
            this.zzakg.zzap(str);
            return this;
        }

        public Builder setGender(int i) {
            this.zzakg.zzx(i);
            return this;
        }

        public Builder setIsDesignedForFamilies(boolean z) {
            this.zzakg.zzp(z);
            return this;
        }

        public Builder setLocation(Location location) {
            this.zzakg.zzb(location);
            return this;
        }

        @Deprecated
        public Builder setManualImpressionsEnabled(boolean z) {
            this.zzakg.setManualImpressionsEnabled(z);
            return this;
        }

        public Builder setPublisherProvidedId(String str) {
            this.zzakg.zzaq(str);
            return this;
        }

        public Builder setRequestAgent(String str) {
            this.zzakg.zzar(str);
            return this;
        }

        public Builder tagForChildDirectedTreatment(boolean z) {
            this.zzakg.zzo(z);
            return this;
        }
    }

    static {
        DEVICE_ID_EMULATOR = zzad.DEVICE_ID_EMULATOR;
    }

    private PublisherAdRequest(Builder builder) {
        this.zzakf = new zzad(builder.zzakg);
    }

    public static void updateCorrelator() {
    }

    public Date getBirthday() {
        return this.zzakf.getBirthday();
    }

    public String getContentUrl() {
        return this.zzakf.getContentUrl();
    }

    public <T extends CustomEvent> Bundle getCustomEventExtrasBundle(Class<T> cls) {
        return this.zzakf.getCustomEventExtrasBundle(cls);
    }

    public Bundle getCustomTargeting() {
        return this.zzakf.getCustomTargeting();
    }

    public int getGender() {
        return this.zzakf.getGender();
    }

    public Set<String> getKeywords() {
        return this.zzakf.getKeywords();
    }

    public Location getLocation() {
        return this.zzakf.getLocation();
    }

    public boolean getManualImpressionsEnabled() {
        return this.zzakf.getManualImpressionsEnabled();
    }

    @Deprecated
    public <T extends NetworkExtras> T getNetworkExtras(Class<T> cls) {
        return this.zzakf.getNetworkExtras(cls);
    }

    public <T extends MediationAdapter> Bundle getNetworkExtrasBundle(Class<T> cls) {
        return this.zzakf.getNetworkExtrasBundle(cls);
    }

    public String getPublisherProvidedId() {
        return this.zzakf.getPublisherProvidedId();
    }

    public boolean isTestDevice(Context context) {
        return this.zzakf.isTestDevice(context);
    }

    public zzad zzdt() {
        return this.zzakf;
    }
}
