/*
 * This document and the source codes contained herein are the property of Schneider Enterprises, LLC. 
 * Copyright (c) 2015, Schneider Enterprises, LLC.  All rights reserved.
 */

package com.ls.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import com.ls.drupalcon.R;

public class FontTextView extends TextView {

	public FontTextView(Context context) {
		super(context);
	}

	public FontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		applyAttributes(attrs);
	}

	public FontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		applyAttributes(attrs);
	}

	private void applyAttributes(AttributeSet attrs) {
		if (isInEditMode()) {
			return;
		}
		TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.FontTextView);
		String font = array.getString(R.styleable.FontTextView_custom_font);
		if (font != null) {
			setTypeface(font);
		}
		array.recycle();
	}

	public void setTypeface(final String fontName) {
		setTypeface(TypefaceManger.getTypeface(fontName, getContext()));
	}

	public void setTypeface(final String theName, int theStyle) {
		this.setTypeface(TypefaceManger.getTypeface(theName, getContext()), theStyle);
	}

	public void setTypeface(int theNameResId) {
		this.setTypeface(TypefaceManger.getTypeface(getContext().getString(theNameResId), this.getContext()));
	}

	public void setTypeface(int theNameResId, int theStyle) {
		this.setTypeface(TypefaceManger.getTypeface(getContext().getString(theNameResId), this.getContext()), theStyle);
	}
}
