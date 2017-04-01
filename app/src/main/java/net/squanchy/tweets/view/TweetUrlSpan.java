package net.squanchy.tweets.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import timber.log.Timber;

public class TweetUrlSpan extends ClickableSpan {

    private final String url;
    private final int linkColor;

    TweetUrlSpan(String url, int linkColor) {
        this.url = url;
        this.linkColor = linkColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(linkColor);
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        Intent intent = createIntentWith(context);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Timber.e(e, "Unable to start activity for Twitter url: %s", url);
        }
    }

    private Intent createIntentWith(Context context) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        return intent;
    }
}
