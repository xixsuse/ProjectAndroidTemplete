package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.os.BuildCompat;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.util.Pair;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import edu.jas.vector.GenVectorModul;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.matheclipse.core.interfaces.IExpr;

/* compiled from: FragmentManager */
final class FragmentManagerImpl extends FragmentManager implements LayoutInflaterFactory {
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final boolean HONEYCOMB;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    ArrayList<Fragment> mActive;
    ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<Integer> mAvailIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    SparseArray<Parcelable> mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    Runnable[] mTmpActions;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;

    /* compiled from: FragmentManager */
    interface OpGenerator {
        boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl.1 */
    class FragmentManager implements Runnable {
        FragmentManager() {
        }

        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    }

    /* compiled from: FragmentManager */
    static class AnimateOnHWLayerIfNeededListener implements AnimationListener {
        private AnimationListener mOriginalListener;
        private boolean mShouldRunOnHWLayer;
        View mView;

        /* renamed from: android.support.v4.app.FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.1 */
        class FragmentManager implements Runnable {
            FragmentManager() {
            }

            public void run() {
                ViewCompat.setLayerType(AnimateOnHWLayerIfNeededListener.this.mView, 0, null);
            }
        }

        public AnimateOnHWLayerIfNeededListener(View v, Animation anim) {
            if (v != null && anim != null) {
                this.mView = v;
            }
        }

        public AnimateOnHWLayerIfNeededListener(View v, Animation anim, AnimationListener listener) {
            if (v != null && anim != null) {
                this.mOriginalListener = listener;
                this.mView = v;
                this.mShouldRunOnHWLayer = true;
            }
        }

        @CallSuper
        public void onAnimationStart(Animation animation) {
            if (this.mOriginalListener != null) {
                this.mOriginalListener.onAnimationStart(animation);
            }
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            if (this.mView != null && this.mShouldRunOnHWLayer) {
                if (ViewCompat.isAttachedToWindow(this.mView) || BuildCompat.isAtLeastN()) {
                    this.mView.post(new FragmentManager());
                } else {
                    ViewCompat.setLayerType(this.mView, 0, null);
                }
            }
            if (this.mOriginalListener != null) {
                this.mOriginalListener.onAnimationEnd(animation);
            }
        }

        public void onAnimationRepeat(Animation animation) {
            if (this.mOriginalListener != null) {
                this.mOriginalListener.onAnimationRepeat(animation);
            }
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl.2 */
    class FragmentManager extends AnimateOnHWLayerIfNeededListener {
        final /* synthetic */ Fragment val$fragment;

        FragmentManager(View v, Animation anim, Fragment fragment) {
            this.val$fragment = fragment;
            super(v, anim);
        }

        public void onAnimationEnd(Animation animation) {
            super.onAnimationEnd(animation);
            if (this.val$fragment.getAnimatingAway() != null) {
                this.val$fragment.setAnimatingAway(null);
                FragmentManagerImpl.this.moveToState(this.val$fragment, this.val$fragment.getStateAfterAnimating(), 0, 0, FragmentManagerImpl.HONEYCOMB);
            }
        }
    }

    /* compiled from: FragmentManager */
    static class FragmentTag {
        public static final int[] Fragment;
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        FragmentTag() {
        }

        static {
            Fragment = new int[]{16842755, 16842960, 16842961};
        }
    }

    /* compiled from: FragmentManager */
    private class PopBackStackState implements OpGenerator {
        final int mFlags;
        final int mId;
        final String mName;

        PopBackStackState(String name, int id, int flags) {
            this.mName = name;
            this.mId = id;
            this.mFlags = flags;
        }

        public boolean generateOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
            return FragmentManagerImpl.this.popBackStackState(records, isRecordPop, this.mName, this.mId, this.mFlags);
        }
    }

    /* compiled from: FragmentManager */
    static class StartEnterTransitionListener implements OnStartEnterTransitionListener {
        private final boolean mIsBack;
        private int mNumPostponed;
        private final BackStackRecord mRecord;

        StartEnterTransitionListener(BackStackRecord record, boolean isBack) {
            this.mIsBack = isBack;
            this.mRecord = record;
        }

        public void onStartEnterTransition() {
            this.mNumPostponed--;
            if (this.mNumPostponed == 0) {
                this.mRecord.mManager.scheduleCommit();
            }
        }

        public void startListening() {
            this.mNumPostponed += FragmentManagerImpl.ANIM_STYLE_OPEN_ENTER;
        }

        public boolean isReady() {
            return this.mNumPostponed == 0 ? true : FragmentManagerImpl.HONEYCOMB;
        }

        public void completeTransaction() {
            boolean canceled;
            boolean z = FragmentManagerImpl.HONEYCOMB;
            if (this.mNumPostponed > 0) {
                canceled = true;
            } else {
                canceled = FragmentManagerImpl.HONEYCOMB;
            }
            FragmentManagerImpl manager = this.mRecord.mManager;
            int numAdded = manager.mAdded.size();
            for (int i = 0; i < numAdded; i += FragmentManagerImpl.ANIM_STYLE_OPEN_ENTER) {
                Fragment fragment = (Fragment) manager.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (canceled && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            FragmentManagerImpl fragmentManagerImpl = this.mRecord.mManager;
            BackStackRecord backStackRecord = this.mRecord;
            boolean z2 = this.mIsBack;
            if (!canceled) {
                z = true;
            }
            fragmentManagerImpl.completeExecute(backStackRecord, z2, z, true);
        }

        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, FragmentManagerImpl.HONEYCOMB, FragmentManagerImpl.HONEYCOMB);
        }
    }

    public boolean execPendingActions() {
        /* JADX: method processing error */
/*
        Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r4 = this;
        r3 = 1;
        r4.ensureExecReady(r3);
        r0 = 0;
    L_0x0005:
        r1 = r4.mTmpRecords;
        r2 = r4.mTmpIsPop;
        r1 = r4.generateOpsForPendingActions(r1, r2);
        if (r1 == 0) goto L_0x0022;
    L_0x000f:
        r4.mExecutingActions = r3;
        r1 = r4.mTmpRecords;	 Catch:{ all -> 0x001d }
        r2 = r4.mTmpIsPop;	 Catch:{ all -> 0x001d }
        r4.optimizeAndExecuteOps(r1, r2);	 Catch:{ all -> 0x001d }
        r4.cleanupExec();
        r0 = 1;
        goto L_0x0005;
    L_0x001d:
        r1 = move-exception;
        r4.cleanupExec();
        throw r1;
    L_0x0022:
        r4.doPendingDeferredStart();
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.execPendingActions():boolean");
    }

    FragmentManagerImpl() {
        this.mCurState = 0;
        this.mStateBundle = null;
        this.mStateArray = null;
        this.mExecCommit = new FragmentManager();
    }

    static {
        boolean z = HONEYCOMB;
        DEBUG = HONEYCOMB;
        if (VERSION.SDK_INT >= 11) {
            z = true;
        }
        HONEYCOMB = z;
        sAnimationListenerField = null;
        DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    }

    static boolean modifiesAlpha(Animation anim) {
        if (anim instanceof AlphaAnimation) {
            return true;
        }
        if (anim instanceof AnimationSet) {
            List<Animation> anims = ((AnimationSet) anim).getAnimations();
            for (int i = 0; i < anims.size(); i += ANIM_STYLE_OPEN_ENTER) {
                if (anims.get(i) instanceof AlphaAnimation) {
                    return true;
                }
            }
        }
        return HONEYCOMB;
    }

    static boolean shouldRunOnHWLayer(View v, Animation anim) {
        return (VERSION.SDK_INT >= 19 && ViewCompat.getLayerType(v) == 0 && ViewCompat.hasOverlappingRendering(v) && modifiesAlpha(anim)) ? true : HONEYCOMB;
    }

    private void throwException(RuntimeException ex) {
        Log.e(TAG, ex.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter pw = new PrintWriter(new LogWriter(TAG));
        if (this.mHost != null) {
            try {
                this.mHost.onDump("  ", null, pw, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", null, pw, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw ex;
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public boolean executePendingTransactions() {
        boolean updates = execPendingActions();
        forcePostponedTransactions();
        return updates;
    }

    public void popBackStack() {
        enqueueAction(new PopBackStackState(null, -1, 0), HONEYCOMB);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        return popBackStackImmediate(null, -1, 0);
    }

    public void popBackStack(String name, int flags) {
        enqueueAction(new PopBackStackState(name, -1, flags), HONEYCOMB);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        checkStateLoss();
        return popBackStackImmediate(name, -1, flags);
    }

    public void popBackStack(int id, int flags) {
        if (id < 0) {
            throw new IllegalArgumentException("Bad id: " + id);
        }
        enqueueAction(new PopBackStackState(null, id, flags), HONEYCOMB);
    }

    public boolean popBackStackImmediate(int id, int flags) {
        checkStateLoss();
        execPendingActions();
        if (id >= 0) {
            return popBackStackImmediate(null, id, flags);
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    private boolean popBackStackImmediate(String name, int id, int flags) {
        execPendingActions();
        ensureExecReady(true);
        boolean executePop = popBackStackState(this.mTmpRecords, this.mTmpIsPop, name, id, flags);
        if (executePop) {
            this.mExecutingActions = true;
            try {
                optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        doPendingDeferredStart();
        return executePop;
    }

    public int getBackStackEntryCount() {
        return this.mBackStack != null ? this.mBackStack.size() : 0;
    }

    public BackStackEntry getBackStackEntryAt(int index) {
        return (BackStackEntry) this.mBackStack.get(index);
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(listener);
        }
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putInt(key, fragment.mIndex);
    }

    public Fragment getFragment(Bundle bundle, String key) {
        int index = bundle.getInt(key, -1);
        if (index == -1) {
            return null;
        }
        if (index >= this.mActive.size()) {
            throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": index " + index));
        }
        Fragment f = (Fragment) this.mActive.get(index);
        if (f != null) {
            return f;
        }
        throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": index " + index));
        return f;
    }

    public List<Fragment> getFragments() {
        return this.mActive;
    }

    public SavedState saveFragmentInstanceState(Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        if (fragment.mState <= 0) {
            return null;
        }
        Bundle result = saveFragmentBasicState(fragment);
        if (result != null) {
            return new SavedState(result);
        }
        return null;
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(IExpr.SYMBOLID);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        int N;
        int i;
        Fragment f;
        String innerPrefix = prefix + "    ";
        if (this.mActive != null) {
            N = this.mActive.size();
            if (N > 0) {
                writer.print(prefix);
                writer.print("Active Fragments in ");
                writer.print(Integer.toHexString(System.identityHashCode(this)));
                writer.println(":");
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    f = (Fragment) this.mActive.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f);
                    if (f != null) {
                        f.dump(innerPrefix, fd, writer, args);
                    }
                }
            }
        }
        if (this.mAdded != null) {
            N = this.mAdded.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Added Fragments:");
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    f = (Fragment) this.mAdded.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                }
            }
        }
        if (this.mCreatedMenus != null) {
            N = this.mCreatedMenus.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Fragments Created Menus:");
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    f = (Fragment) this.mCreatedMenus.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            N = this.mBackStack.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Back Stack:");
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    BackStackRecord bs = (BackStackRecord) this.mBackStack.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(bs.toString());
                    bs.dump(innerPrefix, fd, writer, args);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                N = this.mBackStackIndices.size();
                if (N > 0) {
                    writer.print(prefix);
                    writer.println("Back Stack Indices:");
                    for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                        bs = (BackStackRecord) this.mBackStackIndices.get(i);
                        writer.print(prefix);
                        writer.print("  #");
                        writer.print(i);
                        writer.print(": ");
                        writer.println(bs);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                writer.print(prefix);
                writer.print("mAvailBackStackIndices: ");
                writer.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        if (this.mPendingActions != null) {
            N = this.mPendingActions.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Pending Actions:");
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    OpGenerator r = (OpGenerator) this.mPendingActions.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(r);
                }
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(this.mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(this.mContainer);
        if (this.mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(this.mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            writer.print(prefix);
            writer.print("  mNoTransactionsBecause=");
            writer.println(this.mNoTransactionsBecause);
        }
        if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
            writer.print(prefix);
            writer.print("  mAvailIndices: ");
            writer.println(Arrays.toString(this.mAvailIndices.toArray()));
        }
    }

    static Animation makeOpenCloseAnimation(Context context, float startScale, float endScale, float startAlpha, float endAlpha) {
        AnimationSet set = new AnimationSet(HONEYCOMB);
        ScaleAnimation scale = new ScaleAnimation(startScale, endScale, startScale, endScale, ANIM_STYLE_OPEN_ENTER, GenVectorModul.DEFAULT_DENSITY, ANIM_STYLE_OPEN_ENTER, GenVectorModul.DEFAULT_DENSITY);
        scale.setInterpolator(DECELERATE_QUINT);
        scale.setDuration(220);
        set.addAnimation(scale);
        AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
        alpha.setInterpolator(DECELERATE_CUBIC);
        alpha.setDuration(220);
        set.addAnimation(alpha);
        return set;
    }

    static Animation makeFadeAnimation(Context context, float start, float end) {
        AlphaAnimation anim = new AlphaAnimation(start, end);
        anim.setInterpolator(DECELERATE_CUBIC);
        anim.setDuration(220);
        return anim;
    }

    Animation loadAnimation(Fragment fragment, int transit, boolean enter, int transitionStyle) {
        Animation animObj = fragment.onCreateAnimation(transit, enter, fragment.getNextAnim());
        if (animObj != null) {
            return animObj;
        }
        if (fragment.getNextAnim() != 0) {
            Animation anim = AnimationUtils.loadAnimation(this.mHost.getContext(), fragment.getNextAnim());
            if (anim != null) {
                return anim;
            }
        }
        if (transit == 0) {
            return null;
        }
        int styleIndex = transitToStyleIndex(transit, enter);
        if (styleIndex < 0) {
            return null;
        }
        switch (styleIndex) {
            case ANIM_STYLE_OPEN_ENTER /*1*/:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            case ANIM_STYLE_OPEN_EXIT /*2*/:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            case ANIM_STYLE_CLOSE_ENTER /*3*/:
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            case ANIM_STYLE_CLOSE_EXIT /*4*/:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            case ANIM_STYLE_FADE_ENTER /*5*/:
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            case ANIM_STYLE_FADE_EXIT /*6*/:
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            default:
                if (transitionStyle == 0 && this.mHost.onHasWindowAnimations()) {
                    transitionStyle = this.mHost.onGetWindowAnimations();
                }
                if (transitionStyle == 0) {
                    return null;
                }
                return null;
        }
    }

    public void performPendingDeferredStart(Fragment f) {
        if (!f.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
        }
        f.mDeferStart = HONEYCOMB;
        moveToState(f, this.mCurState, 0, 0, HONEYCOMB);
    }

    private void setHWLayerAnimListenerIfAlpha(View v, Animation anim) {
        if (v != null && anim != null && shouldRunOnHWLayer(v, anim)) {
            AnimationListener originalListener = null;
            try {
                if (sAnimationListenerField == null) {
                    sAnimationListenerField = Animation.class.getDeclaredField("mListener");
                    sAnimationListenerField.setAccessible(true);
                }
                originalListener = (AnimationListener) sAnimationListenerField.get(anim);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "No field with the name mListener is found in Animation class", e);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "Cannot access Animation's mListener field", e2);
            }
            ViewCompat.setLayerType(v, ANIM_STYLE_OPEN_EXIT, null);
            anim.setAnimationListener(new AnimateOnHWLayerIfNeededListener(v, anim, originalListener));
        }
    }

    boolean isStateAtLeast(int state) {
        return this.mCurState >= state ? true : HONEYCOMB;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void moveToState(android.support.v4.app.Fragment r18, int r19, int r20, int r21, boolean r22) {
        /*
        r17 = this;
        r0 = r18;
        r4 = r0.mAdded;
        if (r4 == 0) goto L_0x000c;
    L_0x0006:
        r0 = r18;
        r4 = r0.mDetached;
        if (r4 == 0) goto L_0x0013;
    L_0x000c:
        r4 = 1;
        r0 = r19;
        if (r0 <= r4) goto L_0x0013;
    L_0x0011:
        r19 = 1;
    L_0x0013:
        r0 = r18;
        r4 = r0.mRemoving;
        if (r4 == 0) goto L_0x0027;
    L_0x0019:
        r0 = r18;
        r4 = r0.mState;
        r0 = r19;
        if (r0 <= r4) goto L_0x0027;
    L_0x0021:
        r0 = r18;
        r0 = r0.mState;
        r19 = r0;
    L_0x0027:
        r0 = r18;
        r4 = r0.mDeferStart;
        if (r4 == 0) goto L_0x003b;
    L_0x002d:
        r0 = r18;
        r4 = r0.mState;
        r5 = 4;
        if (r4 >= r5) goto L_0x003b;
    L_0x0034:
        r4 = 3;
        r0 = r19;
        if (r0 <= r4) goto L_0x003b;
    L_0x0039:
        r19 = 3;
    L_0x003b:
        r0 = r18;
        r4 = r0.mState;
        r0 = r19;
        if (r4 >= r0) goto L_0x0472;
    L_0x0043:
        r0 = r18;
        r4 = r0.mFromLayout;
        if (r4 == 0) goto L_0x0050;
    L_0x0049:
        r0 = r18;
        r4 = r0.mInLayout;
        if (r4 != 0) goto L_0x0050;
    L_0x004f:
        return;
    L_0x0050:
        r4 = r18.getAnimatingAway();
        if (r4 == 0) goto L_0x006a;
    L_0x0056:
        r4 = 0;
        r0 = r18;
        r0.setAnimatingAway(r4);
        r6 = r18.getStateAfterAnimating();
        r7 = 0;
        r8 = 0;
        r9 = 1;
        r4 = r17;
        r5 = r18;
        r4.moveToState(r5, r6, r7, r8, r9);
    L_0x006a:
        r0 = r18;
        r4 = r0.mState;
        switch(r4) {
            case 0: goto L_0x00ba;
            case 1: goto L_0x025d;
            case 2: goto L_0x03b6;
            case 3: goto L_0x03c0;
            case 4: goto L_0x03ee;
            default: goto L_0x0071;
        };
    L_0x0071:
        r0 = r18;
        r4 = r0.mState;
        r0 = r19;
        if (r4 == r0) goto L_0x004f;
    L_0x0079:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "moveToState: Fragment state for ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r6 = " not updated inline; ";
        r5 = r5.append(r6);
        r6 = "expected state ";
        r5 = r5.append(r6);
        r0 = r19;
        r5 = r5.append(r0);
        r6 = " found ";
        r5 = r5.append(r6);
        r0 = r18;
        r6 = r0.mState;
        r5 = r5.append(r6);
        r5 = r5.toString();
        android.util.Log.w(r4, r5);
        r0 = r19;
        r1 = r18;
        r1.mState = r0;
        goto L_0x004f;
    L_0x00ba:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x00d8;
    L_0x00be:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "moveto CREATED: ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        android.util.Log.v(r4, r5);
    L_0x00d8:
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        if (r4 == 0) goto L_0x0145;
    L_0x00de:
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r0 = r17;
        r5 = r0.mHost;
        r5 = r5.getContext();
        r5 = r5.getClassLoader();
        r4.setClassLoader(r5);
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r5 = "android:view_state";
        r4 = r4.getSparseParcelableArray(r5);
        r0 = r18;
        r0.mSavedViewState = r4;
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r5 = "android:target_state";
        r0 = r17;
        r4 = r0.getFragment(r4, r5);
        r0 = r18;
        r0.mTarget = r4;
        r0 = r18;
        r4 = r0.mTarget;
        if (r4 == 0) goto L_0x0124;
    L_0x0115:
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r5 = "android:target_req_state";
        r6 = 0;
        r4 = r4.getInt(r5, r6);
        r0 = r18;
        r0.mTargetRequestCode = r4;
    L_0x0124:
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r5 = "android:user_visible_hint";
        r6 = 1;
        r4 = r4.getBoolean(r5, r6);
        r0 = r18;
        r0.mUserVisibleHint = r4;
        r0 = r18;
        r4 = r0.mUserVisibleHint;
        if (r4 != 0) goto L_0x0145;
    L_0x0139:
        r4 = 1;
        r0 = r18;
        r0.mDeferStart = r4;
        r4 = 3;
        r0 = r19;
        if (r0 <= r4) goto L_0x0145;
    L_0x0143:
        r19 = 3;
    L_0x0145:
        r0 = r17;
        r4 = r0.mHost;
        r0 = r18;
        r0.mHost = r4;
        r0 = r17;
        r4 = r0.mParent;
        r0 = r18;
        r0.mParentFragment = r4;
        r0 = r17;
        r4 = r0.mParent;
        if (r4 == 0) goto L_0x01ae;
    L_0x015b:
        r0 = r17;
        r4 = r0.mParent;
        r4 = r4.mChildFragmentManager;
    L_0x0161:
        r0 = r18;
        r0.mFragmentManager = r4;
        r0 = r17;
        r4 = r0.mHost;
        r4 = r4.getContext();
        r5 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentPreAttached(r1, r4, r5);
        r4 = 0;
        r0 = r18;
        r0.mCalled = r4;
        r0 = r17;
        r4 = r0.mHost;
        r4 = r4.getContext();
        r0 = r18;
        r0.onAttach(r4);
        r0 = r18;
        r4 = r0.mCalled;
        if (r4 != 0) goto L_0x01b7;
    L_0x018d:
        r4 = new android.support.v4.app.SuperNotCalledException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Fragment ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r6 = " did not call through to super.onAttach()";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
    L_0x01ae:
        r0 = r17;
        r4 = r0.mHost;
        r4 = r4.getFragmentManagerImpl();
        goto L_0x0161;
    L_0x01b7:
        r0 = r18;
        r4 = r0.mParentFragment;
        if (r4 != 0) goto L_0x0428;
    L_0x01bd:
        r0 = r17;
        r4 = r0.mHost;
        r0 = r18;
        r4.onAttachFragment(r0);
    L_0x01c6:
        r0 = r17;
        r4 = r0.mHost;
        r4 = r4.getContext();
        r5 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentAttached(r1, r4, r5);
        r0 = r18;
        r4 = r0.mRetaining;
        if (r4 != 0) goto L_0x0433;
    L_0x01dc:
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r0 = r18;
        r0.performCreate(r4);
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r5 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentCreated(r1, r4, r5);
    L_0x01f1:
        r4 = 0;
        r0 = r18;
        r0.mRetaining = r4;
        r0 = r18;
        r4 = r0.mFromLayout;
        if (r4 == 0) goto L_0x025d;
    L_0x01fc:
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r0 = r18;
        r4 = r0.getLayoutInflater(r4);
        r5 = 0;
        r0 = r18;
        r6 = r0.mSavedFragmentState;
        r0 = r18;
        r4 = r0.performCreateView(r4, r5, r6);
        r0 = r18;
        r0.mView = r4;
        r0 = r18;
        r4 = r0.mView;
        if (r4 == 0) goto L_0x0451;
    L_0x021b:
        r0 = r18;
        r4 = r0.mView;
        r0 = r18;
        r0.mInnerView = r4;
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 11;
        if (r4 < r5) goto L_0x0443;
    L_0x0229:
        r0 = r18;
        r4 = r0.mView;
        r5 = 0;
        android.support.v4.view.ViewCompat.setSaveFromParentEnabled(r4, r5);
    L_0x0231:
        r0 = r18;
        r4 = r0.mHidden;
        if (r4 == 0) goto L_0x0240;
    L_0x0237:
        r0 = r18;
        r4 = r0.mView;
        r5 = 8;
        r4.setVisibility(r5);
    L_0x0240:
        r0 = r18;
        r4 = r0.mView;
        r0 = r18;
        r5 = r0.mSavedFragmentState;
        r0 = r18;
        r0.onViewCreated(r4, r5);
        r0 = r18;
        r4 = r0.mView;
        r0 = r18;
        r5 = r0.mSavedFragmentState;
        r6 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentViewCreated(r1, r4, r5, r6);
    L_0x025d:
        r4 = 1;
        r0 = r19;
        if (r0 <= r4) goto L_0x03b6;
    L_0x0262:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x0280;
    L_0x0266:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "moveto ACTIVITY_CREATED: ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        android.util.Log.v(r4, r5);
    L_0x0280:
        r0 = r18;
        r4 = r0.mFromLayout;
        if (r4 != 0) goto L_0x038d;
    L_0x0286:
        r11 = 0;
        r0 = r18;
        r4 = r0.mContainerId;
        if (r4 == 0) goto L_0x0316;
    L_0x028d:
        r0 = r18;
        r4 = r0.mContainerId;
        r5 = -1;
        if (r4 != r5) goto L_0x02b9;
    L_0x0294:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Cannot create fragment ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r6 = " for a container view with no id";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.<init>(r5);
        r0 = r17;
        r0.throwException(r4);
    L_0x02b9:
        r0 = r17;
        r4 = r0.mContainer;
        r0 = r18;
        r5 = r0.mContainerId;
        r11 = r4.onFindViewById(r5);
        r11 = (android.view.ViewGroup) r11;
        if (r11 != 0) goto L_0x0316;
    L_0x02c9:
        r0 = r18;
        r4 = r0.mRestored;
        if (r4 != 0) goto L_0x0316;
    L_0x02cf:
        r4 = r18.getResources();	 Catch:{ NotFoundException -> 0x0458 }
        r0 = r18;
        r5 = r0.mContainerId;	 Catch:{ NotFoundException -> 0x0458 }
        r14 = r4.getResourceName(r5);	 Catch:{ NotFoundException -> 0x0458 }
    L_0x02db:
        r4 = new java.lang.IllegalArgumentException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "No view found for id 0x";
        r5 = r5.append(r6);
        r0 = r18;
        r6 = r0.mContainerId;
        r6 = java.lang.Integer.toHexString(r6);
        r5 = r5.append(r6);
        r6 = " (";
        r5 = r5.append(r6);
        r5 = r5.append(r14);
        r6 = ") for fragment ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        r4.<init>(r5);
        r0 = r17;
        r0.throwException(r4);
    L_0x0316:
        r0 = r18;
        r0.mContainer = r11;
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r0 = r18;
        r4 = r0.getLayoutInflater(r4);
        r0 = r18;
        r5 = r0.mSavedFragmentState;
        r0 = r18;
        r4 = r0.performCreateView(r4, r11, r5);
        r0 = r18;
        r0.mView = r4;
        r0 = r18;
        r4 = r0.mView;
        if (r4 == 0) goto L_0x046b;
    L_0x0338:
        r0 = r18;
        r4 = r0.mView;
        r0 = r18;
        r0.mInnerView = r4;
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 11;
        if (r4 < r5) goto L_0x045d;
    L_0x0346:
        r0 = r18;
        r4 = r0.mView;
        r5 = 0;
        android.support.v4.view.ViewCompat.setSaveFromParentEnabled(r4, r5);
    L_0x034e:
        if (r11 == 0) goto L_0x035c;
    L_0x0350:
        r0 = r18;
        r4 = r0.mView;
        r11.addView(r4);
        r4 = 1;
        r0 = r18;
        r0.mIsNewlyAdded = r4;
    L_0x035c:
        r0 = r18;
        r4 = r0.mHidden;
        if (r4 == 0) goto L_0x0370;
    L_0x0362:
        r0 = r18;
        r4 = r0.mView;
        r5 = 8;
        r4.setVisibility(r5);
        r4 = 0;
        r0 = r18;
        r0.mIsNewlyAdded = r4;
    L_0x0370:
        r0 = r18;
        r4 = r0.mView;
        r0 = r18;
        r5 = r0.mSavedFragmentState;
        r0 = r18;
        r0.onViewCreated(r4, r5);
        r0 = r18;
        r4 = r0.mView;
        r0 = r18;
        r5 = r0.mSavedFragmentState;
        r6 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentViewCreated(r1, r4, r5, r6);
    L_0x038d:
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r0 = r18;
        r0.performActivityCreated(r4);
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r5 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentActivityCreated(r1, r4, r5);
        r0 = r18;
        r4 = r0.mView;
        if (r4 == 0) goto L_0x03b1;
    L_0x03a8:
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r0 = r18;
        r0.restoreViewState(r4);
    L_0x03b1:
        r4 = 0;
        r0 = r18;
        r0.mSavedFragmentState = r4;
    L_0x03b6:
        r4 = 2;
        r0 = r19;
        if (r0 <= r4) goto L_0x03c0;
    L_0x03bb:
        r4 = 3;
        r0 = r18;
        r0.mState = r4;
    L_0x03c0:
        r4 = 3;
        r0 = r19;
        if (r0 <= r4) goto L_0x03ee;
    L_0x03c5:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x03e3;
    L_0x03c9:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "moveto STARTED: ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        android.util.Log.v(r4, r5);
    L_0x03e3:
        r18.performStart();
        r4 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentStarted(r1, r4);
    L_0x03ee:
        r4 = 4;
        r0 = r19;
        if (r0 <= r4) goto L_0x0071;
    L_0x03f3:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x0411;
    L_0x03f7:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "moveto RESUMED: ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        android.util.Log.v(r4, r5);
    L_0x0411:
        r18.performResume();
        r4 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentResumed(r1, r4);
        r4 = 0;
        r0 = r18;
        r0.mSavedFragmentState = r4;
        r4 = 0;
        r0 = r18;
        r0.mSavedViewState = r4;
        goto L_0x0071;
    L_0x0428:
        r0 = r18;
        r4 = r0.mParentFragment;
        r0 = r18;
        r4.onAttachFragment(r0);
        goto L_0x01c6;
    L_0x0433:
        r0 = r18;
        r4 = r0.mSavedFragmentState;
        r0 = r18;
        r0.restoreChildFragmentState(r4);
        r4 = 1;
        r0 = r18;
        r0.mState = r4;
        goto L_0x01f1;
    L_0x0443:
        r0 = r18;
        r4 = r0.mView;
        r4 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r4);
        r0 = r18;
        r0.mView = r4;
        goto L_0x0231;
    L_0x0451:
        r4 = 0;
        r0 = r18;
        r0.mInnerView = r4;
        goto L_0x025d;
    L_0x0458:
        r12 = move-exception;
        r14 = "unknown";
        goto L_0x02db;
    L_0x045d:
        r0 = r18;
        r4 = r0.mView;
        r4 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r4);
        r0 = r18;
        r0.mView = r4;
        goto L_0x034e;
    L_0x046b:
        r4 = 0;
        r0 = r18;
        r0.mInnerView = r4;
        goto L_0x038d;
    L_0x0472:
        r0 = r18;
        r4 = r0.mState;
        r0 = r19;
        if (r4 <= r0) goto L_0x0071;
    L_0x047a:
        r0 = r18;
        r4 = r0.mState;
        switch(r4) {
            case 1: goto L_0x0483;
            case 2: goto L_0x0530;
            case 3: goto L_0x050a;
            case 4: goto L_0x04dc;
            case 5: goto L_0x04ae;
            default: goto L_0x0481;
        };
    L_0x0481:
        goto L_0x0071;
    L_0x0483:
        r4 = 1;
        r0 = r19;
        if (r0 >= r4) goto L_0x0071;
    L_0x0488:
        r0 = r17;
        r4 = r0.mDestroyed;
        if (r4 == 0) goto L_0x04a1;
    L_0x048e:
        r4 = r18.getAnimatingAway();
        if (r4 == 0) goto L_0x04a1;
    L_0x0494:
        r15 = r18.getAnimatingAway();
        r4 = 0;
        r0 = r18;
        r0.setAnimatingAway(r4);
        r15.clearAnimation();
    L_0x04a1:
        r4 = r18.getAnimatingAway();
        if (r4 == 0) goto L_0x05ee;
    L_0x04a7:
        r18.setStateAfterAnimating(r19);
        r19 = 1;
        goto L_0x0071;
    L_0x04ae:
        r4 = 5;
        r0 = r19;
        if (r0 >= r4) goto L_0x04dc;
    L_0x04b3:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x04d1;
    L_0x04b7:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "movefrom RESUMED: ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        android.util.Log.v(r4, r5);
    L_0x04d1:
        r18.performPause();
        r4 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentPaused(r1, r4);
    L_0x04dc:
        r4 = 4;
        r0 = r19;
        if (r0 >= r4) goto L_0x050a;
    L_0x04e1:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x04ff;
    L_0x04e5:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "movefrom STARTED: ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        android.util.Log.v(r4, r5);
    L_0x04ff:
        r18.performStop();
        r4 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentStopped(r1, r4);
    L_0x050a:
        r4 = 3;
        r0 = r19;
        if (r0 >= r4) goto L_0x0530;
    L_0x050f:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x052d;
    L_0x0513:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "movefrom STOPPED: ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        android.util.Log.v(r4, r5);
    L_0x052d:
        r18.performReallyStop();
    L_0x0530:
        r4 = 2;
        r0 = r19;
        if (r0 >= r4) goto L_0x0483;
    L_0x0535:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x0553;
    L_0x0539:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "movefrom ACTIVITY_CREATED: ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        android.util.Log.v(r4, r5);
    L_0x0553:
        r0 = r18;
        r4 = r0.mView;
        if (r4 == 0) goto L_0x056e;
    L_0x0559:
        r0 = r17;
        r4 = r0.mHost;
        r0 = r18;
        r4 = r4.onShouldSaveFragmentState(r0);
        if (r4 == 0) goto L_0x056e;
    L_0x0565:
        r0 = r18;
        r4 = r0.mSavedViewState;
        if (r4 != 0) goto L_0x056e;
    L_0x056b:
        r17.saveFragmentViewState(r18);
    L_0x056e:
        r18.performDestroyView();
        r4 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentViewDestroyed(r1, r4);
        r0 = r18;
        r4 = r0.mView;
        if (r4 == 0) goto L_0x05dd;
    L_0x057f:
        r0 = r18;
        r4 = r0.mContainer;
        if (r4 == 0) goto L_0x05dd;
    L_0x0585:
        r10 = 0;
        r0 = r17;
        r4 = r0.mCurState;
        if (r4 <= 0) goto L_0x05a9;
    L_0x058c:
        r0 = r17;
        r4 = r0.mDestroyed;
        if (r4 != 0) goto L_0x05a9;
    L_0x0592:
        r0 = r18;
        r4 = r0.mView;
        r4 = r4.getVisibility();
        if (r4 != 0) goto L_0x05a9;
    L_0x059c:
        r4 = 0;
        r0 = r17;
        r1 = r18;
        r2 = r20;
        r3 = r21;
        r10 = r0.loadAnimation(r1, r2, r4, r3);
    L_0x05a9:
        if (r10 == 0) goto L_0x05d2;
    L_0x05ab:
        r13 = r18;
        r0 = r18;
        r4 = r0.mView;
        r0 = r18;
        r0.setAnimatingAway(r4);
        r18.setStateAfterAnimating(r19);
        r0 = r18;
        r0 = r0.mView;
        r16 = r0;
        r4 = new android.support.v4.app.FragmentManagerImpl$2;
        r0 = r17;
        r1 = r16;
        r4.<init>(r1, r10, r13);
        r10.setAnimationListener(r4);
        r0 = r18;
        r4 = r0.mView;
        r4.startAnimation(r10);
    L_0x05d2:
        r0 = r18;
        r4 = r0.mContainer;
        r0 = r18;
        r5 = r0.mView;
        r4.removeView(r5);
    L_0x05dd:
        r4 = 0;
        r0 = r18;
        r0.mContainer = r4;
        r4 = 0;
        r0 = r18;
        r0.mView = r4;
        r4 = 0;
        r0 = r18;
        r0.mInnerView = r4;
        goto L_0x0483;
    L_0x05ee:
        r4 = DEBUG;
        if (r4 == 0) goto L_0x060c;
    L_0x05f2:
        r4 = "FragmentManager";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "movefrom CREATED: ";
        r5 = r5.append(r6);
        r0 = r18;
        r5 = r5.append(r0);
        r5 = r5.toString();
        android.util.Log.v(r4, r5);
    L_0x060c:
        r0 = r18;
        r4 = r0.mRetaining;
        if (r4 != 0) goto L_0x0635;
    L_0x0612:
        r18.performDestroy();
        r4 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentDestroyed(r1, r4);
    L_0x061d:
        r18.performDetach();
        r4 = 0;
        r0 = r17;
        r1 = r18;
        r0.dispatchOnFragmentDetached(r1, r4);
        if (r22 != 0) goto L_0x0071;
    L_0x062a:
        r0 = r18;
        r4 = r0.mRetaining;
        if (r4 != 0) goto L_0x063b;
    L_0x0630:
        r17.makeInactive(r18);
        goto L_0x0071;
    L_0x0635:
        r4 = 0;
        r0 = r18;
        r0.mState = r4;
        goto L_0x061d;
    L_0x063b:
        r4 = 0;
        r0 = r18;
        r0.mHost = r4;
        r4 = 0;
        r0 = r18;
        r0.mParentFragment = r4;
        r4 = 0;
        r0 = r18;
        r0.mFragmentManager = r4;
        goto L_0x0071;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
    }

    void moveToState(Fragment f) {
        moveToState(f, this.mCurState, 0, 0, HONEYCOMB);
    }

    void completeShowHideFragment(Fragment fragment) {
        if (fragment.mView != null) {
            int visibility;
            Animation anim = loadAnimation(fragment, fragment.getNextTransition(), !fragment.mHidden ? true : HONEYCOMB, fragment.getNextTransitionStyle());
            if (anim != null) {
                setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                fragment.mView.startAnimation(anim);
                setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                anim.start();
            }
            if (!fragment.mHidden || fragment.isHideReplaced()) {
                visibility = 0;
            } else {
                visibility = 8;
            }
            fragment.mView.setVisibility(visibility);
            if (fragment.isHideReplaced()) {
                fragment.setHideReplaced(HONEYCOMB);
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = HONEYCOMB;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    void moveFragmentToExpectedState(Fragment f) {
        if (f != null) {
            int nextState = this.mCurState;
            if (f.mRemoving) {
                if (f.isInBackStack()) {
                    nextState = Math.min(nextState, ANIM_STYLE_OPEN_ENTER);
                } else {
                    nextState = Math.min(nextState, 0);
                }
            }
            moveToState(f, nextState, f.getNextTransition(), f.getNextTransitionStyle(), HONEYCOMB);
            if (f.mView != null) {
                Fragment underFragment = findFragmentUnder(f);
                if (underFragment != null) {
                    View underView = underFragment.mView;
                    ViewGroup container = f.mContainer;
                    int underIndex = container.indexOfChild(underView);
                    int viewIndex = container.indexOfChild(f.mView);
                    if (viewIndex < underIndex) {
                        container.removeViewAt(viewIndex);
                        container.addView(f.mView, underIndex);
                    }
                }
                if (f.mIsNewlyAdded && f.mContainer != null) {
                    f.mView.setVisibility(0);
                    f.mIsNewlyAdded = HONEYCOMB;
                    Animation anim = loadAnimation(f, f.getNextTransition(), true, f.getNextTransitionStyle());
                    if (anim != null) {
                        setHWLayerAnimListenerIfAlpha(f.mView, anim);
                        f.mView.startAnimation(anim);
                    }
                }
            }
            if (f.mHiddenChanged) {
                completeShowHideFragment(f);
            }
        }
    }

    void moveToState(int newState, boolean always) {
        if (this.mHost == null && newState != 0) {
            throw new IllegalStateException("No activity");
        } else if (always || newState != this.mCurState) {
            this.mCurState = newState;
            if (this.mActive != null) {
                int i;
                Fragment f;
                boolean loadersRunning = HONEYCOMB;
                if (this.mAdded != null) {
                    int numAdded = this.mAdded.size();
                    for (i = 0; i < numAdded; i += ANIM_STYLE_OPEN_ENTER) {
                        f = (Fragment) this.mAdded.get(i);
                        moveFragmentToExpectedState(f);
                        if (f.mLoaderManager != null) {
                            loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                        }
                    }
                }
                int numActive = this.mActive.size();
                for (i = 0; i < numActive; i += ANIM_STYLE_OPEN_ENTER) {
                    f = (Fragment) this.mActive.get(i);
                    if (f != null && ((f.mRemoving || f.mDetached) && !f.mIsNewlyAdded)) {
                        moveFragmentToExpectedState(f);
                        if (f.mLoaderManager != null) {
                            loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                        }
                    }
                }
                if (!loadersRunning) {
                    startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == ANIM_STYLE_FADE_ENTER) {
                    this.mHost.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = HONEYCOMB;
                }
            }
        }
    }

    void startPendingDeferredFragments() {
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (f != null) {
                    performPendingDeferredStart(f);
                }
            }
        }
    }

    void makeActive(Fragment f) {
        if (f.mIndex < 0) {
            if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
                if (this.mActive == null) {
                    this.mActive = new ArrayList();
                }
                f.setIndex(this.mActive.size(), this.mParent);
                this.mActive.add(f);
            } else {
                f.setIndex(((Integer) this.mAvailIndices.remove(this.mAvailIndices.size() - 1)).intValue(), this.mParent);
                this.mActive.set(f.mIndex, f);
            }
            if (DEBUG) {
                Log.v(TAG, "Allocated fragment index " + f);
            }
        }
    }

    void makeInactive(Fragment f) {
        if (f.mIndex >= 0) {
            if (DEBUG) {
                Log.v(TAG, "Freeing fragment index " + f);
            }
            this.mActive.set(f.mIndex, null);
            if (this.mAvailIndices == null) {
                this.mAvailIndices = new ArrayList();
            }
            this.mAvailIndices.add(Integer.valueOf(f.mIndex));
            this.mHost.inactivateFragment(f.mWho);
            f.initState();
        }
    }

    public void addFragment(Fragment fragment, boolean moveToStateNow) {
        if (this.mAdded == null) {
            this.mAdded = new ArrayList();
        }
        if (DEBUG) {
            Log.v(TAG, "add: " + fragment);
        }
        makeActive(fragment);
        if (!fragment.mDetached) {
            if (this.mAdded.contains(fragment)) {
                throw new IllegalStateException("Fragment already added: " + fragment);
            }
            this.mAdded.add(fragment);
            fragment.mAdded = true;
            fragment.mRemoving = HONEYCOMB;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = HONEYCOMB;
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (moveToStateNow) {
                moveToState(fragment);
            }
        }
    }

    public void removeFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean inactive;
        if (fragment.isInBackStack()) {
            inactive = HONEYCOMB;
        } else {
            inactive = true;
        }
        if (!fragment.mDetached || inactive) {
            if (this.mAdded != null) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = HONEYCOMB;
            fragment.mRemoving = true;
        }
    }

    public void hideFragment(Fragment fragment) {
        boolean z = true;
        if (DEBUG) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            if (fragment.mHiddenChanged) {
                z = HONEYCOMB;
            }
            fragment.mHiddenChanged = z;
        }
    }

    public void showFragment(Fragment fragment) {
        boolean z = HONEYCOMB;
        if (DEBUG) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = HONEYCOMB;
            if (!fragment.mHiddenChanged) {
                z = true;
            }
            fragment.mHiddenChanged = z;
        }
    }

    public void detachFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (this.mAdded != null) {
                    if (DEBUG) {
                        Log.v(TAG, "remove from detach: " + fragment);
                    }
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = HONEYCOMB;
            }
        }
    }

    public void attachFragment(Fragment fragment) {
        if (DEBUG) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = HONEYCOMB;
            if (!fragment.mAdded) {
                if (this.mAdded == null) {
                    this.mAdded = new ArrayList();
                }
                if (this.mAdded.contains(fragment)) {
                    throw new IllegalStateException("Fragment already added: " + fragment);
                }
                if (DEBUG) {
                    Log.v(TAG, "add from attach: " + fragment);
                }
                this.mAdded.add(fragment);
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
            }
        }
    }

    public Fragment findFragmentById(int id) {
        int i;
        Fragment f;
        if (this.mAdded != null) {
            for (i = this.mAdded.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && f.mFragmentId == id) {
                    return f;
                }
            }
        }
        if (this.mActive != null) {
            for (i = this.mActive.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mActive.get(i);
                if (f != null && f.mFragmentId == id) {
                    return f;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String tag) {
        int i;
        Fragment f;
        if (!(this.mAdded == null || tag == null)) {
            for (i = this.mAdded.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        if (!(this.mActive == null || tag == null)) {
            for (i = this.mActive.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mActive.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String who) {
        if (!(this.mActive == null || who == null)) {
            for (int i = this.mActive.size() - 1; i >= 0; i--) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (f != null) {
                    f = f.findFragmentByWho(who);
                    if (f != null) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        } else if (this.mNoTransactionsBecause != null) {
            throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
        }
    }

    public void enqueueAction(OpGenerator action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            checkStateLoss();
        }
        synchronized (this) {
            if (this.mDestroyed || this.mHost == null) {
                throw new IllegalStateException("Activity has been destroyed");
            }
            if (this.mPendingActions == null) {
                this.mPendingActions = new ArrayList();
            }
            this.mPendingActions.add(action);
            scheduleCommit();
        }
    }

    private void scheduleCommit() {
        boolean pendingReady = true;
        synchronized (this) {
            boolean postponeReady;
            if (this.mPostponedTransactions == null || this.mPostponedTransactions.isEmpty()) {
                postponeReady = HONEYCOMB;
            } else {
                postponeReady = true;
            }
            if (this.mPendingActions == null || this.mPendingActions.size() != ANIM_STYLE_OPEN_ENTER) {
                pendingReady = HONEYCOMB;
            }
            if (postponeReady || pendingReady) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
        }
    }

    public int allocBackStackIndex(BackStackRecord bse) {
        synchronized (this) {
            int index;
            if (this.mAvailBackStackIndices == null || this.mAvailBackStackIndices.size() <= 0) {
                if (this.mBackStackIndices == null) {
                    this.mBackStackIndices = new ArrayList();
                }
                index = this.mBackStackIndices.size();
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + index + " to " + bse);
                }
                this.mBackStackIndices.add(bse);
                return index;
            }
            index = ((Integer) this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1)).intValue();
            if (DEBUG) {
                Log.v(TAG, "Adding back stack index " + index + " with " + bse);
            }
            this.mBackStackIndices.set(index, bse);
            return index;
        }
    }

    public void setBackStackIndex(int index, BackStackRecord bse) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            int N = this.mBackStackIndices.size();
            if (index < N) {
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + index + " to " + bse);
                }
                this.mBackStackIndices.set(index, bse);
            } else {
                while (N < index) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList();
                    }
                    if (DEBUG) {
                        Log.v(TAG, "Adding available back stack index " + N);
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(N));
                    N += ANIM_STYLE_OPEN_ENTER;
                }
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + index + " with " + bse);
                }
                this.mBackStackIndices.add(bse);
            }
        }
    }

    public void freeBackStackIndex(int index) {
        synchronized (this) {
            this.mBackStackIndices.set(index, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList();
            }
            if (DEBUG) {
                Log.v(TAG, "Freeing back stack index " + index);
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(index));
        }
    }

    private void ensureExecReady(boolean allowStateLoss) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        } else {
            if (!allowStateLoss) {
                checkStateLoss();
            }
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new ArrayList();
                this.mTmpIsPop = new ArrayList();
            }
            executePostponedTransaction(null, null);
        }
    }

    public void execSingleAction(OpGenerator action, boolean allowStateLoss) {
        ensureExecReady(allowStateLoss);
        if (action.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
            } finally {
                cleanupExec();
            }
        }
        doPendingDeferredStart();
    }

    private void cleanupExec() {
        this.mExecutingActions = HONEYCOMB;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }

    private void executePostponedTransaction(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        int numPostponed = this.mPostponedTransactions == null ? 0 : this.mPostponedTransactions.size();
        int i = 0;
        while (i < numPostponed) {
            int index;
            StartEnterTransitionListener listener = (StartEnterTransitionListener) this.mPostponedTransactions.get(i);
            if (!(records == null || listener.mIsBack)) {
                index = records.indexOf(listener.mRecord);
                if (index != -1 && ((Boolean) isRecordPop.get(index)).booleanValue()) {
                    listener.cancelTransaction();
                    i += ANIM_STYLE_OPEN_ENTER;
                }
            }
            if (listener.isReady() || (records != null && listener.mRecord.interactsWith(records, 0, records.size()))) {
                this.mPostponedTransactions.remove(i);
                i--;
                numPostponed--;
                if (!(records == null || listener.mIsBack)) {
                    index = records.indexOf(listener.mRecord);
                    if (index != -1 && ((Boolean) isRecordPop.get(index)).booleanValue()) {
                        listener.cancelTransaction();
                    }
                }
                listener.completeTransaction();
            }
            i += ANIM_STYLE_OPEN_ENTER;
        }
    }

    private void optimizeAndExecuteOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop) {
        if (records != null && !records.isEmpty()) {
            if (isRecordPop == null || records.size() != isRecordPop.size()) {
                throw new IllegalStateException("Internal error with the back stack records");
            }
            executePostponedTransaction(records, isRecordPop);
            int numRecords = records.size();
            int startIndex = 0;
            int recordNum = 0;
            while (recordNum < numRecords) {
                if (!((BackStackRecord) records.get(recordNum)).mAllowOptimization) {
                    if (startIndex != recordNum) {
                        executeOpsTogether(records, isRecordPop, startIndex, recordNum);
                    }
                    int optimizeEnd = recordNum + ANIM_STYLE_OPEN_ENTER;
                    while (optimizeEnd < numRecords && !((BackStackRecord) records.get(optimizeEnd)).mAllowOptimization) {
                        optimizeEnd += ANIM_STYLE_OPEN_ENTER;
                    }
                    executeOpsTogether(records, isRecordPop, recordNum, optimizeEnd);
                    startIndex = optimizeEnd;
                    recordNum = optimizeEnd - 1;
                }
                recordNum += ANIM_STYLE_OPEN_ENTER;
            }
            if (startIndex != numRecords) {
                executeOpsTogether(records, isRecordPop, startIndex, numRecords);
            }
        }
    }

    private void executeOpsTogether(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        int recordNum;
        boolean allowOptimization = ((BackStackRecord) records.get(startIndex)).mAllowOptimization;
        boolean addToBackStack = HONEYCOMB;
        if (this.mTmpAddedFragments == null) {
            this.mTmpAddedFragments = new ArrayList();
        } else {
            this.mTmpAddedFragments.clear();
        }
        if (this.mAdded != null) {
            this.mTmpAddedFragments.addAll(this.mAdded);
        }
        for (recordNum = startIndex; recordNum < endIndex; recordNum += ANIM_STYLE_OPEN_ENTER) {
            BackStackRecord record = (BackStackRecord) records.get(recordNum);
            boolean isPop = ((Boolean) isRecordPop.get(recordNum)).booleanValue();
            if (!isPop) {
                record.expandReplaceOps(this.mTmpAddedFragments);
            }
            record.bumpBackStackNesting(isPop ? -1 : ANIM_STYLE_OPEN_ENTER);
            if (addToBackStack || record.mAddToBackStack) {
                addToBackStack = true;
            } else {
                addToBackStack = HONEYCOMB;
            }
        }
        this.mTmpAddedFragments.clear();
        if (!allowOptimization) {
            FragmentTransition.startTransitions(this, records, isRecordPop, startIndex, endIndex, HONEYCOMB);
        }
        executeOps(records, isRecordPop, startIndex, endIndex);
        int postponeIndex = endIndex;
        if (allowOptimization) {
            moveFragmentsToInvisible();
            postponeIndex = postponePostponableTransactions(records, isRecordPop, startIndex, endIndex);
        }
        if (postponeIndex != startIndex && allowOptimization) {
            FragmentTransition.startTransitions(this, records, isRecordPop, startIndex, postponeIndex, true);
            moveToState(this.mCurState, true);
        }
        for (recordNum = startIndex; recordNum < endIndex; recordNum += ANIM_STYLE_OPEN_ENTER) {
            record = (BackStackRecord) records.get(recordNum);
            if (((Boolean) isRecordPop.get(recordNum)).booleanValue() && record.mIndex >= 0) {
                freeBackStackIndex(record.mIndex);
                record.mIndex = -1;
            }
        }
        if (addToBackStack) {
            reportBackStackChanged();
        }
    }

    private int postponePostponableTransactions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        int postponeIndex = endIndex;
        for (int i = endIndex - 1; i >= startIndex; i--) {
            BackStackRecord record = (BackStackRecord) records.get(i);
            boolean isPop = ((Boolean) isRecordPop.get(i)).booleanValue();
            boolean isPostponed = (!record.isPostponed() || record.interactsWith(records, i + ANIM_STYLE_OPEN_ENTER, endIndex)) ? HONEYCOMB : true;
            if (isPostponed) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList();
                }
                StartEnterTransitionListener listener = new StartEnterTransitionListener(record, isPop);
                this.mPostponedTransactions.add(listener);
                record.setOnStartPostponedListener(listener);
                if (isPop) {
                    record.executeOps();
                } else {
                    record.executePopOps();
                }
                postponeIndex--;
                if (i != postponeIndex) {
                    records.remove(i);
                    records.add(postponeIndex, record);
                }
                moveFragmentsToInvisible();
            }
        }
        return postponeIndex;
    }

    private void completeExecute(BackStackRecord record, boolean isPop, boolean runTransitions, boolean moveToState) {
        ArrayList<BackStackRecord> records = new ArrayList(ANIM_STYLE_OPEN_ENTER);
        ArrayList<Boolean> isRecordPop = new ArrayList(ANIM_STYLE_OPEN_ENTER);
        records.add(record);
        isRecordPop.add(Boolean.valueOf(isPop));
        executeOps(records, isRecordPop, 0, ANIM_STYLE_OPEN_ENTER);
        if (runTransitions) {
            FragmentTransition.startTransitions(this, records, isRecordPop, 0, ANIM_STYLE_OPEN_ENTER, true);
        }
        if (moveToState) {
            moveToState(this.mCurState, true);
        } else if (this.mActive != null) {
            int numActive = this.mActive.size();
            for (int i = 0; i < numActive; i += ANIM_STYLE_OPEN_ENTER) {
                Fragment fragment = (Fragment) this.mActive.get(i);
                if (fragment.mView != null && fragment.mIsNewlyAdded && record.interactsWith(fragment.mContainerId)) {
                    fragment.mIsNewlyAdded = HONEYCOMB;
                }
            }
        }
    }

    private Fragment findFragmentUnder(Fragment f) {
        ViewGroup container = f.mContainer;
        View view = f.mView;
        if (container == null || view == null) {
            return null;
        }
        for (int i = this.mAdded.indexOf(f) - 1; i >= 0; i--) {
            Fragment underFragment = (Fragment) this.mAdded.get(i);
            if (underFragment.mContainer == container && underFragment.mView != null) {
                return underFragment;
            }
        }
        return null;
    }

    private static void executeOps(ArrayList<BackStackRecord> records, ArrayList<Boolean> isRecordPop, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i += ANIM_STYLE_OPEN_ENTER) {
            BackStackRecord record = (BackStackRecord) records.get(i);
            if (((Boolean) isRecordPop.get(i)).booleanValue()) {
                record.executePopOps();
            } else {
                record.executeOps();
            }
        }
    }

    private void moveFragmentsToInvisible() {
        if (this.mCurState >= ANIM_STYLE_OPEN_ENTER) {
            int state = Math.min(this.mCurState, ANIM_STYLE_CLOSE_EXIT);
            int numAdded = this.mAdded == null ? 0 : this.mAdded.size();
            for (int i = 0; i < numAdded; i += ANIM_STYLE_OPEN_ENTER) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment.mState < state) {
                    moveToState(fragment, state, fragment.getNextAnim(), fragment.getNextTransition(), HONEYCOMB);
                    if (!(fragment.mView == null || fragment.mHidden || !fragment.mIsNewlyAdded)) {
                        fragment.mView.setVisibility(ANIM_STYLE_CLOSE_EXIT);
                    }
                }
            }
        }
    }

    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                ((StartEnterTransitionListener) this.mPostponedTransactions.remove(0)).completeTransaction();
            }
        }
    }

    private void endAnimatingAwayFragments() {
        int numFragments;
        if (this.mActive == null) {
            numFragments = 0;
        } else {
            numFragments = this.mActive.size();
        }
        for (int i = 0; i < numFragments; i += ANIM_STYLE_OPEN_ENTER) {
            Fragment fragment = (Fragment) this.mActive.get(i);
            if (!(fragment == null || fragment.getAnimatingAway() == null)) {
                int stateAfterAnimating = fragment.getStateAfterAnimating();
                View animatingAway = fragment.getAnimatingAway();
                fragment.setAnimatingAway(null);
                animatingAway.clearAnimation();
                moveToState(fragment, stateAfterAnimating, 0, 0, HONEYCOMB);
            }
        }
    }

    private boolean generateOpsForPendingActions(ArrayList<BackStackRecord> records, ArrayList<Boolean> isPop) {
        synchronized (this) {
            if (this.mPendingActions == null || this.mPendingActions.size() == 0) {
                return HONEYCOMB;
            }
            int numActions = this.mPendingActions.size();
            for (int i = 0; i < numActions; i += ANIM_STYLE_OPEN_ENTER) {
                ((OpGenerator) this.mPendingActions.get(i)).generateOps(records, isPop);
            }
            this.mPendingActions.clear();
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
            if (numActions > 0) {
                return true;
            }
            return HONEYCOMB;
        }
    }

    void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            boolean loadersRunning = HONEYCOMB;
            for (int i = 0; i < this.mActive.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (!(f == null || f.mLoaderManager == null)) {
                    loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                }
            }
            if (!loadersRunning) {
                this.mHavePendingDeferredStart = HONEYCOMB;
                startPendingDeferredFragments();
            }
        }
    }

    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i += ANIM_STYLE_OPEN_ENTER) {
                ((OnBackStackChangedListener) this.mBackStackChangeListeners.get(i)).onBackStackChanged();
            }
        }
    }

    void addBackStackState(BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(state);
        reportBackStackChanged();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean popBackStackState(java.util.ArrayList<android.support.v4.app.BackStackRecord> r8, java.util.ArrayList<java.lang.Boolean> r9, java.lang.String r10, int r11, int r12) {
        /*
        r7 = this;
        r5 = 1;
        r4 = 0;
        r6 = r7.mBackStack;
        if (r6 != 0) goto L_0x0007;
    L_0x0006:
        return r4;
    L_0x0007:
        if (r10 != 0) goto L_0x002b;
    L_0x0009:
        if (r11 >= 0) goto L_0x002b;
    L_0x000b:
        r6 = r12 & 1;
        if (r6 != 0) goto L_0x002b;
    L_0x000f:
        r6 = r7.mBackStack;
        r6 = r6.size();
        r3 = r6 + -1;
        if (r3 < 0) goto L_0x0006;
    L_0x0019:
        r4 = r7.mBackStack;
        r4 = r4.remove(r3);
        r8.add(r4);
        r4 = java.lang.Boolean.valueOf(r5);
        r9.add(r4);
    L_0x0029:
        r4 = r5;
        goto L_0x0006;
    L_0x002b:
        r2 = -1;
        if (r10 != 0) goto L_0x0030;
    L_0x002e:
        if (r11 < 0) goto L_0x007e;
    L_0x0030:
        r6 = r7.mBackStack;
        r6 = r6.size();
        r2 = r6 + -1;
    L_0x0038:
        if (r2 < 0) goto L_0x004e;
    L_0x003a:
        r6 = r7.mBackStack;
        r0 = r6.get(r2);
        r0 = (android.support.v4.app.BackStackRecord) r0;
        if (r10 == 0) goto L_0x0075;
    L_0x0044:
        r6 = r0.getName();
        r6 = r10.equals(r6);
        if (r6 == 0) goto L_0x0075;
    L_0x004e:
        if (r2 < 0) goto L_0x0006;
    L_0x0050:
        r6 = r12 & 1;
        if (r6 == 0) goto L_0x007e;
    L_0x0054:
        r2 = r2 + -1;
    L_0x0056:
        if (r2 < 0) goto L_0x007e;
    L_0x0058:
        r6 = r7.mBackStack;
        r0 = r6.get(r2);
        r0 = (android.support.v4.app.BackStackRecord) r0;
        if (r10 == 0) goto L_0x006c;
    L_0x0062:
        r6 = r0.getName();
        r6 = r10.equals(r6);
        if (r6 != 0) goto L_0x0072;
    L_0x006c:
        if (r11 < 0) goto L_0x007e;
    L_0x006e:
        r6 = r0.mIndex;
        if (r11 != r6) goto L_0x007e;
    L_0x0072:
        r2 = r2 + -1;
        goto L_0x0056;
    L_0x0075:
        if (r11 < 0) goto L_0x007b;
    L_0x0077:
        r6 = r0.mIndex;
        if (r11 == r6) goto L_0x004e;
    L_0x007b:
        r2 = r2 + -1;
        goto L_0x0038;
    L_0x007e:
        r6 = r7.mBackStack;
        r6 = r6.size();
        r6 = r6 + -1;
        if (r2 == r6) goto L_0x0006;
    L_0x0088:
        r4 = r7.mBackStack;
        r4 = r4.size();
        r1 = r4 + -1;
    L_0x0090:
        if (r1 <= r2) goto L_0x0029;
    L_0x0092:
        r4 = r7.mBackStack;
        r4 = r4.remove(r1);
        r8.add(r4);
        r4 = java.lang.Boolean.valueOf(r5);
        r9.add(r4);
        r1 = r1 + -1;
        goto L_0x0090;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.popBackStackState(java.util.ArrayList, java.util.ArrayList, java.lang.String, int, int):boolean");
    }

    FragmentManagerNonConfig retainNonConfig() {
        ArrayList<Fragment> fragments = null;
        ArrayList<FragmentManagerNonConfig> childFragments = null;
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (f != null) {
                    if (f.mRetainInstance) {
                        if (fragments == null) {
                            fragments = new ArrayList();
                        }
                        fragments.add(f);
                        f.mRetaining = true;
                        f.mTargetIndex = f.mTarget != null ? f.mTarget.mIndex : -1;
                        if (DEBUG) {
                            Log.v(TAG, "retainNonConfig: keeping retained " + f);
                        }
                    }
                    boolean addedChild = HONEYCOMB;
                    if (f.mChildFragmentManager != null) {
                        FragmentManagerNonConfig child = f.mChildFragmentManager.retainNonConfig();
                        if (child != null) {
                            if (childFragments == null) {
                                childFragments = new ArrayList();
                                for (int j = 0; j < i; j += ANIM_STYLE_OPEN_ENTER) {
                                    childFragments.add(null);
                                }
                            }
                            childFragments.add(child);
                            addedChild = true;
                        }
                    }
                    if (!(childFragments == null || addedChild)) {
                        childFragments.add(null);
                    }
                }
            }
        }
        if (fragments == null && childFragments == null) {
            return null;
        }
        return new FragmentManagerNonConfig(fragments, childFragments);
    }

    void saveFragmentViewState(Fragment f) {
        if (f.mInnerView != null) {
            if (this.mStateArray == null) {
                this.mStateArray = new SparseArray();
            } else {
                this.mStateArray.clear();
            }
            f.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                f.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    Bundle saveFragmentBasicState(Fragment f) {
        Bundle result = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        f.performSaveInstanceState(this.mStateBundle);
        dispatchOnFragmentSaveInstanceState(f, this.mStateBundle, HONEYCOMB);
        if (!this.mStateBundle.isEmpty()) {
            result = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (f.mView != null) {
            saveFragmentViewState(f);
        }
        if (f.mSavedViewState != null) {
            if (result == null) {
                result = new Bundle();
            }
            result.putSparseParcelableArray(VIEW_STATE_TAG, f.mSavedViewState);
        }
        if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new Bundle();
            }
            result.putBoolean(USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
        }
        return result;
    }

    Parcelable saveAllState() {
        forcePostponedTransactions();
        endAnimatingAwayFragments();
        execPendingActions();
        if (HONEYCOMB) {
            this.mStateSaved = true;
        }
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        int i;
        int N = this.mActive.size();
        FragmentState[] active = new FragmentState[N];
        boolean haveFragments = HONEYCOMB;
        for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
            Fragment f = (Fragment) this.mActive.get(i);
            if (f != null) {
                if (f.mIndex < 0) {
                    throwException(new IllegalStateException("Failure saving state: active " + f + " has cleared index: " + f.mIndex));
                }
                haveFragments = true;
                FragmentState fs = new FragmentState(f);
                active[i] = fs;
                if (f.mState <= 0 || fs.mSavedFragmentState != null) {
                    fs.mSavedFragmentState = f.mSavedFragmentState;
                } else {
                    fs.mSavedFragmentState = saveFragmentBasicState(f);
                    if (f.mTarget != null) {
                        if (f.mTarget.mIndex < 0) {
                            throwException(new IllegalStateException("Failure saving state: " + f + " has target not in fragment manager: " + f.mTarget));
                        }
                        if (fs.mSavedFragmentState == null) {
                            fs.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fs.mSavedFragmentState, TARGET_STATE_TAG, f.mTarget);
                        if (f.mTargetRequestCode != 0) {
                            fs.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    Log.v(TAG, "Saved state of " + f + ": " + fs.mSavedFragmentState);
                }
            }
        }
        if (haveFragments) {
            int[] added = null;
            BackStackState[] backStack = null;
            if (this.mAdded != null) {
                N = this.mAdded.size();
                if (N > 0) {
                    added = new int[N];
                    for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                        added[i] = ((Fragment) this.mAdded.get(i)).mIndex;
                        if (added[i] < 0) {
                            throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(i) + " has cleared index: " + added[i]));
                        }
                        if (DEBUG) {
                            Log.v(TAG, "saveAllState: adding fragment #" + i + ": " + this.mAdded.get(i));
                        }
                    }
                }
            }
            if (this.mBackStack != null) {
                N = this.mBackStack.size();
                if (N > 0) {
                    backStack = new BackStackState[N];
                    for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                        backStack[i] = new BackStackState((BackStackRecord) this.mBackStack.get(i));
                        if (DEBUG) {
                            Log.v(TAG, "saveAllState: adding back stack #" + i + ": " + this.mBackStack.get(i));
                        }
                    }
                }
            }
            Parcelable fms = new FragmentManagerState();
            fms.mActive = active;
            fms.mAdded = added;
            fms.mBackStack = backStack;
            return fms;
        } else if (!DEBUG) {
            return null;
        } else {
            Log.v(TAG, "saveAllState: no fragments!");
            return null;
        }
    }

    void restoreAllState(Parcelable state, FragmentManagerNonConfig nonConfig) {
        if (state != null) {
            FragmentManagerState fms = (FragmentManagerState) state;
            if (fms.mActive != null) {
                List<Fragment> nonConfigFragments;
                int count;
                int i;
                Fragment f;
                FragmentState fs;
                List<FragmentManagerNonConfig> childNonConfigs = null;
                if (nonConfig != null) {
                    nonConfigFragments = nonConfig.getFragments();
                    childNonConfigs = nonConfig.getChildNonConfigs();
                    count = nonConfigFragments != null ? nonConfigFragments.size() : 0;
                    for (i = 0; i < count; i += ANIM_STYLE_OPEN_ENTER) {
                        f = (Fragment) nonConfigFragments.get(i);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: re-attaching retained " + f);
                        }
                        fs = fms.mActive[f.mIndex];
                        fs.mInstance = f;
                        f.mSavedViewState = null;
                        f.mBackStackNesting = 0;
                        f.mInLayout = HONEYCOMB;
                        f.mAdded = HONEYCOMB;
                        f.mTarget = null;
                        if (fs.mSavedFragmentState != null) {
                            fs.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                            f.mSavedFragmentState = fs.mSavedFragmentState;
                        }
                    }
                }
                this.mActive = new ArrayList(fms.mActive.length);
                if (this.mAvailIndices != null) {
                    this.mAvailIndices.clear();
                }
                i = 0;
                while (i < fms.mActive.length) {
                    fs = fms.mActive[i];
                    if (fs != null) {
                        FragmentManagerNonConfig childNonConfig = null;
                        if (childNonConfigs != null && i < childNonConfigs.size()) {
                            childNonConfig = (FragmentManagerNonConfig) childNonConfigs.get(i);
                        }
                        f = fs.instantiate(this.mHost, this.mParent, childNonConfig);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: active #" + i + ": " + f);
                        }
                        this.mActive.add(f);
                        fs.mInstance = null;
                    } else {
                        this.mActive.add(null);
                        if (this.mAvailIndices == null) {
                            this.mAvailIndices = new ArrayList();
                        }
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: avail #" + i);
                        }
                        this.mAvailIndices.add(Integer.valueOf(i));
                    }
                    i += ANIM_STYLE_OPEN_ENTER;
                }
                if (nonConfig != null) {
                    nonConfigFragments = nonConfig.getFragments();
                    count = nonConfigFragments != null ? nonConfigFragments.size() : 0;
                    for (i = 0; i < count; i += ANIM_STYLE_OPEN_ENTER) {
                        f = (Fragment) nonConfigFragments.get(i);
                        if (f.mTargetIndex >= 0) {
                            if (f.mTargetIndex < this.mActive.size()) {
                                f.mTarget = (Fragment) this.mActive.get(f.mTargetIndex);
                            } else {
                                Log.w(TAG, "Re-attaching retained fragment " + f + " target no longer exists: " + f.mTargetIndex);
                                f.mTarget = null;
                            }
                        }
                    }
                }
                if (fms.mAdded != null) {
                    this.mAdded = new ArrayList(fms.mAdded.length);
                    for (i = 0; i < fms.mAdded.length; i += ANIM_STYLE_OPEN_ENTER) {
                        f = (Fragment) this.mActive.get(fms.mAdded[i]);
                        if (f == null) {
                            throwException(new IllegalStateException("No instantiated fragment for index #" + fms.mAdded[i]));
                        }
                        f.mAdded = true;
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: added #" + i + ": " + f);
                        }
                        if (this.mAdded.contains(f)) {
                            throw new IllegalStateException("Already added!");
                        }
                        this.mAdded.add(f);
                    }
                } else {
                    this.mAdded = null;
                }
                if (fms.mBackStack != null) {
                    this.mBackStack = new ArrayList(fms.mBackStack.length);
                    for (i = 0; i < fms.mBackStack.length; i += ANIM_STYLE_OPEN_ENTER) {
                        BackStackRecord bse = fms.mBackStack[i].instantiate(this);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: back stack #" + i + " (index " + bse.mIndex + "): " + bse);
                            bse.dump("  ", new PrintWriter(new LogWriter(TAG)), HONEYCOMB);
                        }
                        this.mBackStack.add(bse);
                        if (bse.mIndex >= 0) {
                            setBackStackIndex(bse.mIndex, bse);
                        }
                    }
                    return;
                }
                this.mBackStack = null;
            }
        }
    }

    public void attachController(FragmentHostCallback host, FragmentContainer container, Fragment parent) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mHost = host;
        this.mContainer = container;
        this.mParent = parent;
    }

    public void noteStateNotSaved() {
        this.mStateSaved = HONEYCOMB;
    }

    public void dispatchCreate() {
        this.mStateSaved = HONEYCOMB;
        moveToState(ANIM_STYLE_OPEN_ENTER, HONEYCOMB);
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = HONEYCOMB;
        moveToState(ANIM_STYLE_OPEN_EXIT, HONEYCOMB);
    }

    public void dispatchStart() {
        this.mStateSaved = HONEYCOMB;
        moveToState(ANIM_STYLE_CLOSE_EXIT, HONEYCOMB);
    }

    public void dispatchResume() {
        this.mStateSaved = HONEYCOMB;
        moveToState(ANIM_STYLE_FADE_ENTER, HONEYCOMB);
    }

    public void dispatchPause() {
        moveToState(ANIM_STYLE_CLOSE_EXIT, HONEYCOMB);
    }

    public void dispatchStop() {
        this.mStateSaved = true;
        moveToState(ANIM_STYLE_CLOSE_ENTER, HONEYCOMB);
    }

    public void dispatchReallyStop() {
        moveToState(ANIM_STYLE_OPEN_EXIT, HONEYCOMB);
    }

    public void dispatchDestroyView() {
        moveToState(ANIM_STYLE_OPEN_ENTER, HONEYCOMB);
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        moveToState(0, HONEYCOMB);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }

    public void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode) {
        if (this.mAdded != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; i--) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performMultiWindowModeChanged(isInMultiWindowMode);
                }
            }
        }
    }

    public void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        if (this.mAdded != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; i--) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performPictureInPictureModeChanged(isInPictureInPictureMode);
                }
            }
        }
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performConfigurationChanged(newConfig);
                }
            }
        }
    }

    public void dispatchLowMemory() {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performLowMemory();
                }
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int i;
        Fragment f;
        boolean show = HONEYCOMB;
        ArrayList<Fragment> newMenus = null;
        if (this.mAdded != null) {
            for (i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && f.performCreateOptionsMenu(menu, inflater)) {
                    show = true;
                    if (newMenus == null) {
                        newMenus = new ArrayList();
                    }
                    newMenus.add(f);
                }
            }
        }
        if (this.mCreatedMenus != null) {
            for (i = 0; i < this.mCreatedMenus.size(); i += ANIM_STYLE_OPEN_ENTER) {
                f = (Fragment) this.mCreatedMenus.get(i);
                if (newMenus == null || !newMenus.contains(f)) {
                    f.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = newMenus;
        return show;
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        boolean show = HONEYCOMB;
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && f.performPrepareOptionsMenu(menu)) {
                    show = true;
                }
            }
        }
        return show;
    }

    public boolean dispatchOptionsItemSelected(MenuItem item) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && f.performOptionsItemSelected(item)) {
                    return true;
                }
            }
        }
        return HONEYCOMB;
    }

    public boolean dispatchContextItemSelected(MenuItem item) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && f.performContextItemSelected(item)) {
                    return true;
                }
            }
        }
        return HONEYCOMB;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {
        if (this.mLifecycleCallbacks == null) {
            this.mLifecycleCallbacks = new CopyOnWriteArrayList();
        }
        this.mLifecycleCallbacks.add(new Pair(cb, Boolean.valueOf(recursive)));
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {
        if (this.mLifecycleCallbacks != null) {
            synchronized (this.mLifecycleCallbacks) {
                int N = this.mLifecycleCallbacks.size();
                for (int i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    if (((Pair) this.mLifecycleCallbacks.get(i)).first == cb) {
                        this.mLifecycleCallbacks.remove(i);
                        break;
                    }
                }
            }
        }
    }

    void dispatchOnFragmentPreAttached(Fragment f, Context context, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPreAttached(f, context, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentPreAttached(this, f, context);
                }
            }
        }
    }

    void dispatchOnFragmentAttached(Fragment f, Context context, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentAttached(f, context, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentAttached(this, f, context);
                }
            }
        }
    }

    void dispatchOnFragmentCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentCreated(f, savedInstanceState, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentCreated(this, f, savedInstanceState);
                }
            }
        }
    }

    void dispatchOnFragmentActivityCreated(Fragment f, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentActivityCreated(f, savedInstanceState, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentActivityCreated(this, f, savedInstanceState);
                }
            }
        }
    }

    void dispatchOnFragmentViewCreated(Fragment f, View v, Bundle savedInstanceState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewCreated(f, v, savedInstanceState, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentViewCreated(this, f, v, savedInstanceState);
                }
            }
        }
    }

    void dispatchOnFragmentStarted(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStarted(f, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentStarted(this, f);
                }
            }
        }
    }

    void dispatchOnFragmentResumed(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentResumed(f, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentResumed(this, f);
                }
            }
        }
    }

    void dispatchOnFragmentPaused(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentPaused(f, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentPaused(this, f);
                }
            }
        }
    }

    void dispatchOnFragmentStopped(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentStopped(f, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentStopped(this, f);
                }
            }
        }
    }

    void dispatchOnFragmentSaveInstanceState(Fragment f, Bundle outState, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentSaveInstanceState(f, outState, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentSaveInstanceState(this, f, outState);
                }
            }
        }
    }

    void dispatchOnFragmentViewDestroyed(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentViewDestroyed(f, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentViewDestroyed(this, f);
                }
            }
        }
    }

    void dispatchOnFragmentDestroyed(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDestroyed(f, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentDestroyed(this, f);
                }
            }
        }
    }

    void dispatchOnFragmentDetached(Fragment f, boolean onlyRecursive) {
        if (this.mParent != null) {
            FragmentManager parentManager = this.mParent.getFragmentManager();
            if (parentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl) parentManager).dispatchOnFragmentDetached(f, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            Iterator it = this.mLifecycleCallbacks.iterator();
            while (it.hasNext()) {
                Pair<FragmentLifecycleCallbacks, Boolean> p = (Pair) it.next();
                if (!onlyRecursive || ((Boolean) p.second).booleanValue()) {
                    ((FragmentLifecycleCallbacks) p.first).onFragmentDetached(this, f);
                }
            }
        }
    }

    public static int reverseTransit(int transit) {
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /*4097*/:
                return InputDeviceCompat.SOURCE_MOUSE;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /*4099*/:
                return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
            case InputDeviceCompat.SOURCE_MOUSE /*8194*/:
                return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
            default:
                return 0;
        }
    }

    public static int transitToStyleIndex(int transit, boolean enter) {
        int animAttr = -1;
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /*4097*/:
                animAttr = enter ? ANIM_STYLE_OPEN_ENTER : ANIM_STYLE_OPEN_EXIT;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /*4099*/:
                animAttr = enter ? ANIM_STYLE_FADE_ENTER : ANIM_STYLE_FADE_EXIT;
                break;
            case InputDeviceCompat.SOURCE_MOUSE /*8194*/:
                animAttr = enter ? ANIM_STYLE_CLOSE_ENTER : ANIM_STYLE_CLOSE_EXIT;
                break;
        }
        return animAttr;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (!"fragment".equals(name)) {
            return null;
        }
        String fname = attrs.getAttributeValue(null, "class");
        TypedArray a = context.obtainStyledAttributes(attrs, FragmentTag.Fragment);
        if (fname == null) {
            fname = a.getString(0);
        }
        int id = a.getResourceId(ANIM_STYLE_OPEN_ENTER, -1);
        String tag = a.getString(ANIM_STYLE_OPEN_EXIT);
        a.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), fname)) {
            return null;
        }
        int containerId;
        if (parent != null) {
            containerId = parent.getId();
        } else {
            containerId = 0;
        }
        if (containerId == -1 && id == -1 && tag == null) {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + fname);
        }
        Fragment fragment;
        if (id != -1) {
            fragment = findFragmentById(id);
        } else {
            fragment = null;
        }
        if (fragment == null && tag != null) {
            fragment = findFragmentByTag(tag);
        }
        if (fragment == null && containerId != -1) {
            fragment = findFragmentById(containerId);
        }
        if (DEBUG) {
            Log.v(TAG, "onCreateView: id=0x" + Integer.toHexString(id) + " fname=" + fname + " existing=" + fragment);
        }
        if (fragment == null) {
            int i;
            fragment = Fragment.instantiate(context, fname);
            fragment.mFromLayout = true;
            if (id != 0) {
                i = id;
            } else {
                i = containerId;
            }
            fragment.mFragmentId = i;
            fragment.mContainerId = containerId;
            fragment.mTag = tag;
            fragment.mInLayout = true;
            fragment.mFragmentManager = this;
            fragment.mHost = this.mHost;
            fragment.onInflate(this.mHost.getContext(), attrs, fragment.mSavedFragmentState);
            addFragment(fragment, true);
        } else if (fragment.mInLayout) {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(id) + ", tag " + tag + ", or parent id 0x" + Integer.toHexString(containerId) + " with another fragment for " + fname);
        } else {
            fragment.mInLayout = true;
            fragment.mHost = this.mHost;
            if (!fragment.mRetaining) {
                fragment.onInflate(this.mHost.getContext(), attrs, fragment.mSavedFragmentState);
            }
        }
        if (this.mCurState >= ANIM_STYLE_OPEN_ENTER || !fragment.mFromLayout) {
            moveToState(fragment);
        } else {
            moveToState(fragment, ANIM_STYLE_OPEN_ENTER, 0, 0, HONEYCOMB);
        }
        if (fragment.mView == null) {
            throw new IllegalStateException("Fragment " + fname + " did not create a view.");
        }
        if (id != 0) {
            fragment.mView.setId(id);
        }
        if (fragment.mView.getTag() == null) {
            fragment.mView.setTag(tag);
        }
        return fragment.mView;
    }

    LayoutInflaterFactory getLayoutInflaterFactory() {
        return this;
    }
}
