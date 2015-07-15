package io.github.indrora.jouretnuit.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by indrora on 7/4/15.
 */
public class CircleGaugeView extends View {

    /**
     * Class to draw a circle for timers and stopwatches.
     * These two usages require two different animation modes:
     * Timer counts down. In this mode the animation is counter-clockwise and stops at 0.
     * Stopwatch counts up. In this mode the animation is clockwise and will run until stopped.
     */


    private int mAccentColor;
    private int mOverflowColor;
    private int mWhiteColor;
    private long mMaxValue = 0;
    private long mCurrentValue = 0;
    private long mOverflowValue = 0;
    private static float mStrokeSize = 12;
    private static float mDotRadius = 6;
    private static float mMarkerStrokeSize = 14;
    private final Paint mPaint = new Paint();
    private final Paint mFill = new Paint();
    private final RectF mArcRect = new RectF();
    private float mRadiusOffset;   // amount to remove from radius to account for markers on circle
    private float mScreenDensity;

    // Stopwatch mode is the default.
    private boolean mTimerMode = false;

    @SuppressWarnings("unused")
    public CircleGaugeView(Context context) {
        this(context, null);
    }

    public CircleGaugeView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);
    }

    public void setValue(long value, long max) {
        if (value >= max) {
            mMaxValue = mCurrentValue = value;
            mOverflowValue = max;
        } else {
            mOverflowValue = 0;
            mMaxValue = max;
            mCurrentValue = value;
        }
        invalidate();
    }


    /**
     * Calculate the amount by which the radius of a CircleTimerView should be offset by the any
     * of the extra painted objects.
     */
    public static float calculateRadiusOffset(
            float strokeSize, float dotStrokeSize, float markerStrokeSize) {
        return Math.max(strokeSize, Math.max(dotStrokeSize, markerStrokeSize));
    }


    private void init(Context c) {

        Resources resources = c.getResources();
        mScreenDensity = resources.getDisplayMetrics().density;
        mStrokeSize = 16* mScreenDensity;
        mMarkerStrokeSize = 16* mScreenDensity;
        mDotRadius = 8* mScreenDensity;
        mRadiusOffset = calculateRadiusOffset(
                mStrokeSize, mDotRadius * 2.0f, mMarkerStrokeSize);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);



        mWhiteColor = resources.getColor(android.support.v7.appcompat.R.color.background_material_dark);
        mAccentColor = resources.getColor(android.support.v7.appcompat.R.color.accent_material_light);
        mOverflowColor = resources.getColor(android.support.v7.appcompat.R.color.accent_material_dark);

        mFill.setAntiAlias(true);
        mFill.setStyle(Paint.Style.FILL);
        mFill.setColor(mAccentColor);
    }

    public void setTimerMode(boolean mode) {
        mTimerMode = mode;
    }

    @Override
    public void onDraw(Canvas canvas) {
        int xCenter = getWidth() / 2 + 1;
        int yCenter = getHeight() / 2;

        mPaint.setStrokeWidth(mStrokeSize);
        float radius = Math.min(xCenter, yCenter) - mRadiusOffset;

        if (mCurrentValue == 0) {
            // just draw a complete white circle, no red arc needed
            mPaint.setColor(mWhiteColor);
            canvas.drawCircle(xCenter, yCenter, radius, mPaint);
        } else {
            //draw a combination of red and white arcs to create a circle
            mArcRect.top = yCenter - radius;
            mArcRect.bottom = yCenter + radius;
            mArcRect.left = xCenter - radius;
            mArcRect.right = xCenter + radius;
            float redPercent = (float) mCurrentValue / (float) mMaxValue;
            // prevent timer from doing more than one full circle
            redPercent = (redPercent > 1 && mTimerMode) ? 1 : redPercent;

            float whitePercent = 1 - (redPercent > 1 ? 1 : redPercent);
            // draw red arc here
            mPaint.setColor(mAccentColor);
            if (mTimerMode) {
                canvas.drawArc(mArcRect, 270, -redPercent * 360, false, mPaint);
            } else {
                canvas.drawArc(mArcRect, 270, +redPercent * 360, false, mPaint);
            }

            // draw white arc here
            mPaint.setStrokeWidth(mStrokeSize);
            mPaint.setColor(mWhiteColor);
            if (mTimerMode) {
                canvas.drawArc(mArcRect, 270, +whitePercent * 360, false, mPaint);
            } else {
                canvas.drawArc(mArcRect, 270 + (1 - whitePercent) * 360,
                        whitePercent * 360, false, mPaint);
            }

            if (mOverflowValue > 0) {
                float angle = (float) (mOverflowValue) / (float) mMaxValue * 360;

                mPaint.setStrokeWidth(mStrokeSize);
                mPaint.setColor(mOverflowColor);
                canvas.drawArc(mArcRect, 270, -360 + angle, false, mPaint);

                mPaint.setStrokeWidth(1.25f * mStrokeSize);
                mPaint.setColor(mWhiteColor);
                // draw 2dips thick marker
                // the formula to draw the marker 1 unit thick is:
                // 180 / (radius * Math.PI)
                // after that we have to scale it by the screen density
                canvas.drawArc(mArcRect, 270 + angle, 2*mScreenDensity *
                        (float) (360 / (radius * Math.PI)), false, mPaint);

            }

            if(mCurrentValue< mMaxValue) {
                drawRedDot(canvas, redPercent, xCenter, yCenter, radius);
            }

        }
    }

    protected void drawRedDot(
            Canvas canvas, float degrees, int xCenter, int yCenter, float radius) {
        mPaint.setColor(mAccentColor);
        float dotPercent;
        if (mTimerMode) {
            dotPercent = 270 - degrees * 360;
        } else {
            dotPercent = 270 + degrees * 360;
        }

        final double dotRadians = Math.toRadians(dotPercent);
        canvas.drawCircle(xCenter + (float) (radius * Math.cos(dotRadians)),
                yCenter + (float) (radius * Math.sin(dotRadians)), mDotRadius, mFill);
    }

}
