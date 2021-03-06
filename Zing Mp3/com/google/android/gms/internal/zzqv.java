package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.api.PendingResult.zza;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class zzqv {
    private final Map<zzqq<?>, Boolean> zs;
    private final Map<TaskCompletionSource<?>, Boolean> zt;

    /* renamed from: com.google.android.gms.internal.zzqv.1 */
    class C14801 implements zza {
        final /* synthetic */ zzqq zu;
        final /* synthetic */ zzqv zv;

        C14801(zzqv com_google_android_gms_internal_zzqv, zzqq com_google_android_gms_internal_zzqq) {
            this.zv = com_google_android_gms_internal_zzqv;
            this.zu = com_google_android_gms_internal_zzqq;
        }

        public void zzx(Status status) {
            this.zv.zs.remove(this.zu);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzqv.2 */
    class C14812 implements OnCompleteListener<TResult> {
        final /* synthetic */ zzqv zv;
        final /* synthetic */ TaskCompletionSource zw;

        C14812(zzqv com_google_android_gms_internal_zzqv, TaskCompletionSource taskCompletionSource) {
            this.zv = com_google_android_gms_internal_zzqv;
            this.zw = taskCompletionSource;
        }

        public void onComplete(@NonNull Task<TResult> task) {
            this.zv.zt.remove(this.zw);
        }
    }

    public zzqv() {
        this.zs = Collections.synchronizedMap(new WeakHashMap());
        this.zt = Collections.synchronizedMap(new WeakHashMap());
    }

    private void zza(boolean z, Status status) {
        synchronized (this.zs) {
            Map hashMap = new HashMap(this.zs);
        }
        synchronized (this.zt) {
            Map hashMap2 = new HashMap(this.zt);
        }
        for (Entry entry : hashMap.entrySet()) {
            if (z || ((Boolean) entry.getValue()).booleanValue()) {
                ((zzqq) entry.getKey()).zzab(status);
            }
        }
        for (Entry entry2 : hashMap2.entrySet()) {
            if (z || ((Boolean) entry2.getValue()).booleanValue()) {
                ((TaskCompletionSource) entry2.getKey()).trySetException(new com.google.android.gms.common.api.zza(status));
            }
        }
    }

    void zza(zzqq<? extends Result> com_google_android_gms_internal_zzqq__extends_com_google_android_gms_common_api_Result, boolean z) {
        this.zs.put(com_google_android_gms_internal_zzqq__extends_com_google_android_gms_common_api_Result, Boolean.valueOf(z));
        com_google_android_gms_internal_zzqq__extends_com_google_android_gms_common_api_Result.zza(new C14801(this, com_google_android_gms_internal_zzqq__extends_com_google_android_gms_common_api_Result));
    }

    <TResult> void zza(TaskCompletionSource<TResult> taskCompletionSource, boolean z) {
        this.zt.put(taskCompletionSource, Boolean.valueOf(z));
        taskCompletionSource.getTask().addOnCompleteListener(new C14812(this, taskCompletionSource));
    }

    boolean zzasi() {
        return (this.zs.isEmpty() && this.zt.isEmpty()) ? false : true;
    }

    public void zzasj() {
        zza(false, zzrh.AG);
    }

    public void zzask() {
        zza(true, zzsg.ym);
    }
}
