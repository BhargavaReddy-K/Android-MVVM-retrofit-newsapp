package com.company.demoapp.model.font;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.LruCache;

public class TypeFaceSpan extends MetricAffectingSpan {
    private static final LruCache<String, Typeface> stringTypefaceLruCache = new LruCache<>(12);

    private Typeface typeface;

    public TypeFaceSpan(Context context, String typefaceName) {
        typeface = stringTypefaceLruCache.get(typefaceName);

        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), String.format("fonts/%s", typefaceName));
            stringTypefaceLruCache.put(typefaceName, typeface);
        }
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint textPaint) {
        textPaint.setTypeface(typeface);

        textPaint.setFlags(textPaint.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setTypeface(typeface);
        textPaint.setFlags(textPaint.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}

