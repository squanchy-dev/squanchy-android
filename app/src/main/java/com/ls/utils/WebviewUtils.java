package com.ls.utils;

import android.app.Activity;

import com.ls.drupalcon.R;

public class WebviewUtils {

    public static String getHtml(Activity activity, String body) {
        String css = activity.getString(R.string.css);
        return String.format("<html><header>%s</header><body>%s</body></html>", css, body);
    }
}
