package net.squanchy.tweets.util;

import java.util.regex.Pattern;

abstract  class TweetRegexParserTemplate<T> extends TweetParserTemplate<T>{

    @ParsingRegex
    abstract String regex();

    @Override
    Pattern pattern() {
        return Pattern.compile(regex());
    }
}
