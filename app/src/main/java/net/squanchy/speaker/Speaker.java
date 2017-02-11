package net.squanchy.speaker;

import com.google.auto.value.AutoValue;

import net.squanchy.service.firebase.model.FirebaseSpeaker;

@AutoValue
public abstract class Speaker {

    public static Speaker create(FirebaseSpeaker speaker){
        return new AutoValue_Speaker(speaker.speakerId, speaker.firstName, speaker.lastName);
    }

    public abstract long id();

    public abstract String firstName();

    public abstract String lastName();

    public String getCompleteName(){
        return firstName() + " " + lastName();
    }
}
