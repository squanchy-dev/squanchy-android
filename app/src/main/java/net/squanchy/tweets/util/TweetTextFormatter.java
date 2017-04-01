package net.squanchy.tweets.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;

import java.util.List;

import net.squanchy.tweets.domain.view.HashtagEntity;
import net.squanchy.tweets.domain.view.MentionEntity;
import net.squanchy.tweets.domain.view.Tweet;
import net.squanchy.tweets.domain.view.UrlEntity;
import net.squanchy.tweets.model.ParsedTweet;
import net.squanchy.tweets.parsing.TweetTextParser;

public class TweetTextFormatter {

    private static final String BASE_TWITTER_URL = "https://twitter.com/";
    private static final String MENTION_URL_TEMPLATE = BASE_TWITTER_URL + "%s";
    private static final String QUERY_URL_TEMPLATE = BASE_TWITTER_URL + "search?q=%s";

    private final TweetTextParser tweetTextParser;

    public TweetTextFormatter(TweetTextParser tweetTextParser) {
        this.tweetTextParser = tweetTextParser;
    }

    public Spannable format(Tweet tweet) {
        Spannable spannable = new SpannableString(tweet.text());

        ParsedTweet parsedTweet = tweetTextParser.parse(tweet.text());

        addUrlSpans(spannable, parsedTweet.urls());
        addMentionSpans(spannable, parsedTweet.mentions());
        addHashtagSpans(spannable, parsedTweet.hashtags());

        return spannable;
    }

    private void addUrlSpans(Spannable spannable, List<UrlEntity> urls) {
        for (UrlEntity urlEntity : urls) {
            spannable.setSpan(new URLSpan(urlEntity.url()), urlEntity.start(), urlEntity.end(), 0);
        }
    }

    private void addMentionSpans(Spannable spannable, List<MentionEntity> mentions) {
        for (MentionEntity entity : mentions) {
            String url = String.format(MENTION_URL_TEMPLATE, entity.displayName());
            spannable.setSpan(new URLSpan(url), entity.start(), entity.end(), 0);
        }
    }

    private void addHashtagSpans(Spannable spannable, List<HashtagEntity> hashtags) {
        for (HashtagEntity entity : hashtags) {
            String url = String.format(QUERY_URL_TEMPLATE, entity.text());
            spannable.setSpan(new URLSpan(url), entity.start(), entity.end(), 0);
        }
    }
}
