package com.ls.utils;

import com.ls.drupalcon.R;

import android.app.Activity;

public class WebviewUtils {

    public static String getHtml(Activity activity, String body) {
        String css = activity.getString(R.string.css);
        return String.format("<html><header>%s</header><body>%s</body></html>", css, body);
    }
}
