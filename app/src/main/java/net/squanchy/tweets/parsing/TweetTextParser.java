package net.squanchy.tweets.parsing;

import net.squanchy.tweets.model.ParsedTweet;

public class TweetTextParser {

    private static final HashtagParser hashtagParser = new HashtagParser();
    private static final MentionParser mentionParser = new MentionParser();
    private static final UrlParser urlParser = new UrlParser();

    public ParsedTweet parse(String tweetText) {
        return ParsedTweet.create(
                hashtagParser.parseDataFrom(tweetText),
                mentionParser.parseDataFrom(tweetText),
                urlParser.parseDataFrom(tweetText)
        );
    }
}
