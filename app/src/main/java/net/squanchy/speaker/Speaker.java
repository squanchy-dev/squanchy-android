package net.squanchy.speaker;

import com.google.auto.value.AutoValue;

import net.squanchy.service.firebase.model.FirebaseSpeaker;

@AutoValue
public abstract class Speaker {

    public static Speaker create(FirebaseSpeaker speaker) {
        return new AutoValue_Speaker(speaker.speakerId, speaker.firstName,
                speaker.lastName, speaker.avatarImageURL);
    }

    public abstract long id();

    public abstract String firstName();

    public abstract String lastName();

    public abstract String avatarImageURL();

    public String fullName() {
        return firstName() + " " + lastName();
    }
}
