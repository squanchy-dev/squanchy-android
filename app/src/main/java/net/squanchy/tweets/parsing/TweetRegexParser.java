package net.squanchy.tweets.parsing;

import java.util.regex.Pattern;

abstract class TweetRegexParser<T> extends TweetParser<T> {

    abstract Regex regex();

    @Override
    Pattern pattern() {
        return regex().pattern();
    }
}
