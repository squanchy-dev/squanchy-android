package net.squanchy.tweets.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.util.TypedValue;

import net.squanchy.R;

public class TweetUrlSpanFactory {

    private final Context context;

    public TweetUrlSpanFactory(Context context) {
        this.context = context;
    }

    public TweetUrlSpan createFor(String url) {
        int linkColor = getColorFromTheme(context.getTheme(), R.attr.tweetLinkTextColor);
        return new TweetUrlSpan(url, linkColor);
    }

    @ColorInt
    private int getColorFromTheme(Resources.Theme theme, @AttrRes int attributeId) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attributeId, typedValue, true);
        return typedValue.data;
    }
}
