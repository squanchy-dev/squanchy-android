package com.ls.drupalconapp.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LightFontTextView extends TextView {

    public LightFontTextView(Context context) {
        super(context);
        setTypeface(getFont(context));
    }

    public LightFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(getFont(context));
    }

    public LightFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(getFont(context));
    }

    private Typeface getFont(Context context) {
        return FontHelper.getInstance(context).getFontRobotoLight();
    }
}
