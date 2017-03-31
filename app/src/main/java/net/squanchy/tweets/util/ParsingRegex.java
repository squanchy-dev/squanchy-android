package net.squanchy.tweets.util;

import android.support.annotation.StringDef;

import static net.squanchy.tweets.util.ParsingRegex.HASHTAG_REGEX;
import static net.squanchy.tweets.util.ParsingRegex.MENTIONS_REGEX;

@StringDef({MENTIONS_REGEX, HASHTAG_REGEX})
public @interface ParsingRegex {

    String MENTIONS_REGEX = "@\\w+";
    String HASHTAG_REGEX = "#\\w+";
}
