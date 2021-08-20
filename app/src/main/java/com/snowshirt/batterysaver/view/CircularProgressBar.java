package com.snowshirt.batterysaver.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.snowshirt.batterysaver.R;


public class CircularProgressBar extends View {

    private int mViewWidth;
    private int mViewHeight;

    private float mSweepAngle = 0;              // How long to sweep from mStartAngle
    private float mMaxSweepAngle = 360;         // Max degrees to sweep = full circle
    private float mStrokeWidth = 8;              // Width of outline
    private int mMaxProgress = 100;             // Max progress to use
    private boolean mRoundedCorners = true;     // Set to true if rounded corners should be applied to outline ends
    private int mProgressColor = Color.WHITE;   // Outline color

    private final Paint mPaint;                 // Allocate paint outside onDraw to avoid unnecessary object creation
    private Context mContext;

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mStrokeWidth = context.getResources().getDimension(R.dimen.cirularsize);
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initMeasurments();
        drawOutlineTmp(canvas);
        drawOutlineArc(canvas);
    }

    private void initByAttributes(TypedArray attributes) {
        mProgressColor = attributes.getColor(R.styleable.CircularProgressBar_finished_color, Color.WHITE);
        //   setProgress(30);
    }

    private void initMeasurments() {
        mViewWidth = getWidth();
        mViewHeight = getHeight();
    }

    private void drawOutlineTmp(Canvas canvas) {
        final int diameter = Math.min(mViewWidth, mViewHeight);
        final float pad = (float) (mStrokeWidth / 2.0);
        final RectF outerOval = new RectF(pad, pad, diameter - pad, diameter - pad);

        mPaint.setColor(ContextCompat.getColor(mContext, R.color.progress_background));
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(mRoundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(outerOval, 90, 360, false, mPaint);
    }

    private void drawOutlineArc(Canvas canvas) {
        final int diameter = Math.min(mViewWidth, mViewHeight);
        final float pad = (float) (mStrokeWidth / 2.0);
        final RectF outerOval = new RectF(pad, pad, diameter - pad, diameter - pad);

        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(mRoundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(outerOval, 90, mSweepAngle, false, mPaint);
    }

    private float calcSweepAngleFromProgress(int progress) {
        return (mMaxSweepAngle / mMaxProgress) * progress;
    }

    private int calcProgressFromSweepAngle(float sweepAngle) {
        return (int) ((sweepAngle * mMaxProgress) / mMaxSweepAngle);
    }

    /**
     * Set progress of the circular progress bar.
     *
     * @param progress progress between 0 and 100.
     */
    public void setProgress(int progress) {
        mRoundedCorners = progress != 100;
        ValueAnimator animator = ValueAnimator.ofFloat(mSweepAngle, calcSweepAngleFromProgress(progress));
        animator.setInterpolator(new DecelerateInterpolator());
        int mAnimationDuration = 500;
        animator.setDuration(mAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSweepAngle = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
        invalidate();
    }

    public void setProgressWidth(int width) {
        mStrokeWidth = width;
        invalidate();
    }

    public void setTextColor(int color) {
        invalidate();
    }

    public void showProgressText(boolean show) {
        invalidate();
    }

    /**
     * Toggle this if you don't want rounded corners on progress bar.
     * Default is true.
     *
     * @param roundedCorners true if you want rounded corners of false otherwise.
     */
    public void useRoundedCorners(boolean roundedCorners) {
        mRoundedCorners = roundedCorners;
        invalidate();
    }
}
