package net.squanchy.tweets.domain.view;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MentionEntity {

    public static MentionEntity create(String displayName, int start, int end) {
        return new AutoValue_MentionEntity(displayName, start, end);
    }

    public abstract String displayName();

    public abstract int start();

    public abstract int end();
}
