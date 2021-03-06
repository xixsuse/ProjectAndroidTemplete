package com.google.android.gms.ads.internal.reward.mediation.client;

import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzji;

@zzji
public class zzb implements MediationRewardedVideoAdListener {
    private final zza zzcsb;

    public zzb(zza com_google_android_gms_ads_internal_reward_mediation_client_zza) {
        this.zzcsb = com_google_android_gms_ads_internal_reward_mediation_client_zza;
    }

    public void onAdClicked(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter) {
        zzaa.zzhs("onAdClicked must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onAdClicked.");
        try {
            this.zzcsb.zzv(zze.zzac(mediationRewardedVideoAdAdapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onAdClicked.", e);
        }
    }

    public void onAdClosed(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter) {
        zzaa.zzhs("onAdClosed must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onAdClosed.");
        try {
            this.zzcsb.zzu(zze.zzac(mediationRewardedVideoAdAdapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onAdClosed.", e);
        }
    }

    public void onAdFailedToLoad(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter, int i) {
        zzaa.zzhs("onAdFailedToLoad must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onAdFailedToLoad.");
        try {
            this.zzcsb.zzc(zze.zzac(mediationRewardedVideoAdAdapter), i);
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onAdFailedToLoad.", e);
        }
    }

    public void onAdLeftApplication(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter) {
        zzaa.zzhs("onAdLeftApplication must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onAdLeftApplication.");
        try {
            this.zzcsb.zzw(zze.zzac(mediationRewardedVideoAdAdapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onAdLeftApplication.", e);
        }
    }

    public void onAdLoaded(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter) {
        zzaa.zzhs("onAdLoaded must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onAdLoaded.");
        try {
            this.zzcsb.zzr(zze.zzac(mediationRewardedVideoAdAdapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onAdLoaded.", e);
        }
    }

    public void onAdOpened(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter) {
        zzaa.zzhs("onAdOpened must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onAdOpened.");
        try {
            this.zzcsb.zzs(zze.zzac(mediationRewardedVideoAdAdapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onAdOpened.", e);
        }
    }

    public void onInitializationFailed(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter, int i) {
        zzaa.zzhs("onInitializationFailed must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onInitializationFailed.");
        try {
            this.zzcsb.zzb(zze.zzac(mediationRewardedVideoAdAdapter), i);
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onInitializationFailed.", e);
        }
    }

    public void onInitializationSucceeded(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter) {
        zzaa.zzhs("onInitializationSucceeded must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onInitializationSucceeded.");
        try {
            this.zzcsb.zzq(zze.zzac(mediationRewardedVideoAdAdapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onInitializationSucceeded.", e);
        }
    }

    public void onRewarded(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter, RewardItem rewardItem) {
        zzaa.zzhs("onRewarded must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onRewarded.");
        if (rewardItem != null) {
            try {
                this.zzcsb.zza(zze.zzac(mediationRewardedVideoAdAdapter), new RewardItemParcel(rewardItem));
                return;
            } catch (Throwable e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onRewarded.", e);
                return;
            }
        }
        this.zzcsb.zza(zze.zzac(mediationRewardedVideoAdAdapter), new RewardItemParcel(mediationRewardedVideoAdAdapter.getClass().getName(), 1));
    }

    public void onVideoStarted(MediationRewardedVideoAdAdapter mediationRewardedVideoAdAdapter) {
        zzaa.zzhs("onVideoStarted must be called on the main UI thread.");
        com.google.android.gms.ads.internal.util.client.zzb.zzdg("Adapter called onVideoStarted.");
        try {
            this.zzcsb.zzt(zze.zzac(mediationRewardedVideoAdAdapter));
        } catch (Throwable e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Could not call onVideoStarted.", e);
        }
    }
}
