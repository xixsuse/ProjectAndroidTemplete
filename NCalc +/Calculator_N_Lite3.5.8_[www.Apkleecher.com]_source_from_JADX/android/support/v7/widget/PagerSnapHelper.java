package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.SmoothScroller.Action;
import android.support.v7.widget.RecyclerView.SmoothScroller.ScrollVectorProvider;
import android.support.v7.widget.RecyclerView.State;
import android.util.DisplayMetrics;
import android.view.View;
import org.apache.commons.math4.analysis.integration.BaseAbstractUnivariateIntegrator;

public class PagerSnapHelper extends SnapHelper {
    private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
    @Nullable
    private OrientationHelper mHorizontalHelper;
    @Nullable
    private OrientationHelper mVerticalHelper;

    /* renamed from: android.support.v7.widget.PagerSnapHelper.1 */
    class C03021 extends LinearSmoothScroller {
        C03021(Context context) {
            super(context);
        }

        protected void onTargetFound(View targetView, State state, Action action) {
            int[] snapDistances = PagerSnapHelper.this.calculateDistanceToFinalSnap(PagerSnapHelper.this.mRecyclerView.getLayoutManager(), targetView);
            int dx = snapDistances[0];
            int dy = snapDistances[1];
            int time = calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
            if (time > 0) {
                action.update(dx, dy, time, this.mDecelerateInterpolator);
            }
        }

        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return 100.0f / ((float) displayMetrics.densityDpi);
        }

        protected int calculateTimeForScrolling(int dx) {
            return Math.min(PagerSnapHelper.MAX_SCROLL_ON_FLING_DURATION, super.calculateTimeForScrolling(dx));
        }
    }

    @Nullable
    public int[] calculateDistanceToFinalSnap(@NonNull LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToCenter(layoutManager, targetView, getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }
        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(layoutManager, targetView, getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        return out;
    }

    @Nullable
    public View findSnapView(LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    public int findTargetSnapPosition(LayoutManager layoutManager, int velocityX, int velocityY) {
        int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return -1;
        }
        View mStartMostChildView = null;
        if (layoutManager.canScrollVertically()) {
            mStartMostChildView = findStartView(layoutManager, getVerticalHelper(layoutManager));
        } else if (layoutManager.canScrollHorizontally()) {
            mStartMostChildView = findStartView(layoutManager, getHorizontalHelper(layoutManager));
        }
        if (mStartMostChildView == null) {
            return -1;
        }
        int centerPosition = layoutManager.getPosition(mStartMostChildView);
        if (centerPosition == -1) {
            return -1;
        }
        boolean forwardDirection;
        if (!layoutManager.canScrollHorizontally()) {
            forwardDirection = velocityY > 0;
        } else if (velocityX > 0) {
            forwardDirection = true;
        } else {
            forwardDirection = false;
        }
        boolean reverseLayout = false;
        if (layoutManager instanceof ScrollVectorProvider) {
            PointF vectorForEnd = ((ScrollVectorProvider) layoutManager).computeScrollVectorForPosition(itemCount - 1);
            if (vectorForEnd != null) {
                reverseLayout = vectorForEnd.x < 0.0f || vectorForEnd.y < 0.0f;
            }
        }
        return reverseLayout ? forwardDirection ? centerPosition - 1 : centerPosition : forwardDirection ? centerPosition + 1 : centerPosition;
    }

    protected LinearSmoothScroller createSnapScroller(LayoutManager layoutManager) {
        if (layoutManager instanceof ScrollVectorProvider) {
            return new C03021(this.mRecyclerView.getContext());
        }
        return null;
    }

    private int distanceToCenter(@NonNull LayoutManager layoutManager, @NonNull View targetView, OrientationHelper helper) {
        int containerCenter;
        int childCenter = helper.getDecoratedStart(targetView) + (helper.getDecoratedMeasurement(targetView) / 2);
        if (layoutManager.getClipToPadding()) {
            containerCenter = helper.getStartAfterPadding() + (helper.getTotalSpace() / 2);
        } else {
            containerCenter = helper.getEnd() / 2;
        }
        return childCenter - containerCenter;
    }

    @Nullable
    private View findCenterView(LayoutManager layoutManager, OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        int center;
        View closestChild = null;
        if (layoutManager.getClipToPadding()) {
            center = helper.getStartAfterPadding() + (helper.getTotalSpace() / 2);
        } else {
            center = helper.getEnd() / 2;
        }
        int absClosest = BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT;
        for (int i = 0; i < childCount; i++) {
            View child = layoutManager.getChildAt(i);
            int absDistance = Math.abs((helper.getDecoratedStart(child) + (helper.getDecoratedMeasurement(child) / 2)) - center);
            if (absDistance < absClosest) {
                absClosest = absDistance;
                closestChild = child;
            }
        }
        return closestChild;
    }

    @Nullable
    private View findStartView(LayoutManager layoutManager, OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        View closestChild = null;
        int startest = BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT;
        for (int i = 0; i < childCount; i++) {
            View child = layoutManager.getChildAt(i);
            int childStart = helper.getDecoratedStart(child);
            if (childStart < startest) {
                startest = childStart;
                closestChild = child;
            }
        }
        return closestChild;
    }

    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull LayoutManager layoutManager) {
        if (this.mVerticalHelper == null || this.mVerticalHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
    }

    @NonNull
    private OrientationHelper getHorizontalHelper(@NonNull LayoutManager layoutManager) {
        if (this.mHorizontalHelper == null || this.mHorizontalHelper.mLayoutManager != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return this.mHorizontalHelper;
    }
}
