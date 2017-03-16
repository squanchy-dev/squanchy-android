package net.squanchy.speaker.domain.view;

import com.google.auto.value.AutoValue;

import net.squanchy.support.lang.Optional;

@AutoValue
public abstract class Speaker {

    public static Speaker create(String speakerId, long numericSpeakerId, String name, String photoUrl) {
        return new AutoValue_Speaker(numericSpeakerId, speakerId, name, Optional.fromNullable(photoUrl));
    }

    public abstract long numericId();

    public abstract String id();

    public abstract String name();

    public abstract Optional<String> avatarImageURL();
}
