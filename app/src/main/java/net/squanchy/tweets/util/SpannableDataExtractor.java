package net.squanchy.tweets.util;

import net.squanchy.tweets.model.TweetSpecialTextData;

class SpannableDataExtractor {

    static TweetSpecialTextData extract(String text, int start, int end) {
        String substring = text.substring(start, end);
        return TweetSpecialTextData.from(substring, start, end);
    }
}
