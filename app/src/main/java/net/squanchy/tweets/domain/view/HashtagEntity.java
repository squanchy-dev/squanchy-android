package net.squanchy.tweets.domain.view;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class HashtagEntity {

    public static HashtagEntity create(String text, int start, int end) {
        return new AutoValue_HashtagEntity(text, start, end);
    }

    public abstract String text();

    public abstract int start();

    public abstract int end();
}
