package com.ls.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.ls.drupalcon.R;

import android.app.Activity;

public class WebviewUtils {

	public static String getHtml(Activity activity, String body) {
		String css = activity.getString(R.string.css);
		return String.format("<html><header>%s</header><body>%s</body></html>", css, body);
	}

	public static void openUrl(@NonNull Context context, @NonNull String url) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, R.string.no_default_browser_found, Toast.LENGTH_SHORT).show();
		}
	}
}
