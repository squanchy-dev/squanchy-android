package net.squanchy.service.firebase.model;

import java.util.Collections;
import java.util.Map;

public class FirebaseUserData {

    public static FirebaseUserData empty() {
        return new FirebaseUserData(Collections.emptyMap(), Collections.emptyMap());
    }

    public FirebaseUserData() {
    }

    public FirebaseUserData(Map<String, Boolean> favorites, Map<String, Long> achievements) {
        this.favorites = favorites;
        this.achievements = achievements;
    }

    public Map<String, Boolean> favorites;

    public Map<String, Long> achievements;
}
