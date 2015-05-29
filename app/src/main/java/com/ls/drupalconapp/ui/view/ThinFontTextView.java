package com.ls.drupalconapp.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ThinFontTextView extends TextView {

    public ThinFontTextView(Context context) {
        super(context);
        setTypeface(getFont(context));
    }

    public ThinFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(getFont(context));
    }

    public ThinFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(getFont(context));
    }

    private Typeface getFont(Context context) {
        return FontHelper.getInstance(context).getFontRobotoThin();
    }
}
