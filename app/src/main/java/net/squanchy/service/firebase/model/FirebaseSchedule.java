package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebaseSchedule {

    public FirebaseSchedule() {}

    public FirebaseSchedule(List<FirebaseEvent> sessions) {
        this.sessions = sessions;
    }

    public List<FirebaseEvent> sessions;
}
