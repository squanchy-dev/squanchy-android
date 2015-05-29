package com.ls.drupalconapp.ui.view.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class MultiFontsTypefaceSpan extends TypefaceSpan {

    private Typeface newType;

    public MultiFontsTypefaceSpan(String family, Typeface  type) {
        super(family);
        newType = type;
    }

    public MultiFontsTypefaceSpan(String family) {
        super(family);
    }

    public MultiFontsTypefaceSpan(Parcel src) {
        super(src);
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