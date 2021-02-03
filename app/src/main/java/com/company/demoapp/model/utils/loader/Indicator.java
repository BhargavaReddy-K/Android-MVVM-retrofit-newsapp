package com.company.demoapp.model.utils.loader;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class Indicator extends Drawable implements Animatable {

    private ArrayList<ValueAnimator> animatorList;
    private int alpha = 255;
    private static final Rect ZERO_BOUNDS_RECT = new Rect();
    private Rect drawBounds = ZERO_BOUNDS_RECT;

    private boolean isAnimator;
    private final HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener> updateListeners = new HashMap<>();

    private final Paint paint = new Paint();

    public Indicator() {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }


    public void setColor(int color) {
        paint.setColor(color);
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    @Override
    public int getAlpha() {
        return alpha;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        draw(canvas, paint);
    }

    protected abstract void draw(Canvas canvas, Paint paint);

    protected abstract ArrayList<ValueAnimator> onCreateAnimators();

    @Override
    public void start() {
        ensureAnimators();
        if (animatorList == null) {
            return;
        }
        // If the animators has not ended, do nothing.
        if (isStarted()) {
            return;
        }
        startAnimators();
        invalidateSelf();
    }

    private void startAnimators() {
        for (int i = 0; i < animatorList.size(); i++) {
            ValueAnimator animator = animatorList.get(i);

            ValueAnimator.AnimatorUpdateListener updateListener = updateListeners.get(animator);
            if (updateListener != null) {
                animator.addUpdateListener(updateListener);
            }

            animator.start();
        }
    }

    @SuppressLint("NewApi")
    private void stopAnimators() {
        if (animatorList != null) {
            for (ValueAnimator animator : animatorList) {
                if (animator != null && animator.isStarted()) {
                    animator.removeAllUpdateListeners();
                    animator.end();
                }
            }
        }
    }

    private void ensureAnimators() {
        if (!isAnimator) {
            animatorList = onCreateAnimators();
            isAnimator = true;
        }
    }

    @Override
    public void stop() {
        stopAnimators();
    }

    @SuppressLint("NewApi")
    private boolean isStarted() {
        for (ValueAnimator animator : animatorList) {
            return animator.isStarted();
        }
        return false;
    }

    @Override
    public boolean isRunning() {
        for (ValueAnimator animator : animatorList) {
            return animator.isRunning();
        }
        return false;
    }

    void addUpdateListener(ValueAnimator animator, ValueAnimator.AnimatorUpdateListener updateListener) {
        updateListeners.put(animator, updateListener);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        setDrawBounds(bounds);
    }

    private void setDrawBounds(Rect drawBounds) {
        setDrawBounds(drawBounds.left, drawBounds.top, drawBounds.right, drawBounds.bottom);
    }

    private void setDrawBounds(int left, int top, int right, int bottom) {
        this.drawBounds = new Rect(left, top, right, bottom);
    }

    void postInvalidate() {
        invalidateSelf();
    }

    int getWidth() {
        return drawBounds.width();
    }

    int getHeight() {
        return drawBounds.height();
    }


}