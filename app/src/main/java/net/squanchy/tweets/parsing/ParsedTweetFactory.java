package net.squanchy.tweets.parsing;

import android.support.annotation.NonNull;

import net.squanchy.tweets.domain.view.Tweet;
import net.squanchy.tweets.model.ParsedTweetData;

public class ParsedTweetFactory {

    private static final HashtagParser hashtagParser = new HashtagParser();
    private static final MentionParser mentionParser = new MentionParser();
    private static final UrlParser urlParser = new UrlParser();

    private ParsedTweetFactory() {
        //no instance
    }

    public static ParsedTweetData from(@NonNull Tweet tweet) {
        return ParsedTweetData.create(hashtagParser.parseDataFrom(tweet.text()),
                mentionParser.parseDataFrom(tweet.text()),
                urlParser.parseDataFrom(tweet.text()));
    }
}
