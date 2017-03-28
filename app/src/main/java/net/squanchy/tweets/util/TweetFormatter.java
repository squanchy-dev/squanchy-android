package net.squanchy.tweets.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;

import java.util.List;

import net.squanchy.tweets.domain.view.HashtagEntity;
import net.squanchy.tweets.domain.view.MentionEntity;
import net.squanchy.tweets.domain.view.Tweet;
import net.squanchy.tweets.domain.view.UrlEntity;

public class TweetFormatter {

    private static final String BASE_TWITTER_URL = "https://twitter.com/";
    private static final String TWITTER_QUERY = "search?q=";

    public static Spannable format(Tweet tweet) {
        Spannable spannable = new SpannableString(tweet.text());

        addUrlSpan(spannable, tweet.urls());
        addMentionSpan(spannable, tweet.mentions());
        addHashtagSpan(spannable, tweet.hashtags());

        return spannable;
    }

    private static void addUrlSpan(Spannable spannable, List<UrlEntity> urlEntities) {
        for (UrlEntity urlEntity : urlEntities) {
            spannable.setSpan(new URLSpan(urlEntity.url()), urlEntity.start(), urlEntity.end(), 0);
        }
    }

    private static void addMentionSpan(Spannable spannable, List<MentionEntity> mentionEntities) {
        for (MentionEntity entity : mentionEntities) {
            spannable.setSpan(new URLSpan(BASE_TWITTER_URL + entity.displayName()), entity.start(), entity.end(), 0);
        }
    }

    private static void addHashtagSpan(Spannable spannable, List<HashtagEntity> hashtagEntities) {
        for (HashtagEntity entity : hashtagEntities) {
            spannable.setSpan(new URLSpan(BASE_TWITTER_URL + TWITTER_QUERY + entity.text()), entity.start(), entity.end(), 0);
        }
    }
}
