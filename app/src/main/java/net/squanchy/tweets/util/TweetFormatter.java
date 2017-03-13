package net.squanchy.tweets.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.UrlEntity;

import java.util.List;

public class TweetFormatter {

    public static Spannable format(Tweet tweet) {
        Spannable spannable = new SpannableStringBuilder(tweet.text);
        addUrlSpan(spannable, tweet.entities.urls);
        return spannable;
    }

    private static void addUrlSpan(Spannable spannable, List<UrlEntity> urlEntities) {
        for (UrlEntity urlEntity : urlEntities) {
            spannable.setSpan(new URLSpan(urlEntity.url), urlEntity.getStart(), urlEntity.getEnd(), 0);
        }
    }
}
