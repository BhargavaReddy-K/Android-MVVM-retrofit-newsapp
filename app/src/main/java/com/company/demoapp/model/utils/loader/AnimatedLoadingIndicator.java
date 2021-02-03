package com.company.demoapp.model.utils.loader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.company.demoapp.R;


public class AnimatedLoadingIndicator extends View {


    private int minWidth;
    private int maxWidth;
    private int minHeight;
    private int maxHeight;

    private Indicator indicator;
    private int indicatorColor;
    private boolean shouldStartAnimationDrawable;
    private static final ProgressBallMultipleIndicator DEFAULT_INDICATOR = new ProgressBallMultipleIndicator();

    public AnimatedLoadingIndicator(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public AnimatedLoadingIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, R.style.AnimatedLoadingIndicatorView);
    }

    public AnimatedLoadingIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, R.style.AnimatedLoadingIndicatorView);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        minWidth = 24;
        maxWidth = 48;
        minHeight = 24;
        maxHeight = 48;

        @SuppressLint("CustomViewStyleable") final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedLoadingIndicatorView, defStyleAttr, defStyleRes);

        minWidth = typedArray.getDimensionPixelSize(R.styleable.AnimatedLoadingIndicatorView_minWidth, minWidth);
        maxWidth = typedArray.getDimensionPixelSize(R.styleable.AnimatedLoadingIndicatorView_maxWidth, maxWidth);
        minHeight = typedArray.getDimensionPixelSize(R.styleable.AnimatedLoadingIndicatorView_minHeight, minHeight);
        maxHeight = typedArray.getDimensionPixelSize(R.styleable.AnimatedLoadingIndicatorView_maxHeight, maxHeight);
        String indicatorName = typedArray.getString(R.styleable.AnimatedLoadingIndicatorView_indicatorName);
        indicatorColor = typedArray.getColor(R.styleable.AnimatedLoadingIndicatorView_indicatorColor, Color.WHITE);
        setIndicator(indicatorName);
        if (indicator == null) {
            setIndicator(DEFAULT_INDICATOR);
        }
        typedArray.recycle();
    }


    private void setIndicator(Indicator indicator) {
        if (this.indicator != indicator) {
            if (this.indicator != null) {
                this.indicator.setCallback(null);
                unscheduleDrawable(this.indicator);
            }
            this.indicator = indicator;
            setIndicatorColor(indicatorColor);
            if (indicator != null) {
                indicator.setCallback(this);
            }
            postInvalidate();
        }
    }

    private void setIndicatorColor(int color) {
        this.indicatorColor = color;
        indicator.setColor(color);
    }

    private void setIndicator(String indicatorName) {
        if (TextUtils.isEmpty(indicatorName)) {
            return;
        }
        StringBuilder drawableClassName = new StringBuilder();
        if (!indicatorName.contains(".")) {
            String defaultPackageName = getClass().getPackage().getName();
            drawableClassName.append(defaultPackageName)
                    .append(".indicators")
                    .append(".");
        }
        drawableClassName.append(indicatorName);
        try {
            Class<?> drawableClass = Class.forName(drawableClassName.toString());
            Indicator indicator = (Indicator) drawableClass.newInstance();
            setIndicator(indicator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable drawable) {
        return drawable == indicator || super.verifyDrawable(drawable);
    }

    private void startAnimation() {
        if (getVisibility() != VISIBLE) {
            return;
        }

        if (indicator != null) {
            shouldStartAnimationDrawable = true;
        }
        postInvalidate();
    }

    private void stopAnimation() {
        if (indicator != null) {
            indicator.stop();
            shouldStartAnimationDrawable = false;
        }
        postInvalidate();
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                stopAnimation();
            } else {
                startAnimation();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        if (verifyDrawable(drawable)) {
            final Rect dirty = drawable.getBounds();
            final int scrollX = getScrollX() + getPaddingLeft();
            final int scrollY = getScrollY() + getPaddingTop();

            invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        updateDrawableBounds(width, height);
    }

    private void updateDrawableBounds(int width, int height) {
        width -= getPaddingRight() + getPaddingLeft();
        height -= getPaddingTop() + getPaddingBottom();

        int right = width;
        int bottom = height;
        int top = 0;
        int left = 0;

        if (indicator != null) {
            final int intrinsicWidth = indicator.getIntrinsicWidth();
            final int intrinsicHeight = indicator.getIntrinsicHeight();
            final float intrinsicAspect = (float) intrinsicWidth / intrinsicHeight;
            final float boundAspect = (float) width / height;
            if (intrinsicAspect != boundAspect) {
                if (boundAspect > intrinsicAspect) {
                    final int widthInt = (int) (height * intrinsicAspect);
                    left = (width - widthInt) / 2;
                    right = left + widthInt;
                } else {
                    final int heightInt = (int) (width * (1 / intrinsicAspect));
                    top = (height - heightInt) / 2;
                    bottom = top + heightInt;
                }
            }
            indicator.setBounds(left, top, right, bottom);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);
    }

    private  void drawTrack(Canvas canvas) {
        final Drawable drawable = indicator;
        if (drawable != null) {
            final int saveCount = canvas.save();

            canvas.translate(getPaddingLeft(), getPaddingTop());

            drawable.draw(canvas);
            canvas.restoreToCount(saveCount);

            if (shouldStartAnimationDrawable) {
                ((Animatable) drawable).start();
                shouldStartAnimationDrawable = false;
            }
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dWidth = 0;
        int dHeight = 0;

        final Drawable d = indicator;
        if (d != null) {
            dWidth = Math.max(minWidth, Math.min(maxWidth, d.getIntrinsicWidth()));
            dHeight = Math.max(minHeight, Math.min(maxHeight, d.getIntrinsicHeight()));
        }

        updateDrawableState();

        dWidth += getPaddingLeft() + getPaddingRight();
        dHeight += getPaddingTop() + getPaddingBottom();

        final int measuredWidth = resolveSizeAndState(dWidth, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(dHeight, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateDrawableState();
    }

    private void updateDrawableState() {
        final int[] state = getDrawableState();
        if (indicator != null && indicator.isStateful()) {
            indicator.setState(state);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);

        if (indicator != null) {
            indicator.setHotspot(x, y);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

}