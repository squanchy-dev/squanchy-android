package net.squanchy.tweets.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Tweet {

    public abstract long id();

    public abstract String text();

    public abstract User user();

    public abstract String createdAt();

    public abstract List<HashtagEntity> hashtags();

    public abstract List<MentionEntity> mentions();

    public abstract List<UrlEntity> urls();

    public static Builder builder() {
        return new AutoValue_Tweet.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder text(String text);

        public abstract Builder user(User user);

        public abstract Builder createdAt(String createdAt);

        public abstract Builder hashtags(List<HashtagEntity> hashtagEntities);

        public abstract Builder mentions(List<MentionEntity> mentionEntities);

        public abstract Builder urls(List<UrlEntity> urlEntities);

        public abstract Tweet build();
    }
}
