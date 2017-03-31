package net.squanchy.tweets.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.squanchy.tweets.model.TweetSpecialTextData;

import static net.squanchy.tweets.util.SpannableDataExtractor.extract;

abstract class TweetParserTemplate<T> {

    @ParsingRegex
    abstract String regex();

    abstract T convertFrom(TweetSpecialTextData data);

    List<T> parseDataFrom(@NonNull String text) {
        Matcher matcher = Pattern.compile(regex()).matcher(text);
        List<T> matches = new ArrayList<>();

        while (matcher.find()) {
            matches.add(convertFrom(extract(text, matcher.start(), matcher.end())));
        }

        return matches;
    }
}
