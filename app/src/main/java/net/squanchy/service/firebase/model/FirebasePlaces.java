package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebasePlaces {

    public FirebasePlaces() {
    }

    public FirebasePlaces(List<FirebasePlace> places) {
        this.places = places;
    }

    public List<FirebasePlace> places;
}
