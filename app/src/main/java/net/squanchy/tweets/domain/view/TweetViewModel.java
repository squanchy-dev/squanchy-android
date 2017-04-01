package net.squanchy.tweets.domain.view;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class TweetViewModel {

    public abstract long id();

    public abstract String text();

    public abstract CharSequence spannedText();

    public abstract User user();

    public abstract String createdAt();

    public abstract List<String> mediaUrls();

    public static Builder builder() {
        return new AutoValue_TweetViewModel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder text(String text);

        public abstract Builder spannedText(CharSequence spannedText);

        public abstract Builder user(User user);

        public abstract Builder createdAt(String createdAt);

        public abstract Builder mediaUrls(List<String> mediaUrls);

        public abstract TweetViewModel build();
    }
}
