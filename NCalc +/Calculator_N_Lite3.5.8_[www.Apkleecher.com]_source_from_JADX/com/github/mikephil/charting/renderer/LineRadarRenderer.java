package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class LineRadarRenderer extends LineScatterCandleRadarRenderer {
    public LineRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    protected void drawFilledPath(Canvas c, Path filledPath, Drawable drawable) {
        if (clipPathSupported()) {
            int save = c.save();
            c.clipPath(filledPath);
            drawable.setBounds((int) this.mViewPortHandler.contentLeft(), (int) this.mViewPortHandler.contentTop(), (int) this.mViewPortHandler.contentRight(), (int) this.mViewPortHandler.contentBottom());
            drawable.draw(c);
            c.restoreToCount(save);
            return;
        }
        throw new RuntimeException("Fill-drawables not (yet) supported below API level 18, this code was run on API level " + Utils.getSDKInt() + ".");
    }

    protected void drawFilledPath(Canvas c, Path filledPath, int fillColor, int fillAlpha) {
        int color = (fillAlpha << 24) | (ViewCompat.MEASURED_SIZE_MASK & fillColor);
        if (clipPathSupported()) {
            int save = c.save();
            c.clipPath(filledPath);
            c.drawColor(color);
            c.restoreToCount(save);
            return;
        }
        Style previous = this.mRenderPaint.getStyle();
        int previousColor = this.mRenderPaint.getColor();
        this.mRenderPaint.setStyle(Style.FILL);
        this.mRenderPaint.setColor(color);
        c.drawPath(filledPath, this.mRenderPaint);
        this.mRenderPaint.setColor(previousColor);
        this.mRenderPaint.setStyle(previous);
    }

    private boolean clipPathSupported() {
        return Utils.getSDKInt() >= 18;
    }
}
