package com.ls.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class MultiFontsTypefaceSpan extends TypefaceSpan {

    private Typeface newType;

    public MultiFontsTypefaceSpan(String family, Typeface  type) {
        super(family);
        newType = type;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        if(tf != null) {
            paint.setTypeface(tf);
        }
    }
}