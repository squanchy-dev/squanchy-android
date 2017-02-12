package net.squanchy.speaker;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import net.squanchy.service.firebase.model.FirebaseSpeaker;

@AutoValue
public abstract class Speaker implements Comparable<Speaker>{

    public static Speaker create(FirebaseSpeaker speaker){
        return new AutoValue_Speaker(speaker.speakerId, speaker.firstName,
                speaker.lastName, speaker.avatarImageURL);
    }

    public abstract long id();

    public abstract String firstName();

    public abstract String lastName();

    public abstract String avatarImageURL();

    public String getCompleteName(){
        return firstName() + " " + lastName();
    }

    @Override
    public int compareTo(@NonNull Speaker speaker) {
        return getCompleteName().compareToIgnoreCase(speaker.getCompleteName());
    }
}
