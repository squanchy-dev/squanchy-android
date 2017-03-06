package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebaseEvents {

    public FirebaseEvents() {}

    public FirebaseEvents(List<FirebaseEvent> events) {
        this.events = events;
    }

    public List<FirebaseEvent> events;
}
