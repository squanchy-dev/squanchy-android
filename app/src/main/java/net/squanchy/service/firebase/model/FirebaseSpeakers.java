package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebaseSpeakers {

    public FirebaseSpeakers(List<FirebaseSpeaker> speakers) {
        this.speakers = speakers;
    }

    public List<FirebaseSpeaker> speakers;
}
