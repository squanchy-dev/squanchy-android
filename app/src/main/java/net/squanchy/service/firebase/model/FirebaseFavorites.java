package net.squanchy.service.firebase.model;

import java.util.Collections;
import java.util.Map;

public class FirebaseFavorites {

    public FirebaseFavorites() {
    }

    public FirebaseFavorites(Map<String, Boolean> favorites) {
        this.favorites = favorites;
    }

    public Map<String, Boolean> favorites;

    public static FirebaseFavorites empty() {
        return new FirebaseFavorites(Collections.emptyMap());
    }
}
