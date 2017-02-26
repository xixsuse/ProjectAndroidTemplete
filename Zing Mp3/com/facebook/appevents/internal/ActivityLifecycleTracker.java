package com.facebook.appevents.internal;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdError;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.appevents.internal.SourceApplicationInfo.Factory;
import com.facebook.internal.FetchedAppSettings;
import com.facebook.internal.FetchedAppSettingsManager;
import com.facebook.internal.Utility;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ActivityLifecycleTracker {
    private static final String INCORRECT_IMPL_WARNING = "Unexpected activity pause without a matching activity resume. Logging data may be incorrect. Make sure you call activateApp from your Application's onCreate method";
    private static final long INTERRUPTION_THRESHOLD_MILLISECONDS = 1000;
    private static final String TAG;
    private static String appId;
    private static long currentActivityAppearTime;
    private static volatile ScheduledFuture currentFuture;
    private static volatile SessionInfo currentSession;
    private static AtomicInteger foregroundActivityCount;
    private static final ScheduledExecutorService singleThreadExecutor;
    private static AtomicBoolean tracking;

    /* renamed from: com.facebook.appevents.internal.ActivityLifecycleTracker.1 */
    static class C08261 implements ActivityLifecycleCallbacks {
        C08261() {
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            AppEventUtility.assertIsMainThread();
            ActivityLifecycleTracker.onActivityCreated(activity);
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
            AppEventUtility.assertIsMainThread();
            ActivityLifecycleTracker.onActivityResumed(activity);
        }

        public void onActivityPaused(Activity activity) {
            AppEventUtility.assertIsMainThread();
            ActivityLifecycleTracker.onActivityPaused(activity);
        }

        public void onActivityStopped(Activity activity) {
            AppEventsLogger.onContextStop();
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onActivityDestroyed(Activity activity) {
        }
    }

    /* renamed from: com.facebook.appevents.internal.ActivityLifecycleTracker.2 */
    static class C08272 implements Runnable {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ long val$currentTime;

        C08272(Activity activity, long j) {
            this.val$activity = activity;
            this.val$currentTime = j;
        }

        public void run() {
            if (ActivityLifecycleTracker.currentSession == null) {
                Context applicationContext = this.val$activity.getApplicationContext();
                String activityName = Utility.getActivityName(this.val$activity);
                SessionInfo lastSession = SessionInfo.getStoredSessionInfo();
                if (lastSession != null) {
                    SessionLogger.logDeactivateApp(applicationContext, activityName, lastSession, ActivityLifecycleTracker.appId);
                }
                ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(this.val$currentTime), null);
                SourceApplicationInfo sourceApplicationInfo = Factory.create(this.val$activity);
                ActivityLifecycleTracker.currentSession.setSourceApplicationInfo(sourceApplicationInfo);
                SessionLogger.logActivateApp(applicationContext, activityName, sourceApplicationInfo, ActivityLifecycleTracker.appId);
            }
        }
    }

    /* renamed from: com.facebook.appevents.internal.ActivityLifecycleTracker.3 */
    static class C08283 implements Runnable {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ long val$currentTime;

        C08283(Activity activity, long j) {
            this.val$activity = activity;
            this.val$currentTime = j;
        }

        public void run() {
            Context applicationContext = this.val$activity.getApplicationContext();
            String activityName = Utility.getActivityName(this.val$activity);
            if (ActivityLifecycleTracker.currentSession == null) {
                ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(this.val$currentTime), null);
                SessionLogger.logActivateApp(applicationContext, activityName, null, ActivityLifecycleTracker.appId);
            } else if (ActivityLifecycleTracker.currentSession.getSessionLastEventTime() != null) {
                long suspendTime = this.val$currentTime - ActivityLifecycleTracker.currentSession.getSessionLastEventTime().longValue();
                if (suspendTime > ((long) (ActivityLifecycleTracker.getSessionTimeoutInSeconds() * AdError.NETWORK_ERROR_CODE))) {
                    SessionLogger.logDeactivateApp(applicationContext, activityName, ActivityLifecycleTracker.currentSession, ActivityLifecycleTracker.appId);
                    SessionLogger.logActivateApp(applicationContext, activityName, null, ActivityLifecycleTracker.appId);
                    ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(this.val$currentTime), null);
                } else if (suspendTime > ActivityLifecycleTracker.INTERRUPTION_THRESHOLD_MILLISECONDS) {
                    ActivityLifecycleTracker.currentSession.incrementInterruptionCount();
                }
            }
            ActivityLifecycleTracker.currentSession.setSessionLastEventTime(Long.valueOf(this.val$currentTime));
            ActivityLifecycleTracker.currentSession.writeSessionToDisk();
        }
    }

    /* renamed from: com.facebook.appevents.internal.ActivityLifecycleTracker.4 */
    static class C08304 implements Runnable {
        final /* synthetic */ String val$activityName;
        final /* synthetic */ Context val$applicationContext;
        final /* synthetic */ long val$currentTime;

        /* renamed from: com.facebook.appevents.internal.ActivityLifecycleTracker.4.1 */
        class C08291 implements Runnable {
            C08291() {
            }

            public void run() {
                if (ActivityLifecycleTracker.foregroundActivityCount.get() <= 0) {
                    SessionLogger.logDeactivateApp(C08304.this.val$applicationContext, C08304.this.val$activityName, ActivityLifecycleTracker.currentSession, ActivityLifecycleTracker.appId);
                    SessionInfo.clearSavedSessionFromDisk();
                    ActivityLifecycleTracker.currentSession = null;
                }
                ActivityLifecycleTracker.currentFuture = null;
            }
        }

        C08304(long j, Context context, String str) {
            this.val$currentTime = j;
            this.val$applicationContext = context;
            this.val$activityName = str;
        }

        public void run() {
            long timeSpentOnActivityInSeconds = 0;
            if (ActivityLifecycleTracker.currentSession == null) {
                ActivityLifecycleTracker.currentSession = new SessionInfo(Long.valueOf(this.val$currentTime), null);
            }
            ActivityLifecycleTracker.currentSession.setSessionLastEventTime(Long.valueOf(this.val$currentTime));
            if (ActivityLifecycleTracker.foregroundActivityCount.get() <= 0) {
                ActivityLifecycleTracker.currentFuture = ActivityLifecycleTracker.singleThreadExecutor.schedule(new C08291(), (long) ActivityLifecycleTracker.getSessionTimeoutInSeconds(), TimeUnit.SECONDS);
            }
            long appearTime = ActivityLifecycleTracker.currentActivityAppearTime;
            if (appearTime > 0) {
                timeSpentOnActivityInSeconds = (this.val$currentTime - appearTime) / ActivityLifecycleTracker.INTERRUPTION_THRESHOLD_MILLISECONDS;
            }
            AutomaticAnalyticsLogger.logActivityTimeSpentEvent(this.val$applicationContext, ActivityLifecycleTracker.appId, this.val$activityName, timeSpentOnActivityInSeconds);
            ActivityLifecycleTracker.currentSession.writeSessionToDisk();
        }
    }

    static {
        TAG = ActivityLifecycleTracker.class.getCanonicalName();
        singleThreadExecutor = Executors.newSingleThreadScheduledExecutor();
        foregroundActivityCount = new AtomicInteger(0);
        tracking = new AtomicBoolean(false);
    }

    public static void startTracking(Application application, String appId) {
        if (tracking.compareAndSet(false, true)) {
            appId = appId;
            application.registerActivityLifecycleCallbacks(new C08261());
        }
    }

    public static boolean isTracking() {
        return tracking.get();
    }

    public static UUID getCurrentSessionGuid() {
        return currentSession != null ? currentSession.getSessionId() : null;
    }

    public static void onActivityCreated(Activity activity) {
        singleThreadExecutor.execute(new C08272(activity, System.currentTimeMillis()));
    }

    public static void onActivityResumed(Activity activity) {
        foregroundActivityCount.incrementAndGet();
        cancelCurrentTask();
        long currentTime = System.currentTimeMillis();
        currentActivityAppearTime = currentTime;
        singleThreadExecutor.execute(new C08283(activity, currentTime));
    }

    private static void onActivityPaused(Activity activity) {
        if (foregroundActivityCount.decrementAndGet() < 0) {
            foregroundActivityCount.set(0);
            Log.w(TAG, INCORRECT_IMPL_WARNING);
        }
        cancelCurrentTask();
        singleThreadExecutor.execute(new C08304(System.currentTimeMillis(), activity.getApplicationContext(), Utility.getActivityName(activity)));
    }

    private static int getSessionTimeoutInSeconds() {
        FetchedAppSettings settings = FetchedAppSettingsManager.getAppSettingsWithoutQuery(FacebookSdk.getApplicationId());
        if (settings == null) {
            return Constants.getDefaultAppEventsSessionTimeoutInSeconds();
        }
        return settings.getSessionTimeoutInSeconds();
    }

    private static void cancelCurrentTask() {
        if (currentFuture != null) {
            currentFuture.cancel(false);
        }
        currentFuture = null;
    }
}
