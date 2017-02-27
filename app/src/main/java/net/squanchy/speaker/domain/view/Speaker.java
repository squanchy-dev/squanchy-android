package net.squanchy.speaker.domain.view;

import com.google.auto.value.AutoValue;

import net.squanchy.service.firebase.model.FirebaseSpeaker;
import net.squanchy.support.lang.Ids;

@AutoValue
public abstract class Speaker {

    public static Speaker create(FirebaseSpeaker speaker) {
        return new AutoValue_Speaker(Ids.safelyConvertIdToLong(speaker.id), speaker.name,speaker.photo_url);
    }

    public abstract long id();

    public abstract String fullName();

    public abstract String avatarImageURL();

}
