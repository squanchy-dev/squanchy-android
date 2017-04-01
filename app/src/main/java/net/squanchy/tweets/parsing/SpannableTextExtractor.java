package net.squanchy.tweets.parsing;

import net.squanchy.tweets.model.TweetSpannableText;

class SpannableTextExtractor {

    static TweetSpannableText extract(String text, int start, int end) {
        String substring = text.substring(start, end);
        return TweetSpannableText.from(substring, start, end);
    }
}
