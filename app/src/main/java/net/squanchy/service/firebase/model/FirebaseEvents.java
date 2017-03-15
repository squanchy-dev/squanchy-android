package net.squanchy.service.firebase.model;

import java.util.Map;

public class FirebaseEvents {

    public FirebaseEvents() {}

    public FirebaseEvents(Map<String, FirebaseEvent> events) {
        this.events = events;
    }

    public Map<String, FirebaseEvent> events;
}
