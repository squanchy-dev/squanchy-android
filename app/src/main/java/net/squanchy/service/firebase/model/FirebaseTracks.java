package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebaseTracks {

    public FirebaseTracks() {
    }

    public FirebaseTracks(List<FirebaseTrack> tracks) {
        this.tracks = tracks;
    }

    public List<FirebaseTrack> tracks;
}
