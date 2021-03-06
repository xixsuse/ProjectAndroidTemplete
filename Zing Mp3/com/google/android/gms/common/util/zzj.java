package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.os.SystemClock;
import com.google.android.exoplayer.hls.HlsChunkSource;

public final class zzj {
    private static IntentFilter Gv;
    private static long Gw;
    private static float Gx;

    static {
        Gv = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Gx = Float.NaN;
    }

    @TargetApi(20)
    public static boolean zzb(PowerManager powerManager) {
        return zzs.zzayv() ? powerManager.isInteractive() : powerManager.isScreenOn();
    }

    @TargetApi(20)
    public static int zzck(Context context) {
        int i = 1;
        if (context == null || context.getApplicationContext() == null) {
            return -1;
        }
        Intent registerReceiver = context.getApplicationContext().registerReceiver(null, Gv);
        int i2 = ((registerReceiver == null ? 0 : registerReceiver.getIntExtra("plugged", 0)) & 7) != 0 ? 1 : 0;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        if (powerManager == null) {
            return -1;
        }
        int i3 = (zzb(powerManager) ? 1 : 0) << 1;
        if (i2 == 0) {
            i = 0;
        }
        return i3 | i;
    }

    public static synchronized float zzcl(Context context) {
        float f;
        synchronized (zzj.class) {
            if (SystemClock.elapsedRealtime() - Gw >= HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS || Float.isNaN(Gx)) {
                Intent registerReceiver = context.getApplicationContext().registerReceiver(null, Gv);
                if (registerReceiver != null) {
                    Gx = ((float) registerReceiver.getIntExtra("level", -1)) / ((float) registerReceiver.getIntExtra("scale", -1));
                }
                Gw = SystemClock.elapsedRealtime();
                f = Gx;
            } else {
                f = Gx;
            }
        }
        return f;
    }
}
