package net.squanchy.speaker.domain.view;

import com.google.auto.value.AutoValue;

import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.support.lang.Ids;
import net.squanchy.support.lang.Optional;

@AutoValue
public abstract class Speaker {

    public static Speaker create(FirebaseSpeaker speaker, long numericSpeakerId) {
        return new AutoValue_Speaker(numericSpeakerId, speaker.id, speaker.name, Optional.fromNullable(speaker.photo_url));
    }

    public abstract long numericId();

    public abstract String id();

    public abstract String name();

    public abstract Optional<String> avatarImageURL();
}
