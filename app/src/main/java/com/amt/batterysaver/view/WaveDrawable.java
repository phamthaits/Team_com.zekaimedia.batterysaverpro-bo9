package com.amt.batterysaver.view;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;

public class WaveDrawable extends Drawable {
    protected int alpha;
    private Interpolator alphaInterpolator;
    private long animationTime;
    private Animator animator;
    private AnimatorSet animatorSet;
    private int color;
    private Paint mainPaint;
    private boolean play;
    private int radius;
    private boolean single;
    private Interpolator waveInterpolator;
    private Paint wavePaint;
    private Paint wavePaint2;
    protected float waveScale;
    protected float waveSize;

    public WaveDrawable(int i, int i2, float f) {
        this.animationTime = 2000;
        this.play = true;
        this.single = false;
        this.color = i;
        this.radius = i2;
        this.waveScale = f;
        this.waveSize = f;
        this.alpha = 128;
        this.wavePaint = new Paint(1);
        this.wavePaint2 = new Paint(1);
        this.mainPaint = new Paint(1);
        this.animatorSet = new AnimatorSet();
    }

    public WaveDrawable(int i, int i2, float f, long j) {
        this(i, i2, f);
        this.animationTime = j;
    }

    private Animator generateAnimation() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "waveScale", this.waveSize, 1.0f);
        ofFloat.setDuration(this.animationTime);
        if (this.waveInterpolator != null) {
            ofFloat.setInterpolator(this.waveInterpolator);
        }
        ofFloat.setRepeatCount(ValueAnimator.INFINITE);
        ofFloat.setRepeatMode(ValueAnimator.RESTART);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "alpha", 128, 0);
        ofInt.setDuration(this.animationTime);
        if (this.alphaInterpolator != null) {
            ofInt.setInterpolator(this.alphaInterpolator);
        }
        ofInt.setRepeatCount(-1);
        ofInt.setRepeatMode(1);
        this.animatorSet.playTogether(ofFloat, ofInt);
        return this.animatorSet;
    }

    public void draw(Canvas canvas) {
        if (this.play) {
            Rect bounds = getBounds();
            this.wavePaint.setStyle(Style.FILL);
            this.wavePaint.setColor(this.color);
            this.wavePaint.setAlpha(this.alpha);
            this.mainPaint.setStyle(Style.FILL);
            this.mainPaint.setColor(this.color);
            canvas.drawCircle((float) bounds.centerX(), (float) bounds.centerY(), ((float) this.radius) * this.waveSize, this.mainPaint);
            canvas.drawCircle((float) bounds.centerX(), (float) bounds.centerY(), ((float) this.radius) * this.waveScale, this.wavePaint);
            if (!this.single) {
                if (this.waveScale < (this.waveSize + 1.0f) / 2.0f) {
                    this.wavePaint2.setStyle(Style.FILL);
                    this.wavePaint2.setColor(this.color);
                    this.wavePaint2.setAlpha(this.alpha - 64);
                    canvas.drawCircle((float) bounds.centerX(), (float) bounds.centerY(), ((float) this.radius) * (((1.0f - this.waveSize) / 2.0f) + this.waveScale), this.wavePaint2);
                    return;
                }
                this.wavePaint2.setStyle(Style.FILL);
                this.wavePaint2.setColor(this.color);
                this.wavePaint2.setAlpha(this.alpha + 64);
                canvas.drawCircle((float) bounds.centerX(), (float) bounds.centerY(), ((float) this.radius) * (this.waveScale - ((1.0f - this.waveSize) / 2.0f)), this.wavePaint2);
            }
        }
    }

    public int getOpacity() {
        return this.wavePaint.getAlpha();
    }

    protected float getWaveScale() {
        return this.waveScale;
    }

    public boolean isAnimationRunning() {
        return this.animator != null && this.animator.isRunning();
    }

    public void setAlpha(int i) {
        this.alpha = i;
        invalidateSelf();
    }

    public void setAlphaInterpolator(Interpolator interpolator) {
        this.alphaInterpolator = interpolator;
    }

    public void setAnimationTime(long j) {
        this.animationTime = j;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.wavePaint.setColorFilter(colorFilter);
    }

    public void setSingleWave(boolean z) {
        this.single = z;
    }

    public void setWaveInterpolator(Interpolator interpolator) {
        this.waveInterpolator = interpolator;
    }

    protected void setWaveScale(float f) {
        this.waveScale = f;
        invalidateSelf();
    }

    public void startAnimation() {
        this.play = true;
        this.animator = generateAnimation();
        this.animator.start();
    }

    public void stopAnimation() {
        this.play = false;
        if (this.animatorSet != null) {
            this.animatorSet.end();
        }
        if (this.animator.isRunning()) {
            this.animator.end();
        }
    }
}
