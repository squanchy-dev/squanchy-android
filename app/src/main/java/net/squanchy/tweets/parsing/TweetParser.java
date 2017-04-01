package net.squanchy.tweets.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.squanchy.tweets.model.TweetSpannableText;

abstract class TweetParser<T> {

    abstract Pattern pattern();

    abstract T convertFrom(TweetSpannableText data);

    List<T> parseDataFrom(String text) {
        Matcher matcher = pattern().matcher(text);
        List<T> matches = new ArrayList<>();

        while (matcher.find()) {
            matches.add(convertFrom(SpannableDataExtractor.extract(text, matcher.start(), matcher.end())));
        }

        return matches;
    }
}
