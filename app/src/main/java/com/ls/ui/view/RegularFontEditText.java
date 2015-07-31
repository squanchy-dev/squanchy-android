package com.ls.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by omeres on 09.07.2014.
 */
public class RegularFontEditText extends EditText {

    public RegularFontEditText(Context context) {
        super(context);

        setTypeface(getFont(context));
    }

    public RegularFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setTypeface(getFont(context));
    }

    public RegularFontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setTypeface(getFont(context));
    }

    private Typeface getFont(Context context) {
        return FontHelper.getInstance(context).getFontRobotoRegular();
    }
}
