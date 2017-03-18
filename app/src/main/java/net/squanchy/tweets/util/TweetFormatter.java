package net.squanchy.tweets.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;

import com.twitter.sdk.android.core.models.HashtagEntity;
import com.twitter.sdk.android.core.models.MentionEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.UrlEntity;

import java.util.List;

public class TweetFormatter {

    private static final String BASE_TWITTER_URL = "https://twitter.com/";
    private static final String TWITTER_QUERY = "search?q=";

    public static Spannable format(Tweet tweet) {
        Spannable spannable = new SpannableString(tweet.text);

        addUrlSpan(spannable, tweet.entities.urls);
        addMentionSpan(spannable, tweet.entities.userMentions);
        addHashtagSpan(spannable, tweet.entities.hashtags);

        return spannable;
    }

    private static void addUrlSpan(Spannable spannable, List<UrlEntity> urlEntities) {
        for (UrlEntity urlEntity : urlEntities) {
            spannable.setSpan(new URLSpan(urlEntity.url), urlEntity.getStart(), urlEntity.getEnd(), 0);
        }
    }

    private static void addMentionSpan(Spannable spannable, List<MentionEntity> mentionEntities) {
        for (MentionEntity entity : mentionEntities) {
            spannable.setSpan(new URLSpan(BASE_TWITTER_URL + entity.screenName), entity.getStart(), entity.getEnd(), 0);
        }
    }

    private static void addHashtagSpan(Spannable spannable, List<HashtagEntity> hashtagEntities) {
        for (HashtagEntity entity : hashtagEntities) {
            spannable.setSpan(new URLSpan(BASE_TWITTER_URL + TWITTER_QUERY + entity.text), entity.getStart(), entity.getEnd(), 0);
        }
    }
}
