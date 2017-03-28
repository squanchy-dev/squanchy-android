package net.squanchy.tweets.domain.view;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UrlEntity {

    public static UrlEntity create(String url, int start, int end) {
        return new AutoValue_UrlEntity(url, start, end);
    }

    public abstract String url();

    public abstract int start();

    public abstract int end();
}
