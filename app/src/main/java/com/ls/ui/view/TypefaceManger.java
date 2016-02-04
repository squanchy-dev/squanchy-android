/*
 * This document and the source codes contained herein are the property of Schneider Enterprises, LLC. 
 * Copyright (c) 2015, Schneider Enterprises, LLC.  All rights reserved.
 */

package com.ls.ui.view;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class TypefaceManger {

	private static final Map<String, Typeface> typefaces = new HashMap<>(13);

	public static Typeface getTypeface(String fontName, Context context) {
		Typeface ret = typefaces.get(fontName);

		if (ret == null) {
			ret = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
			typefaces.put(fontName, ret);
		}

		return ret;
	}

	public static Typeface getTypeface(int fontNameId, Context context) {
		return getTypeface(context.getString(fontNameId), context);
	}
}
