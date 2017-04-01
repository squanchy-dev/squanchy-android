package net.squanchy.tweets.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TweetSpannableText {

    public static TweetSpannableText from(String text, int begin, int end) {
        return new AutoValue_TweetSpannableText(text, begin, end);
    }

    public abstract String text();

    public abstract int begin();

    public abstract int end();
}
