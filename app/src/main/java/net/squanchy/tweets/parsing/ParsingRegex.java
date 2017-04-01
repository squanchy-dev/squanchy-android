package net.squanchy.tweets.parsing;

import android.support.annotation.StringDef;

import static net.squanchy.tweets.parsing.ParsingRegex.HASHTAG_REGEX;
import static net.squanchy.tweets.parsing.ParsingRegex.MENTIONS_REGEX;

@StringDef({MENTIONS_REGEX, HASHTAG_REGEX})
@interface ParsingRegex {

    String MENTIONS_REGEX = "@\\w+";
    String HASHTAG_REGEX = "#\\w+";
}
