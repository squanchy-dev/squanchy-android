package net.squanchy.tweets.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.squanchy.tweets.model.TweetSpecialTextData;

public class TweetParser {
    private static final String MENTIONS_REGEX = "@\\w+";
    private static final String HASHTAG_REGEX = "#\\w+";

    public static List<TweetSpecialTextData> parseHashtagsFrom(String text) {
        Matcher matcher = Pattern.compile(HASHTAG_REGEX).matcher(text);
        List<TweetSpecialTextData> matches = new ArrayList<>();

        while(matcher.find()) {
            matches.add(extract(text, matcher.start(), matcher.end()));
        }

        return matches;
    }

    public static TweetSpecialTextData extract(String text, int start, int end) {
        String substring = text.substring(start, end);
        return TweetSpecialTextData.from(substring, start, end);
    }
}
