package net.squanchy.speaker.domain.view;

import com.google.auto.value.AutoValue;

import net.squanchy.support.lang.Optional;

@AutoValue
public abstract class Speaker {

    public static Speaker create(
            String speakerId,
            long numericSpeakerId,
            String name,
            String bio,
            Optional<String> companyName,
            Optional<String> companyUrl,
            Optional<String> personalUrl,
            Optional<String> photoUrl,
            Optional<String> twitterUsername
    ) {
        return new AutoValue_Speaker.Builder()
                .numericId(numericSpeakerId)
                .id(speakerId)
                .name(name)
                .bio(bio)
                .companyName(companyName)
                .companyUrl(companyUrl)
                .personalUrl(personalUrl)
                .photoUrl(photoUrl)
                .twitterUsername(twitterUsername)
                .build();
    }

    public abstract long numericId();

    public abstract String id();

    public abstract String name();

    public abstract String bio();

    public abstract Optional<String> companyName();

    public abstract Optional<String> companyUrl();

    public abstract Optional<String> personalUrl();

    public abstract Optional<String> photoUrl();

    public abstract Optional<String> twitterUsername();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder numericId(long numericId);

        public abstract Builder id(String id);

        public abstract Builder name(String name);

        public abstract Builder bio(String bio);

        public abstract Builder companyName(Optional<String> companyName);

        public abstract Builder companyUrl(Optional<String> companyUrl);

        public abstract Builder personalUrl(Optional<String> personalUrl);

        public abstract Builder photoUrl(Optional<String> photoUrl);

        public abstract Builder twitterUsername(Optional<String> twitterUsername);

        public abstract Speaker build();
    }
}
