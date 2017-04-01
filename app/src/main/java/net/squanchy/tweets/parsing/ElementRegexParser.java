package net.squanchy.tweets.parsing;

import java.util.regex.Pattern;

abstract class ElementRegexParser<T> extends ElementParser<T> {

    abstract Regex regex();

    @Override
    Pattern pattern() {
        return regex().pattern();
    }
}
