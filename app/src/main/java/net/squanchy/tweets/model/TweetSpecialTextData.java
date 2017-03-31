package net.squanchy.tweets.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TweetSpecialTextData {

    public abstract String text();

    public abstract int begin();

    public abstract int end();

    public static TweetSpecialTextData from(String text, int begin, int end) {
        return new AutoValue_TweetSpecialTextData(text, begin, end);
    }
}
