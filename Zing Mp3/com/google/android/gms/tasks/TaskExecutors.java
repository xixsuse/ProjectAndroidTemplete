package com.google.android.gms.tasks;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

public final class TaskExecutors {
    public static final Executor MAIN_THREAD;
    static final Executor aMT;

    /* renamed from: com.google.android.gms.tasks.TaskExecutors.1 */
    class C15551 implements Executor {
        C15551() {
        }

        public void execute(@NonNull Runnable runnable) {
            runnable.run();
        }
    }

    private static final class zza implements Executor {
        private final Handler mHandler;

        public zza() {
            this.mHandler = new Handler(Looper.getMainLooper());
        }

        public void execute(@NonNull Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }

    static {
        MAIN_THREAD = new zza();
        aMT = new C15551();
    }

    private TaskExecutors() {
    }
}
