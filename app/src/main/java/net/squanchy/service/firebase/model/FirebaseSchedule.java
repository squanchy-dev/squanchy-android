package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebaseSchedule {

    public FirebaseSchedule() {}

    public FirebaseSchedule(List<FirebaseEvent> events) {
        this.events = events;
    }

    public List<FirebaseEvent> events;
}
